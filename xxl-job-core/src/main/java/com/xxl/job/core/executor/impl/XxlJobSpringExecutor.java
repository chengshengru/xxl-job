package com.xxl.job.core.executor.impl;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * xxl-job executor (for spring)
 *
 * @author xuxueli 2018-11-01 09:24:52
 */
public class XxlJobSpringExecutor extends XxlJobExecutor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobSpringExecutor.class);

    // ---------------------- field ----------------------

    /**
     * excluded package, like "org.springframework"、"org.aaa,org.bbb"
     */
    private String excludedPackage = "org.springframework.,spring.";

    public void setExcludedPackage(String excludedPackage) {
        this.excludedPackage = excludedPackage;
    }
    
    @Override
    public void afterSingletonsInstantiated() {
        // inject spring managed beans
        if (applicationContext != null) {
            try {
                setTriggerCallbackThread(applicationContext.getBean(TriggerCallbackThread.class));
                setJobLogFileCleanThread(applicationContext.getBean(JobLogFileCleanThread.class));
                setExecutorRegistryThread(applicationContext.getBean(ExecutorRegistryThread.class));
            } catch (Exception e) {
                logger.warn("xxl-job: Spring managed beans injection failed, fallback to manual creation.", e);
                // fallback to manual creation if beans not found
                if (getTriggerCallbackThread() == null) {
                    setTriggerCallbackThread(new TriggerCallbackThread());
                }
                if (getJobLogFileCleanThread() == null) {
                    setJobLogFileCleanThread(new JobLogFileCleanThread());
                }
                if (getExecutorRegistryThread() == null) {
                    setExecutorRegistryThread(new ExecutorRegistryThread());
                }
            }
        }
        
        // scan JobHandler method
        scanJobHandlerMethod(applicationContext);

        // refresh GlueFactory
        GlueFactory.refreshInstance(1);

        // super start
        try {
            super.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // getter methods for fallback creation
    public TriggerCallbackThread getTriggerCallbackThread() {
        // use reflection to access private field
        try {
            java.lang.reflect.Field field = XxlJobExecutor.class.getDeclaredField("triggerCallbackThread");
            field.setAccessible(true);
            return (TriggerCallbackThread) field.get(this);
        } catch (Exception e) {
            return null;
        }
    }
    
    public JobLogFileCleanThread getJobLogFileCleanThread() {
        // use reflection to access private field
        try {
            java.lang.reflect.Field field = XxlJobExecutor.class.getDeclaredField("jobLogFileCleanThread");
            field.setAccessible(true);
            return (JobLogFileCleanThread) field.get(this);
        } catch (Exception e) {
            return null;
        }
    }
    
    public ExecutorRegistryThread getExecutorRegistryThread() {
        // use reflection to access private field
        try {
            java.lang.reflect.Field field = XxlJobExecutor.class.getDeclaredField("executorRegistryThread");
            field.setAccessible(true);
            return (ExecutorRegistryThread) field.get(this);
        } catch (Exception e) {
            return null;
        }
    }


    // ---------------------- start / stop ----------------------

    /**
     * stop
      */
    @Override
    public void destroy() {
        super.destroy();
    }


    /**
     * init job handler from method
     *
     * @param applicationContext applicationContext
     */
    private void scanJobHandlerMethod(ApplicationContext applicationContext) {
        // valid
        if (applicationContext == null) {
            return;
        }

        // 1、build excluded-package list
        List<String> excludedPackageList = new ArrayList<>();
        if (excludedPackage != null) {
            for (String excludedPackage : excludedPackage.split(",")) {
                if (!excludedPackage.trim().isEmpty()){
                    excludedPackageList.add(excludedPackage.trim());
                }
            }
        }

        // 2、scan bean form jobhandler
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class, false, false);  // allowEagerInit=false, avoid early initialization
        for (String beanName : beanNames) {

            /**
             * 2.1、skip by BeanDefinition:
             *      - skip excluded-package bean
             *      - skip lazy-init bean
              */
            if (applicationContext instanceof BeanDefinitionRegistry beanDefinitionRegistry) {
                // get BeanDefinition
                if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
                    continue;
                }
                BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);

                // skip excluded-package bean
                String beanClassName = beanDefinition.getBeanClassName();
                if (isExcluded(excludedPackageList, beanClassName)) {
                    logger.debug(">>>>>>>>>>> xxl-job bean-definition scan, skip excluded-package beanName:{}, beanClassName:{}", beanName, beanClassName);
                    continue;
                }

                // skip lazy-init bean
                if (beanDefinition.isLazyInit()) {
                    logger.debug(">>>>>>>>>>> xxl-job bean-definition scan, skip lazy-init beanName:{}", beanName);
                    continue;
                }
            }

            /**
             * 2.2、skip by BeanDefinition Class
             *      - skip beanClass is null
             *      - skip method annotation(@XxlJob) is null
             */
            Class<?> beanClass = applicationContext.getType(beanName, false);
            if (beanClass == null) {
                logger.debug(">>>>>>>>>>> xxl-job bean-definition scan, skip beanClass-null beanName:{}", beanName);
                continue;
            }
            // filter method
            Map<Method, XxlJob> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(beanClass,
                        new MethodIntrospector.MetadataLookup<XxlJob>() {
                            @Override
                            public XxlJob inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class);
                            }
                        });
            } catch (Throwable ex) {
                logger.error(">>>>>>>>>>> xxl-job method-jobhandler resolve error for bean[" + beanName + "].", ex);
            }
            if (annotatedMethods==null || annotatedMethods.isEmpty()) {
                continue;
            }

            // 2.3、scan + registry Jobhandler
            Object jobBean = applicationContext.getBean(beanName);
            for (Map.Entry<Method, XxlJob> jobMethodEntry : annotatedMethods.entrySet()) {
                Method jobMethod = jobMethodEntry.getKey();
                XxlJob xxlJob = jobMethodEntry.getValue();
                // regist
                registryJobHandler(xxlJob, jobBean, jobMethod);
            }

        }
    }

    /**
     * check bean if excluded
     *
     * @param excludedPackageList   excludedPackageList
     * @param beanClassName         beanClassName
     * @return  true if excluded
     */
    private boolean isExcluded(List<String> excludedPackageList, String beanClassName) {
        // excludedPackageList is empty, no excluded
        if (excludedPackageList == null || excludedPackageList.isEmpty()) {
            return false;
        }

        // beanClassName is null, no excluded
        if (beanClassName == null) {
            return false;
        }

        // excludedPackageList match, excluded (not scan)
        for (String excludedPackage : excludedPackageList) {
            if (beanClassName.startsWith(excludedPackage)) {
                return true;
            }
        }
        return false;
    }


    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XxlJobSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}

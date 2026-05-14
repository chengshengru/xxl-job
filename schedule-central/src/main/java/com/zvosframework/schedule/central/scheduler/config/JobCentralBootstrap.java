package com.zvosframework.schedule.central.scheduler.config;

import com.zvosframework.schedule.central.mapper.*;
import com.zvosframework.schedule.central.scheduler.alarm.JobAlarmer;
import com.zvosframework.schedule.central.scheduler.complete.JobCompleter;
import com.zvosframework.schedule.central.scheduler.thread.*;
import com.zvosframework.schedule.central.scheduler.trigger.JobTrigger;
import com.zvosframework.schedule.core.constant.Const;
import com.zvosframework.schedule.core.openapi.ExecutorBiz;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.http.HttpTool;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */

@Component
public class JobCentralBootstrap implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(JobCentralBootstrap.class);

    // ---------------------- instance ----------------------

    private static JobCentralBootstrap centralConfig = null;
    public static JobCentralBootstrap getInstance() {
        return centralConfig;
    }


    // ---------------------- start / stop ----------------------

    @Override
    public void afterPropertiesSet() throws Exception {
        // init instance
        centralConfig = this;

        // start
        doStart();
    }

    @Override
    public void destroy() throws Exception {
        // stop
        doStop();
    }

    // job module
    private JobTriggerPoolHelper jobTriggerPoolHelper;
    private JobRegistryHelper jobRegistryHelper;
    private JobFailAlarmMonitorHelper jobFailAlarmMonitorHelper;
    private JobCompleteHelper jobCompleteHelper;
    private JobLogReportHelper jobLogReportHelper;
    private JobScheduleHelper jobScheduleHelper;

    public JobTriggerPoolHelper getJobTriggerPoolHelper() {
        return jobTriggerPoolHelper;
    }
    public JobRegistryHelper getJobRegistryHelper() {
        return jobRegistryHelper;
    }
    public JobCompleteHelper getJobCompleteHelper() {
        return jobCompleteHelper;
    }

    /**
     * do start
     */
    private void doStart() throws Exception {
        // trigger-pool start
        jobTriggerPoolHelper = new JobTriggerPoolHelper();
        jobTriggerPoolHelper.start();

        // registry monitor start
        jobRegistryHelper = new JobRegistryHelper();
        jobRegistryHelper.start();

        // fail-alarm monitor start
        jobFailAlarmMonitorHelper = new JobFailAlarmMonitorHelper();
        jobFailAlarmMonitorHelper.start();

        // job complate start  ( depend on JobTriggerPoolHelper ) for callback and result-lost
        jobCompleteHelper = new JobCompleteHelper();
        jobCompleteHelper.start();

        // log-report start
        jobLogReportHelper = new JobLogReportHelper();
        jobLogReportHelper.start();

        // job-schedule start  ( depend on JobTriggerPoolHelper )
        jobScheduleHelper = new JobScheduleHelper();
        jobScheduleHelper.start();

        logger.info(">>>>>>>>> schedule-job central start success.");
    }

    /**
     * do stop
     */
    private void doStop(){
        // job-schedule stop
        jobScheduleHelper.stop();

        // log-report stop
        jobLogReportHelper.stop();

        // job complate stop
        jobCompleteHelper.stop();

        // fail-alarm monitor stop
        jobFailAlarmMonitorHelper.stop();

        // registry monitor stop
        jobRegistryHelper.stop();

        // trigger-pool stop
        jobTriggerPoolHelper.stop();

        logger.info(">>>>>>>>> schedule-job central stopped.");
    }


    // ---------------------- executor-client ----------------------

    private static ConcurrentMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();
    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
        // valid
        if (StringTool.isBlank(address)) {
            return null;
        }

        // load-cache
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        // set-cache
        executorBiz = HttpTool.createClient()
                .url(address)
                .timeout(JobCentralBootstrap.getInstance().getTimeout() * 1000)
                .header(Const.XXL_JOB_ACCESS_TOKEN, JobCentralBootstrap.getInstance().getAccessToken())
                .proxy(ExecutorBiz.class);
        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }


    // ---------------------- field ----------------------

    // conf
    @Value("${schedule.i18n}")
    private String i18n;

    @Value("${schedule.accessToken}")
    private String accessToken;

    @Value("${schedule.timeout}")
    private int timeout;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @Value("${schedule.triggerpool.fast.max}")
    private int triggerPoolFastMax;

    @Value("${schedule.triggerpool.slow.max}")
    private int triggerPoolSlowMax;

    @Value("${schedule.schedule.batchsize}")
    private int scheduleBatchSize;

    @Value("${schedule.logretentiondays}")
    private int logretentiondays;

    // service, mapper
    @Resource
    private JobLogMapper xxlJobLogMapper;
    @Resource
    private JobInfoMapper xxlJobInfoMapper;
    @Resource
    private JobRegistryMapper xxlJobRegistryMapper;
    @Resource
    private JobGroupMapper xxlJobGroupMapper;
    @Resource
    private JobLogReportMapper xxlJobLogReportMapper;
    @Resource
    private JobLockMapper xxlJobLockMapper;
    @Resource
    private JavaMailSender mailSender;
    /*@Resource
    private DataSource dataSource;*/
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private JobAlarmer jobAlarmer;
    @Resource
    private JobTrigger jobTrigger;
    @Resource
    private JobCompleter jobCompleter;


    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public int getTriggerPoolFastMax() {
        if (triggerPoolFastMax < 200) {
            return 200;
        }
        return triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        if (triggerPoolSlowMax < 100) {
            return 100;
        }
        return triggerPoolSlowMax;
    }

    public int getScheduleBatchSize() {
        if (!(scheduleBatchSize >=50 && scheduleBatchSize <= 500)) {
            return 100;
        }
        return scheduleBatchSize;
    }

    public int getLogretentiondays() {
        if (logretentiondays < 3) {
            return -1;  // Limit greater than or equal to 3, otherwise close
        }
        return logretentiondays;
    }

    public JobLogMapper getJobLogMapper() {
        return xxlJobLogMapper;
    }

    public JobInfoMapper getJobInfoMapper() {
        return xxlJobInfoMapper;
    }

    public JobRegistryMapper getJobRegistryMapper() {
        return xxlJobRegistryMapper;
    }

    public JobGroupMapper getJobGroupMapper() {
        return xxlJobGroupMapper;
    }

    public JobLogReportMapper getJobLogReportMapper() {
        return xxlJobLogReportMapper;
    }

    public JobLockMapper getJobLockMapper() {
        return xxlJobLockMapper;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    /*public DataSource getDataSource() {
        return dataSource;
    }*/

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public JobAlarmer getJobAlarmer() {
        return jobAlarmer;
    }

    public JobTrigger getJobTrigger() {
        return jobTrigger;
    }

    public JobCompleter getJobCompleter() {
        return jobCompleter;
    }

}

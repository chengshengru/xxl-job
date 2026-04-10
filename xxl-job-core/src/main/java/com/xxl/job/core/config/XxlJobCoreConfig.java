package com.xxl.job.core.config;

import com.xxl.job.core.thread.ExecutorRegistryThread;
import com.xxl.job.core.thread.JobLogFileCleanThread;
import com.xxl.job.core.thread.TriggerCallbackThread;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XxlJob core configuration
 * 
 * @author xuxueli
 */
@Configuration
public class XxlJobCoreConfig {

    @Bean
    public TriggerCallbackThread triggerCallbackThread() {
        return new TriggerCallbackThread();
    }

    @Bean
    public JobLogFileCleanThread jobLogFileCleanThread() {
        return new JobLogFileCleanThread();
    }

    @Bean
    public ExecutorRegistryThread executorRegistryThread() {
        return new ExecutorRegistryThread();
    }

}

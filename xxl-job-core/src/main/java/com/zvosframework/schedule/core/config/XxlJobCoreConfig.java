package com.zvosframework.schedule.core.config;

import com.zvosframework.schedule.core.thread.ExecutorRegistryThread;
import com.zvosframework.schedule.core.thread.JobLogFileCleanThread;
import com.zvosframework.schedule.core.thread.TriggerCallbackThread;
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

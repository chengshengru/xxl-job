package com.zvosframework.schedule.executor.sample.frameless.config;

import com.zvosframework.schedule.core.handler.annotation.Schedule;
import com.zvosframework.schedule.core.handler.annotation.ScheduleSimpleExecutor;
import com.xxl.tool.core.PropTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author xuxueli 2018-10-31 19:05:43
 */
public class FrameLessJobConfig {
    private static final Logger logger = LoggerFactory.getLogger(FrameLessJobConfig.class);


    private static final FrameLessJobConfig instance = new FrameLessJobConfig();
    public static FrameLessJobConfig getInstance() {
        return instance;
    }


    private JobSimpleExecutor xxlJobExecutor = null;

    /**
     * init
     */
    public void initJobExecutor() {

        // load executor prop
        Properties xxlJobProp = PropTool.loadProp("schedule-executor.properties");

        // init executor
        xxlJobExecutor = new JobSimpleExecutor();
        xxlJobExecutor.setAdminAddresses(xxlJobProp.getProperty("schedule.admin.addresses"));
        xxlJobExecutor.setAccessToken(xxlJobProp.getProperty("schedule.admin.accessToken"));
        xxlJobExecutor.setTimeout(Integer.valueOf(xxlJobProp.getProperty("schedule.admin.timeout")));
        xxlJobExecutor.setEnabled(Boolean.valueOf(xxlJobProp.getProperty("schedule.executor.enabled")));
        xxlJobExecutor.setAppname(xxlJobProp.getProperty("schedule.executor.appname"));
        xxlJobExecutor.setAddress(xxlJobProp.getProperty("schedule.executor.address"));
        xxlJobExecutor.setIp(xxlJobProp.getProperty("schedule.executor.ip"));
        xxlJobExecutor.setPort(Integer.parseInt(xxlJobProp.getProperty("schedule.executor.port")));
        xxlJobExecutor.setLogPath(xxlJobProp.getProperty("schedule.executor.logpath"));
        xxlJobExecutor.setLogRetentionDays(Integer.parseInt(xxlJobProp.getProperty("schedule.executor.logretentiondays")));

        // registry job bean
        xxlJobExecutor.setJobBeanList(Arrays.asList(new SampleJob()));

        // start executor
        try {
            xxlJobExecutor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * destroy
     */
    public void destroyJobExecutor() {
        if (xxlJobExecutor != null) {
            xxlJobExecutor.destroy();
        }
    }

}

package com.zvosframework.schedule.executor.sample.frameless;

import com.zvosframework.schedule.core.handler.annotation.ScheduleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author xuxueli 2018-10-31 19:05:43
 */
public class JobFramelessApplication {
    private static final Logger logger = LoggerFactory.getLogger(JobFramelessApplication.class);

    public static void main(String[] args) {

        try {
            // start
            FrameLessJobConfig.getInstance().initJobExecutor();

            // Blocks until interrupted
            while (true) {
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // destroy
            FrameLessJobConfig.getInstance().destroyJobExecutor();
        }

    }

}

package com.zvosframework.schedule.central.scheduler.misfire.strategy;

import com.zvosframework.schedule.central.scheduler.misfire.MisfireHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MisfireDoNothing extends MisfireHandler {
    private static final Logger logger = LoggerFactory.getLogger(MisfireDoNothing.class);

    @Override
    public void handle(int jobId) {
        logger.warn(">>>>>>>>>>> xxl-job, schedule MisfireDoNothing: jobId = " + jobId );
    }

}

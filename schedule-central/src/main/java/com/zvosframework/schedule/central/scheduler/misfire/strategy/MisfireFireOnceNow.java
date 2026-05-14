package com.zvosframework.schedule.central.scheduler.misfire.strategy;

import com.zvosframework.schedule.core.handler.annotation.ScheduleAdminBootstrap;
import com.zvosframework.schedule.central.scheduler.misfire.MisfireHandler;
import com.zvosframework.schedule.central.scheduler.trigger.TriggerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MisfireFireOnceNow extends MisfireHandler {
    protected static Logger logger = LoggerFactory.getLogger(MisfireFireOnceNow.class);

    @Override
    public void handle(int jobId) {
        // FIRE_ONCE_NOW 》 trigger
        JobCentralBootstrap.getInstance().getJobTriggerPoolHelper().trigger(jobId, TriggerTypeEnum.MISFIRE, -1, null, null, null);
        logger.warn(">>>>>>>>>>> xxl-job, schedule MisfireFireOnceNow: jobId = " + jobId );
    }

}

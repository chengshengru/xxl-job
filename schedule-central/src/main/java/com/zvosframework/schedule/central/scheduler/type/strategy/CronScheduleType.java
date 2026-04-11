package com.zvosframework.schedule.central.scheduler.type.strategy;

import com.zvosframework.schedule.core.handler.annotation.ScheduleInfo;
import com.zvosframework.schedule.central.scheduler.cron.CronExpression;
import com.zvosframework.schedule.central.scheduler.type.ScheduleType;

import java.util.Date;

public class CronScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(JobInfo jobInfo, Date fromTime) throws Exception {
        // generate next trigger time, with cron
        return new CronExpression(jobInfo.getScheduleConf()).getNextValidTimeAfter(fromTime);
    }

}

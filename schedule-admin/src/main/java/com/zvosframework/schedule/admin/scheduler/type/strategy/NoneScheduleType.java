package com.zvosframework.schedule.admin.scheduler.type.strategy;

import com.zvosframework.schedule.core.handler.annotation.ScheduleInfo;
import com.zvosframework.schedule.admin.scheduler.type.ScheduleType;

import java.util.Date;

public class NoneScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(JobInfo jobInfo, Date fromTime) throws Exception {
        // generate none trigger-time
        return null;
    }

}

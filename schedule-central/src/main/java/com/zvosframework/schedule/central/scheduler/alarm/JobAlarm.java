package com.zvosframework.schedule.central.scheduler.alarm;

import com.zvosframework.schedule.core.handler.annotation.ScheduleInfo;
import com.zvosframework.schedule.core.handler.annotation.ScheduleLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(JobInfo info, JobLog jobLog);

}

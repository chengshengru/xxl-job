package com.zvosframework.schedule.admin.scheduler.type;

import com.zvosframework.schedule.core.handler.annotation.ScheduleInfo;

import java.util.Date;

/**
 * Schedule Type
 *
 * @author xuxueli 2020-10-29
 */
public abstract class ScheduleType {

    /**
     * generate next trigger time
     *
     * @param jobInfo       job info
     * @param fromTime      from time
     */
    public abstract Date generateNextTriggerTime(XxlJobInfo jobInfo, Date fromTime) throws Exception;

}

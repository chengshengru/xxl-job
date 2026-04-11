package com.zvosframework.schedule.admin.scheduler.type.strategy;

import com.zvosframework.schedule.admin.model.JobInfo;
import com.zvosframework.schedule.admin.scheduler.type.ScheduleType;
import com.xxl.tool.json.GsonTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeRangeScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(JobInfo jobInfo, Date fromTime) throws Exception {
        String scheduleConf = jobInfo.getScheduleConf();
        TimeRangeConfig config = GsonTool.fromJson(scheduleConf, TimeRangeConfig.class);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse(config.getStartTime());
        Date endTime = sdf.parse(config.getEndTime());
        
        if (fromTime.before(startTime)) {
            return startTime;
        }
        if (fromTime.after(endTime)) {
            return null;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromTime);
        
        switch (config.getIntervalUnit()) {
            case "second":
                calendar.add(Calendar.SECOND, config.getInterval());
                break;
            case "minute":
                calendar.add(Calendar.MINUTE, config.getInterval());
                break;
            case "hour":
                calendar.add(Calendar.HOUR_OF_DAY, config.getInterval());
                break;
            default:
                throw new IllegalArgumentException("Invalid interval unit: " + config.getIntervalUnit());
        }
        
        Date nextTime = calendar.getTime();
        
        if (nextTime.after(endTime)) {
            return null;
        }
        
        return nextTime;
    }
}

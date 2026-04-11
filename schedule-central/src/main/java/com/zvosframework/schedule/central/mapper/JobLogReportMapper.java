package com.zvosframework.schedule.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleLogReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface JobLogReportMapper extends BaseMapper<JobLogReport> {

	public int saveOrUpdate(JobLogReport xxlJobLogReport);

	public List<JobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
												@Param("triggerDayTo") Date triggerDayTo);

	public JobLogReport queryLogReportTotal();

}

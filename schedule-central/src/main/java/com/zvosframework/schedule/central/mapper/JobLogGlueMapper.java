package com.zvosframework.schedule.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobLogGlueMapper extends BaseMapper<JobLogGlue> {
	
	public int save(JobLogGlue xxlJobLogGlue);
	
	public List<JobLogGlue> findByJobId(@Param("jobId") int jobId);

	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

	public int deleteByJobId(@Param("jobId") int jobId);
	
}

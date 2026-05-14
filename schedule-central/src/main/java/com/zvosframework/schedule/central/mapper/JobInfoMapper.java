package com.zvosframework.schedule.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobInfoMapper extends BaseMapper<JobInfo> {

	public List<JobInfo> pageList(@Param("offset") int offset,
									 @Param("pagesize") int pagesize,
									 @Param("jobGroup") int jobGroup,
									 @Param("triggerStatus") int triggerStatus,
									 @Param("jobDesc") String jobDesc,
									 @Param("executorHandler") String executorHandler,
									 @Param("author") String author);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author);
	
	public int save(JobInfo info);

	public JobInfo loadById(@Param("id") int id);
	
	public int update(JobInfo xxlJobInfo);
	
	public int delete(@Param("id") long id);

	public List<JobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();

	public List<JobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );

	public int scheduleUpdate(JobInfo xxlJobInfo);

	public int scheduleBatchUpdate(@Param("list") List<JobInfo> jobInfoList);

}

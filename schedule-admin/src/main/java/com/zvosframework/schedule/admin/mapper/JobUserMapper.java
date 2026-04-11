package com.zvosframework.schedule.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface JobUserMapper extends BaseMapper<JobUser> {

	public List<JobUser> pageList(@Param("offset") int offset,
                                     @Param("pagesize") int pagesize,
                                     @Param("username") String username,
									 @Param("role") int role);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("username") String username,
							 @Param("role") int role);

	public JobUser loadByUserName(@Param("username") String username);

	public JobUser loadById(@Param("id") int id);

	public int save(JobUser xxlJobUser);

	public int update(JobUser xxlJobUser);
	
	public int delete(@Param("id") int id);

	public int updateToken(@Param("id") int id, @Param("token") String token);

}

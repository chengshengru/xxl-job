package com.zvosframework.schedule.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobGroupMapper extends BaseMapper<JobGroup> {

    public List<JobGroup> findAll();

    public List<JobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(JobGroup xxlJobGroup);

    public int update(JobGroup xxlJobGroup);

    public int remove(@Param("id") int id);

    public JobGroup load(@Param("id") int id);

    public List<JobGroup> pageList(@Param("offset") int offset,
                                      @Param("pagesize") int pagesize,
                                      @Param("appname") String appname,
                                      @Param("title") String title);

    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("appname") String appname,
                             @Param("title") String title);

}

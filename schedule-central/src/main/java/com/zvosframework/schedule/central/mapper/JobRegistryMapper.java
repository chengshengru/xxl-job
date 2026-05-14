package com.zvosframework.schedule.central.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface JobRegistryMapper extends BaseMapper<JobRegistry> {

    public List<Integer> findDead(@Param("timeout") int timeout,
                                  @Param("nowTime") Date nowTime);

    public int removeDead(@Param("ids") List<Integer> ids);

    public List<JobRegistry> findAll(@Param("timeout") int timeout,
                                        @Param("nowTime") Date nowTime);

    public int registrySaveOrUpdate(@Param("registryGroup") String registryGroup,
                            @Param("registryKey") String registryKey,
                            @Param("registryValue") String registryValue,
                            @Param("updateTime") Date updateTime);

    public int registryDelete(@Param("registryGroup") String registryGroup,
                          @Param("registryKey") String registryKey,
                          @Param("registryValue") String registryValue);

    public int removeByRegistryGroupAndKey(@Param("registryGroup") String registryGroup,
                                           @Param("registryKey") String registryKey);

}

package com.zvosframework.schedule.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * job lock
 *
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface JobLockMapper {

    /**
     * get schedule lock
     */
    String scheduleLock();

}
package com.xxl.job.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxl.tool.core.StringTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@TableName("xxl_job_group")
public class XxlJobGroup {

    @TableId(type = IdType.AUTO)
    private int id;
    
    @TableField("appname")
    private String appname;
    
    @TableField("title")
    private String title;
    
    @TableField("address_type")
    private int addressType;
    
    @TableField("address_list")
    private String addressList;
    
    @TableField("update_time")
    private Date updateTime;

    private List<String> registryList;
    public List<String> getRegistryList() {
        if (StringTool.isNotBlank(addressList)) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

}

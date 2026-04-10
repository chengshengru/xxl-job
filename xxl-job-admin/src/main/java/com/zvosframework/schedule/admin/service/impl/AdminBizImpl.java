package com.zvosframework.schedule.admin.service.impl;

import com.zvosframework.schedule.admin.scheduler.config.XxlJobAdminBootstrap;
import com.zvosframework.schedule.core.openapi.AdminBiz;
import com.zvosframework.schedule.core.openapi.model.CallbackRequest;
import com.zvosframework.schedule.core.openapi.model.RegistryRequest;
import com.xxl.tool.response.Response;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {

    @Override
    public Response<String> callback(List<CallbackRequest> callbackRequestList) {
        return XxlJobAdminBootstrap.getInstance().getJobCompleteHelper().callback(callbackRequestList);
    }

    @Override
    public Response<String> registry(RegistryRequest registryRequest) {
        return XxlJobAdminBootstrap.getInstance().getJobRegistryHelper().registry(registryRequest);
    }

    @Override
    public Response<String> registryRemove(RegistryRequest registryRequest) {
        return XxlJobAdminBootstrap.getInstance().getJobRegistryHelper().registryRemove(registryRequest);
    }

}

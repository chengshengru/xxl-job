//package com.zvosframework.schedule.core.openapi.client;
//
//import com.zvosframework.schedule.core.openapi.AdminBiz;
//import com.zvosframework.schedule.core.openapi.model.HandleCallbackRequest;
//import com.zvosframework.schedule.core.openapi.model.RegistryRequest;
//import com.zvosframework.schedule.core.handler.annotation.ScheduleRemotingUtil;
//import com.xxl.tool.response.Response;
//
//import java.util.List;
//
///**
// * admin api test
// *
// * @author xuxueli 2017-07-28 22:14:52
// */
//public class AdminBizClient implements AdminBiz {
//
//    public AdminBizClient() {
//    }
//    public AdminBizClient(String addressUrl, String accessToken, int timeout) {
//        this.addressUrl = addressUrl;
//        this.accessToken = accessToken;
//        this.timeout = timeout;
//
//        // valid
//        if (!this.addressUrl.endsWith("/")) {
//            this.addressUrl = this.addressUrl + "/";
//        }
//        if (!(this.timeout >=1 && this.timeout <= 10)) {
//            this.timeout = 3;
//        }
//    }
//
//    private String addressUrl ;
//    private String accessToken;
//    private int timeout;
//
//
//    @Override
//    public Response<String> callback(List<HandleCallbackRequest> handleCallbackRequestList) {
//        return XxlJobRemotingUtil.postBody(addressUrl+"api/callback", accessToken, timeout, handleCallbackRequestList, String.class);
//    }
//
//    @Override
//    public Response<String> registry(RegistryRequest registryRequest) {
//        return XxlJobRemotingUtil.postBody(addressUrl + "api/registry", accessToken, timeout, registryRequest, String.class);
//    }
//
//    @Override
//    public Response<String> registryRemove(RegistryRequest registryRequest) {
//        return XxlJobRemotingUtil.postBody(addressUrl + "api/registryRemove", accessToken, timeout, registryRequest, String.class);
//    }
//
//}

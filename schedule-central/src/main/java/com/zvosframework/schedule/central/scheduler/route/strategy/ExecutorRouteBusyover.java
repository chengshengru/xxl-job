package com.zvosframework.schedule.central.scheduler.route.strategy;

import com.zvosframework.schedule.core.handler.annotation.ScheduleAdminBootstrap;
import com.zvosframework.schedule.central.scheduler.route.ExecutorRouter;
import com.zvosframework.schedule.central.util.I18nUtil;
import com.zvosframework.schedule.core.openapi.ExecutorBiz;
import com.zvosframework.schedule.core.openapi.model.IdleBeatRequest;
import com.zvosframework.schedule.core.openapi.model.TriggerRequest;
import com.xxl.tool.response.Response;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public Response<String> route(TriggerRequest triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            Response<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = JobCentralBootstrap.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(new IdleBeatRequest(triggerParam.getJobId()));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = Response.ofFail( ""+e );
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_idleBeat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.isSuccess()) {
                idleBeatResult.setMsg(idleBeatResultSB.toString());
                idleBeatResult.setData(address);
                return idleBeatResult;
            }
        }

        return Response.ofFail( idleBeatResultSB.toString());
    }

}

package com.zvosframework.schedule.central.controller.biz;

import com.zvosframework.schedule.central.constant.Consts;
import com.zvosframework.schedule.core.handler.annotation.ScheduleGroup;
import com.zvosframework.schedule.core.handler.annotation.ScheduleRegistry;
import com.zvosframework.schedule.central.util.I18nUtil;
import com.zvosframework.schedule.core.handler.annotation.ScheduleGroupMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleInfoMapper;
import com.zvosframework.schedule.core.handler.annotation.ScheduleRegistryMapper;
import com.zvosframework.schedule.core.constant.Const;
import com.zvosframework.schedule.core.constant.RegistType;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.http.HttpTool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * job group controller
 * @author xuxueli 2016-10-02 20:52:56
 */
@Controller
@RequestMapping("/jobgroup")
public class JobGroupController {

	@Resource
	public JobInfoMapper xxlJobInfoMapper;
	@Resource
	public JobGroupMapper xxlJobGroupMapper;
	@Resource
	private JobRegistryMapper xxlJobRegistryMapper;

	@RequestMapping
	@XxlSso(role = Consts.ADMIN_ROLE)
	public String index(Model model) {
		return "biz/group.list";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	@XxlSso(role = Consts.ADMIN_ROLE)
	public Response<PageModel<JobGroup>> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
													 @RequestParam(required = false, defaultValue = "10") int pagesize,
													 String appname,
													 String title) {

		// page query
		List<JobGroup> list = xxlJobGroupMapper.pageList(offset, pagesize, appname, title);
		int list_count = xxlJobGroupMapper.pageListCount(offset, pagesize, appname, title);

		// package result
		PageModel<JobGroup> pageModel = new PageModel<>();
		pageModel.setData(list);
		pageModel.setTotal(list_count);

		return Response.ofSuccess(pageModel);
	}

	@RequestMapping("/insert")
	@ResponseBody
	@XxlSso(role = Consts.ADMIN_ROLE)
	public Response<String> insert(JobGroup xxlJobGroup){

		// valid
		if (StringTool.isBlank(xxlJobGroup.getAppname())) {
			return Response.ofFail((I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (xxlJobGroup.getAppname().length()<4 || xxlJobGroup.getAppname().length()>64) {
			return Response.ofFail( I18nUtil.getString("jobgroup_field_appname_length") );
		}
		if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
			return Response.ofFail( "AppName"+I18nUtil.getString("system_invalid") );
		}
		if (StringTool.isBlank(xxlJobGroup.getTitle())) {
			return Response.ofFail((I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
			return Response.ofFail(I18nUtil.getString("jobgroup_field_title")+I18nUtil.getString("system_invalid") );
		}
		if (xxlJobGroup.getAddressType()!=0) {
			if (StringTool.isBlank(xxlJobGroup.getAddressList())) {
				return Response.ofFail( I18nUtil.getString("jobgroup_field_addressType_limit") );
			}
			if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
				return Response.ofFail(I18nUtil.getString("jobgroup_field_registryList")+I18nUtil.getString("system_invalid") );
			}

			String[] addresss = xxlJobGroup.getAddressList().split(",");
			for (String item: addresss) {
				if (StringTool.isBlank(item)) {
					return Response.ofFail( I18nUtil.getString("jobgroup_field_registryList_invalid") );
				}
                if (!(HttpTool.isHttp(item) || HttpTool.isHttps(item))) {
                    return Response.ofFail( I18nUtil.getString("jobgroup_field_registryList_invalid")+"[2]" );
                }
			}
		}

		// process
		xxlJobGroup.setUpdateTime(new Date());

		int ret = xxlJobGroupMapper.save(xxlJobGroup);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/update")
	@ResponseBody
	@XxlSso(role = Consts.ADMIN_ROLE)
	public Response<String> update(JobGroup xxlJobGroup){
		// valid
		if (StringTool.isBlank(xxlJobGroup.getAppname())) {
			return Response.ofFail((I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (xxlJobGroup.getAppname().length()<4 || xxlJobGroup.getAppname().length()>64) {
			return Response.ofFail( I18nUtil.getString("jobgroup_field_appname_length") );
		}
		if (StringTool.isBlank(xxlJobGroup.getTitle())) {
			return Response.ofFail( (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (xxlJobGroup.getAddressType() == 0) {
			// 0=自动注册
			List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppname());
			String addressListStr = null;
			if (CollectionTool.isNotEmpty(registryList)) {
				Collections.sort(registryList);
				addressListStr = String.join(",", registryList);
			}
			xxlJobGroup.setAddressList(addressListStr);
		} else {
			// 1=手动录入
			if (StringTool.isBlank(xxlJobGroup.getAddressList())) {
				return Response.ofFail( I18nUtil.getString("jobgroup_field_addressType_limit") );
			}
			String[] addresss = xxlJobGroup.getAddressList().split(",");
			for (String item: addresss) {
				if (StringTool.isBlank(item)) {
					return Response.ofFail(I18nUtil.getString("jobgroup_field_registryList_invalid") );
				}
                if (!(HttpTool.isHttp(item) || HttpTool.isHttps(item))) {
                    return Response.ofFail( I18nUtil.getString("jobgroup_field_registryList_invalid")+"[2]" );
                }
			}
		}

		// process
		xxlJobGroup.setUpdateTime(new Date());

		int ret = xxlJobGroupMapper.update(xxlJobGroup);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	private List<String> findRegistryByAppName(String appnameParam){
		HashMap<String, List<String>> appAddressMap = new HashMap<>();
		List<JobRegistry> list = xxlJobRegistryMapper.findAll(Const.DEAD_TIMEOUT, new Date());
		if (CollectionTool.isNotEmpty(list)) {
			for (JobRegistry item: list) {
				if (!RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
					continue;
				}

				String appname = item.getRegistryKey();
                List<String> registryList = appAddressMap.computeIfAbsent(appname, k -> new ArrayList<>());

                if (!registryList.contains(item.getRegistryValue())) {
					registryList.add(item.getRegistryValue());
				}
			}
		}
		return appAddressMap.get(appnameParam);
	}

	@RequestMapping("/delete")
	@ResponseBody
	@XxlSso(role = Consts.ADMIN_ROLE)
	public Response<String> delete(@RequestParam("ids[]") List<Integer> ids){

		// parse id
		if (CollectionTool.isEmpty(ids) || ids.size()!=1) {
			return Response.ofFail(I18nUtil.getString("system_please_choose") + I18nUtil.getString("system_one") + I18nUtil.getString("system_data"));
		}
		int id = ids.get(0);

        // valid repeat operation
        JobGroup xxlJobGroup = xxlJobGroupMapper.load(id);
        if (xxlJobGroup == null) {
            return Response.ofSuccess();
        }

		// whether exists job
		int count = xxlJobInfoMapper.pageListCount(0, 10, id, -1,  null, null, null);
		if (count > 0) {
			return Response.ofFail( I18nUtil.getString("jobgroup_del_limit_0") );
		}

        // whether only exists one group
		List<JobGroup> allList = xxlJobGroupMapper.findAll();
		if (allList.size() == 1) {
			return Response.ofFail( I18nUtil.getString("jobgroup_del_limit_1") );
		}

        // remove group
		int ret = xxlJobGroupMapper.remove(id);
        // remove registry-data
        xxlJobRegistryMapper.removeByRegistryGroupAndKey(RegistType.EXECUTOR.name(), xxlJobGroup.getAppname());
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/loadById")
	@ResponseBody
	//@XxlSso(role = Consts.ADMIN_ROLE)		// open to default user, support show registry nodes
	public Response<JobGroup> loadById(@RequestParam("id") int id){
		JobGroup jobGroup = xxlJobGroupMapper.load(id);
		return jobGroup!=null?Response.ofSuccess(jobGroup):Response.ofFail();
	}

}

package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_ManageForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_ProjProgForcast_ManageService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：工程进度巡查管理 Company：ZhiShuSZ
 */
@Controller
public class Empj_ProjProgForcast_ManageController extends BaseController {
	@Autowired
	private Empj_ProjProgForcast_ManageService service;

	@RequestMapping(value = "/Empj_ProjProgForcast_Manage", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String execute(@RequestBody Empj_ProjProgForcast_ManageForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.execute(model);
			break;
		}
		default: {
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	
	
	@RequestMapping(value = "/Empj_ProjProgForcast_ManageList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String afListExecute(@ModelAttribute Empj_ProjProgForcast_ManageForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.afListExecute(model);
			break;
		}
		default: {
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	
	@RequestMapping(value = "/Empj_ProjProgForcast_ManageDetail", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String detailExecute(@ModelAttribute Empj_ProjProgForcast_ManageForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.detailExecute(model);
			break;
		}
		default: {
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	
	@RequestMapping(value = "/Empj_ProjProgForcast_ManageSubmit", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String submitExecute(@ModelAttribute Empj_ProjProgForcast_ManageForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.submitExecute(model);
			break;
		}
		default: {
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	
	
	/**
	 * 提交楼幢到网站
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Empj_ProjProgForcast_ManageBuildSubmit", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String buildSubmitExecute(@ModelAttribute Empj_ProjProgForcast_ManageForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		
		properties = service.buildSubmitExecute(model);

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	
	
	/**
	 * 保存附件信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Empj_ProjProgForcast_ManageSave", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String saveExecute(@RequestBody Empj_ProjProgForcast_ManageForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.saveExecute(model);
			break;
		}
		default: {
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	

}

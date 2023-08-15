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
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_ProjProgForcast_AFService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：工程进度巡查-主 Company：ZhiShuSZ
 */
@Controller
public class Empj_ProjProgForcast_AFController extends BaseController {
	@Autowired
	private Empj_ProjProgForcast_AFService service;

	@RequestMapping(value = "/Empj_ProjProgForcast_AF", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String execute(@RequestBody Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
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

	@RequestMapping(value = "/Empj_ProjProgForcast_AFList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String afListExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
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

	@RequestMapping(value = "/Empj_ProjProgForcast_AFBatchDelete", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String batchDeletetExecute(@RequestBody Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.batchDeletetExecute(model);
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

	@RequestMapping(value = "/Empj_ProjProgForcast_AFDetail", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String detailExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
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

		String jsonStr = new JsonUtil().propertiesToJson(properties, 8);

		// String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}

	@RequestMapping(value = "/Empj_ProjProgForcastSave", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String saveExecute(@RequestBody Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
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

	@RequestMapping(value = "/Empj_ProjProgForcastSubmit", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String submitExecute(@RequestBody Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
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

	@RequestMapping(value = "/Empj_ProjProgInspectionReport", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String reportExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.reportExecute(model);
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

	@RequestMapping(value = "/Empj_ProjProgInspectionReportExport", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String reportExportExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.reportExportExecute(model);
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

	@RequestMapping(value = "/Empj_ProjProgForcast_AFExport", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String exportExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.exportExecute(model);
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

	@RequestMapping(value = "/Empj_ProjProgForcast_AFHandler", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String handlerExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.handlerExecute(model);
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
	
	@RequestMapping(value = "/Empj_ProjProgForcast_AFPush", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String pushExecute(@ModelAttribute Empj_ProjProgForcast_AFForm model, HttpServletRequest request) {
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion()) {
		case 19000101: {
			properties = service.pushExecute(model);
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

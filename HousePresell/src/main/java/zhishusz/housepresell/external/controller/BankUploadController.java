package zhishusz.housepresell.external.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import zhishusz.housepresell.external.service.BankUploadService;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.util.JsonUtil;

/**
 * 网银数据推送接收Controller
 * 
 * @author Administrator
 *
 */
@Controller
public class BankUploadController {

	@Autowired
	private BankUploadService service;
	
	@Autowired
	private CommonService service1;

	@RequestMapping(value = "/bankUpload", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String execute(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {

		Properties properties = null;

		properties = service.execute(request, obj);

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		return jsonStr;
	}
	
	
	@RequestMapping(value = "/Outside_ProjProgForcast_ApprovalFeedback1", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String approvalFeedback1(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {

		Properties properties = null;

		properties = service1.approvalFeedback1(request, obj);

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		return jsonStr;
	}
	
	
	@RequestMapping(value = "/Outside_ProjProgForcast_ApprovalFeedback2", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String approvalFeedback2(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {

		Properties properties = null;

		properties = service1.approvalFeedback2(request, obj);

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		return jsonStr;
	}
	
	@RequestMapping(value = "/Outside_ProjProgForcast_ApprovalFeedback3", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String approvalFeedback3(HttpServletRequest request, HttpServletResponse response) {

		Properties properties = null;

		properties = service1.approvalFeedback3(request);

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		return jsonStr;
	}
	
	@RequestMapping(value = "/Outside_ProjProgForcast_ApprovalFeedback", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String approvalFeedback(HttpServletRequest request, HttpServletResponse response) {

		Properties properties = null;

		properties = service1.approvalFeedback3(request);

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		return jsonStr;
	}
}

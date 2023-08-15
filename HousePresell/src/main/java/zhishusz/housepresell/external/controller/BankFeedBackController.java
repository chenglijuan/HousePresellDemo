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

import zhishusz.housepresell.external.service.BatchBankFeedBackService;
import zhishusz.housepresell.util.JsonUtil;

/**
 * 接受支付结果推送信息Controller
 * 
 * @author Administrator
 *
 */
@Controller
public class BankFeedBackController {

	@Autowired
	private BatchBankFeedBackService service;

	@RequestMapping(value = "/bankFeedBack", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String execute(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {

		Properties properties = null;

		properties = service.execute(request, obj);

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		return jsonStr;
	}
}

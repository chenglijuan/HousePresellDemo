package zhishusz.housepresell.external.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import zhishusz.housepresell.external.po.TripleAgreementModel;
import zhishusz.housepresell.external.service.Tgxy_TripleAgreementAnswerInterfaceService;

/**
 * 2.三方协议归档结果反馈接口
 * @author Administrator
 *
 */
@Controller
public class Tgxy_TripleAgreementAnswerController
{
	@Autowired
	private Tgxy_TripleAgreementAnswerInterfaceService service;
	@Autowired
	private Gson gson;
	
	@RequestMapping(value="/answerSfXy1",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute TripleAgreementModel model, HttpServletRequest request)
	{
		
		Integer execute = service.execute(model);
		
		return gson.toJson(execute);
	}
	
	
	@RequestMapping(value="/answerSfXy",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(HttpServletRequest request,HttpServletResponse response)
	{
		
		
		String xybh = request.getParameter("xybh");
		String cljg = request.getParameter("cljg");
		String clyj = request.getParameter("clyj");
		
		TripleAgreementModel model = new TripleAgreementModel();
		model.setXybh(xybh);
		model.setCljg(cljg);
		model.setClyj(clyj);
		
		Integer execute = service.execute(model);
		
		return gson.toJson(execute);
	}
}

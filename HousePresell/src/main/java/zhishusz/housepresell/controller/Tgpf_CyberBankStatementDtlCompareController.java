package zhishusz.housepresell.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementDtlCompareService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller 网银对账-人工对账
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_CyberBankStatementDtlCompareController
{
	@Autowired
	private Tgpf_CyberBankStatementDtlCompareService service;
	
	@RequestMapping(value="/tgpf_CyberBankStatementDtlCompare",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_CyberBankStatementDtlForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				
//				// 时间戳
//				model.setTradeTimeStamp("时间戳");
//				// 收款账户
//				model.setRecipientAccount("收款账户");
//				// 收款账号
//				model.setRecipientName("收款账号");
//				// 交易金额
//				model.setTradeAmount("交易金额"); 
//				// 摘要
//				model.setRemark("");
//				
//				List<Tgpf_CyberBankStatementDtlForm> tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtlForm>();		
//				Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
//				tgpf_CyberBankStatementDtlForm.setTableId(747l);
//				tgpf_CyberBankStatementDtlForm.setCyBankTripleAgreementNum("123456");
//				
//				Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm1 = new Tgpf_CyberBankStatementDtlForm();
//				tgpf_CyberBankStatementDtlForm1.setTableId(760l);
//				tgpf_CyberBankStatementDtlForm1.setCyBankTripleAgreementNum("123456");
//				
//				tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtlForm);
//				tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtlForm1);
				
//				model.setTgpf_CyberBankStatementDtl(tgpf_CyberBankStatementDtlList);
				
				properties = service.execute(model);
				break;
			}
			default :
			{
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
				break;
			}
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgpf_CyberBankStatementDtlDelete", model, properties, jsonStr);
		
		return jsonStr;
	}
}

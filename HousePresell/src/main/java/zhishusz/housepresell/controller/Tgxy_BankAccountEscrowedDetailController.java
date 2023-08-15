package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.service.Tgxy_BankAccountEscrowedDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_BankAccountEscrowedRebuild;

/*
 * Controller详情：托管账户
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgxy_BankAccountEscrowedDetailController extends BaseController
{
	@Autowired
	private Tgxy_BankAccountEscrowedDetailService service;
	@Autowired
	private Tgxy_BankAccountEscrowedRebuild rebuild;
	
	@RequestMapping(value="/Tgxy_BankAccountEscrowedDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_BankAccountEscrowedForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{

				Object obj = request.getSession().getAttribute("user");
				Sm_User user = (Sm_User)obj;
				//判断是开发企业的账号
				Emmp_CompanyInfo companyInfo = user.getCompany();
				// 如果是开发企业访问 直接返回无授权操作
				if(StringUtils.isBlank(companyInfo.geteCode()) || !"10000".equals(companyInfo.geteCode())){
					properties = new MyProperties();
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, "无授权操作");
					break;
				}

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
		
		if(MyBackInfo.isSuccess(properties))
		{
			properties.put("tgxy_BankAccountEscrowed", rebuild.execute((Tgxy_BankAccountEscrowed)(properties.get("tgxy_BankAccountEscrowed"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgxy_BankAccountEscrowedDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

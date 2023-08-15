package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountCompareListService;
import zhishusz.housepresell.service.Tgpf_BalanceOfAccountCompareService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_BalanceOfAccountRebuild;

/*
 * Controller列表查询：网银对账(自动对账)
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_BalanceOfAccountCompareController
{
	@Autowired
	private Tgpf_BalanceOfAccountCompareService service;
	@Autowired
	private Tgpf_BalanceOfAccountRebuild rebuild;
	
	@RequestMapping(value="/Tgpf_BalanceOfAccountCompare",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_DepositDetailForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
//				Long[] idArr = new Long[]{193l};
				
//				model.setIdArr(idArr);
				
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
		
//		super.writeOperateHistory("Tgpf_BalanceOfAccountAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}

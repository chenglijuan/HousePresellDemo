package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.service.Tgpj_EscrowStandardVerMngDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpj_EscrowStandardVerMngRebuild;

/*
 * Controller详情：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpj_EscrowStandardVerMngDetailController extends BaseController
{
	@Autowired
	private Tgpj_EscrowStandardVerMngDetailService service;
	@Autowired
	private Tgpj_EscrowStandardVerMngRebuild rebuild;
	
	@RequestMapping(value="/Tgpj_EscrowStandardVerMngDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpj_EscrowStandardVerMngForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
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
			if (model.getGetDetailType() != null && "1".equals(model.getGetDetailType()))
			{
				//审批流
				properties.put("tgpj_EscrowStandardVerMng", rebuild.getDetailForApprovalProcess((Tgpj_EscrowStandardVerMng)(properties.get("tgpj_EscrowStandardVerMng"))));
			}
			else
			{
				properties.put("tgpj_EscrowStandardVerMng", rebuild.execute((Tgpj_EscrowStandardVerMng)(properties.get("tgpj_EscrowStandardVerMng"))));
			}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpj_EscrowStandardVerMngDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

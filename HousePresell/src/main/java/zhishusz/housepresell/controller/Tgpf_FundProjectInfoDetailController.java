package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_FundProjectInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpf_FundProjectInfo;
import zhishusz.housepresell.service.Tgpf_FundProjectInfoDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundProjectInfoRebuild;

/*
 * Controller详情：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundProjectInfoDetailController extends BaseController
{
	@Autowired
	private Tgpf_FundProjectInfoDetailService service;
	@Autowired
	private Tgpf_FundProjectInfoRebuild rebuild;
	
	@RequestMapping(value="/Tgpf_FundProjectInfoDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_FundProjectInfoForm model, HttpServletRequest request)
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
			properties.put("tgpf_FundProjectInfo", rebuild.execute((Tgpf_FundProjectInfo)(properties.get("tgpf_FundProjectInfo"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundProjectInfoDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

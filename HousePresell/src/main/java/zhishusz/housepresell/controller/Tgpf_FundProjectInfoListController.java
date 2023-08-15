package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Tgpf_FundProjectInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundProjectInfoListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundProjectInfoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundProjectInfoListController extends BaseController
{
	@Autowired
	private Tgpf_FundProjectInfoListService service;
	@Autowired
	private Tgpf_FundProjectInfoRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_FundProjectInfoList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
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
			properties.put("tgpf_FundProjectInfoList", rebuild.execute((List)(properties.get("tgpf_FundProjectInfoList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundProjectInfoList", model, properties, jsonStr);
		
		return jsonStr;
	}
}

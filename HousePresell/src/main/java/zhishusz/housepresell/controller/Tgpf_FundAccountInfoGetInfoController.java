package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgpf_FundAccountInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundAccountInfoGetInfoService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAccountInfoRebuild;

/*
 * Controller列表查询：推送给财务系统-设置,查询新增
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundAccountInfoGetInfoController
{

	@Autowired
	private Tgpf_FundAccountInfoGetInfoService service;
	@Autowired
	private Tgpf_FundAccountInfoRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_FundAccountInfoGetInfo",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_FundAccountInfoForm model, HttpServletRequest request)
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
			
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Tgpf_FundAccountInfoList", model, properties, jsonStr);
		
		return jsonStr;
	}
}

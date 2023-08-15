package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundAppropriated_AFAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.json.JSONUtil;

/*
 * Controller添加操作：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundAppropriated_AFAddController extends BaseController
{
	@Autowired
	private Tgpf_FundAppropriated_AFAddService service;
	
	@RequestMapping(value="/Tgpf_FundAppropriated_AFAdd",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@RequestBody Tgpf_FundAppropriated_AFForm model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Tgpf_FundAppropriated_AFAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}

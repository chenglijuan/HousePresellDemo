package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_CyberBankStatementDtlRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_CyberBankStatementRebuild;

/*
 * Controller详情：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_CyberBankStatementDetailController extends BaseController
{
	@Autowired
	private Tgpf_CyberBankStatementDetailService service;
	@Autowired
	private Tgpf_CyberBankStatementRebuild rebuild;
	@Autowired
	private Tgpf_CyberBankStatementDtlRebuild rebuilddel;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Tgpf_CyberBankStatementDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_CyberBankStatementForm model, HttpServletRequest request)
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
			properties.put("tgpf_CyberBankStatement", rebuild.execute((Tgpf_CyberBankStatement)(properties.get("tgpf_CyberBankStatement"))));
			
			properties.put("tgpf_CyberBankStatementDtlList", rebuilddel.execute((List)(properties.get("tgpf_CyberBankStatementDtlList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_CyberBankStatementDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

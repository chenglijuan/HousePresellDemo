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

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_RefundInfoUnclearedDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_RefundInfoRebuild;
import zhishusz.housepresell.util.rebuild.Tgpj_BankAccountSupervisedRebuild;

/*
 * Controller详情：退房退款-贷款未结清
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_RefundInfoUnclearedDetailController extends BaseController
{
	@Autowired
	private Tgpf_RefundInfoUnclearedDetailService service;
	@Autowired
	private Tgpf_RefundInfoRebuild rebuild;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild3;
	@Autowired
	private Tgpj_BankAccountSupervisedRebuild bankRebuild;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Tgpf_RefundInfoUnclearedDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_RefundInfoForm model, HttpServletRequest request)
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
			properties.put("tgpf_RefundInfoUnclearedDetail", rebuild.execute((Tgpf_RefundInfo)(properties.get("tgpf_RefundInfoUnclearedDetail"))));
			
			properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
			
			properties.put("bankAccountSupervisedList", bankRebuild.executeForSelectList2((List)(properties.get("bankAccountSupervisedList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_RefundInfoUnclearedDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

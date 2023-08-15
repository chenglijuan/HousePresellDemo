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

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.service.Sm_ApprovalProcess_SpecialFundAppropriatedDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_SpecialFundAppropriated_AFRebuild;

/*
 * Controller详情：特殊拨付-申请主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_Approvalprocess_SpecialFundAppropriatedDetailController extends BaseController
{
	@Autowired
	private Sm_ApprovalProcess_SpecialFundAppropriatedDetailService service;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFRebuild rebuild;
	@Autowired
	private Sm_AttachmentCfgRebuild rebuild4;
	
	@RequestMapping(value="/Sm_ApprovalProcess_SpecialFundAppropriatedDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_SpecialFundAppropriated_AFForm model, HttpServletRequest request)
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
			
			properties.put("smAttachmentCfgList",
					rebuild4.getDetailForAdmin2((List) (properties.get("smAttachmentCfgList"))));
			
			properties.put("tgpf_SpecialFundAppropriated_AF", rebuild.getDetailForSpecialFund((Tgpf_SpecialFundAppropriated_AF)(properties.get("tgpf_SpecialFundAppropriated_AF"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_SpecialFundAppropriated_AFDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

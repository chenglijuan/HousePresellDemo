package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Emmp_CompanyCooperationDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;
import zhishusz.housepresell.util.rebuild.Emmp_OrgMemberRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/*
 * Controller详情：机构信息
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class Emmp_CompanyCooperationDetailController extends BaseController
{
	@Autowired
	private Emmp_CompanyCooperationDetailService service;
	@Autowired
	private Emmp_CompanyInfoRebuild rebuild;

	@Autowired
	private Emmp_OrgMemberRebuild rebuild2;
	
	@RequestMapping(value="/Emmp_CompanyCooperationDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Emmp_CompanyCooperation", key="'Emmp_CompanyInfoId'+#model.getTableId()")
	public String execute(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request)
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
			if ("详情".equals(model.getReqSource()))
			{
				properties.put("emmp_CompanyInfo", rebuild.execute((Emmp_CompanyInfo)(properties.get("emmp_CompanyInfo"))));
			}
			else if ("审批".equals(model.getReqSource()))
			{
				properties.put("emmp_CompanyInfo", rebuild.getDetailForApproval2((Emmp_CompanyInfo)(properties.get(
						"emmp_CompanyInfo")), model));

				properties.put("emmp_OrgMemberList", rebuild2.getListForApproval2((List)(properties.get(
						"emmp_OrgMemberList")), model));
			}
			else
			{
				properties.put("emmp_CompanyInfo", rebuild.getDetailForApproval((Emmp_CompanyInfo)(properties.get(
						"emmp_CompanyInfo"))));

				properties.put("emmp_OrgMemberList", rebuild2.getListForApproval((List)(properties.get(
						"emmp_OrgMemberList")), model.getTableId(),
						S_BusiState.NoRecord.equals(model.getBusiState()) ? "020108" : "020109", model.getBusiState()));
			}
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Emmp_CompanyCooperationDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

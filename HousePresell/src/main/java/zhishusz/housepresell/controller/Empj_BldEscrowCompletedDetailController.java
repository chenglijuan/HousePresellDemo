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

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.service.Empj_BldEscrowCompletedDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BldEscrowCompletedRebuild;
import zhishusz.housepresell.util.rebuild.Empj_BldEscrowCompleted_DtlRebuild;

/*
 * Controller详情：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_BldEscrowCompletedDetailController extends BaseController
{
	@Autowired
	private Empj_BldEscrowCompletedDetailService service;
	@Autowired
	private Empj_BldEscrowCompletedRebuild rebuild;
	@Autowired
	private Empj_BldEscrowCompleted_DtlRebuild dltRebuild;
	
	@RequestMapping(value="/Empj_BldEscrowCompletedDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_BldEscrowCompletedForm model, HttpServletRequest request)
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
				properties.put("empj_BldEscrowCompleted", rebuild.getDetailForApprovalProcess((Empj_BldEscrowCompleted)(properties.get("empj_BldEscrowCompleted"))));
				//托管终止楼栋信息（明细表）
				properties.put("empj_BldEscrowCompleted_DtlList", dltRebuild.execute((List)properties.get("empj_BldEscrowCompleted_DtlList")));
			}
			else
			{
				properties.put("empj_BldEscrowCompleted", rebuild.execute((Empj_BldEscrowCompleted)(properties.get("empj_BldEscrowCompleted"))));
				//托管终止楼栋信息（明细表）
				properties.put("empj_BldEscrowCompleted_DtlList", dltRebuild.execute((List)properties.get("empj_BldEscrowCompleted_DtlList")));
			}
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_BldEscrowCompletedDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
	
	@RequestMapping(value="/Empj_BldEscrowCompletedPush",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String pushExecute(@ModelAttribute Empj_BldEscrowCompletedForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.pushExecute(model);
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
		
		return jsonStr;
	}
	
	@RequestMapping(value="/Empj_BldEscrowCompletedSign",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String signExecute(@ModelAttribute Empj_BldEscrowCompletedForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.signExecute(model);
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
		
		return jsonStr;
	}
}

package zhishusz.housepresell.controller;

import cn.hutool.db.DaoTemplate;
import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_FundOverallPlanListDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_FundAppropriatedRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_FundOverallPlanRebuild;
import zhishusz.housepresell.util.rebuild.Tgpf_OverallPlanAccoutRebuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/*
 * Controller列表查询：资金统筹
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_FundOverallPlanListDetailController extends BaseController
{
	@Autowired
	private Tgpf_FundOverallPlanListDetailService service;
	@Autowired
	private Tgpf_FundOverallPlanRebuild rebuild;
	@Autowired
	private Tgpf_OverallPlanAccoutRebuild accoutRebuild;
	@Autowired
	private Tgpf_FundAppropriatedRebuild fundAppropriatedRebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgpf_FundOverallPlanListDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_OverallPlanAccoutForm model, HttpServletRequest request)
	{
		model.init(request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				//System.out.println("时间1----"+sdf.format(new Date()));
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
			//资金统筹
			System.out.println("时间5----"+sdf.format(new Date()));
			properties.put("tgpf_FundOverallPlan",rebuild.getDetail((Tgpf_FundOverallPlan) properties.get("tgpf_FundOverallPlan")));
			//统筹账户
			System.out.println("时间6----"+sdf.format(new Date()));
			properties.put("tgpf_overallPlanAccoutList", accoutRebuild.getDetailForAdmin((List)(properties.get("tgpf_overallPlanAccoutList"))));
			//用款申请汇总
			System.out.println("时间7----"+sdf.format(new Date()));
			properties.put("fundOverallPlanDetailList", rebuild.getFundOverallPlanDetailList((List)properties.get("fundOverallPlanDetailList")));
			//资金拨付
			System.out.println("时间8----"+sdf.format(new Date()));
			properties.put("tgpf_FundAppropriatedList", fundAppropriatedRebuild.execute((List)properties.get("tgpf_FundAppropriatedList")));

		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgpf_FundOverallPlanListDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

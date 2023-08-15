package zhishusz.housepresell.controller.extra;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.extra.Sm_HomePageProjectDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;

/**
 * 首页地图展示项目详情
 * 
 * @ClassName: Sm_HomePageProjectDetailController
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月12日 下午3:10:40
 * @version V1.0
 *
 */
@Controller
public class Sm_HomePageProjectDetailController extends BaseController
{
	@Autowired
	private Sm_HomePageProjectDetailService service;
	@Autowired
	private Empj_ProjectInfoRebuild rebuild;// 项目

	@RequestMapping(value = "/Sm_HomePageProjectDetail", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Empj_ProjectInfoForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
		case 19000101:
		{
			properties = service.execute(model);
			break;
		}
		default:
		{
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		if (MyBackInfo.isSuccess(properties))
		{
			try
			{
				properties.put("empj_ProjectInfo",
						rebuild.getHomePageByDetail((Empj_ProjectInfo) (properties.get("empj_ProjectInfo"))));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_HomePageProjectDetail", model, properties, jsonStr);

		return jsonStr;
	}
}

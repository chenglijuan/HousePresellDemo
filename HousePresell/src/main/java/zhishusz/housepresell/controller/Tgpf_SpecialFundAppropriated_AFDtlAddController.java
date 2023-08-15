package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_SpecialFundAppropriated_AFDtlAddService;
import zhishusz.housepresell.service.Tgpf_SpecialFundAppropriated_AFDtlUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：特殊拨付-申请子表
 * Company：ZhiShuSZ
 */
@Controller
public class Tgpf_SpecialFundAppropriated_AFDtlAddController extends BaseController
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlAddService serviceAdd;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlUpdateService serviceUpdate;

	@RequestMapping(value = "/Tgpf_SpecialFundAppropriated_AFDtlAdd", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_SpecialFundAppropriated_AFDtlForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
		case 19000101:
		{
			Long tableId = model.getTableId();
			if (null == tableId || tableId <= 0)
			{
				//新增操作
				properties = serviceAdd.execute(model);
				
			}
			else
			{
				//修改操作
				properties = serviceUpdate.execute(model);
				
			}

			
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

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgpf_SpecialFundAppropriated_AFDtlAdd", model, properties, jsonStr);

		return jsonStr;
	}
}

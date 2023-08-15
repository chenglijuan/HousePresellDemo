package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tg_BankLoanInSituation_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_BankLoanOutSituation_ViewExportExcelService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询操作：银行放款项目出账情况表-导出Excel
 * Company：ZhiShuSZ
 */
@Controller
public class Tg_BankLoanOutSituation_ViewExportExcelController extends BaseController
{
	@Autowired
	private Tg_BankLoanOutSituation_ViewExportExcelService service;

	@RequestMapping(value = "/Tg_BankLoanOutSituation_ViewExportExcel", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tg_BankLoanInSituation_ViewForm model, HttpServletRequest request)
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

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tg_BankLoanOutSituation_ViewExportExcel", model, properties, jsonStr);

		return jsonStr;
	}
}

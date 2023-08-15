package zhishusz.housepresell.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tg_DepositProjectAnalysis_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tg_DepositProjectAnalysis_ViewExportExcelService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询操作：托管项目情况分析表-导出Excel
 * Company：ZhiShuSZ
 */
@Controller
public class Tg_DepositProjectAnalysis_ViewExportExcelController extends BaseController
{
	@Autowired
	private Tg_DepositProjectAnalysis_ViewExportExcelService service;

	@RequestMapping(value = "/Tg_DepositProjectAnalysis_ViewExportExcel", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tg_DepositProjectAnalysis_ViewForm model, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException
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

		//super.writeOperateHistory("Tg_DepositProjectAnalysis_ViewExportExcel", model, properties, jsonStr);

		return jsonStr;
	}

}

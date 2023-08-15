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
import zhishusz.housepresell.controller.form.extra.Qs_EscrowBankFunds_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.extra.Qs_EscrowBankFunds_ViewExportExcelService;
import zhishusz.housepresell.service.extra.Qs_EscrowBankFunds_ViewListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询：贷款银行资金明细表-Excel导出
 * Company：ZhiShuSZ
 * */
@Controller
public class Qs_EscrowBankFunds_ViewExportExcelController extends BaseController
{
	@Autowired
	private Qs_EscrowBankFunds_ViewExportExcelService service;
	
	@RequestMapping(value="/Qs_EscrowBankFunds_ViewExportExcel",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Qs_EscrowBankFunds_ViewForm model, HttpServletRequest request)
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
//		super.writeOperateHistory("Qs_EscrowBankFunds_ViewExportExcel", model, properties, jsonStr);
		
		return jsonStr;
	}
}

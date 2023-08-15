package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementExportExcelService;
import zhishusz.housepresell.service.Tgpf_CyberBankStatementListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgpf_CyberBankStatementRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：网银数据上传-导出Excel
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_CyberBankStatementExportExcelController extends BaseController
{
	@Autowired
	private Tgpf_CyberBankStatementExportExcelService service;
	
	@RequestMapping(value="/Tgpf_CyberBankStatement_Export",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_CyberBankStatementForm model, HttpServletRequest request)
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

		super.writeOperateHistory("tgpf_CyberBankStatementExportExcel", model, properties, jsonStr);

		return jsonStr;
	}
}

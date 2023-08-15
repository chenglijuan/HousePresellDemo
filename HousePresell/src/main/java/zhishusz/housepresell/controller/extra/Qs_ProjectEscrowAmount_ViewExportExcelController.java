package zhishusz.housepresell.controller.extra;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.extra.Qs_ProjectEscrowAmount_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.extra.Qs_ProjectEscrowAmount_ViewExportExcelService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/**
 * 统计报表-项目托管资金收支情况表-导出Excel
 * @ClassName:  Qs_ProjectEscrowAmount_ViewExportExcelController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月20日 上午11:44:41   
 * @version V1.0 
 *
 */
@Controller
public class Qs_ProjectEscrowAmount_ViewExportExcelController extends BaseController
{
	@Autowired
	private Qs_ProjectEscrowAmount_ViewExportExcelService service;
	
	@RequestMapping(value="/Qs_ProjectEscrowAmount_ViewExportExcel",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Qs_ProjectEscrowAmount_ViewForm model, HttpServletRequest request)
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
		
//		super.writeOperateHistory("Qs_ProjectEscrowAmount_ViewExportExcel", model, properties, jsonStr);
		
		return jsonStr;
	}
}

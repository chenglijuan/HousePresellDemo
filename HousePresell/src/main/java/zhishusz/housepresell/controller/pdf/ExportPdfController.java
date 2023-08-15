package zhishusz.housepresell.controller.pdf;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.pdf.ExportPdfService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller  导出操作：导出PDF
 * Company：ZhiShuSZ
 * */
@Controller
public class ExportPdfController extends BaseController
{
	
	@Autowired
	private ExportPdfService service;
	
	@RequestMapping(value="/exportPDF",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute ExportPdfForm model, HttpServletRequest request)
	{
	/*model.setSourceId("66");
		model.setSourceBusiCode("06120201");
		model.setReqAddress("http://192.168.1.6:8010/HousePresell/admin/Sm_HomePageList");*/
		
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
		
		super.writeOperateHistory("exportPDF", model, properties, jsonStr);
		
		return jsonStr;
	}
}

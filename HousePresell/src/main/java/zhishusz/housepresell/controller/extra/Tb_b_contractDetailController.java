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
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.service.extra.Tb_b_contractDetailDetailService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库查询合同详情
 * @ClassName:  Tb_b_ontractDetailController   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月2日 下午8:48:51   
 * @version V1.0 
 *
 */
@Controller
public class Tb_b_contractDetailController extends BaseController
{
	@Autowired
	private Tb_b_contractDetailDetailService service;
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value="/Tb_b_contractDetail_bak",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String executeBak(@ModelAttribute Tb_b_contractFrom model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Tb_b_contractDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
	
	@RequestMapping(value="/Tb_b_contractDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tb_b_contractFrom model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = commonService.queryContractInfo(model);
//				properties = commonService.queryContractInfoForJson(model);
				
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
		
		super.writeOperateHistory("Tb_b_contractDetail", model, properties, jsonStr);
		
		return jsonStr;
	}
}

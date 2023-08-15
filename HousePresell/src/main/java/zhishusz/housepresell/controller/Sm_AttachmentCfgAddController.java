package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Sm_AttachmentCfgAddService;
import zhishusz.housepresell.service.Sm_AttachmentCfgUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：附件配置
 * Company：ZhiShuSZ
 * */
@Controller
public class Sm_AttachmentCfgAddController extends BaseController
{
	@Autowired
	private Sm_AttachmentCfgAddService service;
	@Autowired
	private Sm_AttachmentCfgUpdateService updateservice;
	
	@RequestMapping(value="/Sm_AttachmentCfgAdd",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_AttachmentCfgForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				
				//添加
				if(model!=null && model.getTableId()==null){					
					//获取业务类型
					String busiTypeid=request.getParameter("busiTypeId");
					properties = service.execute(model,busiTypeid);

				}else//编辑				
				{
					properties = updateservice.execute(model);			
				}
				
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
		
		super.writeOperateHistory("Sm_AttachmentCfgAdd", model, properties, jsonStr);
		
		return jsonStr;
	}
}

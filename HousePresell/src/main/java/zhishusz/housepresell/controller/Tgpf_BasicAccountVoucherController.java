package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgpf_BasicAccountVoucherDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_BasicAccountVoucherForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_BasicAccountVoucherService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询：基本户凭证
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgpf_BasicAccountVoucherController extends BaseController
{
	@Autowired
	private Tgpf_BasicAccountVoucherService service;
	
	/**
	 * list
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Tgpf_BasicAccountVoucherList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpf_BasicAccountVoucherForm model, HttpServletRequest request)
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
		
		super.writeOperateHistory("Tgpf_BasicAccountVoucherList", model, properties, jsonStr);
		
		return jsonStr;
	}
	
	/**
     * detail
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/Tgpf_BasicAccountVoucherDetail",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String detailExecute(@ModelAttribute Tgpf_BasicAccountVoucherForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.detailExecute(model);
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
        
        super.writeOperateHistory("Tgpf_BasicAccountVoucherDetail", model, properties, jsonStr);
        
        return jsonStr;
    }
    
    /**
     * list
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/Tgpf_BasicAccountVoucherDtlList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dtlListExecute(@ModelAttribute Tgpf_BasicAccountVoucherDtlForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.dtlListExecute(model);
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
        
        super.writeOperateHistory("Tgpf_BasicAccountVoucherDtlList", model, properties, jsonStr);
        
        return jsonStr;
    }
    
    /**
     * dtlSave
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/Tgpf_BasicAccountVoucherDtlSave",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dtlSaveExecute(@ModelAttribute Tgpf_BasicAccountVoucherDtlForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.dtlSaveExecute(model);
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
        
        super.writeOperateHistory("Tgpf_BasicAccountVoucherDtlSave", model, properties, jsonStr);
        
        return jsonStr;
    }
    
    /**
     * dtlUpdate
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/Tgpf_BasicAccountVoucherDtlUpdate",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dtlUpdateExecute(@ModelAttribute Tgpf_BasicAccountVoucherDtlForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.dtlUpdateExecute(model);
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
        
        super.writeOperateHistory("Tgpf_BasicAccountVoucherDtlUpdate", model, properties, jsonStr);
        
        return jsonStr;
    }
    
    /**
     * dtlDelete
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/Tgpf_BasicAccountVoucherDtlDelete",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dtlDeleteExecute(@ModelAttribute Tgpf_BasicAccountVoucherDtlForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.dtlDeleteExecute(model);
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
        
        super.writeOperateHistory("Tgpf_BasicAccountVoucherDtlDelete", model, properties, jsonStr);
        
        return jsonStr;
    }
    
    /**
     * send
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/Tgpf_BasicAccountVoucherSend",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String sendExecute(@ModelAttribute Tgpf_BasicAccountVoucherForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.sendExecute(model);
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
        
        super.writeOperateHistory("Tgpf_BasicAccountVoucherSend", model, properties, jsonStr);
        
        return jsonStr;
    }
}

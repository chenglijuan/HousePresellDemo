package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_NodePredictionService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：楼幢预测节点
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_NodePredictionController extends BaseController
{
	@Autowired
	private Empj_NodePredictionService service;
	
	/**
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Empj_NodePrediction",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String saveExecute(@RequestBody CommonForm model, HttpServletRequest request)
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
		
		return jsonStr;
	}
	
	/**
	 * 加载楼幢进度节点列表
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/loadingVersionListByBuildingId",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String loadingListExecute(@RequestBody CommonForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.loadingListExecute(model);
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
        
        return jsonStr;
    }
    
    /**
     * 保存预测楼幢进度节点
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/saveBuildindVersionList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String saveListExecute(@RequestBody CommonForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.saveListExecute(model);
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
        
        return jsonStr;
    }
	
}

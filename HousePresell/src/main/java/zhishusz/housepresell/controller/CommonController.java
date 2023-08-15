package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import zhishusz.housepresell.controller.form.ApprovalForm;
import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MQConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;

/*
 * Controller公共查询：
 * Company：ZhiShuSZ
 */
@Controller
public class CommonController extends BaseController
{

	@Autowired
	private CommonService service;
	
	@Autowired
	private Empj_BuildingInfoRebuild empj_buildingInfoRebuild;
	
	/**
     * 工程进度节点更新楼幢加载
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Common_BldForBuildingList", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String bldForBuildingList(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.bldForBuildingList(model);
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

        return jsonStr;
    }
    
	/**
	 * 工程进度节点更新楼幢加载
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/Common_BldForBuildingList1", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String bldForBuildingList1(@ModelAttribute CommonForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.bldForBuildingList1(model);
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

		if (MyBackInfo.isSuccess(properties))
		{
			properties.put("empj_BuildingInfoList", empj_buildingInfoRebuild.executeBldForBuildingList((List) (properties.get("empj_BuildingInfoList"))));
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}
	
	/**
	 * 项目部报表-楼幢销售面积查询

	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Report_SaleAreaForBuildingList", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String saleAreaForBuildingList(@ModelAttribute CommonForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.saleAreaForBuildingList(model);
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

		return jsonStr;
	}
	
	/**
	 * 项目部报表-楼幢销售面积查询-导出

	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Report_SaleAreaForBuildingList_ExportExcel", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String saleAreaForBuildingList_ExportExcel(@ModelAttribute CommonForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.saleAreaForBuildingList_ExportExcel(model);
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

		return jsonStr;
	}
	
	/**
	 * 受限额度变更审批确认
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Empj_BldLimitAmount_AFHandler", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String bldLimitAmount_AFHandler(@ModelAttribute CommonForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.bldLimitAmount_AFHandler(model);
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

		return jsonStr;
	}
	
	
	@Autowired
	protected Gson gson;
	@Autowired
	MQConnectionUtil mqConnectionUtil;
	
	@RequestMapping(value = "/SendMessage", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public void sendMessage(@ModelAttribute CommonForm model, HttpServletRequest request)
	{
		System.out.println("start");
		mqConnectionUtil.sendMessage(MQKey_EventType.EARLY_WARNING_SENDER, MQKey_OrgType.EARLY,"qweqweqweqwe");
		System.out.println("end");
	}
	
	
	   /**
     * 受限额度变更审批确认
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/PhotoHandleTest", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String photoHandle(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        properties = service.photoHandle(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    /**
     * 获取预售合同
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetContractTest", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getContract(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        properties = service.getContract(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    /**
     * 网站对接SSO验证
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/LoginVerification", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String loginVerification(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        properties = service.loginVerification(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    
    /**
     * 网站审核反馈接口
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Outside_ProjProgForcast_ApprovalFeedback11111", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String approvalFeedback(@RequestBody CommonForm model, HttpServletRequest request)
    {

        Properties properties = null;
        properties = service.approvalFeedback(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    /**
     * 处理图片
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/HandlerPicForProjProgForcast", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String handlerPicForProjProgForcast(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        properties = service.handlerPicForProjProgForcast(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    /**
     * 处理图片
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/PushPicForProjProgForcast", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String pushPicForProjProgForcast(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        properties = service.pushPicForProjProgForcast(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    
    
    /**
     * 工程进度巡查楼幢提交
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/PushSumitForProjProgForcast", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String pushSumitForProjProgForcast(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
    	
        Properties properties = null;
        properties = service.pushSumitForProjProgForcast(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    
    /**
     * 工程进度巡查审核通过
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/ProjProgForcastApproval", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String projProgForcastApproval(@ModelAttribute CommonForm model, HttpServletRequest request)
    {
    	
        Properties properties = null;
        properties = service.projProgForcastApproval(model);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }


	/**
	 * 推送信息到公积金
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/PushApprovalInfo", produces = "text/html;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String pushApprovalInfo(@ModelAttribute CommonForm model)
	{

//		model.setTableId(8698l);
		Properties properties = null;
		properties = service.PushApprovalInfo(model);

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		return jsonStr;
	}

}

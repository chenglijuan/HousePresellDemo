package zhishusz.housepresell.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.controller.form.Emmp_BankInfoForm;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.controller.form.Sm_StreetInfoForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Emmp_BankBranchListService;
import zhishusz.housepresell.service.Emmp_BankInfoListService;
import zhishusz.housepresell.service.Emmp_CompanyInfoListForSelectService;
import zhishusz.housepresell.service.Emmp_CompanyInfoListService;
import zhishusz.housepresell.service.Emmp_CompanyInfoSpecialListService;
import zhishusz.housepresell.service.Empj_BuildingInfoListService;
import zhishusz.housepresell.service.Empj_ProjectInfoListService;
import zhishusz.housepresell.service.Sm_BaseParameterListService;
import zhishusz.housepresell.service.Sm_CityRegionInfoForRangeAuthListService;
import zhishusz.housepresell.service.Sm_CityRegionInfoListService;
import zhishusz.housepresell.service.Sm_Permission_RoleListForSelectService;
import zhishusz.housepresell.service.Sm_Permission_UIResourceListService;
import zhishusz.housepresell.service.Sm_StreetInfoListService;
import zhishusz.housepresell.service.Sm_UserListService;
import zhishusz.housepresell.service.Tgpj_BankAccountSupervisedListService;
import zhishusz.housepresell.service.Tgpj_EscrowStandardVerMngListService;
import zhishusz.housepresell.service.Tgxy_BankAccountEscrowedListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.rebuild.Emmp_BankBranchRebuild;
import zhishusz.housepresell.util.rebuild.Emmp_BankInfoRebuild;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;
import zhishusz.housepresell.util.rebuild.RebuilderBase;
import zhishusz.housepresell.util.rebuild.Sm_BaseParameterRebuild;
import zhishusz.housepresell.util.rebuild.Sm_CityRegionInfoRebuild;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RoleRebuild;
import zhishusz.housepresell.util.rebuild.Sm_Permission_UIResourceRebuild;
import zhishusz.housepresell.util.rebuild.Sm_StreetInfoRebuild;
import zhishusz.housepresell.util.rebuild.Sm_UserRebuild;
import zhishusz.housepresell.util.rebuild.Tgpj_BankAccountSupervisedRebuild;
import zhishusz.housepresell.util.rebuild.Tgpj_EscrowStandardVerMngRebuild;
import zhishusz.housepresell.util.rebuild.Tgxy_BankAccountEscrowedRebuild;

/**
 *
 */
/*
 * Controller下拉列表查询
 * Company：ZhiShuSZ
 * */
@Controller
@EnableCaching
public class ListForSelectController extends BaseController
{
	@Autowired
	private Empj_ProjectInfoListService empj_ProjectInfoListService;
	@Autowired
	private Empj_ProjectInfoRebuild empj_ProjectInfoRebuild;

	@RequestMapping(value="/Empj_ProjectInfoForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	/*@Cacheable(value="Empj_ProjectInfo", key="#model.getMD5()")*/
	public String execute(@ModelAttribute Empj_ProjectInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> empj_ProjectInfoListService.execute(model));
		return executeInterface(model, versionMap,empj_ProjectInfoRebuild,"Empj_ProjectInfo");
	}

	@Autowired
	private Empj_BuildingInfoListService empj_buildingInfoListService;
	@Autowired
	private Empj_BuildingInfoRebuild empj_buildingInfoRebuild;

	@RequestMapping(value="/Empj_BuildingInfoForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,
			RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Empj_BuildingInfo", key="#model.getMD5()")
	public String execute(@ModelAttribute Empj_BuildingInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> empj_buildingInfoListService.execute(model));
		return executeInterface(model, versionMap,empj_buildingInfoRebuild,"Empj_BuildingInfo");
	}

	@Autowired
	private Emmp_CompanyInfoSpecialListService emmp_CompanyInfoSpecialListService;
	@Autowired
	private Emmp_CompanyInfoRebuild emmp_CompanyInfoSpecialRebuild;

	@RequestMapping(value="/Emmp_CompanyInfoForSpecialSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String getCompany(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		model.setTheState(S_TheState.Normal);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> emmp_CompanyInfoSpecialListService.execute(model));
		return executeInterface(model, versionMap,emmp_CompanyInfoSpecialRebuild,"Emmp_CompanyInfo");
	}
	
	@Autowired
	private Emmp_CompanyInfoListForSelectService emmp_CompanyInfoListForSelectService;
	@Autowired
	private Emmp_CompanyInfoRebuild emmp_CompanyInfoRebuild;
	@RequestMapping(value="/Emmp_CompanyInfoForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Emmp_CompanyInfo", key="#model.getMD5()")
	public String execute(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		model.setTheState(S_TheState.Normal);

		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> emmp_CompanyInfoListForSelectService.execute(model));
		return executeInterface(model, versionMap,emmp_CompanyInfoRebuild,"Emmp_CompanyInfo");
	}

	@Autowired
	private Emmp_CompanyInfoListService emmp_CompanyInfoListService;
	//查询所有机构（列表信息）
	@RequestMapping(value="/Emmp_CompanyInfoForAllForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Emmp_CompanyInfo", key="#model.getMD5()")
	public String execute2(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		model.setTheState(S_TheState.Normal);
		
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> emmp_CompanyInfoListService.execute(model));
		return executeInterface(model, versionMap,emmp_CompanyInfoRebuild,"Emmp_CompanyInfo");
	}
	
	@Autowired
	private Sm_UserListService sm_UserListService;
	@Autowired
	private Sm_UserRebuild sm_UserRebuild;
	
	@RequestMapping(value="/Sm_UserForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	//@Cacheable(value="Sm_User", key="#model.getMD5()")
	public String execute(@ModelAttribute Sm_UserForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_UserListService.execute(model));
		return executeInterface(model, versionMap,sm_UserRebuild,"Sm_User");
	}
	
	@Autowired
	private Sm_CityRegionInfoListService sm_CityRegionInfoListService;
	@Autowired
	private Sm_CityRegionInfoRebuild sm_CityRegionInfoRebuild;

	@RequestMapping(value="/Sm_CityRegionInfoForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Sm_CityRegionInfo", key="#model.getMD5()")
	public String execute(@ModelAttribute Sm_CityRegionInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_CityRegionInfoListService.execute(model));
		return executeInterface(model, versionMap,sm_CityRegionInfoRebuild,"Sm_CityRegionInfo");
	}

	@Autowired
	private Sm_StreetInfoListService sm_StreetInfoListService;
	@Autowired
	private Sm_StreetInfoRebuild sm_StreetInfoRebuild;

	@RequestMapping(value="/Sm_StreetInfoForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Sm_StreetInfo", key="#model.getMD5()")
	public String execute(@ModelAttribute Sm_StreetInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_StreetInfoListService.execute(model));
		return executeInterface(model, versionMap,sm_StreetInfoRebuild,"Sm_StreetInfo");
	}

	@Autowired
	private Emmp_BankBranchListService emmp_BankBranchListService;
	@Autowired
	private Emmp_BankBranchRebuild emmp_BankBranchRebuild;

	@RequestMapping(value="/Emmp_BankBranchForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Emmp_BankBranch", key="#model.getMD5()")
	public String execute(@ModelAttribute Emmp_BankBranchForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		model.setTheState(S_TheState.Normal);
		versionMap.put(19000101,()-> emmp_BankBranchListService.execute(model));
		return executeInterface(model, versionMap, emmp_BankBranchRebuild,"Emmp_BankBranch");
	}

	@Autowired
	private Emmp_BankInfoListService emmp_bankInfoListService;
	@Autowired
	private Emmp_BankInfoRebuild emmp_bankInfoRebuild;

	@RequestMapping(value="/Emmp_BankInfoForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Emmp_BankInfo", key="#model.getMD5()")
	public String execute(@ModelAttribute Emmp_BankInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> emmp_bankInfoListService.execute(model));
		return executeInterface(model, versionMap, emmp_bankInfoRebuild,"Emmp_BankInfo");
	}

	@Autowired
	private Tgpj_BankAccountSupervisedListService tgpj_BankAccountSupervisedListService;
	@Autowired
	private Tgpj_BankAccountSupervisedRebuild tgpj_BankAccountSupervisedRebuild;

	@RequestMapping(value="/Tgpj_BankAccountSupervisedForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
//	@Cacheable(value="Tgpj_BankAccountSupervised", key="#model.getMD5()")
	public String execute(@ModelAttribute Tgpj_BankAccountSupervisedForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> tgpj_BankAccountSupervisedListService.execute(model));
		return executeInterface(model, versionMap, tgpj_BankAccountSupervisedRebuild,"Tgpj_BankAccountSupervised");
	}

	@Autowired
	private Sm_Permission_RoleListForSelectService sm_Permission_RoleListForSelectService;
	@Autowired
	private Sm_Permission_RoleRebuild sm_Permission_RoleRebuild;

	@RequestMapping(value="/Sm_Permission_RoleForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	/*@Cacheable(value="Sm_Permission_Role", key="#model.getMD5()")*/
	public String execute(@ModelAttribute Sm_Permission_RoleForm model, HttpServletRequest request)
	{
		model.init(request);

		Object obj = request.getSession().getAttribute("user");
		Sm_User user = (Sm_User)obj;
		if(!("TEST".equals(user.getTheAccount()) || "jssq".equals(user.getTheAccount())
				|| "hjy".equals(user.getTheAccount()) || "zry".equals(user.getTheAccount()))){
			return "无授权操作";
		}

		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_Permission_RoleListForSelectService.execute(model));
		return executeInterface(model, versionMap, sm_Permission_RoleRebuild,"Sm_Permission_Role");
	}

	@RequestMapping(value="/Sm_Permission_RoleForSelect_copy",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	/*@Cacheable(value="Sm_Permission_Role", key="#model.getMD5()")*/
	public String executeCopy(@ModelAttribute Sm_Permission_RoleForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_Permission_RoleListForSelectService.execute(model));
		return executeInterface(model, versionMap, sm_Permission_RoleRebuild,"Sm_Permission_Role");
	}

	
	@Autowired
	private Sm_Permission_UIResourceListService sm_Permission_UIResourceListService;
	@Autowired
	private Sm_Permission_UIResourceRebuild sm_Permission_UIResourceRebuild;

	@RequestMapping(value="/Sm_Permission_UIResource_UrlForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_Permission_UIResourceForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_Permission_UIResourceListService.executeUrlForSelect(model));
		return executeInterface(model, versionMap, sm_Permission_UIResourceRebuild, "Sm_Permission_UIResource");
	}

	@Autowired
	private Tgpj_EscrowStandardVerMngListService tgpj_EscrowStandardVerMngListService;
	@Autowired
	private Tgpj_EscrowStandardVerMngRebuild tgpj_EscrowStandardVerMngRebuild;

	@RequestMapping(value="/Tgpj_EscrowStandardVerMngForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgpj_EscrowStandardVerMngForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		model.setExpirationDate(System.currentTimeMillis());//筛选有效期
		versionMap.put(19000101,()-> tgpj_EscrowStandardVerMngListService.execute(model));
		return executeInterface(model, versionMap, tgpj_EscrowStandardVerMngRebuild, "Tgpj_EscrowStandardVerMng");
	}

	@Autowired
	private Tgxy_BankAccountEscrowedListService tgxy_BankAccountEscrowedListService;
	@Autowired
	private Tgxy_BankAccountEscrowedRebuild tgxy_BankAccountEscrowedRebuild;
	@RequestMapping(value="/Tgxy_BankAccountEscrowedForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	//	@Cacheable(value="Tgxy_BankAccountEscrowed", key="#model.getMD5()")
	public String execute(@ModelAttribute Tgxy_BankAccountEscrowedForm model, HttpServletRequest request)
	{
		model.init(request);
		model.setTheState(S_TheState.Normal);

		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> tgxy_BankAccountEscrowedListService.execute(model));
		return executeInterface(model, versionMap,tgxy_BankAccountEscrowedRebuild,"Tgxy_BankAccountEscrowed");
	}

	
	@Autowired
	private Sm_BaseParameterListService sm_baseParameterListService;
	@Autowired
	private Sm_BaseParameterRebuild sm_baseParameterRebuild;

	@RequestMapping(value="/Sm_BaseParameterForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Sm_BaseParameterForm model, HttpServletRequest request)
	{
		model.init(request);
		HashMap<Integer, Action> versionMap = new HashMap<>();
		versionMap.put(19000101,()-> sm_baseParameterListService.execute(model));
		return executeInterface(model, versionMap, sm_baseParameterRebuild, "Sm_BaseParameter");
	}
	
	@Autowired
	private Sm_CityRegionInfoForRangeAuthListService sm_CityRegionInfoForRangeAuthListService;
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value="/Sm_CityRegionInfoListForRangeAuthAddForSelect",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String executeForRangeAuth(@ModelAttribute Sm_CityRegionInfoForm model, HttpServletRequest request)
	{
		model.init(request);
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = sm_CityRegionInfoForRangeAuthListService.executeForSelect(model);
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
		if(MyBackInfo.isSuccess(properties))
		{
			properties.put("sm_CityRegionInfoList", sm_CityRegionInfoRebuild.getDetailForRangeAuth((List)(properties.get("sm_CityRegionInfoList")), model));
		}
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		super.writeOperateHistory("Sm_CityRegionInfoListForRangeAuthAddForSelect", model, properties, jsonStr);
		return jsonStr;
	}
	
	/**
	 * @param model 请求的form，主要是获取interfaceVersion
	 * @param versionMap 版本号对应执行方法的map，代替switch
	 * @param rebuild 需要执行的rebuild
	 * @param poName PO对象的类名
	 * @return 整个Properties转为的json字符串
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String executeInterface(@ModelAttribute BaseForm model, HashMap<Integer, Action> versionMap,
			RebuilderBase rebuild, String poName)
	{
		Properties properties = null;
		if (versionMap.containsKey(model.getInterfaceVersion()))
		{
			properties=versionMap.get(model.getInterfaceVersion()).onAction();
		}
		else
		{
			properties = setDefaultProperties();
		}
		if (MyBackInfo.isSuccess(properties))
		{
			String listName = MyString.getInstance().firstLowcase(poName) + "List";
			if(rebuild != null)
			{
				properties.put(listName, rebuild.executeForSelectList((List) (properties.get(listName))));
			}
		}
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		super.writeOperateHistory(MyString.getInstance().firstUpcase(poName)+"ForSelect", model, properties, jsonStr);
		return jsonStr;
	}

	private MyProperties setDefaultProperties()
	{
		MyProperties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.fail);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
		return properties;
	}

	private interface Action
	{
		Properties onAction();
	}

}

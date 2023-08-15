package zhishusz.housepresell.controller.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.extra.Sm_HomePageListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;
import zhishusz.housepresell.util.rebuild.Sm_FastNavigateRebuild;

/**
 * 首页展示加载
 * 
 * @ClassName: Sm_HomePageListController
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月12日 下午2:13:56
 * @version V1.0
 *
 */
@Controller
public class Sm_HomePageListController extends BaseController
{
	@Autowired
	private Sm_HomePageListService service;
	@Autowired
	private Empj_ProjectInfoRebuild rebuild;// 项目
	@Autowired
	private Sm_FastNavigateRebuild rebuild2;// 快捷导航
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/admin/Sm_HomePageList", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tb_b_contractFrom model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
		case 19000101:
		{

			Object obj = request.getSession().getAttribute("user");
			Sm_User user = (Sm_User)obj;
			Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
			sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
			sm_Permission_RoleUserForm.setSm_UserId(user.getTableId());
			List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao
					.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(),
							sm_Permission_RoleUserForm));
//			System.out.println("长度为："+sm_Permission_RoleUserList.size());
			if(sm_Permission_RoleUserList == null || sm_Permission_RoleUserList.size() <= 0 ){
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, "无授权操作");
				break;
			}

			properties = service.execute(model);

			Sm_User currentuser=model.getUser();
			Sm_Permission_RoleUserForm	smForm=new Sm_Permission_RoleUserForm();
			sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
			sm_Permission_RoleUserForm.setUser(currentuser);
			List<Sm_Permission_RoleUser> Sm_Permission_RoleUserList= sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQLByuser(), sm_Permission_RoleUserForm).getResultList();
			List<Object> lists=new ArrayList<Object>();
			List<Sm_Permission_UIResource> outSm_Permission_UIResourceList=new ArrayList<Sm_Permission_UIResource>();
			if(Sm_Permission_RoleUserList.size()>0) {
				for (Sm_Permission_RoleUser Sm_Permission_RoleUser : Sm_Permission_RoleUserList) {
					if (Sm_Permission_RoleUser.getSm_Permission_Role().getUiResourceList() != null) {
						List<Sm_Permission_UIResource> Sm_Permission_UIResourceList = Sm_Permission_RoleUser.getSm_Permission_Role().getUiResourceList();

//						for (Sm_Permission_UIResource uiResource:sm_Permission_RoleUserList) {
//
//						}
						outSm_Permission_UIResourceList.addAll(Sm_Permission_UIResourceList);
					}

				}
			}

			System.out.println("outSm_Permission_UIResourceList.length="+outSm_Permission_UIResourceList.size());




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
			properties.put("sm_FastNavigateList", rebuild2.execute((List) (properties.get("sm_FastNavigateList"))));
		}
		

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_HomePageOfProjectList", model, properties, jsonStr);

		return jsonStr;
	}


	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/admin/Sm_HomePageList_copy", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String executeCopy(@ModelAttribute Tb_b_contractFrom model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
			case 19000101:
			{
				properties = service.execute(model);
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
//			properties.put("empj_ProjectInfoList",
//					rebuild.executeForHomePageList((List) (properties.get("empj_ProjectInfoList"))));

			properties.put("sm_FastNavigateList", rebuild2.execute((List) (properties.get("sm_FastNavigateList"))));
		}


		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Sm_HomePageOfProjectList", model, properties, jsonStr);

		return jsonStr;
	}

}

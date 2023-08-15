package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：UI权限资源
 * 加载登录人菜单权限
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_Permission_UIResourceLoginListService
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;// UI资源权限
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;// 用户角色对应关系

	/**
	 * 根据登录者，获取左侧菜单静态化所需的数据
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Properties executeForSideBarInit(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (user == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}

		// 根据用户id查询用户所属角色信息
		Sm_Permission_RoleUserForm form = new Sm_Permission_RoleUserForm();
		form.setSm_UserId(user.getTableId());
		form.setTheState(S_TheState.Normal);
		sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), form));

		// 获取所有正常状态的“链接”
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_UIResourceType.TheResource);
		List<Sm_Permission_UIResource> sm_Permission_UIResourceList = sm_Permission_UIResourceDao
				.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), model));

		properties.put("sm_Permission_UIResourceList", sm_Permission_UIResourceList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		model.setKeyword("%" + keyword + "%");

		Integer totalCount = sm_Permission_UIResourceDao.findByPage_Size(
				sm_Permission_UIResourceDao.getQuery_Size(sm_Permission_UIResourceDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Sm_Permission_UIResource> sm_Permission_UIResourceList;
		if (totalCount > 0)
		{
			sm_Permission_UIResourceList = sm_Permission_UIResourceDao.findByPage(
					sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			sm_Permission_UIResourceList = new ArrayList<Sm_Permission_UIResource>();
		}

		properties.put("sm_Permission_UIResourceList", sm_Permission_UIResourceList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

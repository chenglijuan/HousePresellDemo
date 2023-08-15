package test.initPermission;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

/**
 * 初始化指定某个角色拥有当前系统的所有（菜单和按钮权限） 
 * @author http://zhishusz
 */
@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitSm_Role_UIResource_Rel extends BaseJunitTest
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	/**
	 * 指定某个角色拥有所有权限（用于初始化）
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void execute() throws Exception 
	{
		Long roleId = 1L;//角色Id
		
		//========================== 查询角色 信息 Start =========================================//
		Sm_Permission_Role sm_Permission_Role = sm_Permission_RoleDao.findById(roleId);
		if(sm_Permission_Role == null)
		{
			System.out.println("该角色不存在");
		}
		//========================== 查询角色 信息 End ==========================================//
		
		//查询所有菜单信息和按钮信息
		Sm_Permission_UIResourceForm menuForm = new Sm_Permission_UIResourceForm();
		menuForm.setTheState(S_TheState.Normal);
		List<Sm_Permission_UIResource> menuCheckList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getHQLForRoleUIAddOrUpdate(), menuForm));

		Sm_Permission_UIResourceForm btnForm = new Sm_Permission_UIResourceForm();
		btnForm.setTheState(S_TheState.Normal);
		List<Sm_Permission_UIResource> btnCheckList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getHQLForRoleUIAddOrUpdate(), btnForm));

		//将菜单信息和按钮信息合并List
		menuCheckList.addAll(btnCheckList);
		sm_Permission_Role.setUiResourceList(menuCheckList);
		sm_Permission_RoleDao.save(sm_Permission_Role);
	}
}

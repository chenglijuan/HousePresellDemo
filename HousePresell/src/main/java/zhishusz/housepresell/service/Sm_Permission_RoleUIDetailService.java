package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUIForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleUIDetailService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;

	public Properties execute(Sm_Permission_RoleUIForm model)
	{
		Properties properties = new MyProperties();

		Long sm_Permission_RoleUIId = model.getTableId();
		Sm_Permission_Role sm_Permission_RoleUI = (Sm_Permission_Role)sm_Permission_RoleDao.findById(sm_Permission_RoleUIId);
		if(sm_Permission_RoleUI == null || S_TheState.Deleted.equals(sm_Permission_RoleUI.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_Permission_RoleUI(Id:" + sm_Permission_RoleUIId + ")'不存在");
		}
		
		//查询已经给该角色分配的菜单Id
		List<Long> menuCheckArr = new ArrayList<Long>(); 
		//查询已经给该角色分配的按钮Id
		List<Long> btnCheckArr = new ArrayList<Long>();
		List<Sm_Permission_UIResource> uiResourceList = sm_Permission_RoleUI.getUiResourceList();
		for(Sm_Permission_UIResource uiResource : uiResourceList)
		{
			if(S_UIResourceType.VirtualMenu.equals(uiResource.getTheType()) || S_UIResourceType.RealityMenu.equals(uiResource.getTheType()))
			{
				menuCheckArr.add(uiResource.getTableId());
			}
			else if(S_UIResourceType.Button.equals(uiResource.getTheType()))
			{
				btnCheckArr.add(uiResource.getTableId());
			}
		}
		
		properties.put("menuCheckArr", menuCheckArr);
		properties.put("btnCheckArr", btnCheckArr);
		properties.put("sm_Permission_RoleUI", sm_Permission_RoleUI);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

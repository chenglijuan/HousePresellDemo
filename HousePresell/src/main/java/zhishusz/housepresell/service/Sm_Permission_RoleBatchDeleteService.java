package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RoleBusiType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：管理角色
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleBatchDeleteService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "请选择角色");
		}

		for(Long tableId : idArr)
		{
			Sm_Permission_Role sm_Permission_Role = (Sm_Permission_Role)sm_Permission_RoleDao.findById(tableId);
			if(sm_Permission_Role == null)
			{
				return MyBackInfo.fail(properties, "该角色信息不存在");
			}

			String busiType = sm_Permission_Role.getBusiType();//是否启用：（1:否 ，0：是)
			if(S_RoleBusiType.Valid.equals(busiType))
			{
				throw new RoolBackException("角色"+sm_Permission_Role.getTheName()+"已启用，请先停用后再删除");
			}
		
			Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
			sm_Permission_RoleUserForm.setSm_Permission_RoleId(tableId);
			sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
			List<Sm_Permission_RoleUser> sm_Permission_RoleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), sm_Permission_RoleUserForm));
			if(sm_Permission_RoleUserList != null && !sm_Permission_RoleUserList.isEmpty())
			{
				return MyBackInfo.fail(properties, "角色"+sm_Permission_Role.getTheName()+"有用户正在使用不能删除");
			}
			sm_Permission_Role.setTheState(S_TheState.Deleted);
			sm_Permission_RoleDao.save(sm_Permission_Role);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

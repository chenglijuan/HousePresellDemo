package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleDataForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDataDao;
import zhishusz.housepresell.database.po.Sm_Permission_RoleData;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleDataDetailService
{
	@Autowired
	private Sm_Permission_RoleDataDao sm_Permission_RoleDataDao;

	public Properties execute(Sm_Permission_RoleDataForm model)
	{
		Properties properties = new MyProperties();

		Long sm_Permission_RoleDataId = model.getTableId();
		Sm_Permission_RoleData sm_Permission_RoleData = (Sm_Permission_RoleData)sm_Permission_RoleDataDao.findById(sm_Permission_RoleDataId);
		if(sm_Permission_RoleData == null || S_TheState.Deleted.equals(sm_Permission_RoleData.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_Permission_RoleData(Id:" + sm_Permission_RoleDataId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_Permission_RoleData", sm_Permission_RoleData);

		return properties;
	}
}

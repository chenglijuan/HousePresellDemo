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
 * Service批量删除：角色与数据权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RoleDataBatchDeleteService
{
	@Autowired
	private Sm_Permission_RoleDataDao sm_Permission_RoleDataDao;

	public Properties execute(Sm_Permission_RoleDataForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_Permission_RoleData sm_Permission_RoleData = (Sm_Permission_RoleData)sm_Permission_RoleDataDao.findById(tableId);
			if(sm_Permission_RoleData == null)
			{
				return MyBackInfo.fail(properties, "'Sm_Permission_RoleData(Id:" + tableId + ")'不存在");
			}
		
			sm_Permission_RoleData.setTheState(S_TheState.Deleted);
			sm_Permission_RoleDataDao.save(sm_Permission_RoleData);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

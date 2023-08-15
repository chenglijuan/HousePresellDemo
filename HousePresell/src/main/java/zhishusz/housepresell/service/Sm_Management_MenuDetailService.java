package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情查询：菜单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Management_MenuDetailService
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	public Properties execute(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();
		
		Long tableId = model.getTableId();
		Sm_Permission_UIResource sm_Permission_UIResource = sm_Permission_UIResourceDao.findById(tableId);
		
		if(sm_Permission_UIResource == null)
		{
			return MyBackInfo.fail(properties, "请选择菜单");
		}
		
		properties.put("sm_Permission_UIResource", sm_Permission_UIResource);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

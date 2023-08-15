package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_UIResourceForm;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：菜单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Management_MenuListService
{
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_UIResourceForm model)
	{
		Properties properties = new MyProperties();
		
		//目前先获取所有菜单
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_UIResourceType.Menu);
		model.setOrderBy("theIndex,tableId asc");
		List<Sm_Permission_UIResource> sm_Management_MenuList = sm_Permission_UIResourceDao.findByPage(sm_Permission_UIResourceDao.getQuery(sm_Permission_UIResourceDao.getBasicHQL(), model));
		
		properties.put("sm_Management_MenuList", sm_Management_MenuList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

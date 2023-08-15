package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_HouseInfoBuildingecoListsService {
	
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();	
		model.setTheState(S_TheState.Normal);				 
		List<Empj_BuildingInfo>  empj_BuildingInfoList=  empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), model).getResultList();
		properties.put("empj_BuildingInfoList", empj_BuildingInfoList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

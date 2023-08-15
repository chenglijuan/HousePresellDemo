package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_HouseInfoProjectNameListsService {
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
		
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();
		
		model.setTheState(S_TheState.Normal);
		
		List<Empj_ProjectInfo>  empj_ProjectInfoList=  empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), model).getResultList();		 		
		
		properties.put("empj_ProjectInfoList", empj_ProjectInfoList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：角色授权添加页面--加载 区域 或者 区域、项目 或者 区域项目楼幢
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_CityRegionInfoForRangeAuthListService
{
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties executeForSelect(Sm_CityRegionInfoForm model)
	{
		Properties properties = new MyProperties();

		model.setTheState(S_TheState.Normal);
		List<Sm_CityRegionInfo> sm_CityRegionInfoList = sm_CityRegionInfoDao.findByPage(sm_CityRegionInfoDao.getQuery(sm_CityRegionInfoDao.getBasicHQL(), model));
        
		properties.put("sm_CityRegionInfoList", sm_CityRegionInfoList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}

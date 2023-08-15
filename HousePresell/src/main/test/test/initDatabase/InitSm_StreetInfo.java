package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitSm_StreetInfo extends BaseJunitTest 
{
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化街道信息
		Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(5l);
		Sm_StreetInfo sm_StreetInfo = new Sm_StreetInfo();
		sm_StreetInfo.setCityRegion(sm_CityRegionInfo);
		sm_StreetInfo.setCreateTimeStamp(System.currentTimeMillis());
		sm_StreetInfo.setTheName("竹林西路");
		sm_StreetInfo.setTheState(S_TheState.Normal);
		sm_StreetInfoDao.save(sm_StreetInfo);
	}
}

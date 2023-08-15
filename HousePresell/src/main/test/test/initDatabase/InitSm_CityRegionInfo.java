package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitSm_CityRegionInfo extends BaseJunitTest 
{
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化行政区划地区信息
		String[] areaNameStrArr = {"10_金坛区", "20_武进区", "21_经开区", "30_新北区", "40_天宁区", "50_钟楼区", "60_溧阳区"};
		for(int i=0;i<areaNameStrArr.length;i++)
		{
			String[] codeNameArr = areaNameStrArr[i].split("_");
			
			Sm_CityRegionInfo sm_CityRegionInfo = new Sm_CityRegionInfo();
			sm_CityRegionInfo.setBusiState("业务状态");
			sm_CityRegionInfo.setCreateTimeStamp(System.currentTimeMillis());
			sm_CityRegionInfo.seteCode(codeNameArr[0]);
			sm_CityRegionInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
			sm_CityRegionInfo.setRecordTimeStamp(System.currentTimeMillis());
			sm_CityRegionInfo.setTheName(codeNameArr[1]);
			sm_CityRegionInfo.setTheState(S_TheState.Normal);
			
			sm_CityRegionInfoDao.save(sm_CityRegionInfo);
		}
	}
}

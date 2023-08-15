package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmpj_UnitInfo extends BaseJunitTest 
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化单元信息
		Empj_UnitInfo empj_UnitInfo = new Empj_UnitInfo();
		
		empj_UnitInfo.setBuilding(empj_BuildingInfoDao.findById(11440L));
		empj_UnitInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_UnitInfo.setDownfloorHouseHoldNumber(8);
		empj_UnitInfo.setDownfloorNumber(3.0);
		empj_UnitInfo.setTheName("一单元");
		empj_UnitInfo.setTheState(S_TheState.Normal);
		empj_UnitInfo.setUpfloorHouseHoldNumber(8);
		empj_UnitInfo.setUpfloorNumber(3.0);
		
		empj_UnitInfoDao.save(empj_UnitInfo);
	}
}

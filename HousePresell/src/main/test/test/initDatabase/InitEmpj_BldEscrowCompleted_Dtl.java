package test.initDatabase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmpj_BldEscrowCompleted_Dtl extends BaseJunitTest 
{
	@Autowired
	private Empj_BldEscrowCompleted_DtlDao empj_BldEscrowCompleted_DtlDao;
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化托管终止明细
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(1l);
		Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl = new Empj_BldEscrowCompleted_Dtl();
		empj_BldEscrowCompleted_Dtl.setBuilding(empj_BuildingInfo);
		empj_BldEscrowCompleted_Dtl.setTheState(S_TheState.Normal);
		empj_BldEscrowCompleted_Dtl.setCreateTimeStamp(System.currentTimeMillis());
		
		empj_BldEscrowCompleted_DtlDao.save(empj_BldEscrowCompleted_Dtl);
		
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList = new ArrayList<Empj_BldEscrowCompleted_Dtl>();
		empj_BldEscrowCompleted_DtlList.add(empj_BldEscrowCompleted_Dtl);
		
		Empj_BldEscrowCompleted empj_BldEscrowCompleted = new Empj_BldEscrowCompleted();
		empj_BldEscrowCompleted.setEmpj_BldEscrowCompleted_DtlList(empj_BldEscrowCompleted_DtlList);
		empj_BldEscrowCompleted.setTheState(S_TheState.Normal);
		empj_BldEscrowCompleted.setCreateTimeStamp(System.currentTimeMillis());
		empj_BldEscrowCompletedDao.save(empj_BldEscrowCompleted);
		
		empj_BldEscrowCompleted_Dtl.setMainTable(empj_BldEscrowCompleted);
		empj_BldEscrowCompleted_DtlDao.save(empj_BldEscrowCompleted_Dtl);
	}
}

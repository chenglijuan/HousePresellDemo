package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgpf_BuildingRemainRightLog extends BaseJunitTest 
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = new Tgpf_BuildingRemainRightLog();
		tgpf_BuildingRemainRightLog.setBuilding(empj_BuildingInfoDao.findById(1L));
		tgpf_BuildingRemainRightLog.setSrcBusiType("测试");
		tgpf_BuildingRemainRightLog.setBillTimeStamp("2018-09-21");
		tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
	}
}

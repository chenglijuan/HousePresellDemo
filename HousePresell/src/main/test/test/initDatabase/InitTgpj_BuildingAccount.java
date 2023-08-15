package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgpj_BuildingAccount extends BaseJunitTest 
{
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化楼幢账户
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(11440l);
		Tgpj_BuildingAccount tgpj_BuildingAccount = new Tgpj_BuildingAccount();
		
		tgpj_BuildingAccount.setBuilding(empj_BuildingInfo);
		tgpj_BuildingAccount.setTheState(S_TheState.Normal);
		tgpj_BuildingAccount.setCreateTimeStamp(System.currentTimeMillis());
		tgpj_BuildingAccount.setTotalAccountAmount(1600.0);
		tgpj_BuildingAccount.setEffectiveLimitedAmount(1600.0);
		tgpj_BuildingAccount.setActualAmount(1600.0);
		
		tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
	}
}

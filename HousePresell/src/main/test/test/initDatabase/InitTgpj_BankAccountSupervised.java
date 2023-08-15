package test.initDatabase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgpj_BankAccountSupervised extends BaseJunitTest 
{
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化监管账号
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(1l);
		Tgpj_BankAccountSupervised tgpj_BankAccountSupervised = new Tgpj_BankAccountSupervised();
		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		empj_BuildingInfoList.add(empj_BuildingInfo);
		tgpj_BankAccountSupervised.setTheState(S_TheState.Normal);
		tgpj_BankAccountSupervised.setCreateTimeStamp(System.currentTimeMillis());
		tgpj_BankAccountSupervised.seteCode("JGZH20180914");
		tgpj_BankAccountSupervised.setTheName("客户交易结算资金");
		tgpj_BankAccountSupervisedDao.save(tgpj_BankAccountSupervised);
		
		List<Tgpj_BankAccountSupervised> tgpj_BankAccountSupervisedList = new ArrayList<Tgpj_BankAccountSupervised>();
		tgpj_BankAccountSupervisedList.add(tgpj_BankAccountSupervised);
		empj_BuildingInfoDao.save(empj_BuildingInfo);
	}
}

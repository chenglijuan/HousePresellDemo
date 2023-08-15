package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgxy_ContractInfo extends BaseJunitTest 
{
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化合同
		Empj_HouseInfo empj_HouseInfo = empj_HouseInfoDao.findById(1l);
		
		Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
		
		tgxy_ContractInfo.seteCodeOfContractRecord("HSGBH20180914");
		tgxy_ContractInfo.setBusiState("正常");
		tgxy_ContractInfo.setContractSumPrice(1000000.0);
		tgxy_ContractInfo.setContractSignDate("2018-09-14");
		tgxy_ContractInfo.setPaymentMethod("贷款");
		tgxy_ContractInfo.setFirstPaymentAmount(300000.0);
		tgxy_ContractInfo.setLoanAmount(700000.0);
		tgxy_ContractInfo.setPayDate("2020-01-01");
		tgxy_ContractInfo.setHouseInfo(empj_HouseInfo);
		
		tgxy_ContractInfoDao.save(tgxy_ContractInfo);
	}
}

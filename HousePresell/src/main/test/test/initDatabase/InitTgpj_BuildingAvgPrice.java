package test.initDatabase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgpj_BuildingAvgPrice extends BaseJunitTest 
{
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化备案均价
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(1l);
		Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = new Tgpj_BuildingAvgPrice();
		tgpj_BuildingAvgPrice.setBuildingInfo(empj_BuildingInfo);
		tgpj_BuildingAvgPrice.setTheState(S_TheState.Normal);
		tgpj_BuildingAvgPrice.setCreateTimeStamp(System.currentTimeMillis());
		tgpj_BuildingAvgPrice.setRecordAveragePrice(10000.0);
		tgpj_BuildingAvgPrice.seteCode("LZZZBA20180917");
		tgpj_BuildingAvgPriceDao.save(tgpj_BuildingAvgPrice);
	}
}

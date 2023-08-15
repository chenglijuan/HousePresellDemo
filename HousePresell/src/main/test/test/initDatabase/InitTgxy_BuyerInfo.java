package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgxy_BuyerInfo extends BaseJunitTest 
{
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化用户
		Empj_HouseInfo empj_HouseInfo = empj_HouseInfoDao.findById(1l);
		
		Tgxy_BuyerInfo tgxy_BuyerInfo = new Tgxy_BuyerInfo();
		tgxy_BuyerInfo.setTheState(S_TheState.Normal);
		tgxy_BuyerInfo.setBuyerName("张三");
		tgxy_BuyerInfo.setContactPhone("15162327001");
		tgxy_BuyerInfo.seteCodeOfcertificate("320582199109062001");
		tgxy_BuyerInfo.setHouseInfo(empj_HouseInfo);
		
		tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);
	}
}

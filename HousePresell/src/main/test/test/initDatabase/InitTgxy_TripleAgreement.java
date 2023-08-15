package test.initDatabase;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTgxy_TripleAgreement extends BaseJunitTest 
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	@SuppressWarnings("unchecked")
	@Test
	public void execute() throws Exception 
	{
		//初始化三方协议
		Empj_HouseInfoForm model = new Empj_HouseInfoForm();
		model.setBuildingId(11440l);
		List<Empj_HouseInfo> empj_HouseInfoList = empj_HouseInfoDao.findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getBuildingHQL(), model));
		
		for(Empj_HouseInfo empj_HouseInfo : empj_HouseInfoList)
		{
			Tgxy_TripleAgreement tgxy_TripleAgreement = new Tgxy_TripleAgreement();
			
			tgxy_TripleAgreement.seteCodeOfTripleAgreement("SFXY20180914");
			tgxy_TripleAgreement.setTripleAgreementTimeStamp("2018-09-14");
			tgxy_TripleAgreement.seteCodeOfContractRecord("HTBA20180914");
			tgxy_TripleAgreement.setTheStateOfTripleAgreement("已上传");
			tgxy_TripleAgreement.setTheStateOfTripleAgreementFiling("已扫描");
			tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect("生效");
			tgxy_TripleAgreement.setHouse(empj_HouseInfo);
			tgxy_TripleAgreement.setTheState(S_TheState.Normal);
			tgxy_TripleAgreement.setCreateTimeStamp(System.currentTimeMillis());
			tgxy_TripleAgreement.setTotalAmountOfHouse(100.0);
			
			tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
		}
	}
}

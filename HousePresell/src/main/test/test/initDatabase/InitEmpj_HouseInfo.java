package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyInteger;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmpj_HouseInfo extends BaseJunitTest 
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化户室信息
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(11440L);
		Empj_UnitInfo empj_UnitInfo = empj_UnitInfoDao.findById(10000L);
		
		/*Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
		empj_HouseInfo.setBuilding(empj_BuildingInfo);
		empj_HouseInfo.setUnitInfo(empj_UnitInfo);
		empj_HouseInfo.seteCodeOfUnitInfo(empj_UnitInfo.getTheName());
		empj_HouseInfo.setRoomId("503");
		empj_HouseInfo.setLimitState(3);
		empj_HouseInfo.setBusiState("已办产权");//已搭建0，已批准预售1，合同已签订2，合同已备案3，已办产权4
		empj_HouseInfo.setTheState(S_TheState.Normal);
		empj_HouseInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_HouseInfo.setFloor(5.0);
		empj_HouseInfo.setRowSpan(1);
		empj_HouseInfo.setColSpan(1);
		empj_HouseInfo.setRowNumber(4);
		empj_HouseInfo.setColNumber(3);
		
		empj_HouseInfoDao.save(empj_HouseInfo);*/
		
		for(int i=1;i<5;i++)	
		{
			for(int j=1;j<5;j++)
			{
				Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
				
				empj_HouseInfo.setBuilding(empj_BuildingInfo);
				empj_HouseInfo.setUnitInfo(empj_UnitInfo);
				empj_HouseInfo.seteCodeOfUnitInfo(empj_UnitInfo.getTheName());
				empj_HouseInfo.setRoomId(i+"0"+j);
				empj_HouseInfo.setLimitState(2);
				empj_HouseInfo.setBusiState("已搭建");
				empj_HouseInfo.setTheState(S_TheState.Normal);
				empj_HouseInfo.setCreateTimeStamp(System.currentTimeMillis());
				empj_HouseInfo.setFloor(MyDouble.getInstance().parse(i));
				empj_HouseInfo.setRowSpan(1);
				empj_HouseInfo.setColSpan(1);
				empj_HouseInfo.setRowNumber(i);
				empj_HouseInfo.setColNumber(j);
				
				empj_HouseInfoDao.save(empj_HouseInfo);
			}
		}
		//updateUnitInfo();
	}
	
	public void updateUnitInfo()
	{
		Empj_HouseInfo empj_HouseInfo = empj_HouseInfoDao.findById(1L);
//		empj_HouseInfo.setActualArea(100.0);//建筑面积
//		empj_HouseInfo.setShareConsArea(200.0);//分摊面积
//		empj_HouseInfo.setInnerconsArea(100.0);//套内
//		empj_HouseInfo.setPurpose("商品房");
//		empj_HouseInfo.setContractInfo(tgxy_ContractInfoDao.findById(1l));
		empj_HouseInfo.setTripleAgreement(tgxy_TripleAgreementDao.findById(1l));
		empj_HouseInfo.setSettlementStateOfTripleAgreement(1);
		empj_HouseInfoDao.save(empj_HouseInfo);
	}
}

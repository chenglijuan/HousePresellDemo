package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
	
/*
 * Service添加操作：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_UnitInfoAddService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	private static final String BUSI_CODE = "03010204";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BuildingInfoId = model.getTableId();
		
		Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(empj_BuildingInfoId);
		if(empj_BuildingInfo == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "该楼幢信息有误！");
		}
		
		
		String eCodeOfBuilding = empj_BuildingInfo.geteCodeFromConstruction();// 楼幢编号
		String theName = model.getTheName();		//单元名称
		Double upfloorNumber = model.getUpfloorNumber();	//地上楼层数
		Integer upfloorHouseHoldNumber = model.getUpfloorHouseHoldNumber();	//地上每层户数
		Double downfloorNumber = model.getDownfloorNumber();//地下楼层数
		Integer downfloorHouseHoldNumber = model.getDownfloorHouseHoldNumber();//地下每层户数
		Integer elevatorNumber = model.getElevatorNumber();//电梯数
		Boolean hasSecondaryWaterSupply = true ;//有无二次供水
		if(null == model.getSecondaryWaterSupply()|| 0 == model.getSecondaryWaterSupply())
		{
			hasSecondaryWaterSupply = false;
		}
		model.getHasSecondaryWaterSupply();
		String remark = model.getRemark();//备注
//		Long logId = model.getLogId();
		
		
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "单元名称不能为空");
		}
		if(upfloorNumber == null || upfloorNumber < 0)
		{
			return MyBackInfo.fail(properties, "地上层数不能为空");
		}
		if(upfloorHouseHoldNumber == null || upfloorHouseHoldNumber < 0)
		{
			return MyBackInfo.fail(properties, "地上每层户数不能为空");
		}
		if(downfloorNumber == null || downfloorNumber < 0)
		{
			return MyBackInfo.fail(properties, "地下层数不能为空");
		}
		if(downfloorHouseHoldNumber == null || downfloorHouseHoldNumber < 0)
		{
			return MyBackInfo.fail(properties, "地下每层户数不能为空");
		}
		if(elevatorNumber == null || elevatorNumber < 0)
		{
			return MyBackInfo.fail(properties, "有无电梯不能为空");
		}
		if(hasSecondaryWaterSupply == null)
		{
			return MyBackInfo.fail(properties, "是否二次供水不能为空");
		}
//		if(remark == null || remark.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "备注不能为空");
//		}
//		if(logId == null || logId < 1)
//		{
//			return MyBackInfo.fail(properties, "'logId'不能为空");
//		}
		
		// 修改楼幢信息表中的单元数
		int unitNumber =  0 ;
		if(null == empj_BuildingInfo.getUnitNumber() || 0 == empj_BuildingInfo.getUnitNumber())
		{
			unitNumber = 0;
		}
		else
		{
			unitNumber = empj_BuildingInfo.getUnitNumber();
		}
		
		empj_BuildingInfo.setUnitNumber(unitNumber+1);		
		empj_BuildingInfoDao.save(empj_BuildingInfo);
		
		Sm_User user = model.getUser();
	
		Empj_UnitInfo empj_UnitInfo = new Empj_UnitInfo();
		empj_UnitInfo.setTheState(S_TheState.Normal);
//		empj_UnitInfo.setBusiState(busiState);
		empj_UnitInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		empj_UnitInfo.setUserStart(user);
		empj_UnitInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_UnitInfo.setUserUpdate(user);
		empj_UnitInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
//		empj_UnitInfo.setUserRecord(userRecord);
//		empj_UnitInfo.setRecordTimeStamp(recordTimeStamp);
		empj_UnitInfo.setBuilding(empj_BuildingInfo);
		empj_UnitInfo.seteCodeOfBuilding(eCodeOfBuilding);
		empj_UnitInfo.setTheName(theName);
		empj_UnitInfo.setUpfloorNumber(upfloorNumber);
		empj_UnitInfo.setUpfloorHouseHoldNumber(upfloorHouseHoldNumber);
		empj_UnitInfo.setDownfloorNumber(downfloorNumber);
		empj_UnitInfo.setDownfloorHouseHoldNumber(downfloorHouseHoldNumber);
		empj_UnitInfo.setElevatorNumber(elevatorNumber);
		empj_UnitInfo.setHasSecondaryWaterSupply(hasSecondaryWaterSupply);
		empj_UnitInfo.setRemark(remark);
//		empj_UnitInfo.setLogId(logId);
		empj_UnitInfoDao.save(empj_UnitInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

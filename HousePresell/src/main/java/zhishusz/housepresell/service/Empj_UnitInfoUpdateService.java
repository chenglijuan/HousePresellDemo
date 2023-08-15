package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Service更新操作：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_UnitInfoUpdateService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();
		
//		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String theName = model.getTheName();
		Double upfloorNumber = model.getUpfloorNumber();
		Integer upfloorHouseHoldNumber = model.getUpfloorHouseHoldNumber();
		Double downfloorNumber = model.getDownfloorNumber();
		Integer downfloorHouseHoldNumber = model.getDownfloorHouseHoldNumber();
		Integer elevatorNumber = model.getElevatorNumber();
		Boolean hasSecondaryWaterSupply = true ;//有无二次供水
		if(null == model.getSecondaryWaterSupply()|| 0 == model.getSecondaryWaterSupply())
		{
			hasSecondaryWaterSupply = false;
		}
		String remark = model.getRemark();
//		Long logId = model.getLogId();
				
//		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
//		}
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
//			return MyBackInfo.fail(properties, "'remark'不能为空");
//		}
//		if(logId == null || logId < 1)
//		{
//			return MyBackInfo.fail(properties, "'logId'不能为空");
//		}
		
		Long empj_UnitInfoId = model.getTableId();
		Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);
		if(empj_UnitInfo == null)
		{
			return MyBackInfo.fail(properties, "'Empj_UnitInfo(Id:" + empj_UnitInfoId + ")'不存在");
		}
		
		Sm_User user = model.getUser();
		
		empj_UnitInfo.setTheState(S_TheState.Normal);
		empj_UnitInfo.setUserUpdate(user);
		empj_UnitInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
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

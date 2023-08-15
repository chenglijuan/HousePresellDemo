package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：楼幢-单元-自动添加户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_UnitInfoAutoHouseAddService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	MyDouble mydouble = MyDouble.getInstance();
	
	private static final String BUSI_CODE = "03010205";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();

		Long empj_UnitInfoId = model.getTableId();
		Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);
		if(empj_UnitInfo == null || S_TheState.Deleted.equals(empj_UnitInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "单元信息不存在");
		}
		
		Empj_HouseInfoForm houseInfoForm = new Empj_HouseInfoForm();
		houseInfoForm.setUnitInfo(empj_UnitInfo);
		houseInfoForm.setTheState(S_TheState.Normal);
		
		Integer totalCount = empj_HouseInfoDao.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getBasicHQL(), houseInfoForm));
		
		if(totalCount > 0)
		{
			return MyBackInfo.fail(properties, "单元下已存在户室！");
		}
		
		int upfloorNumber = mydouble.getShort(empj_UnitInfo.getUpfloorNumber(), 0).intValue();	//地上楼层数
		int upfloorHouseHoldNumber = empj_UnitInfo.getUpfloorHouseHoldNumber();	//地上每层户数
		int downfloorNumber = mydouble.getShort(empj_UnitInfo.getDownfloorNumber(), 0).intValue();//地下楼层数
		int downfloorHouseHoldNumber = empj_UnitInfo.getDownfloorHouseHoldNumber();//地下每层户数
		
		
		List<Empj_HouseInfoForm> empj_HouseInfoListUpfloor = JSON.parseArray(model.getEmpj_HouseInfoListUpfloor(), Empj_HouseInfoForm.class);
		List<Empj_HouseInfoForm> empj_HouseInfoListDownfloor = JSON.parseArray(model.getEmpj_HouseInfoListDownfloor(), Empj_HouseInfoForm.class);
		
		for(int i = 0;i < empj_HouseInfoListUpfloor.size(); i++)
		{
			Empj_HouseInfoForm empj_HouseInfoForm = empj_HouseInfoListUpfloor.get(i);
			
			if( null == empj_HouseInfoForm.getRoomId()|| empj_HouseInfoForm.getRoomId().length() == 0)
			{
				return MyBackInfo.fail(properties, "户室号错误");
			}			
		}
		for(int i = 0;i < empj_HouseInfoListDownfloor.size(); i++)
		{
			Empj_HouseInfoForm empj_HouseInfoForm = empj_HouseInfoListDownfloor.get(i);
			
			if( null == empj_HouseInfoForm.getRoomId()|| empj_HouseInfoForm.getRoomId().length() == 0)
			{
				return MyBackInfo.fail(properties, "户室号错误");
			}			
		}
		
		Empj_BuildingInfo empj_BuildingInfo = empj_UnitInfo.getBuilding();
		
		// 修改楼幢信息表中的总户数
		int sumFamilyNumber =  0 ;
		if(null == empj_BuildingInfo.getSumFamilyNumber() || 0 == empj_BuildingInfo.getSumFamilyNumber())
		{
			sumFamilyNumber = upfloorNumber * upfloorHouseHoldNumber + downfloorNumber * downfloorHouseHoldNumber;
		}
		else
		{
			sumFamilyNumber = empj_BuildingInfo.getSumFamilyNumber()+ upfloorNumber * upfloorHouseHoldNumber + downfloorNumber * downfloorHouseHoldNumber;
		}
		
		empj_BuildingInfo.setSumFamilyNumber(sumFamilyNumber);		
		empj_BuildingInfoDao.save(empj_BuildingInfo);
		
		if( upfloorNumber > 0 && upfloorHouseHoldNumber > 0)
		{
			for(int j = 0;j < upfloorNumber; j++) // 楼层循环
			{
				for(int i = 0;i < upfloorHouseHoldNumber ; i++) // 户数循环
				{
					int roomId = 0;
					Double forecastArea = 0.0;
					String purpose = " " ;
										
					for(int k = 0;k < empj_HouseInfoListUpfloor.size(); k++)
					{
						Empj_HouseInfoForm empj_HouseInfoForm = empj_HouseInfoListUpfloor.get(k);
						
						if( null == empj_HouseInfoForm.getRoomId()|| empj_HouseInfoForm.getRoomId().length() == 0)
						{
							continue;
						}
						
						roomId = Integer.parseInt(empj_HouseInfoForm.getRoomId().substring(empj_HouseInfoForm.getRoomId().length()-1));
						
						if( roomId == i+1)
						{
							forecastArea = empj_HouseInfoForm.getForecastArea();
							purpose = empj_HouseInfoForm.getPurpose();
							break;
						}						
					}
					Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
					empj_HouseInfo.setFloor(new Double((j+1)));
					empj_HouseInfo.setRoomId((j+1)+"0"+(i+1));				
					empj_HouseInfo.setPurpose("0");	
					empj_HouseInfo.setForecastArea(forecastArea);
					
					saveHouseInfo(empj_HouseInfo,empj_UnitInfo,user);
				}	
			}	
		}
		
		if( downfloorNumber > 0 && downfloorHouseHoldNumber > 0)
		{
			for(int j = 0;j < downfloorNumber; j++)
			{
				for(int i = 0;i < downfloorHouseHoldNumber ; i++)
				{
					int roomId = 0;
					Double forecastArea = 0.0;
					String purpose = " " ;
										
					for(int k = 0;k < empj_HouseInfoListDownfloor.size(); k++)
					{
						Empj_HouseInfoForm empj_HouseInfoForm = empj_HouseInfoListDownfloor.get(k);
						
						if( null == empj_HouseInfoForm.getRoomId()|| empj_HouseInfoForm.getRoomId().length() == 0)
						{
							continue;
						}
						
						roomId = Integer.parseInt(empj_HouseInfoForm.getRoomId().substring(empj_HouseInfoForm.getRoomId().length()-1));
						
						if( roomId == i+1)
						{
							forecastArea = empj_HouseInfoForm.getForecastArea();
							purpose = empj_HouseInfoForm.getPurpose();
							break;
						}						
					}
					
					Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
					empj_HouseInfo.setFloor(new Double((j+1)));
					empj_HouseInfo.setRoomId("地下室"+(j+1)+"0"+(i+1));
					empj_HouseInfo.setPurpose("0");
					empj_HouseInfo.setForecastArea(forecastArea);
					
					saveHouseInfo(empj_HouseInfo,empj_UnitInfo,user);			
				}	
			}	
		}
		

		

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	public void saveHouseInfo(Empj_HouseInfo houseInfo , Empj_UnitInfo empj_UnitInfo ,Sm_User user)
	{
		Empj_BuildingInfo building = empj_UnitInfo.getBuilding();
		
		String buildingPosition = "";
		String unitName = "";
		String roomId = "";
		if(null != building.getPosition())
		{
			buildingPosition = building.getPosition();
		}
		if(null != empj_UnitInfo.getTheName())
		{
			unitName = empj_UnitInfo.getTheName();
		}
		if(null != houseInfo.getRoomId())
		{
			roomId = houseInfo.getRoomId();
		}
		String position = buildingPosition + unitName + roomId;
		
		Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
		empj_HouseInfo.setTheState(S_TheState.Normal);
//		empj_HouseInfo.setBusiState(0);
		empj_HouseInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		empj_HouseInfo.setUserStart(user);
		empj_HouseInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_HouseInfo.setUserUpdate(user);
		empj_HouseInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		empj_HouseInfo.setBuilding(building);
		empj_HouseInfo.seteCodeOfBuilding(building.geteCode());
		empj_HouseInfo.setUnitInfo(empj_UnitInfo);
		empj_HouseInfo.seteCodeOfUnitInfo(empj_UnitInfo.geteCode());
//		empj_HouseInfo.seteCodeFromPresellSystem(building);
//		empj_HouseInfo.seteCodeFromEscrowSystem(eCodeFromEscrowSystem);
		empj_HouseInfo.seteCodeFromPublicSecurity(building.geteCodeFromPresellSystem());
//		empj_HouseInfo.setAddressFromPublicSecurity(building.getAddressFromPublicSecurity());
//		empj_HouseInfo.setRecordPrice(recordPrice);
//		empj_HouseInfo.setLastTimeStampSyncRecordPriceToPresellSystem(lastTimeStampSyncRecordPriceToPresellSystem);
//		empj_HouseInfo.setSettlementStateOfTripleAgreement(settlementStateOfTripleAgreement);
//		empj_HouseInfo.setTripleAgreement(tripleAgreement);
		empj_HouseInfo.seteCodeFromPresellCert(building.geteCodeFromPresellCert());
		empj_HouseInfo.setFloor(houseInfo.getFloor());
		empj_HouseInfo.setRoomId(houseInfo.getRoomId());
//		empj_HouseInfo.setTheNameOfRoomId(theNameOfRoomId);
//		empj_HouseInfo.setIsOverFloor(isOverFloor);
//		empj_HouseInfo.setOverFloors(overFloors);
		empj_HouseInfo.setPosition(position);
		empj_HouseInfo.setPurpose(houseInfo.getPurpose());
//		empj_HouseInfo.setProperty(property);
		empj_HouseInfo.setDeliveryType(building.getDeliveryType());
		empj_HouseInfo.setForecastArea(houseInfo.getForecastArea());
//		empj_HouseInfo.setActualArea(actualArea);
//		empj_HouseInfo.setInnerconsArea(innerconsArea);
//		empj_HouseInfo.setShareConsArea(shareConsArea);
//		empj_HouseInfo.setUseArea(useArea);
//		empj_HouseInfo.setBalconyArea(balconyArea);
		empj_HouseInfo.setHeigh(building.getHeigh());
//		empj_HouseInfo.setUnitType(unitType);
//		empj_HouseInfo.setRoomNumber(roomNumber);
//		empj_HouseInfo.setHallNumber(hallNumber);
//		empj_HouseInfo.setKitchenNumber(kitchenNumber);
//		empj_HouseInfo.setToiletNumber(toiletNumber);
//		empj_HouseInfo.seteCodeOfOriginalHouse(eCodeOfOriginalHouse);
		empj_HouseInfo.setIsOpen(false);
		empj_HouseInfo.setIsPresell(false);
		empj_HouseInfo.setIsMortgage(false);
		empj_HouseInfo.setLimitState(0);
//		empj_HouseInfo.seteCodeOfRealBuidingUnit(eCodeOfRealBuidingUnit);
//		empj_HouseInfo.seteCodeOfBusManage1(eCodeOfBusManage1);
//		empj_HouseInfo.seteCodeOfBusManage2(eCodeOfBusManage2);
		empj_HouseInfo.seteCodeOfMapping(building.geteCodeOfMapping());
		empj_HouseInfo.seteCodeOfPicture(building.geteCodeOfPicture());
//		empj_HouseInfo.setRemark(remark);
//		empj_HouseInfo.setLogId(logId);
		empj_HouseInfoDao.save(empj_HouseInfo);
	}
	
	
}

package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoUpdateService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Long buildingId = model.getBuildingId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		Long unitInfoId = model.getUnitInfoId();
		String eCodeOfUnitInfo = model.geteCodeOfUnitInfo();
		String eCodeFromPresellSystem = model.geteCodeFromPresellSystem();
		String eCodeFromEscrowSystem = model.geteCodeFromEscrowSystem();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		String addressFromPublicSecurity = model.getAddressFromPublicSecurity();
		Double recordPrice = model.getRecordPrice();
		Long lastTimeStampSyncRecordPriceToPresellSystem = model.getLastTimeStampSyncRecordPriceToPresellSystem();
		Integer settlementStateOfTripleAgreement = model.getSettlementStateOfTripleAgreement();
		Long tripleAgreementId = model.getTripleAgreementId();
		String eCodeFromPresellCert = model.geteCodeFromPresellCert();
		Double floor = model.getFloor();
		String roomId = model.getRoomId();
		String theNameOfRoomId = model.getTheNameOfRoomId();
		String ySpan = model.getySpan();
		String xSpan = model.getxSpan();
		Boolean isMerged = model.getIsMerged();
		Integer mergedNums = model.getMergedNums();
		Boolean isOverFloor = model.getIsOverFloor();
		Integer overFloors = model.getOverFloors();
		String position = model.getPosition();
		String purpose = model.getPurpose();
		String property = model.getProperty();
		String deliveryType = model.getDeliveryType();
		Double forecastArea = model.getForecastArea();
		Double actualArea = model.getActualArea();
		Double innerconsArea = model.getInnerconsArea();
		Double shareConsArea = model.getShareConsArea();
		Double useArea = model.getUseArea();
		Double balconyArea = model.getBalconyArea();
		Double heigh = model.getHeigh();
		String unitType = model.getUnitType();
		Integer roomNumber = model.getRoomNumber();
		Integer hallNumber = model.getHallNumber();
		Integer kitchenNumber = model.getKitchenNumber();
		Integer toiletNumber = model.getToiletNumber();
		String eCodeOfOriginalHouse = model.geteCodeOfOriginalHouse();
		Boolean isOpen = model.getIsOpen();
		Boolean isPresell = model.getIsPresell();
		Boolean isMortgage = model.getIsMortgage();
		Integer limitState = model.getLimitState();
		String eCodeOfRealBuidingUnit = model.geteCodeOfRealBuidingUnit();
		String eCodeOfBusManage1 = model.geteCodeOfBusManage1();
		String eCodeOfBusManage2 = model.geteCodeOfBusManage2();
		String eCodeOfMapping = model.geteCodeOfMapping();
		String eCodeOfPicture = model.geteCodeOfPicture();
		String remark = model.getRemark();
		Long logId = model.getLogId();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(unitInfoId == null || unitInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'unitInfo'不能为空");
		}
		if(eCodeOfUnitInfo == null || eCodeOfUnitInfo.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfUnitInfo'不能为空");
		}
		if(eCodeFromPresellSystem == null || eCodeFromPresellSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPresellSystem'不能为空");
		}
		if(eCodeFromEscrowSystem == null || eCodeFromEscrowSystem.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromEscrowSystem'不能为空");
		}
		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPublicSecurity'不能为空");
		}
		if(addressFromPublicSecurity == null || addressFromPublicSecurity.length() == 0)
		{
			return MyBackInfo.fail(properties, "'addressFromPublicSecurity'不能为空");
		}
		if(recordPrice == null || recordPrice < 1)
		{
			return MyBackInfo.fail(properties, "'recordPrice'不能为空");
		}
		if(lastTimeStampSyncRecordPriceToPresellSystem == null || lastTimeStampSyncRecordPriceToPresellSystem < 1)
		{
			return MyBackInfo.fail(properties, "'lastTimeStampSyncRecordPriceToPresellSystem'不能为空");
		}
		if(settlementStateOfTripleAgreement == null || settlementStateOfTripleAgreement < 1)
		{
			return MyBackInfo.fail(properties, "'settlementStateOfTripleAgreement'不能为空");
		}
		if(tripleAgreementId == null || tripleAgreementId < 1)
		{
			return MyBackInfo.fail(properties, "'tripleAgreement'不能为空");
		}
		if(eCodeFromPresellCert == null || eCodeFromPresellCert.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPresellCert'不能为空");
		}
		if(floor == null || floor < 1)
		{
			return MyBackInfo.fail(properties, "'floor'不能为空");
		}
		if(roomId == null || roomId.length() == 0)
		{
			return MyBackInfo.fail(properties, "'roomId'不能为空");
		}
		if(theNameOfRoomId == null || theNameOfRoomId.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfRoomId'不能为空");
		}
		if(ySpan == null || ySpan.length() == 0)
		{
			return MyBackInfo.fail(properties, "'ySpan'不能为空");
		}
		if(xSpan == null || xSpan.length() == 0)
		{
			return MyBackInfo.fail(properties, "'xSpan'不能为空");
		}
		if(isMerged == null)
		{
			return MyBackInfo.fail(properties, "'isMerged'不能为空");
		}
		if(mergedNums == null || mergedNums < 0)
		{
			return MyBackInfo.fail(properties, "'mergedNums'不能为空");
		}
		if(isOverFloor == null)
		{
			return MyBackInfo.fail(properties, "'isOverFloor'不能为空");
		}
		if(overFloors == null || overFloors < 0)
		{
			return MyBackInfo.fail(properties, "'overFloors'不能为空");
		}
		if(position == null || position.length() == 0)
		{
			return MyBackInfo.fail(properties, "'position'不能为空");
		}
		if(purpose == null || purpose.length() == 0)
		{
			return MyBackInfo.fail(properties, "'purpose'不能为空");
		}
		if(property == null || property.length() == 0)
		{
			return MyBackInfo.fail(properties, "'property'不能为空");
		}
		if(deliveryType == null || deliveryType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'deliveryType'不能为空");
		}
		if(forecastArea == null || forecastArea < 1)
		{
			return MyBackInfo.fail(properties, "'forecastArea'不能为空");
		}
		if(actualArea == null || actualArea < 1)
		{
			return MyBackInfo.fail(properties, "'actualArea'不能为空");
		}
		if(innerconsArea == null || innerconsArea < 1)
		{
			return MyBackInfo.fail(properties, "'innerconsArea'不能为空");
		}
		if(shareConsArea == null || shareConsArea < 1)
		{
			return MyBackInfo.fail(properties, "'shareConsArea'不能为空");
		}
		if(useArea == null || useArea < 1)
		{
			return MyBackInfo.fail(properties, "'useArea'不能为空");
		}
		if(balconyArea == null || balconyArea < 1)
		{
			return MyBackInfo.fail(properties, "'balconyArea'不能为空");
		}
		if(heigh == null || heigh < 1)
		{
			return MyBackInfo.fail(properties, "'heigh'不能为空");
		}
		if(unitType == null || unitType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'unitType'不能为空");
		}
		if(roomNumber == null || roomNumber < 1)
		{
			return MyBackInfo.fail(properties, "'roomNumber'不能为空");
		}
		if(hallNumber == null || hallNumber < 1)
		{
			return MyBackInfo.fail(properties, "'hallNumber'不能为空");
		}
		if(kitchenNumber == null || kitchenNumber < 1)
		{
			return MyBackInfo.fail(properties, "'kitchenNumber'不能为空");
		}
		if(toiletNumber == null || toiletNumber < 1)
		{
			return MyBackInfo.fail(properties, "'toiletNumber'不能为空");
		}
		if(eCodeOfOriginalHouse == null || eCodeOfOriginalHouse.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfOriginalHouse'不能为空");
		}
		if(isOpen == null)
		{
			return MyBackInfo.fail(properties, "'isOpen'不能为空");
		}
		if(isPresell == null)
		{
			return MyBackInfo.fail(properties, "'isPresell'不能为空");
		}
		if(isMortgage == null)
		{
			return MyBackInfo.fail(properties, "'isMortgage'不能为空");
		}
		if(limitState == null || limitState < 1)
		{
			return MyBackInfo.fail(properties, "'limitState'不能为空");
		}
		if(eCodeOfRealBuidingUnit == null || eCodeOfRealBuidingUnit.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfRealBuidingUnit'不能为空");
		}
		if(eCodeOfBusManage1 == null || eCodeOfBusManage1.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBusManage1'不能为空");
		}
		if(eCodeOfBusManage2 == null || eCodeOfBusManage2.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBusManage2'不能为空");
		}
		if(eCodeOfMapping == null || eCodeOfMapping.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfMapping'不能为空");
		}
		if(eCodeOfPicture == null || eCodeOfPicture.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfPicture'不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remark'不能为空");
		}
		if(logId == null || logId < 1)
		{
			return MyBackInfo.fail(properties, "'logId'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdate + ")'不存在");
		}
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'building(Id:" + buildingId + ")'不存在");
		}
		Empj_UnitInfo unitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(unitInfoId);
		if(unitInfo == null)
		{
			return MyBackInfo.fail(properties, "'unitInfo(Id:" + unitInfoId + ")'不存在");
		}
		Tgxy_TripleAgreement tripleAgreement = (Tgxy_TripleAgreement)tgxy_TripleAgreementDao.findById(tripleAgreementId);
		if(tripleAgreement == null)
		{
			return MyBackInfo.fail(properties, "'tripleAgreement(Id:" + tripleAgreementId + ")'不存在");
		}
	
		Long empj_HouseInfoId = model.getTableId();
		Empj_HouseInfo empj_HouseInfo = (Empj_HouseInfo)empj_HouseInfoDao.findById(empj_HouseInfoId);
		if(empj_HouseInfo == null)
		{
			return MyBackInfo.fail(properties, "'Empj_HouseInfo(Id:" + empj_HouseInfoId + ")'不存在");
		}
		
		empj_HouseInfo.setTheState(theState);
		empj_HouseInfo.setBusiState(busiState);
		empj_HouseInfo.seteCode(eCode);
		empj_HouseInfo.setUserStart(userStart);
		empj_HouseInfo.setCreateTimeStamp(createTimeStamp);
		empj_HouseInfo.setUserUpdate(userUpdate);
		empj_HouseInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		empj_HouseInfo.setUserRecord(userRecord);
		empj_HouseInfo.setRecordTimeStamp(recordTimeStamp);
		empj_HouseInfo.setBuilding(building);
		empj_HouseInfo.seteCodeOfBuilding(eCodeOfBuilding);
		empj_HouseInfo.setUnitInfo(unitInfo);
		empj_HouseInfo.seteCodeOfUnitInfo(eCodeOfUnitInfo);
		empj_HouseInfo.seteCodeFromPresellSystem(eCodeFromPresellSystem);
		empj_HouseInfo.seteCodeFromEscrowSystem(eCodeFromEscrowSystem);
		empj_HouseInfo.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		empj_HouseInfo.setAddressFromPublicSecurity(addressFromPublicSecurity);
		empj_HouseInfo.setRecordPrice(recordPrice);
		empj_HouseInfo.setLastTimeStampSyncRecordPriceToPresellSystem(lastTimeStampSyncRecordPriceToPresellSystem);
		empj_HouseInfo.setSettlementStateOfTripleAgreement(settlementStateOfTripleAgreement);
		empj_HouseInfo.setTripleAgreement(tripleAgreement);
		empj_HouseInfo.seteCodeFromPresellCert(eCodeFromPresellCert);
		empj_HouseInfo.setFloor(floor);
		empj_HouseInfo.setRoomId(roomId);
		empj_HouseInfo.setTheNameOfRoomId(theNameOfRoomId);
		empj_HouseInfo.setIsOverFloor(isOverFloor);
		empj_HouseInfo.setOverFloors(overFloors);
		empj_HouseInfo.setPosition(position);
		empj_HouseInfo.setPurpose(purpose);
		empj_HouseInfo.setProperty(property);
		empj_HouseInfo.setDeliveryType(deliveryType);
		empj_HouseInfo.setForecastArea(forecastArea);
		empj_HouseInfo.setActualArea(actualArea);
		empj_HouseInfo.setInnerconsArea(innerconsArea);
		empj_HouseInfo.setShareConsArea(shareConsArea);
		empj_HouseInfo.setUseArea(useArea);
		empj_HouseInfo.setBalconyArea(balconyArea);
		empj_HouseInfo.setHeigh(heigh);
		empj_HouseInfo.setUnitType(unitType);
		empj_HouseInfo.setRoomNumber(roomNumber);
		empj_HouseInfo.setHallNumber(hallNumber);
		empj_HouseInfo.setKitchenNumber(kitchenNumber);
		empj_HouseInfo.setToiletNumber(toiletNumber);
		empj_HouseInfo.seteCodeOfOriginalHouse(eCodeOfOriginalHouse);
		empj_HouseInfo.setIsOpen(isOpen);
		empj_HouseInfo.setIsPresell(isPresell);
		empj_HouseInfo.setIsMortgage(isMortgage);
		empj_HouseInfo.setLimitState(limitState);
		empj_HouseInfo.seteCodeOfRealBuidingUnit(eCodeOfRealBuidingUnit);
		empj_HouseInfo.seteCodeOfBusManage1(eCodeOfBusManage1);
		empj_HouseInfo.seteCodeOfBusManage2(eCodeOfBusManage2);
		empj_HouseInfo.seteCodeOfMapping(eCodeOfMapping);
		empj_HouseInfo.seteCodeOfPicture(eCodeOfPicture);
		empj_HouseInfo.setRemark(remark);
		empj_HouseInfo.setLogId(logId);
	
		empj_HouseInfoDao.save(empj_HouseInfo);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

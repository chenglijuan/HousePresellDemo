package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_HouseLoanAmount_ViewForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.database.dao.Tg_HouseLoanAmount_ViewListDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_HouseLoanAmount_View;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_HouseBusiState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_HouseInfoRebuild extends RebuilderBase<Empj_HouseInfo>
{
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
	@Autowired
	private Tgxy_BuyerInfoRebuild tgxy_BuyerInfoRebuild;
	@Autowired
	private Tg_HouseLoanAmount_ViewListDao tg_HouseLoanAmount_ViewListDao;//户入账信息
	
	
	@Override
	public Properties getSimpleInfo(Empj_HouseInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());		 //预售系统户编号
		properties.put("eCodeFromEscrowSystem", object.geteCodeFromEscrowSystem());  		 //托管系统户编号
		Empj_BuildingInfo buildingInfo = object.getBuilding();
		if (buildingInfo != null)
		{
			properties.put("eCodeFromConstruction", buildingInfo.geteCodeFromConstruction());	  //户施工编号
			properties.put("eCodeFromPublicSecurity", buildingInfo.geteCodeFromPublicSecurity()); //户公安编号
		}
		properties.put("position", object.getPosition());		 	      //户施工坐落
		properties.put("addressFromPublicSecurity", object.getAddressFromPublicSecurity());//户公安坐落
		properties.put("actualArea", object.getActualArea());		      //建筑面积（实测）
		properties.put("shareConsArea", object.getShareConsArea());		  //分摊建筑面积（㎡）
		properties.put("innerconsArea", object.getInnerconsArea());		  //套内建筑面积（㎡）
		Empj_UnitInfo unitInfo = object.getUnitInfo();
		if (unitInfo != null)
		{
			properties.put("UnitName", unitInfo.getTheName());  		  //单元号
		}
		Sm_User userStart = object.getUserStart();
		if (userStart != null)
		{
			properties.put("userStartName", userStart.getTheName());     //操作人
		}
		properties.put("createTimeStamp",
				MyDatetime.getInstance().dateToString(object.getCreateTimeStamp()));  //操作时间


		properties.put("floor", object.getFloor());						  //所在楼层
		properties.put("purpose", object.getPurpose());		 			  //房屋用途
		properties.put("busiState", object.getBusiState());				  //房屋状态
		properties.put("recordPrice", object.getRecordPrice());      	  //户物价备价格
		properties.put("lastTimeStampSyncRecordPriceToPresellSystem", MyDatetime.getInstance().dateToString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));		  //预售系统物价备案价格最后一次同步时间
		if (object.getSettlementStateOfTripleAgreement() != null && object.getSettlementStateOfTripleAgreement() == 1)
		{
			properties.put("tripleAgreementState", "已结算");		  //户三方协议结算状态
		}
		else
		{
			properties.put("tripleAgreementState", "未结算");		  //户三方协议结算状态
		}

		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
		properties.put("eCodeOfUnitInfo", object.geteCodeOfUnitInfo());
		properties.put("busiState", object.getBusiState());
		properties.put("settlementStateOfTripleAgreement", object.getSettlementStateOfTripleAgreement());
		
		Tgxy_TripleAgreement tgxy_TripleAgreement = object.getTripleAgreement();
		
		if(tgxy_TripleAgreement != null)//关联三方协议
		{
			properties.put("theStateOfTripleAgreement", tgxy_TripleAgreement.getTheStateOfTripleAgreement());//三方协议状态
		}
		
		properties.put("roomId", object.getRoomId());
		properties.put("theNameOfRoomId", object.getTheNameOfRoomId());
		
		return properties;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Properties getDetail(Empj_HouseInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("unitInfo", object.getUnitInfo());
		properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
		properties.put("eCodeFromEscrowSystem", object.geteCodeFromEscrowSystem());
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
		properties.put("addressFromPublicSecurity", object.getAddressFromPublicSecurity());
		properties.put("recordPrice", object.getRecordPrice());//户物价备案价格
		properties.put("lastTimeStampSyncRecordPriceToPresellSystem", MyDatetime.getInstance().dateToString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));
		properties.put("settlementStateOfTripleAgreement", object.getSettlementStateOfTripleAgreement());
		properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());
		properties.put("floor", object.getFloor());//所在楼层
		properties.put("theNameOfRoomId", object.getTheNameOfRoomId());
		properties.put("isOverFloor", object.getIsOverFloor());
		properties.put("overFloors", object.getOverFloors());
		properties.put("position", object.getPosition());//户施工坐落
		properties.put("property", object.getProperty());
		properties.put("deliveryType", object.getDeliveryType());
		properties.put("forecastArea", object.getForecastArea());
		properties.put("useArea", object.getUseArea());
		properties.put("balconyArea", object.getBalconyArea());
		properties.put("heigh", object.getHeigh());
		properties.put("unitType", object.getUnitType());
		properties.put("roomNumber", object.getRoomNumber());
		properties.put("hallNumber", object.getHallNumber());
		properties.put("kitchenNumber", object.getKitchenNumber());
		properties.put("toiletNumber", object.getToiletNumber());
		properties.put("eCodeOfOriginalHouse", object.geteCodeOfOriginalHouse());
		properties.put("isOpen", object.getIsOpen());
		properties.put("isPresell", object.getIsPresell());
		properties.put("isMortgage", object.getIsMortgage());
		properties.put("limitState", object.getLimitState());
		properties.put("eCodeOfRealBuidingUnit", object.geteCodeOfRealBuidingUnit());
		properties.put("eCodeOfBusManage1", object.geteCodeOfBusManage1());
		properties.put("eCodeOfBusManage2", object.geteCodeOfBusManage2());
		properties.put("eCodeOfMapping", object.geteCodeOfMapping());
		properties.put("eCodeOfPicture", object.geteCodeOfPicture());
		properties.put("remark", object.getRemark());
		properties.put("logId", object.getLogId());
		
		if(object.getBuilding() != null)//关联楼幢
		{
			properties.put("eCodeFromConstruction", object.getBuilding().geteCodeFromConstruction());//户施工编号
		}
		properties.put("actualArea", object.getActualArea());//建筑面积（实测）
		properties.put("shareConsArea", object.getShareConsArea());//分摊建筑面积（㎡）
		properties.put("innerconsArea", object.getInnerconsArea());//套内建筑面积（㎡）
		properties.put("eCodeOfUnitInfo", object.geteCodeOfUnitInfo());//楼幢单元
		properties.put("roomId", object.getRoomId());//室号
		properties.put("purpose", object.getPurpose());//房屋用途
		properties.put("busiState", object.getBusiState());//房屋状态
		
		//根据户室去查三方协议
//		Tgxy_TripleAgreementForm tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
//		tgxy_TripleAgreementForm.setHouse(object);
//		tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
//		
//		Tgxy_TripleAgreement tgxy_TripleAgreement = tgxy_TripleAgreementDao.findOneByQuery_T(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementForm));
		
		Tgxy_TripleAgreement tgxy_TripleAgreement = object.getTripleAgreement();
		
		if(tgxy_TripleAgreement != null)//关联三方协议
		{
			properties.put("eCodeOfTripleAgreement", tgxy_TripleAgreement.geteCodeOfTripleAgreement());//三方协议号
			properties.put("tripleAgreementTimeStamp", tgxy_TripleAgreement.getTripleAgreementTimeStamp());//协议日期
			properties.put("eCodeOfTAContractRecord", tgxy_TripleAgreement.geteCodeOfContractRecord());//合同备案号
			properties.put("theStateOfTripleAgreement", tgxy_TripleAgreement.getTheStateOfTripleAgreement());//三方协议状态
			properties.put("theStateOfTripleAgreementFiling", tgxy_TripleAgreement.getTheStateOfTripleAgreementFiling());//三方协议归档状态
			properties.put("theStateOfTripleAgreementEffect", tgxy_TripleAgreement.getTheStateOfTripleAgreementEffect());//三方协议效力状态
			
		}
		properties.put("settlementStateOfTripleAgreement", object.getSettlementStateOfTripleAgreement());//三方协议结算状态
		
		//根据户室去查买卖合同
//		Tgxy_ContractInfoForm tgxy_ContractInfoForm = new Tgxy_ContractInfoForm();
//		tgxy_ContractInfoForm.setTheState(S_TheState.Normal);
//		tgxy_ContractInfoForm.setHouseInfo(object);
//		
//		Tgxy_ContractInfo tgxy_ContractInfo = tgxy_ContractInfoDao.findOneByQuery_T(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), tgxy_ContractInfoForm));
		
		Tgxy_ContractInfo tgxy_ContractInfo = object.getContractInfo();
		
		if(tgxy_ContractInfo != null)//关联预售系统买卖合同
		{
			properties.put("eCodeOfContractRecord", tgxy_ContractInfo.geteCodeOfContractRecord());//合同备案号
			properties.put("contractBusiState", tgxy_ContractInfo.getBusiState());//合同状态
			properties.put("contractSumPrice", tgxy_ContractInfo.getContractSumPrice());//合同总价
			properties.put("contractSignDate", tgxy_ContractInfo.getContractSignDate());//合同签订日期
			properties.put("paymentMethod", tgxy_ContractInfo.getPaymentMethod());//付款方式
			properties.put("firstPaymentAmount", tgxy_ContractInfo.getFirstPaymentAmount());//首付款金额（元）
			properties.put("loanAmount", tgxy_ContractInfo.getLoanAmount());//贷款金额（元）
			properties.put("payDate", tgxy_ContractInfo.getPayDate());//交付日期
		}
		
		Tgxy_BuyerInfoForm tgxy_BuyerInfoForm = new Tgxy_BuyerInfoForm();
		tgxy_BuyerInfoForm.setHouseInfoId(object.getTableId());
		List<Tgxy_BuyerInfo> tgxy_BuyerInfoList = tgxy_BuyerInfoDao.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getHouseHQL(), tgxy_BuyerInfoForm));
		if(tgxy_BuyerInfoList != null && !tgxy_BuyerInfoList.isEmpty())
		{
			properties.put("tgxy_BuyerInfoList", tgxy_BuyerInfoRebuild.execute(tgxy_BuyerInfoList));//买受人信息
		}
		
		//户入账明细信息(前提：三方协议存在)
		List<Tg_HouseLoanAmount_View> houseLoanAmountList;
		if(tgxy_TripleAgreement != null)
		{
			Tg_HouseLoanAmount_ViewForm houseLoanAmountModel = new Tg_HouseLoanAmount_ViewForm();
			houseLoanAmountModel.setECodeOfTripleagreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
			
			houseLoanAmountList = tg_HouseLoanAmount_ViewListDao.findByPage(tg_HouseLoanAmount_ViewListDao.getQuery(tg_HouseLoanAmount_ViewListDao.getHouseBasicHQL(), houseLoanAmountModel));
			
			if(null==houseLoanAmountList)
			{
				houseLoanAmountList = new ArrayList<Tg_HouseLoanAmount_View>();
			}
			
		}
		else
		{
			houseLoanAmountList = new ArrayList<Tg_HouseLoanAmount_View>();
		}
		
		properties.put("houseLoanAmountList", houseLoanAmountList);//户入账明细
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_HouseInfo> empj_HouseInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_HouseInfoList != null)
		{
			for(Empj_HouseInfo object:empj_HouseInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("unitInfo", object.getUnitInfo());
				properties.put("unitInfoId", object.getUnitInfo().getTableId());
				properties.put("eCodeOfUnitInfo", object.geteCodeOfUnitInfo());
				properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
				properties.put("eCodeFromEscrowSystem", object.geteCodeFromEscrowSystem());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("addressFromPublicSecurity", object.getAddressFromPublicSecurity());
				properties.put("recordPrice", object.getRecordPrice());
				properties.put("lastTimeStampSyncRecordPriceToPresellSystem", MyDatetime.getInstance().dateToString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));
				properties.put("settlementStateOfTripleAgreement", object.getSettlementStateOfTripleAgreement());
				properties.put("tripleAgreement", object.getTripleAgreement());
				properties.put("tripleAgreementId", object.getTripleAgreement().getTableId());
				properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());
				properties.put("floor", object.getFloor());
				properties.put("roomId", object.getRoomId());
				properties.put("theNameOfRoomId", object.getTheNameOfRoomId());
				properties.put("isOverFloor", object.getIsOverFloor());
				properties.put("overFloors", object.getOverFloors());
				properties.put("position", object.getPosition());
				properties.put("purpose", object.getPurpose());
				properties.put("property", object.getProperty());
				properties.put("deliveryType", object.getDeliveryType());
				properties.put("forecastArea", object.getForecastArea());
				properties.put("actualArea", object.getActualArea());
				properties.put("innerconsArea", object.getInnerconsArea());
				properties.put("shareConsArea", object.getShareConsArea());
				properties.put("useArea", object.getUseArea());
				properties.put("balconyArea", object.getBalconyArea());
				properties.put("heigh", object.getHeigh());
				properties.put("unitType", object.getUnitType());
				properties.put("roomNumber", object.getRoomNumber());
				properties.put("hallNumber", object.getHallNumber());
				properties.put("kitchenNumber", object.getKitchenNumber());
				properties.put("toiletNumber", object.getToiletNumber());
				properties.put("eCodeOfOriginalHouse", object.geteCodeOfOriginalHouse());
				properties.put("isOpen", object.getIsOpen());
				properties.put("isPresell", object.getIsPresell());
				properties.put("isMortgage", object.getIsMortgage());
				properties.put("limitState", object.getLimitState());
				properties.put("eCodeOfRealBuidingUnit", object.geteCodeOfRealBuidingUnit());
				properties.put("eCodeOfBusManage1", object.geteCodeOfBusManage1());
				properties.put("eCodeOfBusManage2", object.geteCodeOfBusManage2());
				properties.put("eCodeOfMapping", object.geteCodeOfMapping());
				properties.put("eCodeOfPicture", object.geteCodeOfPicture());
				properties.put("remark", object.getRemark());
				properties.put("logId", object.getLogId());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	public Properties getByDetail(Empj_HouseInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		//户室主键
		properties.put("tableId", object.getTableId());
		//室号
		properties.put("roomId", object.getRoomId());
		//室号名称
		properties.put("theNameOfRoomId", object.getTheNameOfRoomId());
		//单元
		properties.put("eCodeOfUnitInfo", object.geteCodeOfUnitInfo());
		//房屋座落
		properties.put("position", object.getPosition());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List executeForHouseTableDetail(List<Empj_HouseInfo> empj_HouseInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_HouseInfoList != null)
		{
			for(Empj_HouseInfo object:empj_HouseInfoList)
			{
				if(object==null)continue;
				
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("busiState", object.getBusiState());//房屋状态（已搭建，已批准预售）
				properties.put("limitState", object.getLimitState());//限制状态（查封，限制，处置）
				
				properties.put("isLimit", object.getIsLimit());
				properties.put("isSequestration", object.getIsSequestration());
				properties.put("isManagement", object.getIsManagement());
				
				properties.put("floor", object.getFloor());//所在楼层
				properties.put("roomId", object.getRoomId());//室号
				properties.put("rowSpan", object.getRowSpan());//合并行数
				properties.put("colSpan", object.getColSpan());//合并列数
				
				properties.put("actualArea", object.getActualArea());//车库面积
				
				if(!S_HouseBusiState.EmptyHouse.equals(object.getBusiState()))
				{
					if(object.getTripleAgreement() != null)//三方协议
					{
						properties.put("theStateOfTripleAgreement", object.getTripleAgreement().getTheStateOfTripleAgreement());//三方协议状态
						//资金状态
						if(object.getTripleAgreement().getTotalAmountOfHouse() != null && object.getTripleAgreement().getTotalAmountOfHouse() > 0)
						{
							properties.put("capitalState", "已到账");
						}
						else
						{
							properties.put("capitalState", "未到账");
						}
					}
					else
					{
						properties.put("theStateOfTripleAgreement", "未签署");
						properties.put("capitalState", "未到账");
					}
				}
				
				if(object.getUnitInfo() != null)//关联单元
				{
					properties.put("unitName", object.getUnitInfo().getTheName());//单元名称
				}

				list.add(properties);
			}
		}
		return list;
	}
	
	public Properties getSimpleInfoToUpdate(Empj_HouseInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		if(object.getTableId()!=null){
			properties.put("tableId", object.getTableId());
		}
	    if(object.getRoomId()!=null){
	    	properties.put("roomId", object.getRoomId());
	    }
	    if(object.getPosition()!=null){
	    	properties.put("position", object.getPosition());
	    }
	    if(object.getFloor()!=null){
	    	properties.put("floor", object.getFloor());
	    }
	    if(object.getForecastArea()!=null){
	    	properties.put("forecastArea", object.getForecastArea());
	    }
	    if(object.getShareConsArea()!=null){
	    	properties.put("shareConsArea", object.getShareConsArea());
	    }
	    if(object.getInnerconsArea()!=null){
	    	properties.put("innerconsArea", object.getInnerconsArea());
	    }
	    if(object.getPurpose()!=null){
	    	properties.put("purpose", object.getPurpose());
	    }
	    if(object.getRecordPrice()!=null){
	    	properties.put("recordPrice", object.getRecordPrice());
	    }
	    if(object.getLastTimeStampSyncRecordPriceToPresellSystem()!=null){
	    	properties.put("lastTimeStampSyncRecordPriceToPresellSystem", MyDatetime.getInstance().dateToSimpleString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));
	    }
	    if(object.getBusiState()!=null){
	    	properties.put("busiState", object.getBusiState());
	    }
	    if(object.getTripleAgreement()!=null){
	    	properties.put("eCodeOfTripleAgreement", object.getTripleAgreement().geteCodeOfTripleAgreement());
	    }	
	    /*if(object.getUnitInfo()!=null){
	    	properties.put("empj_UnitInfoId", object.getUnitInfo().getTableId());
	    }*/
	    /*if(object.getTheHouseState()!=null){
	    	properties.put("theHouseState", object.getTheHouseState());
	    }*/
	    if(object.getRemark()!=null){
	    	properties.put("remark", object.getRemark());
	    }
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getFromListDetil(List<Empj_HouseInfo> empj_HouseInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_HouseInfoList != null)
		{
			for(Empj_HouseInfo object:empj_HouseInfoList)
			{
				if(object==null)continue;
				
				Properties properties = new MyProperties();
				if(object.getTableId()!=null){
					properties.put("tableId", object.getTableId());
				}
			    if(object.getRoomId()!=null){
			    	properties.put("roomId", object.getRoomId());
			    }
			    if(object.getPosition()!=null){
			    	properties.put("position", object.getPosition());
			    }
			    if(object.getFloor()!=null){
			    	properties.put("floor", object.getFloor());
			    }
			    if(object.getForecastArea()!=null){
			    	properties.put("forecastArea", object.getForecastArea());
			    }
			    if(object.getShareConsArea()!=null){
			    	properties.put("shareConsArea", object.getShareConsArea());
			    }
			    if(object.getInnerconsArea()!=null){
			    	properties.put("innerconsArea", object.getInnerconsArea());
			    }
			    if(object.getPurpose()!=null){
			    	properties.put("purpose", object.getPurpose());
			    }
			    if(object.getRecordPrice()!=null){
			    	properties.put("recordPrice", object.getRecordPrice());
			    }
			    if(object.getLastTimeStampSyncRecordPriceToPresellSystem()!=null){
			    	properties.put("lastTimeStampSyncRecordPriceToPresellSystem", MyDatetime.getInstance().dateToSimpleString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));
			    }
			    if(object.getBusiState()!=null){
			    	properties.put("busiState", object.getBusiState());
			    }
			    if(object.getTripleAgreement()!=null){
			    	properties.put("eCodeOfTripleAgreement", object.getTripleAgreement().geteCodeOfTripleAgreement());
			    }	
			    /*if(object.getUnitInfo()!=null){
			    	properties.put("empj_UnitInfoId", object.getUnitInfo().getTableId());
			    }*/
			    /*if(object.getTheHouseState()!=null){
			    	properties.put("theHouseState", object.getTheHouseState());
			    }*/
			    if(object.getRemark()!=null){
			    	properties.put("remark", object.getRemark());
			    }
				list.add(properties);
			}
		}
		return list;
	}
}

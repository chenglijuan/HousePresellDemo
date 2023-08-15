package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_PreSaleBuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_PreSaleContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service：三方协议自动打印
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_TripleAgreementAutoPrintService
{
//	@Autowired
//	private Tgxy_PreSaleContractInfoDao tgxy_PreSaleContractInfoDao;
//	
//	@Autowired
//	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
//	
//	@Autowired
//	private Tgxy_PreSaleBuyerInfoDao tgxy_PreSaleBuyerInfoDao;
//	
//	@Autowired
//	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
//	
//	@Autowired
//	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
//	
//	@Autowired
//	private Sm_UserDao sm_UserDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();
		
//		//获取前台传递过来的分页条件
//		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
//		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
//		String keyword = model.getKeyword();
//		Long userStartId = model.getUserStartId();//创建人id
//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		 //合同编号
//		String eCode = model.geteCode(); 
//		if(eCode == null || eCode.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'合同编号'不能为空");
//		}
//		
//
//		 
//		//判断是否已经生成协议  
//		Tgxy_TripleAgreement tgxy_TripleAgreement1 =(Tgxy_TripleAgreement) tgxy_TripleAgreementDao.findOneByQuery(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(),model));
//		if(tgxy_TripleAgreement1==null){
//			//打印三方协议
//		}else{
//			//根据合同信息  自动生成三方协议 并打印
//			
//			Tgxy_TripleAgreement tgxy_TripleAgreement = new Tgxy_TripleAgreement();
////			tgxy_TripleAgreement.setTheState(theState);
////			tgxy_TripleAgreement.setBusiState(busiState);
//			tgxy_TripleAgreement.seteCode(eCode);   //协议编号  后面需要拼流水 
//			tgxy_TripleAgreement.setUserStart(userStart);  //创建人
//			tgxy_TripleAgreement.setCreateTimeStamp(System.currentTimeMillis()); //创建时间
////			tgxy_TripleAgreement.setLastUpdateTimeStamp(lastUpdateTimeStamp);   
////			tgxy_TripleAgreement.setUserRecord(userRecord);
////			tgxy_TripleAgreement.setRecordTimeStamp(recordTimeStamp);
////			tgxy_TripleAgreement.seteCodeOfTripleAgreement(eCodeOfTripleAgreement);
////			tgxy_TripleAgreement.setTripleAgreementTimeStamp(System.currentTimeMillis()); //协议日期
//			tgxy_TripleAgreement.setEscrowCompany("常州正泰房产居间服务有限公司"); //托管机构
//			
//			//判断合同编号是否已经同步到托管系统  如果存在 不需要同步
//			Tgxy_ContractInfoForm tgxy_ContractInfomodel = new Tgxy_ContractInfoForm();
//			tgxy_ContractInfomodel.seteCode(model.geteCodeOfContractRecord());
//			Tgxy_ContractInfo tgxy_ContractInfo1 =(Tgxy_ContractInfo) tgxy_ContractInfoDao.findOneByQuery(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(),tgxy_ContractInfomodel));
//			if(tgxy_ContractInfo1!=null){
//				//根据前台传递预售系统合同编号 去中间库查询合同信息
//				Tgxy_PreSaleContractInfoForm Tgxy_PreSaleContractInfomodel = new Tgxy_PreSaleContractInfoForm();
//				Tgxy_PreSaleContractInfomodel.seteCode(model.geteCodeOfContractRecord());
//				Tgxy_PreSaleContractInfo tgxy_PreSaleContractInfo =(Tgxy_PreSaleContractInfo) tgxy_PreSaleContractInfoDao.findOneByQuery(tgxy_PreSaleContractInfoDao.getQuery(tgxy_PreSaleContractInfoDao.getBasicHQL(),Tgxy_PreSaleContractInfomodel));
//				  //查询出来的合同信息 同步到托管系统合同信息表
//				Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
//				tgxy_ContractInfo.setTheState(tgxy_PreSaleContractInfo.getTheState());
//				tgxy_ContractInfo.setBusiState(tgxy_PreSaleContractInfo.getBusiState());
//				tgxy_ContractInfo.seteCode(tgxy_PreSaleContractInfo.geteCode());
//				tgxy_ContractInfo.setUserStart(tgxy_PreSaleContractInfo.getUserStart());
//				tgxy_ContractInfo.setCreateTimeStamp(tgxy_PreSaleContractInfo.getCreateTimeStamp());
//				tgxy_ContractInfo.setLastUpdateTimeStamp(tgxy_PreSaleContractInfo.getLastUpdateTimeStamp());
//				tgxy_ContractInfo.setUserRecord(tgxy_PreSaleContractInfo.getUserRecord());
//				tgxy_ContractInfo.setRecordTimeStamp(tgxy_PreSaleContractInfo.getRecordTimeStamp());
//				tgxy_ContractInfo.seteCodeOfContractRecord(tgxy_PreSaleContractInfo.geteCodeOfContractRecord());
//				tgxy_ContractInfo.setCompany(tgxy_PreSaleContractInfo.getCompany());
//				tgxy_ContractInfo.setTheNameFormCompany(tgxy_PreSaleContractInfo.getTheNameFormCompany());
//				tgxy_ContractInfo.setTheNameOfProject(tgxy_PreSaleContractInfo.getTheNameOfProject());
//				tgxy_ContractInfo.seteCodeFromConstruction(tgxy_PreSaleContractInfo.geteCodeFromConstruction());
//				tgxy_ContractInfo.setHouseInfo(tgxy_PreSaleContractInfo.getHouseInfo());
//				tgxy_ContractInfo.seteCodeOfHouseInfo(tgxy_PreSaleContractInfo.geteCodeOfHouseInfo());
//				tgxy_ContractInfo.setRoomIdOfHouseInfo(tgxy_PreSaleContractInfo.getRoomIdOfHouseInfo());
//				tgxy_ContractInfo.setContractSumPrice(tgxy_PreSaleContractInfo.getContractSumPrice());
//				tgxy_ContractInfo.setBuildingArea(tgxy_PreSaleContractInfo.getBuildingArea());
//				tgxy_ContractInfo.setPosition(tgxy_PreSaleContractInfo.getPosition());
////				tgxy_ContractInfo.setContractSignDate(tgxy_PreSaleContractInfo.getContractSignDate());
//				tgxy_ContractInfo.setPaymentMethod(tgxy_PreSaleContractInfo.getPaymentMethod());
//				tgxy_ContractInfo.setLoanBank(tgxy_PreSaleContractInfo.getLoanBank());
//				tgxy_ContractInfo.setFirstPaymentAmount(tgxy_PreSaleContractInfo.getFirstPaymentAmount());
//				tgxy_ContractInfo.setLoanAmount(tgxy_PreSaleContractInfo.getLoanAmount());
//				tgxy_ContractInfo.setEscrowState(tgxy_PreSaleContractInfo.getEscrowState());
////				tgxy_ContractInfo.setPayDate(tgxy_PreSaleContractInfo.getPayDate());
//				tgxy_ContractInfo.seteCodeOfBuilding(tgxy_PreSaleContractInfo.geteCodeOfBuilding());
//				tgxy_ContractInfo.seteCodeFromPublicSecurity(tgxy_PreSaleContractInfo.geteCodeFromPublicSecurity());
////				tgxy_ContractInfo.setContractRecordDate(tgxy_PreSaleContractInfo.getContractRecordDate());
//				tgxy_ContractInfo.setSyncPerson(tgxy_PreSaleContractInfo.getSyncPerson());
//				tgxy_ContractInfo.setSyncDate(tgxy_PreSaleContractInfo.getSyncDate());
//				tgxy_ContractInfoDao.save(tgxy_ContractInfo);
//
//				//三方协议信息赋值
//				tgxy_TripleAgreement.seteCodeOfContractRecord(tgxy_PreSaleContractInfo.geteCodeOfContractRecord()); //合同备案号
//
//				tgxy_TripleAgreement.setTheNameOfProject(tgxy_PreSaleContractInfo.getTheNameOfProject());//项目名称
////				tgxy_TripleAgreement.setBuildingInfo(buildingInfo);
//				tgxy_TripleAgreement.seteCodeOfBuilding(tgxy_PreSaleContractInfo.geteCodeOfBuilding());//楼幢编号
//				tgxy_TripleAgreement.seteCodeFromConstruction(tgxy_PreSaleContractInfo.geteCodeFromConstruction());//施工编号
////				tgxy_TripleAgreement.setUnitInfo();
////				tgxy_TripleAgreement.seteCodeOfUnit(eCodeOfUnit);
////				tgxy_TripleAgreement.setUnitRoom(unitRoom);
//				tgxy_TripleAgreement.setBuildingArea(tgxy_PreSaleContractInfo.getBuildingArea()); //建筑面积
//				tgxy_TripleAgreement.setContractAmount(tgxy_PreSaleContractInfo.getContractSumPrice()); //合同总价
//				tgxy_TripleAgreement.setFirstPayment(tgxy_PreSaleContractInfo.getFirstPaymentAmount()); //首付款金额
//				tgxy_TripleAgreement.setLoanAmount(tgxy_PreSaleContractInfo.getLoanAmount()); //贷款金额
////				tgxy_TripleAgreement.setBuyerInfoSet(buyerInfoSet); //买受人
////				tgxy_TripleAgreement.setTheStateOfTripleAgreement(theStateOfTripleAgreement);
////				tgxy_TripleAgreement.setTheStateOfTripleAgreementFiling(theStateOfTripleAgreementFiling);
////				tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect(theStateOfTripleAgreementEffect);
////				tgxy_TripleAgreement.setPrintMethod(printMethod);
////				tgxy_TripleAgreement.setTheAmountOfRetainedEquity(theAmountOfRetainedEquity);
////				tgxy_TripleAgreement.setTheAmountOfInterestRetained(theAmountOfInterestRetained);
////				tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(theAmountOfInterestUnRetained);
////				tgxy_TripleAgreement.setTotalAmountOfHouse(totalAmountOfHouse);
//				
//			}else{
//				//三方协议信息赋值
//				tgxy_TripleAgreement.seteCodeOfContractRecord(tgxy_ContractInfo1.geteCodeOfContractRecord()); //合同备案号
//
//				tgxy_TripleAgreement.setTheNameOfProject(tgxy_ContractInfo1.getTheNameOfProject());//项目名称
////				tgxy_TripleAgreement.setBuildingInfo(buildingInfo);
//				tgxy_TripleAgreement.seteCodeOfBuilding(tgxy_ContractInfo1.geteCodeOfBuilding());//楼幢编号
//				tgxy_TripleAgreement.seteCodeFromConstruction(tgxy_ContractInfo1.geteCodeFromConstruction());//施工编号
////				tgxy_TripleAgreement.setUnitInfo();
////				tgxy_TripleAgreement.seteCodeOfUnit(eCodeOfUnit);
////				tgxy_TripleAgreement.setUnitRoom(unitRoom);
//				tgxy_TripleAgreement.setBuildingArea(tgxy_ContractInfo1.getBuildingArea()); //建筑面积
//				tgxy_TripleAgreement.setContractAmount(tgxy_ContractInfo1.getContractSumPrice()); //合同总价
//				tgxy_TripleAgreement.setFirstPayment(tgxy_ContractInfo1.getFirstPaymentAmount()); //首付款金额
//				tgxy_TripleAgreement.setLoanAmount(tgxy_ContractInfo1.getLoanAmount()); //贷款金额
////				tgxy_TripleAgreement.setBuyerInfoSet(buyerInfoSet); //买受人
////				tgxy_TripleAgreement.setTheStateOfTripleAgreement(theStateOfTripleAgreement);
////				tgxy_TripleAgreement.setTheStateOfTripleAgreementFiling(theStateOfTripleAgreementFiling);
////				tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect(theStateOfTripleAgreementEffect);
////				tgxy_TripleAgreement.setPrintMethod(printMethod);
////				tgxy_TripleAgreement.setTheAmountOfRetainedEquity(theAmountOfRetainedEquity);
////				tgxy_TripleAgreement.setTheAmountOfInterestRetained(theAmountOfInterestRetained);
////				tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(theAmountOfInterestUnRetained);
////				tgxy_TripleAgreement.setTotalAmountOfHouse(totalAmountOfHouse);
//			}
//			
//				//判断买受人是否存在 不存在同步到托管系统
//			    Tgxy_BuyerInfoForm  Tgxy_BuyerInfomodel = new Tgxy_BuyerInfoForm();
//			    Tgxy_BuyerInfomodel.seteCodeOfContract(model.geteCodeOfContractRecord());
//				Tgxy_BuyerInfo tgxy_BuyerInfo1 = (Tgxy_BuyerInfo) tgxy_BuyerInfoDao.findOneByQuery(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(),Tgxy_BuyerInfomodel));
//	            if(tgxy_BuyerInfo1!=null){
//	            	//根据合同备案号查询备案系统买受人信息
//	    		    Tgxy_PreSaleBuyerInfoForm Tgxy_PreSaleBuyerInfomodel = new Tgxy_PreSaleBuyerInfoForm();
//	    		    Tgxy_PreSaleBuyerInfomodel.seteCodeOfContract(model.geteCode());
//	    		    Tgxy_PreSaleBuyerInfo tgxy_PreSaleBuyerInfo = (Tgxy_PreSaleBuyerInfo) tgxy_PreSaleBuyerInfoDao.findOneByQuery(tgxy_PreSaleBuyerInfoDao.getQuery(tgxy_PreSaleBuyerInfoDao.getBasicHQL(),Tgxy_PreSaleBuyerInfomodel));
//	    			
//	    			//同步买受人
//	    			Tgxy_BuyerInfo tgxy_BuyerInfo = new Tgxy_BuyerInfo();
//	    			tgxy_BuyerInfo.setTheState(tgxy_PreSaleBuyerInfo.getTheState());
//	    			tgxy_BuyerInfo.setBusiState(tgxy_PreSaleBuyerInfo.getBusiState());
//	    			tgxy_BuyerInfo.seteCode(tgxy_PreSaleBuyerInfo.geteCode());
//	    			tgxy_BuyerInfo.setUserStart(userStart);
//	    			tgxy_BuyerInfo.setCreateTimeStamp(tgxy_PreSaleBuyerInfo.getCreateTimeStamp());
//	    			tgxy_BuyerInfo.setLastUpdateTimeStamp(tgxy_PreSaleBuyerInfo.getLastUpdateTimeStamp());
////	    			tgxy_BuyerInfo.setUserRecord(userRecord);
////	    			tgxy_BuyerInfo.setRecordTimeStamp(recordTimeStamp);
//	    			tgxy_BuyerInfo.setBuyerName(tgxy_PreSaleBuyerInfo.getBuyerName());
//	    			tgxy_BuyerInfo.setBuyerType(tgxy_PreSaleBuyerInfo.getBuyerType());
//	    			tgxy_BuyerInfo.setCertificateType(tgxy_PreSaleBuyerInfo.getCertificateType());
//	    			tgxy_BuyerInfo.seteCodeOfcertificate(tgxy_PreSaleBuyerInfo.geteCodeOfcertificate());
//	    			tgxy_BuyerInfo.setContactPhone(tgxy_PreSaleBuyerInfo.getContactPhone());
//	    			tgxy_BuyerInfo.setContactAdress(tgxy_PreSaleBuyerInfo.getContactAdress());
//	    			tgxy_BuyerInfo.setAgentName(tgxy_PreSaleBuyerInfo.getAgentName());
//	    			tgxy_BuyerInfo.setAgentCertType(tgxy_PreSaleBuyerInfo.getAgentCertType());
//	    			tgxy_BuyerInfo.setAgentCertNumber(tgxy_PreSaleBuyerInfo.getAgentCertNumber());
//	    			tgxy_BuyerInfo.setAgentPhone(tgxy_PreSaleBuyerInfo.getAgentPhone());
//	    			tgxy_BuyerInfo.setAgentAddress(tgxy_PreSaleBuyerInfo.getAgentAddress());
//	    			tgxy_BuyerInfo.seteCodeOfContract(tgxy_PreSaleBuyerInfo.geteCodeOfContract());
//	    			tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);
//	    			//三方协议信息
//					tgxy_TripleAgreement.setSellerName(tgxy_PreSaleBuyerInfo.getBuyerName()); //出卖人姓名
//	            }else{
//	            	//三方协议信息
//					tgxy_TripleAgreement.setSellerName(tgxy_BuyerInfo1.getBuyerName()); //出卖人姓名
//	            }
//
//			   
//	            
//				
//
//
//				tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
//				//打印三方协议
//				
////			}
////			else
////			{
////				tgxy_PreSaleContractInfoList = new ArrayList<Tgxy_PreSaleContractInfo>();
////			}
//			
//		
//			
//			
//			
//			
//		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

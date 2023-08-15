package zhishusz.housepresell.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleAddService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;	
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	private static final String BUSI_CODE = "06110304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();


		String signTimeStamp = model.getSignTimeStamp();
		String applySettlementDate = model.getApplySettlementDate();
		String startSettlementDate = model.getStartSettlementDate();
		String endSettlementDate = model.getEndSettlementDate();
		Integer protocolNumbers = model.getProtocolNumbers();
		Integer settlementState = model.getSettlementState();
		Integer beforeNumbers = model.getBeforeNumbers();
			
		Sm_User userStart = model.getUser();	// 管理员账户
		
		Emmp_CompanyInfo agentCompany = userStart.getCompany();
		
		Serializable entity;

		Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = new Tgxy_CoopAgreementSettle();
		tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
		tgxy_CoopAgreementSettle.setUserStart(userStart);
		tgxy_CoopAgreementSettle.setCreateTimeStamp(System.currentTimeMillis());
		tgxy_CoopAgreementSettle.setUserUpdate(userStart);
		tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_CoopAgreementSettle.seteCode(sm_BusinessCodeGetService.getCoopAgreementSettleEcode(BUSI_CODE));
		tgxy_CoopAgreementSettle.setSignTimeStamp(signTimeStamp);
		tgxy_CoopAgreementSettle.setAgentCompany(agentCompany);
		tgxy_CoopAgreementSettle.setCompanyName(agentCompany.getTheName());
		tgxy_CoopAgreementSettle.setApplySettlementDate(applySettlementDate);
		tgxy_CoopAgreementSettle.setStartSettlementDate(startSettlementDate);
		tgxy_CoopAgreementSettle.setEndSettlementDate(endSettlementDate);
		tgxy_CoopAgreementSettle.setProtocolNumbers(protocolNumbers);
		tgxy_CoopAgreementSettle.setSettlementState(settlementState);
		tgxy_CoopAgreementSettle.setBeforeNumbers(beforeNumbers);
		
		entity = tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
		
		
		try {
			Map<String, Object> query_COOPAGREEMENT_SAVE = tgxy_CoopAgreementSettleDao.QUERY_COOPAGREEMENT_SAVE(Long.valueOf(entity.toString()), model.getUserId(), model.getUser().getCompany().getTableId(), MyDatetime.getInstance().stringToLong(startSettlementDate), MyDatetime.getInstance().stringToLong(endSettlementDate));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		//过滤三方协议业务的提交日期
        /*Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("06110301");
        afModel.setOrderBy("createTimeStamp");
        afModel.setStartTimeStamp(MyDatetime.getInstance().stringToLong(startSettlementDate));
        afModel.setEndTimeStamp(MyDatetime.getInstance().stringToLong(endSettlementDate));
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicStartHQL(), afModel));
        if(!afList.isEmpty())
        {
        	Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = new Tgxy_CoopAgreementSettle();
			tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
//			tgxy_CoopAgreementSettle.setBusiState(busiState);
			tgxy_CoopAgreementSettle.setUserStart(userStart);
			tgxy_CoopAgreementSettle.setCreateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.setUserUpdate(userStart);
			tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
//			tgxy_CoopAgreementSettle.setUserRecord(userRecord);
//			tgxy_CoopAgreementSettle.setRecordTimeStamp(recordTimeStamp);
			tgxy_CoopAgreementSettle.seteCode(sm_BusinessCodeGetService.getCoopAgreementSettleEcode(BUSI_CODE));
			tgxy_CoopAgreementSettle.setSignTimeStamp(signTimeStamp);
			tgxy_CoopAgreementSettle.setAgentCompany(agentCompany);
			tgxy_CoopAgreementSettle.setCompanyName(agentCompany.getTheName());
			tgxy_CoopAgreementSettle.setApplySettlementDate(applySettlementDate);
			tgxy_CoopAgreementSettle.setStartSettlementDate(startSettlementDate);
			tgxy_CoopAgreementSettle.setEndSettlementDate(endSettlementDate);
			tgxy_CoopAgreementSettle.setProtocolNumbers(protocolNumbers);
			tgxy_CoopAgreementSettle.setSettlementState(settlementState);
			tgxy_CoopAgreementSettle.setBeforeNumbers(beforeNumbers);
			
			entity = tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle) ;
			
        	int index = 0;
        	Tgxy_TripleAgreement tgxy_TripleAgreement = new Tgxy_TripleAgreement();
        	for (Sm_ApprovalProcess_AF sm_ApprovalProcess_AF : afList) {
				
        		tgxy_TripleAgreement = tgxy_TripleAgreementDao.findById(sm_ApprovalProcess_AF.getSourceId());
        		
        		if( null != tgxy_TripleAgreement && S_TheState.Normal == tgxy_TripleAgreement.getTheState() && S_ApprovalState.Completed.equals(tgxy_TripleAgreement.getApprovalState()))
        		{
        			//过滤开发企业
        			Emmp_CompanyInfo company = userStart.getCompany();
        			if(null != company && S_CompanyType.Agency.equals(company.getTheType()) )
        			{
        				boolean isUse = true;
            			//过滤区域授权
            			Long[] projectInfoIdArr = model.getProjectInfoIdArr();
            			for (int i = 0; i < projectInfoIdArr.length; i++) {
            				if(projectInfoIdArr[i].equals(tgxy_TripleAgreement.getProject().getTableId()))
    						{
    							isUse = false;
    							break;
    						}
    					}
            			
            			if(isUse)
            			{
            				Long[] buildingInfoIdIdArr = model.getBuildingInfoIdIdArr();
                			for (int i = 0; i < buildingInfoIdIdArr.length; i++) {
        						if(buildingInfoIdIdArr[i].equals(tgxy_TripleAgreement.getBuildingInfo().getTableId()))
        						{
        							isUse = false;
        							break;
        						}
        					}
            			}
            			
            			if(isUse)
            			{
            				continue;
            			}
        			}
        			else if(S_CompanyType.Zhengtai.equals(company.getTheType()))
        			{
        				
        			}
        			else
        			{
        				if(!company.getTableId().equals(tgxy_TripleAgreement.getProject().getDevelopCompany().getTableId()))
        				{
        					continue;
        				}
        			}
        			
        			Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
    				tgxy_CoopAgreementSettleDtlForm.setTheState(S_TheState.Normal);
    				tgxy_CoopAgreementSettleDtlForm.setTgxy_TripleAgreement(tgxy_TripleAgreement);
    				
    				Integer detailCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));

    				if(detailCount > 0)
    				{
    					continue;
    				}
    				
    				// 关联户室
    				Empj_HouseInfo house = tgxy_TripleAgreement.getHouse();
    				
    				Empj_UnitInfo unit = new Empj_UnitInfo();
    				if(null != house)
    				{
    					// 关联单元
    					unit = house.getUnitInfo();
    				}
    				
    				if(null != house.getSettlementStateOfTripleAgreement() && 1 == house.getSettlementStateOfTripleAgreement())
    				{
    					continue;
    				}
    				
    				house.setSettlementStateOfTripleAgreement(1);
    				
    				empj_HouseInfoDao.save(house);
    				
    				Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
    				tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Normal);
//    				tgxy_CoopAgreementSettleDtl.setBusiState(busiState);
    				tgxy_CoopAgreementSettleDtl.setUserStart(userStart);
    				tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(System.currentTimeMillis());
    				tgxy_CoopAgreementSettleDtl.setUserUpdate(userStart);
    				tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
    				tgxy_CoopAgreementSettleDtl.setUserRecord(tgxy_TripleAgreement.getUserRecord());
    				tgxy_CoopAgreementSettleDtl.setRecordTimeStamp(tgxy_TripleAgreement.getRecordTimeStamp());
    				tgxy_CoopAgreementSettleDtl.setMainTable(tgxy_CoopAgreementSettle);
    				tgxy_CoopAgreementSettleDtl.seteCode(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
    				tgxy_CoopAgreementSettleDtl.setAgreementDate(tgxy_TripleAgreement.getTripleAgreementTimeStamp()); //备案日期
    				tgxy_CoopAgreementSettleDtl.setSeller(tgxy_TripleAgreement.getSellerName());
    				tgxy_CoopAgreementSettleDtl.setBuyer(tgxy_TripleAgreement.getBuyerName());
    				tgxy_CoopAgreementSettleDtl.setProject(tgxy_TripleAgreement.getProject());
    				tgxy_CoopAgreementSettleDtl.setTheNameOfProject(tgxy_TripleAgreement.getTheNameOfProject());
    				tgxy_CoopAgreementSettleDtl.setBuildingInfo(tgxy_TripleAgreement.getBuildingInfo());
    				tgxy_CoopAgreementSettleDtl.seteCodeOfBuilding(tgxy_TripleAgreement.geteCodeOfBuilding());
    				tgxy_CoopAgreementSettleDtl.seteCodeFromConstruction(tgxy_TripleAgreement.geteCodeFromConstruction());
    				tgxy_CoopAgreementSettleDtl.setUnitInfo(unit);
    				tgxy_CoopAgreementSettleDtl.setHouseInfo(house);
    				tgxy_CoopAgreementSettleDtl.setTgxy_TripleAgreement(tgxy_TripleAgreement);
    				
    				tgxy_CoopAgreementSettleDtlDao.save(tgxy_CoopAgreementSettleDtl);
    				
    				index++;
    			}
    			
			}
        	
        	if(index == 0)
			{
				tgxy_CoopAgreementSettle.setTheState(S_TheState.Deleted);
				
				tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
				return MyBackInfo.fail(properties, "日期区间内不存在未结算三方协议，请重新选择区间后进行保存！");
			}
        	
        }
        else
		{
			return MyBackInfo.fail(properties, "日期区间内不存在未结算的三方协议，请重新选择区间！");
		}*/
        
	
		/*Tgxy_TripleAgreementForm tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
		tgxy_TripleAgreementForm.setEndTime(myDatetime.stringToLong(myDatetime.getSpecifiedDayAfter(endSettlementDate)));
		tgxy_TripleAgreementForm.setBeginTime(myDatetime.stringToLong(startSettlementDate));
		tgxy_TripleAgreementForm.setEndDate(endSettlementDate);
		tgxy_TripleAgreementForm.setStartDate(startSettlementDate);
		tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
		tgxy_TripleAgreementForm.setBusiState("(1,2)");
//		tgxy_TripleAgreementForm.setCompany(userStart.getCompany());
		
		tgxy_TripleAgreementForm.setProjectInfoIdArr(model.getProjectInfoIdArr());
		tgxy_TripleAgreementForm.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
		tgxy_TripleAgreementForm.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
		
		tgxy_TripleAgreementForm.setApprovalState(S_ApprovalState.Completed);
		
		
		 * xsz by time 2019-2-25 18:29:19
		 * 判断当前登录人是否是代理公司人员
		 
		Emmp_CompanyInfo company = userStart.getCompany();
		if(null != company && ( S_CompanyType.Agency.equals(company.getTheType()) || S_CompanyType.Zhengtai.equals(company.getTheType())) )
		{
			tgxy_TripleAgreementForm.setIsAgency(null);
		}
		else
		{
			tgxy_TripleAgreementForm.setIsAgency("0");
			tgxy_TripleAgreementForm.setCompanyId(company.getTableId());
		}
		
		Integer totalCount = tgxy_TripleAgreementDao
				.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getSpecialHQL(), tgxy_TripleAgreementForm));
		
		List<Tgxy_TripleAgreement> tgxy_TripleAgreementList;
		if (totalCount > 0)
		{
			
			Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = new Tgxy_CoopAgreementSettle();
			tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
//			tgxy_CoopAgreementSettle.setBusiState(busiState);
			tgxy_CoopAgreementSettle.setUserStart(userStart);
			tgxy_CoopAgreementSettle.setCreateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.setUserUpdate(userStart);
			tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
//			tgxy_CoopAgreementSettle.setUserRecord(userRecord);
//			tgxy_CoopAgreementSettle.setRecordTimeStamp(recordTimeStamp);
			tgxy_CoopAgreementSettle.seteCode(sm_BusinessCodeGetService.getCoopAgreementSettleEcode(BUSI_CODE));
			tgxy_CoopAgreementSettle.setSignTimeStamp(signTimeStamp);
			tgxy_CoopAgreementSettle.setAgentCompany(agentCompany);
			tgxy_CoopAgreementSettle.setCompanyName(agentCompany.getTheName());
			tgxy_CoopAgreementSettle.setApplySettlementDate(applySettlementDate);
			tgxy_CoopAgreementSettle.setStartSettlementDate(startSettlementDate);
			tgxy_CoopAgreementSettle.setEndSettlementDate(endSettlementDate);
			tgxy_CoopAgreementSettle.setProtocolNumbers(protocolNumbers);
			tgxy_CoopAgreementSettle.setSettlementState(settlementState);
			tgxy_CoopAgreementSettle.setBeforeNumbers(beforeNumbers);
			
			entity = tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle) ;
			
			tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(
					tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getSpecialHQL(), tgxy_TripleAgreementForm));
			
			int index = 0;
			for(Tgxy_TripleAgreement tgxy_TripleAgreement : tgxy_TripleAgreementList)
			{
				
				Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
				tgxy_CoopAgreementSettleDtlForm.setTheState(S_TheState.Normal);
				tgxy_CoopAgreementSettleDtlForm.setTgxy_TripleAgreement(tgxy_TripleAgreement);
				
				Integer detailCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));

				if(detailCount > 0)
				{
					totalCount --;
					continue;
				}
			
				index++;
				// 关联户室
				Empj_HouseInfo house = tgxy_TripleAgreement.getHouse();
				
				Empj_UnitInfo unit = new Empj_UnitInfo();
				if(null != house)
				{
					// 关联单元
					unit = house.getUnitInfo();
				}
				// 如果已经结算了，就跳过
				if(null != house.getSettlementStateOfTripleAgreement() && 1 == house.getSettlementStateOfTripleAgreement())
				{
					totalCount --;
					continue;
				}
				
				house.setSettlementStateOfTripleAgreement(1);
				
				empj_HouseInfoDao.save(house);
				
				Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
				tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Normal);
//				tgxy_CoopAgreementSettleDtl.setBusiState(busiState);
				tgxy_CoopAgreementSettleDtl.setUserStart(userStart);
				tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(System.currentTimeMillis());
				tgxy_CoopAgreementSettleDtl.setUserUpdate(userStart);
				tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgxy_CoopAgreementSettleDtl.setUserRecord(tgxy_TripleAgreement.getUserRecord());
				tgxy_CoopAgreementSettleDtl.setRecordTimeStamp(tgxy_TripleAgreement.getRecordTimeStamp());
				tgxy_CoopAgreementSettleDtl.setMainTable(tgxy_CoopAgreementSettle);
				tgxy_CoopAgreementSettleDtl.seteCode(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
				tgxy_CoopAgreementSettleDtl.setAgreementDate(tgxy_TripleAgreement.getTripleAgreementTimeStamp()); //备案日期
				tgxy_CoopAgreementSettleDtl.setSeller(tgxy_TripleAgreement.getSellerName());
				tgxy_CoopAgreementSettleDtl.setBuyer(tgxy_TripleAgreement.getBuyerName());
				tgxy_CoopAgreementSettleDtl.setProject(tgxy_TripleAgreement.getProject());
				tgxy_CoopAgreementSettleDtl.setTheNameOfProject(tgxy_TripleAgreement.getTheNameOfProject());
				tgxy_CoopAgreementSettleDtl.setBuildingInfo(tgxy_TripleAgreement.getBuildingInfo());
				tgxy_CoopAgreementSettleDtl.seteCodeOfBuilding(tgxy_TripleAgreement.geteCodeOfBuilding());
				tgxy_CoopAgreementSettleDtl.seteCodeFromConstruction(tgxy_TripleAgreement.geteCodeFromConstruction());
				tgxy_CoopAgreementSettleDtl.setUnitInfo(unit);
				tgxy_CoopAgreementSettleDtl.setHouseInfo(house);
				tgxy_CoopAgreementSettleDtl.setTgxy_TripleAgreement(tgxy_TripleAgreement);
				
				tgxy_CoopAgreementSettleDtlDao.save(tgxy_CoopAgreementSettleDtl);		
			}
			
			if(index == 0)
			{
				tgxy_CoopAgreementSettle.setTheState(S_TheState.Deleted);
				
				tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
				return MyBackInfo.fail(properties, "日期区间内不存在未结算三方协议，请重新选择区间后进行保存！");
			}

		}
		else
		{
			return MyBackInfo.fail(properties, "日期区间内不存在未结算的三方协议，请重新选择区间！");
		}*/
		
		// 保存附件
		// "[{\"sourseType\":\"营业执照\",\"theLink\":\"http://192.168.1.8:19001/OssSave/bananaUpload/201808/23/eabf01f1c8214073a012fb6c465af7b4.png\",\"fileType\":\"png\",\"theSize\":\"23.85KB\",\"remark\":\"\",\"id\":\"eabf01f1c8214073a012fb6c465af7b4\"},{\"sourseType\":\"营业执照\",\"theLink\":\"http://192.168.1.8:19001/OssSave/bananaUpload/201808/23/eabf01f1c8214073a012fb6c465af7b4.png\",\"fileType\":\"png\",\"theSize\":\"23.85KB\",\"remark\":\"\",\"id\":\"eabf01f1c8214073a012fb6c465af7b4\"}]";

		
		// 附件信息
		/*String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();
		}

		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

		if (null != gasList && gasList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				sm_Attachment.setSourceId(entity.toString());
				sm_Attachment.setTheState(S_TheState.Normal);
				smAttachmentDao.save(sm_Attachment);
			}
		}*/
				
		properties.put("tableId", entity);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}
}

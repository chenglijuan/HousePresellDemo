package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_CoopAgreementSettleDetailListService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{		
		Properties properties = new MyProperties();
		
		Sm_User userStart = model.getUser(); // admin
		
		String currentTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
		
		// 备案起始区间
		String startSettlementDate = model.getStartSettlementDate();
		String endSettlementDate = model.getEndSettlementDate();
		if(null == startSettlementDate || startSettlementDate.length() == 0)
		{
			startSettlementDate  = currentTime;
		}
		if(null == endSettlementDate || endSettlementDate.length() == 0)
		{
			endSettlementDate  = myDatetime.getSpecifiedDayAfter(currentTime);
		}
		
		List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
		
		
		try {
			Map<String, Object> map = tgxy_CoopAgreementSettleDtlDao.query_Coopagreementdtllist(model.getUserId(), model.getUser().getCompany().getTableId(), MyDatetime.getInstance().stringToLong(startSettlementDate), MyDatetime.getInstance().stringToLong(endSettlementDate));
			
			tgxy_CoopAgreementSettleDtlList = (List<Tgxy_CoopAgreementSettleDtl>) map.get("list");
			String sign = (String) map.get("sign");
			String info = (String) map.get("info");
			
			properties.put("beforeNumbers", map.get("beforeNumbers"));
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		/*//过滤三方协议业务的提交日期
        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("06110301");
        afModel.setOrderBy("createTimeStamp");
        afModel.setStartTimeStamp(MyDatetime.getInstance().stringToLong(startSettlementDate));
        afModel.setEndTimeStamp(MyDatetime.getInstance().stringToLong(endSettlementDate));
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicStartHQL(), afModel));
        if(!afList.isEmpty())
        {
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
    				
    				Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
    				tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Normal);
    				tgxy_CoopAgreementSettleDtl.setUserStart(userStart);
    				tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(System.currentTimeMillis());
    				tgxy_CoopAgreementSettleDtl.setUserUpdate(userStart);
    				tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
    				tgxy_CoopAgreementSettleDtl.setRecordTimeStamp(tgxy_TripleAgreement.getRecordTimeStamp());
    				tgxy_CoopAgreementSettleDtl.seteCode(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
    				tgxy_CoopAgreementSettleDtl.setAgreementDate(MyDatetime.getInstance().dateToString(sm_ApprovalProcess_AF.getStartTimeStamp())); //备案日期
    				tgxy_CoopAgreementSettleDtl.setSeller(tgxy_TripleAgreement.getSellerName());
    				tgxy_CoopAgreementSettleDtl.setBuyer(tgxy_TripleAgreement.getBuyerName());
    				tgxy_CoopAgreementSettleDtl.setProject(tgxy_TripleAgreement.getProject());
    				tgxy_CoopAgreementSettleDtl.setTheNameOfProject(tgxy_TripleAgreement.getTheNameOfProject());
    				tgxy_CoopAgreementSettleDtl.setBuildingInfo(tgxy_TripleAgreement.getBuildingInfo());
    				tgxy_CoopAgreementSettleDtl.seteCodeOfBuilding(tgxy_TripleAgreement.geteCodeOfBuilding());
    				tgxy_CoopAgreementSettleDtl.seteCodeFromConstruction(tgxy_TripleAgreement.geteCodeFromConstruction());
    				tgxy_CoopAgreementSettleDtl.setUnitInfo(unit);
    				tgxy_CoopAgreementSettleDtl.setHouseInfo(house);
    				tgxy_CoopAgreementSettleDtl.setTableId(tgxy_TripleAgreement.getTableId());
    				tgxy_CoopAgreementSettleDtl.setHouseInfoName(house.getRoomId());
    				
    				tgxy_CoopAgreementSettleDtlList.add(tgxy_CoopAgreementSettleDtl);
        			
        		}
        		
			}
        }
        else
        {
        	tgxy_CoopAgreementSettleDtlList = new ArrayList<>();
        }*/
        
        	
        	
        	
        	
		
		
		/*Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
			
		
		
		Tgxy_TripleAgreementForm tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
		
//		tgxy_TripleAgreementForm.setEndDate(endSettlementDate);
//		tgxy_TripleAgreementForm.setStartDate(startSettlementDate);
		tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
		tgxy_TripleAgreementForm.setBusiState("(1,2)");
		
		tgxy_TripleAgreementForm.setProjectInfoIdArr(model.getProjectInfoIdArr());
		tgxy_TripleAgreementForm.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
		tgxy_TripleAgreementForm.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
		
		tgxy_TripleAgreementForm.setApprovalState(S_ApprovalState.Completed);
		
		
		 * xsz by time 2019-2-25 18:29:19
		 * 判断当前登录人是否是代理公司人员
		 
		
		
		Integer totalCount = tgxy_TripleAgreementDao
				.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getSpecialHQL(), tgxy_TripleAgreementForm));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_TripleAgreement> tgxy_TripleAgreementList;
		if (totalCount > 0)
		{
			tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(
					tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getSpecialHQL(), tgxy_TripleAgreementForm));
			
			for(int i = 0; i<tgxy_TripleAgreementList.size();i++)
//			for(Tgxy_TripleAgreement tgxy_TripleAgreement : tgxy_TripleAgreementList)
			{
				
				*//**
		         * xsz by time 2019-7-10 11:12:19
		         * 
		         *//*
				Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = new Sm_ApprovalProcess_AF();
		        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
		        afModel.setTheState(S_TheState.Normal);
		        afModel.setBusiCode("06110301");
		        afModel.setSourceId(tgxy_TripleAgreementList.get(i).getTableId());
		        afModel.setOrderBy("createTimeStamp");
		        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
		        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
		        if(null == afList || afList.size() == 0)
		        {
		        	totalCount --;
		        	continue;
		        }
		        else
		        {
		        	sm_ApprovalProcess_AF = afList.get(0);
		        	
		        	if(MyDatetime.getInstance().stringToLong(startSettlementDate)>sm_ApprovalProcess_AF.getStartTimeStamp()||MyDatetime.getInstance().stringToLong(endSettlementDate)<sm_ApprovalProcess_AF.getStartTimeStamp())
		        	{
		        		totalCount --;
		        		continue;
		        	}
		        	
		        }
				
				Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
				tgxy_CoopAgreementSettleDtlForm.setTheState(S_TheState.Normal);
				tgxy_CoopAgreementSettleDtlForm.setTgxy_TripleAgreement(tgxy_TripleAgreementList.get(i));
				
				Integer detailCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));

				if(detailCount > 0)
				{
					totalCount --;
					continue;
				}
				
				// 关联户室
				Empj_HouseInfo house = tgxy_TripleAgreementList.get(i).getHouse();
				
				Empj_UnitInfo unit = new Empj_UnitInfo();
				if(null != house)
				{
					// 关联单元
					unit = house.getUnitInfo();
				}
				
				if(null != house.getSettlementStateOfTripleAgreement() && 1 == house.getSettlementStateOfTripleAgreement())
				{
					totalCount --;
					continue;
				}
				
				Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
				tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Normal);
//				tgxy_CoopAgreementSettleDtl.setBusiState(busiState);
				tgxy_CoopAgreementSettleDtl.setUserStart(userStart);
				tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(System.currentTimeMillis());
				tgxy_CoopAgreementSettleDtl.setUserUpdate(userStart);
				tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
//				tgxy_CoopAgreementSettleDtl.setUserRecord(userRecord);
				tgxy_CoopAgreementSettleDtl.setRecordTimeStamp(tgxy_TripleAgreementList.get(i).getRecordTimeStamp());
//				tgxy_CoopAgreementSettleDtl.setMainTable(tgxy_CoopAgreementSettle);
				tgxy_CoopAgreementSettleDtl.seteCode(tgxy_TripleAgreementList.get(i).geteCodeOfTripleAgreement());
				tgxy_CoopAgreementSettleDtl.setAgreementDate(MyDatetime.getInstance().dateToString2(sm_ApprovalProcess_AF.getStartTimeStamp())); //备案日期
				tgxy_CoopAgreementSettleDtl.setSeller(tgxy_TripleAgreementList.get(i).getSellerName());
				tgxy_CoopAgreementSettleDtl.setBuyer(tgxy_TripleAgreementList.get(i).getBuyerName());
				tgxy_CoopAgreementSettleDtl.setProject(tgxy_TripleAgreementList.get(i).getProject());
				tgxy_CoopAgreementSettleDtl.setTheNameOfProject(tgxy_TripleAgreementList.get(i).getTheNameOfProject());
				tgxy_CoopAgreementSettleDtl.setBuildingInfo(tgxy_TripleAgreementList.get(i).getBuildingInfo());
				tgxy_CoopAgreementSettleDtl.seteCodeOfBuilding(tgxy_TripleAgreementList.get(i).geteCodeOfBuilding());
				tgxy_CoopAgreementSettleDtl.seteCodeFromConstruction(tgxy_TripleAgreementList.get(i).geteCodeFromConstruction());
				tgxy_CoopAgreementSettleDtl.setUnitInfo(unit);
				tgxy_CoopAgreementSettleDtl.setHouseInfo(house);
				tgxy_CoopAgreementSettleDtl.setTableId(tgxy_TripleAgreementList.get(i).getTableId());
//				tgxy_CoopAgreementSettleDtl.setUnitInfoName(unit.getTheName());
				tgxy_CoopAgreementSettleDtl.setHouseInfoName(house.getRoomId());
				
				tgxy_CoopAgreementSettleDtlList.add(tgxy_CoopAgreementSettleDtl);
			}		
		}
		else
		{
			tgxy_TripleAgreementList = new ArrayList<Tgxy_TripleAgreement>();
		}	
		
		totalCount = tgxy_CoopAgreementSettleDtlList.size();
		
		totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		int startPage = (pageNumber - 1 ) * countPerPage;
		int endPage = pageNumber * countPerPage;
		
		if(endPage > tgxy_CoopAgreementSettleDtlList.size())
		{
			endPage = tgxy_CoopAgreementSettleDtlList.size();
		}
//		tgxy_CoopAgreementSettleDtlList.subList(startPage, endPage);
*/		
		
		properties.put("tgxy_CoopAgreementSettleDtlList", tgxy_CoopAgreementSettleDtlList);
		properties.put("protocolNumbers", tgxy_CoopAgreementSettleDtlList.size());
		properties.put(S_NormalFlag.totalPage, 1);
		properties.put(S_NormalFlag.pageNumber, 1);
		properties.put(S_NormalFlag.countPerPage, 1);
		properties.put(S_NormalFlag.totalCount, tgxy_CoopAgreementSettleDtlList.size());
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleDetailService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();

	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_CoopAgreementSettleId = model.getTableId();
		
		Sm_User userStart = model.getUser();
		
		String currentTime = myDatetime.dateToSimpleString(System.currentTimeMillis());
		
		Integer pageNumber  = 0;
		Integer countPerPage = 0 ;
		Integer totalCount = 0;
		Integer totalPage = 0;
		
		// 初始化接收参数
		// 主表
		Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = new Tgxy_CoopAgreementSettle();
		// 子表
		List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
		// 附件
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
		// 附件类型
		List<Sm_AttachmentCfg> smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
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
			
		// 新增
		if( null == tgxy_CoopAgreementSettleId || tgxy_CoopAgreementSettleId < 0)
		{
			tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
			tgxy_CoopAgreementSettle.setUserStart(userStart);
			tgxy_CoopAgreementSettle.setCreateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.setUserUpdate(userStart);
			tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.seteCode("");
			tgxy_CoopAgreementSettle.setSignTimeStamp(currentTime);
			tgxy_CoopAgreementSettle.setAgentCompany(userStart.getCompany());
			tgxy_CoopAgreementSettle.setApplySettlementDate(currentTime);
			tgxy_CoopAgreementSettle.setStartSettlementDate(startSettlementDate);
			tgxy_CoopAgreementSettle.setEndSettlementDate(startSettlementDate);
			tgxy_CoopAgreementSettle.setCompanyName(userStart.getCompany().getTheName());
			tgxy_CoopAgreementSettle.setSettlementState(0);

			/*
			pageNumber = Checker.getInstance().checkPageNumber(1);
			countPerPage = Checker.getInstance().checkCountPerPage(20);
			
			Tgxy_TripleAgreementForm tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
			tgxy_TripleAgreementForm.setEndTime(myDatetime.stringToLong(endSettlementDate));
			tgxy_TripleAgreementForm.setBeginTime(myDatetime.stringToLong(startSettlementDate));
			tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
			tgxy_TripleAgreementForm.setBusiState("(1,2)");
			tgxy_TripleAgreementForm.setCompany(userStart.getCompany());
			
			totalCount = tgxy_TripleAgreementDao
					.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getSpecialHQL(), tgxy_TripleAgreementForm));
			
			totalPage = totalCount / countPerPage;
			if (totalCount % countPerPage > 0) totalPage++;
			if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
			
			
			
			List<Tgxy_TripleAgreement> tgxy_TripleAgreementList;
			if (totalCount > 0)
			{
				tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(
						tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getSpecialHQL(), tgxy_TripleAgreementForm), pageNumber, countPerPage);
				
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
						totalCount --;
						continue;
					}
					
					Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl = new Tgxy_CoopAgreementSettleDtl();
					tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Normal);
					tgxy_CoopAgreementSettleDtl.setUserStart(userStart);
					tgxy_CoopAgreementSettleDtl.setCreateTimeStamp(System.currentTimeMillis());
					tgxy_CoopAgreementSettleDtl.setUserUpdate(userStart);
					tgxy_CoopAgreementSettleDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
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
					tgxy_CoopAgreementSettleDtl.setTableId(tgxy_TripleAgreement.getTableId());
					tgxy_CoopAgreementSettleDtl.setHouseInfoName(house.getRoomId());
					
					tgxy_CoopAgreementSettleDtlList.add(tgxy_CoopAgreementSettleDtl);
				}		
			}
			else
			{
				tgxy_TripleAgreementList = new ArrayList<Tgxy_TripleAgreement>();
			}*/
			tgxy_CoopAgreementSettle.setProtocolNumbers(0); // 三方协议有效份数
		}
		else //修改
		{
			tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tgxy_CoopAgreementSettleId);
			if(tgxy_CoopAgreementSettle == null || S_TheState.Deleted.equals(tgxy_CoopAgreementSettle.getTheState()))
			{
				return MyBackInfo.fail(properties, "该记录不存在，请联系管理员！");
			}
						
			Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
			tgxy_CoopAgreementSettleDtlForm.setMainTable(tgxy_CoopAgreementSettle);
			tgxy_CoopAgreementSettleDtlForm.setTheState(S_TheState.Normal);
			
			pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
			countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
			
			totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));
			
			totalPage = totalCount / countPerPage;
			if (totalCount % countPerPage > 0) totalPage++;
			if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
			
			if(totalCount > 0)
			{
				tgxy_CoopAgreementSettleDtlList = tgxy_CoopAgreementSettleDtlDao.findByPage(tgxy_CoopAgreementSettleDtlDao.getQuery(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm), pageNumber, countPerPage);	
				
				for(Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl : tgxy_CoopAgreementSettleDtlList)
				{
//					tgxy_CoopAgreementSettleDtl.setUnitInfoName(tgxy_CoopAgreementSettleDtl.getUnitInfo().getTheName());
					tgxy_CoopAgreementSettleDtl.setHouseInfoName(tgxy_CoopAgreementSettleDtl.getHouseInfo().getTheNameOfRoomId());
				}				
			}
			else
			{
				tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
			}
						
			Sm_AttachmentForm from = new Sm_AttachmentForm();
			String sourceId = String.valueOf(tgxy_CoopAgreementSettleId);
			from.setSourceId(sourceId);
			from.setTheState(S_TheState.Normal);

			// 查询附件
			smAttachmentList = smAttachmentDao.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), from));

			if (null == smAttachmentList || smAttachmentList.size() == 0)
			{
				smAttachmentList = new ArrayList<Sm_Attachment>();
			}
		}
		
		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
		attachmentCfgForm.setBusiType(model.getBusiCode());
		smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();

				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.getTableId() == cfgTableId)
					{
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0)
						{
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}
		
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_CoopAgreementSettle", tgxy_CoopAgreementSettle);
		properties.put("smAttachmentList", smAttachmentList);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put("tgxy_CoopAgreementSettleDtlList", tgxy_CoopAgreementSettleDtlList);

		return properties;
	}
}

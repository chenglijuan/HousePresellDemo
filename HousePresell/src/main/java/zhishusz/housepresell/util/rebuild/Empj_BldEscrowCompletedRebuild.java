package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_IsNeedBackup;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDouble;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;

import java.util.List;

import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyDatetime;

/*
 * Rebuilder：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_BldEscrowCompletedRebuild extends RebuilderBase<Empj_BldEscrowCompleted>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

	@Override
	public Properties getSimpleInfo(Empj_BldEscrowCompleted object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		properties.put("eCode", object.geteCode());
		properties.put("eCodeFromDRAD", object.geteCodeFromDRAD());
		
		properties.put("hasPush", StrUtil.isBlank(object.getHasPush()) ? "0" : object.getHasPush());
		
		Emmp_CompanyInfo developCompany = object.getDevelopCompany();
		if (developCompany != null)
		{
			properties.put("developCompanyId", developCompany.getTableId());
			properties.put("developCompanyName", developCompany.getTheName());
		}
		Empj_ProjectInfo project = object.getProject();
		if (project != null)
		{
			properties.put("projectId", project.getTableId());
			properties.put("projectName", project.getTheName());
		}
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList = object.getEmpj_BldEscrowCompleted_DtlList();
		if (empj_BldEscrowCompleted_DtlList !=null && empj_BldEscrowCompleted_DtlList.size() > 0)
		{
			List<String> buildingECodeList = new ArrayList<String>();
			Double allBuildingArea = 0.0;
			Double allEscrowArea = 0.0;
			for (Empj_BldEscrowCompleted_Dtl itemBldEscrowCompleted_Dtl: empj_BldEscrowCompleted_DtlList)
			{
				Empj_BuildingInfo building = itemBldEscrowCompleted_Dtl.getBuilding();
				if (building != null)
				{
					MyDouble muDouble = MyDouble.getInstance();
					String ecodeFromConstruction = building.geteCodeFromConstruction();
					if (ecodeFromConstruction != null)
					{
						buildingECodeList.add(ecodeFromConstruction);
					}
					Double buildingArea = building.getBuildingArea();
					if (buildingArea != null)
					{
						allBuildingArea += muDouble.getShort(buildingArea, 2); //建筑面积
					}
					Double EscrowArea = building.getEscrowArea();
					if (EscrowArea != null)
					{
						allEscrowArea += muDouble.getShort(EscrowArea, 2); 	 //托管面积
					}
				}
			}
			String buildingName = StringUtils.join(buildingECodeList, "、");
			properties.put("buildingName", buildingName);
			properties.put("allBuildingArea", allBuildingArea); //建筑面积
			properties.put("allEscrowArea", allEscrowArea);     //托管面积

			//查找申请单
			Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
			sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
			sm_approvalProcess_afForm.setBusiCode(S_BusiCode.busiCode_03030102);
			sm_approvalProcess_afForm.setSourceId(object.getTableId());
			Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));

			if(sm_approvalProcess_af !=null)
			{
				properties.put("afId",sm_approvalProcess_af.getTableId()); // 申请单id
				properties.put("workflowId",sm_approvalProcess_af.getCurrentIndex());//当前结点Id
				properties.put("busiType",sm_approvalProcess_af.getBusiType());  //业务类型
				properties.put("busiCode",sm_approvalProcess_af.getBusiCode()); //业务编码
			}
		}
		
		return properties;
	}
	
	@Override
	public Properties getDetail(Empj_BldEscrowCompleted object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		properties.put("eCode", object.geteCode());
		properties.put("eCodeFromDRAD", object.geteCodeFromDRAD());
		Emmp_CompanyInfo companyInfo = object.getDevelopCompany();
		if (companyInfo != null)
		{
			properties.put("developCompanyId", companyInfo.getTableId());
			properties.put("developCompanyName", companyInfo.getTheName());			
		}
		Empj_ProjectInfo projectInfo = object.getProject();
		if (projectInfo != null)
		{
			properties.put("projectId", projectInfo.getTableId());
			properties.put("projectName", projectInfo.getTheName());	
			properties.put("address", projectInfo.getAddress());
			Sm_CityRegionInfo cityRegionInfo = object.getProject().getCityRegion();
			if (cityRegionInfo != null)
			{
				properties.put("cityRegionName", cityRegionInfo.getTheName());
			}
		}
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate == null)
		{
			userUpdate =  object.getUserStart();
		}
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{
			properties.put("userRecordId", userRecord.getTableId());
			properties.put("userRecordName", userRecord.getTheName());
		}

		Long operationTimeStamp = object.getLastUpdateTimeStamp();
		if (operationTimeStamp == null || operationTimeStamp < 1)
		{
			operationTimeStamp = object.getCreateTimeStamp();
		}
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp",  MyDatetime.getInstance().dateToSimpleString(operationTimeStamp));
		properties.put("recordTimeStamp",  MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		properties.put("remark", object.getRemark());
		
		properties.put("hasFormula", StrUtil.isBlank(object.getHasFormula()) ? "0" : object.getHasFormula());
		properties.put("hasPush", StrUtil.isBlank(object.getHasPush()) ? "0" : object.getHasPush());
		properties.put("webSite", StrUtil.isBlank(object.getWebSite()) ? "" : object.getWebSite());
		
		
		/**
         * xsz by time 2019-4-30 16:22:49
         * 操作时间取申请单的提交时间
         * 
         */
        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("03030102");
        afModel.setSourceId(object.getTableId());
        afModel.setOrderBy("createTimeStamp");
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
        if(null == afList || afList.size() == 0)
        {
        	properties.put("lastUpdateTimeStamp", "-");
        }
        else
        {
        	Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = afList.get(0);
        	properties.put("lastUpdateTimeStamp",
        			myDatetime.dateToSimpleString(sm_ApprovalProcess_AF.getStartTimeStamp()));// 最后修改日期
        }
		
		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties getDetailForApprovalProcess(Empj_BldEscrowCompleted object)
	{
		if (object == null) return null;
		Properties properties = getDetail(object);
		//此处不需要从OSS上拿取审批流中字段，备注：审批流-变更字段.xlsx文件中托管终止无可变更字段

//		//查待提交的申请单
//		Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
//		sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
//		sm_ApprovalProcess_AFForm.setBusiState("待提交");
//		sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
//		sm_ApprovalProcess_AFForm.setBusiCode("03030102");
//		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//
//		if(sm_ApprovalProcess_AF == null)
//		{
//			sm_ApprovalProcess_AFForm.setBusiState("审核中");
//			sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//
//			if(sm_ApprovalProcess_AF == null)
//			{
//				return properties;
//			}
//		}
//
//		Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
//		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_ApprovalProcess_AF.getConfiguration();
//		Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(currentNode);
//		Boolean isNeedBackup = null;
//		if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
//		{
//			if(S_IsNeedBackup.Yes.equals(sm_approvalProcess_cfg.getIsNeedBackup()))
//			{
//				isNeedBackup = true;
//			}
//		}
//		else
//		{
//			isNeedBackup = false;
//		}
//
//		properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮

		return properties;
	}

		@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_BldEscrowCompleted> empj_BldEscrowCompletedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_BldEscrowCompletedList != null)
		{
			for(Empj_BldEscrowCompleted object:empj_BldEscrowCompletedList)
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
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("eCodeOfProject", object.geteCodeOfProject());
				properties.put("eCodeFromDRAD", object.geteCodeFromDRAD());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
}

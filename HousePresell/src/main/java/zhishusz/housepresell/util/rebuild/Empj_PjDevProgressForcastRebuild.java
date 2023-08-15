package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_PjDevProgressForcastRebuild extends RebuilderBase<Empj_PjDevProgressForcast>
{
	@Override
	public Properties getSimpleInfo(Empj_PjDevProgressForcast object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("patrolTimestamp", MyDatetime.getInstance().dateToSimpleString(object.getPatrolTimestamp()));
		properties.put("busiState", object.getBusiState());
		Emmp_CompanyInfo companyInfo = object.getDevelopCompany();
		if (companyInfo != null)
		{
			properties.put("developCompanyName", companyInfo.getTheName());
		}
		Empj_ProjectInfo projectInfo = object.getProject();
		if (projectInfo != null)
		{
			properties.put("projectName", projectInfo.getTheName());
		}
		Empj_BuildingInfo buildingInfo = object.getBuilding();
		if (buildingInfo != null)
		{
			properties.put("buildingId", buildingInfo.getTableId());
			properties.put("eCodeOfBuilding", buildingInfo.geteCode());
			properties.put("eCodeFromConstruction", buildingInfo.geteCodeFromConstruction());
			properties.put("eCodeFromPublicSecurity", buildingInfo.geteCodeFromPublicSecurity());
			Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();    //关联楼幢账户
			if (buildingAccount != null)
			{
				properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress()); //当前形象进度
			}
		}

		return properties;
	}

	@Override
	public Properties getDetail(Empj_PjDevProgressForcast object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());

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
		properties.put("recordTimeStamp",  MyDatetime.getInstance().dateToSimpleString(object.getRecordTimeStamp()));

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
		}
		Empj_BuildingInfo buildingInfo = object.getBuilding();
		if (buildingInfo != null)
		{
			properties.put("buildingId", buildingInfo.getTableId());
			properties.put("eCodeOfBuilding", buildingInfo.geteCode());
			properties.put("position", buildingInfo.getPosition());
			properties.put("upfloorNumber", MyDouble.getInstance().getShort(buildingInfo.getUpfloorNumber(), 0));

			Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();    //关联楼幢账户
			if (buildingAccount != null)
			{
				properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress()); //当前形象进度
				properties.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio());	 //当前受限比例
				Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl = buildingAccount.getBldLimitAmountVerDtl();
				if (bldLimitAmountVer_afDtl != null)
				{
					properties.put("currentBldLimitAmountVerAfDtlId", bldLimitAmountVer_afDtl.getTableId()); //受限额度节点ID
					properties.put("newCurrentFigureProgress", bldLimitAmountVer_afDtl.getStageName()); //当前形象进度
					properties.put("newCurrentLimitedRatio", bldLimitAmountVer_afDtl.getLimitedAmount()); //当前受限比例
				}
			}
		}
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
		properties.put("payoutType", object.getPayoutType());
//		properties.put("currentFigureProgress", object.getCurrentFigureProgress());
		properties.put("currentBuildProgress", object.getCurrentBuildProgress());
		properties.put("patrolPerson", object.getPatrolPerson());
		properties.put("patrolTimestamp", MyDatetime.getInstance().dateToSimpleString(object.getPatrolTimestamp()));
		properties.put("patrolInstruction", object.getPatrolInstruction());
		properties.put("remark", object.getRemark());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_PjDevProgressForcast> empj_PjDevProgressForcastList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_PjDevProgressForcastList != null)
		{
			for(Empj_PjDevProgressForcast object:empj_PjDevProgressForcastList)
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
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
				properties.put("streetInfo", object.getStreetInfo());
				properties.put("streetInfoId", object.getStreetInfo().getTableId());
				properties.put("theNameOfStreet", object.getTheNameOfStreet());
				properties.put("payoutType", object.getPayoutType());
				properties.put("currentFigureProgress", object.getCurrentFigureProgress());
				properties.put("currentBuildProgress", object.getCurrentBuildProgress());
				properties.put("patrolPerson", object.getPatrolPerson());
				properties.put("patrolTimestamp", object.getPatrolTimestamp());
				properties.put("patrolInstruction", object.getPatrolInstruction());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
}

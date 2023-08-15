package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyDouble;

/*
 * Rebuilder：申请表-项目托管终止（审批）-明细表
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_BldEscrowCompleted_DtlRebuild extends RebuilderBase<Empj_BldEscrowCompleted_Dtl>
{
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;

	@Override
	public Properties getSimpleInfo(Empj_BldEscrowCompleted_Dtl object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());				 //托管状态
		properties.put("createTimeStamp", object.getCreateTimeStamp());  //终止申请时间
		Empj_BldEscrowCompleted bldEscrowCompleted = object.getMainTable();
		if (bldEscrowCompleted != null) 
		{
			properties.put("mainTableId", object.getMainTable().getTableId());
			properties.put("eCode", object.getMainTable().geteCode());
			properties.put("eCodeFromDRAD", object.getMainTable().geteCodeFromDRAD());
		}
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
		Empj_BuildingInfo building = object.getBuilding();
		if (building != null) 
		{
			MyDouble muDouble = MyDouble.getInstance(); 
			properties.put("buildingId", building.getTableId());
			properties.put("buildingName", building.geteCode());
			properties.put("eCodeofBuilding", building.geteCode());
			properties.put("eCodeFromConstruction", building.geteCodeFromConstruction());	  //施工编号
			properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity()); //公安编号
			properties.put("upfloorNumber", building.getUpfloorNumber());					  //地上层数
			properties.put("buildingArea", muDouble.getShort(building.getBuildingArea(), 2)); //建筑面积
			properties.put("escrowArea", muDouble.getShort(building.getEscrowArea(), 2));     //托管面积
			properties.put("deliveryType", building.getDeliveryType());   					  //交付类型
			Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", building.getDeliveryType());
			if(sm_BaseParameter != null)
			{
				properties.put("deliveryTypeName", sm_BaseParameter.getTheName());
			}

			properties.put("escrowStandard", building.getEscrowStandard()); 		 		  //托管标准
			Tgpj_EscrowStandardVerMng escrowStandardVerMng = building.getEscrowStandardVerMng();
			if (escrowStandardVerMng != null)
			{
				if (S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
				{
					Double amount = escrowStandardVerMng.getAmount();
					if (amount == null)
					{
						amount = 0.0;
					}
					properties.put("newEscrowStandard", MyDouble.pointTOThousandths(amount)+"（元/m²）");
				}
				else
				{
					Double percentage = escrowStandardVerMng.getPercentage();
					if (percentage == null)
					{
						percentage = 0.0;
					}
					properties.put("newEscrowStandard", "物价备案均价*"+ MyString.getInstance().parse(percentage.intValue())+"%");
				}
			}

			Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
			if (buildingAccount != null)
			{
				properties.put("currentEscrowFund", MyDouble.pointTOThousandths(buildingAccount.getCurrentEscrowFund()));   //托管余额
				properties.put("allocableAmount", MyDouble.pointTOThousandths(buildingAccount.getAllocableAmount()));       //可划拨金额
				properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress()); //当前形象进度
				properties.put("effectiveLimitedAmount",
						MyDouble.pointTOThousandths(buildingAccount.getEffectiveLimitedAmount())); //当前受限额度/有效受限额度
				properties.put("recordAvgPriceOfBuilding",  muDouble.getShort(buildingAccount.getRecordAvgPriceOfBuilding(), 2));
			}
			Empj_BuildingExtendInfo buildingExtendInfo = building.getExtendInfo();
			if (buildingExtendInfo != null)
			{
				properties.put("escrowState", buildingExtendInfo.getEscrowState());   //托管状态
			}
		}			
		
		return properties;
	}

//	@SuppressWarnings("unchecked")
//	public Properties getSimpleInfoForApprovalProcess(Empj_BldEscrowCompleted_Dtl object)
//	{
//		if (object == null) return null;
//		Properties properties = getSimpleInfo(object);
//		//此处不需要从OSS上拿取审批流中字段，备注：审批流-变更字段.xlsx文件中托管终止无可变更字段
//		return properties;
//	}

	@Override
	public Properties getDetail(Empj_BldEscrowCompleted_Dtl object)
	{
		if(object == null) return null;
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
		properties.put("eCodeOfMainTable", object.geteCodeOfMainTable());
		properties.put("mainTable", object.getMainTable());
		properties.put("mainTableId", object.getMainTable().getTableId());
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
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_BldEscrowCompleted_DtlList != null)
		{
			for(Empj_BldEscrowCompleted_Dtl object:empj_BldEscrowCompleted_DtlList)
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
				properties.put("eCodeOfMainTable", object.geteCodeOfMainTable());
				properties.put("mainTable", object.getMainTable());
				properties.put("mainTableId", object.getMainTable().getTableId());
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
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				
				list.add(properties);
			}
		}
		return list;
	}
}

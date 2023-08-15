package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_BuildingRemainRightLogRebuild extends RebuilderBase<Tgpf_BuildingRemainRightLog>
{

	@Override
	public Properties getSimpleInfo(Tgpf_BuildingRemainRightLog object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("srcBusiType", object.getSrcBusiType());
		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("companyId", object.getProject().getDevelopCompany().getSourceId());
		properties.put("theNameOfCompany", object.getProject().getDevelopCompany().getTheName());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getProject().getTheName());
		properties.put("eCodeOfBuilding", object.getBuilding().geteCode());
		properties.put("eCodeOfBuild", object.getBuilding().geteCodeFromConstruction());
		properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
		properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
		properties.put("totalAccountAmount", object.getTotalAccountAmount());
		properties.put("billTimeStamp", object.getBillTimeStamp());
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		if(object.getBuilding() != null)
		{
			properties.put("buildingId", object.getBuilding().getTableId());
		}
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_BuildingRemainRightLog object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("company", object.getDevelopCompany());
		properties.put("companyId", object.getProject().getDevelopCompany().getSourceId());
		properties.put("theNameOfCompany", object.getProject().getDevelopCompany().getTheName());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("eCodeOfProject", object.geteCodeOfProject());
		properties.put("building", object.getBuilding());
		properties.put("buildingId", object.getBuilding().getTableId());
		if(object.getBuilding().getEscrowStandardVerMng() != null)
		{
			properties.put("tgpj_EscrowStandardVerMngId", object.getBuilding().getEscrowStandardVerMng().getTableId());//托管标准ID
			if(S_EscrowStandardType.StandardAmount.equals(object.getBuilding().getEscrowStandardVerMng().getTheType()))
			{
				properties.put("escrowStandard", object.getBuilding().getEscrowStandardVerMng().getAmount() + "元");//托管标准金额

			}
			if(S_EscrowStandardType.StandardPercentage.equals(object.getBuilding().getEscrowStandardVerMng().getTheType()))
			{

				properties.put("escrowStandard", "物价备案均价*" + object.getBuilding().getEscrowStandardVerMng().getPercentage() + "%");//托管标准比例
			}
		}
		else
		{
			properties.put("tgpj_EscrowStandardVerMngId", "");
		}

		properties.put("buildingAccount", object.getBuildingAccount());
		if (object.getBuildingAccount() != null) {
			properties.put("buildingAccountId", object.getBuildingAccount().getTableId());
		}
		properties.put("buildingExtendInfo", object.getBuildingExtendInfo());
		if (object.getBuildingExtendInfo() != null) {
			properties.put("buildingExtendInfoId", object.getBuildingExtendInfo().getTableId());
			properties.put("escrowState", object.getBuildingExtendInfo().getEscrowState());//托管状态
		}
		properties.put("currentFigureProgress", object.getCurrentFigureProgress());
		properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
		properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
		properties.put("totalAccountAmount", object.getTotalAccountAmount());
		properties.put("billTimeStamp", object.getBillTimeStamp());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_BuildingRemainRightLog> tgpf_BuildingRemainRightLogList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_BuildingRemainRightLogList != null)
		{
			for(Tgpf_BuildingRemainRightLog object:tgpf_BuildingRemainRightLogList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("userUpdate", object.getUserUpdate());
				properties.put("userUpdateId", object.getUserUpdate().getTableId());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("eCodeOfProject", object.geteCodeOfProject());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("buildingAccount", object.getBuildingAccount());
				properties.put("buildingAccountId", object.getBuildingAccount().getTableId());
				properties.put("buildingExtendInfo", object.getBuildingExtendInfo());
				properties.put("buildingExtendInfoId", object.getBuildingExtendInfo().getTableId());
				properties.put("currentFigureProgress", object.getCurrentFigureProgress());
				properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
				properties.put("nodeLimitedAmount", object.getNodeLimitedAmount());
				properties.put("totalAccountAmount", object.getTotalAccountAmount());
				properties.put("billTimeStamp", object.getBillTimeStamp());
				
				list.add(properties);
			}
		}
		return list;
	}
}

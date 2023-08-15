package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpj_BuildingAvgPriceRebuild extends RebuilderBase<Tgpj_BuildingAvgPrice>
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Override
	public Properties getSimpleInfo(Tgpj_BuildingAvgPrice object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpj_BuildingAvgPrice object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		
		Sm_User userStart = object.getUserStart();
		if(userStart!=null){
//			properties.put("userStart", userStart);
			properties.put("userStartId", userStart.getTableId());
			properties.put("userStartName", userStart.getTheName());
			Emmp_CompanyInfo company = userStart.getCompany();
			if(company!=null){
				properties.put("companyName", company.getTheName());
				properties.put("companyId", company.getTableId());
			}
		}

		if(object.getCreateTimeStamp()!=null){
			properties.put("createTimeStamp",MyDatetime.getInstance().dateToSimpleString( object.getCreateTimeStamp()));
		}
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
//		properties.put("userRecord", object.getUserRecord());
		if(object.getUserRecord()!=null){
			properties.put("userRecordId", object.getUserRecord().getTableId());
			properties.put("userRecordName", object.getUserRecord().getTheName());
		}

		if(object.getRecordTimeStamp()!=null){
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		}

		properties.put("recordAveragePrice", object.getRecordAveragePrice());
		properties.put("recordAveragePriceFromPresellSystem", object.getRecordAveragePriceFromPresellSystem());
		Empj_BuildingInfo buildingInfo = object.getBuildingInfo();
		if (buildingInfo != null)
		{
//			properties.put("buildingInfo", buildingInfo);
			properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
			properties.put("eCodeFromConstruction", object.getBuildingInfo().geteCodeFromConstruction());
			properties.put("eCodeFromPublicSecurity", object.getBuildingInfo().geteCodeFromPublicSecurity());
			properties.put("buildingEcode", object.getBuildingInfo().geteCode());
			properties.put("theName", object.getBuildingInfo().geteCodeFromConstruction());
			properties.put("position", object.getBuildingInfo().getPosition());

			Empj_ProjectInfo project = buildingInfo.getProject();
			if(project!=null){
				properties.put("projectName", project.getTheName());
				properties.put("projectId", project.getTableId());
			}
		}

		if (object.getAveragePriceRecordDate() != null)
		{
			properties.put("averagePriceRecordDateString",
					MyDatetime.getInstance().dateToSimpleString(object.getAveragePriceRecordDate()));
		}
		properties.put("approvalState", object.getApprovalState());//流程状态
		if(object.getRecordAveragePriceFromPresellSystem()!=null){
			properties.put("recordAveragePriceFromPresellSystem", object.getRecordAveragePriceFromPresellSystem());
		}
		properties.put("remark", object.getRemark());
		Boolean isNeedBackup = null;

		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpj_BuildingAvgPrice> tgpj_BuildingAvgPriceList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpj_BuildingAvgPriceList != null)
		{
			for(Tgpj_BuildingAvgPrice object:tgpj_BuildingAvgPriceList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				Sm_User userStart = object.getUserStart();
				if(userStart!=null){
//					properties.put("userStart",userStart );
					properties.put("userStartId", userStart.getTableId());
					properties.put("userStartName", userStart.getTheName());
					Emmp_CompanyInfo company = userStart.getCompany();
					if(company!=null){
						properties.put("companyName", company.getTheName());
						properties.put("companyId", company.getTableId());
					}
				}
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("recordAveragePrice", object.getRecordAveragePrice());
				Empj_BuildingInfo buildingInfo = object.getBuildingInfo();
				if(buildingInfo!=null){
					properties.put("buildingInfoId", buildingInfo.getTableId());
					properties.put("eCodeFromConstruction", object.getBuildingInfo().geteCodeFromConstruction());
					properties.put("eCodeFromPublicSecurity", object.getBuildingInfo().geteCodeFromPublicSecurity());
					properties.put("buildingEcode", object.getBuildingInfo().geteCode());
					Empj_BuildingInfo empj_buildingInfo = new Empj_BuildingInfo();
					empj_buildingInfo.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
					properties.put("building", empj_buildingInfo);

					Empj_ProjectInfo project = buildingInfo.getProject();
					if(project!=null){
						properties.put("projectName", project.getTheName());
						properties.put("projectId", project.getTableId());
					}
				}
				properties.put("averagePriceRecordDate", object.getAveragePriceRecordDate());
				properties.put("approvalState", object.getApprovalState());
				properties.put("recordAveragePriceFromPresellSystem", object.getRecordAveragePriceFromPresellSystem());
				
				list.add(properties);
			}
		}
		return list;
	}
}

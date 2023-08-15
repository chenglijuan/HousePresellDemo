package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_BuildingAccountSupervisedRebuild extends RebuilderBase<Empj_BuildingAccountSupervised>
{
	@Override
	public Properties getSimpleInfo(Empj_BuildingAccountSupervised object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Empj_BuildingAccountSupervised object)
	{
		if(object == null) return null;
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
		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToSimpleString(object.getLastUpdateTimeStamp()));
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("buildingInfo", object.getBuildingInfo());
		properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
		properties.put("bankAccountSupervised", object.getBankAccountSupervised());
		properties.put("bankAccountSupervisedId", object.getBankAccountSupervised().getTableId());
//		properties.put("beginTimeStamp", object.getBeginTimeStamp());
//		properties.put("endTimeStamp", object.getEndTimeStamp());
		properties.put("isUsing", object.getIsUsing());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_BuildingAccountSupervised> empj_BuildingAccountSupervisedList)
	{
		List<Properties> list = new ArrayList<Properties>();
		MyDatetime myDatetime = MyDatetime.getInstance();
		if(empj_BuildingAccountSupervisedList != null)
		{
			for(Empj_BuildingAccountSupervised object:empj_BuildingAccountSupervisedList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				
//				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
//				properties.put("eCode", object.geteCode());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("userUpdate", object.getUserUpdate());
//				properties.put("userUpdateId", object.getUserUpdate().getTableId());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				Empj_BuildingInfo buildingInfo = object.getBuildingInfo();
				if (buildingInfo != null)
				{
					Empj_ProjectInfo project = buildingInfo.getProject();
					if (project != null)
					{
						properties.put("projectName", project.getTheName());
						properties.put("projectId", project.getTableId());
					}
					properties.put("buildingId", buildingInfo.getTableId());
					properties.put("buildingEcode", buildingInfo.geteCode());
					properties.put("eCodeFromConstruction", buildingInfo.geteCodeFromConstruction());
					properties.put("eCodeFromPublicSecurity", buildingInfo.geteCodeFromPublicSecurity());
				}
				Tgpj_BankAccountSupervised bankAccountSupervised = object.getBankAccountSupervised();
				if (bankAccountSupervised != null)
				{
					properties.put("bankAccountSupervisedId", bankAccountSupervised.getTableId());
					properties.put("bankAccountSupervisedName", bankAccountSupervised.getTheName());
				}
				properties.put("isUsing", object.getIsUsing());
//				properties.put("beginTimeStamp", object.getBeginTimeStamp());
//				properties.put("beginTimeStampString", myDatetime.dateToSimpleString(object.getBeginTimeStamp()));
//				properties.put("endTimeStamp", object.getEndTimeStamp());
//				properties.put("endTimeStampString", myDatetime.dateToSimpleString(object.getEndTimeStamp()));

				list.add(properties);
			}
		}
		return list;
	}
}

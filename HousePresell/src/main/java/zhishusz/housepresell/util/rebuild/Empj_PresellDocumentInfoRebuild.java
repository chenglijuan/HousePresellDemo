package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：预售证信息
 * Company：ZhiShuSZ
 * */
@Service
public class Empj_PresellDocumentInfoRebuild extends RebuilderBase<Empj_PresellDocumentInfo>
{
	@Override
	public Properties getSimpleInfo(Empj_PresellDocumentInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Empj_PresellDocumentInfo object)
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
		properties.put("developCompany", object.getDevelopCompany());
		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("addressOfProject", object.getAddressOfProject());
		properties.put("buildingInfoSet", object.getBuildingInfoSet());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_PresellDocumentInfo> empj_PresellDocumentInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(empj_PresellDocumentInfoList != null)
		{
			for(Empj_PresellDocumentInfo object:empj_PresellDocumentInfoList)
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
				properties.put("addressOfProject", object.getAddressOfProject());
				properties.put("buildingInfoSet", object.getBuildingInfoSet());
				
				list.add(properties);
			}
		}
		return list;
	}
}

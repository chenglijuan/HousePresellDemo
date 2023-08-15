package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：资质认证信息
 * Company：ZhiShuSZ
 * */
@Service
public class Emmp_QualificationInfoRebuild extends RebuilderBase<Emmp_QualificationInfo>
{
	@Override
	public Properties getSimpleInfo(Emmp_QualificationInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Emmp_QualificationInfo object)
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
		properties.put("company", object.getCompany());
		properties.put("companyId", object.getCompany().getTableId());
		properties.put("theType", object.getTheType());
		properties.put("theLevel", object.getTheLevel());
		properties.put("issuanceDate", object.getIssuanceDate());
		properties.put("expiryDate", object.getExpiryDate());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Emmp_QualificationInfo> emmp_QualificationInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_QualificationInfoList != null)
		{
			for(Emmp_QualificationInfo object:emmp_QualificationInfoList)
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
				properties.put("company", object.getCompany());
				properties.put("companyId", object.getCompany().getTableId());
				properties.put("theType", object.getTheType());
				properties.put("theLevel", object.getTheLevel());
				properties.put("issuanceDate", object.getIssuanceDate());
				properties.put("expiryDate", object.getExpiryDate());
				
				list.add(properties);
			}
		}
		return list;
	}
}

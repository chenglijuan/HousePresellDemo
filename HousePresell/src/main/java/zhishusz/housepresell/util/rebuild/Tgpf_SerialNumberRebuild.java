package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_SerialNumber;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：流水号
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_SerialNumberRebuild extends RebuilderBase<Tgpf_SerialNumber>
{
	@Override
	public Properties getSimpleInfo(Tgpf_SerialNumber object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_SerialNumber object)
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
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("businessType", object.getBusinessType());
		properties.put("serialNumber", object.getSerialNumber());
		properties.put("serialDate", object.getSerialDate());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_SerialNumber> tgpf_SerialNumberList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_SerialNumberList != null)
		{
			for(Tgpf_SerialNumber object:tgpf_SerialNumberList)
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
				properties.put("businessType", object.getBusinessType());
				properties.put("serialNumber", object.getSerialNumber());
				properties.put("serialDate", object.getSerialDate());
				
				list.add(properties);
			}
		}
		return list;
	}
}

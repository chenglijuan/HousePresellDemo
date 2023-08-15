package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：附件
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_AttachmentRebuild extends RebuilderBase<Sm_Attachment>
{
	@Override
	public Properties getSimpleInfo(Sm_Attachment object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("sourceType", object.getSourceType());
		properties.put("sourceId", object.getSourceId());
		properties.put("busiType", object.getBusiType());
		properties.put("theLink", object.getTheLink());
		properties.put("fileType", object.getFileType());
		properties.put("remark", object.getRemark());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_Attachment object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("sourceType", object.getSourceType());
		properties.put("sourceId", object.getSourceId());
		properties.put("fileType", object.getFileType());
		properties.put("totalPage", object.getTotalPage());
		properties.put("theLink", object.getTheLink());
		properties.put("theSize", object.getTheSize());
		properties.put("remark", object.getRemark());
		properties.put("md5Info", object.getMd5Info());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Attachment> sm_AttachmentList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_AttachmentList != null)
		{
			for(Sm_Attachment object:sm_AttachmentList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("sourceType", object.getSourceType());
				properties.put("sourceId", object.getSourceId());
				properties.put("fileType", object.getFileType());
				properties.put("totalPage", object.getTotalPage());
				properties.put("theLink", object.getTheLink());
				properties.put("theSize", object.getTheSize());
				properties.put("remark", object.getRemark());
				properties.put("md5Info", object.getMd5Info());
				
				list.add(properties);
			}
		}
		return list;
	}
}

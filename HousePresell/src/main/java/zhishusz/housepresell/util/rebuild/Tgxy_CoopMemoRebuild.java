package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgxy_CoopMemo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：合作备忘录
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_CoopMemoRebuild extends RebuilderBase<Tgxy_CoopMemo>
{
	@Override
	public Properties getSimpleInfo(Tgxy_CoopMemo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-13 16:38:08
		 * 设置列表展示字段信息
		 */
		properties.put("tableId", object.getTableId());

		properties.put("eCode", object.geteCode());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("signDate", object.getSignDate());
		properties.put("userStart", object.getUserStart());
		properties.put("userUpdate", object.getUserUpdate());

		/*
		 * xsz by time 2018-8-15 17:55:13
		 * 添加显示字段
		 */
		// 处理时间格式yyyy-MM-dd HH:mm:ss
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));

		// 修改人信息
		// String userStartName = object.getUserStart().getTheName();
		// properties.put("userUpdate", userStartName);

		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_CoopMemo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-15 13:46:47
		 * 设置详情基本信息显示字段
		 * 
		 */

		properties.put("tableId", object.getTableId());
		
		// 获取相关创建人和修改人等关联信息
		properties.put("userStart", object.getUserStart());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());

		// 处理时间格式yyyy-MM-dd HH:mm:ss
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));

		properties.put("eCode", object.geteCode());
		properties.put("eCodeOfCooperationMemo", object.geteCodeOfCooperationMemo());
		properties.put("bankId", object.getBank().getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("signDate", object.getSignDate());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_CoopMemo> tgxy_CoopMemoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgxy_CoopMemoList != null)
		{
			for (Tgxy_CoopMemo object : tgxy_CoopMemoList)
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
				properties.put("eCodeOfCooperationMemo", object.geteCodeOfCooperationMemo());
				properties.put("bank", object.getBank());
				properties.put("bankId", object.getBank().getTableId());
				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("signDate", object.getSignDate());

				list.add(properties);
			}
		}
		return list;
	}
}

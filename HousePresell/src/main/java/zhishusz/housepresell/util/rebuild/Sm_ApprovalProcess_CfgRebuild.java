package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.Sm_BaseParameter;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_ApprovalProcess_CfgRebuild extends RebuilderBase<Sm_ApprovalProcess_Cfg>
{
	@Override
	public Properties getSimpleInfo(Sm_ApprovalProcess_Cfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("busiCode",object.getBusiCode()+"-"+object.getBusiType());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
//		properties.put("isNeedBackup", object.getIsNeedBackup());
		properties.put("remark", object.getRemark());

		return properties;
	}

	@Override
	public Properties getDetail(Sm_ApprovalProcess_Cfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//详情页面
		properties.put("tableId", object.getTableId());
		properties.put("busiCode",object.getBusiCode());
		properties.put("codeType",object.getBusiCode()+"-"+object.getBusiType());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		properties.put("isNeedBackup", object.getIsNeedBackup());
		properties.put("remark", object.getRemark());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_ApprovalProcess_Cfg> sm_ApprovalProcess_CfgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_CfgList != null)
		{
			for(Sm_ApprovalProcess_Cfg object:sm_ApprovalProcess_CfgList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("theName", object.getTheName());
				properties.put("eCode", object.geteCode());
				properties.put("isNeedBackup", object.getIsNeedBackup());
				properties.put("nodeList", object.getNodeList());
				
				list.add(properties);
			}
		}
		return list;
	}
}

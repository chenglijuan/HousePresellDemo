package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_ApprovalProcess_ConditionRebuild extends RebuilderBase<Sm_ApprovalProcess_Condition>
{
	@Override
	public Properties getSimpleInfo(Sm_ApprovalProcess_Condition object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theContent", object.getTheContent());
		properties.put("nextStep",object.getNextStep());

		return properties;
	}

	@Override
	public Properties getDetail(Sm_ApprovalProcess_Condition object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_ApprovalProcess_Condition> sm_approvalProcess_conditionList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_approvalProcess_conditionList != null)
		{
			for(Sm_ApprovalProcess_Condition object:sm_approvalProcess_conditionList)
			{
			}
		}
		return list;
	}
}

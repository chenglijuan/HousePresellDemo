package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：审批流-节点
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_ApprovalProcess_NodeRebuild extends RebuilderBase<Sm_ApprovalProcess_Node>
{
	@Override
	public Properties getSimpleInfo(Sm_ApprovalProcess_Node object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		return properties;
	}

	@Override
	public Properties getDetail(Sm_ApprovalProcess_Node object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		properties.put("eCode", object.geteCode());
		properties.put("approvalModel", object.getApprovalModel());
		properties.put("rejectModel", object.getRejectModel());
		properties.put("finishPercentage",object.getFinishPercentage());
		properties.put("passPercentage",object.getPassPercentage());

		if(object.getRole() != null)
		{
			properties.put("roleId",object.getRole().getTableId());
			properties.put("roleName", object.getRole().getTheName());
		}

		if(object.getSm_messageTemplate_cfgList() != null)
		{
			properties.put("messageTemplate_cfgList",object.getSm_messageTemplate_cfgList());
		}
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_ApprovalProcess_Node> sm_ApprovalProcess_NodeList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_NodeList != null)
		{
			int orderNumber = 1;
			for(Sm_ApprovalProcess_Node object:sm_ApprovalProcess_NodeList)
			{
				if(object.getApprovalProcess_conditionList()!=null && object.getApprovalProcess_conditionList().size() > 0)
				{
					for(Sm_ApprovalProcess_Condition sm_approvalProcess_condition : object.getApprovalProcess_conditionList())
					{
						Properties properties = new MyProperties();

						properties.put("tableId", object.getTableId());
						properties.put("orderNumber",orderNumber);
						properties.put("theName", object.getTheName());
						properties.put("approvalModel", object.getApprovalModel());
						properties.put("finishPercentage",object.getFinishPercentage());
						properties.put("passPercentage",object.getPassPercentage());
						properties.put("theContent",sm_approvalProcess_condition.getTheContent());
						properties.put("nextStep",sm_approvalProcess_condition.getNextStep());
						properties.put("nextStepName",sm_approvalProcess_condition.getNextStepName());
						properties.put("rejectModel", object.getRejectModel());
						if(object.getRole() != null)
						{
							properties.put("roleId",object.getRole().getTableId());
							properties.put("roleName", object.getRole().getTheName());
						}
						if(object.getSm_messageTemplate_cfgList()!=null && object.getSm_messageTemplate_cfgList().size() > 0)
						{
							String tempalteName = "";
							for(Sm_MessageTemplate_Cfg sm_messageTemplate_cfg : object.getSm_messageTemplate_cfgList())
							{
								tempalteName += sm_messageTemplate_cfg.getTheName()+",";
							}
							properties.put("thetemplate", tempalteName.substring(0,tempalteName.length()-1));
						}
						list.add(properties);
					}
				}
				else
				{
					Properties properties = new MyProperties();

					properties.put("tableId", object.getTableId());
					properties.put("orderNumber",orderNumber);
					properties.put("theName", object.getTheName());
					properties.put("approvalModel", object.getApprovalModel());
					properties.put("finishPercentage",object.getFinishPercentage());
					properties.put("passPercentage",object.getPassPercentage());
					properties.put("rejectModel", object.getRejectModel());
					if(object.getRole() != null)
					{
						properties.put("roleId",object.getRole().getTableId());
						properties.put("roleName", object.getRole().getTheName());
					}
					if(object.getSm_messageTemplate_cfgList()!=null && object.getSm_messageTemplate_cfgList().size() > 0)
					{
						String tempalteName = "";
						for(Sm_MessageTemplate_Cfg sm_messageTemplate_cfg : object.getSm_messageTemplate_cfgList())
						{
							tempalteName += sm_messageTemplate_cfg.getTheName()+",";
						}
						properties.put("thetemplate", tempalteName.substring(0,tempalteName.length()-1));
					}
					list.add(properties);
				}
				orderNumber ++;
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getModalNodeList(List<Sm_ApprovalProcess_Node> sm_ApprovalProcess_NodeList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_NodeList != null)
		{
			for(Sm_ApprovalProcess_Node object:sm_ApprovalProcess_NodeList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				list.add(properties);
			}
		}
		return list;
	}
}

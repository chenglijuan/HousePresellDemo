package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_ApprovalProcess_AFRebuild extends RebuilderBase<Sm_ApprovalProcess_AF>
{
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();
	@Override
	public Properties getSimpleInfo(Sm_ApprovalProcess_AF object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Sm_ApprovalProcess_AF object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("configuration", object.getConfiguration());
		properties.put("configurationId", object.getConfiguration().getTableId());
		properties.put("companyInfo", object.getCompanyInfo());
		properties.put("companyInfoId", object.getCompanyInfo().getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("startTimeStamp", object.getStartTimeStamp());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("sourceId", object.getSourceId());
		properties.put("sourceType", object.getSourceType());
		properties.put("orgObjJsonFilePath", object.getOrgObjJsonFilePath());
		properties.put("expectObjJsonFilePath", object.getExpectObjJsonFilePath());
		properties.put("attachmentList", object.getAttachmentList());
		properties.put("workFlowList", object.getWorkFlowList());
		properties.put("currentIndex", object.getCurrentIndex());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_ApprovalProcess_AF> sm_ApprovalProcess_AFList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_AFList != null)
		{
			for(Sm_ApprovalProcess_AF object:sm_ApprovalProcess_AFList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId()); // 申请单id
				properties.put("eCode", object.geteCode());//业务单号
				properties.put("sourceId",object.getSourceId()); //业务id
				properties.put("busiState", object.getBusiState());//业务状态
				properties.put("currentWorkflowId",object.getCurrentIndex());
				properties.put("busiType",object.getBusiType());  //业务类型
				properties.put("busiCode",object.getBusiCode()); //业务编码
				properties.put("theme", object.getTheme());//主题

				if(object.getCompanyInfo() !=null)
				{
					properties.put("theNameOfCompanyInfo", object.getCompanyInfo().getTheName());
				}
				if(object.getUserStart() != null)
				{
					properties.put("applicant",object.getUserStart().getTheName()); // 申请人
				}
				properties.put("createTimeStamp",myDatetime.dateToString(object.getCreateTimeStamp()));   //申请日期

				if(object.getBusiState()!=null && (S_ApprovalState.Completed.equals(object.getBusiState()) || S_ApprovalState.NoPass.equals(object.getBusiState())))
				{
					properties.put("endDate",myDatetime.dateToString(object.getLastUpdateTimeStamp())); //完成日期
				}

//				Sm_ApprovalProcess_WorkflowForm workflowForm = new Sm_ApprovalProcess_WorkflowForm();
//				workflowForm.setTheState(S_TheState.Normal);
//				workflowForm.setBusiState("审核中");
//				workflowForm.setApprovalProcess_AFId(object.getTableId());
//				Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_ApprovalProcess_WorkflowDao.findOneByQuery_T(sm_ApprovalProcess_WorkflowDao.getQuery(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), workflowForm));
//				if(sm_approvalProcess_workflow !=null)
//				{
//					if(sm_approvalProcess_workflow.getApprovalModel().equals(1))
//					{
//						List<Properties> approvalProcess_recordList_Pro = new ArrayList<Properties>();
//						List<Sm_ApprovalProcess_Record> approvalProcess_recordList = sm_approvalProcess_workflow.getApprovalProcess_recordList();
//						if(approvalProcess_recordList == null)
//						{
//							approvalProcess_recordList = new ArrayList<Sm_ApprovalProcess_Record>();
//						}
//						//审批记录
//						for(Sm_ApprovalProcess_Record sm_ApprovalProcess_Record : approvalProcess_recordList)
//						{
//							Properties approvalProcess_record_Pro = new MyProperties();
//							if(sm_ApprovalProcess_Record.getUserOperate() != null)
//							{
//								approvalProcess_record_Pro.put("userOperate", sm_ApprovalProcess_Record.getUserOperate().getTheName());
//							}
//							approvalProcess_record_Pro.put("operateTimeStamp", myDatetime.dateToString(sm_ApprovalProcess_Record.getOperateTimeStamp()));
//							approvalProcess_recordList_Pro.add(approvalProcess_record_Pro);
//						}
//						properties.put("approvalModel",sm_approvalProcess_workflow.getApprovalModel()); //审批模式
//						properties.put("finishPercentage",sm_approvalProcess_workflow.getFinishPercentage());//完成阀值
//						properties.put("passPercentage",sm_approvalProcess_workflow.getPassPercentage());//通过阀值
//						//审批记录
//						properties.put("approvalProcess_recordList", approvalProcess_recordList_Pro);
//					}
//				}

				properties.put("busiState",object.getBusiState());
				
				list.add(properties);
			}
		}
		return list;
	}
}

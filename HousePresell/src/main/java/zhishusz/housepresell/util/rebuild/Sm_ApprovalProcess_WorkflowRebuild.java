package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalModel;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_ApprovalProcess_WorkflowRebuild extends RebuilderBase<Sm_ApprovalProcess_Workflow>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

	@Override
	public Properties getSimpleInfo(Sm_ApprovalProcess_Workflow object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		return  properties;
	}

	@Override
	public Properties getDetail(Sm_ApprovalProcess_Workflow object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("lastAction", object.getLastAction());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_ApprovalProcess_Workflow> sm_ApprovalProcess_WorkflowList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_WorkflowList != null)
		{
			for(Sm_ApprovalProcess_Workflow object:sm_ApprovalProcess_WorkflowList)
			{
				Properties properties = new MyProperties();

				//列表页面
				properties.put("tableId", object.getTableId());
				properties.put("theName",object.getTheName());//步骤名称

				if(object.getApprovalProcess_AF() != null || (S_TheState.Normal == object.getApprovalProcess_AF().getTheState()))
				{
					Sm_ApprovalProcess_AF sm_approvalProcess_af = object.getApprovalProcess_AF();
					properties.put("afId",sm_approvalProcess_af.getTableId()); // 申请单id
					properties.put("sourceId",sm_approvalProcess_af.getSourceId()); //业务id
					properties.put("eCode",sm_approvalProcess_af.geteCode());//业务单号
					properties.put("theme",sm_approvalProcess_af.getTheme());//主题
					properties.put("busiType",sm_approvalProcess_af.getBusiType());  //业务类型
					properties.put("busiCode",sm_approvalProcess_af.getBusiCode()); //业务编码

					if(sm_approvalProcess_af.getCompanyInfo() != null)
					{
						Emmp_CompanyInfo emmp_companyInfo = sm_approvalProcess_af.getCompanyInfo();
						properties.put("theNameOfCompanyInfo",emmp_companyInfo.getTheName()); // 申请机构
					}

					if(sm_approvalProcess_af.getUserStart() != null)
					{
						properties.put("applicant",sm_approvalProcess_af.getUserStart().getTheName()); // 申请人
					}

//					properties.put("createTimeStamp",myDatetime.dateToString(sm_approvalProcess_af.getCreateTimeStamp()));   //申请日期
					properties.put("createTimeStamp",myDatetime.dateToString(sm_approvalProcess_af.getStartTimeStamp()));   //申请日期

					//当前处理人
					Long currentNodeId = sm_approvalProcess_af.getCurrentIndex(); // 当前结点Id(审核中)
					Sm_ApprovalProcess_Workflow currentWorkflow = sm_approvalProcess_workflowDao.findById(currentNodeId);
					properties.put("approvalModel",currentWorkflow.getApprovalModel()); //当前结点审批模式

					if(currentWorkflow.getApprovalModel().equals(S_ApprovalModel.PreemptionModel))
					{
						//抢占模式
						//上传附件，更新当前处理人信息为 上传该附件的用户
						if(currentWorkflow.getUserOperate() != null)
						{
							properties.put("nowPerson" ,currentWorkflow.getUserOperate().getTheName()); //当前处理人
						}
						properties.put("operateTimeStamp",myDatetime.dateToString2(currentWorkflow.getOperateTimeStamp()));  //处理时间
					}
					else if(currentWorkflow.getApprovalModel().equals(S_ApprovalModel.SignModel))
					{
						List<Properties> approvalProcess_recordList_Pro = new ArrayList<Properties>();
						List<Sm_ApprovalProcess_Record> approvalProcess_recordList = currentWorkflow.getApprovalProcess_recordList();
						if(approvalProcess_recordList == null)
						{
							approvalProcess_recordList = new ArrayList<Sm_ApprovalProcess_Record>();
						}
						//当前环节审批记录
						for(Sm_ApprovalProcess_Record sm_ApprovalProcess_Record : approvalProcess_recordList)
						{
							Properties approvalProcess_record_Pro = new MyProperties();
							if(sm_ApprovalProcess_Record.getUserOperate() != null)
							{
								approvalProcess_record_Pro.put("userOperate", sm_ApprovalProcess_Record.getUserOperate().getTheName());
							}
							approvalProcess_record_Pro.put("operateTimeStamp", myDatetime.dateToString2(sm_ApprovalProcess_Record.getOperateTimeStamp()));//操作时间
							approvalProcess_record_Pro.put("theAction",sm_ApprovalProcess_Record.getTheAction());
							approvalProcess_recordList_Pro.add(approvalProcess_record_Pro);
						}
						//当前环节审批记录
						properties.put("approvalProcess_recordList", approvalProcess_recordList_Pro);
						properties.put("finishPercentage",object.getFinishPercentage());//当前结点完成阀值
						properties.put("passPercentage",object.getPassPercentage());//当前结点通过阀值
					}

					/**
					 * 上步处理人
					 * 1.sourceId(上一环节)
					 * 2.rejectNodeId(驳回)
					 */
					if(object.getBusiState().equals(S_WorkflowBusiState.Examining))
					{
						Long sourceId;
						Sm_ApprovalProcess_Workflow sourceWorkflow = null;
						//驳回结点不为空，则当前结点由上一节点驳回过来的
						if(object.getRejectNodeId() != null && object.getRejectNodeId() > 0)
						{
							sourceId = object.getRejectNodeId();
							sourceWorkflow = sm_approvalProcess_workflowDao.findById(sourceId); //驳回结点
						}
						//驳回结点为空，则当前结点由上一环节通过过来的
						else
						{
							if(object.getSourceId()!=null && object.getSourceId() > 0)
							{
								sourceId = object.getSourceId();
								sourceWorkflow = sm_approvalProcess_workflowDao.findById(sourceId); //来源结点
							}
						}
						if(sourceWorkflow !=null)
						{
							properties.put("lastApprovalModel",sourceWorkflow.getApprovalModel());//上步审批模式
							if(sourceWorkflow.getApprovalModel().equals(S_ApprovalModel.PreemptionModel))
							{
								if(sourceWorkflow.getUserUpdate()!=null)
								{
									properties.put("beforePerson",sourceWorkflow.getUserUpdate().getTheName());//上步处理人
								}
								properties.put("beforeDate",myDatetime.dateToString2(sourceWorkflow.getLastUpdateTimeStamp()));//上步处理时间
							}
							else if(sourceWorkflow.getApprovalModel().equals(S_ApprovalModel.SignModel))
							{
								List<Properties> approvalProcess_recordList_Pro = new ArrayList<Properties>();
								List<Sm_ApprovalProcess_Record> approvalProcess_recordList = sourceWorkflow.getApprovalProcess_recordList();
								if(approvalProcess_recordList == null)
								{
									approvalProcess_recordList = new ArrayList<Sm_ApprovalProcess_Record>();
								}
								//审批记录
								for(Sm_ApprovalProcess_Record sm_ApprovalProcess_Record : approvalProcess_recordList)
								{
									Properties approvalProcess_record_Pro = new MyProperties();
									if(sm_ApprovalProcess_Record.getUserOperate() != null)
									{
										approvalProcess_record_Pro.put("userOperate", sm_ApprovalProcess_Record.getUserOperate().getTheName());
									}
									approvalProcess_record_Pro.put("operateTimeStamp", myDatetime.dateToString2(sm_ApprovalProcess_Record.getOperateTimeStamp()));
									approvalProcess_record_Pro.put("lastAction",sm_ApprovalProcess_Record.getTheAction());
									approvalProcess_recordList_Pro.add(approvalProcess_record_Pro);
								}
								//审批记录
								properties.put("lastRecords", approvalProcess_recordList_Pro);
								properties.put("lastFinishPercentage",sourceWorkflow.getFinishPercentage());//上步完成阀值
								properties.put("lastPassPercentage",sourceWorkflow.getPassPercentage());//上步通过阀值
							}
						}
					}
					properties.put("nowState",object.getBusiState()); //当前状态
				}else{
				    continue;
				}
				list.add(properties);
			}
		}
		return list;
	}

	//审批弹窗 -- 审批流程
	@SuppressWarnings("rawtypes")
	public List getWorkflowModalList(List<Sm_ApprovalProcess_Workflow> sm_ApprovalProcess_WorkflowList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_ApprovalProcess_WorkflowList != null)
		{
			for(Sm_ApprovalProcess_Workflow object:sm_ApprovalProcess_WorkflowList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());

				properties.put("theName",object.getTheName());//步骤名称
				properties.put("approvalModel",object.getApprovalModel()); // 审批模式
				if(object.getRole() != null)
				{
					properties.put("roleName",object.getRole().getTheName()); // 处理角色
				}

				properties.put("operateTimeStamp",myDatetime.dateToString2(object.getLastUpdateTimeStamp()));  // 处理时间
				properties.put("busiState",object.getBusiState()) ;// 审批进度

				list.add(properties);
			}
		}
		return list;
	}
}

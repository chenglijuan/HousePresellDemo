package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="审批流-审批流程")
public class Sm_ApprovalProcess_Workflow implements Serializable    //,Comparable<Sm_ApprovalProcess_Workflow>
{
	private static final long serialVersionUID = 5329805893318310864L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;

	@Getter @Setter @IFieldAnnotation(remark="乐观锁")
	private Long version;

	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;

	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;

	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@IFieldAnnotation(remark="结点编号")
	private String eCode;

	@Getter @Setter @IFieldAnnotation(remark="操作人")
	private Sm_User userOperate;

	@Getter @Setter @IFieldAnnotation(remark="操作时间点")
	private Long operateTimeStamp;

	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="角色")
	private Sm_Permission_Role role;

	@Getter @Setter @IFieldAnnotation(remark="节点名称")
	private String theName;

	@Getter @Setter @IFieldAnnotation(remark="最后审批动作")
	private Integer lastAction;//0: 通过、1:驳回  2: 不同意
	
	@Getter @Setter @IFieldAnnotation(remark="审批流-申请单")
	private Sm_ApprovalProcess_AF  approvalProcess_AF;
	
	@Getter @Setter @IFieldAnnotation(remark="上一个流程")
	private Sm_ApprovalProcess_Workflow lastWorkFlow;
	
	@Getter @Setter @IFieldAnnotation(remark="下一个流程")
	private Sm_ApprovalProcess_Workflow nextWorkFlow;

	@Getter @Setter @IFieldAnnotation(remark="来源结点")
	private Long sourceId;

	@Getter @Setter @IFieldAnnotation(remark="发送结点")
	private Long sendId;

	@Getter @Setter @IFieldAnnotation(remark="驳回结点")
	private Long rejectNodeId;

	@Getter @Setter @IFieldAnnotation(remark="结点类型")
	private Integer nodeType; // 0：第一个结点 1：其他结点

	@Getter @Setter @IFieldAnnotation(remark="审批模式")
	private Integer approvalModel; // 0：抢占 1：会签

	@Getter @Setter @IFieldAnnotation(remark="会签完成阀值")
	private Integer finishPercentage;

	@Getter @Setter @IFieldAnnotation(remark="会签通过阀值")
	private Integer passPercentage;

	@Getter @Setter @IFieldAnnotation(remark="驳回模式 S_RejectModel")
	private Integer rejectModel; // 0: 驳回到起草人 1: 驳回到上一级

	@Getter @Setter @IFieldAnnotation(remark="审批条件")
	private List<Sm_ApprovalProcess_WorkflowCondition> workflowConditionList;

	@Getter @Setter @IFieldAnnotation(remark="消息模板配置列表")
	public List<Sm_MessageTemplate_Cfg> sm_messageTemplate_cfgList;

	@Getter @Setter @IFieldAnnotation(remark="审批记录")
	private List<Sm_ApprovalProcess_Record> approvalProcess_recordList;

	public Sm_ApprovalProcess_Workflow(Sm_ApprovalProcess_Node node)
	{
		this.theState = node.getTheState();
		this.eCode = node.geteCode();
		this.theName = node.getTheName();
		this.nodeType = node.getNodeType();
		this.busiState = S_WorkflowBusiState.WaitSubmit;
		this.role  = node.getRole();
		this.approvalModel = node.getApprovalModel();
		this.finishPercentage = node.getFinishPercentage();
		this.passPercentage = node.getPassPercentage();
		this.rejectModel = node.getRejectModel();
		this.createTimeStamp = System.currentTimeMillis();
	}

	public Sm_ApprovalProcess_Workflow()
	{
		super();
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

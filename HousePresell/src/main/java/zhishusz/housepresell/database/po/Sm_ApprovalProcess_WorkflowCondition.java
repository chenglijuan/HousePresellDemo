package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ITypeAnnotation(remark="审批流-审批条件")
public class Sm_ApprovalProcess_WorkflowCondition implements Serializable
{
	private static final long serialVersionUID = 8025556509793603911L;
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;

	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="结点")
	private Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow;

	@Getter @Setter @IFieldAnnotation(remark="条件内容")
	private String theContent;

	@Getter @Setter @IFieldAnnotation(remark="下一步骤")
	private Long nextStep; //节点WorkflowId
}

package zhishusz.housepresell.controller.form;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_User;

/*
 * Form表单：审批流-审批流程
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_WorkflowForm extends NormalActionForm
{
	private static final long serialVersionUID = 5639058171284760788L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private Long roleId;//角色Id
	@Getter @Setter
	private Long[] roleListId;//角色Id
	@Getter @Setter
	private Integer orderNumber;//排序
	@Getter @Setter
	private Integer lastAction;//最后审批动作
	@Getter @Setter
	private Sm_ApprovalProcess_AF  approvalProcess_AF; //审批流-申请单
	@Getter @Setter
	private Long  approvalProcess_AFId; //审批流-申请单
	@Getter @Setter
	private String  approvalApplyDate; //申请日期
	@Getter @Setter
	private Long startTimeStamp;//申请开始日期
	@Getter @Setter
	private Long endTimeStamp;//申请结束日期
	@Getter @Setter
    private Sm_User userUpdate;
	

}

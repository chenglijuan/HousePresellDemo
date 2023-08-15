package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：审批流程：审核通过表单
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_PassForm extends NormalActionForm
{
	private static final long serialVersionUID = 1486433257443076578L;
	@Getter @Setter
	private Long nodeId;      //节点ID
	
	@Getter @Setter
	private Long workflowId;  //流程ID
    
}

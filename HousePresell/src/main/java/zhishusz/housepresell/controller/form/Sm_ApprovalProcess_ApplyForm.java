package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：审批流程：审核通过表单
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_ApplyForm extends NormalActionForm
{
	private static final long serialVersionUID = -3915790537551952553L;

	@Getter @Setter
	private Long cfgId;      //配置信息的tableId （来源于 Sm_ApprovalProcess_Cfg 类中的tableId）
    
}

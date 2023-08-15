package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：审批流-申请单
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_AFDeleteForm extends NormalActionForm
{
	private static final long serialVersionUID = 4107759648157713324L;
	@Getter @Setter
	private Long sourceId;//业务Id
	@Getter @Setter
	private String busiCode;//业务编码
	@Getter @Setter
	private Sm_ApprovalProcess_AFDeleteForm[] selectRow;
}

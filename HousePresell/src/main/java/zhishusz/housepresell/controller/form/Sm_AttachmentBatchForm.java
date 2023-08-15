package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：附件
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_AttachmentBatchForm extends NormalActionForm
{
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String sourceType;//资源类型
	@Getter @Setter
	private String sourceId;//资源Id 
}

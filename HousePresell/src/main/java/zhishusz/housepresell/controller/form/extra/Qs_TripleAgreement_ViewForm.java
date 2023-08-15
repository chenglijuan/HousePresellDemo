package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：三方协议视图
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Qs_TripleAgreement_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -2144959986945805337L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户ID")
	private Long bankAccountEscrowedId;
	
	@Getter @Setter @IFieldAnnotation(remark="项目ID")
	private Long projectId;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议ID")
	private Long tripleAgreementId;
	
	@Getter @Setter @IFieldAnnotation(remark="资金归集ID")
	private Long depositDetailId;
}

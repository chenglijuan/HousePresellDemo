package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：大额到期对比表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Qs_BigAmountCompare_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = -6733725668100461708L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="存款银行")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称（开户行）")
	private String theNameOfDepositBank;
	
	@Getter @Setter @IFieldAnnotation(remark="存款性质")
	private String depositNature;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private String theAccountOfEscrow;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户名称")
	private String theNameOfEscrow;
	
	@Getter @Setter @IFieldAnnotation(remark="存入时间")
	private String depositDate;
	
	@Getter @Setter @IFieldAnnotation(remark="到期时间")
	private String dueDate;
	
	@Getter @Setter @IFieldAnnotation(remark="提取时间")
	private String drawDate;
	
	@Getter @Setter @IFieldAnnotation(remark="存款金额")
	private Double depositAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="存款期限")
	private String depositTimeLimit;
	
	@Getter @Setter @IFieldAnnotation(remark="利率")
	private Double interestRate;
	
	@Getter @Setter @IFieldAnnotation(remark="预计利息")
	private Double expectInterest;
	
	@Getter @Setter @IFieldAnnotation(remark="实际到期利息")
	private Double realInterest;
	
	@Getter @Setter @IFieldAnnotation(remark="差异")
	private Double compareDifference;
	
	@Getter @Setter @IFieldAnnotation(remark="存入日期起始时间")
	private String depositDateStart;
	
	@Getter @Setter @IFieldAnnotation(remark="存入日期结束时间")
	private String depositDateEnd;
	
	@Getter @Setter @IFieldAnnotation(remark="到期日期起始时间")
	private String dueDateStart;
	
	@Getter @Setter @IFieldAnnotation(remark="到期日期结束时间")
	private String dueDateEnd;
	
}

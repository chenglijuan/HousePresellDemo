package zhishusz.housepresell.exportexcelvo;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管银行资金明细表-导出VO
 * @ClassName:  Qs_EscrowBankFunds_ViewExportExcleVO   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月18日 下午5:05:06   
 * @version V1.0 
 *
 */
public class Qs_EscrowBankFunds_ViewExportExcleVO
{
	
	@Getter @Setter
	private Integer ordinal;//序号 
	
	@Getter @Setter @IFieldAnnotation(remark="开户行简称")
	private String theNameOfDepositBank;
	
	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	
	@Getter @Setter @IFieldAnnotation(remark="大额存单")
	private Double certOfDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="结构性存款")
	private Double structuredDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="保本理财")
	private Double breakEvenFinancial;
	
	@Getter @Setter @IFieldAnnotation(remark="活期")
	private Double currentBalance;
	
	@Getter @Setter @IFieldAnnotation(remark="大额占比")
	private Double largeRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="大额+活期占比")
	private Double largeAndCurrentRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="理财占比")
	private Double financialRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="总资金沉淀占比")
	private Double totalFundsRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="正在办理中(资金性质)")
	private String inProgress;
	
	@Getter @Setter @IFieldAnnotation(remark="正在办理中")
	private Double inProgressAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="转出金额")
	private Double transferOutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="转入金额")
	private Double transferInAmount;
}

package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-托管银行资金情况表
 * @ClassName:  Tjbb_EscrowBankFunds_View   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月4日 下午8:22:29   
 * @version V1.0 
 *
 */
@ITypeAnnotation(remark="托管银行资金情况表")
public class Qs_EscrowBankFunds_View implements Serializable
{
	
	private static final long serialVersionUID = -4308150791897260581L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="存款银行")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private String theNameOfDepositBank;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账号")
	private String theAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="托管收入")
	private Double income;

	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	
	@Getter @Setter @IFieldAnnotation(remark="大额存单")
	private Double certOfDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="结构性存款")
	private Double structuredDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="保本理财")
	private Double breakEvenFinancial;
	
	@Getter @Setter @IFieldAnnotation(remark="活期余额")
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
	
	@Getter @Setter @IFieldAnnotation(remark="日期")
	private String timeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="总资金沉淀占比")
	private Double inProgressAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="转出金额")
	private Double transferOutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="转入金额")
	private Double transferInAmount;
}

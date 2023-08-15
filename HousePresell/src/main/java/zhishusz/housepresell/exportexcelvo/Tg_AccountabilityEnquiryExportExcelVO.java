package zhishusz.housepresell.exportexcelvo;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * excel导出:按权责发生制查询利息情况统计表
 * */
public class Tg_AccountabilityEnquiryExportExcelVO
{
	@Getter @Setter
	private Integer ordinal;//序号 
	
	
	
	/*@Getter @Setter @IFieldAnnotation(remark="导出余列1")
	private String theOne;
	
	@Getter @Setter @IFieldAnnotation(remark="导出余列2")
	private String theTwo;*/
	
	@Getter @Setter @IFieldAnnotation(remark="测算开始日期")
	private String calculateStart;
	
	@Getter @Setter @IFieldAnnotation(remark="测算结束日期")
	private String calculateEnd;
	
	@Getter @Setter @IFieldAnnotation(remark="存款银行")
	private String bank;
//	
//	@Getter @Setter @IFieldAnnotation(remark="开户行")
//	private String bankOfDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="存款性质")
	private String depositProperty;
	
//	@Getter @Setter @IFieldAnnotation(remark="托管账号")
//	private String escrowAccount;
//	
//	@Getter @Setter @IFieldAnnotation(remark="托管账号名称")
//	private String escrowAcountName;
	
	@Getter @Setter @IFieldAnnotation(remark="登记日期")
	private String recordDate;
	
	@Getter @Setter @IFieldAnnotation(remark="存入时间")
	private String loadTime;
	
	@Getter @Setter @IFieldAnnotation(remark="到期时间")
	private String expirationTime;
	
	@Getter @Setter @IFieldAnnotation(remark="存款金额")
	private Double amountDeposited ;
	
	@Getter @Setter @IFieldAnnotation(remark="存款期限")
	private String depositCeilings;
	
	@Getter @Setter @IFieldAnnotation(remark="利率")
	private String interestRate;
	
	@Getter @Setter @IFieldAnnotation(remark="天数")
	private Integer fate;
	
	@Getter @Setter @IFieldAnnotation(remark="利息")
	private Double interest;
}

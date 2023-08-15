package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;

/**
 * 大额到期对比  接受Bean
 */
public class Tg_BigAmountCompare_ViewExportExcelVo {
	
	@Getter @Setter
	private Integer ordinal;//序号

	@Getter @Setter @IFieldAnnotation(remark="存款银行")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称（开户行）")
	private String theNameOfDepositBank;
	
	@Getter @Setter @IFieldAnnotation(remark="存款性质")
	private String depositNature;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private String theAccountOfEscrow;	
	
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
}

package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-大额到期对比表
 * @ClassName:  Qs_BigAmountCompare_View   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月6日 上午11:50:30
 * @version V1.0 
 *
 */
@ITypeAnnotation(remark="大额到期对比表")
public class Qs_BigAmountCompare_View implements Serializable
{
	
	private static final long serialVersionUID = 5868003241526928073L;

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
	
}

package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行放款项目出账情况表  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="银行放款项目出账情况表")
public class Tg_BankLoanOutSituation_View  implements Serializable
{
	private static final long serialVersionUID = 8923116091452735841L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称  
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private String escrowAcount;//托管账户 
	@Getter @Setter @IFieldAnnotation(remark="托管账户简称")
	private String escrowAcountShortName;//托管账户简称
	@Getter @Setter @IFieldAnnotation(remark="出账日期")
	private String loanOutDate;//出账日期
	@Getter @Setter @IFieldAnnotation(remark="出账金额")
	private Double loanAmountOut;//出账金额
	@Getter @Setter @IFieldAnnotation(remark="企业名称")
	private String companyName;//企业名称
}

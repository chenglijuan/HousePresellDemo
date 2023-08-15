package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行放款项目入账情况表  接受Bean
 * @author ZS004
 */
public class Tg_BankLoanInSituation_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号
	@Getter @Setter
	private String projectName;//项目名称  
	@Getter @Setter
	private String escrowAcount;//托管账户 
	@Getter @Setter
	private String escrowAcountShortName;//托管账户简称
	@Getter @Setter
	private String billTimeStamp;//入账日期
	@Getter @Setter
	private Double loanAmountIn;//入账金额
	@Getter @Setter
	private String companyName;//企业名称
}

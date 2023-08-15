package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行放款项目出账情况表  接受Bean
 * @author ZS004
 */
public class Tg_BankLoanOutSituation_ViewExportExcelVO
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
	private String loanOutDate;//出账日期
	@Getter @Setter
	private Double loanAmountOut;//出账金额
	@Getter @Setter
	private String companyName;//企业名称
}

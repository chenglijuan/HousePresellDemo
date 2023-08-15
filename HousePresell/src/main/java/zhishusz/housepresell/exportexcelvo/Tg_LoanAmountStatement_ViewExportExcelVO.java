package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管现金流量表  接受Bean
 * @author ZS004
 */
public class Tg_LoanAmountStatement_ViewExportExcelVO
{
	
	@Getter @Setter
	private Integer ordinal;//序号 
	@Getter @Setter
	private String billTimeStamp;//日期 
	@Getter @Setter
	private Double lastAmount;//上期结余
	@Getter @Setter
	private Double loanAmountIn;//托管资金入账金额
	@Getter @Setter
	private Double depositReceipt;//存单存入
	@Getter @Setter
	private Double payoutAmount;//资金拨付金额
	@Getter @Setter
	private Double depositExpire;//存单到期
	@Getter @Setter
	private Double currentBalance;//活期余额
	
}

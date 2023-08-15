package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 日记账统计表  接受Bean
 * @author ZS004
 */
public class Tg_JournalCount_ViewExportExcelVO 
{

	@Getter @Setter
	private Integer ordinal;//序号 
	@Getter @Setter
	private String loanInDate;//入账日期 
	@Getter @Setter 
	private String escrowAcountName;//托管账户名称
	@Getter @Setter
	private Integer tradeCount;//确认总笔数
	@Getter @Setter
	private Double totalTradeAmount;//确认总金额
	@Getter @Setter
	private Integer aToatlLoanAmoutCount;//公积金贷款总笔数
	@Getter @Setter
	private Double aToatlLoanAmout;//公积金贷款总金额
	@Getter @Setter
	private Integer bToatlLoanAmoutCount;//商业贷款总笔数
	@Getter @Setter
	private Double bToatlLoanAmout;//商业贷款总金额
	@Getter @Setter
	private Integer oToatlLoanAmoutCount;//自有资金总笔数
	@Getter @Setter
	private Double oToatlLoanAmout;//自有资金总金额
	@Getter @Setter
	private Integer aFristToatlLoanAmoutCount;//公积金首付款总笔数
	@Getter @Setter
	private Double aFristToatlLoanAmout;//公积金首付款总金额
	@Getter @Setter
	private Integer aToBusinessToatlLoanAmoutCount;//公转商贷款总笔数
	@Getter @Setter
	private Double aToBusinessToatlLoanAmout;//公转商贷款总金额
	
	
	
}

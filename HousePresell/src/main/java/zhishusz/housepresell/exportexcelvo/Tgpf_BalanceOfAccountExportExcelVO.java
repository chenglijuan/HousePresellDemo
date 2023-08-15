package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：业务对账列表Excel导出
 * */
public class Tgpf_BalanceOfAccountExportExcelVO
{
	@Getter @Setter
	private Integer ordinal;//序号 
	@Getter	@Setter
	private String billTimeStamp;//记账日期	
	@Getter	@Setter
	private String bankName;//银行名称	
	@Getter	@Setter
	private String escrowedAccount;//托管账户
	@Getter	@Setter
	private String escrowedAccountTheName;//托管账号名称	
	@Getter	@Setter
	private Integer centerTotalCount;//业务总笔数	
	@Getter	@Setter
	private Double centerTotalAmount;//业务总金额	
	@Getter	@Setter
	private Integer bankTotalCount;//银行总笔数/网银总笔数	
	@Getter	@Setter
	private Double bankTotalAmount;//银行总金额/网银总笔数	
	@Getter	@Setter
	private String reconciliationState;// 对账状态（0-未对账（默认值）1-已对账）
}

package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管现金流量表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_LoanAmountStatement_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = 6338902028038502022L;
	
	@Getter @Setter 
	private Long tableId;//主键
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
	
	//页面接收字段
	@Getter @Setter
	private String endBillTimeStamp;//结束日期;
	
	@Getter @Setter
	private String queryKind;//查询类别 1 按日查询  2 按月查询  3 按年查询
}

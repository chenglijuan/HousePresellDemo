package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：银行放款项目入账情况表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_BankLoanInSituation_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -3529177904134579855L;

	@Getter @Setter
	private Long tableId;//主键
	@Getter @Setter
	private String companyName;//开发企业 
	@Getter @Setter
	private String projectName;//项目名称  
	@Getter @Setter
	private String escrowAcount;//托管账户 
	@Getter @Setter
	private String escrowAcountShortName;//托管账户简称
	@Getter @Setter
	private Double loanAmountIn;//入账金额
	@Getter @Setter
	private String billTimeStamp;//入账日期
	
	//页面接收字段
	@Getter @Setter
	private String endBillTimeStamp;//结束日期;
	@Getter @Setter
	private Long companyId;//开发企业ID
	@Getter @Setter
	private Long projectId;//项目ID
	@Getter @Setter
	private String loanOutDate;//出账日期
	@Getter @Setter
	private String endLoanOutDate;//出账日期
}

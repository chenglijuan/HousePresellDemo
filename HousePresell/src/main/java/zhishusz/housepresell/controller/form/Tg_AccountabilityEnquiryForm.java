package zhishusz.housepresell.controller.form;


import zhishusz.housepresell.database.po.Emmp_BankInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单:按权责发生制查询利息情况统计表
 * */
@ToString(callSuper=true)
public class Tg_AccountabilityEnquiryForm extends NormalActionForm
{	
	
	private static final long serialVersionUID = 7493658077477658595L;
	
	@Getter @Setter 
	private String bank; //存款银行
	
	@Getter @Setter 
	private Long bankId;
	
	@Getter @Setter 
	private String bankOfDeposit;//开户行
	
	@Getter @Setter 
	private String depositProperty;//存款性质
	
	@Getter @Setter 
	private String escrowAccount;//托管账号
	
	@Getter @Setter 
	private String escrowAcountName;//托管账号名称
	
	@Getter @Setter 
	private String recordDate;//登记日期
	
	
	
	@Getter @Setter 
	private String loadTime;//存入时间
	
	@Getter @Setter 
	private String loadTimeStart;//权责日期(起始)
	

	

	@Getter @Setter 
	private String expirationTime;//到期时间
	
	@Getter @Setter 
	private String expirationTimeEnd;//权责日期(结束)
	

	
	
	@Getter @Setter 
	private Double amountDeposited ;//存款金额
	
	@Getter @Setter 
	private String depositCeilings;//存款期限
	
	@Getter @Setter 
	private String interestRate;//利率
	
	@Getter @Setter 
	private Integer fate;//天数
	
	@Getter @Setter 
	private Double interest;//利息
	
	@Getter @Setter 
	private Long bankBranchId; //存款银行Id

}

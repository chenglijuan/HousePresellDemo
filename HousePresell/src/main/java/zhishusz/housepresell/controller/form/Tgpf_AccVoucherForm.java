package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_AccVoucherForm extends NormalActionForm
{
	private static final long serialVersionUID = -2762228818652010360L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private String billTimeStamp;//记账日期 
	@Getter @Setter
	private String theType;//业务类型 :入账、拨付
	@Getter @Setter
	private Integer tradeCount;//总笔数
	@Getter @Setter
	private Double totalTradeAmount;//总金额
	@Getter @Setter
	private String contentJson;//凭证内容
	@Getter @Setter
	private String payoutTimeStamp;//资金拨付日期 
	@Getter @Setter
	private Emmp_CompanyInfo company;//公司
	@Getter @Setter
	private Long companyId;//公司-Id
	@Getter @Setter
	private String theNameOfCompany;//企业名称-冗余
	@Getter @Setter
	private Empj_ProjectInfo project;//项目
	@Getter @Setter
	private Long projectId;//项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private Emmp_BankInfo bank;//银行
	@Getter @Setter
	private Long bankId;//银行-Id
	@Getter @Setter
	private String theNameOfBank;//银行名称-冗余
	@Getter @Setter
	private Integer DayEndBalancingState;//日终结算状态
	@Getter @Setter
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;//托管银行
	@Getter @Setter
	private Long bankAccountEscrowedId;//托管银行-Id
	@Getter @Setter
	private String theAccountOfBankAccountEscrowed;//托管账号
	@Getter @Setter
	private Double payoutAmount;//账户支付金额
	@Getter @Setter
	private Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF;//用款申请主表
	@Getter @Setter
	private Integer relatedType;//关联类型（0-一般拨付，1-特殊拨付）
	@Getter @Setter
	private Long relatedTableId;//关联主键
	
	
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

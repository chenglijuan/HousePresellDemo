package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：协定存款协议
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_DepositAgreementForm extends NormalActionForm
{
	private static final long serialVersionUID = 8591908485288104063L;
	
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
	private Emmp_BankInfo bank;//银行
	@Getter @Setter
	private Long bankId;//银行-Id
	@Getter @Setter
	private String theNameOfBank;//银行名称-冗余
	@Getter @Setter
	private Emmp_BankBranch bankOfDeposit;//开户行
	@Getter @Setter
	private String theNameOfDepositBank;//开户行名称
	@Getter @Setter
	private Long bankOfDepositId;//开户行-Id
	@Getter @Setter
	private Long escrowAccountId;//托管账户Id
	@Getter @Setter
	private String theAccountOfEscrowAccount;//托管账号
	@Getter @Setter
	private Tgxy_BankAccountEscrowed escrowAccount;//托管账户
	@Getter @Setter
	private Double depositRate;//协定存款利率（%）
	@Getter @Setter
	private Double orgAmount;//起始金额（万元）
	@Getter @Setter
	private String signDate;//签订日期
	@Getter @Setter
	private String timeLimit;//期限
	@Getter @Setter
	private String beginExpirationDate;//生效日期
	@Getter @Setter
	private String endExpirationDate;//到期日期
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String smAttachmentList;//附件信息
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}

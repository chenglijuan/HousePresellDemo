package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：银行对账单数据
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_BankUploadDataDetailForm extends NormalActionForm
{
	private static final long serialVersionUID = 5599010692892102837L;
	
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
	private Emmp_BankBranch bankBranch;//开户行
	@Getter @Setter
	private Long bankBranchId;//开户行-Id
	@Getter @Setter
	private String theNameOfBankBranch;//支行名称
	@Getter @Setter
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;//托管账号
	@Getter @Setter
	private Long bankAccountEscrowedId;//托管账号-Id
	@Getter @Setter
	private String theNameOfBankAccountEscrowed;//托管银行名称
	@Getter @Setter
	private String theAccountBankAccountEscrowed;//托管账号名称
	@Getter @Setter
	private String theAccountOfBankAccountEscrowed;//托管账户账号
	@Getter @Setter
	private Double tradeAmount;//交易金额
	@Getter @Setter
	private String enterTimeStamp;//入账日期
	@Getter @Setter
	private String recipientAccount;//对方账号
	@Getter @Setter
	private String recipientName;//对方名称
	@Getter @Setter
	private String lastCfgUser;//配置人
	@Getter @Setter
	private Long lastCfgTimeStamp;//配置日期
	@Getter @Setter
	private String bkpltNo;//银行平台流水号
	@Getter @Setter
	private String eCodeOfTripleAgreement;//三方协议号
	@Getter @Setter
	private Integer reconciliationState;//业务对账状态 【状态：0-未对账、1-已对账】
	@Getter @Setter
	private String reconciliationStamp;//业务对账时间
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String coreNo;//核心流水号
	@Getter @Setter
	private String reconciliationUser;//对账人
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfTripleAgreement() {
		return eCodeOfTripleAgreement;
	}
	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement) {
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}
}

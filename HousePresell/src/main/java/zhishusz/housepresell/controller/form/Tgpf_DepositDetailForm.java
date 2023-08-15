package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：资金归集-明细表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_DepositDetailForm extends NormalActionForm
{
	private static final long serialVersionUID = 5076670211320916145L;
	
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
	private String eCodeFromPayment;//缴款序号
	@Getter @Setter
	private Integer fundProperty;//资金性质
	@Getter @Setter
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;//托管账户信息
	@Getter @Setter
	private Long bankAccountEscrowedId;//托管账户信息-Id
	@Getter @Setter
	private Emmp_BankBranch bankBranch;//托管账户开户行
	@Getter @Setter
	private Long bankBranchId;//托管账户开户行-Id
	@Getter @Setter
	private String theNameOfBankAccountEscrowed;//托管账户名称
	@Getter @Setter
	private String theAccountOfBankAccountEscrowed;//托管账号
	@Getter @Setter
	private String theNameOfCreditor;//贷款人
	@Getter @Setter
	private String idType;//证件类型 S_IDType
	@Getter @Setter
	private String idNumber;//证件号码
	@Getter @Setter
	private String bankAccountForLoan;//用于接收贷款的银行账号
	@Getter @Setter
	private Double loanAmountFromBank;//银行放款金额（元）
	@Getter @Setter
	private String billTimeStamp;//记账日期
	@Getter @Setter
	private String eCodeFromBankCore;//银行核心流水号
	@Getter @Setter
	private String eCodeFromBankPlatform;//银行平台流水号
	@Getter @Setter
	private String remarkFromDepositBill;//缴款记账备注
	@Getter @Setter
	private Emmp_BankBranch theNameOfBankBranchFromDepositBill;//缴费银行网点
	@Getter @Setter
	private Long theNameOfBankBranchFromDepositBillId;//缴费银行网点-Id
	@Getter @Setter
	private String eCodeFromBankWorker;//网点柜员号
	@Getter @Setter
	private Integer depositState;//缴款状态
	@Getter @Setter
	private Tgpf_DayEndBalancing dayEndBalancing;//关联日终结算
	@Getter @Setter
	private Long dayEndBalancingId;//关联日终结算-Id
	@Getter @Setter
	private String depositDatetime;//缴款记账日期
	@Getter @Setter
	private String reconciliationTimeStampFromBusiness;//业务对账日期
	@Getter @Setter
	private Integer reconciliationStateFromBusiness;//业务对账状态
	@Getter @Setter
	private String reconciliationTimeStampFromCyberBank;//网银对账日期
	@Getter @Setter
	private Integer reconciliationStateFromCyberBank;//网银对账状态
	@Getter @Setter
	private Boolean hasVoucher;//是否已生成凭证
	@Getter @Setter
	private String timestampFromReverse;//红冲-日期
	@Getter @Setter
	private Integer theStateFromReverse;//红冲-状态
	@Getter @Setter
	private String eCodeFromReverse;//红冲-平台流水号	
	@Getter @Setter @IFieldAnnotation(remark="关联三方协议")
	private Tgxy_TripleAgreement tripleAgreement;
	@Getter @Setter @IFieldAnnotation(remark="关联三方协议")
	private Long tripleAgreementId;
	@Getter @Setter @IFieldAnnotation(remark="判断条件")
	private String judgeStatement;	
	@Getter @Setter @IFieldAnnotation(remark="对账类型（0-自动对账，1-手动对账）")
	private Integer accountType;
	
	
	//接收前台传入字段
	@Getter @Setter
	private String endBillTimeStamp;//记账日期结束
	
	//接收前台传入字段
	@Getter @Setter
	private Long companyId;
	@Getter @Setter
	private Long projectId;
	@Getter @Setter
	private Empj_ProjectInfo project;//项目
	@Getter @Setter
	private String startTimeStamp;//开始时间
	@Getter @Setter
	private String endTimeStamp;//申请结束日期
	@Getter @Setter
	private String companyName;//企业名称
	@Getter @Setter
	private String[] companyNames;//企业名称
	@Getter @Setter
	private Emmp_CompanyInfo companyInfo;//开发企业
	
	@Getter @Setter
	private Long cityRegionId;//区域Id
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeFromPayment() {
		return eCodeFromPayment;
	}
	public void seteCodeFromPayment(String eCodeFromPayment) {
		this.eCodeFromPayment = eCodeFromPayment;
	}
	public String geteCodeFromBankCore() {
		return eCodeFromBankCore;
	}
	public void seteCodeFromBankCore(String eCodeFromBankCore) {
		this.eCodeFromBankCore = eCodeFromBankCore;
	}
	public String geteCodeFromBankPlatform() {
		return eCodeFromBankPlatform;
	}
	public void seteCodeFromBankPlatform(String eCodeFromBankPlatform) {
		this.eCodeFromBankPlatform = eCodeFromBankPlatform;
	}
	public String geteCodeFromBankWorker() {
		return eCodeFromBankWorker;
	}
	public void seteCodeFromBankWorker(String eCodeFromBankWorker) {
		this.eCodeFromBankWorker = eCodeFromBankWorker;
	}
	public String geteCodeFromReverse() {
		return eCodeFromReverse;
	}
	public void seteCodeFromReverse(String eCodeFromReverse) {
		this.eCodeFromReverse = eCodeFromReverse;
	}
}

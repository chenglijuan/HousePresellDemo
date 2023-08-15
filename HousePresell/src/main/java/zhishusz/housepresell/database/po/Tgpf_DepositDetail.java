package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：资金归集-明细表
 * */
@ITypeAnnotation(remark="资金归集-明细表")
public class Tgpf_DepositDetail implements Serializable
{
	private static final long serialVersionUID = -3008643673738903910L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="资金归集编号")
	private String depositCode;//TODO 系统生成，生成规则：区位（2）+标志（0-交款）+银行编号（2）+年月日（6）+按天流水（4）+验证码（1）
	
	@IFieldAnnotation(remark="缴款序号")
	private String eCodeFromPayment;
	
	@Getter @Setter @IFieldAnnotation(remark="资金性质")
	private Integer fundProperty;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户信息")
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户开户行")
	private Emmp_BankBranch bankBranch;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户名称",columnName="theNameOfBAE")
	private String theNameOfBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账号",columnName="theAccountOfBAE")
	private String theAccountOfBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="贷款人")
	private String theNameOfCreditor;
	
	@Getter @Setter @IFieldAnnotation(remark="证件类型 S_IDType")
	private String idType;

	@Getter @Setter @IFieldAnnotation(remark="证件号码")
	private String idNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="用于接收贷款的银行账号")
	private String bankAccountForLoan;
	
	@Getter @Setter @IFieldAnnotation(remark="银行放款金额（元）")
	private Double loanAmountFromBank;
	
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;

	@IFieldAnnotation(remark="银行核心流水号")
	private String eCodeFromBankCore;
	
	@IFieldAnnotation(remark="银行平台流水号")
	private String eCodeFromBankPlatform;

	@Getter @Setter @IFieldAnnotation(remark="缴款记账备注")
	private String remarkFromDepositBill;

	@Getter @Setter @IFieldAnnotation(remark="缴费银行网点", columnName="bankBranchNameFromDeposit")
	private Emmp_BankBranch theNameOfBankBranchFromDepositBill;

	@IFieldAnnotation(remark="网点柜员号")
	private String eCodeFromBankWorker;
	
	@Getter @Setter @IFieldAnnotation(remark="关联三方协议")
	private Tgxy_TripleAgreement tripleAgreement;

	@Getter @Setter @IFieldAnnotation(remark="缴款状态")
	private Integer depositState;
	
	@Getter @Setter @IFieldAnnotation(remark="关联日终结算")
	private Tgpf_DayEndBalancing dayEndBalancing;
	
	@Getter @Setter @IFieldAnnotation(remark="缴款记账日期")
	private String depositDatetime;

	@Getter @Setter @IFieldAnnotation(remark="业务对账日期",columnName="reconciliationTSFromB")
	private String reconciliationTimeStampFromBusiness;
	
	@Getter @Setter @IFieldAnnotation(remark="业务对账状态",columnName="reconciliationSFromB")
	private Integer reconciliationStateFromBusiness;
    
	@Getter @Setter @IFieldAnnotation(remark="网银对账日期",columnName="reconciliationTSFromOB")
	private String reconciliationTimeStampFromCyberBank;
	
	@Getter @Setter @IFieldAnnotation(remark="网银对账状态",columnName="reconciliationSFromOB")
	private Integer reconciliationStateFromCyberBank;
	
	@Getter @Setter @IFieldAnnotation(remark="是否已生成凭证")
	private Boolean hasVoucher;
	
	@Getter @Setter @IFieldAnnotation(remark="红冲-日期")
	private String timestampFromReverse;
	
	@Getter @Setter @IFieldAnnotation(remark="红冲-状态")
	private Integer theStateFromReverse;
	
	@IFieldAnnotation(remark="红冲-平台流水号")
	private String eCodeFromReverse;
	
	@Getter @Setter @IFieldAnnotation(remark="银行-记账日期") // 接受字段，不入数据库
	private String bankBillTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="银行-缴款金额") // 接受字段，不入数据库
	private Double bankAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="银行-三方协议号") // 接受字段，不入数据库
	private String tripleAgreementNumBank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行-缴款人名称") // 接受字段，不入数据库
	private String theNameOfCreditorBank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行-缴款账号") // 接受字段，不入数据库
	private String bankAccountForLoanBank;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议号") // 接受字段，不入数据库
	private String tripleAgreementNum;	
	
	@Getter @Setter @IFieldAnnotation(remark="业务总笔数") // 接受字段，不入数据库
	private Integer centerTotalCount;
	
	@Getter @Setter @IFieldAnnotation(remark="业务总金额") // 接受字段，不入数据库
	private Double centerTotalAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="银行总笔数") // 接受字段，不入数据库
	private Integer bankTotalCount;
	
	@Getter @Setter @IFieldAnnotation(remark="业务总金额") // 接受字段，不入数据库
	private Double bankTotalAmount;
	

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

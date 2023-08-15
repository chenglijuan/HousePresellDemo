package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：银行对账单数据
 * 银行上传txt文件到Ftp
 * 
 * TODO 交易时间、账号、银行核心流水号、银行平台流水号、借贷方向、缴款金额、手续费、入账金额、摘要、三方协议编号、序号、平台交易流水号、结算渠道。（以上信息用“|”定长分隔）。例：
 *      2016-08-01 15:32:13|32678769318764|425432543523000012|20180101001|借|30000000|0|30000000|张三贷款|6100114070600011|1|129839983918332131311|1|
 * */
@ITypeAnnotation(remark="银行对账单数据")
public class Tgpf_BankUploadDataDetail implements Serializable
{
	private static final long serialVersionUID = -8920290557978815777L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="银行")
	private Emmp_BankInfo bank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称-冗余")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private Emmp_BankBranch bankBranch;//【根据托管账号自动带出】
	
	@Getter @Setter @IFieldAnnotation(remark="支行名称")
	private String theNameOfBankBranch;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账号")
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="托管银行名称",columnName="theNameOfBankAE")
	private String theNameOfBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账号名称",columnName="theAccountBankAE")
	private String theAccountBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户账号",columnName="theAccountOfBAE")
	private String theAccountOfBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="交易金额")
	private Double tradeAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String enterTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="对方账号")
	private String recipientAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="对方名称")
	private String recipientName;

	@Getter @Setter @IFieldAnnotation(remark="配置人")
	private String lastCfgUser;
	
	@Getter @Setter @IFieldAnnotation(remark="配置日期")
	private Long lastCfgTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="银行平台流水号")
	private String bkpltNo;
	
	@IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="业务对账状态")
	private Integer reconciliationState;//【状态：0-未对账、1-已对账】
	
	@Getter @Setter @IFieldAnnotation(remark="业务对账时间")
	private String reconciliationStamp;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="核心流水号")
	private String coreNo;
	
	@Getter @Setter @IFieldAnnotation(remark="对账人")
	private String reconciliationUser;
	
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

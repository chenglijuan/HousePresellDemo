package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：网银对账-后台上传的账单原始Excel数据-明细表
 * 工作人员通过后台上传
 * */
@ITypeAnnotation(remark="网银对账-后台上传的账单原始Excel数据-明细表")
public class Tgpf_CyberBankStatementDtl implements Serializable
{
	private static final long serialVersionUID = 4162157794677747565L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="关联‘网银对账’主表")
	private Tgpf_CyberBankStatement mainTable;

	@Getter @Setter @IFieldAnnotation(remark="交易日期")
	private String tradeTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="对方账号")
	private String recipientAccount;

	@Getter @Setter @IFieldAnnotation(remark="对方账户名称")
	private String recipientName;

	@Getter @Setter @IFieldAnnotation(remark="备注摘要")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="收入")
	private Double income;

	@Getter @Setter @IFieldAnnotation(remark="支出")
	private Double payout;

	@Getter @Setter @IFieldAnnotation(remark="余额")
	private Double balance;

	@Getter @Setter @IFieldAnnotation(remark="网银对账状态")
	private Integer reconciliationState;//【状态：0-未对账、1-已对账】

	@Getter @Setter @IFieldAnnotation(remark="对账人")
	private String reconciliationUser;//【对账反写】
	
	@Getter @Setter @IFieldAnnotation(remark="发生额")
	private Double tradeAmount;

	@Getter @Setter @IFieldAnnotation(remark="网银对账时间")
	private String reconciliationStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="核心流水号")
	private String coreNo;
	
	@Getter @Setter @IFieldAnnotation(remark="文件上传日期")
	private String uploadTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="文件上传人")
	private String uploadUser;
	
	@Getter @Setter @IFieldAnnotation(remark="付款")
	private String sendAccountNum;
	
	@Getter @Setter @IFieldAnnotation(remark="资金归集明细主键")
	private Long tgpf_DepositDetailId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源 0-手动 1-接口")
	private String sourceType;
	
	@Getter @Setter @IFieldAnnotation(remark="对方账户名称")// 接受字段，不入数据库
	private String busRecipientName;
	
	@Getter @Setter @IFieldAnnotation(remark="交易日期")// 接受字段，不入数据库
	private String busTradeTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="对方账号")// 接受字段，不入数据库
	private String busIecipientAccount;

	@Getter @Setter @IFieldAnnotation(remark="三方协议号")// 接受字段，不入数据库
	private String tripleAgreementNum;
	
	@Getter @Setter @IFieldAnnotation(remark="收入")// 接受字段，不入数据库
	private Double busIncome;
	
	@Getter @Setter @IFieldAnnotation(remark="摘要")// 接受字段，不入数据库
	private String busRemark;
	
	@Getter @Setter @IFieldAnnotation(remark="网银三方协议号")// 接受字段，不入数据库
	private String cyBankTripleAgreementNum;
	
	@Getter @Setter @IFieldAnnotation(remark="接收的明细Id")
	private Long detailedId;
	
	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
}

package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：对账列表
 * */
@ITypeAnnotation(remark="对账列表")
public class Tgpf_BalanceOfAccount implements Serializable
{
	private static final long serialVersionUID = 2073690049461315031L;

	// ---------公共字段-Start---------//
	@Getter	@Setter	@IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;

	@Getter	@Setter	@IFieldAnnotation(remark = "状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter	@Setter	@IFieldAnnotation(remark = "业务状态")
	private String busiState;// 凭证状态【自动写入枚举：0-未推送、1-已推送】

	@IFieldAnnotation(remark = "编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter	@Setter	@IFieldAnnotation(remark = "创建人")
	private Sm_User userStart;

	@Getter	@Setter	@IFieldAnnotation(remark = "创建时间")
	private Long createTimeStamp;

	@Getter	@Setter	@IFieldAnnotation(remark = "修改人")
	private Sm_User userUpdate;

	@Getter	@Setter	@IFieldAnnotation(remark = "最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter	@Setter	@IFieldAnnotation(remark = "备案人")
	private Sm_User userRecord;

	@Getter	@Setter	@IFieldAnnotation(remark = "备案日期")
	private Long recordTimeStamp;
	// ---------公共字段-Start---------//
	
	@Getter	@Setter	@IFieldAnnotation(remark = "记账日期")
	private String billTimeStamp;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行名称")
	private String bankName;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "托管账户")
	private String escrowedAccount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "托管账号名称")
	private String escrowedAccountTheName;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "业务总笔数")
	private Integer centerTotalCount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "业务总金额")
	private Double centerTotalAmount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行总笔数")
	private Integer bankTotalCount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行总金额")
	private Double bankTotalAmount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "网银总笔数")
	private Integer cyberBankTotalCount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "网银总金额")
	private Double cyberBankTotalAmount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "网银对账状态(0-未对账，1-已对账)")
	private Integer accountType;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "对账日期")
	private String reconciliationDate;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "业务对账状态（0-未对账（默认值）1-已对账）")
	private Integer reconciliationState;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行网点")
	private Emmp_BankBranch bankBranch;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "托管表")
	private Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed;

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}

package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：业务对账-日终结算
 * */
@ITypeAnnotation(remark="业务对账-日终结算")
public class Tgpf_DayEndBalancing implements Serializable
{
	private static final long serialVersionUID = 5183905594536633649L;
	
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

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	//---------公共字段-Start---------//
	@Getter	@Setter	@IFieldAnnotation(remark = "托管表")
	private Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行名称")
	private String bankName;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "托管账户")
	private String escrowedAccount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "托管账号名称")
	private String escrowedAccountTheName;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "对账总笔数")
	private Integer totalCount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "对账总金额")
	private Double totalAmount;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "记账日期")
	private String billTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark= "记账状态")
	private Integer recordState;
	
	@Getter @Setter @IFieldAnnotation(remark= "日终结算状态")
	private Integer settlementState;
	
	@Getter @Setter @IFieldAnnotation(remark= "日终结算业务日期")
	private String settlementTime;
	
    //TODO  日终结算业务日期、日终结算状态(settlementState)、日终结算完成时间、留存权益计算状态、留存权益计算完成时间、留存权益执行人

}

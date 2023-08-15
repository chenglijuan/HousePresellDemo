   package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：统筹-账户状况信息保存表 
 * 数据来源托管账户表
 * */
@ITypeAnnotation(remark="统筹-账户状况信息保存表")
public class Tgpf_OverallPlanAccout implements Serializable
{
	private static final long serialVersionUID = -5011058689609598506L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="资金统筹")
	private Tgpf_FundOverallPlan fundOverallPlan;
	
	@IFieldAnnotation(remark="统筹单号-冗余")
	private String eCodeOfFundOverallPlan;
	
	@Getter @Setter @IFieldAnnotation(remark="关联托管账号")
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;

	@Getter @Setter @IFieldAnnotation(remark="关联托管账号")
	private Tgxy_BankAccountEscrowedView bankAccountEscrowedView;
	
	@IFieldAnnotation(remark="用款申请单号-冗余",columnName="eCodeOfAWMT")
	private String eCodeOfAFWithdrawMainTable;
	
	@Getter @Setter @IFieldAnnotation(remark="统筹拨付金额")
	private Double overallPlanAmount;

	@Getter @Setter @IFieldAnnotation(remark="当天入账金额调整项")
	private Double accountAmountTrim;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfFundOverallPlan() {
		return eCodeOfFundOverallPlan;
	}

	public void seteCodeOfFundOverallPlan(String eCodeOfFundOverallPlan) {
		this.eCodeOfFundOverallPlan = eCodeOfFundOverallPlan;
	}

	public String geteCodeOfAFWithdrawMainTable() {
		return eCodeOfAFWithdrawMainTable;
	}

	public void seteCodeOfAFWithdrawMainTable(String eCodeOfAFWithdrawMainTable) {
		this.eCodeOfAFWithdrawMainTable = eCodeOfAFWithdrawMainTable;
	}
}

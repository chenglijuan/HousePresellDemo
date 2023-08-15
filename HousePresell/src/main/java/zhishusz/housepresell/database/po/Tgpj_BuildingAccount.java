package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：楼幢账户
 */
@ITypeAnnotation(remark="楼幢账户")
public class Tgpj_BuildingAccount implements Serializable,ILogable
{
	private static final long serialVersionUID = -4828146775026383507L;
	
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

	@Getter @Setter @IFieldAnnotation(remark="最后修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@Getter @Setter @IFieldAnnotation(remark="关联支付保证")
	private Empj_PaymentGuarantee payment;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;
	
	@Getter @Setter @IFieldAnnotation(remark="托管标准")
	private String escrowStandard;
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积")
	private Double escrowArea;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积")
	private Double buildingArea;
	
	@Getter @Setter @IFieldAnnotation(remark="初始受限额度（元）")
	private Double orgLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="当前形象进度")
	private String currentFigureProgress;
	
	@Getter @Setter @IFieldAnnotation(remark="当前受限比例（%）")
	private Double currentLimitedRatio;

	@Getter @Setter @IFieldAnnotation(remark="节点受限额度（元）")
	private Double nodeLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="累计可计入保证金额（元）")
	private Double totalGuaranteeAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="现金受限额度（元）")
	private Double cashLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="有效受限额度（元）")
	private Double effectiveLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="总入账金额（元）")
	private Double totalAccountAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="溢出金额（元）")
	private Double spilloverAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="已拨付金额（元）")
	private Double payoutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="已申请未拨付金额（元）")
	private Double appliedNoPayoutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="已申请退款未拨付金额（元）")
	private Double applyRefundPayoutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="已退款金额（元）")
	private Double refundAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="当前托管余额（元）")
	private Double currentEscrowFund;
	
	@Getter @Setter @IFieldAnnotation(remark="可划拨金额（元）")
	private Double allocableAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="拨付冻结金额")
	private Double appropriateFrozenAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="预售系统楼幢住宅备案均价",columnName="recordAvgPriceBldPS")
	private Double recordAvgPriceOfBuildingFromPresellSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢住宅备案均价")
	private Double recordAvgPriceOfBuilding;

	@IFieldAnnotation(remark="关联日志Id")
	private Long logId;
	
	@Getter @Setter @IFieldAnnotation(remark="幢托管资金实际可用余额")
	private Double actualAmount;

	@Getter @Setter @IFieldAnnotation(remark="支付保证封顶百分比")
	private Double paymentLines;
	
	@Getter @Setter @IFieldAnnotation(remark="支付保证封顶额度")
	private Double paymentProportion;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢项目建设已实际支付金额（元）")
	private Double buildAmountPaid;//楼幢项目建设已实际支付金额（元）
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢项目建设待支付承保累计金额（元）")
	private Double buildAmountPay;//楼幢项目建设待支付承保累计金额（元）
	
	@Getter @Setter @IFieldAnnotation(remark="已落实支付保证累计金额（元）")
	private Double totalAmountGuaranteed;//已落实支付保证累计金额（元）
	
	@Getter @Setter @IFieldAnnotation(remark="版本号")
	private Long versionNo;

	@Getter @Setter @IFieldAnnotation(remark = "当前该楼幢对应的受限额度版本节点")
	private Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl;
	
	@Getter @Setter @IFieldAnnotation(remark = "当前该楼幢对应的受限额度主表")
	private Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF;
	
	@Override
	public Long getLogId()
	{
		return logId;
	}
	@Override
	public void setLogId(Long logId)
	{
		this.logId = logId;
	}
	@Override
	public String getLogData()
	{
		return null;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}
	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
}

package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢账户表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Qs_BuildingAccount_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -2144959986945805337L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String theNameOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="施工楼幢")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String deliveryType;
	
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

	@Getter @Setter @IFieldAnnotation(remark="当前受限额度（元）")
	private Double currentLimitedAmount;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="预售系统楼幢住宅备案均价")
	private Double recordAvgPriceBldPS;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢住宅备案均价")
	private Double recordAvgPriceOfBuilding;
	
	@Getter @Setter
	private String sourceType; //来源页面 0 - 正常查询

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	
	
	
}

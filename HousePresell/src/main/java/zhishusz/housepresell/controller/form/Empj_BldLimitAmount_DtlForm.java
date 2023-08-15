package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.util.IFieldAnnotation;

/*
 * Form表单：申请表-进度节点变更
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BldLimitAmount_DtlForm extends NormalActionForm
{
	private static final long serialVersionUID = -4447133442721315353L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@IFieldAnnotation(remark="申请单号-冗余字段")
	public String eCodeOfMainTable;
	
	@Getter @Setter @IFieldAnnotation(remark="关联申请主表")
	public Empj_BldLimitAmount mainTable;
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@IFieldAnnotation(remark="项目编号")
	public String eCodeOfProject;

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;

	@Getter @Setter @IFieldAnnotation(remark="地上楼层数")
	private Double upfloorNumber;
 
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="当前楼幢住宅备案均价（元/㎡）", columnName="recordAvgPriceOfBuilding")
	private Double recordAveragePriceOfBuilding;
	
	@Getter @Setter @IFieldAnnotation(remark="托管标准")
	private String escrowStandard;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String deliveryType;
	
	@Getter @Setter @IFieldAnnotation(remark="初始受限额度")
	private Double orgLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="当前形象进度")
	private String currentFigureProgress;
	
	@Getter @Setter @IFieldAnnotation(remark="当前受限比例")
	private Double currentLimitedRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="节点受限额度")//当前受限额度
	private Double nodeLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="累计可计入保证金额")
	private Double totalGuaranteeAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="现金受限额度")
	private Double cashLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="有效受限额度")
	private Double effectiveLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="拟变更形象进度")
	private Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress;
	
	@Getter @Setter @IFieldAnnotation(remark="拟变更受限比例")
	private Double expectLimitedRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="拟变更受限额度")
	private Double expectLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="拟变更有效受限额度")
	private Double expectEffectLimitedAmount;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="审批结果（0-不通过 1-通过）")
	private String approvalResult;
	
	@Getter @Setter @IFieldAnnotation(remark="审批批语")
	private String approvalInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="主表申请状态")
	private String afState;

	public String geteCodeOfDevelopCompany()
	{
		return eCodeOfDevelopCompany;
	}
	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany)
	{
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	public String geteCodeOfProject()
	{
		return eCodeOfProject;
	}
	public void seteCodeOfProject(String eCodeOfProject)
	{
		this.eCodeOfProject = eCodeOfProject;
	}
	public String geteCodeOfBuilding()
	{
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding)
	{
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	public String geteCodeOfMainTable() {
		return eCodeOfMainTable;
	}
	public void seteCodeOfMainTable(String eCodeOfMainTable) {
		this.eCodeOfMainTable = eCodeOfMainTable;
	}
	
	
}

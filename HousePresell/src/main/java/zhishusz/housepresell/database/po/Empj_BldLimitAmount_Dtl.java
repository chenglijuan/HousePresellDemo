package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：申请表-工程进度节点更新-明细表
 * */
@ITypeAnnotation(remark="申请表-工程进度节点更新-明细表")
public class Empj_BldLimitAmount_Dtl implements Serializable
{
	private static final long serialVersionUID = 495638873565081976L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="流程状态")
	private String approvalState;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="A预测完成时间")
    private Date completeDateOne;
	
	@Getter @Setter @IFieldAnnotation(remark="B预测完成时间")
    private Date completeDateTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="A审批结果（0-不通过 1-通过）")
    private String resultOne;
	
	@Getter @Setter @IFieldAnnotation(remark="A不通过原因")
    private String resultInfoOne;
	
	@Getter @Setter @IFieldAnnotation(remark="B审批结果（0-不通过 1-通过）")
    private String resultTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="B不通过原因")
    private String resultInfoTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构A")
    private Emmp_CompanyInfo companyOne;
    
    @Getter @Setter @IFieldAnnotation(remark="监理机构A名称")
    private String companyOneName;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构B")
    private Emmp_CompanyInfo companyTwo;
    
    @Getter @Setter @IFieldAnnotation(remark="监理机构B名称")
    private String companyTwoName;
    
    @Getter @Setter @IFieldAnnotation(remark="预测节点")
    private Tgpj_BldLimitAmountVer_AFDtl predictionNode;
    
    @Getter @Setter @IFieldAnnotation(remark="预测节点名称")
    private String predictionNodeName;
	
	@Getter @Setter @IFieldAnnotation(remark="附件信息")
	private List<Sm_AttachmentCfg_Copy> smAttachmentCfgList;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfMainTable() {
		return eCodeOfMainTable;
	}

	public void seteCodeOfMainTable(String eCodeOfMainTable) {
		this.eCodeOfMainTable = eCodeOfMainTable;
	}

	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}

	public String geteCodeOfProject() {
		return eCodeOfProject;
	}

	public void seteCodeOfProject(String eCodeOfProject) {
		this.eCodeOfProject = eCodeOfProject;
	}

	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}

	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}

	public String geteCodeFromPublicSecurity() {
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}

	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
}

package zhishusz.housepresell.database.po;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：申请表-受限额度变更
 * */
@ITypeAnnotation(remark="申请表-受限额度变更")
public class Empj_BldLimitAmount_AF implements Serializable,IApprovable
{
	private static final long serialVersionUID = 118037791673262233L;
	
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

	//工作时限办理
	@Getter @Setter @IFieldAnnotation(remark="受理时间")
	private Long acceptTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="受理说明")
	private String acceptExplain;

	@Getter @Setter @IFieldAnnotation(remark="预约时间")
	private Long appointTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="预约说明")
	private String appointExplain;

	@Getter @Setter @IFieldAnnotation(remark="现场勘查时间")
	private Long sceneInvestigationTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="现场勘查说明")
	private String sceneInvestigationExplain;
	
	@Getter @Setter @IFieldAnnotation(remark="关联进度节点变更子表")
	private Long dtlId;

	@Override
	public String getSourceType() {
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String> peddingApprovalkey = new ArrayList<String>();

		peddingApprovalkey.add("expectFigureProgressId");
		peddingApprovalkey.add("remark");
		peddingApprovalkey.add("generalAttachmentList");
		return peddingApprovalkey;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
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

	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	public String geteCodeFromPublicSecurity() {
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}

	public List getNeedFieldList(){
		return Arrays.asList("eCode");
	}

}

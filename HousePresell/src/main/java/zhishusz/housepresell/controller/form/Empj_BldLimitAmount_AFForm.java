package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BldLimitAmount_AFForm extends NormalActionForm
{
	private static final long serialVersionUID = -2332141917226510619L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private String eCodeOfProject;//项目编号
	@Getter @Setter
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private Double upfloorNumber;//地上楼层数
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private Double recordAveragePriceOfBuilding;//当前楼幢住宅备案均价（元/㎡）
	@Getter @Setter
	private String escrowStandard;//托管标准
	@Getter @Setter
	private String deliveryType;//交付类型
	@Getter @Setter
	private Double orgLimitedAmount;//初始受限额度
	@Getter @Setter
	private String currentFigureProgress;//当前形象进度
	@Getter @Setter
	private Double currentLimitedRatio;//当前受限比例
	@Getter @Setter
	private Double nodeLimitedAmount;//节点受限额度
	@Getter @Setter
	private Double totalGuaranteeAmount;//累计可计入保证金额
	@Getter @Setter
	private Double cashLimitedAmount;//现金受限额度
	@Getter @Setter
	private Double effectiveLimitedAmount;//有效受限额度
	@Getter @Setter
	private Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress;//拟变更形象进度
	@Getter @Setter
	private Long expectFigureProgressId;//拟变更形象进度
	@Getter @Setter
	private Double expectLimitedRatio;//拟变更受限比例
	@Getter @Setter
	private Double expectLimitedAmount;//拟变更受限额度
	@Getter @Setter
	private Double expectEffectLimitedAmount;//拟变更有效受限额度
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Long acceptTimeStamp;//受理时间
	@Getter @Setter
	private String acceptExplain;//受理说明
	@Getter @Setter
	private Long appointTimeStamp;//预约时间
	@Getter @Setter
	private String appointExplain;//预约说明
	@Getter @Setter
	private Long sceneInvestigationTimeStamp;//现场勘查时间
	@Getter @Setter
	private String sceneInvestigationExplain;//现场勘查说明
	@Getter @Setter
	private String isSign;//是否过滤签章 0 - 否  1 - 是

	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
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
}

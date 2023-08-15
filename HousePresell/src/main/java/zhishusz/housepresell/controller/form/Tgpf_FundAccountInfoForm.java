package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundAccountInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 5105178487061965469L;
	
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
	private Emmp_CompanyInfo companyInfo;//机构信息
	@Getter @Setter
	private Long companyInfoId;//机构信息-Id
	@Getter @Setter
	private String theNameOfCompany;//托管库企业名称
	@Getter @Setter
	private String eCodeOfCompany;//托管库企业编码
	@Getter @Setter
	private String fullNameOfCompanyFromFinanceSystem;//财务库企业全称
	@Getter @Setter
	private String shortNameOfCompanyFromFinanceSystem;//财务库企业简称
	@Getter @Setter
	private Empj_ProjectInfo project;//托管库项目
	@Getter @Setter
	private Long projectId;//托管库项目-Id
	@Getter @Setter
	private String theNameOfProject;//托管库项目名称-冗余
	@Getter @Setter
	private String eCodeOfProject;//托管库项目编码-冗余
	@Getter @Setter
	private String fullNameOfProjectFromFinanceSystem;//财务库项目全称
	@Getter @Setter
	private String shortNameOfProjectFromFinanceSystem;//财务库项目简称
	@Getter @Setter
	private Empj_BuildingInfo building;//楼幢
	@Getter @Setter
	private Long buildingId;//楼幢-Id
	@Getter @Setter
	private String eCodeFromConstruction;//托管库楼幢施工编号
	@Getter @Setter
	private String eCodeOfBuilding;//托管库楼幢编码-冗余
	@Getter @Setter
	private String fullNameOfBuildingFromFinanceSystem;//财务库楼幢全称
	@Getter @Setter
	private String shortNameOfBuildingFromFinanceSystem;//财务库楼幢简称
	@Getter @Setter
	private Integer operateType;//操作类型
	@Getter @Setter
	private String depositRemark;//托管系统备注
	@Getter @Setter
	private String financeRemark;//财务系统备注

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfCompany() {
		return eCodeOfCompany;
	}
	public void seteCodeOfCompany(String eCodeOfCompany) {
		this.eCodeOfCompany = eCodeOfCompany;
	}
	public String geteCodeOfProject() {
		return eCodeOfProject;
	}
	public void seteCodeOfProject(String eCodeOfProject) {
		this.eCodeOfProject = eCodeOfProject;
	}
	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
}

package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：推送给财务系统-设置
 * */
@ITypeAnnotation(remark="推送给财务系统-设置")
public class Tgpf_FundAccountInfo implements Serializable
{
	private static final long serialVersionUID = -2885368634551130958L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="机构信息")
	private Emmp_CompanyInfo companyInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="托管库企业名称")
	private String theNameOfCompany;
	
	@IFieldAnnotation(remark="托管库企业编码")
	private String eCodeOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="财务库企业全称",columnName="fullNameOfCFromFS")
	private String fullNameOfCompanyFromFinanceSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="财务库企业简称",columnName="shortNameOfCFromFS")
	private String shortNameOfCompanyFromFinanceSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="托管库项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="托管库项目名称-冗余")
	private String theNameOfProject;
	
	@IFieldAnnotation(remark="托管库项目编码-冗余")
	private String eCodeOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="财务库项目全称",columnName="fullNameOfPFromFS")
	private String fullNameOfProjectFromFinanceSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="财务库项目简称",columnName="shortNameOfPFromFS")
	private String shortNameOfProjectFromFinanceSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="托管库楼幢施工编号")
	private String eCodeFromConstruction;
	
	@IFieldAnnotation(remark="托管库楼幢编码-冗余")
	private String eCodeOfBuilding;
	
	@Getter @Setter @IFieldAnnotation(remark="财务库楼幢全称",columnName="fullNameOfBFromFS")
	private String fullNameOfBuildingFromFinanceSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="财务库楼幢简称",columnName="shortNameOfBFromFS")
	private String shortNameOfBuildingFromFinanceSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="操作类型")
	private Integer operateType;//【不可修改，枚举：0-新增、1-变更】
	
	@Getter @Setter @IFieldAnnotation(remark="配置人")
	private String configureUser;
	
	@Getter @Setter @IFieldAnnotation(remark="配置时间")
	private String configureTime;
	
	@Getter @Setter @IFieldAnnotation(remark="托管系统备注")
	private String depositRemark;
	
	@Getter @Setter @IFieldAnnotation(remark="财务系统备注")
	private String financeRemark;
	
	@Getter @Setter @IFieldAnnotation(remark="财务系统编号")
	private String ITEM_ISN;
	
	@Getter @Setter @IFieldAnnotation(remark="财务系统编号2")
	private String ITEM_CODE;

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

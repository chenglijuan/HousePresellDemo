package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：项目-工程进度预测-主表
 * */
//TODO 参照需求文档 2.2.7.6 --p88 
@ITypeAnnotation(remark="项目-工程进度预测-主表")
public class Empj_PjDevProgressForcast implements Serializable
{
	private static final long serialVersionUID = -6628989620790744728L;
	
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

	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@IFieldAnnotation(remark="项目编号-冗余")
	private String eCodeOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;

	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private Sm_CityRegionInfo cityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="区域名称")
	private String theNameOfCityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="所属街道")
	private Sm_StreetInfo streetInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="街道名称")
	private String theNameOfStreet;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String payoutType;

	@Getter @Setter @IFieldAnnotation(remark="当前形象进度")
	private Double currentFigureProgress;//【根据施工编号自动带出】
	
	@Getter @Setter @IFieldAnnotation(remark="当前建设进度")
	private String currentBuildProgress;//【手工输入，封顶之前输入具体楼层，如“12F”，封顶之后输入对应的进度节点：如“外立面装饰”】
	
	@Getter @Setter @IFieldAnnotation(remark="巡查人")
	private String patrolPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="巡查日期")
	private Long patrolTimestamp;
	
	@Getter @Setter @IFieldAnnotation(remark="巡查说明")
	private Long patrolInstruction;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="进度明细列表")
	private List<Empj_PjDevProgressForcastDtl> detailList;

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
}

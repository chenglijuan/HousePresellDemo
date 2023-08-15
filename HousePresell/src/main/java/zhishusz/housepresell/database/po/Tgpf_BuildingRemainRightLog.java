package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
 * 对象名称：楼栋每日留存权益计算日志
 * */
@ITypeAnnotation(remark="楼栋每日留存权益计算日志")
public class Tgpf_BuildingRemainRightLog implements Serializable
{
	private static final long serialVersionUID = 8826213776439458590L;
	
	public static final String Compared_Success = "2";
	public static final String Compared_Failed = "3";
	public static final String Uncompared = "1";

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
	
	@Getter @Setter @IFieldAnnotation(remark="所属楼栋")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;

	@Getter @Setter  @IFieldAnnotation(remark="关联楼幢账户-冗余")
	private Tgpj_BuildingAccount buildingAccount;

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢扩展信息-冗余")
	private Empj_BuildingExtendInfo buildingExtendInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="当前形象进度")
	private String currentFigureProgress;
	
	@Getter @Setter @IFieldAnnotation(remark="当前受限比例（%）")
	private Double currentLimitedRatio;

	@Getter @Setter @IFieldAnnotation(remark="节点受限额度（元）")
	private Double nodeLimitedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="总入账金额（元）")
	private Double totalAccountAmount;//Tgpj_BuildingAccount
	
	@Getter	@Setter	@IFieldAnnotation(remark = "记账日期")
	private String billTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="来源业务类型")
	private String srcBusiType;
	
	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}

	public String geteCodeOfProject()
	{
		return eCodeOfProject;
	}

	public void seteCodeOfProject(String eCodeOfProject)
	{
		this.eCodeOfProject = eCodeOfProject;
	}

	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
}

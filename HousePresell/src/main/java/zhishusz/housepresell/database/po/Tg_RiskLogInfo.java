package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：风险日志管理
 */
@ITypeAnnotation(remark="风险日志管理")
public class Tg_RiskLogInfo implements Serializable
{
	private static final long serialVersionUID = -6357848671453949776L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private Sm_CityRegionInfo cityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="区域名称")
	private String theNameOfCityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="项目风险评级等级")
	private String riskRating;
	
	@Getter @Setter @IFieldAnnotation(remark="风险日志")
	private String riskLog;
	
	@Getter @Setter @IFieldAnnotation(remark="日志日期")
	private String logDate;
	
	@Getter @Setter @IFieldAnnotation(remark="项目风险评级单号")
	private String eCodeOfPjRiskRating;
	
	@Getter @Setter @IFieldAnnotation(remark="项目风险评级")
	private Tg_PjRiskRating pjRiskRating;

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

	public String geteCodeOfPjRiskRating()
	{
		return eCodeOfPjRiskRating;
	}

	public void seteCodeOfPjRiskRating(String eCodeOfPjRiskRating)
	{
		this.eCodeOfPjRiskRating = eCodeOfPjRiskRating;
	}
	
}

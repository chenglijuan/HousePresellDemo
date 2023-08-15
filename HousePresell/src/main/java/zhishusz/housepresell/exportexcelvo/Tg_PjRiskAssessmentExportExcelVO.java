package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：项目风险评估
 */
public class Tg_PjRiskAssessmentExportExcelVO
{
	@Getter	@Setter
	private Integer ordinal;// 序号
	
	@Getter	@Setter
	private String eCode;//项目风险评估单号
	
	@Getter	@Setter
	private String theNameOfCityRegion;//所属区域
	
	@Getter	@Setter
	private String theCompanyName;//开发企业名称
	
	@Getter	@Setter
	private String theNameOfProject;//项目名称
	
	@Getter	@Setter
	private String assessDate;//项目风险评估日期
	
	@Getter	@Setter
	private String riskAssessment;//项目风险评估内容
	
	@Getter	@Setter
	private String createUserName;//操作人
	
	@Getter	@Setter
	private String createTimeStamp;//操作时间
	
}

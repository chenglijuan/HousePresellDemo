package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 对象名称：见证报告统计表-导出Excel
 * @author ZS004
 */
public class Tg_WitnessStatisticsExportExcelVO
{
	@Getter @Setter
	private Integer ordinal;//序号
	
	@Getter @Setter
	private String projectName;//项目名称  
	
	@Getter @Setter
	private String projectArea;//项目区域
	
	@Getter @Setter 
	private String constructionNumber;//施工编号
	
	@Getter @Setter
	private String witnessNode;//见证节点
	
	@Getter @Setter
	private String supervisionCompany;//监理公司
	
	@Getter @Setter
	private String witnessAppoinTime;//见证预约时间
	
	@Getter @Setter
	private String reportUploadTime;//报告上传时间
}

package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目巡查预测计划表  接受Bean
 * @author ZS004
 */
public class Tg_ProInspectionSchedule_ViewExportExcelVO
{

	@Getter @Setter
	private Integer ordinal;//序号 
	@Getter @Setter
	private String cityRegion;//区域
	@Getter @Setter
	private String projectName;//项目名称
	@Getter @Setter
	private String upTotalFloorNumber;//地上楼层数（总 ）
	@Getter @Setter
	private String eCodeFromConstruction;//施工楼幢
	@Getter @Setter
	private String currentLimitedNote;//当前受限节点
	@Getter @Setter
	private String currentBuildProgress;//当前建设进度
	@Getter @Setter
	private String progressOfUpdateTime;//进度更新时间
	@Getter @Setter
	private String nextChangeNode;//下一个变更节点
	@Getter @Setter
	private String forecastNextChangeTime;//预测变更时间
	@Getter @Setter
	private String preSalePermits;//预售证
	
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
}

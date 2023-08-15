package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：项目巡查预测计划表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_ProInspectionSchedule_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = 889871338259814885L;

	@Getter @Setter 
	private Long tableId;
	@Getter @Setter 
	private String cityRegion;//区域
	@Getter @Setter
	private String companyName;//企业名称
	@Getter @Setter
	private String projectName;//项目名称
	@Getter @Setter
	private Integer upTotalFloorNumber;//地上楼层数（总 ）
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
	
	
	//页面接收字段
	@Getter @Setter
	private Long projectId;//项目ID
	@Getter @Setter
	private Long cityRegionId;//区域ID
	
	
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	
	
}

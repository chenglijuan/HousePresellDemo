package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：项目巡查预测计划表
 * */
@ITypeAnnotation(remark="项目巡查预测计划表")
public class Tg_ProInspectionSchedule_View implements Serializable
{
	private static final long serialVersionUID = 992525257501317959L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private String cityRegion;//区域
	@Getter @Setter @IFieldAnnotation(remark="企业名称")
	private String companyName;//企业名称
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称
	@Getter @Setter @IFieldAnnotation(remark="地上楼层数（总 ）")
	private Integer upTotalFloorNumber;//地上楼层数（总 ）
	@Getter @Setter @IFieldAnnotation(remark="施工楼幢")
	private String eCodeFromConstruction;//施工楼幢
	@Getter @Setter @IFieldAnnotation(remark="当前受限节点")
	private String currentLimitedNote;//当前受限节点
	@Getter @Setter @IFieldAnnotation(remark="当前建设进度")
	private String currentBuildProgress;//当前建设进度
	@Getter @Setter @IFieldAnnotation(remark="进度更新时间")
	private String progressOfUpdateTime;//进度更新时间
	@Getter @Setter @IFieldAnnotation(remark="下一个变更节点")
	private String nextChangeNode;//下一个变更节点
	@Getter @Setter @IFieldAnnotation(remark="预测变更时间")
	private String forecastNextChangeTime;//预测变更时间
	@Getter @Setter @IFieldAnnotation(remark="预售证")
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

package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管项目详情一览表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_projectDetailOfBrowse_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = 5484606359389302573L;

	@Getter @Setter 
	private Long tableId;
	
	@Getter @Setter 
	private String cityRegion;//区域
	
	@Getter @Setter 
	private String projectName;//项目名称

	@Getter @Setter 
	private String eCodeFromConstruction;//施工楼幢编号
	
	@Getter @Setter 
	private Double forEcastArea;//建筑面积
	
	@Getter @Setter
	private Double escrowArea;//托管面积
	
	@Getter @Setter 
	private Double recordAveragePrice;//物价备案均价
	
	@Getter @Setter 
	private Integer houseTotal;//总户数
	
	@Getter @Setter
	private String produceOfProject;//项目介绍
	
	@Getter @Setter 
	private String queryYear;//查询年份
	
	@Getter @Setter 
	private String queryMonth;//查询月份

	
	//页面接收字段
	@Getter @Setter
	private Long projectId;//项目ID
		
	@Getter @Setter
	private Long cityRegionId;//区域ID
	
	@Getter @Setter
	private String queryDate;//查询时间
	
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
}

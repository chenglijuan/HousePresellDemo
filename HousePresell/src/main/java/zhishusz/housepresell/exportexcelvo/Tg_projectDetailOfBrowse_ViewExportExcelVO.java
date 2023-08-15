package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：托管项目详情一览表 - 导出EXCEL
 * */
@ITypeAnnotation(remark="托管项目详情一览表")
public class Tg_projectDetailOfBrowse_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号 
	
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

}

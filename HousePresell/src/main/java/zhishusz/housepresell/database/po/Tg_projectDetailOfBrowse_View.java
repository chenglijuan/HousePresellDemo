package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：托管项目详情一览表
 * */
@ITypeAnnotation(remark="托管项目详情一览表")
public class Tg_projectDetailOfBrowse_View implements Serializable
{

	private static final long serialVersionUID = 1467396382291623528L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private String cityRegion;//区域
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称

	@Getter @Setter @IFieldAnnotation(remark="施工楼幢编号")
	private String eCodeFromConstruction;//施工楼幢编号
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积")
	private Double forEcastArea;//建筑面积
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积")
	private Double escrowArea;//托管面积
	
	@Getter @Setter @IFieldAnnotation(remark="物价备案均价")
	private Double recordAveragePrice;//物价备案均价
	
	@Getter @Setter @IFieldAnnotation(remark="总户数")
	private Integer houseTotal;//总户数
	
	@Getter @Setter @IFieldAnnotation(remark="项目介绍")
	private String produceOfProject;//项目介绍
	
	@Getter @Setter @IFieldAnnotation(remark="查询年份")
	private String queryYear;//查询年份
	
	@Getter @Setter @IFieldAnnotation(remark="查询年份")
	private String queryMonth;//查询月份
	
	@Getter @Setter @IFieldAnnotation(remark="项目信息")
	private Empj_ProjectInfo projectInfo;

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	
}

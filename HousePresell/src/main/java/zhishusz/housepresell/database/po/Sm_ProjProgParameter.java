package zhishusz.housepresell.database.po;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：测算参数配置
 * */
@ITypeAnnotation(remark="测算参数配置")
public class Sm_ProjProgParameter implements Serializable
{

	//---------公共字段-Start---------//

	/**
     *
     */
    private static final long serialVersionUID = -5142835044541729256L;

    @Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

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
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目编号")
    private String projectCode;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
    private String projectName;
	
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private Sm_CityRegionInfo area;
	
	@Getter @Setter @IFieldAnnotation(remark="区域名称")
    private String areaName;

	@Getter @Setter @IFieldAnnotation(remark="参数一（每层建设天数）")
	private Integer parameterOne;
	
	@Getter @Setter @IFieldAnnotation(remark="参数二（主体结构封顶至外立面装饰完工的跨度天数）")
    private Integer parameterTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="参数三（外立面装饰室内装修跨度天数）")
    private Integer parameterThree;
	
	@Getter @Setter @IFieldAnnotation(remark="是否全局（0-否 1-是）")
    private Integer hasAll;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String  remark;
	
	
}

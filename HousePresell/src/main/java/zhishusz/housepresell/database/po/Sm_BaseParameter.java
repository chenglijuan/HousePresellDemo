package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.database.po.state.S_BaseParameter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：参数定义
 * */
@ITypeAnnotation(remark="参数定义")
public class Sm_BaseParameter implements Serializable
{
	private static final long serialVersionUID = 1462025063210179177L;

	//---------公共字段-Start---------//

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

	@Getter @Setter @IFieldAnnotation(remark="父级参数")
	private Sm_BaseParameter parentParameter;
	
	@Getter @Setter @IFieldAnnotation(remark="参数名")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="参数值")
	private String theValue;

	@Getter @Setter @IFieldAnnotation(remark="有效期-开始时间")
	private Long validDateFrom;
	
	@Getter @Setter @IFieldAnnotation(remark="有效期-结束时间")
	private Long validDateTo;
	
	@Getter @Setter @IFieldAnnotation(remark="版本号")
	private Integer theVersion;
	
	@Getter @Setter @IFieldAnnotation(remark="参数类型 S_BaseParameter")
	private String  parametertype;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String  remark;
	
}

package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：操作日志
 * */
@ITypeAnnotation(remark="日志-关键操作")
public class Sm_Operate_Log implements Serializable
{
	private static final long serialVersionUID = 258783262665833106L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="操作人员")
	private Sm_User userOperate;
	
	@Getter @Setter @IFieldAnnotation(remark="访问来源IP")
	private String remoteAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="操作事项")
	private String operate;
	
	@Getter @Setter @IFieldAnnotation(remark="输入参数")
	private String inputForm;//TODO 注意事项：由开发人员修改数据库字段为clob类型
	
	@Getter @Setter @IFieldAnnotation(remark="操作结果")
	private String result;
	
	@Getter @Setter @IFieldAnnotation(remark="操作结果提示信息")
	private String info;
	
	@Getter @Setter @IFieldAnnotation(remark="返回的Json数据")
	private String returnJson;//TODO 注意事项：由开发人员修改数据库字段为clob类型
	
	@Getter @Setter @IFieldAnnotation(remark="操作开始时间")
	private Long startTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="操作结束时间")
	private Long endTimeStamp;
}

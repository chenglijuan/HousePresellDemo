package zhishusz.housepresell.database.po;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

/*
 * 对象名称：系统用户+机构用户
 * */
@ITypeAnnotation(remark="用户登录退出日志表")
public class Sm_UserSignInLog implements Serializable
{
	private static final long serialVersionUID = 2668207667441815492L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark="用户id")
	private String userId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="类型 1登录 2退出")
	private Integer theType;

	@Getter @Setter @IFieldAnnotation(remark="操作时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="登录账号")
	private String theAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="登录名")
	private String realName;

}

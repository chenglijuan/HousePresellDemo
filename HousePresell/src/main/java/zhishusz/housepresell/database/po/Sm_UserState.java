package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：用户登录状态记录表
 * */
@ITypeAnnotation(remark="用户登录状态记录表")
public class Sm_UserState implements Serializable
{
	private static final long serialVersionUID = -4627142761792946589L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="登录用户")
	private Sm_User loginUser;
	
	@Getter @Setter @IFieldAnnotation(remark="登录用户名")
	private String loginUserName;
	
	@Getter @Setter @IFieldAnnotation(remark="登录sessionId")
	private String loginSessionId;
	
	@Getter @Setter @IFieldAnnotation(remark="登录时间 yyyy-MM-dd hh-mm-ss")
	private String loginDate;
	
}

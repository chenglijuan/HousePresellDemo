package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：用户登录记录
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_UserStateForm extends NormalActionForm
{
	
	private static final long serialVersionUID = -4022620174196086879L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	
	@Getter @Setter @IFieldAnnotation(remark="登录用户")
	private Sm_User loginUser;
	
	@Getter @Setter
	private Long loginUserId;//登录用户Id
	
	@Getter @Setter @IFieldAnnotation(remark="登录用户名")
	private String loginUserName;
	
	@Getter @Setter @IFieldAnnotation(remark="登录sessionId")
	private String loginSessionId;
	
	@Getter @Setter @IFieldAnnotation(remark="登录时间 yyyy-MM-dd hh-mm-ss")
	private String loginDate;
	
	
}

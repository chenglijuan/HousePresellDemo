package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：角色与用户对应关系
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_RoleUserForm extends NormalActionForm
{
	private static final long serialVersionUID = -1420757090780385468L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Sm_Permission_Role sm_Permission_Role;//角色
	@Getter @Setter
	private Long sm_Permission_RoleId;//角色-Id
	@Getter @Setter
	private Sm_User sm_User;//用户
	@Getter @Setter
	private Long sm_UserId;//用户-Id
	@Getter @Setter 
	private Long leEnableTimeStamp;//启用时间
	@Getter @Setter
	private Long gtDownTimeStamp;//停用时间
	@Getter @Setter
	private Emmp_CompanyInfo emmp_companyInfo;
}

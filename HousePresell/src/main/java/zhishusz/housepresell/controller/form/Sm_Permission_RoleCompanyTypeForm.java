package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_Permission_Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：角色与机构类型对应关系
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_RoleCompanyTypeForm extends NormalActionForm
{
	private static final long serialVersionUID = -4610534240647080733L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Sm_Permission_Role sm_Permission_Role;//角色
	@Getter @Setter
	private Long sm_Permission_RoleId;//角色-Id
	@Getter @Setter
	private String forCompanyType;//适用机构类型
}

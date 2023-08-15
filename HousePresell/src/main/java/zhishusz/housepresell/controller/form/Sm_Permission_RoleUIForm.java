package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_RoleUIForm extends NormalActionForm
{
	private static final long serialVersionUID = -1659005083291557428L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Sm_Permission_Role sm_Permission_Role;//角色
	@Getter @Setter
	private Long sm_Permission_RoleId;//角色-Id
	@Getter @Setter
	private Sm_Permission_UIResource sm_Permission_UIResource;//UI资源
	@Getter @Setter
	private Long sm_Permission_UIResourceId;//UI资源-Id
	@Getter @Setter
	private String busiType;//业务状态：（1:启用 ，0：停用） S_ValidState
	@Getter @Setter
	private String enableTimeStampRange;//启用时间范围
	@Getter @Setter
	private Long enableTimeStampStart;
	@Getter @Setter
	private Long enableTimeStampEnd;
	@Getter @Setter
	private Long[] sm_Permission_UIResourceIdArr;
	@Getter @Setter
	private Long[] btnCheckArr;//用户勾选的菜单Id数组
	@Getter @Setter
	private Long[] menuCheckArr;//用户勾选的功能按钮Id数组
}

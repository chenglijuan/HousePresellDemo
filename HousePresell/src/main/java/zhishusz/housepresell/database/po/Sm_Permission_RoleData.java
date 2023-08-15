package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：角色与数据权限对应关系
 * TODO：角色与数据权限对应关系
 */
@ITypeAnnotation(remark="角色与数据权限对应关系")
public class Sm_Permission_RoleData implements Serializable
{
	private static final long serialVersionUID = 2179901074565979093L;
    
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@Getter @Setter @IFieldAnnotation(remark="角色")
    private Sm_Permission_Role sm_Permission_Role;

	private Sm_Permission_UIResource Sm_Permission_UIResource;//TODO
}

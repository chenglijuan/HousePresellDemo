package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：角色与用户对应关系
 */
@ITypeAnnotation(remark="角色与用户对应关系")
public class Sm_Permission_RoleUser implements Serializable
{
	private static final long serialVersionUID = 6502322100414115265L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@Getter @Setter @IFieldAnnotation(remark="角色")
	private Sm_Permission_Role sm_Permission_Role;
	
	@Getter @Setter @IFieldAnnotation(remark="用户")
	private Sm_User	sm_User;

	@Getter @Setter @IFieldAnnotation(remark="机构")
	private Emmp_CompanyInfo emmp_companyInfo;

}

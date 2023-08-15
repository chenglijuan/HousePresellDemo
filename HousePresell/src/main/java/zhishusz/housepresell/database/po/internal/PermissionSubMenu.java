package zhishusz.housepresell.database.po.internal;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：权限-子菜单
 * 业务逻辑：分配权限后，将用户权限保存为一个json文件；
 *        用户登录后，获取该json文件，在前端页面加载此数据，用于决定有权限控制的菜单的显示与隐藏
 * */
public class PermissionSubMenu extends PermissionUI
{
	private static final long serialVersionUID = -1892071964937579044L;

	@Getter @Setter @IFieldAnnotation(remark="父级菜单")
	private PermissionFatherMenu fatherMenu;
}

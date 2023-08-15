package zhishusz.housepresell.database.po.internal;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：权限-UI控件（菜单+按钮）
 * 业务逻辑：分配权限后，将用户权限保存为一个json文件；
 *        用户登录后，获取该json文件，在前端页面加载此数据，用于决定有权限控制的UI控件的显示与隐藏
 * */
public class PermissionUI implements Serializable,Comparable<PermissionUI>
{
	private static final long serialVersionUID = -1892071964937579044L;

	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="资源")
	private String theSource;

	@Getter @Setter @IFieldAnnotation(remark="排序")
	private Integer orderNumber;

	@Override
	public int compareTo(PermissionUI permissionUI)
	{
		return this.orderNumber - permissionUI.orderNumber;
	}
}

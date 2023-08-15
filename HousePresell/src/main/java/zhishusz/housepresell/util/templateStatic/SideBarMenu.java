package zhishusz.housepresell.util.templateStatic;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//初始化权限资源模板模型
@ToString
public class SideBarMenu implements Serializable
{
	private static final long serialVersionUID = 499181461991828279L;
	
	@Getter @Setter @IFieldAnnotation(remark="id")
	private String levelNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="pid")
	private String parentLevelNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="图标")
	private String iconPath;
	
	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="链接")
	private String theResource;

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;
	
	@Getter @Setter @IFieldAnnotation(remark="子集")
	private List<SideBarMenu> childrenUIList;
}

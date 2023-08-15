package zhishusz.housepresell.initialize;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//初始化权限资源模板模型
@ToString
public class Sm_Permission_UIResourceTemplate implements Serializable
{
	private static final long serialVersionUID = 3833887455803177179L;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;//业务编码参看“功能菜单-业务编码.xlsx”
	
	@Getter @Setter @IFieldAnnotation(remark="UI权限名称，用于显示")
	private String	theName;

	//系统初始化权限的关键字，用户编辑，修改，仅操作theName字段，theOriginalName字段为最初默认值（禁止修改）。
	@Getter @Setter @IFieldAnnotation(remark="UI权限原始名称，用于初始化")
	private String	theOriginalName;
	
	@Getter @Setter @IFieldAnnotation(remark="自身的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
	private String	levelNumber;

	@Getter @Setter @IFieldAnnotation(remark="父级的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
	private String	parentLevelNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="排序")
	private Double	theIndex;
	
	@Getter @Setter @IFieldAnnotation(remark="资源（URL、）")
	private String	theResource;
	
	@Getter @Setter @IFieldAnnotation(remark="类型：上层菜单、底层菜单、按钮 S_UIResourceType")
	private String	theType;
	
	@Getter @Setter @IFieldAnnotation(remark="是否是默认权限 S_YesNo")
	private Integer	isDefault;
	
	@Getter @Setter @IFieldAnnotation(remark="备注说明")
	private String	remark;
	
	@Getter @Setter @IFieldAnnotation(remark="按钮编号（系统全局唯一，事先定义所有页面的每一个可以控制的按钮编号）")
	private String editNum;
	
	@Getter @Setter @IFieldAnnotation(remark="父级UI资源")
	private Sm_Permission_UIResourceTemplate parentUI;
	
	@Getter @Setter @IFieldAnnotation(remark="子级UI资源")
	private List<Sm_Permission_UIResourceTemplate> childrenUIList;
	 
	public String geteCode() {
		 return eCode;
	}
	
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

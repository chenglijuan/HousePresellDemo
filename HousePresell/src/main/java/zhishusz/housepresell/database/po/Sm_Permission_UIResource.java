package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：UI权限资源=多级菜单权限+按钮权限
 * （权限核心信息）
 */
@ITypeAnnotation(remark="UI权限资源")
public class Sm_Permission_UIResource implements Serializable
{
	private static final long serialVersionUID = -4632732676720505112L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	
	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;//业务编码，参看“功能菜单-业务编码.xlsx”
	
	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="UI权限名称，用于显示")
	private String	theName;

	//系统初始化权限的关键字，用户编辑，修改，仅操作theName字段，theOriginalName字段为最初默认值（禁止修改）。
	@Getter @Setter @IFieldAnnotation(remark="UI权限原始名称，用于初始化，用户自行手动添加的，该参数设置为NULL")
	private String	theOriginalName;
	
	@Getter @Setter @IFieldAnnotation(remark="自身的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
	private String	levelNumber;

	@Getter @Setter @IFieldAnnotation(remark="父级的层级编码，最高一级编码为：0，次一级为1_1，再次一级为1_1_1,")
	private String	parentLevelNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="排序")
	private Double	theIndex;
	
	@Getter @Setter @IFieldAnnotation(remark="资源（URL、）只有theType为链接资源时，有值")
	private String	theResource;

	@Getter @Setter @IFieldAnnotation(remark="管理链接资源对象，只有theType为菜单时，有值")
	private Sm_Permission_UIResource resourceUI;
	
	@Getter @Setter @IFieldAnnotation(remark="类型：虚拟菜单、实体菜单、按钮、链接 S_UIResourceType")
	private Integer	theType;
	
	@Getter @Setter @IFieldAnnotation(remark="是否是默认权限 S_YesNo")
	private Integer	isDefault;
	
	@Getter @Setter @IFieldAnnotation(remark="备注说明")
	private String	remark;
	
	@Getter @Setter @IFieldAnnotation(remark="图标路径")
	private String	iconPath;
	
	@Getter @Setter @IFieldAnnotation(remark="按钮编号（系统全局唯一，事先定义所有页面的每一个可以控制的按钮编号）")
	private String	editNum;

	@Getter @Setter @IFieldAnnotation(remark="父级UI资源")
	private Sm_Permission_UIResource parentUI;
	
	@Getter @Setter @IFieldAnnotation(remark="子级UI资源")
	private List<Sm_Permission_UIResource> childrenUIList;

	@Getter @Setter @IFieldAnnotation(remark="列表访问路径")
	private String	controllerurl;


	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

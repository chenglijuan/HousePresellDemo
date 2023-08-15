package zhishusz.housepresell.controller.form;

import java.util.List;

import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：UI权限资源
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_UIResourceForm extends NormalActionForm
{
	private static final long serialVersionUID = 6195765029542613227L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Long exceptTableId;
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String busiCode;//业务编码
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private String theName;//UI权限名称，用于显示
	@Getter @Setter
	private String	theOriginalName;
	@Getter @Setter @IFieldAnnotation(remark="自身的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
	private String	levelNumber;
	@Getter @Setter @IFieldAnnotation(remark="父级的层级编码，最高一级编码为：1，次一级为1_1，再次一级为1_1_1,")
	private String	parentLevelNumber;
	@Getter @Setter
	private Integer theIndex;//排序
	@Getter @Setter
	private String theResource;//资源（URL、）
	@Getter @Setter
	private Long resourceUIId;//资源（URL）Id
	@Getter @Setter
	private Integer theType;//类型：实体菜单、虚拟菜单、按钮、链接 S_UIResourceType
	@Getter @Setter
	private Integer isDefault;//是否是默认权限
	@Getter @Setter
	private String remark;//备注说明
	@Getter @Setter
	private Sm_Permission_UIResource parentUI;//父级UI资源
	@Getter @Setter
	private Long parentUIId;//父级UI资源-Id
	@Getter @Setter
	private List childrenUIList;//子级UI资源
	@Getter @Setter
	private Long[] buttonIdArr;
	@Getter @Setter
	private String iconPath;
	@Getter @Setter
	private String theIndexStr;
	@Getter @Setter
	private Integer controllerType;
	@Getter @Setter
	private Long parentUIIdForNowMenu;//用户选中的菜单Id，目前用于“添加下级菜单”
	@Getter @Setter
	private Long[] inIdArr;//用户选中的菜单Id，目前用于“添加下级菜单”
	@Getter @Setter
	private String inIdArrStr;//用户选中的菜单Id，目前用于“添加下级菜单”
	@Getter @Setter
	private Boolean userStartNeedNull;//创建人需要为NULL的
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}

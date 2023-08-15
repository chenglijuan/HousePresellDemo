package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：管理角色
 */
@ITypeAnnotation(remark="管理角色")
public class Sm_Permission_Role implements Serializable
{  
	private static final long serialVersionUID = -4086436458579091203L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@IFieldAnnotation(remark="编码")
	private String	eCode;

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
	
	@Getter @Setter @IFieldAnnotation(remark="角色名称")
	private String	theName;
	
	@Getter @Setter @IFieldAnnotation(remark="备注说明")
	private String	remark;
	
	@Getter @Setter @IFieldAnnotation(remark="状态：0正常、1删除 S_TheState")
	private Integer	theState;
	
	@Getter @Setter @IFieldAnnotation(remark="UI权限JSON数据-冗余")
	private String uiPermissionJson;

	@Getter @Setter @IFieldAnnotation(remark="角色对应的UI资源")
	private List<Sm_Permission_UIResource> uiResourceList;

	@Getter @Setter @IFieldAnnotation(remark="是否启用：（1:否 ，0：是）S_RoleBusiType ")
	private String busiType;

	@Getter @Setter @IFieldAnnotation(remark="启用时间")
	private Long enableTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="停用时间")
	private Long downTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="机构类型")
	private String companyType;
	
	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

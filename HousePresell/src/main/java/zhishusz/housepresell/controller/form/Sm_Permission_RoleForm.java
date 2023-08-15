package zhishusz.housepresell.controller.form;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：管理角色
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_RoleForm extends NormalActionForm
{
	private static final long serialVersionUID = 2048263299535120921L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Long exceptTableId;//表ID
	private String eCode;//编码
	@Getter @Setter
	private String theName;//角色名称
	@Getter @Setter
	private String remark;//备注说明
	@Getter @Setter
	private Integer theState;//状态：0正常、1删除
	@Getter @Setter
	private String uiPermissionJson;//UI权限JSON数据-冗余
	@Getter @Setter
	private String busiType; //是否启用：（1:启用 ，0：停用）
	@Getter @Setter
	private Long enableTimeStamp;//启用时间
	@Getter @Setter
	private Long downTimeStamp;//停用时间
	@Getter @Setter
	private String enableDateStr;//启用时间插件字符串
	@Getter @Setter
	private String downDateStr;//停用时间插件字符串
	@Getter @Setter
	private String companyType;//机构类型
	@Getter @Setter
	private String enableDateSearchStr;//启用时间段字符串
	@Getter @Setter
	private Long chooseUserId;//选择的用户ID
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

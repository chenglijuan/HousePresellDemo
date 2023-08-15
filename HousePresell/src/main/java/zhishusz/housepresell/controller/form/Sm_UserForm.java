package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_Department;
import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_UserForm extends NormalActionForm
{
	private static final long serialVersionUID = -7318496244449375776L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态：起用、停用 S_ValidState
	@Getter @Setter
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
	private Long lockUntil;//登录失败N次锁定M小时
	@Getter @Setter
	private Emmp_CompanyInfo company;//所属机构组织
	@Getter @Setter
	private Long companyId;//所属机构组织-Id
	@Getter @Setter
	private String eCodeOfCompany;//企业编号
	@Getter @Setter
	private String theNameOfCompany;//企业名称
	@Getter @Setter
	private Emmp_Department department;//所属部门
	@Getter @Setter
	private Long departmentId;//所属部门-Id
	@Getter @Setter
	private String eCodeOfDepartment;//所属部门编号
	@Getter @Setter
	private String theNameOfDepartment;//所属部门名称
	@Getter @Setter
	private Integer theType;//类型
	@Getter @Setter
	private String theName;//用户名
	@Getter @Setter
	private String theAccount;//用户名
	@Getter @Setter
	private String realName;//真实姓名
	@Getter @Setter
	private String idType;//证件类型 S_IDType
	@Getter @Setter
	private String idNumber;//证件号码
	@Getter @Setter
	private String phoneNumber;//手机号码
	@Getter @Setter
	private String email;//邮箱
	@Getter @Setter
	private String weixin;//微信号
	@Getter @Setter
	private String qq;//QQ
	@Getter @Setter
	private String address;//地址
	@Getter @Setter
	private String loginPassword;//登录密码
	@Getter @Setter
	private Integer errPwdCount;//密码错误次数（登录成功后清零）
	@Getter @Setter
	private String heardImagePath;//头像路径
	@Getter @Setter
	private Long lastLoginTimeStamp;//最后一次登录时间
	@Getter @Setter
	private Integer loginMode;//登录方式
	@Getter @Setter
	private String ukeyNumber;//UKey序列号
	@Getter @Setter
	private Boolean hasQC;//是否有资质证书
	@Getter @Setter
	private Emmp_QualificationInfo qualificationInfo;//证书信息
	@Getter @Setter
	private Long qualificationInfoId;//证书信息-Id
	@Getter @Setter
	private String menuPermissionHtmlPath;//菜单权限
	@Getter @Setter
	private String buttonPermissionJsonPath;//按钮权限
	@Getter @Setter
	private String dataPermssion;//数据权限
	@Getter @Setter
	private Integer isEncrypt;//是否加密
	@Getter @Setter
	private String endTimeStampStr;//停用日期
	@Getter @Setter
	private Long[] sm_Permission_RoleIdArr;//角色IdArr
	@Getter @Setter
	private String oldPwd;//旧密码
	@Getter @Setter
	private String newPwd;//新密码
	@Getter @Setter
	private String cNewPwd;//新密码
	@Getter @Setter
	private Long permission_RoleId;//角色Id
	@Getter @Setter
	private Integer applyState;//锁定状态
	@Getter @Setter
	private String effectiveDateStr;//有效时间段
	@Getter @Setter
	private String requestTime;//请求次数
	@Getter @Setter
	private String smAttachmentList;//请求次数
	@Getter @Setter
	private String isCommit;//是否确认登录（0：否 1：是）
	@Getter @Setter
	private String isSignature;//是否签章（0：否 1：是）
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfCompany() {
		return eCodeOfCompany;
	}
	public void seteCodeOfCompany(String eCodeOfCompany) {
		this.eCodeOfCompany = eCodeOfCompany;
	}
	public String geteCodeOfDepartment() {
		return eCodeOfDepartment;
	}
	public void seteCodeOfDepartment(String eCodeOfDepartment) {
		this.eCodeOfDepartment = eCodeOfDepartment;
	}
	
	
}

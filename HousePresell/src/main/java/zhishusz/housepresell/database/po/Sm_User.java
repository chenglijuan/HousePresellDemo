package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：系统用户+机构用户
 * */
@ITypeAnnotation(remark="系统用户+机构用户")
public class Sm_User implements Serializable
{
	private static final long serialVersionUID = 2668207667441815470L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态：启用、停用 S_ValidState")
	private String busiState;//锁定状态是能过lockUntil参数实时计算的
	
	@Getter @Setter @IFieldAnnotation(remark="是否加密")
	private Integer isEncrypt;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

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
	
	@Getter @Setter @IFieldAnnotation(remark="登录失败N次锁定M小时")
	private Long lockUntil;//锁定状态是通过lockUntil参数实时计算的

	@Getter @Setter @IFieldAnnotation(remark="所属机构组织")
	private Emmp_CompanyInfo company;

	@IFieldAnnotation(remark="企业编号")
	private String eCodeOfCompany;

	@Getter @Setter @IFieldAnnotation(remark="企业名称")
	private String theNameOfCompany;

	@Getter @Setter @IFieldAnnotation(remark="所属部门")
	private Emmp_Department department;
	
	@IFieldAnnotation(remark="所属部门编号")
	private String eCodeOfDepartment;
	
	@Getter @Setter @IFieldAnnotation(remark="所属部门名称")
	private String theNameOfDepartment;
	
	@Getter @Setter @IFieldAnnotation(remark="类型 S_UserType")
	private Integer theType;//正泰、机构 
	
	@Getter @Setter @IFieldAnnotation(remark="用户名")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="真实姓名")
	private String realName;

	@Getter @Setter @IFieldAnnotation(remark="证件类型 S_IDType")
	private String idType;

	@Getter @Setter @IFieldAnnotation(remark="证件号码")
	private String idNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="手机号码")
	private String phoneNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="邮箱")
	private String email;
	
	@Getter @Setter @IFieldAnnotation(remark="微信号")
	private String weixin;
	
	@Getter @Setter @IFieldAnnotation(remark="QQ")
	private String qq;
	
	@Getter @Setter @IFieldAnnotation(remark="地址")
	private String address;
	
	@Getter @Setter @IFieldAnnotation(remark="登录密码")
	private String loginPassword;

	@Getter @Setter @IFieldAnnotation(remark="密码错误次数（登录成功后清零）")
	private Integer errPwdCount;
	
	@Getter @Setter @IFieldAnnotation(remark="头像路径")
	private String heardImagePath;
	
	@Getter @Setter @IFieldAnnotation(remark="最后一次登录时间")
	private Long lastLoginTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="登录方式")
	private Integer loginMode;//登录方式(只能选一种)：不能登录、用户名+密码+图片验证码、用户名+密码+UKey、用户名+短信验证码

	@Getter @Setter @IFieldAnnotation(remark="UKey序列号")
	private String ukeyNumber;

	@Getter @Setter @IFieldAnnotation(remark="是否有资质证书")
	private Boolean hasQC;

	@Getter @Setter @IFieldAnnotation(remark="证书信息")
	private Emmp_QualificationInfo qualificationInfo;//证书信息，只会有一个证书处于有效状态

	@Getter @Setter @IFieldAnnotation(remark="菜单权限")
	private String menuPermissionHtmlPath;
	
	@Getter @Setter @IFieldAnnotation(remark="按钮权限")
	private String buttonPermissionJsonPath;
	
	@Getter @Setter @IFieldAnnotation(remark="数据权限")
	private String dataPermssion;
	
	@Getter @Setter @IFieldAnnotation(remark="生效开始时间")
	private Long startTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="生效结束时间")
	private Long endTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="账户名")
	private String theAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="登录状态 0：未登录 1：已登录")
	private String loginState;
	
	@Getter @Setter @IFieldAnnotation(remark="登录sessionId")
	private String loginSessionId;
	
	@Getter @Setter @IFieldAnnotation(remark="是否具有签章 0：否 1：是")
	private String isSignature;
	@Getter @Setter @IFieldAnnotation(remark="密码有效时间")
	private Long pwdExpireTimeStamp;
	
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

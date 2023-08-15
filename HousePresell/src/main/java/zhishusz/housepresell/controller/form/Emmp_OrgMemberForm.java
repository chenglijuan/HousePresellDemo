package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：机构成员
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Emmp_OrgMemberForm extends NormalActionForm
{
	private static final long serialVersionUID = 5344498973851648826L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
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
	private Emmp_CompanyInfo company;//所属机构组织
	@Getter @Setter
	private Long companyId;//所属机构组织-Id
	@Getter @Setter
	private Emmp_BankInfo bank;//所属机构组织
	@Getter @Setter
	private Long bankId;//所属机构组织-Id
	@Getter @Setter
	private String eCodeOfDepartment;//所属部门编号
	@Getter @Setter
	private String theNameOfDepartment;//所属部门名称
	@Getter @Setter
	private String theType;//类型
	@Getter @Setter
	private String theName;//用户名
	@Getter @Setter
	private String realName;//真实姓名
	@Getter @Setter
	private String idType;//证件类型 S_IDType
	@Getter @Setter
	private String idNumber;//证件号码
	@Getter @Setter
	private Sm_BaseParameter parameter;//基础公用字段 职务
	@Getter @Setter 
	private Long parameterId;//基础公用字段 tableId
	@Getter @Setter 
	private String positionName;//职称
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
	private String heardImagePath;//头像路径
	@Getter @Setter
	private Boolean hasQC;//是否有资质证书
	@Getter @Setter
	private Emmp_QualificationInfo qualificationInformation;//证书信息
	@Getter @Setter
	private Long qualificationInformationId;//证书信息-Id
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Long logId;//关联日志Id
	@Getter @Setter
	private String changeState;

	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeOfDepartment()
	{
		return eCodeOfDepartment;
	}
	public void seteCodeOfDepartment(String eCodeOfDepartment)
	{
		this.eCodeOfDepartment = eCodeOfDepartment;
	}
}

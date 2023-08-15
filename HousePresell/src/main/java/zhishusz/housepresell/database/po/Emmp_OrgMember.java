package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：机构成员
 * */
@ITypeAnnotation(remark="机构成员")
public class Emmp_OrgMember implements Serializable,ILogable,IApprovable
{
	private static final long serialVersionUID = 2668207667441815470L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
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

	@Getter @Setter @IFieldAnnotation(remark="所属机构组织")
	private Emmp_CompanyInfo company;

	@Getter @Setter @IFieldAnnotation(remark="所属金融机构")
	private Emmp_BankInfo bank;

	@IFieldAnnotation(remark="所属部门编号")
	private String eCodeOfDepartment;
	
	@Getter @Setter @IFieldAnnotation(remark="所属部门名称")
	private String theNameOfDepartment;
	
	@Getter @Setter @IFieldAnnotation(remark="类型")
	private String theType;//正泰、机构
	
	@Getter @Setter @IFieldAnnotation(remark="用户名")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="真实姓名")
	private String realName;
	
	@Getter @Setter @IFieldAnnotation(remark="基础公用字段 职务")
	private Sm_BaseParameter parameter;
	
	@Getter @Setter @IFieldAnnotation(remark="职称")
	private String positionName;

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
	
	@Getter @Setter @IFieldAnnotation(remark="头像路径")
	private String heardImagePath;

	@Getter @Setter @IFieldAnnotation(remark="是否有资质证书")
	private Boolean hasQC;
	
	@Getter @Setter @IFieldAnnotation(remark="证书信息")
	private Emmp_QualificationInfo qualificationInformation;//证书信息，只会有一个证书处于有效状态
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@IFieldAnnotation(remark="关联日志Id")
	private Long logId;
	@Override
	public Long getLogId()
	{
		return logId;
	}
	@Override
	public void setLogId(Long logId)
	{
		this.logId = logId;
	}
	@Override
	public String getLogData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceType() {
		return null;
	}

	@Override
	public Long getSourceId() {
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfDepartment() {
		return eCodeOfDepartment;
	}
	public void seteCodeOfDepartment(String eCodeOfDepartment) {
		this.eCodeOfDepartment = eCodeOfDepartment;
	}
}

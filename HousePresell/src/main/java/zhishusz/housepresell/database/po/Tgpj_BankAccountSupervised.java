package zhishusz.housepresell.database.po;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：监管账户
 * */
@ITypeAnnotation(remark="监管账户")
public class Tgpj_BankAccountSupervised implements Serializable,ILogable,IApprovable
{
	private static final long serialVersionUID = 4194480494103538004L;
	
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

	@Getter @Setter @IFieldAnnotation(remark="最后修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="关联企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="企业编号")
	private String eCodeOfDevelopCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="所属银行")
	private Emmp_BankInfo bank;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行名称/银行名称")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行简称")
	private String shortNameOfBank;

	@Getter @Setter @IFieldAnnotation(remark="所属支行")
	private Emmp_BankBranch bankBranch;
	
	@Getter @Setter @IFieldAnnotation(remark="账号名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="账号")
	private String theAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="联系人-姓名")
	private String contactPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人-手机号")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Integer isUsing;//0：启用，1：不启用

//	@Getter @Setter @IFieldAnnotation(remark = "监管账户监管的楼幢")
//	private List<Empj_BuildingInfo> buildingInfoList;

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

	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}

	@Override
	public Long getLogId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogId(Long logId)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLogData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List getNeedFieldList(){
		return Arrays.asList("theName");
	}

}

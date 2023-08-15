package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：特殊拨付-申请主表
 * */
@ITypeAnnotation(remark="特殊拨付-申请主表")
public class Tgpf_SpecialFundAppropriated_AF implements Serializable,IApprovable
{
	private static final long serialVersionUID = -659347541295569032L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态 申请单状态 S_SpecialFundBusiState")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
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
	
	@Getter @Setter @IFieldAnnotation(remark="审批状态 S_ApprovalState")
	private String approvalState;
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;

	@Getter @Setter @IFieldAnnotation(remark="开发企业名称 - 冗余")
	private String theNameOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@Getter @Setter @IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢账户log表")
	private Tgpj_BuildingAccountLog buildingAccountLog;
	
	@Getter @Setter @IFieldAnnotation(remark="关联监管账户")
	private Tgpj_BankAccountSupervised bankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="监管账号")
	private String theAccountOfBankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="监管账号名称")
	private String theNameOfBankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="监管账户开户行")
	private String theBankOfBankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="拨付类型")
	private String appropriatedType;
	
	@Getter @Setter @IFieldAnnotation(remark="拨付说明")
	private String appropriatedRemark;
	
	@Getter @Setter @IFieldAnnotation(remark="申请金额")
	private Double totalApplyAmount;

	@Getter @Setter @IFieldAnnotation(remark="申请日期")
	private String applyDate;

	@Getter @Setter @IFieldAnnotation(remark="拨付状态 S_SpecialFundApplyState")
	private Integer applyState;

	@Getter @Setter @IFieldAnnotation(remark="拨付类型 1-对公，0-对私")
	private String accType;

	@Getter @Setter @IFieldAnnotation(remark="拨付日期")
	private String afPayoutDate;

	@Override
	public String getSourceType() {
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String>  peddingApprovalkey = new ArrayList<>();
		peddingApprovalkey.add("busiState");
		peddingApprovalkey.add("eCode");
		//TODO  存放需要审批的字段
		return peddingApprovalkey;
	}

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

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	
	
}

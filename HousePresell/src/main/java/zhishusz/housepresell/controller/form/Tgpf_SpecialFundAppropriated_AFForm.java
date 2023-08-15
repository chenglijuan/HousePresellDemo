package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：特殊拨付-申请主表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_SpecialFundAppropriated_AFForm extends NormalActionForm
{
	private static final long serialVersionUID = -6461421813541116029L;
	
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
	private String approvalState;//审批状态
	@Getter @Setter
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String theNameOfDevelopCompany;//开发企业名称 - 冗余
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private Tgpj_BuildingAccountLog buildingAccountLog;//关联楼幢账户log表
	@Getter @Setter
	private Long buildingAccountLogId;//关联楼幢账户log表-Id
	@Getter @Setter
	private Tgpj_BankAccountSupervised bankAccount;//关联监管账户
	@Getter @Setter
	private Long bankAccountId;//关联监管账户-Id
	@Getter @Setter
	private String theAccountOfBankAccount;//监管账号
	@Getter @Setter
	private String theNameOfBankAccount;//监管账号名称
	@Getter @Setter
	private String theBankOfBankAccount;//监管账户开户行
	@Getter @Setter
	private String appropriatedType;//拨付类型
	@Getter @Setter
	private String appropriatedRemark;//拨付说明
	@Getter @Setter
	private Double totalApplyAmount;//申请金额
	@Getter @Setter
	private String applyDate;//申请日期
	@Getter @Setter
	private Integer applyState;//申请状态
	@Getter @Setter
	private String smAttachmentList;//附件信息
	@Getter @Setter
	private Long currentWworkflowId;//审批节点信息
	@Getter @Setter
	private String accType;//拨付类型 1-对公，0-对私
	@Getter @Setter
	private String tableids;//多个记录的id



	@Getter
	@Setter
	@IFieldAnnotation(remark = "申请日期")
	private String timeStampStart;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "申请日期")
	private String timeStampEnd;
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
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

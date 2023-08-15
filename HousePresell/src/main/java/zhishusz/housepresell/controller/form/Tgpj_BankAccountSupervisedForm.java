package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：监管账户
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpj_BankAccountSupervisedForm extends NormalActionForm
{
	private static final long serialVersionUID = -4522842705549610596L;
	
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
	private Sm_User userUpdate;//最后修改人人
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
	private Emmp_CompanyInfo developCompany;//关联企业
	@Getter @Setter
	private Long developCompanyId;//关联企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//企业编号
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private Emmp_BankInfo bank;//所属银行
	@Getter @Setter
	private Long bankId;//所属银行-Id
	@Getter @Setter
	private String theNameOfBank;//开户行名称/银行名称
	@Getter @Setter
	private String shortNameOfBank;//银行简称
	@Getter @Setter
	private Emmp_BankBranch bankBranch;//所属支行
	@Getter @Setter
	private Long bankBranchId;//所属支行-Id
	@Getter @Setter
	private String theName;//账号名称
	@Getter @Setter
	private String theAccount;//账号
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String contactPerson;//联系人-姓名
	@Getter @Setter
	private String contactPhone;//联系人-手机号
	@Getter @Setter
	private String externalCode;
	@Getter @Setter
	private String externalId;
	@Getter @Setter
	private String resourceNote;
	@Getter @Setter
	private Long buildingId;
	@Getter @Setter
	private Integer isUsing;//是否启用

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


}

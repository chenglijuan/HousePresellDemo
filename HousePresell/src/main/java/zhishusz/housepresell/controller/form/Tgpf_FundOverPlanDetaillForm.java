package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：用款申请汇总信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundOverPlanDetaillForm extends NormalActionForm
{
	private static final long serialVersionUID = -5079939054983196370L;
	
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

	private Tgpf_FundAppropriated_AF mainTable;//用款主表
	@Getter @Setter
	private Long mainTableId;//用款主表-Id
//	@Getter @Setter
//	private Tgpj_BankAccountSupervised bankAccountSupervised;//关联监管账号
	@Getter @Setter
	private Long bankAccountSupervisedId;//关联监管账号-Id
	@Getter @Setter
	private String supervisedBankAccount;//监管账号
	@Getter @Setter
	private String theNameOfAccount;//监管账号名称
	@Getter @Setter
	private String theNameOfBankBranch;//开户行名称
	@Getter @Setter
	private Empj_ProjectInfo projectInfo;//关联项目
	@Getter @Setter
	private String projectInfoId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称
	@Getter @Setter
	private Double appliedAmount;//本次划款申请金额（元）
	private Tgpf_FundOverallPlan fundOverallPlan;//关联资金统筹
	@Getter @Setter
	private String fundOverallPlanId;//关联资金统筹-Id

	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

}

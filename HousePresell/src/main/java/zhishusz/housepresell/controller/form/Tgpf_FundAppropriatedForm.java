package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：资金拨付
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundAppropriatedForm extends NormalActionForm
{
	private static final long serialVersionUID = 6618203279962804031L;
	
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
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private Tgpf_FundOverallPlan fundOverallPlan;//资金统筹
	@Getter @Setter
	private Long fundOverallPlanId;//资金统筹-Id
	@Getter @Setter
	private Tgpf_FundAppropriated_AF fundAppropriated_AF;//用款申请
	@Getter @Setter
	private Long fundAppropriated_AFId;//用款申请-Id
	@Getter @Setter
	private Double overallPlanPayoutAmount;//统筹拨付金额
	@Getter @Setter
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;//托管账户
	@Getter @Setter
	private Long bankAccountEscrowedId;//托管账户-Id
	@Getter @Setter
	private Tgpj_BankAccountSupervised bankAccountSupervised;//监管账户
	@Getter @Setter
	private Long bankAccountSupervisedId;//监管账户-Id
	@Getter @Setter
	private String actualPayoutDate;//实际拨款日期
	@Getter @Setter
	private String eCodeFromPayoutBill;//打款单号
	@Getter @Setter
	private Double currentPayoutAmount;//本次实际拨付金额
	@Getter @Setter
	private Double canPayAmount;//托管账户可拨付金额
	@Getter @Setter
	private Tgpf_FundAppropriatedForm[] tgpf_FundAppropriatedList;//拨付计划
	@Getter @Setter
	private String  fundOverallPlanDate; //统筹日期
	@Getter @Setter
	private String  remark; //备注
	@Getter @Setter
	private Integer colorState; //颜色状态
	
	@Getter @Setter
	private List<Tgpf_FundAppropriated> fundAppropriatedList;//拨付计划
	
	@Getter @Setter
	private Long cityRegionId;

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
	public String geteCodeFromPayoutBill() {
		return eCodeFromPayoutBill;
	}
	public void seteCodeFromPayoutBill(String eCodeFromPayoutBill) {
		this.eCodeFromPayoutBill = eCodeFromPayoutBill;
	}
}

package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_OverallPlanAccoutForm extends NormalActionForm
{
	private static final long serialVersionUID = -7140102613157256978L;
	
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
	private Tgpf_FundOverallPlan fundOverallPlan;//统筹单
	@Getter @Setter
	private Long fundOverallPlanId;//统筹单-Id
	@Getter @Setter
	private String eCodeOfFundOverallPlan;//统筹单号-冗余
	@Getter @Setter
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;//关联托管账号
	@Getter @Setter
	private Long bankAccountEscrowedId;//关联托管账号-Id
	@Getter @Setter
	private Double overallPlanAmount;//统筹拨付金额
	@Getter @Setter
	private Tgpf_OverallPlanAccoutForm[] tgpf_OverallPlanAccount;//统筹-账户状况信息
	@Getter @Setter
	private Double accountAmountTrim;//当天入账金额调整项
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfFundOverallPlan() {
		return eCodeOfFundOverallPlan;
	}
	public void seteCodeOfFundOverallPlan(String eCodeOfFundOverallPlan) {
		this.eCodeOfFundOverallPlan = eCodeOfFundOverallPlan;
	}
	
}

package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：资金统筹
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundOverallPlanForm extends NormalActionForm
{
	private static final long serialVersionUID = -5819809395630680234L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态(统筹状态)
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
	private Long lastCyberBankBillTimeStamp;//上一天的网银日期
	@Getter @Setter
	private String fundOverallPlanDate; // 统筹日期
	@Getter @Setter
	private String approvalState; // 审批状态
	@Getter @Setter
	private Boolean isFirstFundAppropriated; // 是否是初次统筹
	@Getter @Setter
	private Tgpf_FundAppropriatedForm[] tgpf_FundAppropriatedList;//拨付记录
	@Getter @Setter
	private Tgpf_OverallPlanAccoutForm[] tgpf_OverallPlanAccountList;//统筹账户


	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
}

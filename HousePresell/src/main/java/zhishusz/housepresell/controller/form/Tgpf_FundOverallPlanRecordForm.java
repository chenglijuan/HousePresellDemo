package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：资金统筹 - 打款记录
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundOverallPlanRecordForm extends NormalActionForm
{
	private static final long serialVersionUID = -5819809395630680234L;
	
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
	private Long escroweBankAccountdId;//托管账号id
	@Getter @Setter
	private Long supervisedBankAccountId;//监管账号id
	@Getter @Setter
	private Long fundOverallPlanId;//资金统筹id
	@Getter @Setter
	private Double payoutAmount;//监管账号id
	@Getter @Setter
	private Tgpf_FundOverallPlanRecordForm[]  tgpf_FundOverallPlanRecord;

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
}

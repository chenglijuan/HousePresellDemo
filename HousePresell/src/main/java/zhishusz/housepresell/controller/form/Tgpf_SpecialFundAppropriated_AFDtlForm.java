package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：特殊拨付-申请子表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_SpecialFundAppropriated_AFDtlForm extends NormalActionForm
{
	private static final long serialVersionUID = -9169709857256121855L;
	
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
	private Tgpf_SpecialFundAppropriated_AF specialAppropriated;//关联申请主表
	@Getter @Setter
	private Long specialAppropriatedId;//关联申请主表-Id
	@Getter @Setter
	private String theCodeOfAf;//主表申请编号
	@Getter @Setter
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;//划款账号关联类
	@Getter @Setter
	private Long bankAccountEscrowedId;//划款账号关联类-Id
	@Getter @Setter
	private String accountOfEscrowed;//划款账号
	@Getter @Setter
	private String theNameOfEscrowed;//划款账号名称
	@Getter @Setter
	private Double applyRefundPayoutAmount;//已申请未拨付总金额（元）
	@Getter @Setter
	private Double appliedAmount;//本次划拨申请金额（元）
	@Getter @Setter
	private String accountBalance;//账户余额
	@Getter @Setter
	private String billNumber;//票据号
	@Getter @Setter
	private String payoutChannel;//拨付渠道
	@Getter @Setter
	private String payoutDate;//拨付日期
	@Getter @Setter
	private Integer payoutState;//拨付状态
	@Getter @Setter
	private String SpacialFundAppropriatedSubList;//拨付信息
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}

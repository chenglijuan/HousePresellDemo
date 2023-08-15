package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_BankBranch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：对账列表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_BalanceOfAccountForm extends NormalActionForm
{
	private static final long serialVersionUID = -8120910537256865044L;
	
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
	private String billTimeStamp;//记账日期
	@Getter @Setter
	private String bankName;//银行名称
	@Getter @Setter
	private String escrowedAccount;//托管账户
	@Getter @Setter
	private String escrowedAccountTheName;//托管账号名称
	@Getter @Setter
	private Integer centerTotalCount;//业务总笔数
	@Getter @Setter
	private Double centerTotalAmount;//业务总金额书
	@Getter @Setter
	private Integer bankTotalCount;//银行总笔数
	@Getter @Setter
	private Double bankTotalAmount;//银行总金额
	@Getter @Setter
	private Integer cyberBankTotalCount;//网银总笔数
	@Getter @Setter
	private Double cyberBankTotalAmount;//网银总金额
	@Getter @Setter
	private Integer accountType;//对账类型(0-业务对账，1-网银对账)
	@Getter @Setter
	private Integer theType;//对账类型(0-业务对账，1-网银对账)
	@Getter @Setter
	private String reconciliationDate;//对账日期
	@Getter @Setter
	private Integer reconciliationState;//对账状态（0-未对账（默认值）1-已对账）
	@Getter @Setter
	private Emmp_BankBranch bankBranch;//银行网点
	@Getter @Setter
	private Long bankBranchId;//银行网点-Id
	@Getter	@Setter	
	private Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed;// 托管表
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}

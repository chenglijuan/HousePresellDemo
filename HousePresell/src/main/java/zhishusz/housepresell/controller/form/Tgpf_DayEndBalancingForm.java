package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_DayEndBalancingForm extends NormalActionForm
{
	private static final long serialVersionUID = -680534696100067056L;
	
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
	@Getter	@Setter
	private String bankName;// 银行名称	
	@Getter	@Setter
	private String escrowedAccount;// 托管账户	
	@Getter	@Setter
	private String escrowedAccountTheName;// 托管账号名称	
	@Getter	@Setter
	private Integer totalCount;// 对账总笔数	
	@Getter	@Setter	
	private Double totalAmount;// 对账总金额	
	@Getter	@Setter
	private String billTimeStamp;// 记账日期
	@Getter @Setter
	private Integer recordState;// 记账状态	
	@Getter @Setter
	private Integer settlementState;// 日终结算状态	
	@Getter @Setter
	private String settlementTime;// 日终结算业务日期
	@Getter @Setter
	private Integer judgeState;
	
	@Getter	@Setter
	private Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed; //托管表
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

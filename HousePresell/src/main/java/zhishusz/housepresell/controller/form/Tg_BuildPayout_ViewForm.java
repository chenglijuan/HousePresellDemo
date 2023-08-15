package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管楼幢拨付明细统计表报表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_BuildPayout_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -3529177904134579855L;

	@Getter @Setter
	private Long tableId;//主键
	@Getter @Setter
	private String companyName;//开发企业 
	@Getter @Setter
	private String projectName;//项目名称  
	@Getter @Setter
	private String eCodeFroMconstruction;//楼幢 
	@Getter @Setter
	private String eCodeFromPayoutBill;//资金拨付单号
	@Getter @Setter
	private Double currentApplyPayoutAmount;//本次申请支付金额
	@Getter @Setter
	private Double currentPayoutAmount;//本次实际支付金额
	@Getter @Setter
	private String payoutDate;//拨付日期
	@Getter @Setter
	private String payoutBank;//拨付银行
	@Getter @Setter
	private String payoutBankAccount;//拨付账号
	
	
	//页面接收字段
	@Getter @Setter
	private String endPayoutDate;//结束日期;
	@Getter @Setter
	private Long companyId;//开发企业ID
	@Getter @Setter
	private Long projectId;//项目ID
	@Getter @Setter
	private Long buildId;//楼幢ID
}

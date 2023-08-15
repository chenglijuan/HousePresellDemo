package zhishusz.housepresell.external.po;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付反馈模型
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
public class PaymentFeedbackModel {

	private String serialNo;// 流水号

	private String recAccNo;// 收款银行账号

	private String recAccName;// 收款银行账号户名

	private Double amount;// 交易金额

	private String use;// 用途

	private String remark;// 备注

	private String oppAccountNo;// 付款银行账号

	private String oppAccountName;// 付款银行账户户名

	private String tradeTime;// 交易时间

	private String bankCode;// 银行返回信息编码

	private String bankInfo;// 银行返回信息描述

	/*
	 * 1sending待发送 3failure交易失败 2unkonw可疑 4success交易成功
	 */
	private String bankStatusFlag;// 单据状态

	/*
	 * prepared：制单 process:审批中 valid：审批通过 useless：作废 invalid：无效
	 */
	private String status;// 审核状态

}

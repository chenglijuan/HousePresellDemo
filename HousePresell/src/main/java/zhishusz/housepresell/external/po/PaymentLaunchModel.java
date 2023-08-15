package zhishusz.housepresell.external.po;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付发起主信息
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
public class PaymentLaunchModel {

	private String orgCode;// 企业唯一标识

	private String secretKey;// 操作密钥
	
	/*
	 * 可选，是否走审批流 ，可以不传，默认为false(不走审批流)，如果值为true，则operator，workflowKey必填
	 */
	private Boolean isNeedCheck;// 是否走审批流
	
	/*
	 * 可选,当前操作人，如果isNeedCheck为true,则该参数必填
	 */
	private String operator;//当前操作人
	
	/*
	 * 可选,流程编号，如果isNeedCheck为true,则该参数必填
	 */
	private String workflowKey;//流程编号
	
	private List<PaymentLaunchDtlModel> transferData;//账户交易明细信息
	
	
}

package zhishusz.housepresell.external.po;

import lombok.Getter;
import lombok.Setter;

/**
 * 接受支付结果推送信息model
 * @author Administrator
 *
 */
@Getter
@Setter
public class BankFeedBackDtlModel {
	
	private String serialNo;//流水号
	
	private String bizId;//接口调用方业务流水号（业务单据号）
	
	private String payAccNo;//付款银行账号
	
	private String recBankAccNo;//收款银行账号
	
	private String receiveAccName;//收款银行账号户名
	
	private Double amount;//交易金额，单位 （元） 
	
	private String use;//用途（业务编号）
	
	private String remark;//备注
	
	private String tradeTime;//交易时间yyyy-MM-dd HH:mm:ss
	
	private String bankStatusFlag;//单据状态

}

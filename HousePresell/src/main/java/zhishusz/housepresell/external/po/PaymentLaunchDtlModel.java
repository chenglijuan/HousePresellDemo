package zhishusz.housepresell.external.po;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付发起子信息
 * @author Administrator
 *
 */
@Getter
@Setter
public class PaymentLaunchDtlModel {
	
	private String payAccNo;//付款银行账户账号
	
	private String recBankAccNo;//收款银行账户账号
	
	private String receiveBankCode;//收款人银行编码
	
	private String ledgerBankCode;//联行号
	
	private String receiveAccName;//收款人户名
	
	/*
	 * 必须，账户类型对公对私 public，private
	 */
	private String accType;//账户类型对公对私 
	
	private String use;//用途
	
	private BigDecimal amount;//转账金额
	
	/*
	 * 可选，接口调用方业务流水号(用于与接口生成的流水号一一对应)
	 */
	private String bizId;//接口调用方业务流水号
	
	private String remark;//备注
	
	/*
	 * 可选，是否加急 (on 加急）
	 */
	private String crashFlag;//是否加急
	
	private String areaCode;//地区码

	private String projectName;
}

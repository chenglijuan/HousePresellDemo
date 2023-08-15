package zhishusz.housepresell.external.po;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 网银数据明细接收模型
 * @author Administrator
 *
 */
@Getter
@Setter
public class BankUploadDtlModel {
	
	private Date dataUpdateTime;//接收时间
	
	private Date tradeTime;//交易时间
	
	private String oppaccountNo;//对方账号
	
	private String digest;//摘要
	
	private String accountNo;//本方账号
	
	private String purpose;//用途
	
	private String oppOpenBankName;//对方开户行
	
	private Long id;//明细id
	
	private Double amount;//金额
	
	private Double balance;//账户余额
	
	private Long time;//推送时间戳
	
	private String accountName;//本方户名
	
	private String serialNO;//交易流水号
	
	private String oppaccountName;//对方户名
	
	private String tradeType;//收支方向
	
	private String bankName;//对方开户行
	


	
	
	

}

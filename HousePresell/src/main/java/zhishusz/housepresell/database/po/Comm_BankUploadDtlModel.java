package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.external.po.BankUploadDtlModel;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 网银数据明细接收模型
 * 
 * @author Administrator
 *
 */
@Getter
@Setter
@ITypeAnnotation(remark = "网银数据明细接收模型")
public class Comm_BankUploadDtlModel implements Serializable {

	private static final long serialVersionUID = 5684921508544946636L;
	
	public Comm_BankUploadDtlModel(BankUploadDtlModel po){
		this.accountName = po.getAccountName();
		this.accountNo = po.getAccountNo();
		this.amount = po.getAmount();
		this.balance = po.getBalance();
		this.dataUpdateTime = po.getDataUpdateTime();
		this.digest = po.getDigest();
		this.id = po.getId();
		this.oppaccountName = po.getOppaccountName();
		this.oppaccountNo = po.getOppaccountNo();
		this.oppOpenBankName = po.getOppOpenBankName();
		this.purpose = po.getPurpose();
		this.serialNO = po.getSerialNO();
		this.time = po.getTime();
		this.tradeTime = po.getTradeTime();
		this.tradeType = po.getTradeType();
		this.createTimeStamp = System.currentTimeMillis();
	}

	@IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;

	@IFieldAnnotation(remark = "创建时间")
	private Long createTimeStamp;

	@IFieldAnnotation(remark = "接收时间")
	private Date dataUpdateTime;

	@IFieldAnnotation(remark = "交易时间")
	private Date tradeTime;

	@IFieldAnnotation(remark = "对方账号")
	private String oppaccountNo;

	@IFieldAnnotation(remark = "摘要")
	private String digest;

	@IFieldAnnotation(remark = "本方账号")
	private String accountNo;

	@IFieldAnnotation(remark = "用途")
	private String purpose;

	@IFieldAnnotation(remark = "对方开户行")
	private String oppOpenBankName;

	@IFieldAnnotation(remark = "明细id")
	private Long id;

	@IFieldAnnotation(remark = "金额")
	private Double amount;

	@IFieldAnnotation(remark = "账户余额")
	private Double balance;

	@IFieldAnnotation(remark = "推送时间戳")
	private Long time;

	@IFieldAnnotation(remark = "本方户名")
	private String accountName;

	@IFieldAnnotation(remark = "交易流水号")
	private String serialNO;

	@IFieldAnnotation(remark = "对方户名")
	private String oppaccountName;

	@IFieldAnnotation(remark = "收支方向")
	private String tradeType;

}

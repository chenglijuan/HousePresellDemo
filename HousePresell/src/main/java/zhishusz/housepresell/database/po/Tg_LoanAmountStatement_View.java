package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管现金流量表  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="托管现金流量表")
public class Tg_LoanAmountStatement_View  implements Serializable
{
	private static final long serialVersionUID = -966887912728133259L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="日期")
	private String billTimeStamp;//日期 
	@Getter @Setter @IFieldAnnotation(remark="上期结余")
	private Double lastAmount;//上期结余
	@Getter @Setter @IFieldAnnotation(remark="托管资金入账金额")
	private Double loanAmountIn;//托管资金入账金额
	@Getter @Setter @IFieldAnnotation(remark="存单存入")
	private Double depositReceipt;//存单存入
	@Getter @Setter @IFieldAnnotation(remark="资金拨付金额")
	private Double payoutAmount;//资金拨付金额
	@Getter @Setter @IFieldAnnotation(remark="存单到期")
	private Double depositExpire;//存单到期
	@Getter @Setter @IFieldAnnotation(remark="活期余额")
	private Double currentBalance;//活期余额
	
	
}

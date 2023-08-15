package zhishusz.housepresell.database.po;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 托协定存款统计表
 * 
 * @author ZS004
 */
@ITypeAnnotation(remark = "协定存款统计表")
public class Tg_DepositFloat_View implements Serializable
{

	private static final long serialVersionUID = 4227682571007637817L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主表主键
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称")
	private String bankName;//银行名称

	@Getter @Setter @IFieldAnnotation(remark="协定存款利率")
	private Double depositRate;//协定存款利率
	
	@Getter @Setter @IFieldAnnotation(remark="起始金额（万元）")
	private Double orgAmount;//起始金额（万元）
	
	@Getter @Setter @IFieldAnnotation(remark="签订日期")
	private String signDate;//签订日期
	
	@Getter @Setter @IFieldAnnotation(remark="期限")
	private String timeLimit;//期限
	
	@Getter @Setter @IFieldAnnotation(remark="到期日期")
	private String endExpirationDate;//到期日期
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;//备注
	
}

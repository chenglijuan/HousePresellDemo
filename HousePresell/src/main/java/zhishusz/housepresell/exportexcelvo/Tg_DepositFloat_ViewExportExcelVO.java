package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 托协定存款统计表-导出VO
 * @author ZS004
 */
public class Tg_DepositFloat_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号 
	
	@Getter @Setter 
	private String bankName;//银行名称

	@Getter @Setter
	private Double depositRate;//协定存款利率
	
	@Getter @Setter
	private Double orgAmount;//起始金额（万元）
	
	@Getter @Setter
	private String signDate;//签订日期
	
	@Getter @Setter 
	private String timeLimit;//期限
	
	@Getter @Setter
	private String endExpirationDate;//到期日期
	
	@Getter @Setter 
	private String remark;//备注
}

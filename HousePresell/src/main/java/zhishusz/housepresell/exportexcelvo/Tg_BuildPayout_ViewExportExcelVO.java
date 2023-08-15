package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管楼幢拨付明细统计表报表  接受Bean
 * @author ZS004
 */
public class Tg_BuildPayout_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号 
	@Getter @Setter
	private String companyName;//开发企业 
	@Getter @Setter
	private String projectName;//项目名称  
	@Getter @Setter
	private String eCodeFroMconstruction;//楼幢 
	@Getter @Setter
	private String currentFigureProgress;//当前形象进度 
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
	
	
}

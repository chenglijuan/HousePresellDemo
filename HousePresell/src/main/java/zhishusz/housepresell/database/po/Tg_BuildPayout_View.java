package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管楼幢拨付明细统计表报表  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="托管楼幢拨付明细统计表")
public class Tg_BuildPayout_View  implements Serializable
{
	private static final long serialVersionUID = 8923116091452735841L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String companyName;//开发企业 
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称  
	@Getter @Setter @IFieldAnnotation(remark="楼幢")
	private String eCodeFroMconstruction;//楼幢 
	
	@Getter @Setter @IFieldAnnotation(remark="当前形象进度")
	private String currentFigureProgress;//当前形象进度 
	
	@Getter @Setter @IFieldAnnotation(remark="资金拨付单号")
	private String eCodeFromPayoutBill;//资金拨付单号
	@Getter @Setter @IFieldAnnotation(remark="本次申请支付金额")
	private Double currentApplyPayoutAmount;//本次申请支付金额
	@Getter @Setter @IFieldAnnotation(remark="本次实际支付金额")
	private Double currentPayoutAmount;//本次实际支付金额
	@Getter @Setter @IFieldAnnotation(remark="拨付日期")
	private String payoutDate;//拨付日期
	@Getter @Setter @IFieldAnnotation(remark="拨付银行")
	private String payoutBank;//拨付银行
	@Getter @Setter @IFieldAnnotation(remark="拨付账号")
	private String payoutBankAccount;//拨付账号
	
	public String geteCodeFroMconstruction()
	{
		return eCodeFroMconstruction;
	}
	public void seteCodeFroMconstruction(String eCodeFroMconstruction)
	{
		this.eCodeFroMconstruction = eCodeFroMconstruction;
	}
	public String geteCodeFromPayoutBill()
	{
		return eCodeFromPayoutBill;
	}
	public void seteCodeFromPayoutBill(String eCodeFromPayoutBill)
	{
		this.eCodeFromPayoutBill = eCodeFromPayoutBill;
	}
	
	
	
}

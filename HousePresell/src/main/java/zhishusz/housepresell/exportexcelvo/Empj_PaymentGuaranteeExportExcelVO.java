package zhishusz.housepresell.exportexcelvo;

import java.util.Date;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 支付保证申请撤销-导出Excel
 * */
public class Empj_PaymentGuaranteeExportExcelVO
{
	@Getter @Setter
	private Integer ordinal;//序号 
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "支付保证撤销申请单号")
	private String revokeNo;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "申请日期")
	private String applyDate;// 申请日期
	

	@IFieldAnnotation(remark = "编号")
	private String eCode;// eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "开发企业 ")
	private String companyName;// 开发企业
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目名称")
	private String projectName;// 项目名称
	

	@Getter
	@Setter
	@IFieldAnnotation(remark = "支付保证单号")
	private String guaranteeNo;// 支付保证单号
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "支付保证出具单位")
	private String guaranteeCompany;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "//支付保证类型 (1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )")
	private String guaranteeType;// 支付保证类型(1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )
		
	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目建设工程已实际支付金额 ")
	private Double alreadyActualAmount;// 项目建设工程已实际支付金额

	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目建设工程待支付承保金额 ")
	private Double actualAmount; // 项目建设工程待支付承保金额
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "已落实支付保证金额")
	private Double guaranteedAmount; // 已落实支付保证金额
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "备注")
	private String remark; // 备注
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;//操作人

	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;//操作日期

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;//审核人
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;//审核日期
	
	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}








package zhishusz.housepresell.exportexcelvo;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-项目托管资金收支情况表-导出Excel
 * @ClassName:  Qs_ProjectEscrowAmount_ViewExportExcelVO   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月20日 上午11:37:51   
 * @version V1.0 
 *
 */
public class Qs_ProjectEscrowAmount_ViewExportExcelVO {
	
	@Getter @Setter
	private Integer ordinal;//序号 
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String theNameOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢号")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="放款户数")
	private Integer loansCountHouse;
	
	@Getter @Setter @IFieldAnnotation(remark="托管收入")
	private Double income;
	
	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	
	@Getter @Setter @IFieldAnnotation(remark="余额")
	private Double currentFund;
	
	@Getter @Setter @IFieldAnnotation(remark="溢出金额")
	private Double spilloverAmount;

	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private String cityRegionname;
	
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
}

package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管楼幢入账统计表报表  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="托管楼幢入账统计表")
public class Tg_BuildCount_View  implements Serializable
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
	@Getter @Setter @IFieldAnnotation(remark="银行名称")
	private String bankName;//银行名称
	@Getter @Setter @IFieldAnnotation(remark="托管收入")
	private Double income;
	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	@Getter @Setter @IFieldAnnotation(remark="余额")
	private Double balance;
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;//记账日期
	
	@Getter @Setter 
	private Double commercialLoan;//商贷（托管收入）
	
	@Getter @Setter 
	private Double accumulationFund;//公积金（托管收入）
	
	public String geteCodeFroMconstruction()
	{
		return eCodeFroMconstruction;
	}
	public void seteCodeFroMconstruction(String eCodeFroMconstruction)
	{
		this.eCodeFroMconstruction = eCodeFroMconstruction;
	}
	
	
	
}

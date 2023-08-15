package zhishusz.housepresell.exportexcelvo;


import lombok.Getter;
import lombok.Setter;

/**
 * 托管楼幢查询报表-导出Excel  接受Bean
 * @author ZS004
 */
public class Tg_BuildCount_ViewExportExcelVO 
{
	@Getter @Setter
	private Integer ordinal;//序号 
	@Getter @Setter
	private String companyName;//开发企业 
	@Getter @Setter
	private String projectName;//项目名称  
/*	@Getter @Setter
	private String eCodeFroMconstruction;//楼幢  
	@Getter @Setter
	private String bankName;//银行名称
*/	@Getter @Setter
	private Double income;//托管收入
	@Getter @Setter 
	private Double payout;//托管支出
	@Getter @Setter
	private Double balance;//余额
	@Getter @Setter
	private String billTimeStamp;//记账日期
	
	@Getter @Setter 
	private Double commercialLoan;//商贷（托管收入）
	
	@Getter @Setter 
	private Double accumulationFund;//公积金（托管收入）
}

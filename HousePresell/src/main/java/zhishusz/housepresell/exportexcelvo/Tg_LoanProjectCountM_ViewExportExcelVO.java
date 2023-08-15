package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管项目统计表（财务部）  接受Bean
 * @author ZS004
 */
public class Tg_LoanProjectCountM_ViewExportExcelVO
{
	
	@Getter @Setter 
	private Integer ordinal;//序号 
	@Getter @Setter
	private String cityRegion;//区域
	@Getter @Setter 
	private String companyName;//企业信息
	@Getter @Setter 
	private String projectName;//项目名称
	@Getter @Setter 
	private Integer upTotalFloorNumber;//地上楼层数（总）
	@Getter @Setter
	private String contractSigningDate;//托管合作协议签订日期
	@Getter @Setter
	private String preSaleCardDate;//预售证日期
	@Getter @Setter
	private String preSalePermits;//预售许可证
	@Getter @Setter
	private String eCodeOfAgreement;//协议编号
	@Getter @Setter
	private String remark;//备注
	
}

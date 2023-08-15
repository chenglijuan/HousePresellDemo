package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：托管项目统计表（项目部）
 * */
public class Tg_LoanProjectCountP_ViewExportExcelVO
{

	@Getter @Setter
	private Integer ordinal;//序号 	
	
	@Getter @Setter 
	private String companyGroup;//集团
	
	@Getter @Setter
	private String companyName;//开发企业
	
	@Getter @Setter 
	private String cityRegion;//区域
	
	@Getter @Setter 
	private String projectName;//托管项目
	
	@Getter @Setter
	private String eCodeFromConstruction;//托管楼幢
	
	@Getter @Setter 
	private String deliveryType;//交付类型
	
	@Getter @Setter 
	private Double upTotalFloorNumber;//地上总层数
	
	@Getter @Setter 
	private Double escrowArea;//托管面积
	
	@Getter @Setter 
	private Double recordAvgPriceOfBuilding;//托管楼幢备案均价
	
	@Getter @Setter
	private Double orgLimitedAmount;//初始受限额度
	
	@Getter @Setter
	private Double currentLimitedAmount;//当前受限额度
	
	@Getter @Setter 
	private String currentBuildProgress;//当前建设进度
	
	@Getter @Setter 
	private String currentLimitedNote;//当前受限节点
	
	@Getter @Setter 
	private Double currentEscrowFund;//托管余额
	
	@Getter @Setter 
	private Double amountOffset;//抵充额度
	
	@Getter @Setter 
	private Double sumFamilyNumber;//总户数
	
	@Getter @Setter 
	private Double signHouseNum;//签约户数
	
	@Getter @Setter 
	private Double recordHouseNum;//备案户数
	
	@Getter @Setter 
	private Double depositHouseNum;//托管户数
	
	@Getter @Setter 
	private String isPresell;//预售证（有/无）
	
	@Getter @Setter 
	private String escrowAgRecordTime;//合作协议备案时间
}

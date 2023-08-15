package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管项目统计表（项目部）
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_LoanProjectCountP_ViewForm  extends NormalActionForm
{
	private static final long serialVersionUID = 5393576698400920123L;
	
	@Getter @Setter 
	private Long tableId;//主键
	
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

	
	
	//接收页面传递的参数
	@Getter @Setter
	private Long cityRegionId;//区域Id
	
	@Getter @Setter
	private Long projectId;//项目Id
	
	@Getter @Setter
	private Long companyId;//企业Id
	
	@Getter @Setter
	private String recordDateStart;//托管合作协议签订日期（开始）
	
	@Getter @Setter
	private String recordDateEnd;//托管合作协议签订日期（结束）
}

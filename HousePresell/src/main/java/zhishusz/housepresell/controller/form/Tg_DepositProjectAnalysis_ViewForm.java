package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管项目情况分析表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_DepositProjectAnalysis_ViewForm  extends NormalActionForm
{

	private static final long serialVersionUID = -1028579632854982199L;
	
	@Getter @Setter 
	private Long tableId;//主键
	
	@Getter @Setter 
	private String cityRegion;//区域
	
	@Getter @Setter 
	private String busiKind;//业务状态  当前  同比  环比

	@Getter @Setter 
	private Double escrowArea;//已签约托管面积（㎡）
	
	@Getter @Setter 
	private String escrowAreaRatio;//已签约托管面积区域占比（%）
	
	@Getter @Setter
	private Double preEscrowArea;//已预售托管面积（㎡）
	
	@Getter @Setter 
	private String preEscrowAreaRatio;//已预售托管面积区域占比（%）
	
	@Getter @Setter 
	private String queryYear;//查询年份
	
	@Getter @Setter 
	private String queryQuarter;//查询季度
	
	@Getter @Setter 
	private String queryMonth;//查询月份
	
	@Getter @Setter 
	private String depositTime;//托管时间
	
	
	//接收页面传递
	@Getter @Setter
	private String querytTime;//查询时间
	
	@Getter @Setter
	private String timeKind;//时间类型
	
	@Getter @Setter 
	private Long cityRegionId;//区域Id
	
	
	

}

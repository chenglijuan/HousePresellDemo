package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：托管项目情况分析表
 */
@ITypeAnnotation(remark="托管项目情况分析表")
public class Tg_DepositProjectAnalysis_View  implements Serializable
{

	private static final long serialVersionUID = -2241820140058529662L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private String cityRegion;//区域
	
	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiKind;//业务状态  当前  同比  环比

	@Getter @Setter @IFieldAnnotation(remark="已签约托管面积（㎡）")
	private Double escrowArea;//已签约托管面积（㎡）
	
	@Getter @Setter @IFieldAnnotation(remark="已签约托管面积区域占比（%）")
	private String escrowAreaRatio;//已签约托管面积区域占比（%）
	
	@Getter @Setter @IFieldAnnotation(remark="已预售托管面积（㎡）")
	private Double preEscrowArea;//已预售托管面积（㎡）
	
	@Getter @Setter @IFieldAnnotation(remark="已预售托管面积区域占比（%）")
	private String preEscrowAreaRatio;//已预售托管面积区域占比（%）
	
	@Getter @Setter @IFieldAnnotation(remark="托管时间")
	private String depositTime;//托管时间
	
	@Getter @Setter @IFieldAnnotation(remark="查询年份")
	private String queryYear;//查询年份
	
	@Getter @Setter @IFieldAnnotation(remark="查询季度")
	private String queryQuarter;//查询季度
	
	@Getter @Setter @IFieldAnnotation(remark="查询月份")
	private String queryMonth;//查询月份
	
	@Getter @Setter @IFieldAnnotation(remark="区域信息")
	private Sm_CityRegionInfo cityRegionInfo;//区域信息
}

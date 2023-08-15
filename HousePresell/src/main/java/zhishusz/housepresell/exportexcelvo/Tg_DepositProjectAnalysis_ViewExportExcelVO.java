package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：托管项目情况分析表
 */
@ITypeAnnotation(remark="托管项目情况分析表")
public class Tg_DepositProjectAnalysis_ViewExportExcelVO
{

	@Getter @Setter
	private Integer ordinal;//序号 
	
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

}

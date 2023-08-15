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
public class Tg_DepositProjectAnalysis_HomeView  implements Serializable
{

	private static final long serialVersionUID = -2241820140058529662L;	
	
	@Getter @Setter @IFieldAnnotation(remark="查询月份")
	private String queryTime;//年月
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积（㎡）")
	private Double escrowArea;//托管面积（㎡）
	
	@Getter @Setter @IFieldAnnotation(remark="预售面积（㎡）")
	private Double preEscrowArea;//预售面积（㎡）
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积同比（%）")
	private Double escrowAreaBeforeYear;//托管面积同比（%）	
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积环比（%）")
	private Double escrowAreaBeforeMonth;//托管面积环比（%）
	
	@Getter @Setter @IFieldAnnotation(remark="预售面积同比（%）")
	private Double preEscrowAreaBeforeYear;//预售面积同比（%）	
	
	@Getter @Setter @IFieldAnnotation(remark="预售面积环比（%）")
	private Double preEscrowAreaBeforeMonth;//预售面积环比（%）
	
	
}

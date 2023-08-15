package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：受限额度设置
 * */
@ITypeAnnotation(remark="受限额度设置")
public class Tgpj_BldLimitAmountVer_AFDtl implements Serializable,Comparable<Tgpj_BldLimitAmountVer_AFDtl>
{
	private static final long serialVersionUID = -7192321035665791025L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="最后修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="关联受限额度主表")
	private Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng;

	@Getter @Setter @IFieldAnnotation(remark="阶段名称")
	private String stageName;
	
	@Getter @Setter @IFieldAnnotation(remark="受限额度")
	private Double limitedAmount;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	/*
---------毛坯房---------
序号	受限额度节点		受限比例
1	正负零前			100%
2	正负零			80%
3	主体结构达到1/2	60%
4	主体结构封顶		40%
5	外立面装饰工程完成 	20%
6	完成交付使用备案	0%

---------成品房---------
序号	受限额度节点		受限比例
1	正负零前			100%
2	正负零			80%
3	主体结构达到1/2	60%
4	主体结构封顶		40%
5	外立面装饰工程完成 	35%
6	室内装修工程完成	5%
7	完成交付使用备案	0%
	 * */
	@Override
	public int compareTo(Tgpj_BldLimitAmountVer_AFDtl figureProgressAndLimitedAmount)
	{
		return (int)(this.limitedAmount - figureProgressAndLimitedAmount.limitedAmount);
	}
	/** 
	 * 获取eCode  
	 * @return eCode
	 */
	public String geteCode() {
		return eCode;
	}
	/**  
	 * 设置eCode  
	 * @param eCode
	 */
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public List getNeedFieldList(){
		return Arrays.asList("theName");
	}
	
}

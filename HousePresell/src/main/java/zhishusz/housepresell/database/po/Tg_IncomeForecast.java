package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：收入预测
 */
@ITypeAnnotation(remark="收入预测")
public class Tg_IncomeForecast implements Serializable
{
	private static final long serialVersionUID = 4746116016619027706L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="日期(工作日)")
	private Long theDay;
	
	@Getter @Setter @IFieldAnnotation(remark="星期一到星期日")
	private Integer theWeek;
	
	@Getter @Setter @IFieldAnnotation(remark="入账资金趋势预测（元）")
	private Double incomeTrendForecast;
	
	@Getter @Setter @IFieldAnnotation(remark="定期到期（元）")
	private Double fixedExpire;
	
	@Getter @Setter @IFieldAnnotation(remark="银行放贷额度（元）")
	private Double bankLending;
	
	@Getter @Setter @IFieldAnnotation(remark="收入预测1（元）")
	private Double incomeForecast1;
	
	@Getter @Setter @IFieldAnnotation(remark="收入预测2（元）")
	private Double incomeForecast2;
	
	@Getter @Setter @IFieldAnnotation(remark="收入预测3（元）")
	private Double incomeForecast3;
	
	@Getter @Setter @IFieldAnnotation(remark="收入合计（元）")
	private Double incomeTotal;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

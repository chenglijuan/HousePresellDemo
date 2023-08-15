package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：支出预测
 */
@ITypeAnnotation(remark="支出预测")
public class Tg_ExpForecast implements Serializable
{
	private static final long serialVersionUID = 8985735493013247916L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="支出资金趋势预测（元）")
	private Double payTrendForecast;
	
	@Getter @Setter @IFieldAnnotation(remark="已申请资金拨付（元）")
	private Double applyAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="可拨付金额（元）")
	private Double payableFund;
	
	@Getter @Setter @IFieldAnnotation(remark="节点变更拨付预测（元）")
	private Double nodeChangePayForecast;
	
	@Getter @Setter @IFieldAnnotation(remark="正在办理中的定期存款（元）")
	private Double handlingFixedDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="支出预测1（元）")
	private Double payForecast1;
	
	@Getter @Setter @IFieldAnnotation(remark="支出预测2（元）")
	private Double payForecast2;
	
	@Getter @Setter @IFieldAnnotation(remark="支出预测3（元）")
	private Double payForecast3;
	
	@Getter @Setter @IFieldAnnotation(remark="支出合计（元）")
	private Double payTotal;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

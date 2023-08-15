package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：收支存预测
 */
@ITypeAnnotation(remark="收支存预测")
public class Tg_IncomeExpDepositForecast implements Serializable
{
	private static final long serialVersionUID = 8478475304795653426L;

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
	
	@Getter @Setter @IFieldAnnotation(remark="上日活期结余（元）")
	private Double lastDaySurplus;
	
	@Getter @Setter @IFieldAnnotation(remark="收入合计（元）")
	private Double incomeTotal;
	
	@Getter @Setter @IFieldAnnotation(remark="支出合计（元）")
	private Double expTotal;
	
	@Getter @Setter @IFieldAnnotation(remark="本日活动结余（元）")
	private Double todaySurplus;
	
	@Getter @Setter @IFieldAnnotation(remark="托管余额参考值（元）")
	private Double collocationReference;
	
	@Getter @Setter @IFieldAnnotation(remark="扣减参考值后的托管余额（元）")
	private Double collocationBalance;
	
	@Getter @Setter @IFieldAnnotation(remark="可存入参考值1（元）")
	private Double canDepositReference1;

	@Getter @Setter @IFieldAnnotation(remark="可存入参考值2（元）")
	private Double canDepositReference2;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}

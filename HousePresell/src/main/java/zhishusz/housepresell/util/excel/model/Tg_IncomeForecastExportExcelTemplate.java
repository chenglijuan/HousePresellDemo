package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyString;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Tg_IncomeForecastExportExcelTemplate implements IExportExcel<Tg_IncomeForecast>
{

	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	private MyString myString = MyString.getInstance();

	@Getter @Setter @IFieldAnnotation(remark="日期(工作日)")
	private String theDay;

	@Getter @Setter @IFieldAnnotation(remark="星期一到星期日")
	private String theWeek;

	@Getter @Setter @IFieldAnnotation(remark="入账资金趋势预测（元）")
	private String incomeTrendForecast;

	@Getter @Setter @IFieldAnnotation(remark="定期到期（元）")
	private String fixedExpire;

	@Getter @Setter @IFieldAnnotation(remark="银行放贷额度（元）")
	private String bankLending;

	@Getter @Setter @IFieldAnnotation(remark="收入预测1（元）")
	private String incomeForecast1;

	@Getter @Setter @IFieldAnnotation(remark="收入预测2（元）")
	private String incomeForecast2;

	@Getter @Setter @IFieldAnnotation(remark="收入预测3（元）")
	private String incomeForecast3;

	@Getter @Setter @IFieldAnnotation(remark="收入合计（元）")
	private String incomeTotal;

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("theDay", "日期(工作日)");
		map.put("theWeek", "星期");
		map.put("incomeTrendForecast", "入账资金趋势预测（元）");
		map.put("fixedExpire", "定期到期（元）");
		map.put("bankLending", "银行放贷额度（元）");
		map.put("incomeForecast1", "收入预测1（元）");
		map.put("incomeForecast2", "收入预测2（元）");
		map.put("incomeForecast3", "收入预测3（元）");
		map.put("incomeTotal", "收入合计（元）");

		return map;
	}

	@Override
	public void init(Tg_IncomeForecast fromClass)
	{
		//		this.seteCode(fromClass.geteCode());
		if (fromClass.getTheDay() != null && fromClass.getTheDay() > 0) {
			this.setTheDay(myDatetime.dateToSimpleString(fromClass.getTheDay()));
		}
		if (fromClass.getTheWeek() != null) {
			switch (fromClass.getTheWeek()) {
			case 1 :
				this.setTheWeek("星期一");
				break;
			case 2 :
				this.setTheWeek("星期二");
				break;
			case 3 :
				this.setTheWeek("星期三");
				break;
			case 4 :
				this.setTheWeek("星期四");
				break;
			case 5 :
				this.setTheWeek("星期五");
				break;
			case 6 :
				this.setTheWeek("星期六");
				break;
			default :
				this.setTheWeek("星期日");
				break;
			}
		}
		this.setIncomeTrendForecast(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getIncomeTrendForecast(), 2)));
		this.setFixedExpire(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getFixedExpire(), 2)));
		this.setBankLending(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getBankLending(), 2)));
		this.setIncomeForecast1(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getIncomeForecast1(), 2)));
		this.setIncomeForecast2(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getIncomeForecast2(), 2)));
		this.setIncomeForecast3(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getIncomeForecast3(), 2)));
		this.setIncomeTotal(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getIncomeTrendForecast(), 2)
							+ myDouble.getShort(fromClass.getFixedExpire(), 2)
							+ myDouble.getShort(fromClass.getBankLending(), 2)
							+ myDouble.getShort(fromClass.getIncomeForecast1(), 2)
							+ myDouble.getShort(fromClass.getIncomeForecast2(), 2)
							+ myDouble.getShort(fromClass.getIncomeForecast3(), 2)));

	}

}
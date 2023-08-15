package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Tg_IncomeExpDepositForecast;
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
public class Tg_IncomeExpDepositForecastExportExcelTemplate implements IExportExcel<Tg_IncomeExpDepositForecast>
{

	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	private MyString myString = MyString.getInstance();

	@Getter @Setter @IFieldAnnotation(remark="日期(工作日)")
	private String theDay;

	@Getter @Setter @IFieldAnnotation(remark="星期一到星期日")
	private String theWeek;

	@Getter @Setter @IFieldAnnotation(remark="上日活期结余（元）")
	private String lastDaySurplus;

	@Getter @Setter @IFieldAnnotation(remark="收入预测合计（元）")
	private String incomeTotal;

	@Getter @Setter @IFieldAnnotation(remark="支出预测合计（元）")
	private String expTotal;

	@Getter @Setter @IFieldAnnotation(remark="本日活期余额（元）")
	private String todaySurplus;

	@Getter @Setter @IFieldAnnotation(remark="托管余额参考值（元）")
	private String collocationReference;

	@Getter @Setter @IFieldAnnotation(remark="扣减参考值后的托管余额（元）")
	private String collocationBalance;

	@Getter @Setter @IFieldAnnotation(remark="可存入参考值1（元）")
	private String canDepositReference1;

	@Getter @Setter @IFieldAnnotation(remark="可存入参考值2（元）")
	private String canDepositReference2;

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("theDay", "日期(工作日)");
		map.put("theWeek", "星期");
		map.put("lastDaySurplus", "上日活期结余（元）");
		map.put("incomeTotal", "收入预测合计（元）");
		map.put("expTotal", "支出预测合计（元）");
		map.put("todaySurplus", "本日活期余额（元）");
		map.put("collocationReference", "托管余额参考值（元）");
		map.put("collocationBalance", "扣减参考值后的托管余额（元）");
		map.put("canDepositReference1", "可存入参考值1（元）");
		map.put("canDepositReference2", "可存入参考值2（元）");

		return map;
	}

	@Override
	public void init(Tg_IncomeExpDepositForecast fromClass)
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

		this.setLastDaySurplus(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getLastDaySurplus(), 2)));
		this.setIncomeTotal(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getIncomeTotal(), 2)));
		this.setExpTotal(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getExpTotal(), 2)));
		this.setTodaySurplus(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getTodaySurplus(), 2)));
		this.setCollocationReference(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getCollocationReference(), 2)));
		this.setCollocationBalance(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getCollocationBalance(), 2)));
		this.setCanDepositReference1(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getCanDepositReference1(), 2)));
		this.setCanDepositReference2(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getCanDepositReference2(), 2)));
	}
}
package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Tg_ExpForecast;
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
public class Tg_ExpForecastExportExcelTemplate implements IExportExcel<Tg_ExpForecast>
{

	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	private MyString myString = MyString.getInstance();

	@Getter @Setter @IFieldAnnotation(remark="日期(工作日)")
	private String theDay;

	@Getter @Setter @IFieldAnnotation(remark="星期一到星期日")
	private String theWeek;

	@Getter @Setter @IFieldAnnotation(remark="支出资金趋势预测（元）")
	private String payTrendForecast;

	@Getter @Setter @IFieldAnnotation(remark="已申请资金拨付（元）")
	private String applyAmount;

	@Getter @Setter @IFieldAnnotation(remark="可拨付金额（元）")
	private String payableFund;

	@Getter @Setter @IFieldAnnotation(remark="节点变更拨付预测（元）")
	private String nodeChangePayForecast;

	@Getter @Setter @IFieldAnnotation(remark="正在办理中的定期存款（元）")
	private String handlingFixedDeposit;

	@Getter @Setter @IFieldAnnotation(remark="支出预测1（元）")
	private String payForecast1;

	@Getter @Setter @IFieldAnnotation(remark="支出预测2（元）")
	private String payForecast2;

	@Getter @Setter @IFieldAnnotation(remark="支出预测3（元）")
	private String payForecast3;

	@Getter @Setter @IFieldAnnotation(remark="支出合计（元）")
	private String payTotal;

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("theDay", "日期(工作日)");
		map.put("theWeek", "星期");
		map.put("payTrendForecast", "支出资金趋势预测（元）");
		map.put("applyAmount", "已申请资金拨付（元）");
		map.put("payableFund", "可拨付金额（元）");
		map.put("nodeChangePayForecast", "节点变更拨付预测（元）");
		map.put("handlingFixedDeposit", "正在办理中的定期存款（元）");
		map.put("payForecast1", "支出预测1（元）");
		map.put("payForecast2", "支出预测2（元）");
		map.put("payForecast3", "支出预测3（元）");
		map.put("payTotal", "支出合计（元）");

		return map;
	}

	@Override
	public void init(Tg_ExpForecast fromClass)
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
		this.setPayTrendForecast(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPayTrendForecast(), 2)));
		this.setApplyAmount(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getApplyAmount(), 2)));
		this.setPayableFund(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPayableFund(), 2)));
		this.setNodeChangePayForecast(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getNodeChangePayForecast(), 2)));
		this.setHandlingFixedDeposit(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getHandlingFixedDeposit(), 2)));
		this.setPayForecast1(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPayForecast1(), 2)));
		this.setPayForecast2(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPayForecast2(), 2)));
		this.setPayForecast3(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPayForecast3(), 2)));
		this.setPayTotal(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPayTrendForecast(), 2)
							+ myDouble.getShort(fromClass.getApplyAmount(), 2)
							+ myDouble.getShort(fromClass.getPayableFund(), 2)
							+ myDouble.getShort(fromClass.getNodeChangePayForecast(), 2)
							+ myDouble.getShort(fromClass.getHandlingFixedDeposit(), 2)
							+ myDouble.getShort(fromClass.getPayForecast1(), 2)
							+ myDouble.getShort(fromClass.getPayForecast2(), 2)
							+ myDouble.getShort(fromClass.getPayForecast3(), 2)));

	}

}
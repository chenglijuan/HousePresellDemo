package zhishusz.housepresell.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MyDouble
{
	private static MyDouble instance;

	private MyDouble()
	{

	}

	public static MyDouble getInstance()
	{
		if (instance == null)
			instance = new MyDouble();

		return instance;
	}

	public Double parse(String str)
	{
		Double result = null;
		if (str != null && str.length() > 0)
		{
			if (str.contains(","))
			{
				str = str.replaceAll(",", "");
			}
			try
			{
				result = Double.parseDouble(str);
			}
			catch (NumberFormatException e)
			{
				result = null;
			}
		}
		return result;
	}

	public Double parse(Object object)
	{
		if (object == null)
		{
			return null;
		}
		else if (object instanceof String[])
		{
			String[] objArr = (String[]) object;
			if (objArr.length > 0)
			{
				return parse(objArr[0]);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return parse(object.toString());
		}
	}

	public Double getShort(Double db, Integer flLeng)
	{
		// 截取db,保留flLeng位小数
		if (db != null && flLeng != null && flLeng >= 0)
		{
			StringBuffer stringBuffer = new StringBuffer();

			stringBuffer.append("#0.");
			for (int i = 0; i < flLeng; i++)
			{
				stringBuffer.append("0");
			}

			DecimalFormat df = new DecimalFormat(stringBuffer.toString());

			db = Double.parseDouble(df.format(db));
		}
		else
		{
			return 0.0;
		}
		return db;
	}

	/**
	 * double类的加法(num1+num2)
	 * 
	 * @param num1
	 *            double 类型
	 * @param num2
	 *            double 类型
	 * @return double 类型
	 */
	public Double doubleAddDouble(Double num1, Double num2)
	{
		if(null == num1)
		{
			num1 = 0.00;
		}
		if(null == num2)
		{
			num2 = 0.00;
		}
		return new BigDecimal(num1).add(new BigDecimal(num2)).doubleValue();
	}

	/**
	 * double类的减法(num1-num2)
	 * 
	 * @param num1
	 *            double 类型
	 * @param num2
	 *            double 类型
	 * @return double 类型
	 */
	public Double doubleSubtractDouble(Double num1, Double num2)
	{
		if(null == num1)
		{
			num1 = 0.00;
		}
		if(null == num2)
		{
			num2 = 0.00;
		}
		return new BigDecimal(num1).subtract(new BigDecimal(num2)).doubleValue();
	}

	/**
	 * double类的乘法(num1*num2)
	 * 
	 * @param num1
	 *            double 类型
	 * @param num2
	 *            double 类型
	 * @return double 类型
	 */
	public Double doubleMultiplyDouble(Double num1, Double num2)
	{
		if(null == num1)
		{
			num1 = 0.00;
		}
		if(null == num2)
		{
			num2 = 0.00;
		}
		return new BigDecimal(num1).multiply(new BigDecimal(num2)).doubleValue();
	}

	/**
	 * double类的除法(num1/num2)
	 * 
	 * @param num1
	 *            double 类型
	 * @param num2
	 *            double 类型
	 * @return double 类型
	 */
	public Double doubleDivideDouble(Double num1, Double num2)
	{
		if(null == num1)
		{
			num1 = 0.00;
		}
		if(null == num2)
		{
			num2 = 0.00;
		}
		return new BigDecimal(num1).divide(new BigDecimal(num2), 4).doubleValue();
	}

	/**
	 * double转String(不使用科学计数法的String显示),截取db,保留flLeng位小数
	 * 
	 * @param db
	 *            Double 类型
	 * @param flLeng
	 *            Integer 类型 保留小数位数
	 * @return String 类型
	 */
	public String doubleToString(Double db, Integer flLeng)
	{

		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		return nf.format(getShort(db, flLeng));
	}

	/**
	 * doubel 除法 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 小数位
	 * @return
	 */
	 
	public Double div(double v1, double v2, int scale)
	{
		if (scale < 0)
		{
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public String customDecimal(Double doubleValue,int scale){
		if(doubleValue==null){
			return "";
		}
		BigDecimal bigDecimal = new BigDecimal(doubleValue);
		BigDecimal bigDecimal1 = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bigDecimal1.toString();
	}

	public String twoDecimal(Double doubleValue){
		return customDecimal(doubleValue, 2);
	}

	public String noDecimal(Double doubleValue){
		return customDecimal(doubleValue, 0);
	}


	/**
	 * 格式化数字为千分位
	 * @param amount 金额
	 * @return String 设定文件
	 */
	public static String fmtMicrometer(Double amount) {
		if (amount == null)
		{
			return "0";
		}
		String text = MyString.getInstance().parse(amount);
 		DecimalFormat df = null;
		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1) {
				df = new DecimalFormat("###,##0.0");
			} else {
				df = new DecimalFormat("###,##0.00");
			}
		} else {
			df = new DecimalFormat("###,##0");
		}
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 小数转百分比（小数点后保留两位）
	 * @return
	 */
	public static  String  pointTOPercent (Double point, String type)
	{
		String result = "0.00%";
		if(point !=null && !point.isNaN())
		{
			DecimalFormat df = new DecimalFormat(type);
			result = df.format(point);
		}
		return  result;
	}

	/**
	 * 千分位（小数点后保留两位）不四舍五入
	 * @return
	 */
	public static  String  pointTOThousandths (Double point)
	{
		String result = "0.00";
		if(point !=null && !point.isNaN())
		{
			
			DecimalFormat format = new DecimalFormat("#.0000");
			String b = format.format(point);
			Double d = null;
			if(b.length() < b.indexOf(".")+3)
			{
				d = point;
			}
			else
			{
				String substring = b.substring(0,b.indexOf(".")+3);
				double parseDouble = Double.parseDouble(substring.equals("")?"0": substring);
				d = new Double(parseDouble);
			}
			
			DecimalFormat df=new DecimalFormat(",###,##0.00"); //保留两位小数
			result = df.format(d);
		}
		return  result;
	}
	
	 public static String doubleTrans(double d)
	 {
		if (Math.round(d) - d == 0)
		{
			return String.valueOf((long) d);
		}
		return String.valueOf(d);	 
	 }
	 
	 /**
	 * 千分位（小数点后保留两位）四舍五入
	 * @return
	 */
	public static  String  pointTOThousandths2 (Double point)
	{
		String result = "0.00";
		if(point !=null && !point.isNaN())
		{
			DecimalFormat df=new DecimalFormat(",###,##0.00"); //保留两位小数
			result = df.format(point);
		}
		return  result;
	}
	 
}

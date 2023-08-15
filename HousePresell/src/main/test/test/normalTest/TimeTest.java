package test.normalTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyInteger;

public class TimeTest {

	public static void main(String[] args) 
	{
		MyDatetime myDatetime = MyDatetime.getInstance();
		MyInteger myInteger = MyInteger.getInstance();
		String str = "2018-09";
//		System.out.println(myDatetime.stringToLong(str));
		
		Calendar calendarEnd = Calendar.getInstance();
		Calendar calendarStart = Calendar.getInstance();
		String[] timeArr = str.split("-");
		Integer theYear = myInteger.parse(timeArr[0]);
		Integer theMonth = myInteger.parse(timeArr[1]);
		calendarEnd.set(Calendar.YEAR, theYear);
		calendarEnd.set(Calendar.MONTH, theMonth-1);
		calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
		calendarEnd.set(Calendar.HOUR_OF_DAY, 0);
		calendarEnd.set(Calendar.MINUTE, 0);
		calendarEnd.set(Calendar.SECOND, 0);
		calendarEnd.set(Calendar.MILLISECOND, 0);
		
		calendarStart.set(Calendar.YEAR, theYear);
		calendarStart.set(Calendar.MONTH, theMonth-4);
		calendarStart.set(Calendar.DAY_OF_MONTH, 1);
		calendarStart.set(Calendar.HOUR_OF_DAY, 0);
		calendarStart.set(Calendar.MINUTE, 0);
		calendarStart.set(Calendar.SECOND, 0);
		calendarStart.set(Calendar.MILLISECOND, 0);
		Long endDateStamp = calendarEnd.getTimeInMillis();
		Long startDateStamp = calendarStart.getTimeInMillis();

		System.out.println(endDateStamp);
		System.out.println(startDateStamp);
	}
}

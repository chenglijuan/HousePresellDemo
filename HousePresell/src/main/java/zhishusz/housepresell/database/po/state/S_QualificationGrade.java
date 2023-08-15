package zhishusz.housepresell.database.po.state;

import java.util.HashMap;

//资质等级
public class S_QualificationGrade
{
	public static final String Grade01 = "1";//01-壹级
	public static final String Grade02 = "2";//02-贰级
	public static final String Grade03 = "3";//03-叁级
	public static final String Grade04 = "4";//04-肆级
	public static final String Grade05 = "5";//05-暂壹级
	public static final String Grade06 = "6";//06-暂贰级
	public static final String Grade07 = "7";//07-暂叁级
	public static final String Grade99 = "99";//99-其他

	public static final HashMap<String,String> QUALIFICATION_GRADE_MAP=new HashMap<>();
	static {
		QUALIFICATION_GRADE_MAP.put(Grade01, "01-壹级");
		QUALIFICATION_GRADE_MAP.put(Grade02, "02-贰级");
		QUALIFICATION_GRADE_MAP.put(Grade03, "03-叁级");
		QUALIFICATION_GRADE_MAP.put(Grade04, "04-肆级");
		QUALIFICATION_GRADE_MAP.put(Grade05, "05-暂壹级");
		QUALIFICATION_GRADE_MAP.put(Grade06, "06-暂贰级");
		QUALIFICATION_GRADE_MAP.put(Grade07, "07-暂叁级");
		QUALIFICATION_GRADE_MAP.put(Grade99, "99-其他");
	}
}

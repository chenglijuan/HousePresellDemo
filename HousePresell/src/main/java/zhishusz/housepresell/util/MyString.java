package zhishusz.housepresell.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MyString
{
	private static MyString instance;
	private MyString()
	{
		
	}
	
	public static MyString getInstance()
	{
		if(instance == null) instance = new MyString();
		
		return instance;
	}
	
	public String parse(Object object)
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
			return object.toString();
		}
	}
	
	/**
	 * 专用于导出Excel所需，若为空，则返回空字符串即可。
	 */
	public String parseForExport(Object object)
	{
		if (object == null)
		{
			return "";
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
				return "";
			}
		}
		else
		{
			return object.toString();
		}
	}

	/**
	 *
	 * 验证密码
	 * 由6-20位数字、字母、一般符号组成的
	 * @param password 需要验证的密码
	 * @return true：密码格式通过，false：密码格式不正确
	 */
	public boolean validateOfPassword(String password){
		String regex="[!@#$%\\^&*()/\\+\\[\\]\\{\\}, .?;:<>|\\w]{6,20}";
		boolean result= Pattern.matches(regex, password);
		return result;
	}

	public String firstUpcase(String string){
		String head = string.substring(0, 1).toUpperCase();
		String tail = string.substring(1, string.length());
		return head+tail;
	}

	public String firstLowcase(String string){
		String head = string.substring(0, 1).toLowerCase();
		String tail = string.substring(1, string.length());
		return head+tail;
	}
	
	public Boolean checkNumber(String num,Integer scaleMin,Integer scaleMax)
	{
		if(num == null || num.length() == 0) return false;
		
		String format = "^(\\d+)$"; 
		
		//整数或至少X位
		if(scaleMin != null && scaleMin > 0 && scaleMax == null)
		{
			format = "^(\\d+)(\\.(\\d){"+scaleMin+",})?$";
		}
		//整数或至多X位
		else if(scaleMin == null && scaleMax != null && scaleMax > 0)
		{
			format = "^(\\d+)(\\.(\\d){0,"+scaleMax+"})?$";
		}
		//整数或X~Y位
		else if(scaleMin != null && scaleMin >0 && scaleMax != null && scaleMax > 0)
		{
			format = "^(\\d+)(\\.(\\d){"+scaleMin+","+scaleMax+"})?$";
		}
		Pattern emailPattern = Pattern.compile(format);
		
		if(!emailPattern.matcher(num).matches())
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
	
	//将数组转成 [1,2] -> (1,2)
  	public String getSqlIdIn(Object[] arr)
  	{
  		if(arr == null || arr.length == 0) 
  		return null;
  		
  		String result = "(";
  		for (int i =0;i<arr.length;i++)
  		{
  			if(i == arr.length-1)
  			{
  				result += arr[i]+")";
  			}
  			else
  			{
  				result += arr[i]+",";
  			}
  		}
  		return result;
  	}
  	
  	public Boolean checkPhoneNumber(String phoneNumber)
	{
		if(phoneNumber == null || phoneNumber.length() == 0) return false;
		Pattern phonePattern = Pattern.compile("^(0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8})|(400|800)([0-9\\-]{7,10})|(([0-9]{4}|[0-9]{3})(-| )?)?([0-9]{7,8})((-| |转)*([0-9]{1,4}))?$");
		if(!phonePattern.matcher(phoneNumber).find())
		{
			return false;
		}
		else 
		{
			return true;
		}
	}

	public Boolean checkIdNumber(String idNumber)
	{
		if(idNumber == null || idNumber.length() == 0) return false;
		Pattern phonePattern = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
		if(!phonePattern.matcher(idNumber).find())
		{
			return false;
		}
		else 
		{
			return true;
		}
	}

	public Boolean checkEmailNumber(String email)
	{
		if(email == null || email.length() == 0) return false;
		Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$");
		
		if(!emailPattern.matcher(email).find())
		{
			return false;
		}
		else 
		{
			return true;
		}
	}

	/**
	 * 座机号格式判断
	 * @param fixedNumber
	 * @return
	 */
	public Boolean checkFixedNumber(String fixedNumber)
	{
		if(fixedNumber == null || fixedNumber.length() == 0) return false;
		Pattern phonePattern = Pattern.compile("^(\\d{3}(-)?\\d{8})|(\\d{4}(-)?\\d{8})$");
		if(!phonePattern.matcher(fixedNumber).matches())
		{
			return false;
		}
		else
		{
			return true;
		}
	}


	public Boolean isValid(String businessCode) {
		if ((businessCode.equals("")) || businessCode.length() != 18) {
			return false;
		}
		String baseCode = "0123456789ABCDEFGHJKLMNPQRTUWXY";
		char[] baseCodeArray = baseCode.toCharArray();
		Map<Character, Integer> codes = new HashMap<Character, Integer>();
		for (int i = 0; i < baseCode.length(); i++) {
			codes.put(baseCodeArray[i], i);
		}
		char[] businessCodeArray = businessCode.toCharArray();
		Character check = businessCodeArray[17];
		if (baseCode.indexOf(check) == -1) {
			return false;
		}
		int[] wi = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			Character key = businessCodeArray[i];
			if (baseCode.indexOf(key) == -1) {
				return false;
			}
			sum += (codes.get(key) * wi[i]);
		}
		int value = 31 - sum % 31;
		return value == codes.get(check);
	}

	public static String sqlInject(String str) {

//        if (StringUtils.isBlank(str)) {
//            return str;
//        }
		if (null == str) {
			return null;
		}
		// 去掉'|"|;|\字符
		str = str.replaceAll("<", "＜");
		str = str.replaceAll(">", "＞");
		str = str.replaceAll("'", "＇");
		str = str.replaceAll(";", "﹔");
		str = str.replaceAll("&", "＆");
		str = str.replaceAll("%", "﹪");
		str = str.replaceAll("#", "＃");
		str = str.replaceAll("sleep", " ");
		str = str.replaceAll("select", "seleᴄt");// "c"→"ᴄ"
		str = str.replaceAll("prompt", " ");

		str = str.replaceAll("truncate", "trunᴄate");// "c"→"ᴄ"
		str = str.replaceAll("exec", "exeᴄ");// "c"→"ᴄ"
        str = str.replaceAll("join", "jᴏin");// "o"→"ᴏ"
		str = str.replaceAll("union", "uniᴏn");// "o"→"ᴏ"
		str = str.replaceAll("drop", "drᴏp");// "o"→"ᴏ"
		str = str.replaceAll("alert", "");// "o"→"ᴏ"
        str = str.replaceAll("count", "cᴏunt");// "o"→"ᴏ"
		str = str.replaceAll("insert", "ins℮rt");// "e"→"℮"
        str = str.replaceAll("update", "updat℮");// "e"→"℮"
		str = str.replaceAll("delete", "delet℮");// "e"→"℮"
		str = str.replaceAll("script", "sᴄript");// "c"→"ᴄ"
        str = str.replaceAll("cookie", "cᴏᴏkie");// "o"→"ᴏ"
		str = str.replaceAll("iframe", "ifram℮");// "e"→"℮"
		str = str.replaceAll("onmouseover", "onmouseov℮r");// "e"→"℮"
		str = str.replaceAll("onmousemove", "onmousemov℮");
		str = str + "";

		return str;
	}

}

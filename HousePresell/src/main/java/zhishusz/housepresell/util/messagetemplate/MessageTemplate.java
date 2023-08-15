package zhishusz.housepresell.util.messagetemplate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.MyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageTemplate {
	/**
	 * 消息模板解析
	 * @param templateObject
	 * @param templateString
	 * @return
	 */
	public static String messageTemplateString(Map<String,Object > templateObject, String templateString){
//		String regex = "\\{([\\w]+)\\}";
		String regex = "\\{[^}]+}";
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(templateString);
		List<String> keyArrList = new ArrayList<String>();
		List<String> valArrList = new ArrayList<String>();
		while(matcher.find())
		{
			String keyStr = matcher.group();
			String keyStr1 = keyStr.substring(1,matcher.group().length()-1);
			String value;
			if(keyStr == null) continue;
			if(!keyArrList.contains(keyStr1))
			{
				keyArrList.add(keyStr1);
			}
			if(getValue(templateObject,keyStr1) == null)
			{
				value = keyStr;
			}
			else
			{
				value = MyString.getInstance().parse(getValue(templateObject,keyStr1));
			}

			valArrList.add(value);
		}

		//用户自定义的模板 消息标题
		for(String keyStr_Temp : keyArrList)
		{
			templateString = templateString.replaceAll("\\{"+keyStr_Temp+"\\}", "{}");
		}

		String templateFinal = StrUtil.format(templateString, valArrList.toArray(new String[valArrList.size()]));
		return templateFinal;
	}


	/**
	 * 条件表达式解析
	 * @param templateObject
	 * @param templateString
	 * @return
	 */
	public static String judgeTemplateString(Map<String,Object > templateObject, String templateString){

		//---------------------------找出{content}替换成{}-----------------------------//
		String regex = "\\{[^}]+}";
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(templateString);
		List<String> keyArrList = new ArrayList<String>();
		List<String> valArrList = new ArrayList<String>();
		while(matcher.find())
		{
			String keyStr = matcher.group().substring(1,matcher.group().length()-1);
			if(keyStr == null) continue;
			if(!keyArrList.contains(keyStr))
			{
				keyArrList.add(keyStr);
			}
			String value = MyString.getInstance().parse(getValue(templateObject,keyStr));
			if(getValue(templateObject,keyStr) instanceof  String)
			{
				char[] valueChar=value.toCharArray();
				String valueResult="";
				for(int i=0;i<valueChar.length;i++){
					valueResult +=Integer.toBinaryString(valueChar[i])+ " ";
				}
				value = valueResult;
			}
			valArrList.add(value);
		}

		//用户自定义的模板 消息标题
		for(String keyStr_Temp : keyArrList)
		{
			templateString = templateString.replaceAll("\\{"+keyStr_Temp+"\\}", "{}");
		}

		String templateFinal = StrUtil.format(templateString, valArrList.toArray(new String[valArrList.size()]));
		//---------------------------找出{content}替换成{}-----------------------------//

		//---------------------------找出[]替换成{}-----------------------------//
		String regex2 = "(\\[[^\\]]*\\])";
		Pattern pattern2 =  Pattern.compile(regex2);
		Matcher matcher2 = pattern2.matcher(templateFinal);
		List<String> keyArrList2 = new ArrayList<String>();
		List<String> valArrList2 = new ArrayList<String>();

		while(matcher2.find())
		{
			String keyStr2 = matcher2.group().substring(1,matcher2.group().length()-1);
			if(keyStr2 == null) continue;
			if(!keyArrList2.contains(keyStr2))
			{
				keyArrList2.add(keyStr2);
			}
			//-------------将字符串转换成二进制---------------------//
			char[] valueChar2= keyStr2.toCharArray();
			String valueResult2="";
			for(int i=0;i<valueChar2.length;i++){
				valueResult2 +=Integer.toBinaryString(valueChar2[i])+ " ";
			}
			//-------------将字符串转换成二进制---------------------//
			valArrList2.add(valueResult2);
		}
		//用户自定义的模板{}
		for(String keyStr_Temp2 : keyArrList2)
		{
			templateFinal = templateFinal.replaceAll("\\["+keyStr_Temp2+"\\]", "{}");
		}
		templateFinal = StrUtil.format(templateFinal, valArrList2.toArray(new String[valArrList2.size()]));

		//--------------------------------找出[]替换成{}----------------------------//
		return templateFinal;
	}

	/**
	 * 获取po中属性内容
	 * @param templateObject
	 * @param key
	 * @return
	 */
	public static Object getValue(Map<String, Object> templateObject, String key)
	{
		Object value = null;
		for(Map.Entry entry : templateObject.entrySet())
		{
			String poKey = MyString.getInstance().parse(entry.getKey());
			if(key.indexOf(".")!= -1)
			{
				String[] getKeyArray = key.split("\\.");
				if(getKeyArray[0].equals(poKey))
				{
					value = entry.getValue();
					for (int index = 1; index < getKeyArray.length; index++)
					{
						value = getValue2(value , getKeyArray[index]);
						if(value == null) break;
					}
				}
			}
		}
		return value;
	}

	public static Object getValue2(Object getObj , String getKeyName )
	{
		Object newValue = null;

		Map<String, Object> queryKeyMap = BeanUtil.beanToMap(getObj);
		for (String queryKey : queryKeyMap.keySet())
		{
			if(queryKey.equals(getKeyName))
			{
				newValue = queryKeyMap.get(queryKey);
				break;
			}
		}
		return  newValue;
	}

	/**
	 * 根据审批操作截取发送的内容
	 * @param lastAction
	 * @param theContent
	 * @return
	 */
	public static String getContent(Integer lastAction ,String theContent)
	{
		String newContent = "";
		theContent = theContent.replaceAll("\n","");
		theContent = theContent.replaceAll(" ","");
		for (String splitContent : theContent.split("\\;"))
		{
			String[] theActionAndContent = splitContent.split("\\:");
			Integer theAction = MyInteger.getInstance().parse(theActionAndContent[0]);
			if(theAction!=null && theAction.equals(lastAction))
			{
				newContent = theActionAndContent[1];
				break;
			}
		}
		return newContent;
	}
}

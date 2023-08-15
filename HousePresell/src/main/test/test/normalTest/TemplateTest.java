package test.normalTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.StrUtil;

public class TemplateTest 
{
	public static void main(String[] args) 
	{
		/**
		 * 1、传PO
		 * 2、该Po下所有可能的字段key：例如：["theName","theAge"]
		 * 3、模板信息，例如："我叫{theName}，我今年{theAge}岁了"
		 */
		String str = "我叫{theName}，我今年{theAge}岁了";
		String regex = "\\{([\\w]+)\\}"; 
		Pattern p =  Pattern.compile(regex);
		Matcher m = p.matcher(str);
		
		//模拟从数据库获取到的数据
		Map<String,String> keyValMap = new HashMap<String, String>();
		keyValMap.put("theName", "张三");
		keyValMap.put("theAge", "3");
		
		List<String> keyArrList = new ArrayList<String>();
		List<String> valArrList = new ArrayList<String>();
		while(m.find())
		{
			//System.out.println(m.group());
			String keyStr = m.group().replaceAll(regex, "$1");
			//System.out.println(m.group().replaceAll(regex, "$1"));

			if(keyStr == null) continue;
			if(!keyArrList.contains(keyStr))
			{
				keyArrList.add(keyStr);
			}
			
			String value = keyValMap.get(keyStr);
			valArrList.add(value);
		}
		
		//用户自定义的模板
		for(String keyStr_Temp : keyArrList)
		{
			str = str.replaceAll("\\{"+keyStr_Temp+"\\}", "{}");
		}
		
		String template = str;
		String templateFinal = StrUtil.format(template, valArrList.toArray(new String[valArrList.size()])); //str -> 智奇胜用款申请200000元
		System.out.println(templateFinal);
	}
}

package zhishusz.housepresell.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class GetPinYinUtil
{
	private static GetPinYinUtil instance;
	private GetPinYinUtil()
	{
		
	}
	
	public static GetPinYinUtil getInstance()
	{
		if(instance == null) instance = new GetPinYinUtil();
		
		return instance;
	}
	
	public String getPinYinShort(String content,Boolean isUpcase)
	{
		if(content == null)
		{
			return null;
		}
		else if(content.length()==0)
		{
			return "";
		}
		else
		{
			StringBuffer stringBuffer = new StringBuffer();
			for(int i =0;i<content.length();i++)
			{
				stringBuffer.append(getFirstLetter(content.charAt(i)));
			}
			if(isUpcase ==null || isUpcase)
			{
				return stringBuffer.toString().toUpperCase();
			}
			return stringBuffer.toString().toLowerCase();
		}
	}
	public String getPinYinShort(String content,Boolean isUpcase,Integer index)
	{
		if(content == null)
		{
			return null;
		}
		else if(content.length()==0)
		{
			return "";
		}
		else
		{
			StringBuffer stringBuffer = new StringBuffer();
			for(int i =0;i<content.length();i++)
			{
				stringBuffer.append(getFirstLetter(content.charAt(i),index));
			}
			if(isUpcase ==null || isUpcase)
			{
				return stringBuffer.toString().toUpperCase();
			}
			return stringBuffer.toString().toLowerCase();
		}
	}
	public String getFirstLetter(String content,Boolean isUpcase,Integer index)
	{
		if(content == null)
		{
			return null;
		}
		else if(content.length()==0)
		{
			return "";
		}
		else
		{
			if(isUpcase ==null || isUpcase)
			{
				return getFirstLetter(content.charAt(0),index).toUpperCase();
			}
			return getFirstLetter(content.charAt(0),index).toLowerCase();
		}
		
	}
	public String getFirstLetter(String content,Boolean isUpcase)
	{
		if(content == null)
		{
			return null;
		}
		else if(content.length()==0)
		{
			return "";
		}
		else
		{
			if(isUpcase ==null || isUpcase)
			{
				return getFirstLetter(content.charAt(0)).toUpperCase();
			}
			return getFirstLetter(content.charAt(0)).toLowerCase();
		}
		
	}
	public String getFirstLetter(char cr)
	{
		if(Character.toString(cr).matches("[\\u4E00-\\u9FA5]"))
		{
			String pinyin ="";
			String[] sunpinyinArr = PinyinHelper.toHanyuPinyinStringArray(cr);
			if(sunpinyinArr!= null && sunpinyinArr.length>0)
			{
				if(sunpinyinArr[0]!= null && sunpinyinArr.length>0)
				{
					pinyin = Character.toString(sunpinyinArr[0].charAt(0));
				}
			}
			return pinyin;
		}
		else
		{
			return Character.toString(cr);
		}
	}
	public String getFirstLetter(char cr,Integer index)
	{
		//cr 获取拼音的汉字，index 多音字取值
		if(Character.toString(cr).matches("[\\u4E00-\\u9FA5]"))
		{
			String pinyin ="";
			String[] sunpinyinArr = PinyinHelper.toHanyuPinyinStringArray(cr);
			if(sunpinyinArr!= null && sunpinyinArr.length>0)
			{
				int ind;
				if(index == null || index <0)
				{
					ind = 0;
				}
				else
				{
					ind = index%sunpinyinArr.length;
				}
				if(sunpinyinArr[ind]!= null && sunpinyinArr.length>0)
				{
					pinyin = Character.toString(sunpinyinArr[ind].charAt(0));
				}
			}
			return pinyin;
		}
		else
		{
			return Character.toString(cr);
		}
	}
}

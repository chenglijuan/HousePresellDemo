package zhishusz.housepresell.util;

public class MyInteger
{
	private static MyInteger instance;
	private MyInteger()
	{
		
	}
	
	public static MyInteger getInstance()
	{
		if(instance == null) instance = new MyInteger();
		
		return instance;
	}
	
	public Integer parse(String str)
	{
		Integer result = null;
		if (str != null && str.length() > 0)
		{
			try
			{
				result = Integer.parseInt(str);
			}
			catch (NumberFormatException e)
			{
				result = null;
			}
		}
		return result;
	}

	public Integer parse(Object object)
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
}

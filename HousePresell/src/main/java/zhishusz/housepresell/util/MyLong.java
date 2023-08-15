package zhishusz.housepresell.util;

public class MyLong
{
	private static MyLong instance;
	private MyLong()
	{
		
	}
	
	public static MyLong getInstance()
	{
		if(instance == null) instance = new MyLong();
		
		return instance;
	}
	
	public Long parse(String str)
	{
		Long result = null;
		if (str != null && str.length() > 0)
		{
			try
			{
				result = Long.parseLong(str);
			}
			catch (NumberFormatException e)
			{
				result = null;
			}
		}
		return result;
	}

	public Long parse(Object object)
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

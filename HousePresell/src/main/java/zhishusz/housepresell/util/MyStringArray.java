package zhishusz.housepresell.util;

public class MyStringArray
{
	private static MyStringArray instance;
	private MyStringArray()
	{
		
	}
	
	public static MyStringArray getInstance()
	{
		if(instance == null) instance = new MyStringArray();
		
		return instance;
	}
	
	public String[] parse(Object object)
	{
		if (object == null)
		{
			return null;
		}
		else if (object instanceof String[])
		{
			return (String[]) object;
		}
		else
		{
			return null;
		}
	}
}

package zhishusz.housepresell.util;

public class MyLongArray
{
	private static MyLongArray instance;
	private MyLongArray()
	{
		
	}
	
	public static MyLongArray getInstance()
	{
		if(instance == null) instance = new MyLongArray();
		
		return instance;
	}
	
	public Long[] parse(Object object)
	{
		if (object == null)
		{
			return null;
		}
		else if (object instanceof Long[])
		{
			return (Long[]) object;
		}
		else
		{
			return null;
		}
	}
}

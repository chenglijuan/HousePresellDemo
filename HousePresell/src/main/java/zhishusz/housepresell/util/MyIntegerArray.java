package zhishusz.housepresell.util;

public class MyIntegerArray
{
	private static MyIntegerArray instance;
	private MyIntegerArray()
	{
		
	}
	
	public static MyIntegerArray getInstance()
	{
		if(instance == null) instance = new MyIntegerArray();
		
		return instance;
	}
	
	public Integer[] parse(Object object)
	{
		if (object == null)
		{
			return null;
		}
		else if (object instanceof Integer[])
		{
			return (Integer[]) object;
		}
		else
		{
			return null;
		}
	}
}

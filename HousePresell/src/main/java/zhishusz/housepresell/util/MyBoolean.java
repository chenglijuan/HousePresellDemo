package zhishusz.housepresell.util;

public class MyBoolean {
	private static MyBoolean instance;
	private MyBoolean()
	{
		
	}
	
	public static MyBoolean getInstance()
	{
		if(instance == null) instance = new MyBoolean();
		
		return instance;
	}

	public Boolean parse(String str)
	{
		Boolean result = false;
		if(str != null && str.length() > 0)
		{
			try{
				result = Boolean.parseBoolean(str);
			}
			catch(NumberFormatException e)
			{
				result = false;
			}
		}
		return result;
	}

	public Boolean parse(Object object)
	{
		if(object == null) return false;
		else if(object instanceof String[])
		{
			Boolean[] objArr = (Boolean[])object;
			if(objArr.length > 0) return parse(objArr[0]);
			else return false;
		}
		else return parse(object.toString());
	}
}


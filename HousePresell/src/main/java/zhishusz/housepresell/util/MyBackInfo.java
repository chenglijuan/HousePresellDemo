package zhishusz.housepresell.util;

import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_NormalFlag;

public class MyBackInfo
{
	public static Properties fail(Properties properties, String info)
	{
		properties.put(S_NormalFlag.result, S_NormalFlag.fail);
		properties.put(S_NormalFlag.info, info);

		return properties;
	}
	public static Boolean isSuccess(Properties properties)
	{
		if(properties == null) return false;
		if(S_NormalFlag.success.equals(properties.get(S_NormalFlag.result)))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public static Boolean isFail(Properties properties)
	{
		if(properties == null) return true;
		if(S_NormalFlag.fail.equals(properties.get(S_NormalFlag.result)))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
}

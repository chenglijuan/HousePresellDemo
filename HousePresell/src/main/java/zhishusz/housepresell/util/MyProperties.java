package zhishusz.housepresell.util;

import java.util.Properties;

public class MyProperties extends Properties
{
	private static final long serialVersionUID = 3529685984235262890L;

	public Object put(Object key, Object value)
	{
		if (key != null && value != null)
		{
			return super.put(key, value);
		}
		else
		{
			return this;
		}
	}
}

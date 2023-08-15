package zhishusz.housepresell.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 解决异常：无法在过滤器中修改参数
 * Caused by: java.lang.IllegalStateException: No modifications are allowed to a
 * locked ParameterMap
 * 
 */
public class WrapperRequest extends HttpServletRequestWrapper
{
	private Map<String, String[]> requestParams = null;

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public WrapperRequest(final ServletRequest request, HashMap newMap)
	{
		super((HttpServletRequest) request);
		this.requestParams = newMap;
	}

	@Override
	public String getParameter(final String name)
	{
		if (requestParams.get(name) != null)
		{
			return requestParams.get(name)[0];
		}
		else
		{
			return null;
		}
	}

	@Override
	public Map<String, String[]> getParameterMap()
	{
		if (requestParams == null)
		{
			requestParams = new HashMap<String, String[]>();
			requestParams.putAll(super.getParameterMap());
		}
		return Collections.unmodifiableMap(requestParams);
	}

	@Override
	public Enumeration<String> getParameterNames()
	{
		return Collections.enumeration(getParameterMap().keySet());
	}

	@Override
	public String[] getParameterValues(final String name)
	{
		return getParameterMap().get(name);
	}
}

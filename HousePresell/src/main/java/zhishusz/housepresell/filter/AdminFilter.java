package zhishusz.housepresell.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;


//管理员过滤器
public class AdminFilter extends OncePerRequestFilter
{
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		//Admin Admin = (Admin)request.getSession().getAttribute(S_NormalFlag.admin);
		StringBuffer serverBasePath = new StringBuffer();
		if(80 == request.getServerPort())
    	{
    		serverBasePath.append(request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/");
    	}
    	else
    	{
    		serverBasePath.append(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
    	}
    	//System.out.println(serverBasePath);
    	
		Long logInUserId = (Long)request.getSession().getAttribute(S_NormalFlag.userId);
		
    	//替换原request，追加adminId adminCityId adminCityName isSuperAdmin参入到请求中
		HashMap parameterMap = new HashMap();
		parameterMap.putAll(request.getParameterMap());
		parameterMap.put(S_NormalFlag.serverBasePath, new String[]{MyString.getInstance().parse(serverBasePath.toString())});
		//替换原request，追加adminId adminCityId adminCityName isSuperAdmin参入到请求中
		parameterMap.putAll(request.getParameterMap());
		parameterMap.put(S_NormalFlag.userId, new String[]{MyString.getInstance().parse(logInUserId)});
		
		WrapperRequest wrapperRequest = new WrapperRequest(request,parameterMap);
		filterChain.doFilter(wrapperRequest, response);
	}
	
	public void returnError(String info, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.fail);
		properties.put(S_NormalFlag.info, info);
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		request.setAttribute(S_NormalFlag.info, jsonStr);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsonResult.jsp");
		dispatcher.forward(request, response);
	}
}

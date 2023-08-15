package zhishusz.housepresell.servlet.agent;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DelegatingServletProxy extends GenericServlet
{
	private static final long serialVersionUID = -1196078350884535653L;
	
	private String targetBean;
	private Servlet proxy;

	//用来注入需spring管理的代理类
	
	public void init() throws ServletException
	{
		//this.targetBean = getInitParameter("targetBean");
		this.targetBean = this.getServletName();
		this.getServletBean();
		proxy.init(getServletConfig());
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
	{
		proxy.service(req, res);
	}

	private void getServletBean()
	{
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.proxy = (Servlet) wac.getBean(targetBean);
	}
}

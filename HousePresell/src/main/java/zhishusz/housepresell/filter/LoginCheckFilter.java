package zhishusz.housepresell.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/**
 * xsz by time 2018-9-14 16:27:56
 * 登录状态过滤
 *
 * @ClassName: LoginCheckFilter
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月14日 下午4:27:52
 * @version V1.0
 *
 */
public class LoginCheckFilter extends OncePerRequestFilter
{

	@Autowired
	private Sm_UserDao sm_UserDao;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		// 不过滤的uri
		String[] notFilter = new String[] {
				"login.shtml", ".js", ".css",".png",".ico", "webService/panyunDingdingWebService",
				"webService/panyunDingdingSignWebService",
				"Outside_ProjProgForcast_ApprovalFeedback","bankUpload","bankFeedBack",
				"Outside_ProjProgForcast_ApprovalFeedback3","answerSfXy1","answerSfXy",
				"Gjj","TEST_dingshiqi","PushApprovalInfo", "uploadBalanceOfAccount","Sm_SignatureUploadForPath",
				"/getXybhByBuildingId","/getXyContractInfo","/getCxContractInfo"
		};

		// 请求的uri
		String uri = request.getRequestURI();

		String url = request.getRequestURL().toString();

		// uri中包含admin时才进行过滤
		if (uri.indexOf("admin") != -1)
		{

			// 是否过滤
			boolean doFilter = true;
			for (String s : notFilter)
			{
				if (uri.indexOf(s) != -1)
				{
					// 如果uri中包含不过滤的uri，则不进行过滤
					doFilter = false;
					break;
				}
			}

			if (doFilter)
			{
				/*
				 * 执行过滤
				 * 从session中获取登录信息
				 */
				Object obj = request.getSession().getAttribute("user");
				Object userId = request.getSession().getAttribute("userId");

				System.out.println("---------------sm_UserDao:"+sm_UserDao);
				System.out.println("--------------userId:"+userId);

				


				if (null == obj && null == userId)
				{
					/*
					 * 如果session中不存在登录者实体，则弹出框提示重新登录
					 * 设置request和response的字符集，防止乱码
					 */
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");

					PrintWriter out = response.getWriter();
					response.setContentType("text/html;charset=utf-8");
					String loginPage = request.getScheme() + "://" + request.getServerName() + ":"
							+ request.getServerPort() + request.getContextPath() + "/admin/login.shtml";
					// response.sendRedirect(loginPage);
					// request.getRequestDispatcher("/aadmin/login.shtml").forward(request,
					// response);
					out.println("<script language='javascript' type='text/javascript'>");
					out.println("window.top.location.href='" + loginPage + "'");
					out.println("</script>");

					/*
					 * StringBuilder builder = new StringBuilder();
					 * builder.append("<script type=\"text/javascript\">");
					 */
					// builder.append("alert('请先进行登录');");
					/*
					 * builder.append("window.top.location.href='");
					 * builder.append(loginPage);
					 * builder.append("';");
					 * builder.append("</script>");
					 * System.out.println(builder.toString());
					 * out.print(builder.toString());
					 */

				}
				else
				{
					// 如果session中存在登录者实体，则继续
					filterChain.doFilter(request, response);
				}
			}
			else
			{
				// 如果不执行过滤，则继续
				filterChain.doFilter(request, response);
			}
		}
		else
		{

			boolean isCheck = Boolean.TRUE;

			LinkedList<String> requestMapList = new LinkedList<String>();
			requestMapList.add("login.shtml");
			requestMapList.add(".js");
			requestMapList.add(".css");
			requestMapList.add(".png");
			requestMapList.add(".ico");

			requestMapList.add("webService/panyunDingdingWebService");
			requestMapList.add("webService/panyunDingdingSignWebService");
			requestMapList.add("172.18.5.190");
			requestMapList.add("172.18.7.2");
			requestMapList.add("Outside_ProjProgForcast_ApprovalFeedback");
			requestMapList.add("bankUpload");
			requestMapList.add("Sm_UserLogin");
			requestMapList.add("Outside_ProjProgForcast_ApprovalFeedback3");
			requestMapList.add("answerSfXy1");
			requestMapList.add("answerSfXy");
			requestMapList.add("bankFeedBack");
			requestMapList.add("Gjj");
			requestMapList.add("TEST_dingshiqi");
			requestMapList.add("PushApprovalInfo");
			requestMapList.add("uploadBalanceOfAccount");
			requestMapList.add("Sm_SignatureUploadForPath");
			requestMapList.add("getXybhByBuildingId");
			requestMapList.add("getXyContractInfo");
			requestMapList.add("getCxContractInfo");

			for (String requestUrl : requestMapList)
			{
				if (uri.contains(requestUrl) || url.contains(requestUrl))
				{
					isCheck = Boolean.FALSE;
				}
			}

			if (isCheck)
			{
				/*
				 * 执行过滤
				 * 从session中获取登录信息
				 */
				Object obj = request.getSession().getAttribute("user");
				Object userId = request.getSession().getAttribute("userId");

				if (null == obj && null == userId)
				{
					/*
					 * 如果session中不存在登录者实体，则弹出框提示重新登录
					 * 设置request和response的字符集，防止乱码
					 */
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");

					PrintWriter out = response.getWriter();
					response.setContentType("text/html;charset=utf-8");
					String loginPage = request.getScheme() + "://" + request.getServerName() + ":"
							+ request.getServerPort() + request.getContextPath() + "/admin/login.shtml";
					out.println("<script language='javascript' type='text/javascript'>");
					out.println("window.top.location.href='" + loginPage + "'");
					out.println("</script>");

				}
				else
				{
					// 如果session中存在登录者实体，则继续
					filterChain.doFilter(request, response);
				}
			}
			else
			{
				// 如果session中存在登录者实体，则继续
				filterChain.doFilter(request, response);
			}
		}
	}

}


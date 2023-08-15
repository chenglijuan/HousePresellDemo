package zhishusz.housepresell.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * xss过滤
 * @author xlf
 * @email xlfbe696@gmail.com
 * @date 2017年4月19日 上午10:41:42
 */
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = ((HttpServletRequest) request).getRequestURI();
        // 不过滤的uri
        String[] notFilter = new String[] {
                ".shtml", ".js", ".css",".png",".html",".ico", "webService/panyunDingdingWebService",
                "webService/panyunDingdingSignWebService",
                "Outside_ProjProgForcast_ApprovalFeedback","bankUpload","bankFeedBack",
                "Outside_ProjProgForcast_ApprovalFeedback3","answerSfXy1","answerSfXy",
                "Gjj","TEST_dingshiqi","PushApprovalInfo","uploadBalanceOfAccount","/Sm_OrgUserList","/Sm_UserList",
                "/getXybhByBuildingId"
        };

        // 是否过滤
        boolean doFilter = true;
        for (String s : notFilter)
        {
            if (requestURI.indexOf(s) != -1)
            {
                // 如果uri中包含不过滤的uri，则不进行过滤
                doFilter = false;
                break;
            }
        }

        if (doFilter) {
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
                    (HttpServletRequest) request);
            chain.doFilter(xssRequest, response);
//            chain.doFilter(request, response);
        }else {
            chain.doFilter(request,response);
        }


//        String requestURI = ((HttpServletRequest) request).getRequestURI();
//        if (!requestURI.contains(".shtml") && !requestURI.contains(".js") && !requestURI.contains(".css") && !requestURI.contains(".html")){
//            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
//                    (HttpServletRequest) request);
//            chain.doFilter(xssRequest, response);
//        }else {
//            chain.doFilter(request,response);
//        }
    }

    @Override
    public void destroy() {
    }

}


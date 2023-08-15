package zhishusz.housepresell.aspect;


import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.defender.DefenderHandler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 角色授权数据自动过滤切面
 *
 * @author zhishusz
 */
@Component
@Aspect
public class RequestInterceptAspect {
    //	@Pointcut(
//		//角色授权数据自动过滤切点 （单个设置）
//		"execution(* zhishusz.housepresell.controller.Emmp_BankBranchAddController.*(..)) ||" +
//		"execution(* zhishusz.housepresell.controller.Emmp_BankBranchBatchDeleteController.*(..)) " +
//	"")
	
	private static String[] Aspects = {"Add","Update","Handler","exportPDFByWord","Tg_IncomeForecastList","Tg_ExpForecastList","Tg_IncomeExpDepositForecastList","Tgxy_TripleAgreementApprovalProcess","Empj_BldLimitAmountSubmit"};

    @Autowired
    private DefenderHandler defenderHandler;

    @Pointcut(
            //角色授权数据自动过滤切点 （批量设置）
        "!execution(* zhishusz.housepresell.controller.Tgxy_TripleAgreementDetailController.*(..))  &&  execution(* zhishusz.housepresell.controller..*(..)) " +
                    "")
    public void checkAspect() {
    }

    /**
     * 分权限管理员查看数据
     * 重用代码：
     * if(!model.getIsSuperAdmin())
     * {
     * //若不是超级管理员，则显示分权限的数据,反之，则显示所有
     * model.setCityId(model.getAdminCityId());
     * }
     *
     * @param pjp
     * @return
     */
    @Around("checkAspect()")
    private Object checkIsSuperAdmin(ProceedingJoinPoint pjp) {
        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttribute;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //从request中获取session
        HttpSession session = request.getSession();
        Sm_User loginUser = (Sm_User) session.getAttribute(S_NormalFlag.user);
        StringBuffer requestURL = request.getRequestURL();
//        System.out.println("requestURL is " + requestURL.toString());
        int indexOfSlash = requestURL.lastIndexOf("/");
        //获取执行方法的参数
        Object[] args = pjp.getArgs();
        String interfaceName = requestURL.substring(indexOfSlash + 1, requestURL.length());
        
        //校验是否需要控制唯一请求
        boolean checkAspectArr = checkAspectArr(interfaceName);
        
//        if(!(interfaceName.contains("Add") || interfaceName.contains("Update") || interfaceName.contains("Handler") || interfaceName.contains("exportPDFByWord") || interfaceName.contains("ApprovalProcess")
//                || interfaceName.equals("Tg_IncomeForecastList") || interfaceName.equals("Tg_ExpForecastList") || interfaceName.equals("Tg_IncomeExpDepositForecastList"))){
        if(!checkAspectArr){   
        	try {
               return pjp.proceed(args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                String jsonResult = defaultError(-1, interfaceName, null, throwable);
                return jsonResult;
            }
        }

        if (loginUser == null) {
            return execute(pjp, args, -1, interfaceName, null);
        } else {
            Long userId = loginUser.getTableId();
            DefenderHandler.DefenderMessage needExcludeAndAdd = defenderHandler.isNeedExcludeAndAdd(interfaceName, userId);
            if (!needExcludeAndAdd.isNeedExclude()) {
                return execute(pjp, args, needExcludeAndAdd.getTheIndex(), interfaceName, userId);
            } else {
                Properties properties = new MyProperties();
                properties.put(S_NormalFlag.result, S_NormalFlag.fail);
                properties.put(S_NormalFlag.info, "执行中，请勿连续点击");//S_NormalFlag.info_BusiError
                String jsonResult = new JsonUtil().propertiesToJson(properties);
                return jsonResult;
//            	return execute(pjp, args, needExcludeAndAdd.getTheIndex(), interfaceName, userId);
            }
        }
    }

    public Object execute(ProceedingJoinPoint pjp, Object[] args, int theIndex, String interfaceName, Long userId) {
        Object result = null;
        try {
            //将Form中cityId初始化为登录者所属城市ID
            BaseForm form = (BaseForm) args[0];
            //请求前
            result = pjp.proceed(args);
            //请求后
            System.out.println("请求结束");
            finishNode(theIndex, interfaceName, userId);
        } catch (Throwable e) {
            e.printStackTrace();
            String jsonResult = defaultError(theIndex, interfaceName, userId, e);
            return jsonResult;
        }

        return result;
    }

    private String defaultError(int theIndex, String interfaceName, Long userId, Throwable e) {
        finishNode(theIndex, interfaceName, userId);
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.fail);
        properties.put(S_NormalFlag.info, e.getMessage());//S_NormalFlag.info_BusiError
        return new JsonUtil().propertiesToJson(properties);
    }

    private void finishNode(int theIndex, String interfaceName, Long userId) {
        if (theIndex != -1 && userId != null) {
            defenderHandler.finishHandleNode(theIndex, interfaceName, userId);
        }
    }
    
    /**
     * 校验是否存在需要控制的请求
     * @return
     */
	private boolean checkAspectArr(String interfaceName){
    	for (String aspect : Aspects)
		{
			if(interfaceName.contains(aspect))
			{
				return true;
			}
		}
    	return false;
    }
}

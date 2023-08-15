package zhishusz.housepresell.aspect;

import java.util.Properties;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/**
 * 拦截异常
 * @author zhishusz
 */
@Component
@Aspect
public class ExceptionAspect
{
	@Pointcut("execution(* zhishusz.housepresell.controller..*(..))")
	public void aspect(){}
	
	@Around("aspect()")
	private Object execute(ProceedingJoinPoint pjp)
	{
		Object result = null;
		try
		{
			result = pjp.proceed();
		}
		catch (RoolBackException e)
		{
			Properties properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
			properties.put(S_NormalFlag.debugInfo, e.getMessage());
			String jsonResult = new JsonUtil().propertiesToJson(properties);
			return jsonResult;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			Properties properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);//S_NormalFlag.info_BusiError
			properties.put(S_NormalFlag.debugInfo, e.getMessage());
			String jsonResult = new JsonUtil().propertiesToJson(properties);
			return jsonResult;
		}
		
		return result;
	}
}

package zhishusz.housepresell.approvalprocess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

@Service
@Transactional
public class ApprovalProcessCallbackService
{
	@Resource(name="approvalProcessCallBackMap")
	private Map<String , Object> approvalProcessCallBackMap;
	
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow, BaseForm model)
	{
		Properties properties = null;
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();

		try
		{
			String busiCode = sm_ApprovalProcess_AF.getBusiCode();

			Object service = this.approvalProcessCallBackMap.get(busiCode);
			
			if(service != null)
			{
				Method method = service.getClass().getMethod("execute", Sm_ApprovalProcess_Workflow.class, BaseForm.class);
				if(method != null)
				{
					properties = (Properties)method.invoke(service, approvalProcess_Workflow, model);
					if(properties.get(S_NormalFlag.result) == null)
					{
						properties.put(S_NormalFlag.result, S_NormalFlag.success);
						properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
					}
				}
				else
				{
					properties = new MyProperties();
					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, "没有需要处理的回调");
				}
			}
			else
			{
				properties = new MyProperties();
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		return properties;
	}
}

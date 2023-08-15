package zhishusz.housepresell.service;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.messagetemplate.MessageTemplate;
import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 审批，节点条件表达式解析
 */
@Service
public class Sm_ApprovalProcess_ConditionJudgeService {
	@Autowired
	private Sm_ApprovalProcess_MessageTemplateService messageTemplateService;

	@SuppressWarnings({"unchecked"})
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow)
	{
		Properties properties = new MyProperties();

		List<String> successCondition = new ArrayList<>();

		for(Sm_ApprovalProcess_WorkflowCondition workflowCondition : approvalProcess_Workflow.getWorkflowConditionList())
		{
			Map<String ,Object> templateObject = messageTemplateService.execute(approvalProcess_Workflow);

			//表达式内容
			String theContent = workflowCondition.getTheContent();
			theContent = MessageTemplate.judgeTemplateString(templateObject,theContent);

			Jep jep = new Jep();
			Object result = null;
			try {
				result= jep.evaluate(jep.parse(theContent));
				if(result.equals(true))
				{
					Long nextStep = workflowCondition.getNextStep();
					successCondition.add(MyString.getInstance().parse(nextStep));
				}

			} catch (JepException e) {
				e.printStackTrace();
			}
		}
		if(successCondition !=null && successCondition.size()  == 1)
		{
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, successCondition.get(0));
		}
		else
		{
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
		}
		return properties;
	}
}

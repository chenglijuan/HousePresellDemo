package zhishusz.housepresell.service;

import cn.hutool.core.bean.BeanUtil;
import zhishusz.housepresell.approvalprocess.ApprovalProcessCallbackService;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 审批流程 --- 我发起的：撤回
 * 
 * @author Glad.Wang
 *
 */
@Service
@Transactional
public class Sm_ApprovalProcess_AFRecallService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Sm_UserDao sm_userDao;
	@Autowired
	private Sm_BaseDao sm_BaseDao;
	@Autowired
	ApprovalProcessCallbackService approvalProcessCallbackService;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_ApprovalProcess_AFForm model)
	{
		Properties properties = new MyProperties();

		Long userStartId = model.getUserId();
		Long tableId = model.getTableId();
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}
		Sm_User sm_user = sm_userDao.findById(userStartId);
		if(sm_user == null)
		{
			return MyBackInfo.fail(properties, "登录用户不能为空");
		}

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "申请单不存在");
		}

		Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_approvalProcess_afDao.findById(tableId);

		if(sm_approvalProcess_af == null || sm_approvalProcess_af.getTheState().equals(S_TheState.Deleted))
		{
			return MyBackInfo.fail(properties, "申请单不存在");
		}


		List<Sm_ApprovalProcess_Workflow> workflowList =sm_approvalProcess_af.getWorkFlowList();


		if(workflowList!=null && workflowList.size()>1)
		{
			if(workflowList.get(1).getBusiState().equals(S_WorkflowBusiState.Completed))
			{
				return MyBackInfo.fail(properties, "第一个节点已审核通过，不可撤回");
			}
		}

		sm_approvalProcess_af.setBusiState(S_ApprovalState.WaitSubmit);

		workflowList.get(0).setBusiState(S_WorkflowBusiState.WaitSubmit);
		if(workflowList.size() > 1)
		{
			workflowList.get(1).setBusiState(S_WorkflowBusiState.WaitSubmit);
		}

		sm_approvalProcess_afDao.save(sm_approvalProcess_af);

		Long sourceId = sm_approvalProcess_af.getSourceId();
		String className = sm_approvalProcess_af.getSourceType();
		Object queryObject = null;
		Object ossObject = null;
		try {
			Class expectObjClass =Class.forName(className);
			queryObject = sm_BaseDao.findById(expectObjClass, sourceId);
			if(queryObject == null)
			{
				return MyBackInfo.fail(properties, "'业务审批对象'不存在");
			}
			Map<String, Object> queryMap = BeanUtil.beanToMap(queryObject);

			queryMap.put("approvalState", S_ApprovalState.WaitSubmit); //审批状态 ：待提交

			Object oldObject = (Object) BeanUtil.mapToBean(queryMap,expectObjClass,true);
			BeanCopier beanCopier = BeanCopier.create(expectObjClass, expectObjClass, false);
			beanCopier.copy(oldObject, queryObject, null);

			sm_BaseDao.save(queryObject);

			//审批流回调
			properties = approvalProcessCallbackService.execute(workflowList.get(0),model);
			if(!MyBackInfo.isSuccess(properties))
			{
				throw new RoolBackException(MyString.getInstance().parse(properties.get(S_NormalFlag.info)));
			}


		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

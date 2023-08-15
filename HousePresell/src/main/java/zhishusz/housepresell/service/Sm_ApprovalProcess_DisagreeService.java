package zhishusz.housepresell.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import cn.hutool.core.bean.BeanUtil;
import zhishusz.housepresell.approvalprocess.ApprovalProcessCallbackService;
import zhishusz.housepresell.database.po.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_BaseDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 审批流程 --- 审核:不同意按钮（取消审批流）
 * 
 * @author Glad.Wang
 *
 */
@Service
@Transactional
public class Sm_ApprovalProcess_DisagreeService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	@Autowired
	private Sm_ApprovalProcess_CheckService sm_approvalProcess_checkService;
	@Autowired
	ApprovalProcessCallbackService approvalProcessCallbackService;
	@Autowired
	private Sm_UserDao sm_userDao;
	@Autowired
	private Sm_BaseDao sm_BaseDao;

	public Properties execute(Sm_ApprovalProcess_RecordForm model)
	{
		Properties properties = new MyProperties();

		Long approvalProcessId = model.getApprovalProcessId(); //审批流程Id
		Long userOperateId = model.getUserId();  //审批人Id
		Integer theAction = model.getTheAction(); // 审批动作

		if(approvalProcessId == null || approvalProcessId  < 1)
		{
			return MyBackInfo.fail(properties, "'业务审批流'不能为空");
		}
		if(theAction == null || theAction < 0)
		{
			return MyBackInfo.fail(properties, "'审批结果'不能为空");
		}
		if(userOperateId == null || userOperateId  < 1)
		{
			return MyBackInfo.fail(properties, "'审批人'不能为空");
		}
		Sm_User userOperate = sm_userDao.findById(userOperateId);
		if(userOperate == null)
		{
			return MyBackInfo.fail(properties, "'审批人'不能为空");
		}

		Sm_ApprovalProcess_Workflow approvalProcess_Workflow = sm_ApprovalProcess_WorkflowDao.findById(approvalProcessId);// 当前结点信息
		if(approvalProcess_Workflow == null)
		{
			return MyBackInfo.fail(properties, "'当前节点'不能为空");
		}

		//审批人校验
		properties = sm_approvalProcess_checkService.execute(approvalProcess_Workflow,userOperateId);

		if(properties.getProperty("result").equals("fail"))
		{
			return properties;
		}

		if(approvalProcess_Workflow.getApprovalProcess_AF() == null)
		{
			return MyBackInfo.fail(properties, "'申请单'不能为空");
		}
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();

		List<Sm_ApprovalProcess_Workflow> approvalProcess_WorkflowList = sm_ApprovalProcess_AF.getWorkFlowList();
		if(approvalProcess_WorkflowList != null && !approvalProcess_WorkflowList.isEmpty())
		{
			for(Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow : approvalProcess_WorkflowList)
			{
				sm_approvalProcess_workflow.setBusiState(S_WorkflowBusiState.Completed);
				sm_ApprovalProcess_WorkflowDao.save(sm_approvalProcess_workflow);
			}
		}
		
		sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.NoPass); // 审批状态：不通过
		sm_ApprovalProcess_AF.setLastUpdateTimeStamp(System.currentTimeMillis());

		sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);

		/**
		 * 审批流程 --- 修改Po 业务状态 和审批状态
		 */
		setApprovalState(sm_ApprovalProcess_AF);

		//审批流回调
		approvalProcessCallbackService.execute(approvalProcess_Workflow,model);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 审批流程 --- 修改Po 业务状态 和审批状态
	 */
	public void setApprovalState(Sm_ApprovalProcess_AF sm_approvalProcess_af)
	{
		Long sourceId = sm_approvalProcess_af.getSourceId();
		String className = sm_approvalProcess_af.getSourceType();
		Object queryObject = null;
		Object ossObject = null;
		try {
			Class expectObjClass =Class.forName(className);

			queryObject = sm_BaseDao.findById(expectObjClass, sourceId);

			Map<String, Object> queryMap = BeanUtil.beanToMap(queryObject);
			if(queryMap.get("busiState")!=null && queryMap.get("busiState").equals(S_BusiState.NoRecord))
			{
				queryMap.put("theState", S_TheState.Deleted);
			}
			else if(queryMap.get("busiState")!=null && queryMap.get("busiState").equals(S_BusiState.HaveRecord))
			{
				queryMap.put("approvalState", S_ApprovalState.Completed);
			}

			Object oldObject = (Object) BeanUtil.mapToBean(queryMap,expectObjClass,true);
			BeanCopier beanCopier = BeanCopier.create(expectObjClass, expectObjClass, false);
			beanCopier.copy(oldObject, queryObject, null);

			sm_BaseDao.save(queryObject);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

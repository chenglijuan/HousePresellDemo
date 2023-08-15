package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 审批流程通过处理
 */
@Service
@Transactional
public class Sm_ApprovalProcess_EndService {
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;

	public Properties execute(Long approvalProcessId) {
		Properties properties = new MyProperties();

		Long nowDateTime = System.currentTimeMillis();

		// String buttonType = model.getButtonType();// 2
		// Long approvalProcessId = model.getApprovalProcessId(); // 审批流程Id
		// Long userOperateId = model.getUserId(); // 审批人Id
		// Integer theAction = model.getTheAction(); // 审批动作theAction 0

		if (approvalProcessId == null || approvalProcessId < 1) {
			return MyBackInfo.fail(properties, "'业务审批流'不存在");
		}

		Sm_User userOperate = null;

		// 当前节点信息
		Sm_ApprovalProcess_Workflow approvalProcess_Workflow = sm_ApprovalProcess_WorkflowDao
				.findById(approvalProcessId);
		if (approvalProcess_Workflow == null) {
			return MyBackInfo.fail(properties, "'当前节点'不能为空");
		}

		// 先判断该节点状态是否改变
		if (S_WorkflowBusiState.Completed.equals(approvalProcess_Workflow.getBusiState())) {
			return MyBackInfo.fail(properties, "当前节点已被处理");
		}
		if (approvalProcess_Workflow.getApprovalProcess_AF() == null) {
			return MyBackInfo.fail(properties, "'申请单'不能为空");
		}
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();
		if (sm_ApprovalProcess_AF.getConfiguration() == null) {
			return MyBackInfo.fail(properties, "'流程配置'不能为空");
		}

		// 当前节点通过处理
		Long nextId = passExecute(approvalProcess_Workflow, userOperate, 0, nowDateTime);

		Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_ApprovalProcess_AF.getConfiguration();
		// 生成一条审批记录
		List<Sm_ApprovalProcess_Record> recordList = new ArrayList<>();
		Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = new Sm_ApprovalProcess_Record();
		sm_ApprovalProcess_Record.setTheState(S_TheState.Normal);
		sm_ApprovalProcess_Record.setConfiguration(sm_approvalProcess_cfg); // 流程配置
		sm_ApprovalProcess_Record.setCreateTimeStamp(nowDateTime);
		sm_ApprovalProcess_Record.setLastUpdateTimeStamp(nowDateTime);
		sm_ApprovalProcess_Record.setUserStart(userOperate);
		sm_ApprovalProcess_Record.setUserUpdate(userOperate);
		sm_ApprovalProcess_Record.setUserOperate(userOperate); // 审批人
		sm_ApprovalProcess_Record.setTheContent("已提交至银企直联处理"); // 审批评语
		sm_ApprovalProcess_Record.setTheAction(0); // 审批动作
		sm_ApprovalProcess_Record.setOperateTimeStamp(nowDateTime); // 操作时间点
		sm_ApprovalProcess_RecordDao.save(sm_ApprovalProcess_Record);

		if (approvalProcess_Workflow.getApprovalProcess_recordList() != null
				&& approvalProcess_Workflow.getApprovalProcess_recordList().size() > 0) {
			approvalProcess_Workflow.getApprovalProcess_recordList().add(sm_ApprovalProcess_Record);
		} else {
			approvalProcess_Workflow.setApprovalProcess_recordList(recordList);
			recordList.add(sm_ApprovalProcess_Record);
		}

		sm_ApprovalProcess_AF.setUserUpdate(userOperate);
		sm_ApprovalProcess_AF.setLastUpdateTimeStamp(nowDateTime);
		sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);

		sm_ApprovalProcess_WorkflowDao.save(approvalProcess_Workflow);

		properties.put("nextId", nextId);// 下一个节点信息
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 节点通过处理方法
	 * 
	 * @param approvalProcess_Workflow
	 * @param userOperate
	 * @param theAction
	 * @return
	 */
	public Long passExecute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow, Sm_User userOperate,
			Integer theAction, Long nowDateTime) {

		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();

		// 根据条件查找下一个环节
		Long sendId = null;
		Sm_ApprovalProcess_Workflow sendWofkflow = null;
		Sm_ApprovalProcess_Workflow nextApprovalProcess_Workflow = null;

		// 下一个节点信息
		if (approvalProcess_Workflow.getNextWorkFlow() != null) {
			nextApprovalProcess_Workflow = approvalProcess_Workflow.getNextWorkFlow();
		}

		// 判断是否存在下一个节点信息
		if (nextApprovalProcess_Workflow != null) {

			// 默认没有条件，直接获取节点信息
			sendId = nextApprovalProcess_Workflow.getTableId();
			sendWofkflow = nextApprovalProcess_Workflow;
			// 当前流程处于哪个节点
			sm_ApprovalProcess_AF.setCurrentIndex(sendId);
			// 设置节点信息（当前节点状态为：已办）同时设置下一个节点信息
			approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.Completed);
			approvalProcess_Workflow.setSendId(sendId);

			// 设置下一个节点信息
			sendWofkflow.setLastUpdateTimeStamp(nowDateTime);
			sendWofkflow.setBusiState(S_WorkflowBusiState.Examining);
			sendWofkflow.setSourceId(approvalProcess_Workflow.getTableId());
			sm_ApprovalProcess_WorkflowDao.save(sendWofkflow);

		} else {

			// 当前流程处于哪个节点
			sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.Completed);
			sm_ApprovalProcess_AF.setCurrentIndex(approvalProcess_Workflow.getTableId());

			approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.Completed);

		}

		approvalProcess_Workflow.setRejectNodeId(null);
		approvalProcess_Workflow.setLastAction(theAction);
		approvalProcess_Workflow.setUserUpdate(userOperate);
		approvalProcess_Workflow.setUserOperate(userOperate);
		approvalProcess_Workflow.setOperateTimeStamp(nowDateTime);
		approvalProcess_Workflow.setLastUpdateTimeStamp(nowDateTime);

		return null == nextApprovalProcess_Workflow ? null : nextApprovalProcess_Workflow.getTableId();

	}

}

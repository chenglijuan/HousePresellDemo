package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import java.util.List;

/*
 * Service更新操作：审批流-审批记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_RecordUpdateService
{
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_ApprovalProcess_RecordForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		Long approvalProcessId = model.getApprovalProcessId();
		Long configurationId = model.getConfigurationId();
		Long userOperateId = model.getUserOperateId();
		String theContent = model.getTheContent();
		List attachmentList = model.getAttachmentList();
		Integer theAction = model.getTheAction();
		Long operateTimeStamp = model.getOperateTimeStamp();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(approvalProcessId == null || approvalProcessId < 1)
		{
			return MyBackInfo.fail(properties, "'approvalProcess'不能为空");
		}
		if(configurationId == null || configurationId < 1)
		{
			return MyBackInfo.fail(properties, "'configuration'不能为空");
		}
		if(userOperateId == null || userOperateId < 1)
		{
			return MyBackInfo.fail(properties, "'userOperate'不能为空");
		}
		if(theContent == null || theContent.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theContent'不能为空");
		}
		if(attachmentList == null)
		{
			return MyBackInfo.fail(properties, "'attachmentList'不能为空");
		}
		if(theAction == null || theAction < 0)
		{
			return MyBackInfo.fail(properties, "'theAction'不能为空");
		}
		if(operateTimeStamp == null || operateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'operateTimeStamp'不能为空");
		}
		Sm_ApprovalProcess_Workflow approvalProcess = (Sm_ApprovalProcess_Workflow)sm_ApprovalProcess_WorkflowDao.findById(approvalProcessId);
		if(approvalProcess == null)
		{
			return MyBackInfo.fail(properties, "'approvalProcess(Id:" + approvalProcessId + ")'不存在");
		}
		Sm_ApprovalProcess_Cfg configuration = (Sm_ApprovalProcess_Cfg)sm_ApprovalProcess_CfgDao.findById(configurationId);
		if(configuration == null)
		{
			return MyBackInfo.fail(properties, "'configuration(Id:" + configurationId + ")'不存在");
		}
		Sm_User userOperate = (Sm_User)sm_UserDao.findById(userOperateId);
		if(userOperate == null)
		{
			return MyBackInfo.fail(properties, "'userOperate(Id:" + userOperateId + ")'不存在");
		}
	
		Long sm_ApprovalProcess_RecordId = model.getTableId();
		Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = (Sm_ApprovalProcess_Record)sm_ApprovalProcess_RecordDao.findById(sm_ApprovalProcess_RecordId);
		if(sm_ApprovalProcess_Record == null)
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Record(Id:" + sm_ApprovalProcess_RecordId + ")'不存在");
		}
		
		sm_ApprovalProcess_Record.setTheState(theState);
		sm_ApprovalProcess_Record.setBusiState(busiState);
		sm_ApprovalProcess_Record.setApprovalProcess(approvalProcess);
		sm_ApprovalProcess_Record.setConfiguration(configuration);
		sm_ApprovalProcess_Record.setUserOperate(userOperate);
		sm_ApprovalProcess_Record.setTheContent(theContent);
		sm_ApprovalProcess_Record.setTheAction(theAction);
		sm_ApprovalProcess_Record.setOperateTimeStamp(operateTimeStamp);
	
		sm_ApprovalProcess_RecordDao.save(sm_ApprovalProcess_Record);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

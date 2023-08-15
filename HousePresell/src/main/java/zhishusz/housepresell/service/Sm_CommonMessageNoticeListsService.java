package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_CommonMessageNoticeListsService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();		
		Sm_User user = model.getUser();
		Long loginUserId = model.getUserId();


		//获取待办事项条数
		Sm_CommonMessageNoticeForm sm_CommonMessageNoticeForm1 = new Sm_CommonMessageNoticeForm();
		sm_CommonMessageNoticeForm1.setReceiverId(loginUserId);//登录用户Id
		sm_CommonMessageNoticeForm1.setMessageType(S_CommonMessageType.Backlog); //待办事项
		sm_CommonMessageNoticeForm1.setTheState(S_TheState.Normal);
		sm_CommonMessageNoticeForm1.setIsReader(S_IsReaderState.UnReadMesg);//未读
		Integer totalCount1 = sm_CommonMessageDtlDao.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL(), sm_CommonMessageNoticeForm1));

		//获取未读公告条数
		Sm_CommonMessageNoticeForm sm_CommonMessageNoticeForm2 = new Sm_CommonMessageNoticeForm();
		sm_CommonMessageNoticeForm2.setReceiver(user);
		sm_CommonMessageNoticeForm2.setMessageType(S_CommonMessageType.UnreadBulletin);
		sm_CommonMessageNoticeForm2.setTheState(S_TheState.Normal);
		sm_CommonMessageNoticeForm2.setIsReader(S_IsReaderState.UnReadMesg);//未读
		Integer totalCount2 = sm_CommonMessageDtlDao.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL(), sm_CommonMessageNoticeForm2));
		//未处理预警条数
		Sm_CommonMessageNoticeForm sm_CommonMessageNoticeForm3 = new Sm_CommonMessageNoticeForm();
		sm_CommonMessageNoticeForm3.setReceiver(user);
		sm_CommonMessageNoticeForm3.setMessageType(S_CommonMessageType.UnreadWaring);
		sm_CommonMessageNoticeForm3.setIsReader(S_IsReaderState.UnReadMesg);//未读
		sm_CommonMessageNoticeForm3.setBusiState("0");//未处理
		sm_CommonMessageNoticeForm3.setTheState(S_TheState.Normal);
		Integer totalCount3 = sm_CommonMessageDtlDao.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL1(), sm_CommonMessageNoticeForm3));


//		/**
//		 * 1.通过用户Id查询所属角色
//		 * 2.根据所属角色查询代办流程
//		 */
//		Sm_ApprovalProcess_WorkflowForm sm_ApprovalProcess_WorkflowForm=new Sm_ApprovalProcess_WorkflowForm();
//		sm_ApprovalProcess_WorkflowForm.setUserId(model.getUserId());
//		sm_ApprovalProcess_WorkflowForm.setPageNumber(model.getPageNumber());
//		sm_ApprovalProcess_WorkflowForm.setCountPerPage(model.getCountPerPage());
//		sm_ApprovalProcess_WorkflowForm.setTheState(S_TheState.Normal);
//		sm_ApprovalProcess_WorkflowForm.setBusiState("审核中");
//		Long userId = model.getUserId(); //登录用户id
//
//		if(userId == null || userId < 1)
//		{
//			return MyBackInfo.fail(properties, "'登录用户'不能为空");
//		}
//
//		Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
//		roleUserForm.setTheState(S_TheState.Normal);
//		roleUserForm.setSm_UserId(userId);
//		List<Sm_Permission_RoleUser> sm_permission_roleUserList = sm_permission_roleUserDao.findByPage(sm_permission_roleUserDao.getQuery(sm_permission_roleUserDao.getBasicHQL(), roleUserForm));
//
//		if(sm_permission_roleUserList != null && !sm_permission_roleUserList.isEmpty())
//		{
//			Long[] roleListId = new Long[sm_permission_roleUserList.size()];
//			for(int i=0 ; i<sm_permission_roleUserList.size();i++)
//			{
//				roleListId[i] = sm_permission_roleUserList.get(i).getSm_Permission_Role().getTableId();
//			}
//
//			sm_ApprovalProcess_WorkflowForm.setRoleListId(roleListId);
//		}
//
//		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
//		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
//
//		if(sm_ApprovalProcess_WorkflowForm.getApprovalApplyDate() !=null && sm_ApprovalProcess_WorkflowForm.getApprovalApplyDate().length() > 0)
//		{
//			String[] applyDate = sm_ApprovalProcess_WorkflowForm.getApprovalApplyDate().split(" - ");
//			Long startTimeStamp = myDatetime.stringToLong(applyDate[0]);
//			Long dayTime = 24L * 60 * 60;
//			Long endTimeStamp = myDatetime.stringToLong(applyDate[1])+dayTime;
//			sm_ApprovalProcess_WorkflowForm.setStartTimeStamp(startTimeStamp);
//			sm_ApprovalProcess_WorkflowForm.setEndTimeStamp(endTimeStamp);
//		}
//
//		String keyword = model.getKeyword();
//
//		if(keyword!=null && keyword.length()>0)
//		{
//			model.setKeyword("%"+keyword+"%");
//		}
//		else
//		{
//			model.setKeyword(null);
//		}
//
//
//		Integer totalCount = sm_ApprovalProcess_WorkflowDao.findByPage_Size(sm_ApprovalProcess_WorkflowDao.getQuery_Size(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), sm_ApprovalProcess_WorkflowForm));
//
//		Integer totalPage = totalCount / countPerPage;
//		if (totalCount % countPerPage > 0) totalPage++;
//		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
//
//		List<Sm_ApprovalProcess_Workflow> sm_ApprovalProcess_WorkflowList;
//		if(totalCount > 0)
//		{
//			sm_ApprovalProcess_WorkflowList = sm_ApprovalProcess_WorkflowDao.findByPage(sm_ApprovalProcess_WorkflowDao.getQuery(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), sm_ApprovalProcess_WorkflowForm), pageNumber, countPerPage);
//
//				Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow =  sm_ApprovalProcess_WorkflowList.get(0);
//				//待办流程，会签模式下，已经审批通过的人，待办流程中不可以看到这条结点信息
//				if(sm_approvalProcess_workflow.getBusiState().equals("审核中") && sm_approvalProcess_workflow.getApprovalModel().equals(1))
//				{
//					Sm_ApprovalProcess_RecordForm recordForm = new Sm_ApprovalProcess_RecordForm();
//					recordForm.setTheState(S_TheState.Normal);
//					recordForm.setApprovalProcessId(sm_approvalProcess_workflow.getTableId());
//					if(sm_approvalProcess_workflow.getLastUpdateTimeStamp() !=null)//当前节点的最后更新时间
//					{
//						recordForm.setWorkflowTime(sm_approvalProcess_workflow.getLastUpdateTimeStamp());
//					}
//
//					List<Sm_ApprovalProcess_Record> sm_approvalProcess_recordList = sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao.getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), recordForm));
//					for (Sm_ApprovalProcess_Record sm_approvalProcess_record : sm_approvalProcess_recordList)
//					{
//						if(sm_approvalProcess_record.getUserOperate().getTableId().equals(userId))
//						{
//							sm_ApprovalProcess_WorkflowList.remove(sm_approvalProcess_workflow);
//						}
//					}
//				}
//			if(sm_ApprovalProcess_WorkflowList == null || sm_ApprovalProcess_WorkflowList.isEmpty())
//			{
//				sm_ApprovalProcess_WorkflowList = new ArrayList<>();
//			}
//
//		}
//		else
//		{
//			sm_ApprovalProcess_WorkflowList = new ArrayList<Sm_ApprovalProcess_Workflow>();
//		}
		
		properties.put("DbTotals", totalCount1);
		properties.put("WdTotals", totalCount2);
		properties.put("YjTotals", totalCount3);
//		properties.put("sm_ApprovalProcess_WorkflowList", sm_ApprovalProcess_WorkflowList);
//		properties.put(S_NormalFlag.keyword, keyword);
//		properties.put(S_NormalFlag.totalPage, totalPage);
//		properties.put(S_NormalFlag.pageNumber, pageNumber);
//		properties.put(S_NormalFlag.countPerPage, countPerPage);
//		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

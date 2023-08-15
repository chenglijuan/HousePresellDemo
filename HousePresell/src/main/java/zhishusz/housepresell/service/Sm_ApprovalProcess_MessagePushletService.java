package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_BusinessRecordForm;
import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.util.*;
import zhishusz.housepresell.util.busicode.BusiCodeRange;
import zhishusz.housepresell.util.messagetemplate.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 审批，消息推送
 */
@Service
public class Sm_ApprovalProcess_MessagePushletService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
	@Autowired
	private Sm_PushletService sm_PushletService;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private Sm_BusinessRecordDao sm_businessRecordDao ;
	@Autowired
	private Sm_UserGetRangeService sm_userGetRangeService;
	MyDatetime myDatetime = MyDatetime.getInstance();
	@Autowired
	private Sm_ApprovalProcess_MessageTemplateService messageTemplateService;

	@SuppressWarnings({"unchecked"})
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow,Sm_User userOperate)
	{
		Properties properties = new MyProperties();

		boolean msgSend = true;
		if(approvalProcess_Workflow.getSm_messageTemplate_cfgList() !=null && !approvalProcess_Workflow.getSm_messageTemplate_cfgList().isEmpty())
		{
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = null; //申请单
			String busiCode = null; //业务编码
			Long afId = null;//申请单Id
			Sm_User userStart = null; //发起人
			Long applicantId = null;//发起人Id
			if(approvalProcess_Workflow.getApprovalProcess_AF()!=null)
			{
				sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();
				busiCode = sm_ApprovalProcess_AF.getBusiCode();
				afId = sm_ApprovalProcess_AF.getTableId();
				if(sm_ApprovalProcess_AF.getUserStart()!=null)
				{
					userStart =sm_ApprovalProcess_AF.getUserStart(); //发起人
					applicantId = userStart.getTableId();//发起人
				}
				
				/**
				 * 根据AF申请单关联的单据SOURCEID查询是否存在相同单据申请单
				 */
				Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
				afModel.setTheState(S_TheState.Normal);
				afModel.setSourceId(sm_ApprovalProcess_AF.getSourceId());
				afModel.setSourceType(sm_ApprovalProcess_AF.getSourceType());
				List<Sm_ApprovalProcess_AF> afList = new ArrayList<Sm_ApprovalProcess_AF>();
				afList = sm_approvalProcess_afDao.findByPage(sm_approvalProcess_afDao.getQuery(sm_approvalProcess_afDao.getBasicHQL(), afModel));
				if(null != afList && afList.size() > 1)
				{
					msgSend = false;
				}
				
			}

			if(msgSend)
			{
				for(Sm_MessageTemplate_Cfg sm_messageTemplate_cfg : approvalProcess_Workflow.getSm_messageTemplate_cfgList())
				{
					/**
					 * 根据操作获取标题和内容
					 */
					Integer lastAction = approvalProcess_Workflow.getLastAction();
					String theTitle = MessageTemplate.getContent(lastAction,sm_messageTemplate_cfg.getTheTitle());//消息模板标题
					String content =  MessageTemplate.getContent(lastAction,sm_messageTemplate_cfg.getTheContent());//消息模板内容


					/**
					 * 解析标题和内容
					 */
					Map<String ,Object> templateObject = messageTemplateService.execute(approvalProcess_Workflow);
					theTitle = MessageTemplate.messageTemplateString(templateObject,theTitle); //消息标题
					content = MessageTemplate.messageTemplateString(templateObject,content);//消息内容

					//更新申请单消息主题
					if(theTitle!=null && theTitle.length() > 0 && content!=null && content.length() > 0)
					{
//						sm_ApprovalProcess_AF.setTheme(theTitle);
						sm_approvalProcess_afDao.save(sm_ApprovalProcess_AF);

						//1.先生成消息主表
						// 消息插入主表中
						Sm_CommonMessage sm_CommonMessage = new Sm_CommonMessage();

						sm_CommonMessage.setUserStart(userOperate);//审批人
						sm_CommonMessage.setCreateTimeStamp(System.currentTimeMillis());
						sm_CommonMessage.setUserUpdate(userOperate);//审批人
						sm_CommonMessage.setLastUpdateTimeStamp(System.currentTimeMillis());
						sm_CommonMessage.setTheState(S_TheState.Normal);
						sm_CommonMessage.setMessageType(S_CommonMessageType.Backlog);
						sm_CommonMessage.setBusiState(0);
						sm_CommonMessage.setBusiCode(busiCode);
						sm_CommonMessage.setOrgDataId(afId.toString());
						sm_CommonMessage.setTheTitle(theTitle);
						sm_CommonMessage.setTheContent(content);
						sm_CommonMessage.setSendTimeStamp(myDatetime.dateToString2(System.currentTimeMillis()));

						sm_CommonMessageDao.save(sm_CommonMessage);

						if(sm_messageTemplate_cfg.getSm_permission_roleList() !=null && !sm_messageTemplate_cfg.getSm_permission_roleList().isEmpty())
						{
							/**
							 * 1:先直接查业务关联记录表条数
							 */
							Sm_BusinessRecordForm sm_businessRecordForm1 = new Sm_BusinessRecordForm();
							sm_businessRecordForm1.setTheState(S_TheState.Normal);
							sm_businessRecordForm1.setAfId(sm_ApprovalProcess_AF.getTableId());
							Integer businessRecordCount1 = sm_businessRecordDao.findByPage_Size(sm_businessRecordDao.getQuery_Size(sm_businessRecordDao.getBasicHQL(), sm_businessRecordForm1));

							Sm_Permission_Role sm_permission_role = null;
							for(int i = 0;i<sm_messageTemplate_cfg.getSm_permission_roleList().size();i++)
//							for(Sm_Permission_Role sm_permission_role : sm_messageTemplate_cfg.getSm_permission_roleList())
							{
								sm_permission_role = sm_messageTemplate_cfg.getSm_permission_roleList().get(i);
								if("104820".equals(sm_permission_role.getTableId()))
								{//过滤财务副总角色
									continue;
								}
								
								Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
								roleUserForm.setTheState(S_TheState.Normal);
								roleUserForm.setSm_Permission_RoleId(sm_permission_role.getTableId());
								List<Sm_Permission_RoleUser> sm_permission_roleUserList = sm_permission_roleUserDao.findByPage(sm_permission_roleUserDao.getQuery(sm_permission_roleUserDao.getBasicHQL(), roleUserForm));
								for(Sm_Permission_RoleUser smPermissionRoleUser : sm_permission_roleUserList)
								{
									Sm_User sm_user = smPermissionRoleUser.getSm_User();
									Sm_CommonMessageNoticeForm noticeForm = new Sm_CommonMessageNoticeForm();
									noticeForm.setUserStart(userStart);
									noticeForm.setMessage(sm_CommonMessage);
									noticeForm.setReceiver(sm_user);

									//普通机构不需要做范围授权过滤 - 该角色下的所有人都能够收到消息
									String [] NoRange_BusiCode = BusiCodeRange.NoRange_BusiCode();
									if(!Arrays.asList(NoRange_BusiCode).contains(busiCode))
									{
										if(!sm_user.getTableId().equals(applicantId))
										{
											//消息推送
											sm_PushletService.execute(theTitle, content, sm_user.getTableId());
											//生成待办事项
											sendCommonMessageDtl(noticeForm);
										}
									}
									else
									{
										Properties userGetRange_Pro =  sm_userGetRangeService.execute(sm_user);
										if(userGetRange_Pro.getProperty(S_NormalFlag.result).equals(S_NormalFlag.success))
										{
											Long[]  cityRegionInfoIdArr = (Long[]) userGetRange_Pro.get("cityRegionInfoIdArr");
											Long[] projectInfoIdArr = (Long[]) userGetRange_Pro.get("projectInfoIdArr");
											Long[] buildingInfoIdIdArr = (Long[]) userGetRange_Pro.get("buildingInfoIdIdArr");
											/**
											 * 2:再加上登录用户范围权限去查业务关联记录表条数
											 */
											Sm_BusinessRecordForm sm_businessRecordForm2 = new Sm_BusinessRecordForm();
											sm_businessRecordForm2.setTheState(S_TheState.Normal);
											sm_businessRecordForm2.setAfId(sm_ApprovalProcess_AF.getTableId());

											sm_businessRecordForm2.setCityRegionInfoIdArr(cityRegionInfoIdArr);
											sm_businessRecordForm2.setProjectInfoIdArr(projectInfoIdArr);
											sm_businessRecordForm2.setBuildingInfoIdIdArr(buildingInfoIdIdArr);
											Integer businessRecordCount2 = sm_businessRecordDao.findByPage_Size(sm_businessRecordDao.getQuery_Size(sm_businessRecordDao.getBasicHQL(), sm_businessRecordForm2));

											if(businessRecordCount1 == 1)
											{
												Sm_BusinessRecord sm_businessRecord = sm_businessRecordDao.findOneByQuery_T(sm_businessRecordDao.getQuery(sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm1));
												if(sm_businessRecord.getBuildingInfo()!=null)
												{
													sm_businessRecordForm2.setBuildingInfoIdIdArr(buildingInfoIdIdArr);
												}
												if(sm_businessRecord.getProjectInfo()!=null)
												{
													sm_businessRecordForm2.setProjectInfoIdArr(projectInfoIdArr);
												}
												if(sm_businessRecord.getCityRegion()!=null)
												{
													sm_businessRecordForm2.setCityRegionInfoIdArr(cityRegionInfoIdArr);
												}
											}

											if(businessRecordCount1 > 1)
											{
												sm_businessRecordForm2.setCityRegionInfoIdArr(cityRegionInfoIdArr);
												sm_businessRecordForm2.setProjectInfoIdArr(projectInfoIdArr);
												sm_businessRecordForm2.setBuildingInfoIdIdArr(buildingInfoIdIdArr);
											}

											if((businessRecordCount2 == businessRecordCount1) && !sm_user.getTableId().equals(applicantId))
											{
												//消息推送
												sm_PushletService.execute(theTitle, content, sm_user.getTableId());

												//生成待办事项
												sendCommonMessageDtl(noticeForm);
											}
										}
										else
										{
											return  userGetRange_Pro;
										}
									}
								}
							}
						}
						//推送消息给发起人
						sm_PushletService.execute(theTitle, content, applicantId);
						Sm_CommonMessageNoticeForm noticeForm2 = new Sm_CommonMessageNoticeForm();
						noticeForm2.setUserStart(userStart);
						noticeForm2.setMessage(sm_CommonMessage);
						noticeForm2.setReceiver(userStart);
						sendCommonMessageDtl(noticeForm2);
					}
				}
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	public void  sendCommonMessageDtl (Sm_CommonMessageNoticeForm sm_commonMessageNoticeForm)
	{

		Sm_User sendUser = sm_commonMessageNoticeForm.getUserStart();//发送人
		Sm_CommonMessage sm_commonMessage = sm_commonMessageNoticeForm.getMessage();
		Sm_User receiver = sm_commonMessageNoticeForm.getReceiver();//接收人

		Sm_CommonMessageDtl sm_CommonMessageDtl = new Sm_CommonMessageDtl();

		sm_CommonMessageDtl.setUserStart(sendUser); //消息发送人
		sm_CommonMessageDtl.setCreateTimeStamp(System.currentTimeMillis());
		sm_CommonMessageDtl.setUserUpdate(sendUser);//消息发送人
		sm_CommonMessageDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_CommonMessageDtl.setMessage(sm_commonMessage);
		sm_CommonMessageDtl.setTheState(S_TheState.Normal);
		sm_CommonMessageDtl.setIsReader(S_IsReaderState.UnReadMesg);// 读取状态 ： 0 ：未读 1：已读
		sm_CommonMessageDtl.setMessageType(S_CommonMessageType.Backlog);
		sm_CommonMessageDtl.setSendTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
		sm_CommonMessageDtl.setReceiver(receiver);//消息接收人 (申请单发起人)
		sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);
	}
}

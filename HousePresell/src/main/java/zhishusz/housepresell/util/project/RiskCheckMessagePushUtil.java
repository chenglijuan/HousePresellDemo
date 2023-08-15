package zhishusz.housepresell.util.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_PushletService;
import zhishusz.housepresell.util.MyDatetime;

/**
 * Created by Zachary on 2018/12/6.
 * Company: www.chisalsoft.
 * Usage: 风控抽查推送方法
 */
@Service
public class RiskCheckMessagePushUtil 
{
    @Autowired
    private Sm_CommonMessageDao sm_CommonMessageDao;
    @Autowired
    private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
    @Autowired
    private Sm_PushletService sm_PushletService;

    public void pushMessage(List<Sm_Permission_RoleUser> sm_permission_roleUserList, String theTitle, String theContent, 
			Tg_RiskRoutineCheckInfo riskCheckInfo, Tg_RiskRoutineCheckInfoForm model, String busiCode)
	{
		//1.生成消息主表
		Sm_CommonMessage sm_CommonMessage = new Sm_CommonMessage();
		
		sm_CommonMessage.setUserStart(model.getUser());//创建人
		sm_CommonMessage.setCreateTimeStamp(System.currentTimeMillis());
		sm_CommonMessage.setUserUpdate(model.getUser());//发送人
		sm_CommonMessage.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_CommonMessage.setTheState(S_TheState.Normal);
		sm_CommonMessage.setMessageType(S_CommonMessageType.Backlog);
		sm_CommonMessage.setBusiState(0);
		sm_CommonMessage.setBusiCode(busiCode);
		sm_CommonMessage.setOrgDataId(riskCheckInfo.getTableId().toString());
		sm_CommonMessage.setOrgDataCode(riskCheckInfo.geteCode());
		sm_CommonMessage.setTheTitle(theTitle);
		sm_CommonMessage.setTheContent(theContent);
		sm_CommonMessage.setSendTimeStamp(MyDatetime.getInstance().dateToString2(System.currentTimeMillis()));
		
		sm_CommonMessageDao.save(sm_CommonMessage);
		
		for(Sm_Permission_RoleUser smPermissionRoleUser : sm_permission_roleUserList)
		{
			Sm_User sm_user = smPermissionRoleUser.getSm_User();
			Sm_CommonMessageNoticeForm sm_commonMessageNoticeForm = new Sm_CommonMessageNoticeForm();
			sm_commonMessageNoticeForm.setUserStart(model.getUser());
			sm_commonMessageNoticeForm.setMessage(sm_CommonMessage);
			sm_commonMessageNoticeForm.setReceiver(sm_user);
			
			//2.生成代办事项
			sendCommonMessageDtl(sm_commonMessageNoticeForm);
			
			//3.推送消息
			sm_PushletService.execute(theTitle, theContent, sm_user.getTableId());
		}
	}
	
	public void sendCommonMessageDtl (Sm_CommonMessageNoticeForm sm_commonMessageNoticeForm)
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
		sm_CommonMessageDtl.setSendTimeStamp(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
		sm_CommonMessageDtl.setReceiver(receiver);//消息接收人
		
		sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);
	}

}

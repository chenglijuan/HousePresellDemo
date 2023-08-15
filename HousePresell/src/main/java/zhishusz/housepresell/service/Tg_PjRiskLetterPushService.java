package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskLetterPushService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;
	@Autowired
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	MyDatetime myDatetime = MyDatetime.getInstance();
	// 提示函编码
	private static final String BUSI_CODE = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	// 预警编码
	private static final String busiCode = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		Long tableId = model.getTableId();
		
		Tg_PjRiskLetter tg_PjRiskLetter = (Tg_PjRiskLetter)tg_PjRiskLetterDao.findById(tableId);
		if(tg_PjRiskLetter == null)
		{
			return MyBackInfo.fail(properties, "项目风险函信息有误！");
		}
		
		// 根据主键查询是否推送
		// 查询条件：1.关联主键 2.状态：正常
		Sm_CommonMessageNoticeForm sm_CommonMessageNoticeForm = new Sm_CommonMessageNoticeForm();
		sm_CommonMessageNoticeForm.setOrgDataId(tableId.toString());
		sm_CommonMessageNoticeForm.setTheState(S_TheState.Normal);
		sm_CommonMessageNoticeForm.setBusiCode(BUSI_CODE);
		sm_CommonMessageNoticeForm.setOrgDataCode(tg_PjRiskLetter.geteCode());

		Integer messageCount = sm_CommonMessageDao.findByPage_Size(
				sm_CommonMessageDao.getQuery_Size(sm_CommonMessageDao.getBasicHQL(), sm_CommonMessageNoticeForm));

		if (messageCount > 0)
		{
			return MyBackInfo.fail(properties, "项目风险函已推送内部成员,请勿重复推送！");
		}
		
		// 消息插入主表中
		Sm_CommonMessage sm_CommonMessage = new Sm_CommonMessage();
		
		sm_CommonMessage.seteCode(sm_BusinessCodeGetService.execute(busiCode));
		sm_CommonMessage.setUserStart(user);
		sm_CommonMessage.setCreateTimeStamp(System.currentTimeMillis());
		sm_CommonMessage.setUserUpdate(user);
		sm_CommonMessage.setLastUpdateTimeStamp(System.currentTimeMillis());
		sm_CommonMessage.setTheState(S_TheState.Normal);
		sm_CommonMessage.setMessageType(S_CommonMessageType.UnreadWaring);
		sm_CommonMessage.setBusiState(0);
		sm_CommonMessage.setBusiCode(BUSI_CODE);
		sm_CommonMessage.setOrgDataId(tableId.toString());
		sm_CommonMessage.setOrgDataCode(tg_PjRiskLetter.geteCode());
		sm_CommonMessage.setTheTitle("风险提示函");
		sm_CommonMessage.setTheContent(tg_PjRiskLetter.getRiskNotification());
//		sm_CommonMessage.setTheData(theData);
		sm_CommonMessage.setSendTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
//		sm_CommonMessage.setRemark(remark);
		
		sm_CommonMessageDao.save(sm_CommonMessage);
		
		Tg_PjRiskLetterReceiverForm tg_PjRiskLetterReceiverForm = new Tg_PjRiskLetterReceiverForm();
		Emmp_CompanyInfo emmp_CompanyInfo = user.getCompany();
		tg_PjRiskLetterReceiverForm.setEmmp_CompanyInfo(emmp_CompanyInfo);	
		tg_PjRiskLetterReceiverForm.setTheState(S_TheState.Normal);
		
		Integer totalCount = tg_PjRiskLetterReceiverDao.findByPage_Size(tg_PjRiskLetterReceiverDao.getQuery_Size(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));
		
		List<Tg_PjRiskLetterReceiver> tg_PjRiskLetterReceiverList;
		if(totalCount > 0)
		{
			tg_PjRiskLetterReceiverList = tg_PjRiskLetterReceiverDao.findByPage(tg_PjRiskLetterReceiverDao.getQuery(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));
			
			for(Tg_PjRiskLetterReceiver tg_PjRiskLetterReceiver : tg_PjRiskLetterReceiverList)
			{
				
				Sm_CommonMessageDtl sm_CommonMessageDtl = new Sm_CommonMessageDtl();
				
				sm_CommonMessageDtl.seteCode(sm_BusinessCodeGetService.execute(busiCode));
				sm_CommonMessageDtl.setUserStart(user);
				sm_CommonMessageDtl.setCreateTimeStamp(System.currentTimeMillis());
				sm_CommonMessageDtl.setUserUpdate(user);
				sm_CommonMessageDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_CommonMessageDtl.setMessage(sm_CommonMessage);
				sm_CommonMessageDtl.setTheState(S_TheState.Normal);
				sm_CommonMessageDtl.setBusiState("0");
				sm_CommonMessageDtl.setMessageType(S_CommonMessageType.UnreadWaring);
				sm_CommonMessageDtl.setSendTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
				sm_CommonMessageDtl.setReceiver(tg_PjRiskLetterReceiver.getEmmp_OrgMember());
				sm_CommonMessageDtl.setIsReader(S_IsReaderState.UnReadMesg);
				
				sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);				
			}
		}
		else
		{
			tg_PjRiskLetterReceiverList = new ArrayList<Tg_PjRiskLetterReceiver>();
		}
		
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

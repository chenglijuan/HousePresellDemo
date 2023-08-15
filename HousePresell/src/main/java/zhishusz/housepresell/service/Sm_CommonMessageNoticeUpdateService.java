package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_BusiState;
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
@Transactional
public class Sm_CommonMessageNoticeUpdateService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private SessionFactory sessionFactory;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		Long[] idArr = model.getIdArr();
		
		for(int i = 0 ; i< idArr.length ;i++)
		{
			Long sm_CommonMessageId = idArr[i];
			

			Sm_CommonMessage sm_CommonMessage = (Sm_CommonMessage)sm_CommonMessageDao.findById(sm_CommonMessageId);
			if(sm_CommonMessage == null || S_TheState.Deleted.equals(sm_CommonMessage.getTheState()))
			{
				return MyBackInfo.fail(properties, "通告信息不存在");
			}
			
//			sm_CommonMessage.setUserUpdate(user);
//			sm_CommonMessage.setLastUpdateTimeStamp(System.currentTimeMillis());
//			sm_CommonMessage.setBusiState(1);
//			sm_CommonMessageDao.save(sm_CommonMessage);
			

			// 查询子表
			// 查询条件：1.接收人 2.状态：正常 
			Sm_CommonMessageNoticeForm sm_CommonMessageNoticeForm = new Sm_CommonMessageNoticeForm();
			sm_CommonMessageNoticeForm.setReceiver(user);
			sm_CommonMessageNoticeForm.setMessage(sm_CommonMessage);
			sm_CommonMessageNoticeForm.setTheState(S_TheState.Normal);
			
			Integer totalCount = sm_CommonMessageDtlDao.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL(), sm_CommonMessageNoticeForm));
		
			List<Sm_CommonMessageDtl> sm_CommonMessageDtlList;
			if(totalCount > 0)
			{
				sm_CommonMessageDtlList = sm_CommonMessageDtlDao.findByPage(sm_CommonMessageDtlDao.getQuery(sm_CommonMessageDtlDao.getBasicHQL(), sm_CommonMessageNoticeForm));
				Sm_CommonMessageDtl commonMessage = sm_CommonMessageDtlList.get(0);
				
				commonMessage.setReadTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
				commonMessage.setUserUpdate(user);
				commonMessage.setLastUpdateTimeStamp(System.currentTimeMillis());
				commonMessage.setBusiState(S_BusiState.HaveRecord);
				sm_CommonMessageDtlDao.save(commonMessage);
			
			}
			
			
		}
	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

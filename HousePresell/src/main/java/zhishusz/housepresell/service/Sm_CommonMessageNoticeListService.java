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
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_CommonMessageNoticeListService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		Sm_User user = model.getUser();

		// 查询子表
		// 查询条件：1.接收人 2.状态：正常 3. 消息类型
		Sm_CommonMessageNoticeForm sm_CommonMessageNoticeForm = new Sm_CommonMessageNoticeForm();
		sm_CommonMessageNoticeForm.setReceiver(user);
		sm_CommonMessageNoticeForm.setMessageType("消息通知");
		sm_CommonMessageNoticeForm.setTheState(S_TheState.Normal);
		sm_CommonMessageNoticeForm.setBusiState(S_BusiState.NoRecord);
		
		Integer totalCount = sm_CommonMessageDtlDao.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL(), sm_CommonMessageNoticeForm));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_CommonMessageDtl> sm_CommonMessageDtlList;
		List<Sm_CommonMessage> sm_CommonMessageList = new ArrayList<Sm_CommonMessage>();
		if(totalCount > 0)
		{
			sm_CommonMessageDtlList = sm_CommonMessageDtlDao.findByPage(sm_CommonMessageDtlDao.getQuery(sm_CommonMessageDtlDao.getBasicHQL(), sm_CommonMessageNoticeForm), pageNumber, countPerPage);
			
			for(Sm_CommonMessageDtl sm_CommonMessageDtl : sm_CommonMessageDtlList)
			{			
				sm_CommonMessageList.add(sm_CommonMessageDtl.getMessage());
			}
			
		}
		else
		{
			sm_CommonMessageDtlList = new ArrayList<Sm_CommonMessageDtl>();
		}
		
		properties.put("Sm_CommonMessage", sm_CommonMessageList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

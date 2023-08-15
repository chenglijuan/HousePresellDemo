package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：待办事项
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_ApprovalProcess_BacklogListService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		Long loginUserId = model.getUserId();
		
		String busiCode = model.getBusiCode();
		if(null==busiCode||busiCode.trim().isEmpty())
		{
			model.setBusiCode(null);
		}

		// 查询子表
		// 查询条件：1.接收人 2.状态：正常 3. 消息类型

		//关键字
		String keyword = model.getKeyword();
		if(keyword == null || keyword.length()==0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%"+keyword+"%");
		}

		// 业务状态 ： 0：未读 1：已读
		Integer isReader = model.getIsReader();
		if(isReader == null || isReader < 0)
		{
			model.setBusiState(null);
		}


		model.setReceiverId(loginUserId);//登录用户
		model.setMessageType(S_CommonMessageType.Backlog); //待办事项

		Integer totalCount = sm_CommonMessageDtlDao.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_CommonMessageDtl> sm_CommonMessageDtlList;
		if(totalCount > 0)
		{
			sm_CommonMessageDtlList = sm_CommonMessageDtlDao.findByPage(sm_CommonMessageDtlDao.getQuery(sm_CommonMessageDtlDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_CommonMessageDtlList = new ArrayList<Sm_CommonMessageDtl>();
		}
		
		properties.put("Sm_CommonMessage", sm_CommonMessageDtlList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

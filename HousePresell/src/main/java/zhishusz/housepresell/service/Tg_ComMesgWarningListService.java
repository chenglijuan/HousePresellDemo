package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询操作：预警列表查询
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_ComMesgWarningListService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		// 获取当前登陆用户
		Long userId = model.getUserId();

		String otherBusiCode = model.getOtherBusiCode();

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == otherBusiCode || otherBusiCode.trim().isEmpty())
		{
			model.setBusiCode(null);
		}
		else
		{
			model.setBusiCode(otherBusiCode.trim());
		}

		model.setMessageType(S_CommonMessageType.UnreadWaring);

		Sm_User sm_User = new Sm_User();
		sm_User.setTableId(userId);

		model.setReceiver(sm_User);
		model.setTheState(0);

		Integer totalCount = sm_CommonMessageDtlDao
				.findByPage_Size(sm_CommonMessageDtlDao.getQuery_Size(sm_CommonMessageDtlDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Sm_CommonMessageDtl> sm_CommonMessageDtlList = null;
		if (totalCount > 0)
		{
			sm_CommonMessageDtlList = sm_CommonMessageDtlDao.findByPage(
					sm_CommonMessageDtlDao.getQuery(sm_CommonMessageDtlDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			sm_CommonMessageDtlList = new ArrayList<Sm_CommonMessageDtl>();
		}

		properties.put("sm_CommonMessageDtlList", sm_CommonMessageDtlList);

		properties.put("keyword", keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

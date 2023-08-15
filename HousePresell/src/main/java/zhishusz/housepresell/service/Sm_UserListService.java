package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_UserListService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();
		if(StringUtils.isEmpty(model.getOrderBy()))
		{
			model.setOrderBy(null);
		}
		else
		{
			String[] orderByAr = model.getOrderBy().split(" ");
			model.setOrderBy(orderByAr[0]);
			model.setOrderByType(orderByAr[1]);
		}
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword_Old = model.getKeyword();
		String keyword = Checker.getInstance().checkKeyword(model.getKeyword());
		model.setKeyword(keyword);
		model.setBusiState(model.getBusiState() == "" ? null : model.getBusiState());
		model.setLockUntil(System.currentTimeMillis());
		
		Integer totalCount = sm_UserDao.findByPage_Size(sm_UserDao.getQuery_Size(sm_UserDao.getBasicHQL(), model));
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		List<Sm_User> sm_UserList;
		if(totalCount > 0)
		{
			sm_UserList = sm_UserDao.findByPage(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_UserList = new ArrayList<Sm_User>();
		}
		properties.put("sm_UserList", sm_UserList);
		properties.put(S_NormalFlag.keyword, keyword_Old);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

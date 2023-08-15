package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUIForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RoleUIListService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleUIForm model)
	{
		Checker checker = Checker.getInstance();
		MyDatetime myDatetime = MyDatetime.getInstance();
		Properties properties = new MyProperties();
		
		Integer pageNumber = checker.checkPageNumber(model.getPageNumber());
		Integer countPerPage = checker.checkCountPerPage(model.getCountPerPage());
		
		String keyword_Old = model.getKeyword();
		String keyword = checker.checkKeyword(model.getKeyword());
		model.setKeyword(keyword);
		String busiType = (model.getBusiType() == null || model.getBusiType().length() == 0) ? null : model.getBusiType(); 
		model.setBusiType(busiType);
		
		String enableTimeStampRange = model.getEnableTimeStampRange();
		Long enableTimeStampStart = myDatetime.getDateTimeStampMin(enableTimeStampRange);
		Long enableTimeStampEnd = myDatetime.getDateTimeStampMax(enableTimeStampRange);
		model.setEnableTimeStampStart(enableTimeStampStart);
		model.setEnableTimeStampEnd(enableTimeStampEnd);
		
		String orderBy = model.getOrderBy();
		if(orderBy == null || orderBy.length() == 0)
		{
			//默认 按照 “启用日期” 升序
			orderBy = "enableTimeStamp asc";
			model.setOrderBy(orderBy);
		}
		else
		{
			String[] orderByAr = model.getOrderBy().split(" ");
			if(orderByAr != null && orderByAr.length > 0 && "theName".equals(orderByAr[0]))
			{
				model.setOrderBy(orderByAr[0]);
				model.setOrderByType(orderByAr[1]);
			}
		}
		
		Integer totalCount = sm_Permission_RoleDao.findByPage_Size(sm_Permission_RoleDao.getQuery_Size(sm_Permission_RoleDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Permission_Role> sm_Permission_RoleList;
		if(totalCount > 0)
		{
			sm_Permission_RoleList = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_Permission_RoleList = new ArrayList<Sm_Permission_Role>();
		}
		
		properties.put("sm_Permission_RoleList", sm_Permission_RoleList);
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

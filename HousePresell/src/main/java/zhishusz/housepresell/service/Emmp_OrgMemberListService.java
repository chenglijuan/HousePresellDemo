package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：机构成员
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_OrgMemberListService
{
	@Autowired
	private Emmp_OrgMemberDao emmp_OrgMemberDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_OrgMemberForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		Integer totalCount = emmp_OrgMemberDao.findByPage_Size(emmp_OrgMemberDao.getQuery_Size(emmp_OrgMemberDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Emmp_OrgMember> emmp_OrgMemberList;
		if(totalCount > 0)
		{
			emmp_OrgMemberList = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			emmp_OrgMemberList = new ArrayList<Emmp_OrgMember>();
		}
		
		properties.put("emmp_OrgMemberList", emmp_OrgMemberList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

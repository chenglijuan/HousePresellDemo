package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_FundProjectInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_FundProjectInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundProjectInfoListService
{
	@Autowired
	private Tgpf_FundProjectInfoDao tgpf_FundProjectInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundProjectInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_FundProjectInfoDao.findByPage_Size(tgpf_FundProjectInfoDao.getQuery_Size(tgpf_FundProjectInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_FundProjectInfo> tgpf_FundProjectInfoList;
		if(totalCount > 0)
		{
			tgpf_FundProjectInfoList = tgpf_FundProjectInfoDao.findByPage(tgpf_FundProjectInfoDao.getQuery(tgpf_FundProjectInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_FundProjectInfoList = new ArrayList<Tgpf_FundProjectInfo>();
		}
		
		properties.put("tgpf_FundProjectInfoList", tgpf_FundProjectInfoList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

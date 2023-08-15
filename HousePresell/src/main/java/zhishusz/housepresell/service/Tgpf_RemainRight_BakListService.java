package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_RemainRight_BakForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRight_BakDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight_Bak;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_RemainRight_BakListService
{
	@Autowired
	private Tgpf_RemainRight_BakDao tgpf_RemainRight_BakDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RemainRight_BakForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_RemainRight_BakDao.findByPage_Size(tgpf_RemainRight_BakDao.getQuery_Size(tgpf_RemainRight_BakDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_RemainRight_Bak> tgpf_RemainRight_BakList;
		if(totalCount > 0)
		{
			tgpf_RemainRight_BakList = tgpf_RemainRight_BakDao.findByPage(tgpf_RemainRight_BakDao.getQuery(tgpf_RemainRight_BakDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_RemainRight_BakList = new ArrayList<Tgpf_RemainRight_Bak>();
		}
		
		properties.put("tgpf_RemainRight_BakList", tgpf_RemainRight_BakList);
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

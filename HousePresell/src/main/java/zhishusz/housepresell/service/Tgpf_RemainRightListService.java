package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_RemainRightListService
{
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RemainRightForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = tgpf_RemainRightDao.findByPage_Size(tgpf_RemainRightDao.getQuery_Size(tgpf_RemainRightDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_RemainRight> tgpf_RemainRightList;
		if(totalCount > 0)
		{
			tgpf_RemainRightList = tgpf_RemainRightDao.findByPage(tgpf_RemainRightDao.getQuery(tgpf_RemainRightDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_RemainRightList = new ArrayList<Tgpf_RemainRight>();
		}
//		System.out.println("size = " + tgpf_RemainRightList.size());
		properties.put("tgpf_RemainRightList", tgpf_RemainRightList);
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

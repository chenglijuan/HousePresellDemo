package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
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
 * Service列表查询：银行网点(开户行)
 * Company：ZhiShuSZ
 */
@Service @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Emmp_BankBranchListService
{
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired 
	private Emmp_BankBranchDao emmp_BankBranchDao;

	@SuppressWarnings("unchecked") public Properties execute(Emmp_BankBranchForm model)
	{
		Properties properties = new MyProperties();
		if(StringUtils.isEmpty(model.getOrderBy())){
//			model.setOrderBy(null);
			model.setOrderBy(model.getTheName());
			model.setOrderByType("ASC");
		}else{
			String orderBy = model.getOrderBy();
			String[] split = orderBy.split(" ");
			if(split.length==2){
				model.setOrderBy(split[0]);
				model.setOrderByType(split[1]);
			}
		}

		model.setTheState(S_TheState.Normal);
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		if(keyword != null && keyword.length()>0)
		{
			model.setKeyword("%"+keyword+"%");
		}
		else {
			model.setKeyword(null);
		}

		Integer totalCount = emmp_BankBranchDao
				.findByPage_Size(emmp_BankBranchDao.getQuery_Size(emmp_BankBranchDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Emmp_BankBranch> emmp_BankBranchList;
		if (totalCount > 0)
		{
			emmp_BankBranchList = emmp_BankBranchDao
					.findByPage(emmp_BankBranchDao.getQuery(emmp_BankBranchDao.getBasicHQL(), model), pageNumber,
							countPerPage);
		}
		else
		{
			emmp_BankBranchList = new ArrayList<Emmp_BankBranch>();
		}

		properties.put("emmp_BankBranchList", emmp_BankBranchList);
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

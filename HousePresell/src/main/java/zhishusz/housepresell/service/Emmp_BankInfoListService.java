package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_BankInfoForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
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
 * Service列表查询：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_BankInfoListService
{
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_BankInfoForm model)
	{
		Properties properties = new MyProperties();
//		model.setOrderBy("theName asc");
		if(StringUtils.isEmpty(model.getOrderBy())){
			model.setOrderBy(null);
		}else {
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
		if(null == keyword || keyword.trim().isEmpty()){
			model.setKeyword(null);
		}else{
			model.setKeyword("%" + keyword + "%");
		}
		
		Integer totalCount = emmp_BankInfoDao.findByPage_Size(emmp_BankInfoDao.getQuery_Size(emmp_BankInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Emmp_BankInfo> emmp_BankInfoList;
		if(totalCount > 0)
		{
			emmp_BankInfoList = emmp_BankInfoDao.findByPage(emmp_BankInfoDao.getQuery(emmp_BankInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			emmp_BankInfoList = new ArrayList<Emmp_BankInfo>();
		}

		properties.put("emmp_BankInfoList", emmp_BankInfoList);
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

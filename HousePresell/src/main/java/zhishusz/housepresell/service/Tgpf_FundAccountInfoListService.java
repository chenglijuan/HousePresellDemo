package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_FundAccountInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAccountInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundAccountInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundAccountInfoListService
{
	@Autowired
	private Tgpf_FundAccountInfoDao tgpf_FundAccountInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAccountInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Tgpf_FundAccountInfoForm tgpf_FundAccountInfoForm = new Tgpf_FundAccountInfoForm();
		
		if("".equals(keyword)||null == keyword)
		{
			
		}else{
			
			keyword = "%" + keyword + "%";
			tgpf_FundAccountInfoForm.setKeyword(keyword);
		}
		tgpf_FundAccountInfoForm.setTheState(S_TheState.Normal);
		
		Integer totalCount = tgpf_FundAccountInfoDao.findByPage_Size(tgpf_FundAccountInfoDao.getQuery_Size(tgpf_FundAccountInfoDao.getSpecialHQL(), tgpf_FundAccountInfoForm));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_FundAccountInfo> tgpf_FundAccountInfoList;
		if(totalCount > 0)
		{
			tgpf_FundAccountInfoList = tgpf_FundAccountInfoDao.findByPage(tgpf_FundAccountInfoDao.getQuery(tgpf_FundAccountInfoDao.getSpecialHQL(), tgpf_FundAccountInfoForm), pageNumber, countPerPage);
		}
		else
		{
			tgpf_FundAccountInfoList = new ArrayList<Tgpf_FundAccountInfo>();
		}
		
		properties.put("tgpf_FundAccountInfoList", tgpf_FundAccountInfoList);
		properties.put(S_NormalFlag.keyword, model.getKeyword());
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_CoopAgreementSettleListService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{		
		Properties properties = new MyProperties();
		
		Sm_User userStart = model.getUser(); // admin
		
		// 获取代理公司
		Emmp_CompanyInfo agentCompany = userStart.getCompany();				
		if( S_CompanyType.Zhengtai.equals(agentCompany.getTheType()))
		{
			model.setAgentCompany(null);
		}
		else
		{
			model.setAgentCompany(agentCompany);
		}
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		if(null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{		
			model.setKeyword("%" + keyword + "%");
		}
		
		Integer totalCount = tgxy_CoopAgreementSettleDao.findByPage_Size(tgxy_CoopAgreementSettleDao.getQuery_Size(tgxy_CoopAgreementSettleDao.getSpecialHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_CoopAgreementSettle> tgxy_CoopAgreementSettleList;
		if(totalCount > 0)
		{
			tgxy_CoopAgreementSettleList = tgxy_CoopAgreementSettleDao.findByPage(tgxy_CoopAgreementSettleDao.getQuery(tgxy_CoopAgreementSettleDao.getSpecialHQL(), model), pageNumber, countPerPage);
			
//			for(Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle : tgxy_CoopAgreementSettleList)
//			{
//				if(null == tgxy_CoopAgreementSettle.getUserRecord())
//				{
//					tgxy_CoopAgreementSettle.setRecordName("");
//				}else
//				{
//					tgxy_CoopAgreementSettle.setRecordName(tgxy_CoopAgreementSettle.getUserRecord().getTheName());
//				}
//			}
		}
		else
		{
			tgxy_CoopAgreementSettleList = new ArrayList<Tgxy_CoopAgreementSettle>();
		}
		
		properties.put("tgxy_CoopAgreementSettleList", tgxy_CoopAgreementSettleList);
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

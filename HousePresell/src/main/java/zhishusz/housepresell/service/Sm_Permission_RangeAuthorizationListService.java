package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.OracleOrder;

/*
 * Service列表查询：范围授权列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RangeAuthorizationListService
{
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	/**
	 * 普通机构范围授权列表
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Checker checker = Checker.getInstance();
		MyDatetime myDatetime = MyDatetime.getInstance();
		Properties properties = new MyProperties();
		
		Integer pageNumber = checker.checkPageNumber(model.getPageNumber());
		Integer countPerPage = checker.checkCountPerPage(model.getCountPerPage());
		
		String keyword_Old = model.getKeyword();
		String keyword = checker.checkKeyword(model.getKeyword());
		model.setKeyword(keyword);
		
		String forCompanyType = (model.getForCompanyType() == null || model.getForCompanyType().length() == 0) ? null : model.getForCompanyType();
		model.setForCompanyType(forCompanyType);
		
		Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(model.getEmmp_CompanyInfoId());
		model.setEmmp_CompanyInfo(emmp_CompanyInfo);
		
		String authTimeStampRange = model.getAuthTimeStampRange();
		Long authTimeStampStart = myDatetime.getDateTimeStampMin(authTimeStampRange);
		Long authTimeStampEnd = myDatetime.getDateTimeStampMax(authTimeStampRange);
		model.setAuthStartTimeStamp(authTimeStampStart);
		model.setAuthEndTimeStamp(authTimeStampEnd);
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = sm_Permission_RangeAuthorizationDao.findByPage_Size(sm_Permission_RangeAuthorizationDao.createCriteriaForList(model, null));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_Permission_RangeAuthorization> sm_Permission_RangeAuthorizationList;
		if(totalCount > 0)
		{
			sm_Permission_RangeAuthorizationList = sm_Permission_RangeAuthorizationDao.findByPage(sm_Permission_RangeAuthorizationDao.createCriteriaForList(model, getOrder(model)), pageNumber, countPerPage);
		}
		else
		{
			sm_Permission_RangeAuthorizationList = new ArrayList<Sm_Permission_RangeAuthorization>();
		}
		
		properties.put("sm_Permission_RangeAuthorizationList", sm_Permission_RangeAuthorizationList);
		properties.put(S_NormalFlag.keyword, keyword_Old);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	public Order getOrder(BaseForm model)
	{
		String orderBy = model.getOrderBy();
		
		String propertyName = null;
		if(orderBy != null && orderBy.endsWith(" asc"))
		{
			propertyName = orderBy.split(" asc")[0];
			if("companyType".equals(propertyName))
			{
				propertyName = "forCompanyType";
			}
			else if("companyName".equals(propertyName))
			{
				propertyName = "comInfo.theName";
				return OracleOrder.asc(propertyName);
			}
			return Order.asc(propertyName);
		}
		else if(orderBy != null && orderBy.endsWith(" desc"))
		{
			propertyName = orderBy.split(" desc")[0];
			if("companyType".equals(propertyName))
			{
				propertyName = "forCompanyType";
			}
			else if("companyName".equals(propertyName))
			{
				propertyName = "comInfo.theName";
				return OracleOrder.desc(propertyName);
			}
			return Order.desc(propertyName);
		}
		
		return null;
	}
}

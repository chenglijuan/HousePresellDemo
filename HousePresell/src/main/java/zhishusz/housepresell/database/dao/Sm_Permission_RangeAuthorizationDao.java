package zhishusz.housepresell.database.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;

/*
 * Dao数据库操作：角色授权
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_RangeAuthorizationDao extends BaseDao<Sm_Permission_RangeAuthorization>
{
	/*public String getSizeHQL()
	{
		String condition = getBasicHQL();
		return "from Sm_Permission_RangeAuthorization rangeAuth"
		+ " inner join rangeAuth.emmp_CompanyInfo comInfo where 1=1"
		+ condition;
	}
	public String getListHQL()
    {
		String condition = getBasicHQL();
		return "from Sm_Permission_RangeAuthorization rangeAuth"
		+ " inner join fetch rangeAuth.emmp_CompanyInfo comInfo where 1=1"
		+ condition;
    }
	
	public String getBasicHQL()
    {
    	return " <#if exceptTableId??> and rangeAuth.tableId !=:exceptTableId</#if>"
		+ " <#if eCode??> and rangeAuth.eCode=:eCode</#if>"
		+ " <#if theName??> and rangeAuth.theName=:theName</#if>"
		+ " <#if remark??> and rangeAuth.remark=:remark</#if>"
		+ " <#if theState??> and rangeAuth.theState=:theState</#if>"
		+ " <#if busiType??> and rangeAuth.busiType=:busiType</#if>"
		+ " <#if forCompanyType??> and comInfo.theType=:forCompanyType</#if>"
		+ " <#if emmp_CompanyInfo??> and rangeAuth.emmp_CompanyInfo =:emmp_CompanyInfo</#if>"
		+ " <#if authStartTimeStamp??> and rangeAuth.authStartTimeStamp <=:authStartTimeStamp</#if>"
		+ " <#if authEndTimeStamp??> and rangeAuth.authEndTimeStamp >=:authEndTimeStamp</#if>"
		+ " <#if keyword??> and comInfo.theName like :keyword</#if>";
    }
	
	public String getSizeHQL_ForZT()
	{
		String condition = getBasicHQL_ForZT();
		return "from Sm_Permission_RangeAuthorization rangeAuth"
		+ " inner join rangeAuth.userInfo userInfo where 1=1"
		+ condition;
	}
	public String getListHQL_ForZT()
	{
		String condition = getBasicHQL_ForZT();
		return "from Sm_Permission_RangeAuthorization rangeAuth"
		+ " inner join fetch rangeAuth.userInfo userInfo where 1=1"
		+ condition;
	}
	
	public String getBasicHQL_ForZT()
	{
		return  " and userInfo is not null"
				+" <#if exceptTableId??> and rangeAuth.tableId !=:exceptTableId</#if>"
				+ " <#if eCode??> and rangeAuth.eCode=:eCode</#if>"
				+ " <#if theName??> and rangeAuth.theName=:theName</#if>"
				+ " <#if remark??> and rangeAuth.remark=:remark</#if>"
				+ " <#if theState??> and rangeAuth.theState=:theState</#if>"
				+ " <#if userInfo??> and rangeAuth.userInfo=:userInfo</#if>"
				+ " <#if busiType??> and rangeAuth.busiType=:busiType</#if>"
				+ " <#if authStartTimeStamp??> and rangeAuth.authStartTimeStamp <=:authStartTimeStamp</#if>"
				+ " <#if authEndTimeStamp??> and rangeAuth.authEndTimeStamp >=:authEndTimeStamp</#if>"
				+ " <#if keyword??> and userInfo.theName like :keyword</#if>";
	}*/
	
	public String getExcelListHQL()
    {
    	return "from Sm_Permission_RangeAuthorization where 1=1"
    	+ " <#if idArr??> and tableId in :idArr</#if>"
    	+ " <#if emmp_CompanyInfoId??> and emmp_CompanyInfo.tableId =:emmp_CompanyInfoId</#if>";
    }
	
	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	@SuppressWarnings("unchecked")
	public Criteria createCriteriaForList(Sm_Permission_RangeAuthorizationForm model, Order order)
	{
		Criteria criteria = createCriteria()
				.createAlias("emmp_CompanyInfo", "comInfo");
		
		if(order != null)
		{
			criteria.addOrder(order);
		}
		
		Long exceptTableId = model.getExceptTableId();
		if(exceptTableId != null)
		{
			criteria.add(Restrictions.ne("tableId", exceptTableId));
		}
		
		String eCode = model.geteCode();
		if(eCode != null && eCode.length() > 0)
		{
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		
		Integer theState = model.getTheState();
		if(theState != null)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
		
		String forCompanyType = model.getForCompanyType();
		if(forCompanyType != null && forCompanyType.length() > 0)
		{
			criteria.add(Restrictions.eq("comInfo.theType", forCompanyType));
		}
		
		Emmp_CompanyInfo emmp_CompanyInfo = model.getEmmp_CompanyInfo();
		if(emmp_CompanyInfo != null)
		{
			criteria.add(Restrictions.eq("emmp_CompanyInfo", emmp_CompanyInfo));
		}
		
		Long authStartTimeStamp = model.getAuthStartTimeStamp();
		if(authStartTimeStamp != null)
		{
			criteria.add(Restrictions.le("authStartTimeStamp", authStartTimeStamp));
		}

		Long authEndTimeStamp = model.getAuthEndTimeStamp();
		if(authEndTimeStamp != null)
		{
			criteria.add(Restrictions.ge("authEndTimeStamp", authEndTimeStamp));
		}
		
		Integer rangeAuthType = model.getRangeAuthType();
		if(rangeAuthType != null)
		{
			criteria.add(Restrictions.eq("rangeAuthType", rangeAuthType));
		}
		
		String keyword = model.getKeyword();
		if (keyword != null && keyword.length() != 0)
		{
			//区域/项目名称/楼幢
			//keyword = "%" + keyword + "%";
			Criteria criteria_Region = createCriteria(Sm_Permission_Range.class)
					.add(Restrictions.eq("theType", S_RangeAuthType.Area))
					.createAlias("cityRegionInfo", "cityRegionInfo")
					.add(Restrictions.like("cityRegionInfo.theName", keyword))
					.setProjection(Projections.property("companyInfo"));
			List<Emmp_CompanyInfo> company_RegionList = criteria_Region.list();
			if(company_RegionList == null)
			{
				company_RegionList = new ArrayList<Emmp_CompanyInfo>();
			}
			
			Criteria criteria_Project = createCriteria(Sm_Permission_Range.class)
					.add(Restrictions.eq("theType", S_RangeAuthType.Project))
					.createAlias("projectInfo", "projectInfo")
					.add(Restrictions.like("projectInfo.theName", keyword))
					.setProjection(Projections.property("companyInfo"));
			List<Emmp_CompanyInfo> company_ProjectList = criteria_Project.list();
			if(company_ProjectList == null)
			{
				company_ProjectList = new ArrayList<Emmp_CompanyInfo>();
			}
			
			Criteria criteria_Building = createCriteria(Sm_Permission_Range.class)
					.add(Restrictions.eq("theType", S_RangeAuthType.Building))
					.createAlias("buildingInfo", "buildingInfo")
					.add(Restrictions.like("buildingInfo.eCodeFromConstruction", keyword))
					.setProjection(Projections.property("companyInfo"));
			List<Emmp_CompanyInfo> company_BuildingList = criteria_Building.list();
			if(company_BuildingList == null)
			{
				company_BuildingList = new ArrayList<Emmp_CompanyInfo>();
			}
			
			List<Emmp_CompanyInfo> range_List = new ArrayList<Emmp_CompanyInfo>();
			range_List.addAll(company_RegionList);
			range_List.addAll(company_ProjectList);
			range_List.addAll(company_BuildingList);
			if(range_List != null && range_List.size() > 0)
			{
				criteria.add(Restrictions.in("emmp_CompanyInfo", range_List));
			}
		}
		
		return criteria;
	}
	
	public Criteria createCriteriaForList_ForZT(Sm_Permission_RangeAuthorizationForm model, Order order)
	{
		Criteria criteria = createCriteria()
				.createAlias("userInfo", "userInfo")
				.add(Restrictions.isNotNull("userInfo"));
		
		if(order != null)
		{
			criteria.addOrder(order);
		}
		
		Long exceptTableId = model.getExceptTableId();
		if(exceptTableId != null)
		{
			criteria.add(Restrictions.ne("tableId", exceptTableId));
		}
		
		String eCode = model.geteCode();
		if(eCode != null && eCode.length() > 0)
		{
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		
		Integer theState = model.getTheState();
		if(theState != null)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
		
		Sm_User userInfo = model.getUserInfo();
		if(userInfo != null)
		{
			criteria.add(Restrictions.eq("userInfo", userInfo));
		}
		
		Long authStartTimeStamp = model.getAuthStartTimeStamp();
		if(authStartTimeStamp != null)
		{
			criteria.add(Restrictions.le("authStartTimeStamp", authStartTimeStamp));
		}

		Long authEndTimeStamp = model.getAuthEndTimeStamp();
		if(authEndTimeStamp != null)
		{
			criteria.add(Restrictions.ge("authEndTimeStamp", authEndTimeStamp));
		}
		
		String keyword = model.getKeyword();
		if (keyword != null && keyword.length() != 0)
		{
			//keyword = "%" + keyword + "%";
			criteria.add(Restrictions.disjunction()
				.add(Restrictions.like("userInfo.theName", keyword))
			);
		}
		
		return criteria;
	}
	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 End =======//
}

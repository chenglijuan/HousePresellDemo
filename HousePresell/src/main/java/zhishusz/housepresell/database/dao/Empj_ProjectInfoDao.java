package zhishusz.housepresell.database.dao;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;

/*
 * Dao数据库操作：项目信息
 * Company：ZhiShuSZ
 */
@Repository
public class Empj_ProjectInfoDao extends BaseDao<Empj_ProjectInfo>
{
	public String getBasicHQL()
    {
    	return "from Empj_ProjectInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if zoneCode??> and zoneCode=:zoneCode</#if>"
		+ " <#if address??> and address=:address</#if>"
		+ " <#if doorNumber??> and doorNumber=:doorNumber</#if>"
		+ " <#if doorNumberAnnex??> and doorNumberAnnex=:doorNumberAnnex</#if>"
		+ " <#if introduction??> and introduction=:introduction</#if>"
		+ " <#if longitude??> and longitude=:longitude</#if>"
		+ " <#if latitude??> and latitude=:latitude</#if>"
		+ " <#if propertyType??> and propertyType=:propertyType</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if legalName??> and legalName=:legalName</#if>"
		+ " <#if buildYear??> and buildYear=:buildYear</#if>"
		+ " <#if isPartition??> and isPartition=:isPartition</#if>"
		+ " <#if theProperty??> and theProperty=:theProperty</#if>"
		+ " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
		+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
		+ " <#if projectLeader??> and projectLeader=:projectLeader</#if>"
		+ " <#if leaderPhone??> and leaderPhone=:leaderPhone</#if>"
		+ " <#if landArea??> and landArea=:landArea</#if>"
		+ " <#if obtainMethod??> and obtainMethod=:obtainMethod</#if>"
		+ " <#if investment??> and investment=:investment</#if>"
		+ " <#if landInvest??> and landInvest=:landInvest</#if>"
		+ " <#if coverArea??> and coverArea=:coverArea</#if>"
		+ " <#if houseArea??> and houseArea=:houseArea</#if>"
		+ " <#if siteArea??> and siteArea=:siteArea</#if>"
		+ " <#if planArea??> and planArea=:planArea</#if>"
		+ " <#if agArea??> and agArea=:agArea</#if>"
		+ " <#if ugArea??> and ugArea=:ugArea</#if>"
		+ " <#if greenRatio??> and greenRatio=:greenRatio</#if>"
		+ " <#if capacity??> and capacity=:capacity</#if>"
		+ " <#if parkRatio??> and parkRatio=:parkRatio</#if>"
		+ " <#if unitCount??> and unitCount=:unitCount</#if>"
		+ " <#if buildingCount??> and buildingCount=:buildingCount</#if>"
		+ " <#if payDate??> and payDate=:payDate</#if>"
		+ " <#if planStartDate??> and planStartDate=:planStartDate</#if>"
		+ " <#if planEndDate??> and planEndDate=:planEndDate</#if>"
		+ " <#if developDate??> and developDate=:developDate</#if>"
		+ " <#if eCodeOfDesignCompany??> and eCodeOfDesignCompany=:eCodeOfDesignCompany</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if developProgress??> and developProgress=:developProgress</#if>"
		+ " <#if eastAddress??> and eastAddress=:eastAddress</#if>"
		+ " <#if eastLongitude??> and eastLongitude=:eastLongitude</#if>"
		+ " <#if eastLatitude??> and eastLatitude=:eastLatitude</#if>"
		+ " <#if westAddress??> and westAddress=:westAddress</#if>"
		+ " <#if westLongitude??> and westLongitude=:westLongitude</#if>"
		+ " <#if westLatitude??> and westLatitude=:westLatitude</#if>"
		+ " <#if southAddress??> and southAddress=:southAddress</#if>"
		+ " <#if southLongitude??> and southLongitude=:southLongitude</#if>"
		+ " <#if southLatitude??> and southLatitude=:southLatitude</#if>"
		+ " <#if northAddress??> and northAddress=:northAddress</#if>"
		+ " <#if northLongitude??> and northLongitude=:northLongitude</#if>"
		+ " <#if northLatitude??> and northLatitude=:northLatitude</#if>"
		+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
		+ " <#if cityRegionId??> and cityRegion.tableId=:cityRegionId</#if>"
		+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
		
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
			+ " <#list projectInfoIdArr as projectInfoId>"
				+ " <#if projectInfoId_index == 0> (tableId = ${projectInfoId?c} )</#if>"
				+ " <#if projectInfoId_index != 0> or (tableId = ${projectInfoId?c} )</#if>"
			+ " </#list>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
		+ " </#if>"
		
		+ " <#if keyword??> and CONCAT(eCode, theName, address, contactPerson, contactPhone) like :keyword </#if>"
		+ " ORDER BY CREATETIMESTAMP DESC";
    }
	
	public String getExcelListHQL() 
	{
		return "from Empj_ProjectInfo where 1=1" 
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}
	
	public String getListByIdArrHQL()
	{
		return "from Empj_ProjectInfo where 1=1"
				+ " and theState = "+S_TheState.Normal
				+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and cityRegion.tableId in :cityRegionInfoIdArr</#if>"
//				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and tableId in :projectInfoIdArr</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>";
	}

	/**
	 * 判断是否已有同名项目名称（!(busiState='未备案' && approvalState='待提交')）
	 * @return String hql
	 */
	public String getSameProjectNameListHQL()
	{
		//如果S_TheState，S_ApprovalState中枚举值非String类型，就需要注意符号''
		return "from Empj_ProjectInfo where 1=1"
				+ " <#if tableId??> and tableId != :tableId </#if>"
				+ " <#if theName??> and theName=:theName</#if>"
				+ " and theState = "+S_TheState.Normal
				+ " and (busiState = "+"'"+S_BusiState.HaveRecord+"'"
				+ " or (busiState = "+"'"+S_BusiState.NoRecord+"' and approvalState = "+"'"+S_ApprovalState.Examining+"'))";
	}

	/*
	 * xsz by time 2018-9-4 14:40:21
	 * 用于根据Id匹配主键或外来主键信息查询详情
	 */
	public String getDetailHql() 
	{
    	return "from Empj_ProjectInfo where 1=1 and theState=0 "
    			+ " <#if tableId??> and tableId = :tableId </#if>"
    			+ " <#if externalId??> and externalId = :externalId </#if>";
    			
    }

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public Criteria createNewCriteriaForList(Empj_ProjectInfoForm model)
	{
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "company")
				.createAlias("cityRegion", "city")
				.createAlias("userStart", "userStart");

		Integer theState = model.getTheState();
		if (theState != null) {
			criteria.add(Restrictions.eq("theState", theState));
		}
		String busiState = model.getBusiState();
		if (busiState != null) {
			criteria.add(Restrictions.eq("busiState", busiState));
		}
		String eCode = model.geteCode();
		if (eCode != null) {
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		String theName = model.getTheName();
		if (theName != null) {
			criteria.add(Restrictions.eq("theName", theName));
		}

		if (model.getDevelopCompanyId() != null)
		{
			criteria.add(Restrictions.eq("company.tableId", model.getDevelopCompanyId()));
		}
		if (model.getCityRegionId() != null)
		{
			criteria.add(Restrictions.eq("city.tableId", model.getCityRegionId()));
		}

		String keyword = getKeyWord(model);
		if (keyword != null && keyword.length() != 0)
		{
			//keyword = "%" + keyword + "%";
			criteria.add(Restrictions.like("theName", keyword));
//			criteria.add(Restrictions.disjunction()
//							.add(Restrictions.like("theName", keyword))
//							.add(Restrictions.like("developCompany.theName", keyword))
//							.add(Restrictions.like("project.theName", keyword))
//			);
		}

//		addCompanyLimitRange(criteria, model);

		if(model.getCityRegionInfoIdArr()!=null && model.getCityRegionInfoIdArr().length>0)
		{
			if(model.getUserId() != null)
			{
				criteria.add(Restrictions.disjunction()
						.add(Restrictions.in("city.tableId", model.getCityRegionInfoIdArr()))
						.add(Restrictions.eq("userStart.tableId", model.getUserId()))
				);
			}
			else
			{
				criteria.add(Restrictions.in("city.tableId", model.getCityRegionInfoIdArr()));
			}
		}
		if(model.getProjectInfoIdArr()!=null && model.getProjectInfoIdArr().length>0)
		{
			if(model.getUserId() != null)
			{
				criteria.add(Restrictions.disjunction()
						.add(Restrictions.in("tableId", model.getProjectInfoIdArr()))
						.add(Restrictions.eq("userStart.tableId", model.getUserId()))
				);
			}
			else
			{
				criteria.add(Restrictions.in("tableId", model.getProjectInfoIdArr()));
			}
		}

		//未指定↓↑箭头的用Oracle默认排序，项目信息未特别指定默认排序
		if (StringUtils.isEmpty(model.getOrderBy())) {
//			criteria.addOrder(Order.desc("createTimeStamp"));
		} else {
			ArrayList<String> pinYinList = new ArrayList<>();
			pinYinList.add("theName");
			pinYinList.add("company.theName");
			pinYinList.add("city.theName");
			addCriteriaListOrder(criteria, model, pinYinList);
		}

		return criteria;
	}
}

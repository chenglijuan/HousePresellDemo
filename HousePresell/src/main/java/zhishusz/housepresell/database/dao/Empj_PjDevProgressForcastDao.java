package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;

import java.util.ArrayList;

/*
 * Dao数据库操作：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_PjDevProgressForcastDao extends BaseDao<Empj_PjDevProgressForcast>
{
//	public String getSizeHQL()
//	{
//		String condition = getBasicHQL();
//		return "from Empj_PjDevProgressForcast pjDevProgressForcast"
//				+ " inner join pjDevProgressForcast.developCompany developCompany"
//				+ " inner join pjDevProgressForcast.building building"
//				+ " inner join pjDevProgressForcast.project project where 1=1"
//				+ condition;
//	}
//
//	public String getListHQL()
//	{
//		String condition = getBasicHQL();
//		return "from Empj_PjDevProgressForcast pjDevProgressForcast"
//				+ " inner join fetch pjDevProgressForcast.developCompany developCompany"
//				+ " inner join fetch pjDevProgressForcast.building building"
//				+ " inner join fetch pjDevProgressForcast.project project where 1=1"
//				+ condition;
//	}
//
//	public String getBasicHQL()
//    {
//    	return " <#if theState??> and pjDevProgressForcast.theState=:theState</#if>"
//		+ " <#if busiState??> and pjDevProgressForcast.busiState=:busiState</#if>"
//		+ " <#if eCode??> and pjDevProgressForcast.eCode=:eCode</#if>"
//		+ " <#if createTimeStamp??> and pjDevProgressForcast.createTimeStamp=:createTimeStamp</#if>"
//		+ " <#if lastUpdateTimeStamp??> and pjDevProgressForcast.lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
//		+ " <#if recordTimeStamp??> and pjDevProgressForcast.recordTimeStamp=:recordTimeStamp</#if>"
//		+ " <#if eCodeOfDevelopCompany??> and pjDevProgressForcast.eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
//		+ " <#if theNameOfProject??> and pjDevProgressForcast.theNameOfProject=:theNameOfProject</#if>"
//		+ " <#if eCodeOfProject??> and pjDevProgressForcast.eCodeOfProject=:eCodeOfProject</#if>"
//		+ " <#if eCodeOfBuilding??> and pjDevProgressForcast.eCodeOfBuilding=:eCodeOfBuilding</#if>"
//		+ " <#if eCodeFromConstruction??> and pjDevProgressForcast.eCodeFromConstruction=:eCodeFromConstruction</#if>"
//		+ " <#if eCodeFromPublicSecurity??> and pjDevProgressForcast.eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
//		+ " <#if theNameOfCityRegion??> and pjDevProgressForcast.theNameOfCityRegion=:theNameOfCityRegion</#if>"
//		+ " <#if theNameOfStreet??> and pjDevProgressForcast.theNameOfStreet=:theNameOfStreet</#if>"
//		+ " <#if payoutType??> and pjDevProgressForcast.payoutType=:payoutType</#if>"
//		+ " <#if currentFigureProgress??> and pjDevProgressForcast.currentFigureProgress=:currentFigureProgress</#if>"
//		+ " <#if currentBuildProgress??> and pjDevProgressForcast.currentBuildProgress=:currentBuildProgress</#if>"
//		+ " <#if patrolPerson??> and pjDevProgressForcast.patrolPerson=:patrolPerson</#if>"
//		+ " <#if patrolTimestamp??> and pjDevProgressForcast.patrolTimestamp=:patrolTimestamp</#if>"
//		+ " <#if patrolInstruction??> and pjDevProgressForcast.patrolInstruction=:patrolInstruction</#if>"
//		+ " <#if remark??> and pjDevProgressForcast.remark=:remark</#if>"
//		+ " <#if keyword??> and (pjDevProgressForcast.eCode like :keyword or"
//		+ " pjDevProgressForcast.eCodeOfBuilding like :keyword or"
//		+ " pjDevProgressForcast.eCodeFromConstruction like :keyword or"
//		+ " pjDevProgressForcast.currentBuildProgress like :keyword or"
//		+ " pjDevProgressForcast.patrolTimestamp like :keyword or"
//		+ " developCompany.theName like :keyword or"
//		+ " project.theName like :keyword)</#if>";
//    }
	
	public String getBasictHQL()
	{
		return "from Empj_PjDevProgressForcast where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if eCode??> and eCode=:eCode</#if>";
	}

	public String getExcelListHQL()
	{
		return "from Empj_PjDevProgressForcast where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public Criteria createCriteriaForList(Empj_PjDevProgressForcastForm model, Order order)
	{
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "developCompany")
				.createAlias("building", "building")
				.createAlias("project", "project");

		Integer theState = model.getTheState();
		if (theState != null)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
		String busiState = model.getBusiState();
		if (busiState != null)
		{
			criteria.add(Restrictions.eq("busiState", busiState));
		}
		String eCode = model.geteCode();
		if (eCode != null)
		{
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		Long createTimeStamp = model.getCreateTimeStamp();
		if (createTimeStamp != null)
		{
			criteria.add(Restrictions.eq("createTimeStamp", createTimeStamp));
		}
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		if (lastUpdateTimeStamp != null)
		{
			criteria.add(Restrictions.eq("lastUpdateTimeStamp", lastUpdateTimeStamp));
		}
		Long recordTimeStamp = model.getRecordTimeStamp();
		if (recordTimeStamp != null)
		{
			criteria.add(Restrictions.eq("recordTimeStamp", recordTimeStamp));
		}
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		if (eCodeFromConstruction != null)
		{
			criteria.add(Restrictions.eq("eCodeFromConstruction", eCodeFromConstruction));
		}
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		if (eCodeFromPublicSecurity != null)
		{
			criteria.add(Restrictions.eq("eCodeFromPublicSecurity", eCodeFromPublicSecurity));
		}
		String payoutType = model.getPayoutType();
		if (payoutType != null)
		{
			criteria.add(Restrictions.eq("payoutType", payoutType));
		}
		Double currentFigureProgress = model.getCurrentFigureProgress();
		if (currentFigureProgress != null)
		{
			criteria.add(Restrictions.eq("currentFigureProgress", currentFigureProgress));
		}
		String currentBuildProgress = model.getCurrentBuildProgress();
		if (currentBuildProgress != null)
		{
			criteria.add(Restrictions.eq("currentBuildProgress", currentBuildProgress));
		}
		String patrolPerson = model.getPatrolPerson();
		if (patrolPerson != null)
		{
			criteria.add(Restrictions.eq("patrolPerson", patrolPerson));
		}
		String patrolTimestamp = model.getPatrolTimestamp();
		if (patrolTimestamp != null)
		{
			criteria.add(Restrictions.eq("patrolTimestamp", patrolTimestamp));
		}
		String patrolInstruction = model.getPatrolInstruction();
		if (patrolInstruction != null)
		{
			criteria.add(Restrictions.eq("patrolInstruction", patrolInstruction));
		}
		String remark = model.getRemark();
		if (remark != null)
		{
			criteria.add(Restrictions.eq("remark", remark));
		}

		String keyword = model.getKeyword();
		if (keyword != null && keyword.length() != 0)
		{
			//keyword = "%" + keyword + "%";
			criteria.add(Restrictions.disjunction()
							.add(Restrictions.like("eCode", keyword))
							.add(Restrictions.like("eCodeFromConstruction", keyword))
							.add(Restrictions.like("developCompany.theName", keyword))
							.add(Restrictions.like("project.theName", keyword))
							.add(Restrictions.like("building.eCode", keyword))
//					.add(Restrictions.like("building.buildingArea", keyword))
//					.add(Restrictions.like("building.escrowArea", keyword))
			);
		}

		if(order != null)
		{
			criteria.addOrder(order);
		}

		return criteria;
	}

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public Criteria createNewCriteriaForList(Empj_PjDevProgressForcastForm model)
	{
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "company")
				.createAlias("project", "project")
				.createAlias("building", "building");

		Integer theState = model.getTheState();
		if (theState != null)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
//		String busiState = model.getBusiState();
//		if (busiState != null)
//		{
//			criteria.add(Restrictions.eq("busiState", busiState));
//		}
		String eCode = model.geteCode();
		if (eCode != null)
		{
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		if (eCodeFromConstruction != null)
		{
			criteria.add(Restrictions.eq("eCodeFromConstruction", eCodeFromConstruction));
		}
		if (model.getDevelopCompanyId() != null)
		{
			criteria.add(Restrictions.eq("company.tableId", model.getDevelopCompanyId()));
		}
		if (model.getProjectId() != null) {
			criteria.add(Restrictions.eq("project.tableId", model.getProjectId()));
		}

		String keyword = getKeyWord(model);
		if (keyword != null && keyword.length() != 0)
		{
			//keyword = "%" + keyword + "%";
			criteria.add(Restrictions.disjunction()
							.add(Restrictions.like("eCode", keyword))
							.add(Restrictions.like("building.eCodeFromConstruction", keyword))
//							.add(Restrictions.like("developCompany.theName", keyword))
//							.add(Restrictions.like("developCompany.theName", keyword))
//							.add(Restrictions.like("project.theName", keyword))
			);
		}

		if(model.getProjectInfoIdArr()!=null && model.getProjectInfoIdArr().length>0)
		{
			if(model.getUserId() != null)
			{
				criteria.add(Restrictions.disjunction()
						.add(Restrictions.in("project.tableId", model.getProjectInfoIdArr()))
						.add(Restrictions.eq("userStart.tableId", model.getUserId()))
				);
			}
			else
			{
				criteria.add(Restrictions.in("project.tableId", model.getProjectInfoIdArr()));
			}
		}
		if(model.getBuildingInfoIdIdArr()!=null && model.getBuildingInfoIdIdArr().length>0)
		{
			if(model.getUserId() != null)
			{
				if(model.getBuildingInfoIdIdArr().length>999)
				{
					criteria.add(Restrictions.disjunction().add(Restrictions.eq("userStart.tableId", model.getUserId())));
					
					for (int i = 0; i < model.getBuildingInfoIdIdArr().length; i++)
					{
						if(i == 0)
						{
							criteria.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()[i]));
						}
						else
						{
							criteria.add(Restrictions.or(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()[i])));
						}
					}
				}
				else
				{
					criteria.add(Restrictions.disjunction()
							.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()))
							.add(Restrictions.eq("userStart.tableId", model.getUserId()))
					);
				}
				
			}
			else
			{
				
				if(model.getBuildingInfoIdIdArr().length>999)
				{
					criteria.add(Restrictions.disjunction());
					
					for (int i = 0; i < model.getBuildingInfoIdIdArr().length; i++)
					{
						if(i == 0)
						{
							criteria.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()[i]));
						}
						else
						{
							criteria.add(Restrictions.or(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()[i])));
						}
					}
				}
				else
				{
					criteria.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()));
				}
//				criteria.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()));
			}
		}

//		addCriteriaListOrder(criteria, model);
		//默认根据施工编号升序
		if (StringUtils.isEmpty(model.getOrderBy())) {
			criteria.addOrder(Order.asc("building.eCodeFromConstruction"));
		} else {
			ArrayList<String> pinYinList = new ArrayList<>();
			pinYinList.add("company.theName");
			pinYinList.add("project.theName");
			addCriteriaListOrder(criteria, model, pinYinList);
		}

		return criteria;
	}

	/**
	 * 查找同一楼幢是否已有工程进度预测信息
	 * @param model 工程进度预测表单
	 * @return Criteria 结果集
	 */
	public Criteria getPjDevProgressForcastCountForList(Empj_PjDevProgressForcastForm model) {
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "company")
				.createAlias("project", "project")
				.createAlias("building", "building");

//		Integer theState = model.getTheState();
//		if (theState != null) {
//			criteria.add(Restrictions.eq("theState", theState));
//		}
		criteria.add(Restrictions.eq("theState", S_TheState.Normal));

		if (model.getDevelopCompanyId() != null)
		{
			criteria.add(Restrictions.eq("company.tableId", model.getDevelopCompanyId()));
		}
		if (model.getProjectId() != null) {
			criteria.add(Restrictions.eq("project.tableId", model.getProjectId()));
		}
		if (model.getBuildingId() != null) {
			criteria.add(Restrictions.eq("building.tableId", model.getBuildingId()));
		}
		return criteria;
	}
}

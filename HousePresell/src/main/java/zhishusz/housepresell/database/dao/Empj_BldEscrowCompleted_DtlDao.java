package zhishusz.housepresell.database.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;

/*
 * Dao数据库操作：申请表-项目托管终止（审批）-明细表
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BldEscrowCompleted_DtlDao extends BaseDao<Empj_BldEscrowCompleted_Dtl>
{
//	public String getSizeHQL()
//	{
//		String condition = getBasicHQL();
//		return "from Empj_BldEscrowCompleted_Dtl bldEscrowCompleted"
//		+ " inner join bldEscrowCompleted.developCompany developCompany"
//		+ " inner join bldEscrowCompleted.mainTable mainTable"
//		+ " inner join bldEscrowCompleted.building building"
//		+ " inner join bldEscrowCompleted.project project where 1=1"
//		+ condition;
//	}
//	public String getListHQL()
//    {
//		String condition = getBasicHQL();
//		return "from Empj_BldEscrowCompleted_Dtl bldEscrowCompleted"
//		+ " inner join fetch bldEscrowCompleted.developCompany developCompany"
//		+ " inner join fetch bldEscrowCompleted.mainTable mainTable"
//		+ " inner join fetch bldEscrowCompleted.building building"
//		+ " inner join fetch bldEscrowCompleted.project project where 1=1"
//		+ condition;
//    }
//	public String getBasicHQL()
//    {
//		return " <#if theState??> and bldEscrowCompleted.theState=:theState</#if>"
//		+ " <#if busiState??> and bldEscrowCompleted.busiState=:busiState</#if>"
//		+ " <#if eCode??> and bldEscrowCompleted.eCode=:eCode</#if>"
//		+ " <#if createTimeStamp??> and bldEscrowCompleted.createTimeStamp=:createTimeStamp</#if>"
//		+ " <#if lastUpdateTimeStamp??> and bldEscrowCompleted.lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
//		+ " <#if recordTimeStamp??> and bldEscrowCompleted.recordTimeStamp=:recordTimeStamp</#if>"
//		+ " <#if eCodeOfMainTable??> and bldEscrowCompleted.eCodeOfMainTable=:eCodeOfMainTable</#if>"
//		+ " <#if eCodeOfDevelopCompany??> and bldEscrowCompleted.eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
//		+ " <#if theNameOfProject??> and bldEscrowCompleted.theNameOfProject=:theNameOfProject</#if>"
//		+ " <#if eCodeOfProject??> and bldEscrowCompleted.eCodeOfProject=:eCodeOfProject</#if>"
//		+ " <#if eCodeOfBuilding??> and bldEscrowCompleted.eCodeOfBuilding=:eCodeOfBuilding</#if>"
//		+ " <#if eCodeFromPublicSecurity??> and bldEscrowCompleted.eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
//		+ " <#if eCodeFromConstruction??> and bldEscrowCompleted.eCodeFromConstruction=:eCodeFromConstruction</#if>"
//		+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
//		+ " <#if projectId??> and project.tableId=:projectId</#if>"
//		+ " <#if keyword??> and (bldEscrowCompleted.eCodeOfBuilding like :keyword or"
//		+ " developCompany.theName like :keyword or"
//		+ " mainTable.eCodeFromDRAD like :keyword or"
//		+ " mainTable.eCode like :keyword or"
//		+ " building.buildingArea like :keyword or"
//		+ " building.escrowArea like :keyword or"
//		+ " project.theName like :keyword)</#if>";
//
////    	return "from Empj_BldEscrowCompleted_Dtl where 1=1"
////		+ " <#if theState??> and theState=:theState</#if>"
////		+ " <#if busiState??> and busiState=:busiState</#if>"
////		+ " <#if eCode??> and eCode=:eCode</#if>"
////		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
////		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
////		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
////		+ " <#if eCodeOfMainTable??> and eCodeOfMainTable=:eCodeOfMainTable</#if>"
////		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
////		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
////		+ " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
////		+ " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
////		+ " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
////		+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
////		+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
////		+ " <#if projectId??> and project.tableId=:projectId</#if>"
////		+ " <#if keyword??> and CONCAT(theNameOfProject , eCodeOfBuilding) like :keyword</#if>";
//    }

	public String getExcelListHQL()
    {
    	return "from Empj_BldEscrowCompleted_Dtl where 1=1"
    	+ " <#if idArr??> and tableId in :idArr</#if>";
    }

	public String getBuildingHQL()
    {
    	return "from Empj_BldEscrowCompleted_Dtl where 1=1"
    	+ " <#if theState??> and theState=:theState</#if>"
    	+ " <#if buildingId??> and building.tableId=:buildingId</#if>";
    }

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public  Criteria createCriteriaForList(Empj_BldEscrowCompleted_DtlForm model, Order order)
	{
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "developCompany")
				.createAlias("mainTable", "mainTable")
				.createAlias("building", "building")
				.createAlias("project", "project");

		if(order != null)
		{
			criteria.addOrder(order);
		}

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
		String eCodeOfMainTable = model.geteCodeOfMainTable();
		if (eCodeOfMainTable != null)
		{
			criteria.add(Restrictions.eq("eCodeOfMainTable", eCodeOfMainTable));
		}
		String eCodeOfDevelopCompany = model.geteCodeOfDevelopCompany();
		if (eCodeOfDevelopCompany != null)
		{
			criteria.add(Restrictions.eq("eCodeOfDevelopCompany", eCodeOfDevelopCompany));
		}
		String theNameOfProject = model.getTheNameOfProject();
		if (theNameOfProject != null)
		{
			criteria.add(Restrictions.eq("theNameOfProject", theNameOfProject));
		}
		String eCodeOfProject = model.geteCodeOfProject();
		if (eCodeOfProject != null)
		{
			criteria.add(Restrictions.eq("eCodeOfProject", eCodeOfProject));
		}
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		if (eCodeOfBuilding != null)
		{
			criteria.add(Restrictions.eq("eCodeOfBuilding", eCodeOfBuilding));
		}
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		if (eCodeFromPublicSecurity != null)
		{
			criteria.add(Restrictions.eq("eCodeFromPublicSecurity", eCodeFromPublicSecurity));
		}
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		if (eCodeFromConstruction != null)
		{
			criteria.add(Restrictions.eq("eCodeFromConstruction", eCodeFromConstruction));
		}

		String keyword = model.getKeyword();
		if (keyword != null && keyword.length() != 0)
		{
			//keyword = "%" + keyword + "%";
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.like("developCompany.theName", keyword))
					.add(Restrictions.like("project.theName", keyword))
					.add(Restrictions.like("mainTable.eCodeFromDRAD", keyword))
					.add(Restrictions.like("mainTable.eCode", keyword))
//					.add(Restrictions.like("building.buildingArea", keyword))
//					.add(Restrictions.like("building.escrowArea", keyword))
			);
		}

		return criteria;
	}

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 End =======//
	
	/**
	 * 根据主表id查询子表记录
	 * @param model
	 * @return
	 */
	public  Criteria listByCriteria(Empj_BldEscrowCompleted_DtlForm model)
	{
		Criteria criteria = createCriteria().createAlias("mainTable", "mainTable");

		Integer theState = model.getTheState();
		if (null != theState)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
		
		Long mainTableId = model.getMainTableId();
		if(null != mainTableId && mainTableId > 1){
			criteria.add(Restrictions.eq("mainTable.tableId", mainTableId));
		}


		return criteria;
	}
}

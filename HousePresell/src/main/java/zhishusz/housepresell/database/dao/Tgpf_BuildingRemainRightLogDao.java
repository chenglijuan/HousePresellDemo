package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import org.hibernate.Criteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_BuildingRemainRightLogDao extends BaseDao<Tgpf_BuildingRemainRightLog>
{
//	public String getSizeHQL()
//	{
//		String condition = getBasicHQL();
//		return "from Tgpf_BuildingRemainRightLog buildingRemainRightLog"
//		+ " inner join buildingRemainRightLog.developCompany developCompany"
//		+ " inner join buildingRemainRightLog.building building"
//		+ " inner join buildingRemainRightLog.project project where 1=1"
//		+ condition;
//	}
//	public String getListHQL()
//    {
//		String condition = getBasicHQL();
//		return "from Tgpf_BuildingRemainRightLog buildingRemainRightLog"
//		+ " inner join fetch buildingRemainRightLog.developCompany developCompany"
//		+ " inner join fetch buildingRemainRightLog.building building"
//		+ " inner join fetch buildingRemainRightLog.project project where 1=1"
//		+ condition;
//    }
//	public String getBasicHQL()
//    {
//    	return " <#if theState??> and buildingRemainRightLog.theState=:theState</#if>"
//		+ " <#if busiState??> and buildingRemainRightLog.busiState=:busiState</#if>"
//		+ " <#if eCode??> and buildingRemainRightLog.eCode=:eCode</#if>"
//		+ " <#if projectId??> and project.tableId=:projectId</#if>"
//		+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
//		+ " <#if billTimeStamp??> and buildingRemainRightLog.billTimeStamp=:billTimeStamp</#if>"
//		+ " <#if keyword??> and (buildingRemainRightLog.theNameOfProject like :keyword or"
//		+ " developCompany.theName like :keyword)</#if>"
//		+ " <#if buildingId??> and building.tableId=:buildingId</#if>"
//		+ " <#if srcBusiType??> and buildingRemainRightLog.srcBusiType=:srcBusiType</#if>";
//    }
	
	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public Criteria createCriteriaForList(Tgpf_BuildingRemainRightLogForm model)
	{
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "developCompany")
				.createAlias("building", "building")
				.createAlias("project", "project");
		
		Integer theState = model.getTheState();
		if(theState != null)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
		
		String busiState = model.getBusiState();
		if(busiState != null && busiState.length() > 0)
		{
			criteria.add(Restrictions.eq("busiState", busiState));
		}
		
		String eCode = model.geteCode();
		if(eCode != null && eCode.length() > 0)
		{
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		
		Long projectId = model.getProjectId();
		if(projectId != null)
		{
			criteria.add(Restrictions.eq("project.tableId", projectId));
		}
		
		Long developCompanyId = model.getDevelopCompanyId();
		if(developCompanyId != null)
		{
			criteria.add(Restrictions.eq("developCompany.tableId", developCompanyId));
		}
		
		String billTimeStamp = model.getBillTimeStamp();
		if(billTimeStamp != null && billTimeStamp.length() > 0)
		{
			criteria.add(Restrictions.eq("billTimeStamp", billTimeStamp));
		}
		
		Long buildingId = model.getBuildingId();
		if(buildingId != null)
		{
			criteria.add(Restrictions.eq("building.tableId", buildingId));
		}
		
		String srcBusiType = model.getSrcBusiType();
		if(srcBusiType != null && srcBusiType.length() > 0)
		{
			criteria.add(Restrictions.eq("srcBusiType", srcBusiType));
		}
		
		String keyword = model.getKeyword();
		if (keyword != null && keyword.length() != 0)
		{
			Junction junction = Restrictions.disjunction()
					.add(Restrictions.like("theNameOfProject", keyword))
					.add(Restrictions.like("developCompany.theName", keyword))
					.add(Restrictions.like("building.eCodeFromConstruction", keyword))
					.add(Restrictions.like("billTimeStamp", keyword));
			if (keyword.equals("匹配成功")) {
				junction.add(Restrictions.like("busiState", 1));
			} else if (keyword.equals("未比对")) {
				junction.add(Restrictions.like("busiState", 2));
			} else if (keyword.equals("不匹配")) {
				junction.add(Restrictions.like("busiState", 3));
			}
			criteria.add(junction);
		}

		criteria.addOrder(Order.desc("busiState"));
		criteria.addOrder(Order.desc("billTimeStamp"));
		criteria.addOrder(Order.desc("createTimeStamp"));
		criteria.addOrder(Order.asc("developCompany.theName"));
		criteria.addOrder(Order.asc("theNameOfProject"));
		criteria.addOrder(Order.asc("building.eCodeFromConstruction"));

		return criteria;
	}
	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 End =======//
}

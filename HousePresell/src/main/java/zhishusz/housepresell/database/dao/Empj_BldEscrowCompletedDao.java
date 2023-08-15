package zhishusz.housepresell.database.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;

import java.util.ArrayList;

/*
 * Dao数据库操作：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BldEscrowCompletedDao extends BaseDao<Empj_BldEscrowCompleted>
{
	public String getBasicHQL()
    {
    	return "from Empj_BldEscrowCompleted where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		
		//风控抽查条件
		+ " <#if approvalState??> and approvalState=:approvalState</#if>"
		+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
		+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
		
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
		+ " <#if eCodeFromDRAD??> and eCodeFromDRAD=:eCodeFromDRAD</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
//		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and project.tableId in :projectInfoIdArr</#if>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
			+ " <#list projectInfoIdArr as projectInfoId>"
				+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
				+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
			+ " </#list>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
		+ " </#if>"
		+ " <#if keyword??> and CONCAT(eCode , eCodeOfDevelopCompany, theNameOfProject, eCodeFromDRAD) like :keyword</#if>"
		+ " ORDER BY createTimeStamp DESC";
    }

	public String getExcelListHQL()
	{
		return "from Empj_BldEscrowCompleted where 1=1"
				+ " <#if idArr??> and tableId in :idArr</#if>";
	}

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public Criteria createCriteriaForList(Empj_BldEscrowCompletedForm model, Order order)
	{
		//Empj_BldEscrowCompleted_DtlDao

		Criteria criteria = createCriteria()
				.createAlias("developCompany", "developCompany")
				.createAlias("building", "building")
				.createAlias("project", "project");


		//物价备案均价 Tgpj_BuildingAvgPrice theState=0 && approvalState=审批中  ->buildingInfo
		//受限额度 Empj_BldLimitAmount_AF theState=0 && approvalState=审批中 && developCompany=? &&
		// project=?  ->building
		//退房退款 Tgpf_RefundInfo theState=0 && busiState=审批中 && project=? ->building
		//上面的三个查询结果进行并集处理，得出BuildingIdArr

		//楼幢信息 Empj_BuildingInfo theState=0 && busiState=已备案 && approvalState!=审批中 && developCompany=? &&
		// project=?  Empj_BuildingExtendInfo theState=0 && escrowState=已托管 ->可终止building

		return criteria;
	}

	//===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
	public Criteria createNewCriteriaForList(Empj_BldEscrowCompletedForm model)
	{
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "company")
				.createAlias("project", "project")
				.createAlias("userStart", "userStart");

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
		
		if(model.getHasPush() != null){
			criteria.add(Restrictions.eq("hasPush", model.getHasPush()));
		}
		
		String eCode = model.geteCode();
		if (eCode != null)
		{
			criteria.add(Restrictions.eq("eCode", eCode));
		}
		String eCodeFromDRAD = model.geteCodeFromDRAD();
		if (eCodeFromDRAD != null)
		{
			criteria.add(Restrictions.eq("eCodeFromDRAD", eCodeFromDRAD));
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
							.add(Restrictions.like("eCodeFromDRAD", keyword))
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


//		addCriteriaListOrder(criteria, model);
		//未指定↓↑箭头的用Oracle默认排序，托管终止未特别指定默认排序
		if (StringUtils.isEmpty(model.getOrderBy())) {
			criteria.addOrder(Order.desc("recordTimeStamp"));
		} else {
			ArrayList<String> pinYinList = new ArrayList<>();
			pinYinList.add("company.theName");
			pinYinList.add("project.theName");
			addCriteriaListOrder(criteria, model, pinYinList);
		}

		return criteria;
	}
}

package zhishusz.housepresell.database.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_TheState;

/*
 * Dao数据库操作：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BuildingInfoDao extends BaseDao<Empj_BuildingInfo>
{
	public String getBasicHQL()
    {
    	return "from Empj_BuildingInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState?? && busiState != \"\"> and busiState=:busiState</#if>"
		+ " <#if approvalState?? && approvalState != \"\"> and approvalState=:approvalState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
		+ " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
		+ " <#if theNameFromPresellSystem??> and theNameFromPresellSystem=:theNameFromPresellSystem</#if>"
		+ " <#if eCodeOfProjectFromPresellSystem??> and eCodeOfProjectFromPresellSystem=:eCodeOfProjectFromPresellSystem</#if>"
		+ " <#if eCodeFromPresellSystem??> and eCodeFromPresellSystem=:eCodeFromPresellSystem</#if>"
		+ " <#if theNameFromFinancialAccounting??> and theNameFromFinancialAccounting=:theNameFromFinancialAccounting</#if>"
		+ " <#if eCodeFromPresellCert??> and eCodeFromPresellCert=:eCodeFromPresellCert</#if>"
		+ " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
		+ " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
		+ " <#if bankAccountSupervisedList??> and bankAccountSupervisedList=:bankAccountSupervisedList</#if>"
		+ " <#if eCodeOfProjectPartition??> and eCodeOfProjectPartition=:eCodeOfProjectPartition</#if>"
		+ " <#if zoneCode??> and zoneCode=:zoneCode</#if>"
		+ " <#if theNameOfCityRegion??> and theNameOfCityRegion=:theNameOfCityRegion</#if>"
		+ " <#if theNameOfStreet??> and theNameOfStreet=:theNameOfStreet</#if>"
		+ " <#if eCodeOfGround??> and eCodeOfGround=:eCodeOfGround</#if>"
		+ " <#if eCodeOfLand??> and eCodeOfLand=:eCodeOfLand</#if>"
		+ " <#if position??> and position=:position</#if>"
		+ " <#if purpose??> and purpose=:purpose</#if>"
		+ " <#if structureProperty??> and structureProperty=:structureProperty</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if theProperty??> and theProperty=:theProperty</#if>"
		+ " <#if decorationType??> and decorationType=:decorationType</#if>"
		+ " <#if combType??> and combType=:combType</#if>"
		+ " <#if floorNumer??> and floorNumer=:floorNumer</#if>"
		+ " <#if upfloorNumber??> and upfloorNumber=:upfloorNumber</#if>"
		+ " <#if downfloorNumber??> and downfloorNumber=:downfloorNumber</#if>"
		+ " <#if heigh??> and heigh=:heigh</#if>"
		+ " <#if unitNumber??> and unitNumber=:unitNumber</#if>"
		+ " <#if sumFamilyNumber??> and sumFamilyNumber=:sumFamilyNumber</#if>"
		+ " <#if buildingArea??> and buildingArea=:buildingArea</#if>"
		+ " <#if occupyArea??> and occupyArea=:occupyArea</#if>"
		+ " <#if shareArea??> and shareArea=:shareArea</#if>"
		+ " <#if beginDate??> and beginDate=:beginDate</#if>"
		+ " <#if endDate??> and endDate=:endDate</#if>"
		+ " <#if deliveryDate??> and deliveryDate=:deliveryDate</#if>"
		+ " <#if deliveryType??> and deliveryType=:deliveryType</#if>"
		+ " <#if warrantyDate??> and warrantyDate=:warrantyDate</#if>"
		+ " <#if geoCoordinate??> and geoCoordinate=:geoCoordinate</#if>"
		+ " <#if eCodeOfGis??> and eCodeOfGis=:eCodeOfGis</#if>"
		+ " <#if eCodeOfMapping??> and eCodeOfMapping=:eCodeOfMapping</#if>"
		+ " <#if eCodeOfPicture??> and eCodeOfPicture=:eCodeOfPicture</#if>"
		+ " <#if buildingFacilities??> and buildingFacilities=:buildingFacilities</#if>"
		+ " <#if buildingArround??> and buildingArround=:buildingArround</#if>"
		+ " <#if introduction??> and introduction=:introduction</#if>"
		+ " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
		+ " <#if eCodeOfBusCompany??> and eCodeOfBusCompany=:eCodeOfBusCompany</#if>"
		+ " <#if eCodeOfOwnerCommittee??> and eCodeOfOwnerCommittee=:eCodeOfOwnerCommittee</#if>"
		+ " <#if eCodeOfMappingUnit??> and eCodeOfMappingUnit=:eCodeOfMappingUnit</#if>"
		+ " <#if eCodeOfS??> and eCodeOfS=:eCodeOfS</#if>"
		+ " <#if eCodeOfConsUnit??> and eCodeOfConsUnit=:eCodeOfConsUnit</#if>"
		+ " <#if eCodeOfControlUnit??> and eCodeOfControlUnit=:eCodeOfControlUnit</#if>"
		+ " <#if escrowArea??> and escrowArea=:escrowArea</#if>"
		+ " <#if escrowStandard??> and escrowStandard=:escrowStandard</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if developCompanyId??> and developCompany.tableId=:developCompanyId</#if>"
    	+ " <#if projectId??> and project.tableId =:projectId</#if>"
    	+ " <#if project??> and project =:project</#if>"
    	+ " <#if escrowState?? && escrowState != \"\"> and extendInfo.escrowState =:escrowState</#if>"
    	+ " <#if exceptEscrowState?? && exceptEscrowState != \"\"> and extendInfo.escrowState <>:exceptEscrowState</#if>"
    	+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
//		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (tableId in :buildingInfoIdIdArr or userStart.tableId=:userId)</#if>"

		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
			+ " <#list projectInfoIdArr as projectInfoId>"
				+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
				+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
			+ " </#list>"
		+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or userStart.tableId=:userId )</#if>"
		+ " </#if>"
		
		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
			+ " <#list buildingInfoIdIdArr as buildingInfoId>"
				+ " <#if buildingInfoId_index == 0> (tableId = ${buildingInfoId?c} )</#if>"
				+ " <#if buildingInfoId_index != 0> or (tableId = ${buildingInfoId?c} )</#if>"
				+ " </#list>"
		+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> or userStart.tableId=:userId )</#if>"
		+ " </#if>"
    	+ " <#if paymentId??> and payment.tableId =:paymentId</#if>"
    	+ " <#if keyword??> and ( eCodeFromConstruction like :keyword or cityRegion.theName like :keyword or project.theName like :keyword ) </#if>"
		+ " <#if startEscrow??> and (extendInfo.escrowState=:startEscrow or extendInfo.escrowState=:endEscrow)</#if>"
//		+ " <#if orderBy?? && orderBy != \"\"> order by ${orderBy}</#if>"
    	+ " <#if orderBy?? && orderBy != \"\" && orderBy != 'eCodeFromConstruction' && orderBy != 'eCode'> order by theNameOfProject , busiState ,NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
    	+ " <#if orderBy?? && orderBy != \"\" && orderBy == 'eCodeFromConstruction'> order by theNameOfProject , busiState ,to_number(regexp_substr(eCodeFromConstruction,'[0-9]*[0-9]',1))</#if>"
    	+ " <#if orderBy?? && orderBy != \"\" && orderBy == 'eCode'> order by to_number(regexp_substr(eCodeFromConstruction,'[0-9]*[0-9]',1)) ${orderByType}</#if>"
		+ " <#if gjjTableId??> and gjjTableId=:gjjTableId</#if>"
		+ " <#if approveMonth??> and approveMonth=:approveMonth</#if>";
    	//
    }
	
	/*
	 * xsz by time 2018-9-4 15:28:20
	 * 用于根据Id匹配主键或外来主键信息查询详情
	 */
	public String getDetailHql() 
	{
    	return "from Empj_BuildingInfo where 1=1 and theState=0 "
    			+ " <#if tableId??> and tableId = :tableId </#if>"
    			+ " <#if externalId??> and externalId = :externalId </#if>";
    }
	
	public String getSpecialHQL()
    {
    	return "from Empj_BuildingInfo where 1=1"
    	+ " <#if theState??> and theState=:theState</#if>"
    	+ " <#if project??> and project =:project</#if>"
    	+ " <#if busiState?? && busiState != \"\"> and busiState=:busiState</#if>"
//    	+ " <#if escrowState?? && escrowState != \"\"> and extendInfo.escrowState =:escrowState</#if>"
    	+ " <#if keyword??> and tableId not in :keyword</#if>";
    }
	
	public String getExcelListHQL()
    {
    	return "from Empj_BuildingInfo where 1=1"
    	+ " <#if idArr??> and tableId in :idArr</#if>";
    }

	public String getListByIdArrHQL()
	{
		String str="from Empj_BuildingInfo where 1=1"
				+ " and theState = "+S_TheState.Normal
				+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and cityRegion.tableId in :cityRegionInfoIdArr</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and project.tableId in :projectInfoIdArr</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
				+ " <#list buildingInfoIdIdArr as buildingInfoId>"
				+ " <#if buildingInfoId_index == 0> tableId = ${buildingInfoId?c}</#if>"
				+ " <#if buildingInfoId_index != 0> or tableId = ${buildingInfoId?c}</#if>"
				+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> )</#if>"
				+ " </#if>";
		
		return str; 
	}
	
	/**
	 * 获取某个项目下的楼幢按楼幢编号顺序排序
	 * @return
	 */
	public String getProjectHQL()
    {
    	return "from Empj_BuildingInfo where 1=1 and theState=0"
    			+ " <#if projectId??> and project.tableId =:projectId</#if>"
    			+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//    			+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
//    			+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (tableId in :buildingInfoIdIdArr or userStart.tableId=:userId)</#if>"

				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or userStart.tableId=:userId )</#if>"
				+ " </#if>"
				
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
					+ " <#list buildingInfoIdIdArr as buildingInfoId>"
						+ " <#if buildingInfoId_index == 0> (tableId = ${buildingInfoId?c} )</#if>"
						+ " <#if buildingInfoId_index != 0> or (tableId = ${buildingInfoId?c} )</#if>"
						+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> or userStart.tableId=:userId )</#if>"
				+ " </#if>"
    			
    			+ " order by to_number(regexp_substr(eCodeFromConstruction,'[0-9]*[0-9]',1))";
    }
	
	public String getUnitHQL()
    {
    	return "from Empj_BuildingInfo where 1=1"
    	    	+ " <#if theState??> and theState=:theState</#if>"
    	    	+ " <#if cityRegionInfoIdArr?? && (cityRegionInfoIdArr?size>0)> and (cityRegion.tableId in :cityRegionInfoIdArr or userStart.tableId=:userId)</#if>"
//    			+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (project.tableId in :projectInfoIdArr or userStart.tableId=:userId)</#if>"
//    			+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (tableId in :buildingInfoIdIdArr or userStart.tableId=:userId)</#if>"

				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> or userStart.tableId=:userId )</#if>"
				+ " </#if>"
				
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
					+ " <#list buildingInfoIdIdArr as buildingInfoId>"
						+ " <#if buildingInfoId_index == 0> (tableId = ${buildingInfoId?c} )</#if>"
						+ " <#if buildingInfoId_index != 0> or (tableId = ${buildingInfoId?c} )</#if>"
						+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> or userStart.tableId=:userId )</#if>"
				+ " </#if>"
    	    	
    	    	+ " <#if keyword??> and (theNameOfProject like :keyword or eCodeFromConstruction like :keyword or position like :keyword)</#if>";
    }

	public String getBaseHql()
	{
		return "from Empj_BuildingInfo where 1=1 and theState=0 "
				+ " <#if tableId??> and tableId = :tableId </#if>"
				+ " <#if externalId??> and externalId = :externalId </#if>"
				+ " <#if eCodeFromPresellCert??> and eCodeFromPresellCert=:eCodeFromPresellCert</#if>";
	}


	/**
	 * 用于托管终止--筛选可用楼幢
	 * @param model
	 * @return
	 */
	public Criteria createCriteriaForBuildingInfoList(Empj_BldEscrowCompletedForm model)
	{
		//楼幢信息 Empj_BuildingInfo theState=0 && busiState=已备案 && approvalState!=审批中 && developCompany=? &&
		// project=?  Empj_BuildingExtendInfo theState=0 && escrowState=已托管 ->可终止building
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "developCompany")
				.createAlias("project", "project")
				.createAlias("extendInfo", "extendInfo");

		criteria.add(Restrictions.eq("theState", S_TheState.Normal)); //未删除
		criteria.add(Restrictions.eq("busiState", S_BusiState.HaveRecord)); //已备案
		criteria.add(Restrictions.ne("approvalState", S_ApprovalState.Examining)); //非审批中

		Long developCompanyId = model.getDevelopCompanyId();
		if (developCompanyId != null && developCompanyId > 0)
		{
			criteria.add(Restrictions.eq("developCompany.tableId", developCompanyId));
		}
		Long projectId = model.getProjectId();
		if (projectId != null && projectId > 0)
		{
			criteria.add(Restrictions.eq("project.tableId", projectId));
		}

//		criteria.add(Restrictions.eq("extendInfo.escrowState", S_EscrowState.HasEscrowState));
		//in buildingIdArr
		if (model.getIntersectionBuildingIdArr() != null)
		{
			criteria.add(Restrictions.not(Restrictions.in("tableId", model.getIntersectionBuildingIdArr())));
		}

		return criteria;
	}
	
	/*
	 * xsz by time 2019-3-30 14:07:52
	 * 用于解决加载支付保证选择项目后加载楼幢列表偶发性出现加载所有的楼幢问题
	 * #1408 支付保证 选择项目后，偶发性会加载所有楼幢
	 */
	public Criteria getCriteriaBasicHQL(Empj_BuildingInfoForm model)
	{
		//楼幢信息 Empj_BuildingInfo theState=0 && busiState=已备案 && approvalState!=审批中 && developCompany=? &&
		// project=?  Empj_BuildingExtendInfo theState=0 && escrowState=已托管 ->可终止building
		Criteria criteria = createCriteria()
				.createAlias("developCompany", "developCompany")
				.createAlias("project", "project")
				.createAlias("extendInfo", "extendInfo");

		criteria.add(Restrictions.eq("theState", S_TheState.Normal)); //未删除
		criteria.add(Restrictions.eq("busiState", S_BusiState.HaveRecord)); //已备案
		criteria.add(Restrictions.eq("extendInfo.escrowState", S_EscrowState.HasEscrowState)); //已托管
		criteria.add(Restrictions.ne("approvalState", S_ApprovalState.Examining)); //非审批中

		Long developCompanyId = model.getDevelopCompanyId();
		if (developCompanyId != null && developCompanyId > 0)
		{
			criteria.add(Restrictions.eq("developCompany.tableId", developCompanyId));
		}
		Long projectId = model.getProjectId();
		if (projectId != null && projectId > 0)
		{
			criteria.add(Restrictions.eq("project.tableId", projectId));
		}
		Empj_ProjectInfo project = model.getProject();
		if(null != project)
		{
			criteria.add(Restrictions.eq("project", project));
		}

		Long[] projectInfoIdArr = model.getProjectInfoIdArr();
		if(null != projectInfoIdArr && projectInfoIdArr.length>0)
		{
			criteria.add(Restrictions.eq("project.tableId", projectInfoIdArr));
		}
		Long[] buildingInfoIdIdArr = model.getBuildingInfoIdIdArr();
		if(null != buildingInfoIdIdArr && buildingInfoIdIdArr.length>0)
		{
			criteria.add(Restrictions.eq("tableId", buildingInfoIdIdArr));
		}

		criteria.addOrder(Order.asc("eCodeFromConstruction"));
		
		return criteria;
	}
}

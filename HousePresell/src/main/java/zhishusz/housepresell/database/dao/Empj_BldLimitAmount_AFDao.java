package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/*
 * Dao数据库操作：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BldLimitAmount_AFDao extends BaseDao<Empj_BldLimitAmount_AF> {
    public String getBasicHQL() {
        return "from Empj_BldLimitAmount_AF where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if approvalState??> and approvalState=:approvalState</#if>"
                + " <#if tableId??> and tableId=:tableId</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
                + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
                + " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if eCodeOfDevelopCompany??> and eCodeOfDevelopCompany=:eCodeOfDevelopCompany</#if>"
                + " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
                + " <#if eCodeOfProject??> and eCodeOfProject=:eCodeOfProject</#if>"
                + " <#if eCodeOfBuilding??> and eCodeOfBuilding=:eCodeOfBuilding</#if>"
                + " <#if upfloorNumber??> and upfloorNumber=:upfloorNumber</#if>"
                + " <#if eCodeFromConstruction??> and eCodeFromConstruction=:eCodeFromConstruction</#if>"
                + " <#if eCodeFromPublicSecurity??> and eCodeFromPublicSecurity=:eCodeFromPublicSecurity</#if>"
                + " <#if recordAveragePriceOfBuilding??> and recordAveragePriceOfBuilding=:recordAveragePriceOfBuilding</#if>"
                + " <#if escrowStandard??> and escrowStandard=:escrowStandard</#if>"
                + " <#if deliveryType??> and deliveryType=:deliveryType</#if>"
                + " <#if orgLimitedAmount??> and orgLimitedAmount=:orgLimitedAmount</#if>"
                + " <#if currentFigureProgress??> and currentFigureProgress=:currentFigureProgress</#if>"
                + " <#if currentLimitedRatio??> and currentLimitedRatio=:currentLimitedRatio</#if>"
                + " <#if nodeLimitedAmount??> and nodeLimitedAmount=:nodeLimitedAmount</#if>"
                + " <#if totalGuaranteeAmount??> and totalGuaranteeAmount=:totalGuaranteeAmount</#if>"
                + " <#if cashLimitedAmount??> and cashLimitedAmount=:cashLimitedAmount</#if>"
                + " <#if effectiveLimitedAmount??> and effectiveLimitedAmount=:effectiveLimitedAmount</#if>"
                + " <#if expectFigureProgress??> and expectFigureProgress=:expectFigureProgress</#if>"
                + " <#if expectLimitedRatio??> and expectLimitedRatio=:expectLimitedRatio</#if>"
                + " <#if expectLimitedAmount??> and expectLimitedAmount=:expectLimitedAmount</#if>"
//                + " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and project.tableId in :projectInfoIdArr</#if>"
//                + " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and building.tableId in :buildingInfoIdIdArr</#if>"
                
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>"
				
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> and (</#if>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)>"
					+ " <#list buildingInfoIdIdArr as buildingInfoId>"
						+ " <#if buildingInfoId_index == 0> (building.tableId = ${buildingInfoId?c} )</#if>"
						+ " <#if buildingInfoId_index != 0> or (building.tableId = ${buildingInfoId?c} )</#if>"
						+ " </#list>"
				+ " <#if buildingInfoIdIdArr?? && (buildingInfoIdIdArr?size>0)> )</#if>"
				+ " </#if>"
                
                + " <#if keyword??> and eCode like :keyword</#if>"
                + " <#if expectEffectLimitedAmount??> and expectEffectLimitedAmount=:expectEffectLimitedAmount</#if>";
    }

    public String getExcelListHQL() {
        return "from Empj_BldLimitAmount_AF where 1=1"
                + " <#if idArr??> and tableId in :idArr</#if>";
    }

    //===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
    public Criteria createCriteriaForList(Empj_BldLimitAmount_AFForm model) {
        //企业名称、项目名称
        String keyword = getKeyWord(model);
        Criteria criteria = createCriteria();
        criteria.createAlias("developCompany", "company");
        criteria.createAlias("project", "project");
        criteria.createAlias("building", "building");

        Integer theState = S_TheState.Normal;
        criteria.add(Restrictions.eq("theState", theState));
        criteria.add(Restrictions.or(
                Restrictions.like("eCode", keyword),
                Restrictions.like("building.eCodeFromConstruction", keyword)
//				Restrictions.like("project.theName", keyword)
        ));
        if (StringUtils.isNotEmpty(model.getApprovalState())) {
            criteria.add(Restrictions.eq("approvalState", model.getApprovalState()));
        }
        Long developCompanyId = model.getDevelopCompanyId();
        if (developCompanyId != null && developCompanyId > 0) {
            criteria.add(Restrictions.eq("developCompany.tableId", developCompanyId));
        }
        // 区域授权
        if (model.getCityRegionInfoIdArr() != null && model.getCityRegionInfoIdArr().length > 0)
		{
			criteria.add(Restrictions.in("building.cityRegion.tableId", model.getCityRegionInfoIdArr()));
		}
//        Long buildingId = model.getBuildingId();
//        if (buildingId != null && buildingId > 0) {
//            criteria.add(Restrictions.eq("building.tableId", buildingId));
//        }
//        addProjectRangeAuthorization(criteria, model);
//        addBuildingRangeAuthorization(criteria, model);
//        addCityRangeAuthorization(criteria, model);
//        if (model.getProjectInfoIdArr() != null && model.getProjectInfoIdArr().length > 0) {
//            criteria.add(Restrictions.in("project.tableId", model.getProjectInfoIdArr()));
//        }
//        if (model.getBuildingInfoIdIdArr() != null && model.getBuildingInfoIdIdArr().length > 0) {
//            criteria.add(Restrictions.in("building.tableId", model.getBuildingInfoIdIdArr()));
//        }
        addCompanyLimitRange(criteria, model);

        //UI中没有施工编号
//        if (model.getDevelopCompanyId() != null) {
//            criteria.add(Restrictions.eq("company.tableId", model.getDevelopCompanyId()));
//        }
        if (model.getProjectId() != null) {
            criteria.add(Restrictions.eq("project.tableId", model.getProjectId()));
        }
//        if (StringUtils.isEmpty(model.getOrderBy())) {
//            criteria.addOrder(Order.desc())
//        } else {
        ArrayList<String> pinYinOrderList = new ArrayList<>();
        pinYinOrderList.add("project.theName");
        pinYinOrderList.add("company.theName");
        addCriteriaListOrder(criteria, model,pinYinOrderList);
//        }
        /*
         * xsz by time 2019-2-14 13:44:02
         * 根据操作时间降序排序
         */
        criteria.addOrder(Order.desc("recordTimeStamp"));
        
        return criteria;
    }

    @SuppressWarnings("rawtypes")
    public boolean isUniqueBuilding(Empj_BldLimitAmount_AFForm model) {
        Long buildingInfoId = model.getBuildingId();
        Criteria criteria = createCriteria();
//		criteria.createAlias("building", "building");
        criteria.add(Restrictions.eq("building.tableId", buildingInfoId));
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        List byPage = findByPage(criteria);
        if (byPage.size() > 1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isUniqueLimitAmount(Empj_BldLimitAmount_AFForm model,boolean addOrUpdate) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        criteria.add(Restrictions.eq("building.tableId", model.getBuildingId()));
        criteria.add(Restrictions.or(Restrictions.eq("approvalState", S_ApprovalState.Examining),
                Restrictions.eq("approvalState", S_ApprovalState.WaitSubmit)));
        List byPage = findByPage(criteria);
        int maxNum=-1;
        if(addOrUpdate){
            maxNum=0;
        }else{
            maxNum=1;
        }
        if (byPage.size() > maxNum) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 用于托管终止--筛选不可用楼幢
     *
     * @param model
     * @return
     */
    public Criteria createCriteriaForBldLimitAmountList(Empj_BldEscrowCompletedForm model) {
        //受限额度 Empj_BldLimitAmount_AF theState=0 && approvalState=审批中 && developCompany=? &&
        // project=?  ->building
        Criteria criteria = createCriteria()
                .createAlias("developCompany", "developCompany")
                .createAlias("project", "project")
                .setProjection(Projections.groupProperty("building"));

        criteria.add(Restrictions.eq("theState", S_TheState.Normal)); //未删除
        criteria.add(Restrictions.eq("approvalState", S_ApprovalState.Examining)); //审批中

        Long developCompanyId = model.getDevelopCompanyId();
        if (developCompanyId != null && developCompanyId > 0) {
            criteria.add(Restrictions.eq("developCompany.tableId", developCompanyId));
        }
        Long projectId = model.getProjectId();
        if (projectId != null && projectId > 0) {
            criteria.add(Restrictions.eq("project.tableId", projectId));
        }

        return criteria;
    }
    
    /**
     * 
     * @return
     */
    public String getListHQL() {
        return "from Empj_BldLimitAmount_AF where 1=1"
        		+ " <#if theState??> and theState=:theState</#if>"
        		+ " <#if building??> and building=:building</#if>"
                + " <#if buildingId??> and building.tableId =:BuildingId</#if>"
        		+ " order by createTimeStamp desc";
    }
    
    public String getCheckHQL() {
        return "from Empj_BldLimitAmount_AF where 1=1"
        		+ " <#if theState??> and theState=:theState</#if>"
                + " <#if buildingId??> and building.tableId =:BuildingId</#if>"
        		+ " and (approvalState is null or approvalState <> '已完结') ";
    }
}

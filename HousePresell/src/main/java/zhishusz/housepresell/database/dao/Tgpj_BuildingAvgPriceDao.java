package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
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
 * Dao数据库操作：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_BuildingAvgPriceDao extends BaseDao<Tgpj_BuildingAvgPrice> {
    public String getBasicHQL() {
        return "from Tgpj_BuildingAvgPrice where 1=1"
                + " <#if theState??> and theState=0</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
                + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if buildingInfo??> and buildingInfo=:buildingInfo</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if recordAveragePrice??> and recordAveragePrice=:recordAveragePrice</#if>"
                + " <#if averagePriceRecordDate??> and averagePriceRecordDate=:averagePriceRecordDate</#if>"
                + " <#if keyword??> and eCode like :keyword </#if>"
                + " <#if recordAveragePriceFromPresellSystem??> and recordAveragePriceFromPresellSystem=:recordAveragePriceFromPresellSystem</#if>";
//    	return "from Tgpj_BuildingAvgPrice where 1=1"
//		+ " <#if theState??> and theState=:theState</#if>"
//		+ " <#if busiState??> and busiState=:busiState</#if>"
//		+ " <#if eCode??> and eCode=:eCode</#if>"
//		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
//		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
//		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
//		+ " <#if recordAveragePrice??> and recordAveragePrice=:recordAveragePrice</#if>"
//		+ " <#if averagePriceRecordDate??> and averagePriceRecordDate=:averagePriceRecordDate</#if>"
//		+ " <#if recordAveragePriceFromPresellSystem??> and recordAveragePriceFromPresellSystem=:recordAveragePriceFromPresellSystem</#if>";
    }

    public String getBuildingHQL() {
        return "from Tgpj_BuildingAvgPrice where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if buildingInfoId??> and buildingInfo.tableId=:buildingInfoId</#if>";
    }

    public String getExcelListHQL() {
        return "from Tgpj_BuildingAvgPrice where 1=1"
                + " <#if idArr??> and tableId in :idArr</#if>";
    }

    //===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
    public Criteria createCriteriaForList(Tgpj_BuildingAvgPriceForm model) {
        //	开发企业、项目名称、 施工编号
        String keyword = getKeyWord(model);
        Criteria criteria = createCriteria();
//		Sm_User userStart = model.getUserStart();
//		if (userStart != null)
//		{
        criteria.createAlias("userStart", "user");
        criteria.createAlias("userStart.company", "company");
//		}
        criteria.createAlias("buildingInfo", "building");
        criteria.createAlias("buildingInfo.project", "project");

        Integer theState = S_TheState.Normal;
        criteria.add(Restrictions.eq("theState", theState));
        if (StringUtils.isNotEmpty(model.getApprovalState())) {
            criteria.add(Restrictions.eq("approvalState", model.getApprovalState()));
        }
        if (model.getCompanyId() != null) {
            criteria.add(Restrictions.eq("company.tableId", model.getCompanyId()));
        }
        if (model.getProjectId() != null) {
            criteria.add(Restrictions.eq("project.tableId", model.getProjectId()));
        }
        if (StringUtils.isEmpty(model.getOrderBy())) {
            criteria.addOrder(Order.desc("building.eCodeFromConstruction"));
        } else {
            ArrayList<String> pinYinList = new ArrayList<>();
            pinYinList.add("project.theName");
            pinYinList.add("company.theName");
            addCriteriaListOrder(criteria, model,pinYinList);
        }

        // 区域授权
        if (model.getCityRegionInfoIdArr() != null && model.getCityRegionInfoIdArr().length > 0)
		{
			criteria.add(Restrictions.in("building.cityRegion.tableId", model.getCityRegionInfoIdArr()));
		}
        
        addCompanyLimitRange(criteria, model);
        criteria.add(Restrictions.or(Restrictions.like("building.eCodeFromConstruction", keyword)));
        

        
//        CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
//        SessionImplementor session = (SessionImplementor) getCurrentSession();
//        SessionFactoryImplementor factory = session.getFactory();
//        CriteriaQueryTranslator translator = new CriteriaQueryTranslator(factory, criteriaImpl, criteriaImpl
//                .getEntityOrClassName(), CriteriaQueryTranslator.ROOT_SQL_ALIAS);
//        String[] implementors = factory.getImplementors(criteriaImpl.getEntityOrClassName());
//        CriteriaJoinWalker walker = new CriteriaJoinWalker((OuterJoinLoadable) factory
//                .getEntityPersister(implementors[0]), translator, factory, criteriaImpl, criteriaImpl
//                .getEntityOrClassName(), LoadQueryInfluencers.NONE);
//        String sqlString = walker.getSQLString();
//        System.out.println("sql string is " + sqlString);


        //        criteria.add(Restrictions.or(
//                Restrictions.like("company.theName", keyword),
//                Restrictions.like("project.theName", keyword),
//                Restrictions.like("building.eCodeFromConstruction", keyword)
//        ));
        return criteria;
    }

    public boolean isUniqueBuilding(Tgpj_BuildingAvgPriceForm model) {
        Long buildingInfoId = model.getBuildingInfoId();
        Criteria criteria = createCriteria();
        criteria.createAlias("buildingInfo", "building");
        criteria.add(Restrictions.eq("building.tableId", buildingInfoId));
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        List byPage = findByPage(criteria);
        if (byPage.size() > 0) {
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
    public Criteria createCriteriaForBuildingAvgPriceList(Empj_BldEscrowCompletedForm model) {
        //物价备案均价 Tgpj_BuildingAvgPrice theState=0 && approvalState=审批中  ->buildingInfo
        Criteria criteria = createCriteria()
                .setProjection(Projections.groupProperty("buildingInfo"));

        criteria.add(Restrictions.eq("theState", S_TheState.Normal)); //未删除
        criteria.add(Restrictions.eq("approvalState", S_ApprovalState.Examining)); //审批中

        return criteria;
    }

}

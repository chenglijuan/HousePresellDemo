package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Dao数据库操作：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpj_BldLimitAmountVer_AFDao extends BaseDao<Tgpj_BldLimitAmountVer_AF> {
    public String getBasicHQL() {
        return "from Tgpj_BldLimitAmountVer_AF where 1=1"
                + " <#if theState??> and theState=:theState</#if>"
                + " <#if busiState??> and busiState=:busiState</#if>"
		        + " <#if eCode??> and eCode=:eCode</#if>"
                + " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
                + " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
                + " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
                + " <#if theName??> and theName=:theName</#if>"
                + " <#if theVerion??> and theVerion=:theVerion</#if>"
                + " <#if theType??> and theType=:theType</#if>"
                + " <#if limitedAmountInfoJSON??> and limitedAmountInfoJSON=:limitedAmountInfoJSON</#if>"
                + " <#if beginExpirationDate??> and beginExpirationDate=:beginExpirationDate</#if>"
                + " <#if approvalState??> and approvalState=:approvalState</#if>"
                + " <#if keyword??> and eCode like :keyword</#if>"
                + " <#if endExpirationDate??> and endExpirationDate=:endExpirationDate</#if>"
                + " <#if orderBy??> order by ${orderBy}</#if>";
    }

    public String getExcelListHQL() {
        return "from Tgpj_BldLimitAmountVer_AF where 1=1"
                + " <#if idArr??> and tableId in :idArr</#if>";
    }

    //===================== 用 Hibernate 对象查询语句 解决：HQL多表关联查询，使用 fetch 抓取，有时候返回List<Object>有时候返回List<T>导致Rebuild重构数据异常 Start =====//
    public Criteria createCriteriaForList(Tgpj_BldLimitAmountVer_AFForm model) {
        //开发企业和开户行
        String keyword = getKeyWord(model);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        if (model.getIsUsing() != null) {
            criteria.add(Restrictions.and(Restrictions.eq("isUsing", model.getIsUsing())));
        }
        if(model.getApprovalState()!=null && model.getApprovalState().length()>0){
            criteria.add(Restrictions.and(Restrictions.eq("approvalState", model.getApprovalState())));
        }
        criteria.add(Restrictions.or(
                Restrictions.like("theName", keyword),
                Restrictions.like("eCode", keyword)
        ));
        if(StringUtils.isEmpty(model.getOrderBy())){
            criteria.addOrder(Order.asc("eCode"));
            criteria.addOrder(Order.desc("beginExpirationDate"));
        }else{
            addCriteriaListOrder(criteria,model);
        }

        return criteria;
    }

    public boolean isRepeatEcode(Tgpj_BldLimitAmountVer_AFForm model) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("eCode", model.geteCode()));
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        excludeSelf(model,criteria);
        Integer byPage_size = findByPage_Size(criteria);
        System.out.println("isRepeatEcode size is " + byPage_size);
        if (byPage_size > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInRange(Tgpj_BldLimitAmountVer_AFForm model) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        criteria.add(Restrictions.eq("theType", model.getTheType()));
        criteria.add(Restrictions.eq("isUsing", 0));
        Long begin = model.getBeginExpirationDate();
//		Long end = model.getEndExpirationDate();
        boolean canAdd = true;
        List<Tgpj_BldLimitAmountVer_AF> allList = findByPage(criteria);
        System.out.println("allList size is " + allList.size());
        for (Tgpj_BldLimitAmountVer_AF limitObjInTable : allList) {
//			if(limitObjInTable.getBeginExpirationDate()==null || limitObjInTable.getEndExpirationDate()==null){
            if (limitObjInTable.getBeginExpirationDate() == null) {
                continue;
            }
            if (model.getTableId() != null) {
                if (limitObjInTable.getTableId().equals(model.getTableId())) {
                    continue;
                }
            }
            System.out.println("limitObjInTable is " + limitObjInTable.toString());
            if (begin <= limitObjInTable.getBeginExpirationDate()) {
                canAdd = false;
            } else if (limitObjInTable.getEndExpirationDate() != null) {
                if (begin < limitObjInTable.getEndExpirationDate()) {
                    canAdd = false;
                }
            }

//			if(end<=limitObjInTable.getBeginExpirationDate() || begin>=limitObjInTable.getEndExpirationDate()){
//
//			}else{
//				canAdd=false;
//			}
        }
        return !canAdd;
    }

    public Tgpj_BldLimitAmountVer_AF getNowLimitAmountVer(String theType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        criteria.add(Restrictions.eq("isUsing", 0));
        criteria.add(Restrictions.eq("theType", theType));
        criteria.add(Restrictions.eq("busiState", S_BusiState.HaveRecord));
        long nowTimeStamp = System.currentTimeMillis();
        criteria.add(Restrictions.le("beginExpirationDate", nowTimeStamp));
        criteria.addOrder(Order.desc("beginExpirationDate"));
//		criteria.add(Restrictions.ge("endExpirationDate", nowTimeStamp));
        List<Tgpj_BldLimitAmountVer_AF> tgpj_bldLimitAmountVer_afList = findByPage(criteria);
        if (tgpj_bldLimitAmountVer_afList.size() > 0) {
            return tgpj_bldLimitAmountVer_afList.get(0);
        } else {
            return null;
        }
    }

    public boolean isUniqueWaitSubmitLimitAmount(Tgpj_BldLimitAmountVer_AFForm model) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("theState", S_TheState.Normal));
        criteria.add(Restrictions.eq("theType", model.getTheType()));
        criteria.add(Restrictions.eq("isUsing", 0));
        criteria.add(Restrictions.eq("approvalState", S_ApprovalState.WaitSubmit));
        excludeSelf(model, criteria);
        List byPage = findByPage(criteria);
        if (byPage.size() >= 1) {
            return false;
        } else {
            return true;
        }
    }

    private void excludeSelf(Tgpj_BldLimitAmountVer_AFForm model, Criteria criteria) {
        if (model.getTableId() != null) {
            criteria.add(Restrictions.ne("tableId", model.getTableId()));
        }
    }
}

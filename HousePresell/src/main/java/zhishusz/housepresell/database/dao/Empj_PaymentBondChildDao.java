package zhishusz.housepresell.database.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;

/*
 * Dao数据库操作：支付保函子表
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_PaymentBondChildDao extends BaseDao<Empj_PaymentBondChild>
{
	public String getBasicHQL()
    {
    	return "from Empj_PaymentBondChild where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if empj_PaymentBond??> and empj_PaymentBond=:empj_PaymentBond</#if>"
		+ " <#if empj_BuildingInfo??> and empj_BuildingInfo=:empj_BuildingInfo</#if>"
		+ " order by createTimeStamp desc";
    }
	
	public String getBasicHQLNoRecord()
    {
        return "from Empj_PaymentBondChild where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if empj_PaymentBond??> and empj_PaymentBond=:empj_PaymentBond</#if>"
        + " <#if empj_BuildingInfo??> and empj_BuildingInfo=:empj_BuildingInfo</#if>"
        + " <#if buildingId??> and empj_BuildingInfo.tableId=:buildingId</#if>"
        + " and empj_PaymentBond.approvalState <> '已完结' "
        + " order by createTimeStamp desc";
    }
	
	public String getBasicHQLRecord()
    {
        return "from Empj_PaymentBondChild where 1=1"
        + " <#if theState??> and theState=:theState</#if>"
        + " <#if empj_PaymentBond??> and empj_PaymentBond=:empj_PaymentBond</#if>"
        + " <#if empj_BuildingInfo??> and empj_BuildingInfo=:empj_BuildingInfo</#if>"
        + " and empj_PaymentBond.approvalState <> '已完结' "
        + " order by createTimeStamp desc";
    }
	
	public String getBuildListSortHQL()
    {
        return "from Empj_PaymentBondChild where 1=1 "
        + " <#if theState??> and theState=:theState</#if> "
        + " <#if empj_PaymentBond??> and empj_PaymentBond=:empj_PaymentBond</#if> "
        + " <#if empj_BuildingInfo??> and empj_BuildingInfo=:empj_BuildingInfo</#if> "
        + " order by to_number(regexp_substr(empj_BuildingInfo.eCodeFromConstruction,'[0-9]*[0-9]',1)) asc";
    }
	
	public Criteria getBasicHQLNoRecordCriteria(Empj_PaymentBondChildForm model)
	{
	    Criteria criteria = createCriteria()
	        .createAlias("empj_PaymentBond", "empj_PaymentBond")
            .createAlias("empj_BuildingInfo", "empj_BuildingInfo");
	    criteria.add(Restrictions.eq("theState", S_TheState.Normal)); 
        criteria.add(Restrictions.ne("empj_PaymentBond.approvalState", "已完结"));
        
        if(null != model.getBuildingId()){
            criteria.add(Restrictions.eq("empj_BuildingInfo.tableId", model.getBuildingId()));
        }
	    
	    
	    return criteria;
	}
}

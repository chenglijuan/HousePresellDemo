package zhishusz.housepresell.database.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_TheState;

/*
 * Dao数据库操作：角色与用户对应关系
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_RoleUserDao extends BaseDao<Sm_Permission_RoleUser>
{
	public String getBasicHQL()
    {
    	return "from Sm_Permission_RoleUser where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if sm_User??> and sm_User=:sm_User</#if>"
		+ " <#if sm_Permission_RoleId??> and sm_Permission_Role.tableId=:sm_Permission_RoleId</#if>"
		+ " <#if sm_Permission_Role??> and sm_Permission_Role=:sm_Permission_Role</#if>"
		+ " <#if emmp_companyInfo??> and emmp_companyInfo=:emmp_companyInfo</#if>"
		+ " <#if userStartId??> and sm_User.tableId=:userStartId</#if>"
    	+ " <#if sm_UserId??> and sm_User.tableId=:sm_UserId</#if>";
    }
	
	public String getBasicHQLByuser()
    {
    	return "from Sm_Permission_RoleUser where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if user??> and sm_User=:user</#if>";
    }
	
	public Criteria createCriteriaForListService(Sm_Permission_RoleUserForm model)
	{
		Criteria criteria = createCriteria()
				.add(Restrictions.eq("theState", S_TheState.Normal))
				.createAlias("sm_Permission_Role", "sm_Permission_Role");
		
		Integer theState = model.getTheState();
		if(theState != null)
		{
			criteria.add(Restrictions.eq("theState", theState));
		}
		
		Sm_User loginUser = model.getSm_User();
		if(loginUser != null)
		{
			criteria.add(Restrictions.eq("sm_User", loginUser));
		}
		
		Long leEnableTimeStamp = model.getLeEnableTimeStamp();
		if(leEnableTimeStamp != null)
		{
			criteria.add(Restrictions.le("sm_Permission_Role.enableTimeStamp", leEnableTimeStamp));
		}

		Long gtDownTimeStamp = model.getGtDownTimeStamp();
		if(gtDownTimeStamp != null)
		{
			criteria.add(Restrictions.gt("sm_Permission_Role.downTimeStamp", gtDownTimeStamp));
		}
		
		return criteria;
	}
	
	public Criteria findUserByRole(Sm_Permission_RoleUserForm model)
	{
		Criteria criteria = createCriteria()
				.setProjection(Projections.groupProperty("sm_User"))
				.setProjection(Projections.distinct(Projections.property("sm_User")));
		
		criteria.add(Restrictions.eq("theState", S_TheState.Normal));
		
		Sm_Permission_Role sm_Permission_Role = model.getSm_Permission_Role();
		if(sm_Permission_Role != null)
		{
			criteria.add(Restrictions.eq("sm_Permission_Role", sm_Permission_Role));
		}
		
		return criteria;
	}
	
	
	public Criteria getBasicHQLByCriteria(Sm_Permission_RoleUserForm model)
    {
		Criteria criteria = createCriteria()
				.add(Restrictions.eq("theState", S_TheState.Normal));
		
		criteria.add(Restrictions.eq("sm_User", model.getSm_User()));
		
		return criteria;
    	/*return "from Sm_Permission_RoleUser where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if sm_User??> and sm_User=:sm_User</#if>"
		+ " <#if sm_Permission_RoleId??> and sm_Permission_Role.tableId=:sm_Permission_RoleId</#if>"
		+ " <#if sm_Permission_Role??> and sm_Permission_Role=:sm_Permission_Role</#if>"
		+ " <#if emmp_companyInfo??> and emmp_companyInfo=:emmp_companyInfo</#if>"
		+ " <#if userStartId??> and sm_User.tableId=:userStartId</#if>"
    	+ " <#if sm_UserId??> and sm_User.tableId=:sm_UserId</#if>";*/
    }
}

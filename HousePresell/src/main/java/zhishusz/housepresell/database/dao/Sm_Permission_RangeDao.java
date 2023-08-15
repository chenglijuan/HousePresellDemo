package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_TheState;

/*
 * Dao数据库操作：角色授权数据
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_RangeDao extends BaseDao<Sm_Permission_Range>
{
	public String getBasicHQL()
    {
    	return "from Sm_Permission_Range where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if theType??> and theType=:theType</#if>"
		+ " <#if user??> and userInfo=:user</#if>"
		+ " <#if leAuthStartTimeStamp??> and authStartTimeStamp<=:leAuthStartTimeStamp</#if>"
		+ " <#if gtAuthEndTimeStamp??> and authEndTimeStamp>:gtAuthEndTimeStamp</#if>"
		+ " <#if companyInfo??> and companyInfo=:companyInfo</#if>";
    }
	
	public void deleteBatch(Sm_User userInfo, Emmp_CompanyInfo companyInfo)
	{
		if(userInfo == null && companyInfo == null) return;
		
		String sqlStr = " update Sm_Permission_Range set theState="+S_TheState.Deleted+" where 1=1";
		if(userInfo != null)
		{
			sqlStr += " and userInfo = " + userInfo.getTableId();
		}
		if(companyInfo != null)
		{
			sqlStr += " and companyInfo = " + companyInfo.getTableId();
		}
		updateBySql(sqlStr);
	}
}

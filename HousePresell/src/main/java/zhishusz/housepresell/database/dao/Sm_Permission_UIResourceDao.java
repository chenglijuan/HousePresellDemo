package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;

/*
 * Dao数据库操作：UI权限资源
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_Permission_UIResourceDao extends BaseDao<Sm_Permission_UIResource>
{
	public String getBasicHQL()
    {
    	return "from Sm_Permission_UIResource where 1=1"
		+ " <#if exceptTableId??> and tableId !=:exceptTableId</#if>"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if busiState??> and busiState=:busiState</#if>"
		+ " <#if busiCode??> and busiCode=:busiCode</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>"
		+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
		+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
		+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
		+ " <#if theName??> and theName=:theName</#if>"
		+ " <#if theOriginalName??> and theOriginalName=:theOriginalName</#if>"
		+ " <#if levelNumber??> and levelNumber=:levelNumber</#if>"
		+ " <#if parentLevelNumber??> and parentLevelNumber=:parentLevelNumber</#if>"
		+ " <#if theIndex??> and theIndex=:theIndex</#if>"
		+ " <#if theResource??> and theResource=:theResource</#if>"
		+ " <#if theType?? && theType == "+S_UIResourceType.Menu+"> and (theType="+S_UIResourceType.RealityMenu+" or theType="+S_UIResourceType.VirtualMenu+")</#if>"
		+ " <#if theType?? && theType != "+S_UIResourceType.Menu+"> and theType=:theType</#if>"
		+ " <#if isDefault??> and isDefault=:isDefault</#if>"
		+ " <#if remark??> and remark=:remark</#if>"
		+ " <#if parentUI??> and parentUI=:parentUI</#if>"
		+ " <#if userStartNeedNull?? && userStartNeedNull == true> and userStart=null</#if>"
		+ " <#if buttonIdArr??> and tableId in(:buttonIdArr)</#if>"
		+ " <#if childrenUIList??> and childrenUIList=:childrenUIList</#if>"
		+" <#if keyword??> and theName like :keyword</#if>"
		+ " <#if orderBy??> order by ${orderBy}</#if>"
		;
    }
	
	public String getHQLForUrlSelect()
    {
    	return "from Sm_Permission_UIResource where 1=1"
		+ " and theResource is not null and theResource != ''"
		+ " and theState = " +S_TheState.Normal;
    }

	public String getHQLForRoleUIAddOrUpdate()
	{
		return "from Sm_Permission_UIResource where 1=1"
				+ " and (theType = " + S_UIResourceType.VirtualMenu + " or theType = " + S_UIResourceType.RealityMenu + " or theType = " +S_UIResourceType.Button + " or theType = "+ S_UIResourceType.TheResource +")"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if inIdArr??> and tableId in (:inIdArr)</#if>";
	}
}

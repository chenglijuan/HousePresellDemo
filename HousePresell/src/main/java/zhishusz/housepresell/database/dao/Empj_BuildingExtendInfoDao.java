package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;

/*
 * Dao数据库操作：楼幢-扩展信息
 * Company：ZhiShuSZ
 * */
@Repository
public class Empj_BuildingExtendInfoDao extends BaseDao<Empj_BuildingExtendInfo>
{
	public String getBasicHQL()
    {
    	return "from Empj_BuildingExtendInfo where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if presellState??> and presellState=:presellState</#if>"
		+ " <#if eCodeOfPresell??> and eCodeOfPresell=:eCodeOfPresell</#if>"
		+ " <#if presellDate??> and presellDate=:presellDate</#if>"
		+ " <#if limitState??> and limitState=:limitState</#if>"
		+ " <#if escrowState??> and escrowState=:escrowState</#if>"
		+ " <#if landMortgageState??> and landMortgageState=:landMortgageState</#if>"
		+ " <#if landMortgagor??> and landMortgagor=:landMortgagor</#if>"
		+ " <#if landMortgageAmount??> and landMortgageAmount=:landMortgageAmount</#if>"
		+ " <#if isSupportPGS??> and isSupportPGS=:isSupportPGS</#if>";
    }
}

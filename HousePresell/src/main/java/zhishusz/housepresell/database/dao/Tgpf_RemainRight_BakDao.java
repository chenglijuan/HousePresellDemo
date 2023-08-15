package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_RemainRight_Bak;

/*
 * Dao数据库操作：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_RemainRight_BakDao extends BaseDao<Tgpf_RemainRight_Bak>
{
	public String getBasicHQL()
    {
    	return "from Tgpf_RemainRight_Bak where 1=1"
		+ " <#if theState??> and theState=:theState</#if>"
		+ " <#if eCode??> and eCode=:eCode</#if>";
    }
}

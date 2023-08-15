package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement_View;


/*
 * Dao数据库操作：网银上传Excel
 * Company：ZhiShuSZ
 * */
@Repository
public class Tgpf_CyberBankStatement_ViewListDao extends BaseDao<Tgpf_CyberBankStatement_View>
{
	
	public String getBasicHQL()
    {
    	return "from Tgpf_CyberBankStatement where 1=1"
    	+ " <#if keyword??> and (theNameOfBank like :keyword or accountOfBankAccountEscrowed like :keyword )</#if>"
		+ " <#if billTimeStamp??> and billTimeStamp=:billTimeStamp</#if>"
		+ " order by billTimeStamp desc, NLSSORT(theNameOfBank, 'NLS_SORT = SCHINESE_PINYIN_M')";
    }
	
}

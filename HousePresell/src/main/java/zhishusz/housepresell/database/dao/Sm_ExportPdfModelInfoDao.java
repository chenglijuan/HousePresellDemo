package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.pdf.Sm_ExportPdfModelInfo;

/*
 * Dao数据库操作：查询模板配置表
 * Company：ZhiShuSZ
 * */
@Repository
public class Sm_ExportPdfModelInfoDao extends BaseDao<Sm_ExportPdfModelInfo>
{

	public String getBasicHQL()
    {
    	return "from Sm_ExportPdfModelInfo where 1=1"
		+ " <#if busiCode??> and busiCode=:busiCode</#if>"
		+ " <#if isUsing??> and isUsing=:isUsing</#if>"
		;
    }
}

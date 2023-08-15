package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tgpj_BldLimitAmountVer_AFTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_BldLimitAmountVer_AFExportExcelService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BldLimitAmountVer_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Tgpj_BldLimitAmountVer_AF> tgpj_BldLimitAmountVer_AF;
		if (idArr == null || idArr.length < 1) 
		{
			tgpj_BldLimitAmountVer_AF = tgpj_BldLimitAmountVer_AFDao.findByPage(tgpj_BldLimitAmountVer_AFDao.getQuery(tgpj_BldLimitAmountVer_AFDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			tgpj_BldLimitAmountVer_AF = tgpj_BldLimitAmountVer_AFDao.findByPage(tgpj_BldLimitAmountVer_AFDao.getQuery(tgpj_BldLimitAmountVer_AFDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(tgpj_BldLimitAmountVer_AF, Tgpj_BldLimitAmountVer_AFTemplate.class, "受限额度节点版本列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("tgpj_BldLimitAmountVer_AF", tgpj_BldLimitAmountVer_AF);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

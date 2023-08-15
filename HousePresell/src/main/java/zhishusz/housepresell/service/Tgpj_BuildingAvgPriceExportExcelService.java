package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tgpj_BuildingAvgPriceTemplate;

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
public class Tgpj_BuildingAvgPriceExportExcelService
{
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BuildingAvgPriceForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Tgpj_BuildingAvgPrice> tgpj_BuildingAvgPrice;
		if (idArr == null || idArr.length < 1) 
		{
			tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao.findByPage(tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao.findByPage(tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(tgpj_BuildingAvgPrice, Tgpj_BuildingAvgPriceTemplate.class, "备案价格列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("tgpj_BuildingAvgPrice", tgpj_BuildingAvgPrice);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

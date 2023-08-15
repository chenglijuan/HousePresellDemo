package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_BldLimitAmount_AFTemplate;

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
public class Empj_BldLimitAmount_AFExportExcelService
{
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();

		List<Empj_BldLimitAmount_AF> empj_BldLimitAmount_AF;
		if (idArr == null || idArr.length < 1)
		{
			empj_BldLimitAmount_AF = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getBasicHQL(), model), null, null);
		}
		else
		{
			empj_BldLimitAmount_AF = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getExcelListHQL(), model), null, null);
		}

		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(empj_BldLimitAmount_AF, Empj_BldLimitAmount_AFTemplate.class, "受限额度管理");

		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("empj_BldLimitAmount_AF", empj_BldLimitAmount_AF);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

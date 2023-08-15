package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tgxy_BankAccountEscrowedTemplate;

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
public class Tgxy_BankAccountEscrowedExportExcelService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowed;
		if (idArr == null || idArr.length < 1) 
		{
			tgxy_BankAccountEscrowed = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			tgxy_BankAccountEscrowed = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(tgxy_BankAccountEscrowed, Tgxy_BankAccountEscrowedTemplate.class, "托管账户列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("tgxy_BankAccountEscrowed", tgxy_BankAccountEscrowed);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

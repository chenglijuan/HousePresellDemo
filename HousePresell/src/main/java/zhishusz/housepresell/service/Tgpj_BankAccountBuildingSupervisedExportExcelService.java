package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tgpj_BankAccountBuildingSupervisedTemplate;

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
public class Tgpj_BankAccountBuildingSupervisedExportExcelService
{
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BankAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Tgpj_BankAccountSupervised> tgpj_BankAccountSupervised;
		if (idArr == null || idArr.length < 1) 
		{
			tgpj_BankAccountSupervised = tgpj_BankAccountSupervisedDao.findByPage(tgpj_BankAccountSupervisedDao.getQuery(tgpj_BankAccountSupervisedDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			tgpj_BankAccountSupervised = tgpj_BankAccountSupervisedDao.findByPage(tgpj_BankAccountSupervisedDao.getQuery(tgpj_BankAccountSupervisedDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(tgpj_BankAccountSupervised, Tgpj_BankAccountBuildingSupervisedTemplate.class, "楼幢监管账户列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("tgpj_BankAccountSupervised", tgpj_BankAccountSupervised);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

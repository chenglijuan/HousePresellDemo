package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_BankInfoForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExcelUtil2;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Emmp_BankInfoTemplate;
import zhishusz.housepresell.util.excel.model.Emmp_CompanyInfoTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_BankInfoExportExcelService
{
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	
	@SuppressWarnings({
			"unchecked", "static-access"
	})
	public Properties execute(Emmp_BankInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Emmp_BankInfo> emmp_BankInfoList;
		if (idArr == null || idArr.length < 1) 
		{
			emmp_BankInfoList = emmp_BankInfoDao.findByPage(emmp_BankInfoDao.getQuery(emmp_BankInfoDao.getBasicHQL(), model), null, null);
		} 
		else 
		{
			emmp_BankInfoList = emmp_BankInfoDao.findByPage(emmp_BankInfoDao.getQuery(emmp_BankInfoDao.getExcelListHQL(), model), null, null); 
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(emmp_BankInfoList, Emmp_BankInfoTemplate.class, "金融机构列表");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}
		
		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}


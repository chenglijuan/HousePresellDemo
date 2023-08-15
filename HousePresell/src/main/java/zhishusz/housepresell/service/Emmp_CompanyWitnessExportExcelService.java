package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Emmp_CompanyInfoTemplate;
import zhishusz.housepresell.util.excel.model.Emmp_CompanyWitnessTemplate;
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
public class Emmp_CompanyWitnessExportExcelService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Emmp_CompanyInfo> emmp_CompanyInfoList;
		if (idArr == null || idArr.length < 1) 
		{
			emmp_CompanyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			emmp_CompanyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(emmp_CompanyInfoList, Emmp_CompanyWitnessTemplate.class, "进度见证服务单位列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("emmp_CompanyInfoList", emmp_CompanyInfoList);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

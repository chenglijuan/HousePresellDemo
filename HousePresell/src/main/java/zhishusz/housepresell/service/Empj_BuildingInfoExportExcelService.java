package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_BuildingInfoTemplate;

/*
 * Service列表导出：用户信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BuildingInfoExportExcelService
{
	@Autowired
	private Empj_BuildingInfoDao Empj_BuildingInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String keyword = model.getKeyword() == "" ? null : "%"+model.getKeyword()+"%";
		model.setKeyword(keyword);
		
		List<Empj_BuildingInfo> empj_BuildingInfoList;
		if (idArr == null || idArr.length < 1) 
		{
			empj_BuildingInfoList = Empj_BuildingInfoDao.findByPage(Empj_BuildingInfoDao.getQuery(Empj_BuildingInfoDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			empj_BuildingInfoList = Empj_BuildingInfoDao.findByPage(Empj_BuildingInfoDao.getQuery(Empj_BuildingInfoDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		
		Properties propertiesExport = exportToExcelUtil.execute(empj_BuildingInfoList, Empj_BuildingInfoTemplate.class, "楼幢列表信息");
		
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

package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_ProjectInfoTemplate;
//import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

/*
 * Service列表查询：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_ProjectInfoExportExcelService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String keyword = model.getKeyword();
		if (keyword == null || "".equals(keyword))
		{
			model.setKeyword(null);			
		}
	
		List<Empj_ProjectInfo> empj_ProjectInfoList;
		if (idArr == null || idArr.length < 1)
		{
			empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), model), null, null);
		}
		else
		{
			empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(empj_ProjectInfoList, Empj_ProjectInfoTemplate.class, "项目列表信息");
		
		if (S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result))) 
		{
			return properties;
		}
		
		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("empj_ProjectInfoList", empj_ProjectInfoList);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_BldEscrowCompletedTemplate;

/*
 * Service列表查询：托管终止信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BldEscrowCompletedExportExcelService 
{
	@Autowired
	private Empj_BldEscrowCompletedDao empj_bldEscrowCompletedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BldEscrowCompleted_DtlForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		if (model.getKeyword() == null || "".equals(model.getKeyword()))
		{
			model.setKeyword(null);			
		}
		if ("0".equals(model.getBusiState())) 
		{
			model.setBusiState(null);
		}
		if (model.getDevelopCompanyId() == null || model.getDevelopCompanyId() <= 0)
		{
			model.setDevelopCompanyId(null);
		}
		model.setTheState(S_TheState.Normal);
		
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList;
		if (idArr == null || idArr.length < 1)
		{
			empj_BldEscrowCompleted_DtlList =
					empj_bldEscrowCompletedDao.findByPage(empj_bldEscrowCompletedDao.getQuery(empj_bldEscrowCompletedDao.getBasicHQL(), model), null, null);
		}
		else
		{
			empj_BldEscrowCompleted_DtlList = empj_bldEscrowCompletedDao.findByPage(empj_bldEscrowCompletedDao.getQuery(empj_bldEscrowCompletedDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		
		Properties propertiesExport = exportToExcelUtil.execute(empj_BldEscrowCompleted_DtlList, Empj_BldEscrowCompletedTemplate.class, "托管终止列表信息");
		
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

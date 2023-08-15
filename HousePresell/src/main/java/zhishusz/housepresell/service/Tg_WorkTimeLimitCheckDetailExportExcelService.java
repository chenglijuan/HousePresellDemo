package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_WorkTimeLimitCheckForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.WorkTimeLimitDetailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service导出Excel：工作时限检查详情
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_WorkTimeLimitCheckDetailExportExcelService
{
	@Autowired
	private Tg_WorkTimeLimitCheckDetailService tg_workTimeLimitCheckDetailService;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_WorkTimeLimitCheckForm model)
	{
		Properties properties = tg_workTimeLimitCheckDetailService.execute(model);
		if (S_NormalFlag.fail.equals(properties.getProperty(S_NormalFlag.result)))
		{
			return properties;
		}
		List<Tg_WorkTimeLimitCheckDetailService.WorkTimeLimitDetail> workTimeLimitDetailList = (List<Tg_WorkTimeLimitCheckDetailService.WorkTimeLimitDetail>) properties.get("workTimeLimitDetailList");
		for (int i = 0; i < workTimeLimitDetailList.size(); i++) {
			Tg_WorkTimeLimitCheckDetailService.WorkTimeLimitDetail workTimeLimitDetail = workTimeLimitDetailList.get(i);
			workTimeLimitDetail.setIndex(i + 1);
		}

		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(workTimeLimitDetailList, WorkTimeLimitDetailTemplate.class, "工作时限检查详情-" + model.getDateStr());

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

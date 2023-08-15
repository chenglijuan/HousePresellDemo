package zhishusz.housepresell.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRight_DtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tgpf_RemainRightDifferenceTemplate;

/*
 * ServiceExcel导出：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_RemainRightDifferenceExportExcelService
{
	@Autowired
	private Tgpf_RemainRightDifferenceService tgpf_RemainRightDifferenceService;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RemainRightForm model)
	{
		Properties properties = new MyProperties();
		
		List<Tgpf_RemainRight_DtlForm> tgpf_RemainRight_DtlForms = new ArrayList<Tgpf_RemainRight_DtlForm>();
		String eCodeOfBuilding = null; 
		for (int i = 0; i < model.getTgpf_RemainRight_DtlTab().length; i++) {
			Tgpf_RemainRight_DtlForm tgpf_RemainRight_DtlForm = model.getTgpf_RemainRight_DtlTab()[i];
			if (eCodeOfBuilding == null) {
				eCodeOfBuilding = tgpf_RemainRight_DtlForm.geteCodeOfBuilding();
			}
			if (tgpf_RemainRight_DtlForm.getTheAmount_Compare() == 0 && 
					tgpf_RemainRight_DtlForm.getLimitedRetainRight_Compare() == 0 && 
					tgpf_RemainRight_DtlForm.getWithdrawableRetainRight_Compare() == 0) {
				
			} else {
				tgpf_RemainRight_DtlForm.setIndex(tgpf_RemainRight_DtlForms.size() + 1);
				tgpf_RemainRight_DtlForms.add(tgpf_RemainRight_DtlForm);
			}
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(tgpf_RemainRight_DtlForms, Tgpf_RemainRightDifferenceTemplate.class, "留存权益差异" + (eCodeOfBuilding != null ? "-楼幢号" + eCodeOfBuilding : "") + "-" + model.getBillTimeStamp());
		
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

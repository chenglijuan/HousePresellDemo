package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_RemainRightDifferenceForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightImportExcelForm;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/*
 * Service差异对比：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRightDifferenceService
{
	@Autowired
	private Tgpf_RemainRightListService tgpf_RemainRightListService;
	@Autowired
	private Tgpf_RemainRightImportExcelService tgpf_RemainRightImportExcelService;
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RemainRightDifferenceForm model)
	{
		Properties properties = new MyProperties();
		// Upload RemainRight List
		List<Tgpf_RemainRight> tgpf_RemainRights_Upload;
		if (model.getUrl() != null && model.getUrl().trim().length() > 0) {
			Tgpf_RemainRightImportExcelForm tgpf_RemainRightImportExcelForm = new Tgpf_RemainRightImportExcelForm();
			tgpf_RemainRightImportExcelForm.setUrl(model.getUrl());
			tgpf_RemainRightImportExcelForm.setBillTimeStamp(model.getBillTimeStamp());
			Properties propertiesUpload = tgpf_RemainRightImportExcelService.execute(tgpf_RemainRightImportExcelForm);
			if (S_NormalFlag.fail.equals(propertiesUpload.get(S_NormalFlag.result))) {
				return propertiesUpload;
			}
			tgpf_RemainRights_Upload = (List<Tgpf_RemainRight>) propertiesUpload.get("tgpf_RemainRightList");

			if (tgpf_RemainRights_Upload.size() > 0) {
				Tgpf_RemainRight tgpf_remainRight = tgpf_RemainRights_Upload.get(0);
				if (!tgpf_remainRight.getTheNameOfProject().equals(model.getTheNameOfProject()) || !tgpf_remainRight.geteCodeOfBuilding().equals(model.getECodeFromConstruction())) {
					return MyBackInfo.fail(properties, "请检查项目名称和楼幢是否匹配");
				}
			}
		} else {
			tgpf_RemainRights_Upload = new ArrayList<Tgpf_RemainRight>();
		}
		if (model.getDeleCodes() != null) {
			List<String> delStrings = Arrays.asList(model.getDeleCodes());
			for (Tgpf_RemainRight tgpf_RemainRight : tgpf_RemainRights_Upload) {
				if (delStrings.contains(tgpf_RemainRight.geteCode())) {
					tgpf_RemainRights_Upload.remove(tgpf_RemainRight);
				}
			}
		}
		
		// Platform RemainRight List
		Tgpf_RemainRightForm tgpf_RemainRightForm = new Tgpf_RemainRightForm();
		tgpf_RemainRightForm.setBuildingRemainRightLogId(model.getBuildingRemainRightLogId());
		Properties propertiesPlatform = tgpf_RemainRightListService.execute(tgpf_RemainRightForm);
		List<Tgpf_RemainRight> tgpf_RemainRights_Platform = (List<Tgpf_RemainRight>) propertiesPlatform.get("tgpf_RemainRightList");
		
		if (model.getUrl() != null && model.getUrl().trim().length() > 0) {
			if (tgpf_RemainRights_Upload.size() != tgpf_RemainRights_Platform.size()) {
				Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog =  tgpf_BuildingRemainRightLogDao.findById(model.getBuildingRemainRightLogId());
				tgpf_BuildingRemainRightLog.setBusiState(Tgpf_BuildingRemainRightLog.Compared_Failed);
				tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
			} else {
				boolean failed = false;
				for (int i = 0; i < tgpf_RemainRights_Upload.size(); i++) {
					if (failed) {
						break;
					}
					
					boolean failedSub = true;
					Tgpf_RemainRight tgpf_RemainRight_Upload = tgpf_RemainRights_Upload.get(i);
					for (int j = 0; j < tgpf_RemainRights_Platform.size(); j++) {
						Tgpf_RemainRight tgpf_RemainRight_Platform = tgpf_RemainRights_Platform.get(j);
						if (tgpf_RemainRight_Upload.geteCodeOfContractRecord().equals(tgpf_RemainRight_Platform.geteCodeOfContractRecord())) {
							if (tgpf_RemainRight_Upload.getTheAmount().doubleValue() == tgpf_RemainRight_Platform.getTheAmount().doubleValue()
									&& tgpf_RemainRight_Upload.getLimitedRetainRight().doubleValue() == tgpf_RemainRight_Platform.getLimitedRetainRight().doubleValue()
									&& tgpf_RemainRight_Upload.getWithdrawableRetainRight().doubleValue() == tgpf_RemainRight_Platform.getWithdrawableRetainRight().doubleValue()) {
								failedSub = false;
							} else {
								failed = true;
								Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog =  tgpf_BuildingRemainRightLogDao.findById(model.getBuildingRemainRightLogId());
								tgpf_BuildingRemainRightLog.setBusiState(Tgpf_BuildingRemainRightLog.Compared_Failed);
								tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
							}
							break;
						}
					}
					if (failedSub) {
						failed = true;
						Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog =  tgpf_BuildingRemainRightLogDao.findById(model.getBuildingRemainRightLogId());
						tgpf_BuildingRemainRightLog.setBusiState(Tgpf_BuildingRemainRightLog.Compared_Failed);
						tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
						break;
					}
				}
				if (!failed) {
					Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog =  tgpf_BuildingRemainRightLogDao.findById(model.getBuildingRemainRightLogId());
					tgpf_BuildingRemainRightLog.setBusiState(Tgpf_BuildingRemainRightLog.Compared_Success);
					tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
				}
			}
		}
		
		properties.put("tgpf_RemainRightList_Upload", tgpf_RemainRights_Upload);
		properties.put("tgpf_RemainRightList_Platform", tgpf_RemainRights_Platform);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

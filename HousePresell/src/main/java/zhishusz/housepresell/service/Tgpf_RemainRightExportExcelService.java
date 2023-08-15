package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_RemainRightExportExcelForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExcelUtil2;
import zhishusz.housepresell.util.excel.model.Tgpf_RemainRightTemplate;

/*
 * ServiceExcel导出：留存权益
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_RemainRightExportExcelService
{
	
	@Autowired
	private Tgpf_RemainRightListService tgpf_RemainRightListService;
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RemainRightExportExcelForm model)
	{
		Properties properties = new MyProperties();
		
		List<String> sheetNames = new ArrayList<String>();
		List<String> headerList = getHeadList();
		List<List<Tgpf_RemainRightTemplate>> remainRightsList = new ArrayList<List<Tgpf_RemainRightTemplate>>();
		if (model.getBuildingRemainRightLogIds() != null) {
			for (Long lodId : model.getBuildingRemainRightLogIds()) {
				List<Tgpf_RemainRightTemplate> remainRightList = new ArrayList<Tgpf_RemainRightTemplate>();
				Tgpf_RemainRightForm tgpf_RemainRightForm = new Tgpf_RemainRightForm();
				tgpf_RemainRightForm.setBuildingRemainRightLogId(lodId);
				Properties propertiesRemainRights = tgpf_RemainRightListService.execute(tgpf_RemainRightForm);
				List<Tgpf_RemainRight> tgpf_RemainRightList = (List<Tgpf_RemainRight>) propertiesRemainRights.get("tgpf_RemainRightList");
				for (int i = 0; i < tgpf_RemainRightList.size(); i++) {
					Tgpf_RemainRight tgpf_RemainRight = tgpf_RemainRightList.get(i);
					remainRightList.add(Tgpf_RemainRightTemplate.rebuild(tgpf_RemainRight, i + 1));
				}
				remainRightsList.add(remainRightList);
				Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = tgpf_BuildingRemainRightLogDao.findById(lodId);
				sheetNames.add("楼幢编号" + tgpf_BuildingRemainRightLog.getBuilding().geteCode() + "-" + tgpf_BuildingRemainRightLog.getBillTimeStamp());
			}
		}
		
		Properties propertiesExport = ExcelUtil2.excelExport(sheetNames, headerList, remainRightsList, "留存权益");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	private List<String> getHeadList() {
		List<String> heaList = new ArrayList<String>();
		heaList.add("index#序号");
		heaList.add("enterTimeStamp#入账日期");
		heaList.add("buyer#买受人姓名");
		heaList.add("theNameOfCreditor#借款人姓名");
		heaList.add("idNumberOfCreditor#借款人证件号码");
		heaList.add("eCodeOfContractRecord#合同备案号");
		heaList.add("eCodeOfTripleAgreement#三方协议号");
		heaList.add("theNameOfProject#项目名称");
		heaList.add("eCodeOfBuilding#楼幢号");
		heaList.add("eCodeOfBuildingUnit#单元号");
		heaList.add("eCodeFromRoom#房间号");
		heaList.add("actualDepositAmount#实际入账金额#");
		heaList.add("theAmount#留存权益总金额");
		heaList.add("limitedRetainRight#受限权益");
		heaList.add("withdrawableRetainRight#可支取权益");
		return heaList;
	}
}

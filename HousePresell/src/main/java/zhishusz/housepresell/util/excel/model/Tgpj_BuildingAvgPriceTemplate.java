package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Tgpj_BuildingAvgPriceTemplate implements IExportExcel<Tgpj_BuildingAvgPrice>
{
	@Getter @Setter
	private String eCode;
	@Getter @Setter
	private String companyName;
	@Getter @Setter
	private String projectName;
	@Getter @Setter
	private String eCodeFromConstruction;
	@Getter @Setter
	private String eCodeFromPublicSecurity;
	@Getter @Setter
	private String buildingEcode;
	@Getter @Setter
	private String recordAveragePrice;
	@Getter @Setter
	private String recordAveragePriceFromPresellSystem;
	@Getter @Setter
	private String averagePriceRecordDate;
	@Getter @Setter
	private String busiState;

	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("eCode", "申请单号");
		map.put("companyName", "开发企业");
		map.put("projectName", "项目名称");
		map.put("eCodeFromConstruction", "施工编号");
		map.put("eCodeFromPublicSecurity", "公安编号");
		map.put("buildingEcode", "楼幢编号");
		map.put("recordAveragePrice", "楼幢住宅备案均价(元/m²)");
		map.put("recordAveragePriceFromPresellSystem", "预售系统备案均价(元/m²)");
		map.put("averagePriceRecordDate", "物价备案日期");
		map.put("busiState", "状态");

		return map;
	}

	@Override
	public void init(Tgpj_BuildingAvgPrice fromClass)
	{
		this.setECode(fromClass.geteCode());
		Empj_BuildingInfo buildingInfo = fromClass.getBuildingInfo();
		if (buildingInfo != null)
		{
			Emmp_CompanyInfo developCompany = buildingInfo.getDevelopCompany();
			if (developCompany != null)
			{
				setCompanyName(developCompany.getTheName());
			}
			Empj_ProjectInfo project = buildingInfo.getProject();
			if (project != null)
			{
				setProjectName(project.getTheName());
			}
			setECodeFromConstruction(buildingInfo.geteCodeFromConstruction());
			setECodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());
			setBuildingEcode(buildingInfo.geteCode());

		}
		setRecordAveragePrice(fromClass.getRecordAveragePrice()+"");
		setRecordAveragePriceFromPresellSystem(fromClass.getRecordAveragePriceFromPresellSystem()+"");
		setAveragePriceRecordDate(fromClass.getAveragePriceRecordDate()+"");
		setBusiState(fromClass.getBusiState());

	}
}
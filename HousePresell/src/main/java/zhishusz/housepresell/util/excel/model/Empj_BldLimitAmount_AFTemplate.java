package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Empj_BldLimitAmount_AFTemplate implements IExportExcel<Empj_BldLimitAmount_AF>
{
	@Getter @Setter
	private String eCode;
	@Getter @Setter
	private String companyName;
	@Getter @Setter
	private String projectName;
	@Getter @Setter
	private String buildingEcode;
	@Getter @Setter
	private String buildingArea;
	@Getter @Setter
	private String escrowedArea;
	@Getter @Setter
	private String currentFigureProgress;
	@Getter @Setter
	private String nodeLimitedAmount;
	@Getter @Setter
	private String expectFigureProgress;
	@Getter @Setter
	private String expectLimitedAmount;
	@Getter @Setter
	private String recordAveragePriceOfBuilding;
	@Getter @Setter
	private String busiState;



	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("eCode", "受限额度变更单号");
		map.put("companyName", "开发企业");
		map.put("projectName", "项目名称");
		map.put("buildingEcode", "楼幢编号");
		map.put("buildingArea", "建筑面积");
		map.put("escrowedArea", "托管面积");
		map.put("currentFigureProgress", "当前形象进度");
		map.put("nodeLimitedAmount", "当前受限额度");
		map.put("expectFigureProgress", "拟变更形象进度");
		map.put("expectLimitedAmount", "拟变受限额度");
		map.put("recordAveragePriceOfBuilding", "当前楼幢住宅备案均价（元/㎡）");
		map.put("busiState", "状态");

		return map;
	}

	@Override
	public void init(Empj_BldLimitAmount_AF fromClass)
	{
		setECode(fromClass.geteCode());
		Emmp_CompanyInfo company = fromClass.getDevelopCompany();
		if (company != null)
		{
			setCompanyName(company.getTheName());
		}
		Empj_ProjectInfo project = fromClass.getProject();
		if (project != null)
		{
			setProjectName(project.getTheName());
		}
		Empj_BuildingInfo building = fromClass.getBuilding();
		if (building != null)
		{
			setBuildingEcode(building.geteCode());
			setBuildingArea(building.getBuildingArea()+"");
		}
		setCurrentFigureProgress(fromClass.getCurrentFigureProgress()+"");
		setNodeLimitedAmount(fromClass.getNodeLimitedAmount()+"");
		setExpectFigureProgress(fromClass.getExpectFigureProgress()+"");
		setExpectLimitedAmount(fromClass.getExpectLimitedAmount()+"");
		setRecordAveragePriceOfBuilding(fromClass.getRecordAveragePriceOfBuilding()+"");
		setBusiState(fromClass.getBusiState());

	}
}
package zhishusz.housepresell.util.excel.model;

import java.util.HashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDouble;

public class Empj_BldEscrowCompletedTemplate implements IExportExcel<Empj_BldEscrowCompleted_Dtl>
{	
	@IFieldAnnotation(remark="托管终止申请单号")
	private String eCode;
	
	@IFieldAnnotation(remark="交付备案批准文件号")
	private String eCodeFromDRAD;
	
	@IFieldAnnotation(remark="开发企业")
	private String developCompanyName;
	
	@IFieldAnnotation(remark="项目名称")
	private String projectName;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeofBuilding;
	
	@IFieldAnnotation(remark="建筑面积")
	private Double buildingArea;
	
	@IFieldAnnotation(remark="托管面积")
	private Double escrowArea;
	
	@IFieldAnnotation(remark="当前楼幢住宅备案均价(元/㎡)")
	private Double recordAvgPriceOfBuilding;
	
	@Override
	public Map<String, String> GetExcelHead() {
		// TODO Auto-generated method stub

		Map<String, String> map = new HashMap<String, String>();
		map.put("eCode", "托管终止申请单号");
		map.put("eCodeFromDRAD", "交付备案批准文件号");
		map.put("developCompanyName", "开发企业");
		map.put("projectName", "项目名称");
		map.put("eCodeofBuilding", "楼幢编号");
		map.put("buildingArea", "建筑面积");
		map.put("escrowArea", "托管面积");
		map.put("recordAvgPriceOfBuilding", "当前楼幢住宅备案均价(元/㎡)");
		return map;
	}

	@Override
	public void init(Empj_BldEscrowCompleted_Dtl bldEscrowCompleted_Dtl) {
		// TODO Auto-generated method stub
		Empj_BldEscrowCompleted bldEscrowCompleted = bldEscrowCompleted_Dtl.getMainTable();
		if (bldEscrowCompleted != null) 
		{
			this.seteCode(bldEscrowCompleted.geteCode());
			this.seteCodeFromDRAD(bldEscrowCompleted.geteCodeFromDRAD());
		}
		else
		{
			this.seteCode("");
			this.seteCodeFromDRAD("");
		}
		Emmp_CompanyInfo developCompany = bldEscrowCompleted_Dtl.getDevelopCompany();
		if (developCompany != null) 
		{
			this.setDevelopCompanyName(developCompany.getTheName());
		}
		else
		{
			this.setDevelopCompanyName("");
		}
		Empj_ProjectInfo project = bldEscrowCompleted_Dtl.getProject();
		if (project != null) 
		{
			this.setProjectName(project.getTheName());
		}
		else
		{
			this.setProjectName("");
		}
		Empj_BuildingInfo building = bldEscrowCompleted_Dtl.getBuilding();
		if (building != null) 
		{
			MyDouble muDouble = MyDouble.getInstance(); 
			this.seteCodeofBuilding(building.geteCode());
			this.setBuildingArea(muDouble.getShort(building.getBuildingArea(), 2));
			this.setEscrowArea(muDouble.getShort(building.getEscrowArea(), 2));
			
			Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
			if (buildingAccount != null)
			{
				this.setRecordAvgPriceOfBuilding(muDouble.getShort(buildingAccount.getRecordAvgPriceOfBuilding(), 2));			
			}
			else
			{
				this.setRecordAvgPriceOfBuilding(0.0);
			}
		}
		else
		{
			this.seteCodeofBuilding("");
			this.setBuildingArea(0.0);
			this.setEscrowArea(0.0);
			this.setRecordAvgPriceOfBuilding(0.0);
		}
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeFromDRAD() {
		return eCodeFromDRAD;
	}

	public void seteCodeFromDRAD(String eCodeFromDRAD) {
		this.eCodeFromDRAD = eCodeFromDRAD;
	}

	public String getDevelopCompanyName() {
		return developCompanyName;
	}

	public void setDevelopCompanyName(String developCompanyName) {
		this.developCompanyName = developCompanyName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String geteCodeofBuilding() {
		return eCodeofBuilding;
	}

	public void seteCodeofBuilding(String eCodeofBuilding) {
		this.eCodeofBuilding = eCodeofBuilding;
	}

	public Double getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(Double buildingArea) {
		this.buildingArea = buildingArea;
	}

	public Double getEscrowArea() {
		return escrowArea;
	}

	public void setEscrowArea(Double escrowArea) {
		this.escrowArea = escrowArea;
	}

	public Double getRecordAvgPriceOfBuilding() {
		return recordAvgPriceOfBuilding;
	}

	public void setRecordAvgPriceOfBuilding(Double recordAvgPriceOfBuilding) {
		this.recordAvgPriceOfBuilding = recordAvgPriceOfBuilding;
	}

}

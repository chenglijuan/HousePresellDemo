package zhishusz.housepresell.util.excel.model;

import java.util.LinkedHashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Empj_BuildingInfoTemplate implements IExportExcel<Empj_BuildingInfo>
{
	@IFieldAnnotation(remark="楼幢编号")
	private String eCode;
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="托管项目名称")
	private String theNameFromPresellSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积")
	private Double buildingArea;
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积")
	private Double escrowArea;
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String developCompanyName;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;
	
	@Override
	public String toString() {
		return "{\"eCode\":\"" + eCode + "\",\"eCodeFromConstruction\":\"" + eCodeFromConstruction
				+ "\",\"eCodeFromPublicSecurity\":\"" + eCodeFromPublicSecurity + "\",\"theNameFromPresellSystem\":\""
				+ theNameFromPresellSystem + "\",\"buildingArea\":\"" + buildingArea + "\",\"escrowArea\":\""
				+ escrowArea + "\",\"developCompanyName\":\"" + developCompanyName + "\",\"theNameOfProject\":\""
				+ theNameOfProject + "\"}";
	}

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("eCode", "楼幢编号");
		map.put("eCodeFromConstruction", "施工编号");
		map.put("eCodeFromPublicSecurity", "公安编号");
		map.put("theNameFromPresellSystem", "托管项目名称");
		map.put("buildingArea", "建筑面积");
		map.put("escrowArea", "托管面积");
		map.put("developCompanyName", "开发企业");
		map.put("theNameOfProject", "项目名称");
		
		return map;
	}
	
	public void init(Empj_BuildingInfo buildingInfo)
	{
		this.seteCode(buildingInfo.geteCode());
		this.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
		this.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());
		this.setTheNameFromPresellSystem(buildingInfo.getTheNameFromPresellSystem());
		this.setBuildingArea(buildingInfo.getBuildingArea());
		this.setEscrowArea(buildingInfo.getEscrowArea());
		if(buildingInfo.getDevelopCompany() != null)
		{
			this.setDevelopCompanyName(buildingInfo.getDevelopCompany().getTheName());
		}
		else
		{
			this.setDevelopCompanyName("");
		}
		if(buildingInfo.getProject() != null)
		{
			this.setTheNameOfProject(buildingInfo.getProject().getTheName());
		}
		else
		{
			this.setTheNameOfProject("");
		}
	}

	public String geteCode() 
	{
		return eCode;
	}

	public void seteCode(String eCode) 
	{
		this.eCode = eCode;
	}

	public String geteCodeFromConstruction() 
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction) 
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	public String geteCodeFromPublicSecurity() 
	{
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) 
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
}
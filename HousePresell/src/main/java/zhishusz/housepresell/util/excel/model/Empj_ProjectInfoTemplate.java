package zhishusz.housepresell.util.excel.model;

import java.util.HashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

public class Empj_ProjectInfoTemplate implements IExportExcel<Empj_ProjectInfo>
{
	@Getter @Setter @IFieldAnnotation(remark = "项目编号")
	private String eCode;
	
	@Getter @Setter @IFieldAnnotation(remark = "项目名称")
	private String theName;

	@Getter @Setter @IFieldAnnotation(remark = "开发企业")
	private String developCompanyName;

	@Getter @Setter @IFieldAnnotation(remark = "所属区域")
	private String cityRegionName;

	@Getter @Setter @IFieldAnnotation(remark = "项目地址")
	private String address;

	@Getter @Setter @IFieldAnnotation(remark="项目联系人")
	private String contactPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="项目联系人电话")
	private String contactPhone;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState; 
	
	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<String, String>();

		map.put("eCode", "项目编号");
		map.put("theName", "项目名称");
		map.put("developCompanyName", "开发企业");
		map.put("cityRegionName", "所属区域");
		map.put("address", "项目地址");
		map.put("contactPerson", "项目联系人");
		map.put("contactPhone", "项目联系人电话");
		map.put("busiState", "项目业务状态");

		return map;
	}

	@Override
	public void init(Empj_ProjectInfo projectInfo)
	{
		this.setECode(projectInfo.geteCode());
		this.setTheName(projectInfo.getTheName());
		this.setAddress(projectInfo.getAddress());
		this.setContactPerson(projectInfo.getContactPerson());
		this.setContactPhone(projectInfo.getContactPhone());
		this.setBusiState(projectInfo.getBusiState());
		Emmp_CompanyInfo companyInfo = projectInfo.getDevelopCompany();
		if(companyInfo != null)
		{
			this.setDevelopCompanyName(companyInfo.getTheName());
		}
		else
		{
			this.setDevelopCompanyName("");
		}
		Sm_CityRegionInfo cityRegionInfo = projectInfo.getCityRegion();
		if(cityRegionInfo != null)
		{
			this.setCityRegionName(cityRegionInfo.getTheName());;
		}
		else
		{
			this.setCityRegionName("");
		}
	}
}

package test.ExportExcel;

import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.util.excel.model.Empj_BuildingInfoTemplate;
import zhishusz.housepresell.util.excel.model.IExportExcel;

public class TestDozer 
{
	public static void main(String[] args)
	{
		Emmp_CompanyInfo emmp_CompanyInfo = new Emmp_CompanyInfo();
		emmp_CompanyInfo.setTableId(1l);
		emmp_CompanyInfo.setTheName("企业名称");
		
		Empj_ProjectInfo empj_ProjectInfo = new Empj_ProjectInfo();
		empj_ProjectInfo.setTableId(1l);
		empj_ProjectInfo.setTheName("项目名称");
		empj_ProjectInfo.setDevelopCompany(emmp_CompanyInfo);
		
		Empj_BuildingInfo empj_BuildingInfo = new Empj_BuildingInfo();
		empj_BuildingInfo.setTableId(1l);
		empj_BuildingInfo.setProject(empj_ProjectInfo);
		empj_BuildingInfo.setDevelopCompany(emmp_CompanyInfo);
		
		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		empj_BuildingInfoList.add(empj_BuildingInfo);
		
		getTemplateList(empj_BuildingInfoList, Empj_BuildingInfoTemplate.class);
	}
	
	public static List getTemplateList(List fromList, Class toClass)
	{
		System.out.println(fromList.get(0).toString());
		List list = new ArrayList();
		for(Object fromObj : fromList)
		{
			try
			{
				IExportExcel iExportExcel = (IExportExcel)toClass.newInstance();
				iExportExcel.init(fromObj);
				
				list.add(iExportExcel);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		System.out.println(list);
		
		return list;
	}
}

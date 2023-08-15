package test.ExportExcel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.util.excel.model.Empj_BuildingInfoTemplate;
import zhishusz.housepresell.util.excel.model.IExportExcel;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class TestEmpj_BuildingInfoExportExcel extends BaseJunitTest 
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(3434l);
		
		List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		
		empj_BuildingInfoList.add(empj_BuildingInfo);
		
		getTemplateList(empj_BuildingInfoList, Empj_BuildingInfoTemplate.class);
	}
	
	public List getTemplateList(List fromList, Class toClass)
	{
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

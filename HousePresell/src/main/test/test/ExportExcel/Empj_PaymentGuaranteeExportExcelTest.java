package test.ExportExcel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.util.excel.model.IExportExcel;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class Empj_PaymentGuaranteeExportExcelTest extends BaseJunitTest
{
	
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	
	@Test
	public void execute() throws Exception
	{
		Empj_PaymentGuarantee empj_PaymentGuarantee = empj_PaymentGuaranteeDao.findById(10748);
		
		List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList = new ArrayList<Empj_PaymentGuarantee>();
		
		empj_PaymentGuaranteeList.add(empj_PaymentGuarantee);
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

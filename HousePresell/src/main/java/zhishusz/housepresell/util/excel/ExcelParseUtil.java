package zhishusz.housepresell.util.excel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zhishusz.housepresell.util.MyDatetime;

public class ExcelParseUtil
{
	/*
	 * 用法：ExcelParseUtil excelParseUtil = new ExcelParseUtil();
	 * 		excelParseUtil.decode(list, ExcelMember.class);
	 * */
	@SuppressWarnings("rawtypes")
	public List<?> decode(List<List> orgList, Class<? extends IExcel> iExcel)
	{
		List<IExcel> listResult = new ArrayList<IExcel>();
		
		List headList = orgList.remove(0);//第一行（表头行）
		Integer totalLineCount = orgList.size();
		
		for(int lineNumber=0;lineNumber<totalLineCount;lineNumber++)//List rowList: orgList
		{
			Object obj = null;
			try
			{
				obj = iExcel.newInstance();
			}
			catch (InstantiationException | IllegalAccessException e1)
			{
				e1.printStackTrace();
			}
			if(obj != null && obj instanceof IExcel)
			{
				IExcel iExcelModel = (IExcel)obj;
				iExcelModel.setLineNumber(lineNumber+1);//因为表头行是第一行，此处数据行是第二行

				List rowList = orgList.get(lineNumber);
				int length = rowList.size();
				for(int index=0; index<length; index++)
				{
					String paraName = headList.get(index).toString();
					Object value = rowList.get(index);
					Method set_Method = iExcelModel.getParaSetMethod(paraName);//目前设置成setString
					if(set_Method != null)
					{
						try
						{
							//TODO 一些特殊类型 还需要在这里提前转成 对应的String类型，我们导出的模板定义的属性均为String行，某则无法注入成功，类型不匹配
							if(value instanceof Date)
							{
								value = MyDatetime.getInstance().dateToString(((Date)value).getTime());
							}
							
							set_Method.invoke(iExcelModel, value);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							e.printStackTrace();
						}
					}
				}
				
				//初步验证参数（判空，判合法性）
				iExcelModel.check();
				
				listResult.add(iExcelModel);
			}
		}
		
		return listResult;
	}
}

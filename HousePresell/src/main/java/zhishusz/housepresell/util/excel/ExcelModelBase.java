package zhishusz.housepresell.util.excel;

import java.lang.reflect.Method;
import java.util.Map;

//Excel文件模型基类
public abstract class ExcelModelBase implements IExcel
{
	protected Map<String, String> paraMap;
	protected Integer lineNumber;
	
	public Method getParaSetMethod(String paraHeadName)
	{
		Method method = null;
		
		String paraName = paraMap.get(paraHeadName);
		if(paraName != null)
		{
			try
			{
				//首字母大写
				char[] cs=paraName.toCharArray();
			    cs[0]-=32;
			    
				method = this.getClass().getMethod("set" + String.valueOf(cs), String.class);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
		
		return method;
	}

	public Integer getLineNumber()
	{
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber)
	{
		this.lineNumber = lineNumber;
	}
}

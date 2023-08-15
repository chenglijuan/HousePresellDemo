package zhishusz.housepresell.util.excel;

import java.lang.reflect.Method;

public interface IExcel
{
	void check();
	Method getParaSetMethod(String paraHeadName);
	void setLineNumber(Integer lineNumber);
}

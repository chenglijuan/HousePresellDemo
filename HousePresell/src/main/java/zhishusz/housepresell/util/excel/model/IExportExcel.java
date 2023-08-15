package zhishusz.housepresell.util.excel.model;

import java.util.Map;

public interface IExportExcel<FromClass>
{
	Map<String, String> GetExcelHead();
	void init(FromClass fromClass);
}

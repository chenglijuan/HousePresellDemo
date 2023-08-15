package zhishusz.housepresell.controller;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.service.Sm_Operate_LogAddService;

public class BaseController
{
	@Autowired
	private Sm_Operate_LogAddService operateHistoryAddService;
	
	public void writeOperateHistory(String operate, BaseForm baseForm, Properties properties, String returnJson)
	{
		operateHistoryAddService.execute(operate, baseForm, properties, returnJson);
	}
}

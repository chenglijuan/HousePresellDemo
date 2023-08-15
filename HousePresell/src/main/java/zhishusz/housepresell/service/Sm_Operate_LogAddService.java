package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.dao.Sm_Operate_LogDao;
import zhishusz.housepresell.database.po.Sm_Operate_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
	
/*
 * Service添加操作：日志-关键操作
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Operate_LogAddService
{
	@Autowired
	private Sm_Operate_LogDao sm_Operate_LogDao;
	
	public void execute(String operate, BaseForm actionForm, String result, String info, String returnJson)
	{
		if(operate != null)
		{
			Sm_Operate_Log operateHistory = new Sm_Operate_Log();
			operateHistory.setOperate(operate);
			if(actionForm != null)
			{
				operateHistory.setRemoteAddress(actionForm.getIpAddress());
				operateHistory.setStartTimeStamp(actionForm.getOperateStartDatetime());
				operateHistory.setInputForm(actionForm.toString());
				operateHistory.setUserOperate(actionForm.getUser());
			}
			operateHistory.setResult(result);
			operateHistory.setInfo(info);
			operateHistory.setReturnJson(returnJson);
			operateHistory.setEndTimeStamp(System.currentTimeMillis());
			operateHistory.setTheState(S_TheState.Normal);
//			sm_Operate_LogDao.save(operateHistory);
		}
	}
	
	public void execute(String operate, BaseForm actionForm, Properties properties, String returnJson)
	{
		String result = null;
		String info = null;
		if(properties != null)
		{
			result = (String)properties.get(S_NormalFlag.result);
			info = (String)properties.get(S_NormalFlag.info);
		}
		execute(operate, actionForm, result, info, returnJson);
	}
}

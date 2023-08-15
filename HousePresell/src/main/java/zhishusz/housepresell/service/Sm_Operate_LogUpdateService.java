package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_Operate_LogForm;
import zhishusz.housepresell.database.dao.Sm_Operate_LogDao;
import zhishusz.housepresell.database.po.Sm_Operate_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：日志-关键操作
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Operate_LogUpdateService
{
	@Autowired
	private Sm_Operate_LogDao sm_Operate_LogDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_Operate_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		Long userOperateId = model.getUserOperateId();
		String remoteAddress = model.getRemoteAddress();
		String operate = model.getOperate();
		String inputForm = model.getInputForm();
		String result = model.getResult();
		String info = model.getInfo();
		String returnJson = model.getReturnJson();
		Long startTimeStamp = model.getStartTimeStamp();
		Long endTimeStamp = model.getEndTimeStamp();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(userOperateId == null || userOperateId < 1)
		{
			return MyBackInfo.fail(properties, "'userOperate'不能为空");
		}
		if(remoteAddress == null || remoteAddress.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remoteAddress'不能为空");
		}
		if(operate == null || operate.length() == 0)
		{
			return MyBackInfo.fail(properties, "'operate'不能为空");
		}
		if(inputForm == null || inputForm.length() == 0)
		{
			return MyBackInfo.fail(properties, "'inputForm'不能为空");
		}
		if(result == null || result.length() == 0)
		{
			return MyBackInfo.fail(properties, "'result'不能为空");
		}
		if(info == null || info.length() == 0)
		{
			return MyBackInfo.fail(properties, "'info'不能为空");
		}
		if(returnJson == null || returnJson.length() == 0)
		{
			return MyBackInfo.fail(properties, "'returnJson'不能为空");
		}
		if(startTimeStamp == null || startTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'startTimeStamp'不能为空");
		}
		if(endTimeStamp == null || endTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'endTimeStamp'不能为空");
		}
		Sm_User userOperate = (Sm_User)sm_UserDao.findById(userOperateId);
		if(userOperate == null)
		{
			return MyBackInfo.fail(properties, "'userOperate(Id:" + userOperateId + ")'不存在");
		}
	
		Long sm_Operate_LogId = model.getTableId();
		Sm_Operate_Log sm_Operate_Log = (Sm_Operate_Log)sm_Operate_LogDao.findById(sm_Operate_LogId);
		if(sm_Operate_Log == null)
		{
			return MyBackInfo.fail(properties, "'Sm_Operate_Log(Id:" + sm_Operate_LogId + ")'不存在");
		}
		
		sm_Operate_Log.setTheState(theState);
		sm_Operate_Log.setUserOperate(userOperate);
		sm_Operate_Log.setRemoteAddress(remoteAddress);
		sm_Operate_Log.setOperate(operate);
		sm_Operate_Log.setInputForm(inputForm);
		sm_Operate_Log.setResult(result);
		sm_Operate_Log.setInfo(info);
		sm_Operate_Log.setReturnJson(returnJson);
		sm_Operate_Log.setStartTimeStamp(startTimeStamp);
		sm_Operate_Log.setEndTimeStamp(endTimeStamp);
	
		sm_Operate_LogDao.save(sm_Operate_Log);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Sm_BusiState_LogForm;
import zhishusz.housepresell.database.dao.Sm_BusiState_LogDao;
import zhishusz.housepresell.database.po.Sm_BusiState_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusiState_LogUpdateService
{
	@Autowired
	private Sm_BusiState_LogDao sm_BusiState_LogDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_BusiState_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		Long userOperateId = model.getUserOperateId();
		String remoteAddress = model.getRemoteAddress();
		Long operateTimeStamp = model.getOperateTimeStamp();
		Long sourceId = model.getSourceId();
		String sourceType = model.getSourceType();
		String orgObjJsonFilePath = model.getOrgObjJsonFilePath();
		String newObjJsonFilePath = model.getNewObjJsonFilePath();
		
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
		if(operateTimeStamp == null || operateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'operateTimeStamp'不能为空");
		}
		if(sourceId == null || sourceId < 1)
		{
			return MyBackInfo.fail(properties, "'sourceId'不能为空");
		}
		if(sourceType == null || sourceType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'sourceType'不能为空");
		}
		if(orgObjJsonFilePath == null || orgObjJsonFilePath.length() == 0)
		{
			return MyBackInfo.fail(properties, "'orgObjJsonFilePath'不能为空");
		}
		if(newObjJsonFilePath == null || newObjJsonFilePath.length() == 0)
		{
			return MyBackInfo.fail(properties, "'newObjJsonFilePath'不能为空");
		}
		Sm_User userOperate = (Sm_User)sm_UserDao.findById(userOperateId);
		if(userOperate == null)
		{
			return MyBackInfo.fail(properties, "'userOperate(Id:" + userOperateId + ")'不存在");
		}
	
		Long sm_BusiState_LogId = model.getTableId();
		Sm_BusiState_Log sm_BusiState_Log = (Sm_BusiState_Log)sm_BusiState_LogDao.findById(sm_BusiState_LogId);
		if(sm_BusiState_Log == null)
		{
			return MyBackInfo.fail(properties, "'Sm_BusiState_Log(Id:" + sm_BusiState_LogId + ")'不存在");
		}
		
		sm_BusiState_Log.setTheState(theState);
		sm_BusiState_Log.setUserOperate(userOperate);
		sm_BusiState_Log.setRemoteAddress(remoteAddress);
		sm_BusiState_Log.setOperateTimeStamp(operateTimeStamp);
		sm_BusiState_Log.setSourceId(sourceId);
		sm_BusiState_Log.setSourceType(sourceType);
		sm_BusiState_Log.setOrgObjJsonFilePath(orgObjJsonFilePath);
		sm_BusiState_Log.setNewObjJsonFilePath(newObjJsonFilePath);
	
		sm_BusiState_LogDao.save(sm_BusiState_Log);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

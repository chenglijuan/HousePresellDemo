package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BusiState_LogForm;
import zhishusz.housepresell.database.dao.Sm_BusiState_LogDao;
import zhishusz.housepresell.database.po.Sm_BusiState_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusiState_LogDeleteService
{
	@Autowired
	private Sm_BusiState_LogDao sm_BusiState_LogDao;

	public Properties execute(Sm_BusiState_LogForm model)
	{
		Properties properties = new MyProperties();

		Long sm_BusiState_LogId = model.getTableId();
		Sm_BusiState_Log sm_BusiState_Log = (Sm_BusiState_Log)sm_BusiState_LogDao.findById(sm_BusiState_LogId);
		if(sm_BusiState_Log == null)
		{
			return MyBackInfo.fail(properties, "'Sm_BusiState_Log(Id:" + sm_BusiState_LogId + ")'不存在");
		}
		
		sm_BusiState_Log.setTheState(S_TheState.Deleted);
		sm_BusiState_LogDao.save(sm_BusiState_Log);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

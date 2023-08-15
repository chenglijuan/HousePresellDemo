package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Operate_LogForm;
import zhishusz.housepresell.database.dao.Sm_Operate_LogDao;
import zhishusz.housepresell.database.po.Sm_Operate_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：日志-关键操作
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Operate_LogBatchDeleteService
{
	@Autowired
	private Sm_Operate_LogDao sm_Operate_LogDao;

	public Properties execute(Sm_Operate_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_Operate_Log sm_Operate_Log = (Sm_Operate_Log)sm_Operate_LogDao.findById(tableId);
			if(sm_Operate_Log == null)
			{
				return MyBackInfo.fail(properties, "'Sm_Operate_Log(Id:" + tableId + ")'不存在");
			}
		
			sm_Operate_Log.setTheState(S_TheState.Deleted);
			sm_Operate_LogDao.save(sm_Operate_Log);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

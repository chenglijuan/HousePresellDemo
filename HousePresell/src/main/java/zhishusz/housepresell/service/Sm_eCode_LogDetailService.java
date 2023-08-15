package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_eCode_LogForm;
import zhishusz.housepresell.database.dao.Sm_eCode_LogDao;
import zhishusz.housepresell.database.po.Sm_eCode_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：eCode记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_eCode_LogDetailService
{
	@Autowired
	private Sm_eCode_LogDao sm_eCode_LogDao;

	public Properties execute(Sm_eCode_LogForm model)
	{
		Properties properties = new MyProperties();

		Long sm_eCode_LogId = model.getTableId();
		Sm_eCode_Log sm_eCode_Log = (Sm_eCode_Log)sm_eCode_LogDao.findById(sm_eCode_LogId);
		if(sm_eCode_Log == null || S_TheState.Deleted.equals(sm_eCode_Log.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_eCode_Log(Id:" + sm_eCode_LogId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_eCode_Log", sm_eCode_Log);

		return properties;
	}
}

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
 * Service批量删除：eCode记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_eCode_LogBatchDeleteService
{
	@Autowired
	private Sm_eCode_LogDao sm_eCode_LogDao;

	public Properties execute(Sm_eCode_LogForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_eCode_Log sm_eCode_Log = (Sm_eCode_Log)sm_eCode_LogDao.findById(tableId);
			if(sm_eCode_Log == null)
			{
				return MyBackInfo.fail(properties, "'Sm_eCode_Log(Id:" + tableId + ")'不存在");
			}
		
			sm_eCode_Log.setTheState(S_TheState.Deleted);
			sm_eCode_LogDao.save(sm_eCode_Log);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

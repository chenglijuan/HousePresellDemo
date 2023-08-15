package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_HandleTimeLimitConfigForm;
import zhishusz.housepresell.database.dao.Tg_HandleTimeLimitConfigDao;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_HandleTimeLimitConfigDeleteService
{
	@Autowired
	private Tg_HandleTimeLimitConfigDao tg_HandleTimeLimitConfigDao;

	public Properties execute(Tg_HandleTimeLimitConfigForm model)
	{
		Properties properties = new MyProperties();

		Long tg_HandleTimeLimitConfigId = model.getTableId();
		Tg_HandleTimeLimitConfig tg_HandleTimeLimitConfig = (Tg_HandleTimeLimitConfig)tg_HandleTimeLimitConfigDao.findById(tg_HandleTimeLimitConfigId);
		if(tg_HandleTimeLimitConfig == null)
		{
			return MyBackInfo.fail(properties, "'Tg_HandleTimeLimitConfig(Id:" + tg_HandleTimeLimitConfigId + ")'不存在");
		}
		
		tg_HandleTimeLimitConfig.setTheState(S_TheState.Deleted);
		tg_HandleTimeLimitConfigDao.save(tg_HandleTimeLimitConfig);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

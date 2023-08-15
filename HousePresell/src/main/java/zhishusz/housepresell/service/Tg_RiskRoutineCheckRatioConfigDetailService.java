package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：风控例行抽查比例配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckRatioConfigDetailService
{
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;

	public Properties execute(Tg_RiskRoutineCheckRatioConfigForm model)
	{
		Properties properties = new MyProperties();

		Long tg_RiskRoutineCheckRatioConfigId = model.getTableId();
		Tg_RiskRoutineCheckRatioConfig tg_RiskRoutineCheckRatioConfig = (Tg_RiskRoutineCheckRatioConfig)tg_RiskRoutineCheckRatioConfigDao.findById(tg_RiskRoutineCheckRatioConfigId);
		if(tg_RiskRoutineCheckRatioConfig == null || S_TheState.Deleted.equals(tg_RiskRoutineCheckRatioConfig.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tg_RiskRoutineCheckRatioConfig(Id:" + tg_RiskRoutineCheckRatioConfigId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_RiskRoutineCheckRatioConfig", tg_RiskRoutineCheckRatioConfig);

		return properties;
	}
}

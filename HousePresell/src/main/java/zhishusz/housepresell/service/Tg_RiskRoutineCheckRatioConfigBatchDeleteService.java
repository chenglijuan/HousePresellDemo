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
 * Service批量删除：风控例行抽查比例配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckRatioConfigBatchDeleteService
{
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;

	public Properties execute(Tg_RiskRoutineCheckRatioConfigForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_RiskRoutineCheckRatioConfig tg_RiskRoutineCheckRatioConfig = (Tg_RiskRoutineCheckRatioConfig)tg_RiskRoutineCheckRatioConfigDao.findById(tableId);
			if(tg_RiskRoutineCheckRatioConfig == null)
			{
				return MyBackInfo.fail(properties, "'Tg_RiskRoutineCheckRatioConfig(Id:" + tableId + ")'不存在");
			}
		
			tg_RiskRoutineCheckRatioConfig.setTheState(S_TheState.Deleted);
			tg_RiskRoutineCheckRatioConfigDao.save(tg_RiskRoutineCheckRatioConfig);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

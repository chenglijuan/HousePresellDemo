package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoDeleteService
{
	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;

	public Properties execute(Tg_RiskRoutineCheckInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tg_RiskRoutineCheckInfoId = model.getTableId();
		Tg_RiskRoutineCheckInfo tg_RiskRoutineCheckInfo = (Tg_RiskRoutineCheckInfo)tg_RiskRoutineCheckInfoDao.findById(tg_RiskRoutineCheckInfoId);
		if(tg_RiskRoutineCheckInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tg_RiskRoutineCheckInfo(Id:" + tg_RiskRoutineCheckInfoId + ")'不存在");
		}
		
		tg_RiskRoutineCheckInfo.setTheState(S_TheState.Deleted);
		tg_RiskRoutineCheckInfoDao.save(tg_RiskRoutineCheckInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

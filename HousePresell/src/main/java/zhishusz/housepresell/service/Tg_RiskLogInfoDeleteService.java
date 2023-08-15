package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_RiskLogInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskLogInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskLogInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：风险日志管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskLogInfoDeleteService
{
	@Autowired
	private Tg_RiskLogInfoDao tg_RiskLogInfoDao;

	public Properties execute(Tg_RiskLogInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tg_RiskLogInfoId = model.getTableId();
		Tg_RiskLogInfo tg_RiskLogInfo = (Tg_RiskLogInfo)tg_RiskLogInfoDao.findById(tg_RiskLogInfoId);
		if(tg_RiskLogInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tg_RiskLogInfo(Id:" + tg_RiskLogInfoId + ")'不存在");
		}
		
		tg_RiskLogInfo.setTheState(S_TheState.Deleted);
		tg_RiskLogInfoDao.save(tg_RiskLogInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

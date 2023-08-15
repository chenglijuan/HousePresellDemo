package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightLogDetailService
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;

	public Properties execute(Tgpf_BuildingRemainRightLogForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_BuildingRemainRightLogId = model.getTableId();
		Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = (Tgpf_BuildingRemainRightLog)tgpf_BuildingRemainRightLogDao.findById(tgpf_BuildingRemainRightLogId);
		if(tgpf_BuildingRemainRightLog == null || S_TheState.Deleted.equals(tgpf_BuildingRemainRightLog.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_BuildingRemainRightLog(Id:" + tgpf_BuildingRemainRightLogId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_BuildingRemainRightLog", tgpf_BuildingRemainRightLog);

		return properties;
	}
}

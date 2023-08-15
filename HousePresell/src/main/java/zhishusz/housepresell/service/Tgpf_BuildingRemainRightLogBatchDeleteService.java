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
 * Service批量删除：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightLogBatchDeleteService
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;

	public Properties execute(Tgpf_BuildingRemainRightLogForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = (Tgpf_BuildingRemainRightLog)tgpf_BuildingRemainRightLogDao.findById(tableId);
			if(tgpf_BuildingRemainRightLog == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_BuildingRemainRightLog(Id:" + tableId + ")'不存在");
			}
		
			tgpf_BuildingRemainRightLog.setTheState(S_TheState.Deleted);
			tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

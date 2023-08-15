package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompletedBatchDeleteService
{
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;

	public Properties execute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_BldEscrowCompleted empj_BldEscrowCompleted = (Empj_BldEscrowCompleted)empj_BldEscrowCompletedDao.findById(tableId);
			if(empj_BldEscrowCompleted == null)
			{
				return MyBackInfo.fail(properties, "'Empj_BldEscrowCompleted(Id:" + tableId + ")'不存在");
			}
		
			empj_BldEscrowCompleted.setTheState(S_TheState.Deleted);
			empj_BldEscrowCompletedDao.save(empj_BldEscrowCompleted);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

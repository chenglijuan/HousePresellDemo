package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskLetterReceiverBatchDeleteService
{
	@Autowired
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;

	public Properties execute(Tg_PjRiskLetterReceiverForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_PjRiskLetterReceiver tg_PjRiskLetterReceiver = (Tg_PjRiskLetterReceiver)tg_PjRiskLetterReceiverDao.findById(tableId);
			if(tg_PjRiskLetterReceiver == null)
			{
				return MyBackInfo.fail(properties, "被删除的用户信息不存在");
			}
		
			tg_PjRiskLetterReceiver.setTheState(S_TheState.Deleted);
			tg_PjRiskLetterReceiverDao.save(tg_PjRiskLetterReceiver);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

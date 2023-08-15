package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：项目风险评级
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskRatingBatchDeleteService
{
	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;

	public Properties execute(Tg_PjRiskRatingForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_PjRiskRating tg_PjRiskRating = (Tg_PjRiskRating)tg_PjRiskRatingDao.findById(tableId);
			if(tg_PjRiskRating == null)
			{
				return MyBackInfo.fail(properties, "'Tg_PjRiskRating(Id:" + tableId + ")'不存在");
			}
		
			tg_PjRiskRating.setTheState(S_TheState.Deleted);
			tg_PjRiskRatingDao.save(tg_PjRiskRating);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

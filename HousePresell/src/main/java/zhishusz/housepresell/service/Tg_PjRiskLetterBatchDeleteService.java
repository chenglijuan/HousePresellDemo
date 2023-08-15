package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
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
public class Tg_PjRiskLetterBatchDeleteService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;

	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_PjRiskLetter tg_PjRiskLetter = (Tg_PjRiskLetter)tg_PjRiskLetterDao.findById(tableId);
			if(tg_PjRiskLetter == null)
			{
				return MyBackInfo.fail(properties, "'Tg_PjRiskLetter(Id:" + tableId + ")'不存在");
			}
		
			tg_PjRiskLetter.setTheState(S_TheState.Deleted);
			tg_PjRiskLetterDao.save(tg_PjRiskLetter);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

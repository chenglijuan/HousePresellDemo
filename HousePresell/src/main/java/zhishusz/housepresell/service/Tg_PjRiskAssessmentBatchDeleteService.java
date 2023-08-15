package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskAssessmentDao;
import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：项目风险评估
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskAssessmentBatchDeleteService
{
	@Autowired
	private Tg_PjRiskAssessmentDao tg_PjRiskAssessmentDao;

	public Properties execute(Tg_PjRiskAssessmentForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_PjRiskAssessment tg_PjRiskAssessment = (Tg_PjRiskAssessment)tg_PjRiskAssessmentDao.findById(tableId);
			if(tg_PjRiskAssessment == null)
			{
				return MyBackInfo.fail(properties, "'Tg_PjRiskAssessment(Id:" + tableId + ")'不存在");
			}
		
			tg_PjRiskAssessment.setTheState(S_TheState.Deleted);
			tg_PjRiskAssessmentDao.save(tg_PjRiskAssessment);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

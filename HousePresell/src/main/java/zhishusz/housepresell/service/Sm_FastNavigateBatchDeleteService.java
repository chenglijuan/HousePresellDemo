package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_FastNavigateBatchDeleteService
{
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;

	public Properties execute(Sm_FastNavigateForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_FastNavigate sm_FastNavigate = (Sm_FastNavigate)sm_FastNavigateDao.findById(tableId);
			if(sm_FastNavigate == null)
			{
				return MyBackInfo.fail(properties, "'Sm_FastNavigate(Id:" + tableId + ")'不存在");
			}
		
			sm_FastNavigate.setTheState(S_TheState.Deleted);
			sm_FastNavigateDao.save(sm_FastNavigate);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

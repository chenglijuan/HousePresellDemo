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
 * Service单个删除：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_FastNavigateDeleteService
{
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;

	public Properties execute(Sm_FastNavigateForm model)
	{
		Properties properties = new MyProperties();

		Long sm_FastNavigateId = model.getTableId();
		Sm_FastNavigate sm_FastNavigate = (Sm_FastNavigate)sm_FastNavigateDao.findById(sm_FastNavigateId);
		if(sm_FastNavigate == null)
		{
			return MyBackInfo.fail(properties, "'Sm_FastNavigate(Id:" + sm_FastNavigateId + ")'不存在");
		}
		
		sm_FastNavigate.setTheState(S_TheState.Deleted);
		sm_FastNavigateDao.save(sm_FastNavigate);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

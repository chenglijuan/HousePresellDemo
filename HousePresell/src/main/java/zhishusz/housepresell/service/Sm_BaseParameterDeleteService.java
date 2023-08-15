package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：参数定义
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BaseParameterDeleteService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	public Properties execute(Sm_BaseParameterForm model)
	{
		Properties properties = new MyProperties();

		Long sm_BaseParameterId = model.getTableId();
		Sm_BaseParameter sm_BaseParameter = (Sm_BaseParameter)sm_BaseParameterDao.findById(sm_BaseParameterId);
		if(sm_BaseParameter == null)
		{
			return MyBackInfo.fail(properties, "'Sm_BaseParameter(Id:" + sm_BaseParameterId + ")'不存在");
		}
		
		sm_BaseParameter.setTheState(S_TheState.Deleted);
		sm_BaseParameterDao.save(sm_BaseParameter);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

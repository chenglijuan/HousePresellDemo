package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：参数定义
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_BaseParameterListService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_BaseParameterForm model)
	{
		Properties properties = new MyProperties();

		List<Sm_BaseParameter> sm_BaseParameterList = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), model));;

		properties.put("sm_BaseParameterList", sm_BaseParameterList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

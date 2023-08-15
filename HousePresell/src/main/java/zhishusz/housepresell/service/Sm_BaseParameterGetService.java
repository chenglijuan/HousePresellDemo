package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：获取常量
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_BaseParameterGetService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	public Properties execute(Sm_BaseParameterForm model)
	{
		Properties properties = new MyProperties();
		String theValue = model.getTheValue();//常量值
		String parametertype = model.getParametertype();//常量类型
		if(parametertype == null || parametertype.length() == 0)
		{
			return MyBackInfo.fail(properties, "请上传常量类型");
		}
		if(theValue == null || theValue.length() == 0)
		{
			return MyBackInfo.fail(properties, "请上传常量值");
		}
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterDao.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), model));
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_BaseParameter", sm_BaseParameter);
		
		return properties;
	}
	
	public Sm_BaseParameter getParameter(String parametertype, String theValue)
	{
		if(parametertype == null || parametertype.length() == 0 || theValue == null || theValue.length() == 0)return null;
		
		Sm_BaseParameterForm sm_BaseParameterForm = new Sm_BaseParameterForm();
		sm_BaseParameterForm.setParametertype(parametertype);
		sm_BaseParameterForm.setTheValue(theValue);
		sm_BaseParameterForm.setTheState(S_TheState.Normal);
		
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterDao.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
		
		return sm_BaseParameter;
	}
}

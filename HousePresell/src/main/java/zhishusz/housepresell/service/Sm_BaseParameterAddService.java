package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
	
/*
 * Service添加操作：参数定义
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BaseParameterAddService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_BaseParameterForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long parentParameterId = model.getParentParameterId();
		String theName = model.getTheName();
		String theValue = model.getTheValue();
		Long validDateFrom = model.getValidDateFrom();
		Long validDateTo = model.getValidDateTo();
		Integer theVersion = model.getTheVersion();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(parentParameterId == null || parentParameterId < 1)
		{
			return MyBackInfo.fail(properties, "'parentParameter'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theName'不能为空");
		}
		if(theValue == null || theValue.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theValue'不能为空");
		}
		if(validDateFrom == null || validDateFrom < 1)
		{
			return MyBackInfo.fail(properties, "'validDateFrom'不能为空");
		}
		if(validDateTo == null || validDateTo < 1)
		{
			return MyBackInfo.fail(properties, "'validDateTo'不能为空");
		}
		if(theVersion == null || theVersion < 1)
		{
			return MyBackInfo.fail(properties, "'theVersion'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_BaseParameter parentParameter = (Sm_BaseParameter)sm_BaseParameterDao.findById(parentParameterId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(parentParameter == null)
		{
			return MyBackInfo.fail(properties, "'parentParameter'不能为空");
		}
	
		Sm_BaseParameter sm_BaseParameter = new Sm_BaseParameter();
		sm_BaseParameter.setTheState(theState);
		sm_BaseParameter.setUserStart(userStart);
		sm_BaseParameter.setCreateTimeStamp(createTimeStamp);
		sm_BaseParameter.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		sm_BaseParameter.setParentParameter(parentParameter);
		sm_BaseParameter.setTheName(theName);
		sm_BaseParameter.setTheValue(theValue);
		sm_BaseParameter.setValidDateFrom(validDateFrom);
		sm_BaseParameter.setValidDateTo(validDateTo);
		sm_BaseParameter.setTheVersion(theVersion);
		sm_BaseParameterDao.save(sm_BaseParameter);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_FastNavigateAddService
{
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_FastNavigateForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userTableId = model.getUserTableId();
		Long menuTableId = model.getMenuTableId();
		String theNameOfMenu = model.getTheNameOfMenu();
		String theLinkOfMenu = model.getTheLinkOfMenu();
		Integer orderNumber = model.getOrderNumber();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userTableId == null || userTableId < 1)
		{
			return MyBackInfo.fail(properties, "'userTableId'不能为空");
		}
		if(menuTableId == null || menuTableId < 1)
		{
			return MyBackInfo.fail(properties, "'menuTableId'不能为空");
		}
		if(theNameOfMenu == null || theNameOfMenu.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfMenu'不能为空");
		}
		if(theLinkOfMenu == null || theLinkOfMenu.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theLinkOfMenu'不能为空");
		}
		if(orderNumber == null || orderNumber < 1)
		{
			return MyBackInfo.fail(properties, "'orderNumber'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
		}
	
		Sm_FastNavigate sm_FastNavigate = new Sm_FastNavigate();
		sm_FastNavigate.setTheState(theState);
		sm_FastNavigate.setBusiState(busiState);
		sm_FastNavigate.seteCode(eCode);
		sm_FastNavigate.setUserStart(userStart);
		sm_FastNavigate.setCreateTimeStamp(createTimeStamp);
		sm_FastNavigate.setUserUpdate(userUpdate);
		sm_FastNavigate.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		sm_FastNavigate.setUserTableId(userTableId);
		sm_FastNavigate.setMenuTableId(menuTableId);
		sm_FastNavigate.setTheNameOfMenu(theNameOfMenu);
		sm_FastNavigate.setTheLinkOfMenu(theLinkOfMenu);
		sm_FastNavigate.setOrderNumber(orderNumber);
		sm_FastNavigateDao.save(sm_FastNavigate);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package test.initDatabase;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmpj_BuildingExtendInfo extends BaseJunitTest 
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	@Test
	public void execute() throws Exception 
	{
		Sm_User userLogin = (Sm_User)sm_UserDao.findById(12365L);
		
		//初始化楼幢扩展信息
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setTheState(S_TheState.Normal);
		List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
		for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
		{
			if(empj_BuildingInfo.getExtendInfo() == null)
			{
				Empj_BuildingExtendInfo empj_BuildingExtendInfo = new Empj_BuildingExtendInfo();
				empj_BuildingExtendInfo.setTheState(S_TheState.Normal);
				empj_BuildingExtendInfo.setUserStart(userLogin);
				empj_BuildingExtendInfo.setUserUpdate(userLogin);
				empj_BuildingExtendInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
				empj_BuildingExtendInfo.setCreateTimeStamp(System.currentTimeMillis());
				
				empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);
				
				empj_BuildingInfo.setExtendInfo(empj_BuildingExtendInfo);
				
				empj_BuildingInfoDao.save(empj_BuildingInfo);
			}
		}
	}
}

package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_ProjectInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：申请表-项目信息变更(审批)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_ProjectInfo_AFUpdateService
{
	@Autowired
	private Empj_ProjectInfo_AFDao empj_ProjectInfo_AFDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Empj_ProjectInfo_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long userUpdateId = model.getUserUpdateId();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
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
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate(Id:" + userUpdate + ")'不存在");
		}
	
		Long empj_ProjectInfo_AFId = model.getTableId();
		Empj_ProjectInfo_AF empj_ProjectInfo_AF = (Empj_ProjectInfo_AF)empj_ProjectInfo_AFDao.findById(empj_ProjectInfo_AFId);
		if(empj_ProjectInfo_AF == null)
		{
			return MyBackInfo.fail(properties, "'Empj_ProjectInfo_AF(Id:" + empj_ProjectInfo_AFId + ")'不存在");
		}
		
		empj_ProjectInfo_AF.setTheState(theState);
		empj_ProjectInfo_AF.setBusiState(busiState);
		empj_ProjectInfo_AF.seteCode(eCode);
		empj_ProjectInfo_AF.setUserStart(userStart);
		empj_ProjectInfo_AF.setCreateTimeStamp(createTimeStamp);
		empj_ProjectInfo_AF.setUserUpdate(userUpdate);
		empj_ProjectInfo_AF.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		empj_ProjectInfo_AF.setUserRecord(userRecord);
		empj_ProjectInfo_AF.setRecordTimeStamp(recordTimeStamp);
	
		empj_ProjectInfo_AFDao.save(empj_ProjectInfo_AF);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

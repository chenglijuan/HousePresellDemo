package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightLogAddService
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	
	public Properties execute(Tgpf_BuildingRemainRightLogForm model)
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
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		String eCodeOfProject = model.geteCodeOfProject();
		Long buildingId = model.getBuildingId();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		Long buildingAccountId = model.getBuildingAccountId();
		Long buildingExtendInfoId = model.getBuildingExtendInfoId();
		String currentFigureProgress = model.getCurrentFigureProgress();
		Double currentLimitedRatio = model.getCurrentLimitedRatio();
		Double nodeLimitedAmount = model.getNodeLimitedAmount();
		Double totalAccountAmount = model.getTotalAccountAmount();
		String billTimeStamp = model.getBillTimeStamp();
		
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
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfProject'不能为空");
		}
		if(eCodeOfProject == null || eCodeOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfProject'不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromConstruction'不能为空");
		}
		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeFromPublicSecurity'不能为空");
		}
		if(buildingAccountId == null || buildingAccountId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingAccount'不能为空");
		}
		if(buildingExtendInfoId == null || buildingExtendInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingExtendInfo'不能为空");
		}
		if(currentFigureProgress == null || currentFigureProgress.length() == 0)
		{
			return MyBackInfo.fail(properties, "'currentFigureProgress'不能为空");
		}
		if(currentLimitedRatio == null || currentLimitedRatio < 1)
		{
			return MyBackInfo.fail(properties, "'currentLimitedRatio'不能为空");
		}
		if(nodeLimitedAmount == null || nodeLimitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "'nodeLimitedAmount'不能为空");
		}
		if(totalAccountAmount == null || totalAccountAmount < 1)
		{
			return MyBackInfo.fail(properties, "'totalAccountAmount'不能为空");
		}
		if(billTimeStamp == null || billTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'billTimeStamp'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		Tgpj_BuildingAccount buildingAccount = (Tgpj_BuildingAccount)tgpj_BuildingAccountDao.findById(buildingAccountId);
		Empj_BuildingExtendInfo buildingExtendInfo = (Empj_BuildingExtendInfo)empj_BuildingExtendInfoDao.findById(buildingExtendInfoId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(project == null)
		{
			return MyBackInfo.fail(properties, "'project'不能为空");
		}
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(buildingAccount == null)
		{
			return MyBackInfo.fail(properties, "'buildingAccount'不能为空");
		}
		if(buildingExtendInfo == null)
		{
			return MyBackInfo.fail(properties, "'buildingExtendInfo'不能为空");
		}
	
		Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = new Tgpf_BuildingRemainRightLog();
		tgpf_BuildingRemainRightLog.setTheState(theState);
		tgpf_BuildingRemainRightLog.setBusiState(busiState);
		tgpf_BuildingRemainRightLog.seteCode(eCode);
		tgpf_BuildingRemainRightLog.setUserStart(userStart);
		tgpf_BuildingRemainRightLog.setCreateTimeStamp(createTimeStamp);
		tgpf_BuildingRemainRightLog.setUserUpdate(userUpdate);
		tgpf_BuildingRemainRightLog.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_BuildingRemainRightLog.setUserRecord(userRecord);
		tgpf_BuildingRemainRightLog.setRecordTimeStamp(recordTimeStamp);
		tgpf_BuildingRemainRightLog.setProject(project);
		tgpf_BuildingRemainRightLog.setTheNameOfProject(theNameOfProject);
		tgpf_BuildingRemainRightLog.seteCodeOfProject(eCodeOfProject);
		tgpf_BuildingRemainRightLog.setBuilding(building);
		tgpf_BuildingRemainRightLog.seteCodeFromConstruction(eCodeFromConstruction);
		tgpf_BuildingRemainRightLog.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		tgpf_BuildingRemainRightLog.setBuildingAccount(buildingAccount);
		tgpf_BuildingRemainRightLog.setBuildingExtendInfo(buildingExtendInfo);
		tgpf_BuildingRemainRightLog.setCurrentFigureProgress(currentFigureProgress);
		tgpf_BuildingRemainRightLog.setCurrentLimitedRatio(currentLimitedRatio);
		tgpf_BuildingRemainRightLog.setNodeLimitedAmount(nodeLimitedAmount);
		tgpf_BuildingRemainRightLog.setTotalAccountAmount(totalAccountAmount);
		tgpf_BuildingRemainRightLog.setBillTimeStamp(billTimeStamp);
		tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundProjectInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_FundProjectInfoDao;
import zhishusz.housepresell.database.po.Tgpf_FundProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
	
/*
 * Service添加操作：推送给财务系统-拨付凭证-项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundProjectInfoAddService
{
	@Autowired
	private Tgpf_FundProjectInfoDao tgpf_FundProjectInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Tgpf_FundProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		Long buildingId = model.getBuildingId();
		String eCodeOfBuilding = model.geteCodeOfBuilding();
		Double payoutAmount = model.getPayoutAmount();
		
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
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
		if(eCodeOfBuilding == null || eCodeOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfBuilding'不能为空");
		}
		if(payoutAmount == null || payoutAmount < 1)
		{
			return MyBackInfo.fail(properties, "'payoutAmount'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}
	
		Tgpf_FundProjectInfo tgpf_FundProjectInfo = new Tgpf_FundProjectInfo();
		tgpf_FundProjectInfo.setTheState(theState);
		tgpf_FundProjectInfo.setBusiState(busiState);
		tgpf_FundProjectInfo.seteCode(eCode);
		tgpf_FundProjectInfo.setUserStart(userStart);
		tgpf_FundProjectInfo.setCreateTimeStamp(createTimeStamp);
		tgpf_FundProjectInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_FundProjectInfo.setUserRecord(userRecord);
		tgpf_FundProjectInfo.setRecordTimeStamp(recordTimeStamp);
		tgpf_FundProjectInfo.setBuilding(building);
		tgpf_FundProjectInfo.seteCodeOfBuilding(eCodeOfBuilding);
		tgpf_FundProjectInfo.setPayoutAmount(payoutAmount);
		tgpf_FundProjectInfoDao.save(tgpf_FundProjectInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

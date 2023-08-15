package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.database.dao.Tgpf_OverallPlanAccoutDao;
import zhishusz.housepresell.database.po.Tgpf_OverallPlanAccout;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
	
/*
 * Service添加操作：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_OverallPlanAccoutAddService
{
	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_OverallPlanAccoutDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	
	public Properties execute(Tgpf_OverallPlanAccoutForm model)
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
		Long fundOverallPlanId = model.getFundOverallPlanId();
		String eCodeOfFundOverallPlan = model.geteCodeOfFundOverallPlan();
		Long bankAccountEscrowedId = model.getBankAccountEscrowedId();
		Double overallPlanAmount = model.getOverallPlanAmount();
		
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
		if(fundOverallPlanId == null || fundOverallPlanId < 1)
		{
			return MyBackInfo.fail(properties, "'fundOverallPlan'不能为空");
		}
		if(eCodeOfFundOverallPlan == null || eCodeOfFundOverallPlan.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfFundOverallPlan'不能为空");
		}
		if(bankAccountEscrowedId == null || bankAccountEscrowedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
		if(overallPlanAmount == null || overallPlanAmount < 1)
		{
			return MyBackInfo.fail(properties, "'overallPlanAmount'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Tgpf_FundOverallPlan fundOverallPlan = (Tgpf_FundOverallPlan)tgpf_FundOverallPlanDao.findById(fundOverallPlanId);
		Tgxy_BankAccountEscrowed bankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(bankAccountEscrowedId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(fundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "'fundOverallPlan'不能为空");
		}
		if(bankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountEscrowed'不能为空");
		}
	
		Tgpf_OverallPlanAccout tgpf_OverallPlanAccout = new Tgpf_OverallPlanAccout();
		tgpf_OverallPlanAccout.setTheState(theState);
		tgpf_OverallPlanAccout.setBusiState(busiState);
		tgpf_OverallPlanAccout.seteCode(eCode);
		tgpf_OverallPlanAccout.setUserStart(userStart);
		tgpf_OverallPlanAccout.setCreateTimeStamp(createTimeStamp);
		tgpf_OverallPlanAccout.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_OverallPlanAccout.setUserRecord(userRecord);
		tgpf_OverallPlanAccout.setRecordTimeStamp(recordTimeStamp);
		tgpf_OverallPlanAccout.setFundOverallPlan(fundOverallPlan);
		tgpf_OverallPlanAccout.seteCodeOfFundOverallPlan(eCodeOfFundOverallPlan);
		tgpf_OverallPlanAccout.setBankAccountEscrowed(bankAccountEscrowed);
		tgpf_OverallPlanAccout.setOverallPlanAmount(overallPlanAmount);
		tgpf_OverallPlanAccoutDao.save(tgpf_OverallPlanAccout);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

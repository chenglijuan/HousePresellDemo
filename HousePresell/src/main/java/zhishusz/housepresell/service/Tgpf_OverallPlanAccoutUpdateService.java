package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Service更新操作：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_OverallPlanAccoutUpdateService
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

		Long fundOverallPlanId = model.getFundOverallPlanId();
		if(fundOverallPlanId == null || fundOverallPlanId < 1 )
		{
			return MyBackInfo.fail(properties, "'fundOverallPlanId'不能为空");
		}
		Tgpf_FundOverallPlan fundOverallPlan = (Tgpf_FundOverallPlan)tgpf_FundOverallPlanDao.findById(fundOverallPlanId);
		if(fundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "'fundOverallPlan(Id:" + fundOverallPlanId + ")'不存在");
		}

		for(Tgpf_OverallPlanAccoutForm tgpf_overallPlanAccoutForm : model.getTgpf_OverallPlanAccount())
		{
			if(tgpf_overallPlanAccoutForm == null)
			{
				return MyBackInfo.fail(properties, "'统筹账户状况信息'不能为空");
			}

			Double overallPlanAmount = tgpf_overallPlanAccoutForm.getOverallPlanAmount();
			Long tgpf_OverallPlanAccoutId = tgpf_overallPlanAccoutForm.getTableId();
			Tgpf_OverallPlanAccout tgpf_OverallPlanAccout = (Tgpf_OverallPlanAccout)tgpf_OverallPlanAccoutDao.findById(tgpf_OverallPlanAccoutId);
			if(tgpf_OverallPlanAccout == null)
			{
				return MyBackInfo.fail(properties, "'统筹账户状况信息(Id:" + tgpf_OverallPlanAccoutId + ")'不存在");
			}

			tgpf_OverallPlanAccout.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgpf_OverallPlanAccout.setOverallPlanAmount(overallPlanAmount);
			tgpf_OverallPlanAccoutDao.save(tgpf_OverallPlanAccout);
		}

		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

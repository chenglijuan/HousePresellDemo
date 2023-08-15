package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanRecordForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/*
 * Service添加操作：资金统筹
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundOverallPlanRecordAddService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;
	@Autowired
	private  Tgxy_BankAccountEscrowedDao tgxy_bankAccountEscrowedDao;
	@Autowired
	private  Tgpj_BankAccountSupervisedDao tgpj_bankAccountSupervisedDao;
	@Autowired
	private  Tgpf_FundOverallPlanRecordDao tgpf_fundOverallPlanRecordDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgpf_FundOverallPlanRecordForm model)
	{
		Properties properties = new MyProperties();

		Long fundOverallPlanId = model.getFundOverallPlanId();//资金统筹id
		Tgpf_FundOverallPlan tgpf_fundOverallPlan = tgpf_FundOverallPlanDao.findById(fundOverallPlanId);
		if(tgpf_fundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "'资金统筹'不能为空");
		}

		List<Tgpf_FundOverallPlanRecord> tgpf_fundOverallPlanRecordList = new ArrayList<>();
		for (Tgpf_FundOverallPlanRecordForm tgpf_fundOverallPlanRecordForm : model.getTgpf_FundOverallPlanRecord())
		{
			Double payoutAmount = tgpf_fundOverallPlanRecordForm.getPayoutAmount();
			if(tgpf_fundOverallPlanRecordForm == null)
			{
				return MyBackInfo.fail(properties, "'用款计划'不能为空");
			}
			Long escrowedBankAccountId = tgpf_fundOverallPlanRecordForm.getEscroweBankAccountdId();

			Tgxy_BankAccountEscrowed tgxy_bankAccountEscrowed = tgxy_bankAccountEscrowedDao.findById(escrowedBankAccountId);
			if(tgxy_bankAccountEscrowed == null)
			{
				return MyBackInfo.fail(properties, "'托管账户'不能为空");
			}

			Long supervisedBankAccountId = tgpf_fundOverallPlanRecordForm.getSupervisedBankAccountId();
			Tgpj_BankAccountSupervised tgpj_bankAccountSupervised = tgpj_bankAccountSupervisedDao.findById(supervisedBankAccountId);
			if(tgxy_bankAccountEscrowed == null)
			{
				return MyBackInfo.fail(properties, "'监管账户'不能为空");
			}

 			//打款记录（托管账号 -> 监管账号）
			Tgpf_FundOverallPlanRecord tgpf_fundOverallPlanRecord = new Tgpf_FundOverallPlanRecord();
			tgpf_fundOverallPlanRecord.setTheState(S_TheState.Normal);
			tgpf_fundOverallPlanRecord.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_fundOverallPlanRecord.setFundOverallPlan(tgpf_fundOverallPlan);//资金统筹
			tgpf_fundOverallPlanRecord.setBankAccountEscrowed(tgxy_bankAccountEscrowed); //托管账户
			tgpf_fundOverallPlanRecord.setBankAccountSupervised(tgpj_bankAccountSupervised);//监管账户
			tgpf_fundOverallPlanRecord.setPayoutAmount(payoutAmount);

			tgpf_fundOverallPlanRecordList.add(tgpf_fundOverallPlanRecord);
		}
		//tgpf_fundOverallPlan.setFundOverallPlanRecordList(tgpf_fundOverallPlanRecordList);
		tgpf_FundOverallPlanDao.save(tgpf_fundOverallPlan);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

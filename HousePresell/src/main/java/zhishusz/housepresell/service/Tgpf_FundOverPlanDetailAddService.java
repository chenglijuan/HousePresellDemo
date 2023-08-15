package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_FundOverPlanDetaillForm;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Properties;

/*
 * Service详情：用款申请汇总
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundOverPlanDetailAddService
{
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_bankAccountSupervisedDao;

	public Properties execute(Tgpf_FundOverPlanDetaillForm model)
	{
		Properties properties = new MyProperties();

		String theNameOfProject = model.getTheNameOfProject();//项目名称
		Double appliedAmount = model.getAppliedAmount();//本次划款申请金额
		Long bankAccountSupervisedId  = model.getTableId(); //监管账户id

		if (theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "'项目名称'不能为空");
		}
		if(appliedAmount == null || appliedAmount < 0.00)
		{
			return MyBackInfo.fail(properties, "'本次划拨申请金额'不能为空");
		}
		if(bankAccountSupervisedId == null || bankAccountSupervisedId <1)
		{
			return MyBackInfo.fail(properties, "'监管账户'不能为空");
		}

		Tgpj_BankAccountSupervised tgpj_bankAccountSupervised = tgpj_bankAccountSupervisedDao.findById(bankAccountSupervisedId);

		if(tgpj_bankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'监管账户'不能为空");
		}

		/*if(tgpj_bankAccountSupervised.getBankBranch() == null)
		{
			return MyBackInfo.fail(properties, "'开户行'不能为空");
		}

		Emmp_BankBranch emmp_bankBranch = tgpj_bankAccountSupervised.getBankBranch();*/

		String theNameOfBankBranch = tgpj_bankAccountSupervised.getTheNameOfBank(); //开户行名称
		String theNameOfAccount = tgpj_bankAccountSupervised.getTheName()  ;//监管账户名称
		String supervisedBankAccount = tgpj_bankAccountSupervised.getTheAccount();//预售资金监管账号


		if (theNameOfBankBranch == null || theNameOfBankBranch.length() == 0)
		{
			return MyBackInfo.fail(properties, "'开户行名称'不能为空");
		}
		if (theNameOfAccount == null || theNameOfAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'监管账户名称'不能为空");
		}
		if (supervisedBankAccount == null || supervisedBankAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'预售资金监管账号'不能为空");
		}

		Tgpf_FundOverallPlanDetail tgpf_fundOverallPlanDetail = new Tgpf_FundOverallPlanDetail();
		tgpf_fundOverallPlanDetail.setTheState(S_TheState.Normal);
		tgpf_fundOverallPlanDetail.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_fundOverallPlanDetail.setTheNameOfProject(theNameOfProject);
		tgpf_fundOverallPlanDetail.setTheNameOfAccount(theNameOfAccount);
		tgpf_fundOverallPlanDetail.setBankAccountSupervised(tgpj_bankAccountSupervised);
		tgpf_fundOverallPlanDetail.setSupervisedBankAccount(supervisedBankAccount);
		tgpf_fundOverallPlanDetail.setTheNameOfBankBranch(theNameOfBankBranch);
		tgpf_fundOverallPlanDetail.setAppliedAmount(appliedAmount);

		properties.put("tgpf_FundOverallPlanDetail", tgpf_fundOverallPlanDetail);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：资金拨付
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriatedAddService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;//网银上传主表
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;//网银上传子表
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAppropriatedForm model,Sm_User loginUser)
	{
		Properties properties = new MyProperties();

			Long fundAppropriated_AFId = model.getFundAppropriated_AFId(); // 用款申请主表id
			Long bankAccountEscrowedId = model.getBankAccountEscrowedId(); // 托管账户id
			Long bankAccountSupervisedId = model.getBankAccountSupervisedId();//监管账户id
			Double overallPlanPayoutAmount = model.getOverallPlanPayoutAmount(); //统筹拨付金额
			Double canPayAmount = model.getCanPayAmount();
			Integer colorState = model.getColorState();

			if(fundAppropriated_AFId == null || fundAppropriated_AFId < 1)
			{
				return MyBackInfo.fail(properties, "'用款申请主表信息'不能为空");
			}
			if(bankAccountEscrowedId == null || bankAccountEscrowedId < 1)
			{
				return MyBackInfo.fail(properties, "'托管账户'不能为空");
			}
			if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
			{
				return MyBackInfo.fail(properties, "'监管账户'不能为空");
			}
			if(overallPlanPayoutAmount == null)
			{
				overallPlanPayoutAmount = 0.0;
			}
			else if(overallPlanPayoutAmount < 0)
			{
				return MyBackInfo.fail(properties, "'统筹拨付金额'不能小于0");
			}
	
			
			Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af =  tgpf_FundAppropriated_AFDao.findById(fundAppropriated_AFId);
			Tgxy_BankAccountEscrowed tgxy_bankAccountEscrowed = tgxy_BankAccountEscrowedDao.findById(bankAccountEscrowedId);
			Tgpj_BankAccountSupervised tgpj_bankAccountSupervised = tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);
			
			if(tgpf_fundAppropriated_af == null)
			{
				return MyBackInfo.fail(properties, "'用款申请主表信息'不能为空");
			}
			if(tgxy_bankAccountEscrowed == null)
			{
				return MyBackInfo.fail(properties, "'托管账户'不能为空");
			}
			if(tgpj_bankAccountSupervised == null)
			{
				return MyBackInfo.fail(properties, "'监管账户'不能为空");
			}
            if(tgpf_fundAppropriated_af.getDevelopCompany() == null)
			{
				return MyBackInfo.fail(properties, "'开发企业信息'不能为空");
			}
			Emmp_CompanyInfo developCompany = tgpf_fundAppropriated_af.getDevelopCompany();

            String eCodeOfDevelopCompany = developCompany.geteCode();

			if(eCodeOfDevelopCompany == null || eCodeOfDevelopCompany.length() == 0)
			{
				return MyBackInfo.fail(properties, "'开发企业编号'不能为空");
			}
			if(tgxy_bankAccountEscrowed.getBankBranch()==null)
			{
				return MyBackInfo.fail(properties, "'托管账户所属银行'不能为空");
			}
			/*if(tgpj_bankAccountSupervised.getBankBranch()==null)
			{
				return MyBackInfo.fail(properties, "'监管账户所属银行'不能为空");
			}*/
//			Double cyberBankTotalAmount = 0.00;
////			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Tgpf_CyberBankStatementForm tgpf_CyberBankStatementForm = new Tgpf_CyberBankStatementForm();
//			tgpf_CyberBankStatementForm.setTheState(S_TheState.Normal);
//			tgpf_CyberBankStatementForm.setTheAccountOfBankAccountEscrowed(tgxy_bankAccountEscrowed.getTheAccount());
//			tgpf_CyberBankStatementForm.setReconciliationState(0);
////			tgpf_CyberBankStatementForm.setUploadTimeStamp(sdf.format(new Date()));
//			List<Tgpf_CyberBankStatement> cyberBankStatementList;
//			cyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL(), tgpf_CyberBankStatementForm));
//
//			Tgpf_CyberBankStatementDtlForm tgpf_CyberBankStatementDtlForm;
//			for (Tgpf_CyberBankStatement cyberBankStatement : cyberBankStatementList) {
//				tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
//				tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
//				tgpf_CyberBankStatementDtlForm.setMainTable(cyberBankStatement);
//				tgpf_CyberBankStatementDtlForm.setMainTableId(cyberBankStatement.getTableId());
//				cyberBankTotalAmount += (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(),tgpf_CyberBankStatementDtlForm, " nvl(sum(income),0) "));
//			}
//
//			/*Tgpf_CyberBankStatement cyberBankStatement = tgpf_CyberBankStatementDao.findOneByQuery_T(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL2(), tgpf_CyberBankStatementForm));
//
//			if(null != cyberBankStatement){
//				tgpf_CyberBankStatementDtlForm = new Tgpf_CyberBankStatementDtlForm();
//				tgpf_CyberBankStatementDtlForm.setTheState(S_TheState.Normal);
//				tgpf_CyberBankStatementDtlForm.setMainTable(cyberBankStatement);
//				tgpf_CyberBankStatementDtlForm.setMainTableId(cyberBankStatement.getTableId());
//				cyberBankTotalAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL(),tgpf_CyberBankStatementDtlForm, " nvl(sum(income),0) "));
//
//			}*/
//
//			//活期余额+当天网银入账金额
//			Double countAmount = (null == cyberBankTotalAmount?0.00 : cyberBankTotalAmount) + (null == tgxy_bankAccountEscrowed.getCurrentBalance()?0.00:tgxy_bankAccountEscrowed.getCurrentBalance());
//			if(countAmount < 0 ){
//				countAmount = 0.00;
//			}
//			if(overallPlanPayoutAmount - countAmount > 0){
//				return MyBackInfo.fail(properties, "开户行："+tgxy_bankAccountEscrowed.getTheNameOfBank()+" 托管余额不足！");
//			}

			Tgpf_FundAppropriated tgpf_FundAppropriated = new Tgpf_FundAppropriated();
			tgpf_FundAppropriated.setTheState(S_TheState.Normal);
			tgpf_FundAppropriated.setBusiState(String.valueOf(S_PayoutState.NotAppropriated)); // 未拨付
			tgpf_FundAppropriated.setColorState(colorState);
//			tgpf_FundAppropriated.setCanPayAmount(canPayAmount);
			tgpf_FundAppropriated.setCanPayAmount(tgxy_bankAccountEscrowed.getCurrentBalance());//托管可拨付金额
			tgpf_FundAppropriated.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_FundAppropriated.setDevelopCompany(developCompany); //关联开发企业
			tgpf_FundAppropriated.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);//开发企业编号
			tgpf_FundAppropriated.setFundAppropriated_AF(tgpf_fundAppropriated_af);//用款申请
			tgpf_FundAppropriated.setOverallPlanPayoutAmount(overallPlanPayoutAmount);//统筹拨付金额
			tgpf_FundAppropriated.setCurrentPayoutAmount(overallPlanPayoutAmount);//本次实际拨付金额
			tgpf_FundAppropriated.setBankAccountEscrowed(tgxy_bankAccountEscrowed);//托管账户
			tgpf_FundAppropriated.setBankAccountSupervised(tgpj_bankAccountSupervised);//监管账户
			tgpf_FundAppropriated.setUserStart(loginUser);
			tgpf_FundAppropriated.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_FundAppropriated.setUserUpdate(loginUser);
			tgpf_FundAppropriated.setLastUpdateTimeStamp(System.currentTimeMillis());


		properties.put("tgpf_FundAppropriated",tgpf_FundAppropriated);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
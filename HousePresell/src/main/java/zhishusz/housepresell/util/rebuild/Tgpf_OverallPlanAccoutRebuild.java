package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：统筹-账户状况信息保存表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_OverallPlanAccoutRebuild extends RebuilderBase<Tgpf_OverallPlanAccout>
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;
	@Autowired
	private Tg_DepositManagementDao tg_depositManagementDao;

	private MyDatetime myDatetime = MyDatetime.getInstance();

	private MyDouble myDouble = MyDouble.getInstance();

	@Override
	public Properties getSimpleInfo(Tgpf_OverallPlanAccout object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_OverallPlanAccout object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("fundOverallPlan", object.getFundOverallPlan());
		properties.put("fundOverallPlanId", object.getFundOverallPlan().getTableId());
		properties.put("eCodeOfFundOverallPlan", object.geteCodeOfFundOverallPlan());
		properties.put("bankAccountEscrowed", object.getBankAccountEscrowed());
		properties.put("bankAccountEscrowedId", object.getBankAccountEscrowed().getTableId());
		properties.put("overallPlanAmount", object.getOverallPlanAmount());
		
		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_OverallPlanAccout> tgpf_OverallPlanAccoutList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_OverallPlanAccoutList != null)
		{
			for(Tgpf_OverallPlanAccout object:tgpf_OverallPlanAccoutList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId()); //统筹Id
				properties.put("overallPlanAmount", myDouble.pointTOThousandths(object.getOverallPlanAmount())); //统筹拨付金额
				properties.put("newOverallPlanAmount", object.getOverallPlanAmount()); //统筹拨付金额

				if(object.getBankAccountEscrowed() != null)
				{
					Tgxy_BankAccountEscrowed tgxy_bankAccountEscrowed = object.getBankAccountEscrowed();
					if(tgxy_bankAccountEscrowed.getBankBranch() !=null )
					{
						properties.put("theNameOfBankBranch",tgxy_bankAccountEscrowed.getBankBranch().getTheName()); //托管网银网点
					}
					properties.put("bankAccountEscrowId", tgxy_bankAccountEscrowed.getTableId()); //托管账户id
					properties.put("eCode", tgxy_bankAccountEscrowed.geteCode());
					properties.put("theName", tgxy_bankAccountEscrowed.getTheName());
					properties.put("theAccount", tgxy_bankAccountEscrowed.getTheAccount()); // 托管账号
					properties.put("currentBalance", myDouble.pointTOThousandths(tgxy_bankAccountEscrowed.getCurrentBalance())); // 托管活期余额
//					properties.put("newCurrentBalance", tgxy_bankAccountEscrowed.getCurrentBalance()); // 托管活期余额
					properties.put("currentBalanceView", myDouble.pointTOThousandths(tgxy_bankAccountEscrowed.getCurrentBalanceView())); // 托管活期余额
					properties.put("newCurrentBalance", tgxy_bankAccountEscrowed.getCurrentBalanceView()); // 托管活期余额
					/**
					 * 1. 托管活期余额
					 */
					Double currentBalance = tgxy_bankAccountEscrowed.getCurrentBalance();// 托管活期余额
					if(currentBalance == null)
					{
						currentBalance = 0.0;   //托管活期余额
					}
					Double largeAndCurrentRatio = 0.0; // 大额+活期占比
					Double certOfDeposit = tgxy_bankAccountEscrowed.getCertOfDeposit(); //大额存单
					if(certOfDeposit == null)
					{
						certOfDeposit = 0.0;
					}
					Double income = tgxy_bankAccountEscrowed.getIncome(); // 托管收入
					if(income!=null && income > 0)
					{
						largeAndCurrentRatio = (certOfDeposit + currentBalance)/income;
					}

					properties.put("income", income); //托管收入
					properties.put("largeAndCurrentRatio", myDouble.pointTOPercent(largeAndCurrentRatio,"0.00%")); //大额+活期占比 = 大额+活期占比=（大额存单+托管账户活期余额）/托管收入
					properties.put("totalFundsRatio",  myDouble.pointTOPercent(tgxy_bankAccountEscrowed.getTotalFundsRatio(),"0.00%")); //总资金沉淀占比
					properties.put("totalFundsRatioView",  myDouble.pointTOPercent(tgxy_bankAccountEscrowed.getTotalFundsRatioView(),"0.00%")); //总资金沉淀占比
//					System.out.println("account="+properties.get("theAccount")+"------totalFundsRatio="+properties.get("totalFundsRatio") +"------totalFundsRatioView="+properties.get("totalFundsRatioView"));

					/**
					 * 2. 上一天网银入账金额
					 */
					Double transactionAmount = 0.0;

					Tgpf_FundOverallPlan tgpf_fundOverallPlan = object.getFundOverallPlan();
//					String fundOverallPlanDate = tgpf_fundOverallPlan.getFundOverallPlanDate();//统筹日期
//					Long dayTimeStamp = 24L * 60 * 60 * 1000 - 1;
//					Long fundOverallPlanTimeStamp = myDatetime.stringToLong(fundOverallPlanDate) - dayTimeStamp;

//					String theAccount  = tgxy_bankAccountEscrowed.getTheAccount(); // 托管账号
//					Tgpf_CyberBankStatementForm cyberBankStatementForm = new Tgpf_CyberBankStatementForm();
//					cyberBankStatementForm.setTheState(S_TheState.Normal);
//					cyberBankStatementForm.setTheAccountOfBankAccountEscrowed(theAccount);
//					cyberBankStatementForm.setBillTimeStamp(myDatetime.dateToSimpleString(fundOverallPlanTimeStamp));//yyyy-MM-dd
//					cyberBankStatementForm.setUploadTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));//yyyy-MM-dd
//
//					List<Tgpf_CyberBankStatement>  tgpf_CyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL2(), cyberBankStatementForm));
//					for (Tgpf_CyberBankStatement tgpf_CyberBankStatement : tgpf_CyberBankStatementList)
//					{
//						Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
//						form.setTheState(S_TheState.Normal);
//						form.setBusiState("0");
//						form.setMainTable(tgpf_CyberBankStatement);
//
//						//查询交易总笔数
//						Integer transactionCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
//						if(transactionCount >  0){
//							String queryTransactionAmountCondition = "  nvl(sum(income),0)  ";
//							transactionAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form,queryTransactionAmountCondition));
//						}
//					}
					properties.put("transactionAmount",  myDouble.pointTOThousandths(transactionAmount)); //网银入账金额


					/**
					 * 3 . 当天入账金额调整项
					 */
					Double accountAmountTrim = object.getAccountAmountTrim();

					if(accountAmountTrim == null)
					{
						accountAmountTrim = 0.0;
					}
					properties.put("accountAmountTrim", myDouble.pointTOThousandths(accountAmountTrim)); //当天入账金额调整项


					/**
					 * 4 .已统筹未拨付金额
					 */
					/*if("江南农村商业银行金坛支行".equals(tgxy_bankAccountEscrowed.getShortNameOfBank())) {
						System.out.println(tgxy_bankAccountEscrowed.getShortNameOfBank());
					}*/
					
					Double coordinatedNoPayout = 0.0 ;
					Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
					fundAppropriatedForm.setTheState(S_TheState.Normal);
					fundAppropriatedForm.setBankAccountEscrowedId(tgxy_bankAccountEscrowed.getTableId());//托管账户Id
					fundAppropriatedForm.setBusiState(String.valueOf(S_PayoutState.NotAppropriated)); //未拨付
					fundAppropriatedForm.setFundOverallPlanId(tgpf_fundOverallPlan.getTableId());
					String hql = "  nvl(sum(overallPlanPayoutAmount),0.0)  ";

					Object coordinatedNoPayouto = null;
					coordinatedNoPayouto = tgpf_fundAppropriatedDao.findOneByQuery(tgpf_fundAppropriatedDao.getSpecialQuery(tgpf_fundAppropriatedDao.getBasicHQL2(), fundAppropriatedForm,hql));
					if(coordinatedNoPayouto != null){
						coordinatedNoPayout = (Double)coordinatedNoPayouto;
					}
//					Integer fundAppropriatedCount = tgpf_fundAppropriatedDao.findByPage_Size(tgpf_fundAppropriatedDao.getQuery_Size(tgpf_fundAppropriatedDao.getBasicHQL2(), fundAppropriatedForm));
//					if(fundAppropriatedCount > 0)
//					{
//						coordinatedNoPayout = (Double) tgpf_fundAppropriatedDao.findOneByQuery(tgpf_fundAppropriatedDao.getSpecialQuery(tgpf_fundAppropriatedDao.getBasicHQL2(), fundAppropriatedForm,hql));
//					}
					properties.put("coordinatedNoPayout",  myDouble.pointTOThousandths(coordinatedNoPayout)); //已统筹未拨付金额

					/**
					 * 5 .正在办理中
					 */
					Double handling = 0.0;
					Tg_DepositManagementForm tg_depositManagementForm = new Tg_DepositManagementForm();
					tg_depositManagementForm.setTheState(S_TheState.Normal);
					tg_depositManagementForm.setEscrowAcountId(tgxy_bankAccountEscrowed.getTableId());
					tg_depositManagementForm.setDepositState(S_DepositState.InProgress); //正在办理中
					String hql2 = " nvl(sum(principalAmount),0.0)  ";
					Integer depositManagementCount = tg_depositManagementDao.findByPage_Size(tg_depositManagementDao.getQuery_Size(tg_depositManagementDao.getBasicHQL2(), tg_depositManagementForm));
					if(depositManagementCount > 0)
					{
						handling = (Double) tg_depositManagementDao.findOneByQuery(tg_depositManagementDao.getSpecialQuery(tg_depositManagementDao.getBasicHQL2(), tg_depositManagementForm,hql2));
					}
					properties.put("handling", myDouble.pointTOThousandths(handling)); //正在办理中

					
					//托管可划拨金额=托管活期余额(入账只包括确认过T-2)+上一天的网银入账数据（T-1）+当天入账金额调整项-已统筹未拨付金额-正在办理中
					Double canPayAmount = currentBalance + transactionAmount + accountAmountTrim - coordinatedNoPayout - handling;
					
//					System.out.println(tgxy_bankAccountEscrowed.getShortNameOfBank()+":"+canPayAmount);
					
					properties.put("canPayAmount", myDouble.pointTOThousandths(canPayAmount)); //托管可拨付金额

					properties.put("temporaryAmount",canPayAmount);//托管可拨付金额
				}
				list.add(properties);
			}
		}
		return list;
	}
}

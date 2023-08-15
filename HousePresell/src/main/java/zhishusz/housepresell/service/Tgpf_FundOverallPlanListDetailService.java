package zhishusz.housepresell.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_OverallPlanAccoutForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedViewForm;
import zhishusz.housepresell.controller.form.extra.Qs_TripleAgreement_ViewForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.dao.extra.Qs_TripleAgreement_ViewDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_ColorState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：资金统筹 - 详情页面
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_FundOverallPlanListDetailService
{
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;

	@Autowired
	private Tgpf_OverallPlanAccoutDao tgpf_overallPlanAccoutDao;

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;

	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;

	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;

	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细

	@Autowired
	private Qs_TripleAgreement_ViewDao qs_TripleAgreement_ViewDao;


	@Autowired
	private Tgxy_BankAccountEscrowedViewDao bankAccountEscrowedViewDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_OverallPlanAccoutForm model)
	{
		Properties properties = new MyProperties();

		//获取统筹id
		Long fundOverallPlanId = model.getFundOverallPlanId();
		if(fundOverallPlanId == null || fundOverallPlanId < 1)
		{
			return MyBackInfo.fail(properties, "统筹单不能为空");
		}
		//通过统筹id获取统筹单信息
		Tgpf_FundOverallPlan tgpf_fundOverallPlan  = tgpf_FundOverallPlanDao.findById(fundOverallPlanId);
		if(tgpf_fundOverallPlan == null)
		{
			return MyBackInfo.fail(properties, "统筹单不能为空");
		}

		if(!S_ApprovalState.WaitSubmit.equals(tgpf_fundOverallPlan.getApprovalState()))
		{
			model.setOverallPlanAmount(0D);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		Integer totalCount = tgpf_overallPlanAccoutDao.findByPage_Size(tgpf_overallPlanAccoutDao.getQuery_Size(tgpf_overallPlanAccoutDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;


		List<Tgpf_OverallPlanAccout> tgpf_overallPlanAccoutList;
		List<Tgpf_FundOverallPlanDetail> fundOverallPlanDetailList = null;
		List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList = null;
		Boolean isFirstFundAppropriated = null;
		System.out.println("时间2----"+sdf.format(new Date()));
		if(totalCount > 0)
		{
			//统筹账户列表信息
			tgpf_overallPlanAccoutList = tgpf_overallPlanAccoutDao.findByPage(tgpf_overallPlanAccoutDao.getQuery(tgpf_overallPlanAccoutDao.getBasicHQL(), model), pageNumber, countPerPage);

			//用款申请汇总信息
			if(tgpf_fundOverallPlan.getFundOverallPlanDetailList() != null && tgpf_fundOverallPlan.getFundOverallPlanDetailList().size() > 0)
			{
				fundOverallPlanDetailList = tgpf_fundOverallPlan.getFundOverallPlanDetailList();
			}
			else
			{
				fundOverallPlanDetailList = new ArrayList<>();
			}

			System.out.println("时间3----"+sdf.format(new Date()));
			Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
			fundAppropriatedForm.setFundOverallPlanId(fundOverallPlanId);
			tgpf_FundAppropriatedList = this.tgpf_fundAppropriatedDao.findByPage(this.tgpf_fundAppropriatedDao.getQuery(this.tgpf_fundAppropriatedDao.getBasicHQL(), (BaseForm)fundAppropriatedForm));
			isFirstFundAppropriated = false;
			System.out.println("时间3.1----"+sdf.format(new Date()));

			if(tgpf_FundAppropriatedList == null || tgpf_FundAppropriatedList.size() == 0)
			{
				isFirstFundAppropriated = true;
				tgpf_FundAppropriatedList = new ArrayList<>();


				System.out.println("时间2.1----"+sdf.format(new Date()));
				List<Tgxy_BankAccountEscrowedView> viewList = new ArrayList<Tgxy_BankAccountEscrowedView>();
				//资金拨付
				Tgxy_BankAccountEscrowedViewForm bankAccountEscrowedViewForm = new Tgxy_BankAccountEscrowedViewForm();
				//Tgxy_BankAccountEscrowedViewForm.setFundOverallPlanId(fundOverallPlanId);// 不通过操作删除统筹单，审批详情页面还能够看到明细表信息。
				viewList = bankAccountEscrowedViewDao.findByPage(bankAccountEscrowedViewDao.getQuery(bankAccountEscrowedViewDao.getBasicHQL(), bankAccountEscrowedViewForm));

				System.out.println("viewList="+viewList.size());

				System.out.println("时间2.2----"+sdf.format(new Date()));


				for (Tgpf_OverallPlanAccout tgpf_overallPlanAccout : tgpf_overallPlanAccoutList)
				{
					Tgxy_BankAccountEscrowed overallPlanAccout_BankAccountEscrowed = tgpf_overallPlanAccout.getBankAccountEscrowed(); //统筹账户托管账户

					Tgxy_BankAccountEscrowedView view = tgpf_overallPlanAccout.getBankAccountEscrowedView();

					overallPlanAccout_BankAccountEscrowed.setCurrentBalanceView(view.getCurrentBalance());
					overallPlanAccout_BankAccountEscrowed.setTotalFundsRatioView(view.getTotalFundsRatio());

//					System.out.println("----="+view.getAvzjcdzb());

					for (Tgpf_FundOverallPlanDetail tgpf_fundOverallPlanDetail : fundOverallPlanDetailList)
					{

						Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af = tgpf_fundOverallPlanDetail.getMainTable(); //用款申请主表
						if(tgpf_fundOverallPlanDetail.getMainTable() ==null)
						{
							return MyBackInfo.fail(properties, "'用款申请主表信息'不能为空");
						}

						Tgpj_BankAccountSupervised OverallPlanDetail_BankAccountSupervised = tgpf_fundOverallPlanDetail.getBankAccountSupervised(); //用款申请汇总托管账户

						Tgpf_FundAppropriated tgpf_FundAppropriated = new Tgpf_FundAppropriated(); //实例化一个新的资金拨付
						//同行：监管银行和托管银行是同一个银行的用绿色标识
//						if(overallPlanAccout_BankAccountEscrowed.getBankBranch().getTableId().equals(OverallPlanDetail_BankAccountSupervised.getBankBranch().getTableId()))
						/*
						 * xsz by time 2018-12-27 18:55:11
						 * 监管账户开户行直接取TheNameOfBank()
						 * --start---
						 */
						if(overallPlanAccout_BankAccountEscrowed.getBankBranch().getTheName().equals(OverallPlanDetail_BankAccountSupervised.getTheNameOfBank()))
						/*
						 * xsz by time 2018-12-27 18:55:11
						 * 监管账户开户行直接取TheNameOfBank()
						 * --end---
						 */
						{
							tgpf_FundAppropriated.setColorState(S_ColorState.Green);
						}
						else
						{
							if(tgpf_fundAppropriated_af.getProject() ==null)
							{
								return MyBackInfo.fail(properties, "'用款申请项目'不能为空");
							}
							Empj_ProjectInfo empj_projectInfo = tgpf_fundAppropriated_af.getProject();

							Qs_TripleAgreement_ViewForm qs_TripleAgreement_ViewForm = new Qs_TripleAgreement_ViewForm();
							qs_TripleAgreement_ViewForm.setBankAccountEscrowedId(overallPlanAccout_BankAccountEscrowed.getTableId());
							qs_TripleAgreement_ViewForm.setProjectId(empj_projectInfo.getTableId());

							Integer detailCount = qs_TripleAgreement_ViewDao.findByPage_Size(
									qs_TripleAgreement_ViewDao.getQuery_Size(qs_TripleAgreement_ViewDao.getBasicHQL(), qs_TripleAgreement_ViewForm));

							if(detailCount > 0)
							{
								tgpf_FundAppropriated.setColorState(S_ColorState.yelllow);
							}

							//放款银行：入账数据对应的托管账号，有数据用黄色标记，只要有就可以，不需要一定是确认的数据
						}
						tgpf_FundAppropriated.setOverallPlanPayoutAmount(0.00);//统筹拨付金额
						tgpf_FundAppropriated.setBankAccountEscrowed(overallPlanAccout_BankAccountEscrowed);//托管账户
						tgpf_FundAppropriated.setBankAccountSupervised(OverallPlanDetail_BankAccountSupervised);//监管账户
						tgpf_FundAppropriated.setFundAppropriated_AF(tgpf_fundAppropriated_af); //用跨申请主表

						tgpf_FundAppropriatedList.add(tgpf_FundAppropriated);
					}
				}
			}else{
				for (Tgpf_OverallPlanAccout tgpf_overallPlanAccout : tgpf_overallPlanAccoutList) {
					Tgxy_BankAccountEscrowed overallPlanAccout_BankAccountEscrowed = tgpf_overallPlanAccout.getBankAccountEscrowed(); //统筹账户托管账户

					Tgxy_BankAccountEscrowedView view = tgpf_overallPlanAccout.getBankAccountEscrowedView();

					overallPlanAccout_BankAccountEscrowed.setCurrentBalanceView(view.getCurrentBalance());
					overallPlanAccout_BankAccountEscrowed.setTotalFundsRatioView(view.getTotalFundsRatio());
				}
			}
			System.out.println("时间4----"+sdf.format(new Date()));
		}

		else
		{
			tgpf_overallPlanAccoutList = new ArrayList<>();
		}

		//--------------------审批---------------------------------------//
		Long afId = model.getAfId();//申请单Id
		Sm_ApprovalProcess_AF sm_approvalProcess_af;
		Boolean isNeedBackup = null;
		if(afId!=null && afId > 0)
		{
			sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);

			if(sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState()))
			{
				return MyBackInfo.fail(properties, "'申请单'不存在");
			}
			Long currentNode = sm_approvalProcess_af.getCurrentIndex();
			Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(currentNode);
			if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
			{
				if(sm_approvalProcess_af.getIsNeedBackup().equals(1))
				{
					isNeedBackup = true;
				}
			}
			else
			{
				isNeedBackup = false;
			}
		}

		//--------------------审批---------------------------------------//



		properties.put("tgpf_FundOverallPlan",tgpf_fundOverallPlan); //资金统筹
		properties.put("tgpf_overallPlanAccoutList",tgpf_overallPlanAccoutList);// 统筹账户状况信息
		properties.put("fundOverallPlanDetailList",fundOverallPlanDetailList); // 用款申请汇总信息
		properties.put("tgpf_FundAppropriatedList",tgpf_FundAppropriatedList); // 资金拨付
		properties.put("isFirstFundAppropriated",isFirstFundAppropriated); // 是否是初次统筹
		properties.put("isNeedBackup",isNeedBackup);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

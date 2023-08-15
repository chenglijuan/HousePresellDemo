package zhishusz.housepresell.service.extra;

import zhishusz.housepresell.controller.form.extra.Qs_EscrowBankFunds_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.extra.Qs_EscrowBankFunds_ViewDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.extra.Qs_EscrowBankFunds_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service添加操作：贷款银行资金明细
 * Company：ZhiShuSZ
 */
@Service
// @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
@Transactional
public class Qs_EscrowBankFunds_ViewListService
{
	@Autowired
	private Qs_EscrowBankFunds_ViewDao qs_EscrowBankFunds_ViewDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_EscrowBankFunds_ViewForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String theNameOfDepositBank = model.getTheNameOfDepositBank();
		String timeStamp = model.getTimeStamp();

		if (null != theNameOfDepositBank && theNameOfDepositBank.trim().isEmpty())
		{
			model.setTheNameOfDepositBank(null);
		}
		else
		{
			Emmp_BankBranch emmp_BankBranch = emmp_BankBranchDao.findById(new Long(theNameOfDepositBank));
			if(null != emmp_BankBranch){
				model.setTheNameOfDepositBank(emmp_BankBranch.getTheName());
			}
		}

		if (null != timeStamp && timeStamp.trim().isEmpty())
		{
			model.setTimeStamp(null);
		}

		if (null == keyword || keyword.length() <= 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		Integer totalCount = qs_EscrowBankFunds_ViewDao.findByPage_Size(
				qs_EscrowBankFunds_ViewDao.getQuery_Size(qs_EscrowBankFunds_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Qs_EscrowBankFunds_View> qs_EscrowBankFunds_ViewList;
		if (totalCount > 0)
		{
			qs_EscrowBankFunds_ViewList = qs_EscrowBankFunds_ViewDao.findByPage(
					qs_EscrowBankFunds_ViewDao.getQuery(qs_EscrowBankFunds_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);

			/*
			 * xsz by time 2018-9-5 10:29:15
			 * 合计时的比例计算逻辑：
			 * 大额占比 = 大额存单/托管收入
			 * 大额+活期占比 = (大额存单+活期)/托管收入
			 * 理财占比 = (结构性存款+保本理财)/托管收入
			 * 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
			 * 
			 * 存款合计 = 大额存单+结构性存款+保本理财
			 * 存款大额占比 = 大额存单/存款合计
			 * 存款理财占比 = (结构性存款+保本理财)/存款合计 = 1-存款大额占比
			 */

			// 日期 TIMESTAMP timeStamp
			// 存款银行 THENAMEOFBANK theNameOfBank
			// 开户行 THENAMEOFDEPOSITBANK theNameOfDepositBank
			// 托管账号 THEACCOUNT theAccount
			// 托管账号名称 THENAME theName
			// 托管收入 INCOME income
			// 托管支出 PAYOUT payout
			// 大额存单 CERTOFDEPOSIT certOfDeposit
			// 结构性存款 STRUCTUREDDEPOSIT structuredDeposit
			// 保本理财 BREAKEVENFINANCIAL breakEvenFinancial
			// 活期余额 CURRENTBALANCE currentBalance
			// 大额占比 LARGERATIO largeRatio
			// 大额+活期占比 LARGEANDCURRENTRATIO largeAndCurrentRatio
			// 理财占比 FINANCIALRATIO financialRatio
			// 总资金沉淀占比 TOTALFUNDSRATIO totalFundsRatio
			// 正在办理中 INPROGRESS inProgress
			MyDouble dplan = MyDouble.getInstance();
			Qs_EscrowBankFunds_View qe = new Qs_EscrowBankFunds_View();
			qe.setIncome(0.00);
			qe.setPayout(0.00);
			qe.setCertOfDeposit(0.00);
			qe.setStructuredDeposit(0.00);
			qe.setBreakEvenFinancial(0.00);
			qe.setCurrentBalance(0.00);
			qe.setInProgressAccount(0.00);
			qe.setTransferInAmount(0.00);
			qe.setTransferOutAmount(0.00);
			qe.setTimeStamp("-");
			qe.setInProgress("-");
			qe.setTransferOutAmount(0.00);
			qe.setTransferInAmount(0.00);
			qe.setTheNameOfDepositBank("合计");

			for (Qs_EscrowBankFunds_View qs : qs_EscrowBankFunds_ViewList)
			{

				qe.setIncome(dplan.doubleAddDouble(qe.getIncome(), qs.getIncome()));// 托管收入
				qe.setPayout(dplan.doubleAddDouble(qe.getPayout(), qs.getPayout()));// 托管支出
				qe.setCertOfDeposit(dplan.doubleAddDouble(qe.getCertOfDeposit(), qs.getCertOfDeposit()));// 大额存单
				qe.setStructuredDeposit(dplan.doubleAddDouble(qe.getStructuredDeposit(), qs.getStructuredDeposit()));// 结构性存款
				qe.setBreakEvenFinancial(dplan.doubleAddDouble(qe.getBreakEvenFinancial(), qs.getBreakEvenFinancial()));// 保本理财
				qe.setCurrentBalance(dplan.doubleAddDouble(qe.getCurrentBalance(), qs.getCurrentBalance()));// 活期余额
				qe.setInProgressAccount(dplan.doubleAddDouble(qe.getInProgressAccount(), qs.getInProgressAccount()));// 正在办理中金额
				qe.setTransferInAmount(dplan.doubleAddDouble(qe.getTransferInAmount(), qs.getTransferInAmount()));
				qe.setTransferOutAmount(dplan.doubleAddDouble(qe.getTransferOutAmount(), qs.getTransferOutAmount()));
			}
			/*
			 * doubleAddDouble 加
			 * doubleSubtractDouble 减
			 * doubleMultiplyDouble 乘
			 * div 除
			 * getShort() 保留小数位
			 */
			// 大额占比 = 大额存单/托管收入
			Double a = MyDouble.getInstance().div(qe.getCertOfDeposit(), qe.getIncome(), 4);
			Double b = MyDouble.getInstance().doubleMultiplyDouble(a, 100.00);
			Double c = MyDouble.getInstance().getShort(b, 2);
			qe.setLargeRatio(c);

			// 大额+活期占比 = (大额存单+活期)/托管收入
			Double a1 = MyDouble.getInstance().doubleAddDouble(qe.getCertOfDeposit(), qe.getCurrentBalance());
			Double b1 = MyDouble.getInstance().div(a1, qe.getIncome(), 4);
			Double c1 = MyDouble.getInstance().doubleMultiplyDouble(b1, 100.00);
			Double d1 = MyDouble.getInstance().getShort(c1, 2);
			qe.setLargeAndCurrentRatio(d1);

			// 理财占比 = (结构性存款+保本理财)/托管收入
			Double a2 = MyDouble.getInstance().doubleAddDouble(qe.getStructuredDeposit(), qe.getBreakEvenFinancial());
			Double b2 = MyDouble.getInstance().div(a2, qe.getIncome(), 4);
			Double c2 = MyDouble.getInstance().doubleMultiplyDouble(b2, 100.00);
			Double d2 = MyDouble.getInstance().getShort(c2, 2);
			qe.setFinancialRatio(d2);

			// 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
			Double a3 = MyDouble.getInstance().doubleAddDouble(a1, a2);
			Double b3 = MyDouble.getInstance().div(a3, qe.getIncome(), 4);
			Double c3 = MyDouble.getInstance().doubleMultiplyDouble(b3, 100.00);
			Double d3 = MyDouble.getInstance().getShort(c3, 2);
			qe.setTotalFundsRatio(d3);

			// //存款合计 = 大额存单+结构性存款+保本理财
			// Double ckhj =
			// MyDouble.getInstance().doubleAddDouble(qe.getCertOfDeposit(),
			// a2);
			//
			// properties.put("ckhj", ckhj);
			//
			// //存款大额占比 = 大额存单/存款合计
			// Double a4 = MyDouble.getInstance().div(qe.getCertOfDeposit(),
			// ckhj,4);
			// Double b4 = MyDouble.getInstance().doubleMultiplyDouble(a4,
			// 100.00);
			// Double ckdezb = MyDouble.getInstance().getShort(b4, 2);
			//
			// properties.put("ckdezb", ckdezb);
			//
			// //存款理财占比 = (结构性存款+保本理财)/存款合计 = 1-存款大额占比
			// Double cklczb =
			// MyDouble.getInstance().doubleSubtractDouble(100.00, ckdezb);
			//
			// properties.put("cklczb", cklczb);

			// 合计信息入列表
			qs_EscrowBankFunds_ViewList.add(qe);

		}
		else
		{
			qs_EscrowBankFunds_ViewList = new ArrayList<Qs_EscrowBankFunds_View>();
		}

		properties.put("qs_EscrowBankFunds_ViewList", qs_EscrowBankFunds_ViewList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

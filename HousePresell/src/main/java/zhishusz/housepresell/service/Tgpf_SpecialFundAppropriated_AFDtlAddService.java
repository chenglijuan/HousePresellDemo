package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_SpecialFundApplyState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/*
 * Service添加操作：特殊拨付-申请子表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFDtlAddService
{
	private static String BUSI_CODE = "061206";
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	public Properties execute(Tgpf_SpecialFundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "登录失效，请重新登录");
		}

		// 主表tableId
		Long appropriatedId = model.getSpecialAppropriatedId();
		if (null == appropriatedId || appropriatedId < 0)
		{
			return MyBackInfo.fail(properties, "拨付申请信息不能为空");
		}
		Tgpf_SpecialFundAppropriated_AF appropriated_AF = tgpf_SpecialFundAppropriated_AFDao.findById(appropriatedId);
		if (null == appropriated_AF || S_TheState.Normal != appropriated_AF.getTheState())
		{
			return MyBackInfo.fail(properties, "拨付申请信息已失效，请刷新后重试");
		}

		// 拨付划款信息json
		String fundAppropriatedSubList = model.getSpacialFundAppropriatedSubList();
		if (null == fundAppropriatedSubList || fundAppropriatedSubList.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请增加拨付划款信息");
		}

		List<Tgpf_SpecialFundAppropriated_AFDtlForm> list = JSON.parseArray(fundAppropriatedSubList,
				Tgpf_SpecialFundAppropriated_AFDtlForm.class);
		if (null == list || list.size() == 0)
		{
			return MyBackInfo.fail(properties, "请增加拨付划款信息");
		}

		/*
		 * 对拨付信息进行遍历
		 * 可以同时维护多个托管信息，本次划拨申请金额可进行累加，累加的结果与提交的“本次划款申请金额”必须一致，校验通过后，方可审批通过。
		 */
		Double countAmount = 0.00;
		for (Tgpf_SpecialFundAppropriated_AFDtlForm form : list)
		{

			// 本次划拨申请金额
			Double appliedAmount = form.getAppliedAmount();
			if (null == appliedAmount || appliedAmount < 0)
			{
				return MyBackInfo.fail(properties, "划款申请金额不能为空");
			}

			countAmount += appliedAmount;

		}

		if (countAmount.compareTo(appropriated_AF.getTotalApplyAmount())!=0)
		{
			return MyBackInfo.fail(properties, "划款总金额与申请金额必须一致！");
		}
		
		Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl;
		for (Tgpf_SpecialFundAppropriated_AFDtlForm form : list)
		{
			
			// 托管账户
			Long accountEscrowedId = form.getBankAccountEscrowedId();
			if (null == accountEscrowedId || accountEscrowedId < 0)
			{
				return MyBackInfo.fail(properties, "托管账号不能为空");
			}
			Tgxy_BankAccountEscrowed accountEscrowed = tgxy_BankAccountEscrowedDao.findById(accountEscrowedId);
			if (null == accountEscrowed || S_TheState.Normal != accountEscrowed.getTheState())
			{
				return MyBackInfo.fail(properties, "选择的托管账户信息已失效，请刷新后重试");
			}
			
			// 本次划拨申请金额
			Double appliedAmount = form.getAppliedAmount();
			
			tgpf_SpecialFundAppropriated_AFDtl = new Tgpf_SpecialFundAppropriated_AFDtl();
			tgpf_SpecialFundAppropriated_AFDtl.setTheState(S_TheState.Normal);
			tgpf_SpecialFundAppropriated_AFDtl.setBusiState(S_BusiState.NoRecord);
			tgpf_SpecialFundAppropriated_AFDtl.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
			tgpf_SpecialFundAppropriated_AFDtl.setUserStart(user);
			tgpf_SpecialFundAppropriated_AFDtl.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_SpecialFundAppropriated_AFDtl.setUserUpdate(user);
			tgpf_SpecialFundAppropriated_AFDtl.setLastUpdateTimeStamp(System.currentTimeMillis());

			tgpf_SpecialFundAppropriated_AFDtl.setSpecialAppropriated(appropriated_AF);
			tgpf_SpecialFundAppropriated_AFDtl.setTheCodeOfAf(appropriated_AF.geteCode());
			tgpf_SpecialFundAppropriated_AFDtl.setBankAccountEscrowed(accountEscrowed);
			// currentBalance活期余额
			tgpf_SpecialFundAppropriated_AFDtl.setApplyRefundPayoutAmount(
					null == accountEscrowed.getCurrentBalance() ? 0.00 : accountEscrowed.getCurrentBalance());
			// 账户余额 canPayAmount托管可拨付金额
			tgpf_SpecialFundAppropriated_AFDtl.setAccountBalance(
					null == accountEscrowed.getCanPayAmount() ? 0.00 : accountEscrowed.getCanPayAmount());
			tgpf_SpecialFundAppropriated_AFDtl.setAccountOfEscrowed(accountEscrowed.getTheAccount());
			tgpf_SpecialFundAppropriated_AFDtl.setTheNameOfEscrowed(accountEscrowed.getTheName());
			tgpf_SpecialFundAppropriated_AFDtl.setAppliedAmount(appliedAmount);
//			tgpf_SpecialFundAppropriated_AFDtl.setBillNumber(billNumber);
//			tgpf_SpecialFundAppropriated_AFDtl.setPayoutChannel(payoutChannel);
//			tgpf_SpecialFundAppropriated_AFDtl.setPayoutDate(payoutDate);
			// 拨付状态(未拨付-初始)
			tgpf_SpecialFundAppropriated_AFDtl.setPayoutState(S_SpecialFundApplyState.Notappropriated);
			tgpf_SpecialFundAppropriated_AFDtlDao.save(tgpf_SpecialFundAppropriated_AFDtl);

		}

		// // 托管账户
		// Long accountEscrowedId = model.getBankAccountEscrowedId();
		// if (null == accountEscrowedId || accountEscrowedId < 0)
		// {
		// return MyBackInfo.fail(properties, "托管账号不能为空");
		// }
		// Tgxy_BankAccountEscrowed accountEscrowed =
		// tgxy_BankAccountEscrowedDao.findById(accountEscrowedId);
		// if (null == accountEscrowed || S_TheState.Normal !=
		// accountEscrowed.getTheState())
		// {
		// return MyBackInfo.fail(properties, "选择的托管账户信息已失效，请刷新后重试");
		// }
		//
		// // 本次划拨申请金额
		// Double appliedAmount = model.getAppliedAmount();
		// if (null == appliedAmount || appliedAmount < 1)
		// {
		// return MyBackInfo.fail(properties, "请输入本次划款申请金额");
		// }

		/*
		 * 这三个信息在终审中维护
		 * // 拨付渠道
		 * String payoutChannel = model.getPayoutChannel();
		 * if (null == payoutChannel || payoutChannel.trim().isEmpty())
		 * {
		 * return MyBackInfo.fail(properties, "请选择拨付渠道");
		 * }
		 * 
		 * // 拨付日期
		 * // TODO 拨付日期是否需要手动选择？
		 * String payoutDate = model.getPayoutDate();
		 * if (null == payoutDate || payoutDate.trim().isEmpty())
		 * {
		 * return MyBackInfo.fail(properties, "请选择拨付日期");
		 * }
		 * 
		 * // 票据号
		 * // TODO 票据号是否手输？是否能重复？
		 * String billNumber = model.getBillNumber();
		 * if (null == billNumber || billNumber.trim().isEmpty())
		 * {
		 * return MyBackInfo.fail(properties, "请输入票据号");
		 * }
		 */

		/*
		 * TODO 需要确定？
		 * applyRefundPayoutAmount 已申请未拨付总金额（元）
		 * accountBalance 账户余额
		 * 这两个值无来源
		 */

		/*Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl = new Tgpf_SpecialFundAppropriated_AFDtl();
		tgpf_SpecialFundAppropriated_AFDtl.setTheState(S_TheState.Normal);
		tgpf_SpecialFundAppropriated_AFDtl.setBusiState(S_BusiState.NoRecord);
		tgpf_SpecialFundAppropriated_AFDtl.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tgpf_SpecialFundAppropriated_AFDtl.setUserStart(user);
		tgpf_SpecialFundAppropriated_AFDtl.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_SpecialFundAppropriated_AFDtl.setUserUpdate(user);
		tgpf_SpecialFundAppropriated_AFDtl.setLastUpdateTimeStamp(System.currentTimeMillis());

		tgpf_SpecialFundAppropriated_AFDtl.setSpecialAppropriated(appropriated_AF);
		tgpf_SpecialFundAppropriated_AFDtl.setTheCodeOfAf(appropriated_AF.geteCode());
		tgpf_SpecialFundAppropriated_AFDtl.setBankAccountEscrowed(accountEscrowed);
		// currentBalance活期余额
		tgpf_SpecialFundAppropriated_AFDtl.setApplyRefundPayoutAmount(
				null == accountEscrowed.getCurrentBalance() ? 0.00 : accountEscrowed.getCurrentBalance());
		// 账户余额 canPayAmount托管可拨付金额
		tgpf_SpecialFundAppropriated_AFDtl.setAccountBalance(
				null == accountEscrowed.getCanPayAmount() ? 0.00 : accountEscrowed.getCanPayAmount());
		tgpf_SpecialFundAppropriated_AFDtl.setAccountOfEscrowed(accountEscrowed.getTheAccount());
		tgpf_SpecialFundAppropriated_AFDtl.setTheNameOfEscrowed(accountEscrowed.getTheName());
		tgpf_SpecialFundAppropriated_AFDtl.setAppliedAmount(appliedAmount);
		tgpf_SpecialFundAppropriated_AFDtl.setBillNumber(billNumber);
		tgpf_SpecialFundAppropriated_AFDtl.setPayoutChannel(payoutChannel);
		tgpf_SpecialFundAppropriated_AFDtl.setPayoutDate(payoutDate);
		// 拨付状态(未拨付-初始)
		tgpf_SpecialFundAppropriated_AFDtl.setPayoutState(S_SpecialFundApplyState.Notappropriated);
		tgpf_SpecialFundAppropriated_AFDtlDao.save(tgpf_SpecialFundAppropriated_AFDtl);*/

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

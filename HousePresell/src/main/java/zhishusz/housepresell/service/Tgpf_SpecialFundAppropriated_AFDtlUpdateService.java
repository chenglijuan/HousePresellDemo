package zhishusz.housepresell.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_SpecialFundApplyState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;

/*
 * Service更新操作：特殊拨付-申请子表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFDtlUpdateService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;

	public Properties execute(Tgpf_SpecialFundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "登录失效，请重新登录");
		}

		// 修改的子表主键
		Long tableId = model.getTableId();
		if (null == tableId || tableId < 0)
		{
			return MyBackInfo.fail(properties, "请选择有效的划款信息");
		}
		Tgpf_SpecialFundAppropriated_AFDtl appropriated_AFDtl = tgpf_SpecialFundAppropriated_AFDtlDao.findById(tableId);
		if (null == appropriated_AFDtl || S_TheState.Normal != appropriated_AFDtl.getTheState())
		{
			return MyBackInfo.fail(properties, "选择的划款信息已失效，请刷新后重试");
		}

		// 托管账户
//		Long accountEscrowedId = model.getBankAccountEscrowedId();
//		if (null == accountEscrowedId || accountEscrowedId < 0)
//		{
//			return MyBackInfo.fail(properties, "托管账号不能为空");
//		}
//		Tgxy_BankAccountEscrowed accountEscrowed = tgxy_BankAccountEscrowedDao.findById(accountEscrowedId);
//		if (null == accountEscrowed || S_TheState.Normal != accountEscrowed.getTheState())
//		{
//			return MyBackInfo.fail(properties, "选择的托管账户信息已失效，请刷新后重试");
//		}
//
//		// 本次划拨申请金额
//		Double appliedAmount = model.getAppliedAmount();
//		if (null == appliedAmount || appliedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "请输入本次划款申请金额");
//		}

		// 拨付渠道
		String payoutChannel = model.getPayoutChannel();
		if (null == payoutChannel || payoutChannel.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择拨付渠道");
		}

		// 拨付日期
		// TODO 拨付日期是否需要手动选择？
		String payoutDate = model.getPayoutDate();
		if (null == payoutDate || payoutDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择拨付日期");
		}

		// 票据号
		// TODO 票据号是否手输？
		String billNumber = model.getBillNumber();
//		if (null == billNumber || billNumber.trim().isEmpty())
//		{
//			return MyBackInfo.fail(properties, "请输入票据号");
//		}
		
		appropriated_AFDtl.setUserUpdate(user);
		appropriated_AFDtl.setLastUpdateTimeStamp(System.currentTimeMillis());

//		appropriated_AFDtl.setBankAccountEscrowed(accountEscrowed);
//		appropriated_AFDtl.setAccountOfEscrowed(accountEscrowed.getTheAccount());
//		appropriated_AFDtl.setTheNameOfEscrowed(accountEscrowed.getTheName());
//		//currentBalance活期余额
//		appropriated_AFDtl.setApplyRefundPayoutAmount(null==accountEscrowed.getCurrentBalance()?0.00:accountEscrowed.getCurrentBalance());
//		//账户余额 canPayAmount托管可拨付金额
//		appropriated_AFDtl.setAccountBalance(null==accountEscrowed.getCanPayAmount()?0.00:accountEscrowed.getCanPayAmount());
//		appropriated_AFDtl.setAppliedAmount(appliedAmount);
		appropriated_AFDtl.setBillNumber(billNumber);
		appropriated_AFDtl.setPayoutChannel(payoutChannel);
		appropriated_AFDtl.setPayoutDate(payoutDate);
		tgpf_SpecialFundAppropriated_AFDtlDao.save(appropriated_AFDtl);

		Tgpf_SpecialFundAppropriated_AF af = appropriated_AFDtl.getSpecialAppropriated();
		if(af != null && StringUtils.isNotBlank(af.geteCode())){
			af.setAfPayoutDate(payoutDate);
			tgpf_SpecialFundAppropriated_AFDao.save(af);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

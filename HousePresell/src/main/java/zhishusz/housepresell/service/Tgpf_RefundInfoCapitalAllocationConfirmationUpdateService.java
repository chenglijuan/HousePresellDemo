package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：资金管理-退房退款
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoCapitalAllocationConfirmationUpdateService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;

	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;

	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();
		// 退房退款主键
		Long tableId = model.getTableId();
		if (null == tableId || tableId < 1)
		{
			return MyBackInfo.fail(properties, "'tableId'不能为空");
		}
		// 查询退房退款申请
		Tgpf_RefundInfo tgpf_RefundInfo = tgpf_RefundInfoDao.findById(tableId);
		if (null == tgpf_RefundInfo)
		{
			return MyBackInfo.fail(properties, "'Tgpf_RefundInfo(Id:" + tableId + ")'不存在");
		}

		// 获取实际退款金额
		Double actualRefundAmount = tgpf_RefundInfo.getActualRefundAmount();

		/*
		 * 更新托管账户表
		 * 计算：增加托管支出 减少活期余额
		 */
		Tgxy_BankAccountEscrowed theBankAccountEscrowed = tgpf_RefundInfo.getTheBankAccountEscrowed();
		// 托管支出
		Double payout = theBankAccountEscrowed.getPayout();
		// 活期余额
		Double currentBalance = theBankAccountEscrowed.getCurrentBalance();

		if (null == payout || payout == 0)
		{
			payout = actualRefundAmount;
		}
		else
		{
			payout = MyDouble.getInstance().doubleAddDouble(payout, actualRefundAmount);
		}

		currentBalance = MyDouble.getInstance().doubleSubtractDouble(currentBalance, actualRefundAmount);

		theBankAccountEscrowed.setPayout(payout);
		theBankAccountEscrowed.setCurrentBalance(currentBalance);

		tgxy_BankAccountEscrowedDao.save(theBankAccountEscrowed);

		/*
		 * 更新楼幢账户表
		 * 计算：增加“已拨付金额（元）”、减少“已申请退款未拨付金额（元）”、减少“当前托管余额（元）”、减少“溢出金额（元）
		 */
		// 查询楼幢账户
		Empj_BuildingInfo building = tgpf_RefundInfo.getBuilding();

		if (building == null)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}

		Long buildingId = building.getTableId();

		if (buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingId'不能为空");
		}

		Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao.findById(buildingId);

		if (null == tgpj_BuildingAccount)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BuildingAccount(Id:" + buildingId + ")'不存在");
		}
		// 已拨付金额
		Double payoutAmount = tgpj_BuildingAccount.getPayoutAmount();
		// 已申请退款未拨付金额（元）
		Double appliedNoPayoutAmount = tgpj_BuildingAccount.getAppliedNoPayoutAmount();
		// 当前托管余额（元）
		Double currentEscrowFund = tgpj_BuildingAccount.getCurrentEscrowFund();
		// 溢出金额（元）
		Double spilloverAmount = tgpj_BuildingAccount.getSpilloverAmount();

		if (null == payoutAmount || payoutAmount == 0)
		{
			payoutAmount = actualRefundAmount;
		}
		else
		{
			payoutAmount = MyDouble.getInstance().doubleAddDouble(payoutAmount, actualRefundAmount);
		}

		appliedNoPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(appliedNoPayoutAmount, actualRefundAmount);

		currentEscrowFund = MyDouble.getInstance().doubleSubtractDouble(currentEscrowFund, actualRefundAmount);

		spilloverAmount = MyDouble.getInstance().doubleSubtractDouble(spilloverAmount, actualRefundAmount);

		tgpj_BuildingAccount.setPayoutAmount(payoutAmount);
		tgpj_BuildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		tgpj_BuildingAccount.setCurrentEscrowFund(currentEscrowFund);
		tgpj_BuildingAccount.setSpilloverAmount(spilloverAmount);

		tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

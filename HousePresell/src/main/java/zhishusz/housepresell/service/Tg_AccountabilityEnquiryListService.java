package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_AccountabilityEnquiryForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tg_AccountabilityEnquiryDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tg_AccountabilityEnquiry;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service : 按权责发生制查询利息情况统计表
 */
@Service
@Transactional
public class Tg_AccountabilityEnquiryListService
{
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	@Autowired
	private Tg_AccountabilityEnquiryDao tg_AccountabilityEnquiryDao;

	public Properties execute(Tg_AccountabilityEnquiryForm model)
	{
		Properties properties = new MyProperties();

		String loadTimeStart = model.getLoadTimeStart().trim();// 起始日期
		String expirationTimeEnd = model.getExpirationTimeEnd().trim();// 结束日期

		if (null == loadTimeStart || loadTimeStart.trim().isEmpty() || null == expirationTimeEnd
				|| expirationTimeEnd.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择权责日期");
		}

		Long inBankBranchId = 0L;
		Long bankBranchId = model.getBankBranchId();
		if (bankBranchId != null && bankBranchId > 0)
		{
			Emmp_BankBranch bankBranch = emmp_BankBranchDao.findById(bankBranchId);
			if (null != bankBranch)
			{
				inBankBranchId = bankBranchId;
			}
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try
		{
			list = tg_AccountabilityEnquiryDao.callFunction2(inBankBranchId, loadTimeStart, expirationTimeEnd);
		}
		catch (SQLException e)
		{
			list = new ArrayList<Map<String, Object>>();
			e.printStackTrace();
		}

		List<Tg_AccountabilityEnquiry> tg_AccountabilityEnquiryList = new ArrayList<Tg_AccountabilityEnquiry>();
		Tg_AccountabilityEnquiry po = new Tg_AccountabilityEnquiry();

		if (null != list && list.size() > 0)
		{
			for (Map<String, Object> map : list)
			{
				po = new Tg_AccountabilityEnquiry();
				po.setLoadTime((String) map.get("LOADTIME"));
				po.setAmountDeposited(Double.valueOf((String) map.get("AMOUNTDEPOSITED")));
				po.setBank((String) map.get("BANK"));
				po.setBankOfDeposit((String) map.get("BANKOFDEPOSIT"));
				po.setDepositCeilings((String) map.get("DEPOSITCEILINGS"));
				po.setDepositProperty((String) map.get("DEPOSITPROPERTY"));
				po.setEscrowAccount((String) map.get("ESCROWACCOUNT"));
				po.setEscrowAcountName((String) map.get("ESCROWACOUNTNAME"));
				po.setExpirationTime((String) map.get("EXPIRATIONTIME"));
				po.setFate(Integer.valueOf((String) map.get("FATE")));
				po.setInterest(Double.valueOf((String) map.get("INTEREST")));
				po.setInterestRate((String) map.get("INTERESTRATE"));
				po.setRecordDate((String) map.get("RECORDDATE"));
				po.setTableId(Long.valueOf((String) map.get("TABLEID")));

				tg_AccountabilityEnquiryList.add(po);

			}
		}

		properties.put("tg_AccountabilityEnquiryList", tg_AccountabilityEnquiryList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}
}

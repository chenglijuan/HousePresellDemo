package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：网银对账-单个对账方法
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlCompareAllDBService {

	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;

	MyDatetime myDatetime = MyDatetime.getInstance();


	public Properties execute(Tgpf_BalanceOfAccountForm model) {

		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		// 当传入空值时，取当天所有的记录，进行自动对账
		if (null == idArr || idArr.length == 0) {
			String billTimeStap = model.getBillTimeStamp();

			if (null == billTimeStap || billTimeStap.trim().isEmpty()) {
				billTimeStap = myDatetime
						.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));
				model.setBillTimeStamp(billTimeStap);
			}

			Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
			tgpf_BalanceOfAccountForm.setBillTimeStamp(billTimeStap);
			tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

			Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
					.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));

			List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
			if (totalCount > 0) {
				tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
						.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
				for (int i = 0; i < tgpf_BalanceOfAccountList.size(); i++) {
					Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(i);
					
					properties=tgpf_CyberBankStatementDtlDao.PreCompareofWY(tgpf_BalanceOfAccount.getTableId());	

				}

			}

		} else {
			for (int i = 0; i < idArr.length; i++) {

				Long tgpf_BalanceOfAccountId = idArr[i];
				Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount) tgpf_BalanceOfAccountDao
						.findById(tgpf_BalanceOfAccountId);
				properties=tgpf_CyberBankStatementDtlDao.PreCompareofWY(tgpf_BalanceOfAccount.getTableId());
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

}

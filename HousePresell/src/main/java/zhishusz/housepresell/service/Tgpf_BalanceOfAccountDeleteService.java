package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：对账列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountDeleteService
{
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;

	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_BalanceOfAccountId = model.getTableId();
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = (Tgpf_BalanceOfAccount)tgpf_BalanceOfAccountDao.findById(tgpf_BalanceOfAccountId);
		if(tgpf_BalanceOfAccount == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_BalanceOfAccount(Id:" + tgpf_BalanceOfAccountId + ")'不存在");
		}
		
		tgpf_BalanceOfAccount.setTheState(S_TheState.Deleted);
		tgpf_BalanceOfAccountDao.save(tgpf_BalanceOfAccount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

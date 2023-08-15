package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service单个删除：监管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BankAccountSupervisedDeleteService
{
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;

	public Properties execute(Tgpj_BankAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BankAccountSupervisedId = model.getTableId();
		Tgpj_BankAccountSupervised tgpj_BankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(tgpj_BankAccountSupervisedId);
		if(tgpj_BankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BankAccountSupervised(Id:" + tgpj_BankAccountSupervisedId + ")'不存在");
		}
		
//		tgpj_BankAccountSupervised.setTheState(S_TheState.Deleted);
		tgpj_BankAccountSupervised.setIsUsing(1);
		tgpj_BankAccountSupervisedDao.save(tgpj_BankAccountSupervised);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_ComAccountForm;
import zhishusz.housepresell.database.dao.Emmp_ComAccountDao;
import zhishusz.housepresell.database.po.Emmp_ComAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：机构-财务账号信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_ComAccountDetailService
{
	@Autowired
	private Emmp_ComAccountDao emmp_ComAccountDao;

	public Properties execute(Emmp_ComAccountForm model)
	{
		Properties properties = new MyProperties();

		Long emmp_ComAccountId = model.getTableId();
		Emmp_ComAccount emmp_ComAccount = (Emmp_ComAccount)emmp_ComAccountDao.findById(emmp_ComAccountId);
		if(emmp_ComAccount == null || S_TheState.Deleted.equals(emmp_ComAccount.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Emmp_ComAccount(Id:" + emmp_ComAccountId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("emmp_ComAccount", emmp_ComAccount);

		return properties;
	}
}

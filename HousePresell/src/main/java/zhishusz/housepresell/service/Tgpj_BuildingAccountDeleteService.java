package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：楼幢账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAccountDeleteService
{
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

	public Properties execute(Tgpj_BuildingAccountForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BuildingAccountId = model.getTableId();
		Tgpj_BuildingAccount tgpj_BuildingAccount = (Tgpj_BuildingAccount)tgpj_BuildingAccountDao.findById(tgpj_BuildingAccountId);
		if(tgpj_BuildingAccount == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BuildingAccount(Id:" + tgpj_BuildingAccountId + ")'不存在");
		}
		
		tgpj_BuildingAccount.setTheState(S_TheState.Deleted);
		tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BankAccountEscrowedDetailService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;

	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_BankAccountEscrowedId = model.getTableId();
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(tgxy_BankAccountEscrowedId);
		if(tgxy_BankAccountEscrowed == null || S_TheState.Deleted.equals(tgxy_BankAccountEscrowed.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgxy_BankAccountEscrowed(Id:" + tgxy_BankAccountEscrowedId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgxy_BankAccountEscrowed", tgxy_BankAccountEscrowed);

		return properties;
	}
}

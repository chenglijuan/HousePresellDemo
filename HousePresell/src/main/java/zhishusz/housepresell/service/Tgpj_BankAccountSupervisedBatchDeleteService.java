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
 * Service批量删除：监管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BankAccountSupervisedBatchDeleteService
{
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;

	public Properties execute(Tgpj_BankAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpj_BankAccountSupervised tgpj_BankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(tableId);
			if(tgpj_BankAccountSupervised == null)
			{
				return MyBackInfo.fail(properties, "'Tgpj_BankAccountSupervised(Id:" + tableId + ")'不存在");
			}
		
//			tgpj_BankAccountSupervised.setTheState(S_TheState.Deleted);
			tgpj_BankAccountSupervised.setIsUsing(1);
			tgpj_BankAccountSupervisedDao.save(tgpj_BankAccountSupervised);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

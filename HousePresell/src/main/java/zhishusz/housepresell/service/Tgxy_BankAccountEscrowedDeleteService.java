package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service单个删除：托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BankAccountEscrowedDeleteService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgpf_DepositDetailDao depositDetailDao;

	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();

		Long tgxy_BankAccountEscrowedId = model.getTableId();

		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(tgxy_BankAccountEscrowedId);
		if(tgxy_BankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_BankAccountEscrowed(Id:" + tgxy_BankAccountEscrowedId + ")'不存在");
		}
		if (judgeIsAccountLinked(tgxy_BankAccountEscrowedId)){
			return MyBackInfo.fail(properties, "数据已经被引用，无法删除！");
		}
		tgxy_BankAccountEscrowed.setTheState(S_TheState.Deleted);
		tgxy_BankAccountEscrowedDao.save(tgxy_BankAccountEscrowed);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	public boolean judgeIsAccountLinked(Long tgxy_BankAccountEscrowedId) {
		Tgpf_DepositDetailForm tgpf_depositDetailForm = new Tgpf_DepositDetailForm();
		tgpf_depositDetailForm.setTheState(S_TheState.Normal);
		tgpf_depositDetailForm.setBankAccountEscrowedId(tgxy_BankAccountEscrowedId);
		Integer depositDetailSize = depositDetailDao.findByPage_Size(depositDetailDao.getQuery_Size(depositDetailDao.getBasicHQL(), tgpf_depositDetailForm));
		if (depositDetailSize > 0) {
			return true;
		}
		return false;
	}
}

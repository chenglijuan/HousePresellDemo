package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
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
 * Service批量删除：托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BankAccountEscrowedBatchDeleteService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDeleteService deleteService;

	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}


		for(Long tableId : idArr)
		{
			Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = (Tgxy_BankAccountEscrowed)tgxy_BankAccountEscrowedDao.findById(tableId);
			if(tgxy_BankAccountEscrowed == null)
			{
				return MyBackInfo.fail(properties, "'Tgxy_BankAccountEscrowed(Id:" + tableId + ")'不存在");
			}
			boolean isLinked = deleteService.judgeIsAccountLinked(tableId);
			if(isLinked){
				return MyBackInfo.fail(properties, "托管账户"+tgxy_BankAccountEscrowed.getTheAccount()+"数据已经被引用，无法删除！");
			}

			tgxy_BankAccountEscrowed.setTheState(S_TheState.Deleted);
			tgxy_BankAccountEscrowedDao.save(tgxy_BankAccountEscrowed);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_DepositManagementBatchDeleteService
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;

	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_DepositManagement tg_DepositManagement = (Tg_DepositManagement)tg_DepositManagementDao.findById(tableId);
			if(tg_DepositManagement == null)
			{
				return MyBackInfo.fail(properties, "'Tg_DepositManagement(Id:" + tableId + ")'不存在");
			}
		
			tg_DepositManagement.setTheState(S_TheState.Deleted);
			tg_DepositManagementDao.save(tg_DepositManagement);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

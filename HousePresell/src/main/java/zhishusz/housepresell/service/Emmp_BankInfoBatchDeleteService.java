package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_BankInfoForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_BankInfoBatchDeleteService
{
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;

	public Properties execute(Emmp_BankInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Emmp_BankInfo emmp_BankInfo = (Emmp_BankInfo)emmp_BankInfoDao.findById(tableId);
			if(emmp_BankInfo == null)
			{
				return MyBackInfo.fail(properties, "'Emmp_BankInfo(Id:" + tableId + ")'不存在");
			}
		
			emmp_BankInfo.setTheState(S_TheState.Deleted);
			emmp_BankInfoDao.save(emmp_BankInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

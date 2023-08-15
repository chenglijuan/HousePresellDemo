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
 * Service详情：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_BankInfoDetailService
{
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;

	public Properties execute(Emmp_BankInfoForm model)
	{
		Properties properties = new MyProperties();

		Long emmp_BankInfoId = model.getTableId();
		Emmp_BankInfo emmp_BankInfo = (Emmp_BankInfo)emmp_BankInfoDao.findById(emmp_BankInfoId);
		if(emmp_BankInfo == null || S_TheState.Deleted.equals(emmp_BankInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Emmp_BankInfo(Id:" + emmp_BankInfoId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("emmp_BankInfo", emmp_BankInfo);

		return properties;
	}
}

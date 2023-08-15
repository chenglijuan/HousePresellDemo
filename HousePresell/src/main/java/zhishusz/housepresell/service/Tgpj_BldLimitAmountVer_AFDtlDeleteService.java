package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：受限额度设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BldLimitAmountVer_AFDtlDeleteService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;

	public Properties execute(Tgpj_BldLimitAmountVer_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BldLimitAmountVer_AFDtlId = model.getTableId();
		Tgpj_BldLimitAmountVer_AFDtl tgpj_BldLimitAmountVer_AFDtl = (Tgpj_BldLimitAmountVer_AFDtl)tgpj_BldLimitAmountVer_AFDtlDao.findById(tgpj_BldLimitAmountVer_AFDtlId);
		if(tgpj_BldLimitAmountVer_AFDtl == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BldLimitAmountVer_AFDtl(Id:" + tgpj_BldLimitAmountVer_AFDtlId + ")'不存在");
		}
		
		tgpj_BldLimitAmountVer_AFDtl.setTheState(S_TheState.Deleted);
		tgpj_BldLimitAmountVer_AFDtlDao.save(tgpj_BldLimitAmountVer_AFDtl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

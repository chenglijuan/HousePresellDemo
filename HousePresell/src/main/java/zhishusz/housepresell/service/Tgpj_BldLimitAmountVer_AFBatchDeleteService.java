package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BldLimitAmountVer_AFBatchDeleteService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;

	public Properties execute(Tgpj_BldLimitAmountVer_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpj_BldLimitAmountVer_AF tgpj_BldLimitAmountVer_AF = (Tgpj_BldLimitAmountVer_AF)tgpj_BldLimitAmountVer_AFDao.findById(tableId);
			if(tgpj_BldLimitAmountVer_AF == null)
			{
				return MyBackInfo.fail(properties, "'Tgpj_BldLimitAmountVer_AF(Id:" + tableId + ")'不存在");
			}
		
			tgpj_BldLimitAmountVer_AF.setTheState(S_TheState.Deleted);
			tgpj_BldLimitAmountVer_AFDao.save(tgpj_BldLimitAmountVer_AF);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

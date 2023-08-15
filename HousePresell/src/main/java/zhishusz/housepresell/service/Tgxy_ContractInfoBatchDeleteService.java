package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：预售系统买卖合同
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_ContractInfoBatchDeleteService
{
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;

	public Properties execute(Tgxy_ContractInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgxy_ContractInfo tgxy_ContractInfo = (Tgxy_ContractInfo)tgxy_ContractInfoDao.findById(tableId);
			if(tgxy_ContractInfo == null)
			{
				return MyBackInfo.fail(properties, "'Tgxy_ContractInfo(Id:" + tableId + ")'不存在");
			}
		
			tgxy_ContractInfo.setTheState(S_TheState.Deleted);
			tgxy_ContractInfoDao.save(tgxy_ContractInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

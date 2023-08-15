package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：买受人信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BuyerInfoBatchDeleteService
{
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;

	public Properties execute(Tgxy_BuyerInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgxy_BuyerInfo tgxy_BuyerInfo = (Tgxy_BuyerInfo)tgxy_BuyerInfoDao.findById(tableId);
			if(tgxy_BuyerInfo == null)
			{
				return MyBackInfo.fail(properties, "'Tgxy_BuyerInfo(Id:" + tableId + ")'不存在");
			}
		
			tgxy_BuyerInfo.setTheState(S_TheState.Deleted);
			tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

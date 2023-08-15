package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service用于判断楼幢对应的备案均价是否已经添加（是否是唯一的）
 * Keyword:楼幢 备案均价 唯一 不可重复
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAvgPriceIsUniqueService
{
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;

	public Properties execute(Tgpj_BuildingAvgPriceForm model)
	{
		Properties properties = new MyProperties();

		boolean uniqueBuilding = tgpj_BuildingAvgPriceDao.isUniqueBuilding(model);

//		Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = (Tgpj_BuildingAvgPrice)tgpj_BuildingAvgPriceDao.findById(tgpj_BuildingAvgPriceId);
//		if(tgpj_BuildingAvgPrice == null || S_TheState.Deleted.equals(tgpj_BuildingAvgPrice.getTheState()))
//		{
//			return MyBackInfo.fail(properties, "'Tgpj_BuildingAvgPrice(Id:" + tgpj_BuildingAvgPriceId + ")'不存在");
//		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("isUnique", uniqueBuilding);

		return properties;
	}
}

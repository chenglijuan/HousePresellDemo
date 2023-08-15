package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service单个删除：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAvgPriceDeleteService
{
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpj_BuildingAvgPriceForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BuildingAvgPriceId = model.getTableId();
		Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = (Tgpj_BuildingAvgPrice)tgpj_BuildingAvgPriceDao.findById(tgpj_BuildingAvgPriceId);
		if(tgpj_BuildingAvgPrice == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BuildingAvgPrice(Id:" + tgpj_BuildingAvgPriceId + ")'不存在");
		}
		
		tgpj_BuildingAvgPrice.setTheState(S_TheState.Deleted);
		tgpj_BuildingAvgPriceDao.save(tgpj_BuildingAvgPrice);
		//删除审批流
		deleteService.execute(tgpj_BuildingAvgPriceId, S_BusiCode.busiCode_03010301);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：基础数据-城市区域
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_CityRegionInfoBatchDeleteService
{
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	public Properties execute(Sm_CityRegionInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_CityRegionInfo sm_CityRegionInfo = (Sm_CityRegionInfo)sm_CityRegionInfoDao.findById(tableId);
			if(sm_CityRegionInfo == null)
			{
				return MyBackInfo.fail(properties, "'Sm_CityRegionInfo(Id:" + tableId + ")'不存在");
			}
		
			sm_CityRegionInfo.setTheState(S_TheState.Deleted);
			sm_CityRegionInfoDao.save(sm_CityRegionInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

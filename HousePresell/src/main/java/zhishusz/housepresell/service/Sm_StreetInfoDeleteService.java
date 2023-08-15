package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_StreetInfoForm;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：Sm_StreetInfo
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_StreetInfoDeleteService
{
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;

	public Properties execute(Sm_StreetInfoForm model)
	{
		Properties properties = new MyProperties();

		Long sm_StreetInfoId = model.getTableId();
		Sm_StreetInfo sm_StreetInfo = (Sm_StreetInfo)sm_StreetInfoDao.findById(sm_StreetInfoId);
		if(sm_StreetInfo == null)
		{
			return MyBackInfo.fail(properties, "'Sm_StreetInfo(Id:" + sm_StreetInfoId + ")'不存在");
		}
		
		sm_StreetInfo.setTheState(S_TheState.Deleted);
		sm_StreetInfoDao.save(sm_StreetInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

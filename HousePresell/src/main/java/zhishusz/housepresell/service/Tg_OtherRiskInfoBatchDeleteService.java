package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_OtherRiskInfoForm;
import zhishusz.housepresell.database.dao.Tg_OtherRiskInfoDao;
import zhishusz.housepresell.database.po.Tg_OtherRiskInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：其他风险信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_OtherRiskInfoBatchDeleteService
{
	@Autowired
	private Tg_OtherRiskInfoDao tg_OtherRiskInfoDao;

	public Properties execute(Tg_OtherRiskInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tg_OtherRiskInfo tg_OtherRiskInfo = (Tg_OtherRiskInfo)tg_OtherRiskInfoDao.findById(tableId);
			if(tg_OtherRiskInfo == null)
			{
				return MyBackInfo.fail(properties, "记录不存在");
			}
			tg_OtherRiskInfo.setUserStart(model.getUser());
			tg_OtherRiskInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
			tg_OtherRiskInfo.setIsUsed(false);
			tg_OtherRiskInfo.setTheState(S_TheState.Deleted);
			tg_OtherRiskInfoDao.save(tg_OtherRiskInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

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
 * Service单个删除：其他风险信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_OtherRiskInfoDeleteService
{
	@Autowired
	private Tg_OtherRiskInfoDao tg_OtherRiskInfoDao;

	public Properties execute(Tg_OtherRiskInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tg_OtherRiskInfoId = model.getTableId();
		Tg_OtherRiskInfo tg_OtherRiskInfo = (Tg_OtherRiskInfo)tg_OtherRiskInfoDao.findById(tg_OtherRiskInfoId);
		if(tg_OtherRiskInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tg_OtherRiskInfo(Id:" + tg_OtherRiskInfoId + ")'不存在");
		}
		
		tg_OtherRiskInfo.setTheState(S_TheState.Deleted);
		tg_OtherRiskInfoDao.save(tg_OtherRiskInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

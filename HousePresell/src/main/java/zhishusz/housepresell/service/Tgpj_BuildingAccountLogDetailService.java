package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢账户Log表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAccountLogDetailService
{
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;

	public Properties execute(Tgpj_BuildingAccountLogForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BuildingAccountLogId = model.getTableId();
		Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = (Tgpj_BuildingAccountLog)tgpj_BuildingAccountLogDao.findById(tgpj_BuildingAccountLogId);
		if(tgpj_BuildingAccountLog == null || S_TheState.Deleted.equals(tgpj_BuildingAccountLog.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpj_BuildingAccountLog(Id:" + tgpj_BuildingAccountLogId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpj_BuildingAccountLog", tgpj_BuildingAccountLog);

		return properties;
	}
}

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
 * Service批量删除：楼幢账户Log表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAccountLogBatchDeleteService
{
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;

	public Properties execute(Tgpj_BuildingAccountLogForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = (Tgpj_BuildingAccountLog)tgpj_BuildingAccountLogDao.findById(tableId);
			if(tgpj_BuildingAccountLog == null)
			{
				return MyBackInfo.fail(properties, "'Tgpj_BuildingAccountLog(Id:" + tableId + ")'不存在");
			}
		
			tgpj_BuildingAccountLog.setTheState(S_TheState.Deleted);
			tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

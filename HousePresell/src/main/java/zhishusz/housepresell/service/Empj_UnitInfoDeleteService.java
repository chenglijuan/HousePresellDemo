package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_UnitInfoDeleteService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;

	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_UnitInfoId = model.getTableId();
		Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);
		if(empj_UnitInfo == null)
		{
			return MyBackInfo.fail(properties, "'Empj_UnitInfo(Id:" + empj_UnitInfoId + ")'不存在");
		}
		
		empj_UnitInfo.setTheState(S_TheState.Deleted);
		empj_UnitInfoDao.save(empj_UnitInfo);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

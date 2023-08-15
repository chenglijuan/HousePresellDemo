package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingExtendInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢-扩展信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingExtendInfoDetailService
{
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;

	public Properties execute(Empj_BuildingExtendInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BuildingExtendInfoId = model.getTableId();
		Empj_BuildingExtendInfo empj_BuildingExtendInfo = (Empj_BuildingExtendInfo)empj_BuildingExtendInfoDao.findById(empj_BuildingExtendInfoId);
		if(empj_BuildingExtendInfo == null || S_TheState.Deleted.equals(empj_BuildingExtendInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingExtendInfo(Id:" + empj_BuildingExtendInfoId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_BuildingExtendInfo", empj_BuildingExtendInfo);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoDetailService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;

	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_HouseInfoId = model.getTableId();
		Empj_HouseInfo empj_HouseInfo = (Empj_HouseInfo)empj_HouseInfoDao.findById(empj_HouseInfoId);
		if(empj_HouseInfo == null || S_TheState.Deleted.equals(empj_HouseInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_HouseInfo(Id:" + empj_HouseInfoId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_HouseInfo", empj_HouseInfo);

		return properties;
	}
}

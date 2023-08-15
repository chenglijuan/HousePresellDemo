package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfo_AFDetailService
{
	@Autowired
	private Empj_BuildingInfo_AFDao empj_BuildingInfo_AFDao;

	public Properties execute(Empj_BuildingInfo_AFForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BuildingInfo_AFId = model.getTableId();
		Empj_BuildingInfo_AF empj_BuildingInfo_AF = (Empj_BuildingInfo_AF)empj_BuildingInfo_AFDao.findById(empj_BuildingInfo_AFId);
		if(empj_BuildingInfo_AF == null || S_TheState.Deleted.equals(empj_BuildingInfo_AF.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingInfo_AF(Id:" + empj_BuildingInfo_AFId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_BuildingInfo_AF", empj_BuildingInfo_AF);

		return properties;
	}
}

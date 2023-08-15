package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼盘表详情
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingTableDetailService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_ProjectInfoId = model.getTableId();
		Empj_ProjectInfo empj_ProjectInfo = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(empj_ProjectInfoId);
		if(empj_ProjectInfo == null || S_TheState.Deleted.equals(empj_ProjectInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_ProjectInfo(Id:" + empj_ProjectInfoId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_ProjectInfo", empj_ProjectInfo);

		return properties;
	}
}

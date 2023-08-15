package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingAccountSupervisedDetailService
{
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_BuildingAccountSupervisedDao;

	public Properties execute(Empj_BuildingAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BuildingAccountSupervisedId = model.getTableId();
		Empj_BuildingAccountSupervised empj_BuildingAccountSupervised = (Empj_BuildingAccountSupervised)empj_BuildingAccountSupervisedDao.findById(empj_BuildingAccountSupervisedId);
		if(empj_BuildingAccountSupervised == null || S_TheState.Deleted.equals(empj_BuildingAccountSupervised.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingAccountSupervised(Id:" + empj_BuildingAccountSupervisedId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_BuildingAccountSupervised", empj_BuildingAccountSupervised);

		return properties;
	}
}

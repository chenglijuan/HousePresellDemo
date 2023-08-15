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
 * Service批量删除：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingAccountSupervisedBatchDeleteService
{
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_BuildingAccountSupervisedDao;

	public Properties execute(Empj_BuildingAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_BuildingAccountSupervised empj_BuildingAccountSupervised = (Empj_BuildingAccountSupervised)empj_BuildingAccountSupervisedDao.findById(tableId);
			if(empj_BuildingAccountSupervised == null)
			{
				return MyBackInfo.fail(properties, "'Empj_BuildingAccountSupervised(Id:" + tableId + ")'不存在");
			}
		
			empj_BuildingAccountSupervised.setTheState(S_TheState.Deleted);
			empj_BuildingAccountSupervisedDao.save(empj_BuildingAccountSupervised);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

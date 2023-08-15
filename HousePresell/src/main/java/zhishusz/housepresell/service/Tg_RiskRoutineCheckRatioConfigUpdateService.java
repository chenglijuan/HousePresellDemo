package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：风控例行抽查比例配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_RiskRoutineCheckRatioConfigUpdateService
{
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	public Properties execute(Tg_RiskRoutineCheckRatioConfigForm model)
	{
		Properties properties = new MyProperties();
		
		Tg_RiskRoutineCheckRatioConfigForm[] ratioConfigChangeFormList = model.getRatioConfigChangeFormList();
		
		if(ratioConfigChangeFormList == null || ratioConfigChangeFormList.length == 0)
		{
			return MyBackInfo.fail(properties, "请选择需要修改的抽查比例配置");
		}
		
		if(model.getUser() == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		for(Tg_RiskRoutineCheckRatioConfigForm ratioConfigChangeForm : ratioConfigChangeFormList)
		{
			Tg_RiskRoutineCheckRatioConfig ratioConfig = tg_RiskRoutineCheckRatioConfigDao.findByIdWithClear(ratioConfigChangeForm.getTableId());
			
			if(ratioConfigChangeForm.getTheRatio() < 0 || ratioConfigChangeForm.getTheRatio() > 100)
			{
				return MyBackInfo.fail(properties, "输入的比例不能大于100不能小于0");
			}
			
			if(
				//比例变了
				(ratioConfigChangeForm.getTheRatio() != null && !ratioConfigChangeForm.getTheRatio().equals(ratioConfig.getTheRatio()	))
				||
				//角色变了
				(ratioConfigChangeForm.getRoleId() != null && (ratioConfig.getRole() == null || !ratioConfigChangeForm.getRoleId().equals(ratioConfig.getRole().getTableId()))
			   )
			)
			{
				ratioConfig.setTheRatio(ratioConfigChangeForm.getTheRatio());
				ratioConfig.setRole(sm_Permission_RoleDao.findById(ratioConfigChangeForm.getRoleId()));
				ratioConfig.setUserUpdate(model.getUser());
				ratioConfig.setLastUpdateTimeStamp(System.currentTimeMillis());
				
				tg_RiskRoutineCheckRatioConfigDao.update(ratioConfig);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

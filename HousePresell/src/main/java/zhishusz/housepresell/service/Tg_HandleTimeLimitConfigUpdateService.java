package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_HandleTimeLimitConfigForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_HandleTimeLimitConfigDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service更新操作：办理时限配置表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_HandleTimeLimitConfigUpdateService
{
	@Autowired
	private Tg_HandleTimeLimitConfigDao tg_HandleTimeLimitConfigDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	public Properties execute(Tg_HandleTimeLimitConfigForm model)
	{
		Properties properties = new MyProperties();
		
		Long userId = model.getUserId();
		
		if(userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "'userId'不能为空");
		}
		Sm_User user = (Sm_User)sm_UserDao.findById(userId);
		if(user == null)
		{
			return MyBackInfo.fail(properties, "'user(Id:" + userId + ")'不存在");
		}

		Tg_HandleTimeLimitConfigForm[] tg_HandleTimeLimitConfigs = model.getTg_HandleTimeLimitConfigs();
		if (tg_HandleTimeLimitConfigs != null) {
			for (Tg_HandleTimeLimitConfigForm tg_HandleTimeLimitConfigTemp : tg_HandleTimeLimitConfigs) {
				Tg_HandleTimeLimitConfig tg_HandleTimeLimitConfig = tg_HandleTimeLimitConfigDao.findById(tg_HandleTimeLimitConfigTemp.getTableId());
				if (tg_HandleTimeLimitConfig == null) {
					return MyBackInfo.fail(properties, "'handleTimeLimitConfig(Id:" + tg_HandleTimeLimitConfigTemp.getTableId() + ")'不存在");
				}
				if (tg_HandleTimeLimitConfigTemp.getLimitDayNumber() == null)
				{
					return MyBackInfo.fail(properties, "请填写时限天数！");
				}
				if ((tg_HandleTimeLimitConfig.getLimitDayNumber().intValue() != tg_HandleTimeLimitConfigTemp.getLimitDayNumber().intValue())
						||
						(tg_HandleTimeLimitConfigTemp.getRoleId() != null && (tg_HandleTimeLimitConfig.getRole() == null || !tg_HandleTimeLimitConfigTemp.getRoleId().equals(tg_HandleTimeLimitConfig.getRole().getTableId()))))
				{
					tg_HandleTimeLimitConfig.setLimitDayNumber(tg_HandleTimeLimitConfigTemp.getLimitDayNumber());
					tg_HandleTimeLimitConfig.setRole(sm_Permission_RoleDao.findById(tg_HandleTimeLimitConfigTemp.getRoleId()));
					tg_HandleTimeLimitConfig.setLastCfgUser(user.getTheName());
					tg_HandleTimeLimitConfig.setLastCfgTimeStamp(System.currentTimeMillis());
					tg_HandleTimeLimitConfigDao.update(tg_HandleTimeLimitConfig);
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

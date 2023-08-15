package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量推送：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_DepositManagementTsService
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;

	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要推送的信息");
		}

		StringBuffer sb = new StringBuffer();
		
		for(int i = 0;i<idArr.length;i++)
//		for(Long tableId : idArr)
		{
			Tg_DepositManagement tg_DepositManagement = (Tg_DepositManagement)tg_DepositManagementDao.findById(idArr[i]);
			if(tg_DepositManagement == null)
			{
				return MyBackInfo.fail(properties, "'Tg_DepositManagement(Id:" + idArr[i] + ")'不存在");
			}
		
			if(i == 0)
			{
				sb.append(String.valueOf(idArr[i]));
			}
			else
			{
				sb.append(","+String.valueOf(idArr[i]));
			}
			
		}
		
		//存单推送
		try {
			Map<String, Object> map = tg_DepositManagementDao.prc_Insert_Cdgl(sb.toString(), String.valueOf(model.getUser().getTheName()));
			
			if(S_NormalFlag.fail.equals(map.get("sign")))
			{
				return MyBackInfo.fail(properties, (String) map.get("info"));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return MyBackInfo.fail(properties, "操作失败，请稍后重试！");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

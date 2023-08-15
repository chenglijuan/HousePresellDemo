package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_MessageTemplate_CfgForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_MessageTemplate_CfgDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Properties;

/*
 * Service批量删除：审批流-消息模板配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_MessageTemplate_CfgBatchDeleteService
{
	@Autowired
	private Sm_MessageTemplate_CfgDao sm_messageTemplate_cfgDao;

	public Properties execute(Sm_MessageTemplate_CfgForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_MessageTemplate_Cfg sm_messageTemplate_cfg = (Sm_MessageTemplate_Cfg)sm_messageTemplate_cfgDao.findById(tableId);
			if(sm_messageTemplate_cfg == null)
			{
				return MyBackInfo.fail(properties, "'Sm_MessageTemplate_Cfg(Id:" + tableId + ")'不存在");
			}

			sm_messageTemplate_cfg.setTheState(S_TheState.Deleted);
			sm_messageTemplate_cfgDao.save(sm_messageTemplate_cfg);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

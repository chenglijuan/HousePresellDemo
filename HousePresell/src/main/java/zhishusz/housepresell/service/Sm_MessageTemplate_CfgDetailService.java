package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.controller.form.Sm_MessageTemplate_CfgForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.dao.Sm_MessageTemplate_CfgDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
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
 * Service详情：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_MessageTemplate_CfgDetailService
{

	@Autowired
	private Sm_MessageTemplate_CfgDao sm_messageTemplate_cfgDao;

	public Properties execute(Sm_MessageTemplate_CfgForm model)
	{
		Properties properties = new MyProperties();

		Long sm_MessageTemplate_CfgId = model.getTableId();
		Sm_MessageTemplate_Cfg sm_MessageTemplate_CfgDetail = (Sm_MessageTemplate_Cfg)sm_messageTemplate_cfgDao.findById(sm_MessageTemplate_CfgId);
		if(sm_MessageTemplate_CfgDetail == null || S_TheState.Deleted.equals(sm_MessageTemplate_CfgDetail.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_MessageTemplate_Cfg(Id:" + sm_MessageTemplate_CfgId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_MessageTemplate_CfgDetail", sm_MessageTemplate_CfgDetail);

		return properties;
	}
}

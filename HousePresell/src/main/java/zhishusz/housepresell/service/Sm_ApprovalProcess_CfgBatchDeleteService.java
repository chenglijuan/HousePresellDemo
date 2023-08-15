package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_CfgBatchDeleteService
{
	@Autowired
	private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;

	public Properties execute(Sm_ApprovalProcess_CfgForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = (Sm_ApprovalProcess_Cfg)sm_ApprovalProcess_CfgDao.findById(tableId);
			if(sm_ApprovalProcess_Cfg == null)
			{
				return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Cfg(Id:" + tableId + ")'不存在");
			}
		
			sm_ApprovalProcess_Cfg.setTheState(S_TheState.Deleted);
			sm_ApprovalProcess_CfgDao.save(sm_ApprovalProcess_Cfg);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

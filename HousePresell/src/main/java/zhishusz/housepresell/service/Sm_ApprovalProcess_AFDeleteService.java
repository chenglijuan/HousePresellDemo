package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_AFDeleteService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;

	public Properties execute(Sm_ApprovalProcess_AFForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_AFId = model.getTableId();
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = (Sm_ApprovalProcess_AF)sm_ApprovalProcess_AFDao.findById(sm_ApprovalProcess_AFId);
		if(sm_ApprovalProcess_AF == null)
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_AF(Id:" + sm_ApprovalProcess_AFId + ")'不存在");
		}
		
		sm_ApprovalProcess_AF.setTheState(S_TheState.Deleted);
		sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

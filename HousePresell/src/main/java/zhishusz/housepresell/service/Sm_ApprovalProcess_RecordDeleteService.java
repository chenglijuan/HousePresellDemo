package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：审批流-审批记录
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_RecordDeleteService
{
	@Autowired
	private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;

	public Properties execute(Sm_ApprovalProcess_RecordForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_RecordId = model.getTableId();
		Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = (Sm_ApprovalProcess_Record)sm_ApprovalProcess_RecordDao.findById(sm_ApprovalProcess_RecordId);
		if(sm_ApprovalProcess_Record == null)
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_Record(Id:" + sm_ApprovalProcess_RecordId + ")'不存在");
		}
		
		sm_ApprovalProcess_Record.setTheState(S_TheState.Deleted);
		sm_ApprovalProcess_RecordDao.save(sm_ApprovalProcess_Record);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

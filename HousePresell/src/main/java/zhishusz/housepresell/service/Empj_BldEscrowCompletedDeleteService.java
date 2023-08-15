package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.state.S_BusiCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompletedDeleteService
{
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BldEscrowCompletedId = model.getTableId();
		String busiCode = model.getBusiCode();
		if(busiCode == null || busiCode.length() == 0)
		{
			busiCode = S_BusiCode.busiCode_03030102; //托管终止
		}
		Empj_BldEscrowCompleted empj_BldEscrowCompleted = (Empj_BldEscrowCompleted)empj_BldEscrowCompletedDao.findById(empj_BldEscrowCompletedId);
		if(empj_BldEscrowCompleted == null)
		{
			return MyBackInfo.fail(properties, "删除的托管终止不存在");
		}
		
		empj_BldEscrowCompleted.setTheState(S_TheState.Deleted);
		empj_BldEscrowCompletedDao.save(empj_BldEscrowCompleted);

		//删除审批流
		deleteService.execute(empj_BldEscrowCompletedId,busiCode);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

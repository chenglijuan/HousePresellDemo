package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.util.project.IsNeedBackupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_EscrowStandardVerMngDetailService
{
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao approvalProcessAFDao;
	@Autowired
	private IsNeedBackupUtil isNeedBackupUtil;

	public Properties execute(Tgpj_EscrowStandardVerMngForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_EscrowStandardVerMngId = model.getTableId();
		Tgpj_EscrowStandardVerMng tgpj_EscrowStandardVerMng = (Tgpj_EscrowStandardVerMng)tgpj_EscrowStandardVerMngDao.findById(tgpj_EscrowStandardVerMngId);
		// || S_TheState.Deleted.equals(tgpj_EscrowStandardVerMng.getTheState())
		if(tgpj_EscrowStandardVerMng == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_EscrowStandardVerMng(Id:" + tgpj_EscrowStandardVerMngId + ")'不存在");
		}

		if (model.getAfId() != null)
		{
			Sm_ApprovalProcess_AF byId = approvalProcessAFDao.findById(model.getAfId());
			isNeedBackupUtil.setIsNeedBackup(properties,byId);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpj_EscrowStandardVerMng", tgpj_EscrowStandardVerMng);

		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.state.S_BusiCode;
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
 * Service单个删除：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_EscrowStandardVerMngDeleteService
{
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpj_EscrowStandardVerMngForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_EscrowStandardVerMngId = model.getTableId();

		Tgpj_EscrowStandardVerMng tgpj_EscrowStandardVerMng = (Tgpj_EscrowStandardVerMng)tgpj_EscrowStandardVerMngDao.findById(tgpj_EscrowStandardVerMngId);
		if(tgpj_EscrowStandardVerMng == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_EscrowStandardVerMng(Id:" + tgpj_EscrowStandardVerMngId + ")'不存在");
		}
		
		tgpj_EscrowStandardVerMng.setTheState(S_TheState.Deleted);
		tgpj_EscrowStandardVerMngDao.save(tgpj_EscrowStandardVerMng);

		//删除审批流
		deleteService.execute(tgpj_EscrowStandardVerMngId, S_BusiCode.busiCode_06010101);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

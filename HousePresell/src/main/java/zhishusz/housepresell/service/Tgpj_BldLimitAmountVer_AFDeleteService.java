package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service单个删除：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BldLimitAmountVer_AFDeleteService
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpj_BldLimitAmountVer_AFForm model)
	{
		Properties properties = new MyProperties();

		Long tgpj_BldLimitAmountVer_AFId = model.getTableId();
		Tgpj_BldLimitAmountVer_AF tgpj_BldLimitAmountVer_AF = (Tgpj_BldLimitAmountVer_AF)tgpj_BldLimitAmountVer_AFDao.findById(tgpj_BldLimitAmountVer_AFId);
		if(tgpj_BldLimitAmountVer_AF == null)
		{
			return MyBackInfo.fail(properties, "'Tgpj_BldLimitAmountVer_AF(Id:" + tgpj_BldLimitAmountVer_AFId + ")'不存在");
		}
		
		tgpj_BldLimitAmountVer_AF.setTheState(S_TheState.Deleted);
		tgpj_BldLimitAmountVer_AFDao.save(tgpj_BldLimitAmountVer_AF);
		//删除审批流
		deleteService.execute(tgpj_BldLimitAmountVer_AFId, S_BusiCode.busiCode_06010102);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

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
 * Service批量删除：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_EscrowStandardVerMngBatchDeleteService
{
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgpj_EscrowStandardVerMngForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgpj_EscrowStandardVerMng tgpj_EscrowStandardVerMng = (Tgpj_EscrowStandardVerMng)tgpj_EscrowStandardVerMngDao.findById(tableId);
			if(tgpj_EscrowStandardVerMng == null)
			{
				return MyBackInfo.fail(properties, "'Tgpj_EscrowStandardVerMng(Id:" + tableId + ")'不存在");
			}
		
			tgpj_EscrowStandardVerMng.setTheState(S_TheState.Deleted);
			tgpj_EscrowStandardVerMngDao.save(tgpj_EscrowStandardVerMng);

			//删除审批流
			deleteService.execute(tgpj_EscrowStandardVerMng.getTableId(), S_BusiCode.busiCode_06010101);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

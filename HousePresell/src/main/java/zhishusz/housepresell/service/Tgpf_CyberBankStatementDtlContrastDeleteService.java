package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：网银对账详情列表-删除
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlContrastDeleteService
{

	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	
	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		for(int i = 0; i< idArr.length ; i++)
		{
			Long tgpf_CyberBankStatementDtlId = idArr[i];
			
			Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = (Tgpf_CyberBankStatementDtl)tgpf_CyberBankStatementDtlDao.findById(tgpf_CyberBankStatementDtlId);
			
			if(tgpf_CyberBankStatementDtl == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatementDtl(Id:" + tgpf_CyberBankStatementDtlId + ")'不存在");
			}
			
			tgpf_CyberBankStatementDtl.setTheState(S_TheState.Deleted);
			tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

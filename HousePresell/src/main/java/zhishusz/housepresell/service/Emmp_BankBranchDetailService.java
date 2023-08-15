package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_BankBranchDetailService
{
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	public Properties execute(Emmp_BankBranchForm model)
	{
		Properties properties = new MyProperties();

		Long emmp_BankBranchId = model.getTableId();
		Emmp_BankBranch emmp_BankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(emmp_BankBranchId);
		if(emmp_BankBranch == null || S_TheState.Deleted.equals(emmp_BankBranch.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Emmp_BankBranch(Id:" + emmp_BankBranchId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("emmp_BankBranch", emmp_BankBranch);

		return properties;
	}
}

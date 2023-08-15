package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.BankBranchUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service单个删除：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_BankBranchDeleteService
{
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private BankBranchUtil bankBranchUtil;

	public Properties execute(Emmp_BankBranchForm model)
	{
		Properties properties = new MyProperties();

		Long emmpBankInfoId = model.getTableId();
		Emmp_BankBranch emmp_bankBranch = emmp_BankBranchDao.findById(emmpBankInfoId);
		if(emmp_bankBranch == null)
		{
			return MyBackInfo.fail(properties, "'Emmp_BankBranch(Id:" + emmpBankInfoId + ")'不存在");
		}
		HashMap<String, Object> resultMap = bankBranchUtil.canDeleteThisBankBranck(emmpBankInfoId);
		boolean canDelete = (boolean) resultMap.get(bankBranchUtil.canDelete);
		if(!canDelete){
			return MyBackInfo.fail(properties, (String)(resultMap.get(bankBranchUtil.deleteInfo)));
		}

		emmp_bankBranch.setTheState(S_TheState.Deleted);
		emmp_BankBranchDao.save(emmp_bankBranch);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;


	}
}

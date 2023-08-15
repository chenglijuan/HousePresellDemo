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
 * Service批量删除：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_BankBranchBatchDeleteService
{
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	@Autowired
	private BankBranchUtil bankBranchUtil;

	public Properties execute(Emmp_BankBranchForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Emmp_BankBranch emmp_BankBranch = (Emmp_BankBranch)emmp_BankBranchDao.findById(tableId);
			if(emmp_BankBranch == null)
			{
				return MyBackInfo.fail(properties, "'Emmp_BankBranch(Id:" + tableId + ")'不存在");
			}
			HashMap<String, Object> resultMap = bankBranchUtil.canDeleteThisBankBranck(tableId);
			boolean canDelete = (boolean) resultMap.get(bankBranchUtil.canDelete);
			if(!canDelete){
				return MyBackInfo.fail(properties, (String)(resultMap.get(bankBranchUtil.deleteInfo)));
			}
		
			emmp_BankBranch.setTheState(S_TheState.Deleted);
			emmp_BankBranchDao.save(emmp_BankBranch);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

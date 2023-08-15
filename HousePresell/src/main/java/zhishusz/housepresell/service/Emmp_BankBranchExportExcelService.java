package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Emmp_BankBranchTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_BankBranchExportExcelService
{
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_BankBranchForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		List<Emmp_BankBranch> emmp_BankBranch;
		if (idArr == null || idArr.length < 1) 
		{
			emmp_BankBranch = emmp_BankBranchDao.findByPage(emmp_BankBranchDao.getQuery(emmp_BankBranchDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			emmp_BankBranch = emmp_BankBranchDao.findByPage(emmp_BankBranchDao.getQuery(emmp_BankBranchDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		Properties propertiesExport = exportToExcelUtil.execute(emmp_BankBranch, Emmp_BankBranchTemplate.class, "开户行列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("emmp_BankBranch", emmp_BankBranch);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

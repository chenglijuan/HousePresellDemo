package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Sm_Permission_RoleTemplate;

/*
 * Service列表查询：角色信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RoleExportExcelService
{
	@Autowired
	private Sm_Permission_RoleDao sm_Permission_RoleDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RoleForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String keyword = model.getKeyword();
		model.setKeyword("%"+keyword+"%");
		
		List<Sm_Permission_Role> sm_Permission_RoleList;
		if (idArr == null || idArr.length < 1) 
		{
			sm_Permission_RoleList = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getBasicHQL(), model), null, null);
		} 
		else
		{
			sm_Permission_RoleList = sm_Permission_RoleDao.findByPage(sm_Permission_RoleDao.getQuery(sm_Permission_RoleDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		
		Properties propertiesExport = exportToExcelUtil.execute(sm_Permission_RoleList, Sm_Permission_RoleTemplate.class, "角色列表信息");
		
		if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
		{
			return properties;
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

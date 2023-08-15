package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Sm_UserExportTemplate;

/*
 * Service列表导出：用户信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_UserExportExcelService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String keyword = Checker.getInstance().checkKeyword(model.getKeyword());
		model.setKeyword(keyword);
		model.setBusiState(model.getBusiState() == "" ? null : model.getBusiState());
		model.setLockUntil(System.currentTimeMillis());
		
		List<Sm_User> sm_UserList;
		if(idArr == null || idArr.length < 1)
		{
			sm_UserList = sm_UserDao.findByPage(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), model), null, null);
		}
		else
		{
			sm_UserList = sm_UserDao.findByPage(sm_UserDao.getQuery(sm_UserDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		
		Properties propertiesExport = exportToExcelUtil.execute(sm_UserList, Sm_UserExportTemplate.class, "用户列表信息");
		
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

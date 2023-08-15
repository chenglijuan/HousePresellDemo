package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Sm_Permission_RangeAuthorizationTemplate;

/*
 * Service列表查询：范围授权信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RangeAuthorizationExportExcelService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String keyword = Checker.getInstance().checkKeyword(model.getKeyword());
		model.setKeyword(keyword);
		
		String forCompanyType = (model.getForCompanyType() == null || model.getForCompanyType().length() == 0) ? null : model.getForCompanyType();
		model.setForCompanyType(forCompanyType);
		
		String authTimeStampRange = model.getAuthTimeStampRange();
		Long authTimeStampStart = myDatetime.getDateTimeStampMin(authTimeStampRange);
		Long authTimeStampEnd = myDatetime.getDateTimeStampMax(authTimeStampRange);
		model.setAuthStartTimeStamp(authTimeStampStart);
		model.setAuthEndTimeStamp(authTimeStampEnd);
		
		model.setTheState(S_TheState.Normal);
		
		List<Sm_Permission_RangeAuthorization> sm_Permission_RangeAuthorizationList;
		if (idArr == null || idArr.length < 1) 
		{
			sm_Permission_RangeAuthorizationList = sm_Permission_RangeAuthorizationDao.findByPage(sm_Permission_RangeAuthorizationDao.createCriteriaForList(model, null), null, null);
		} 
		else
		{
			sm_Permission_RangeAuthorizationList = sm_Permission_RangeAuthorizationDao.findByPage(sm_Permission_RangeAuthorizationDao.getQuery(sm_Permission_RangeAuthorizationDao.getExcelListHQL(), model), null, null);
		}
		
		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
		
		Properties propertiesExport = exportToExcelUtil.execute(sm_Permission_RangeAuthorizationList, Sm_Permission_RangeAuthorizationTemplate.class, "范围授权列表信息");
		
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

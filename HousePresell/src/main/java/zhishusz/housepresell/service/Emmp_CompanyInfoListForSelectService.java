package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：开发企业下拉列表信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_CompanyInfoListForSelectService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();

		if(model.getUser() == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		model.setApprovalState(S_ApprovalState.Completed);
		model.setTheState(S_TheState.Normal);
		model.setBusiState(S_BusiState.HaveRecord);
		model.setTheType(S_CompanyType.Development);
		
		List<Emmp_CompanyInfo> emmp_CompanyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQLForDevelop(), model));
		
		properties.put("emmp_CompanyInfoList", emmp_CompanyInfoList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

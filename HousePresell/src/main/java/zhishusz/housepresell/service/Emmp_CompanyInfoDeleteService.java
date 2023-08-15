package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.state.S_BusiCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_CompanyInfoDeleteService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;
	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();

		Long emmp_CompanyInfoId = model.getTableId();
		String busiCode = model.getBusiCode();
		if(busiCode == null || busiCode.length() == 0)
		{
			busiCode = S_BusiCode.busiCode_020101;
		}
		Emmp_CompanyInfo emmp_CompanyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(emmp_CompanyInfoId);
		if(emmp_CompanyInfo == null)
		{
			return MyBackInfo.fail(properties, "开发企业不存在");
		}
		
		emmp_CompanyInfo.setTheState(S_TheState.Deleted);
		emmp_CompanyInfoDao.save(emmp_CompanyInfo);

		//删除审批流
		deleteService.execute(emmp_CompanyInfoId,busiCode);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

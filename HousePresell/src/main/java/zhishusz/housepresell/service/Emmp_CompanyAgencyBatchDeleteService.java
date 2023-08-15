package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Properties;

/*
 * Service批量删除：代理公司
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_CompanyAgencyBatchDeleteService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		String busiCode = S_BusiCode.busiCode_020103;
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Emmp_CompanyInfo emmp_CompanyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(tableId);
			if(emmp_CompanyInfo == null)
			{
				return MyBackInfo.fail(properties, "代理公司不存在");
			}
		
			emmp_CompanyInfo.setTheState(S_TheState.Deleted);
			emmp_CompanyInfoDao.save(emmp_CompanyInfo);

			//删除审批流
			deleteService.execute(tableId,busiCode);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

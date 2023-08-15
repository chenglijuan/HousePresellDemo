package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：范围授权删除（普通机构）
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_Permission_RangeAuthorizationDeleteService
{
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;
	
	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		if(idArr == null || idArr.length == 0)
		{
			return MyBackInfo.fail(properties, "请选择范围授权信息");
		}
		
		for(Long id : idArr)
		{
			Sm_Permission_RangeAuthorization sm_Permission_RangeAuthorization = sm_Permission_RangeAuthorizationDao.findById(id);
			if(sm_Permission_RangeAuthorization == null)
			{
				throw new RoolBackException("该范围授权信息不存在");
			}
			
			//删除该机构原先所有的已经分配的数据
			sm_Permission_RangeDao.deleteBatch(null, sm_Permission_RangeAuthorization.getEmmp_CompanyInfo());
			
			sm_Permission_RangeAuthorization.setTheState(S_TheState.Deleted);
			sm_Permission_RangeAuthorizationDao.save(sm_Permission_RangeAuthorization);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RangeAuthorizationForm;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：范围授权列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_Permission_RangeAuthorizationDetailService
{
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationDao;
	@Autowired
	private Sm_CityRegionInfoForRangeAuthListService sm_CityRegionInfoForRangeAuthListService;
	
	public Properties execute(Sm_Permission_RangeAuthorizationForm model)
	{
		Properties properties = new MyProperties();
		
		Long tableId = model.getTableId();
		
		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择角色授权信息");
		}
		
		Sm_Permission_RangeAuthorization sm_Permission_RangeAuthorization = sm_Permission_RangeAuthorizationDao.findById(tableId);
		
		if(sm_Permission_RangeAuthorization == null)
		{
			return MyBackInfo.fail(properties, "该角色授权信息不存在");
		}
		
		Sm_CityRegionInfoForm sm_CityRegionInfoForm = new Sm_CityRegionInfoForm();
		sm_CityRegionInfoForm.setRangeAuthType(sm_Permission_RangeAuthorization.getRangeAuthType());
		Properties proTemp = sm_CityRegionInfoForRangeAuthListService.executeForSelect(sm_CityRegionInfoForm);

		properties.put("rangeAuthType", sm_Permission_RangeAuthorization.getRangeAuthType());
		properties.put("sm_CityRegionInfoList", proTemp.get("sm_CityRegionInfoList"));
		properties.put("sm_Permission_RangeAuthorization", sm_Permission_RangeAuthorization);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

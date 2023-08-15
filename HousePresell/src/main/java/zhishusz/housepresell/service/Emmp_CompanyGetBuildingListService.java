package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：系统用户+机构用户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_CompanyGetBuildingListService
{
	@Autowired
	private Emmp_CompanyInfoDao companyInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();
//		Long userId = model.getUserId();
		Long tableId = model.getTableId();

		//		Long sm_UserId = model.getTableId();
		Emmp_CompanyInfo company = (Emmp_CompanyInfo)companyInfoDao.findById(tableId);
		if(company!=null){
			Empj_BuildingInfoForm empj_buildingInfoForm = new Empj_BuildingInfoForm();
			empj_buildingInfoForm.setInterfaceVersion(19000101);
			empj_buildingInfoForm.setDevelopCompanyId(company.getTableId());
			List<Empj_BuildingInfo> buildingInfoList = empj_BuildingInfoDao
					.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_buildingInfoForm));
			properties.put("buildingList",buildingInfoList);
		}



		if(company == null || S_TheState.Deleted.equals(company.getTheState()))
		{
			return MyBackInfo.fail(properties, "开发机构不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//		properties.put("sm_User", sm_User);


		return properties;
	}
}

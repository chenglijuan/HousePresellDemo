package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
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
public class Sm_UserGetBuildingListService
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	public Properties execute(Sm_UserForm model)
	{
		Properties properties = new MyProperties();
		Long userId = model.getUserId();

//		Long sm_UserId = model.getTableId();
		Sm_User sm_User = (Sm_User)sm_UserDao.findById(userId);
		if(sm_User!=null){
			Emmp_CompanyInfo company = sm_User.getCompany();
			Empj_BuildingInfoForm empj_buildingInfoForm = new Empj_BuildingInfoForm();
			empj_buildingInfoForm.setInterfaceVersion(19000101);
			empj_buildingInfoForm.setDevelopCompanyId(company.getTableId());
			List<Empj_BuildingInfo> buildingInfoList = empj_BuildingInfoDao
					.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_buildingInfoForm));
			properties.put("buildingList",buildingInfoList);
		}



		if(sm_User == null || S_TheState.Deleted.equals(sm_User.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_User(Id:" + userId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//		properties.put("sm_User", sm_User);


		return properties;
	}
}

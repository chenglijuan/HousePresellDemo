package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：项目风险评估-新增查询
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_PjRiskAssessmentAddQueryService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskAssessmentForm model)
	{
		Properties properties = new MyProperties();

		// 根据企业Id查询项目和区域
		Long developCompanyId = model.getDevelopCompanyId();// 企业Id

		Long projectId = model.getProjectId();

		if (null != developCompanyId && developCompanyId > 1)
		{
			Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(developCompanyId);

			if (emmp_CompanyInfo == null)
			{
				return MyBackInfo.fail(properties, "未查询到有效的开发企业，请核查询条件");
			}

			// 查询项目
			Empj_ProjectInfoForm form2 = new Empj_ProjectInfoForm();
			form2.setTheState(S_TheState.Normal);
			form2.setDevelopCompany(emmp_CompanyInfo);
			List<Empj_ProjectInfo> projectList = empj_ProjectInfoDao
					.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), model));
			if (projectList == null || projectList.size() == 0)
			{
				projectList = new ArrayList<Empj_ProjectInfo>();
			}

			// 查询区域
			Sm_CityRegionInfo cityRegion = emmp_CompanyInfo.getCityRegion();
			if (cityRegion == null)
			{
				cityRegion = new Sm_CityRegionInfo();
			}

			properties.put("projectList", projectList);
			properties.put("companyInfo", emmp_CompanyInfo);
		}

		if (null != projectId && projectId > 1)
		{
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(projectId);
			
			if (projectInfo == null)
			{
				return MyBackInfo.fail(properties, "未查询到有效的项目");
			}
			
			Sm_CityRegionInfo cityRegion = projectInfo.getCityRegion();
			if(null == cityRegion){
				return MyBackInfo.fail(properties, "该项目没有关联区域信息，请核对");
			}
			
			properties.put("cityRegion", cityRegion);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

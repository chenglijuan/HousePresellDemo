package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tg_WitnessStatisticsForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：见证报告统计表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_WitnessStatisticsbyareaService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_WitnessStatisticsForm model)
	{
		Properties properties = new MyProperties();
		Emmp_CompanyInfoForm emmp_CompanyInfoForm = new Emmp_CompanyInfoForm();
		emmp_CompanyInfoForm.setTheType(S_CompanyType.Witness);
		emmp_CompanyInfoForm.setTheState(S_TheState.Normal);

		List<Emmp_CompanyInfo> emmp_CompanyInfoList = emmp_CompanyInfoDao
				.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), emmp_CompanyInfoForm));

		if (null == emmp_CompanyInfoList)
		{
			emmp_CompanyInfoList = new ArrayList<>();
		}

		List<Empj_ProjectInfo> empj_ProjectInfolist = new ArrayList<Empj_ProjectInfo>();
		if (model != null)
		{
			Long tableId = model.getTableId();
			if (null != tableId)
			{
				Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(tableId);
				if (sm_CityRegionInfo != null)
				{
					// 获取域下的项目名称
					Empj_ProjectInfoForm empj_ProjectInfoForm = new Empj_ProjectInfoForm();
					empj_ProjectInfoForm.setCityRegion(sm_CityRegionInfo);
					empj_ProjectInfolist = empj_ProjectInfoDao
							.getQuery(empj_ProjectInfoDao.getBasicHQL(), empj_ProjectInfoForm).getResultList();
				}
			}
		}

		properties.put("emmp_CompanyInfoList", emmp_CompanyInfoList);
		properties.put("empj_ProjectInfolist", empj_ProjectInfolist);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

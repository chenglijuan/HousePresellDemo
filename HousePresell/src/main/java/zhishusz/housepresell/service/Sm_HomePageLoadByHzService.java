package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;

/**
 * 首页展示加载-合作机构首页
 * 
 * @ClassName: Sm_HomePageLoadByHzService
 * @Description:TODO
 * @author: yangyu
 * @date: 2019年1月10日
 * @version V1.0
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_HomePageLoadByHzService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@Autowired
	private Empj_ProjectInfoRebuild rebuild;

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 查询默认地图坐标

	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;// 风险评级轮播

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	@SuppressWarnings({
			"unchecked"
	})
	public Properties execute(Tb_b_contractFrom model)
	{
		Properties properties = new Properties();

		// model.setCityRegionId(78L);

		/**
		 * 获取登陆用户进行判断登陆用户是否是合作机构用户
		 * 是 获取代理机构下的所有的项目并查询
		 * 否 返回提示消息
		 */

		Sm_User sm_User = model.getUser();

		if (null == sm_User)
		{
			return MyBackInfo.fail(properties, "登陆失败，请重新登陆");
		}

		Emmp_CompanyInfo companyInfo = sm_User.getCompany();

		if (null == companyInfo)
		{
			return MyBackInfo.fail(properties, "登陆用户关联楼幢失败，请核对。");
		}

		// 机构类型
		String companyType = companyInfo.getTheType();

		if (null != companyType && companyType.equals(S_CompanyType.Cooperation))
		{
			String kind = model.getKind();// 请求方法
			
			Long regionId = model.getCityRegionId();
			
			Long[] idArr = model.getCityRegionInfoIdArr();

			switch (kind)
			{
			case "1":

				// 获取机构下项目坐标
				Empj_ProjectInfoForm projectForm = new Empj_ProjectInfoForm();
				projectForm.setUser(model.getUser());
				projectForm.setUserId(model.getUserId());
				projectForm.setTheState(S_TheState.Normal);

				String keyword = model.getKeyword();
				
				if (null != keyword && keyword.trim().length() > 0)
				{
					projectForm.setKeyword(keyword);
				}
				else
				{
					projectForm.setKeyword(null);
				}

				if (null != regionId && regionId > 1)
				{
					projectForm.setCityRegionId(model.getCityRegionId());
				}
				else
				{
					projectForm.setCityRegionInfoIdArr(idArr);
				}

				List<Empj_ProjectInfo> empj_ProjectInfoList = new ArrayList<Empj_ProjectInfo>();

				empj_ProjectInfoList = empj_ProjectInfoDao
						.findByPage(empj_ProjectInfoDao.createNewCriteriaForList(projectForm));

				if (null != empj_ProjectInfoList && empj_ProjectInfoList.size() > 0)
				{
					properties.put("empj_ProjectInfoList", rebuild.execute(empj_ProjectInfoList));
				}
				else
				{
					properties.put("empj_ProjectInfoList", empj_ProjectInfoList = new ArrayList<Empj_ProjectInfo>());
				}

				// 获取常州市区域坐标
				HashMap<String, String> map = new HashMap<>();

				Sm_BaseParameterForm baseParameterModel = new Sm_BaseParameterForm();
				baseParameterModel.setTheState(S_TheState.Normal);
				baseParameterModel.setParametertype("64");
				baseParameterModel.setTheValue("6401");

				List<Sm_BaseParameter> listMap = new ArrayList<>();
				listMap = sm_BaseParameterDao.findByPage(
						sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterModel));
				if (null != listMap && listMap.size() > 0)
				{
					String theName = listMap.get(0).getTheName();
					String[] split = theName.split("，");

					map.put("x", split[0]);
					map.put("y", split[1]);
					map.put("z", split[2]);
				}
				else
				{
					map.put("x", "119.980563");
					map.put("y", "31.817432");
					map.put("z", "常州市");
				}

				properties.put("baiduMap", map);

				break;

			case "2":

				// 获取机构下项目坐标
				Empj_ProjectInfoForm projectForm2 = new Empj_ProjectInfoForm();
				projectForm2.setUser(model.getUser());
				projectForm2.setUserId(model.getUserId());
				
				if (null != regionId && regionId > 1)
				{
					projectForm2.setCityRegionId(model.getCityRegionId());
				}
				else
				{
					projectForm2.setCityRegionInfoIdArr(idArr);
				}

				List<Empj_ProjectInfo> empj_ProjectInfoList2 = new ArrayList<Empj_ProjectInfo>();

				empj_ProjectInfoList2 = empj_ProjectInfoDao
						.findByPage(empj_ProjectInfoDao.createNewCriteriaForList(projectForm2));

				if (null != empj_ProjectInfoList2 && empj_ProjectInfoList2.size() > 0)
				{
					properties.put("depositProjectInfoList", rebuild.getDepositProject(empj_ProjectInfoList2));
				}
				else
				{
					properties.put("depositProjectInfoList", empj_ProjectInfoList2 = new ArrayList<Empj_ProjectInfo>());
				}
				
				// 查询轮播风险评判
				/**
				 * xsz by time 2019-5-15 16:41:36
				 * 轮播高风险去除
				 */
				/*List<Map<String, Object>> pjRiskRatingListMap = new ArrayList<>();

				Tg_PjRiskRatingForm pjRiskRatingForm = new Tg_PjRiskRatingForm();
				pjRiskRatingForm.setTheLevel("0");
				pjRiskRatingForm.setTheState(S_TheState.Normal);
				

				if (null != regionId && regionId > 1)
				{
					pjRiskRatingForm.setCityRegionId(model.getCityRegionId());
				}
				else
				{
					pjRiskRatingForm.setCityRegionInfoIdArr(idArr);
				}
				
				List<Tg_PjRiskRating> tg_PjRiskRatingList = tg_PjRiskRatingDao
						.findByPage(tg_PjRiskRatingDao.getQuery(tg_PjRiskRatingDao.getBasicHQL(), pjRiskRatingForm));

				if (null != tg_PjRiskRatingList && tg_PjRiskRatingList.size() > 0)
				{
					for (Tg_PjRiskRating tg_PjRiskRating : tg_PjRiskRatingList)
					{
						Map<String, Object> pjRiskRatingMap = new HashMap<>();
						pjRiskRatingMap.put("tableId", tg_PjRiskRating.getTableId());
						pjRiskRatingMap.put("message", tg_PjRiskRating.getTheNameOfProject() + "，风险级别：" + "高风险");
						pjRiskRatingListMap.add(pjRiskRatingMap);
					}
				}

				properties.put("pjRiskRatingListMap", pjRiskRatingListMap);*/

				break;

			case "4":

				Long[] cityRegionInfoIdArr = model.getCityRegionInfoIdArr();

				List<Map<String, Object>> cityRegionList = new ArrayList<>();

				Map<String, Object> cityRegionMap2 = new HashMap<>();

				cityRegionMap2.put("tableId", 1L);

				cityRegionMap2.put("theName", "请选择区域");

				cityRegionList.add(cityRegionMap2);

				for (Long cityRegionId : cityRegionInfoIdArr)
				{
					Map<String, Object> cityRegionMap = new HashMap<>();

					Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);

					if (null != sm_CityRegionInfo)
					{
						cityRegionMap.put("tableId", sm_CityRegionInfo.getTableId());
						cityRegionMap.put("theName", sm_CityRegionInfo.getTheName());

						cityRegionList.add(cityRegionMap);
					}
				}

				properties.put("cityRegionList", cityRegionList);

				break;

			default:
				break;
			}
		}
		else
		{
			return MyBackInfo.fail(properties, "调用方法失败，请核对调用方法是否正确。");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

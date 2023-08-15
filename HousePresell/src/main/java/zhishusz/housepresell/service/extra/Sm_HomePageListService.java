package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Empj_ProjectInfoListService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;

/**
 * 首页展示加载
 * 
 * @ClassName: Sm_HomePageListService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月12日 下午2:14:11
 * @version V1.0
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_HomePageListService
{
	@Autowired
	private Sm_UserDao sm_UserDao;// 用户
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;// 快捷导航
	@Autowired
	private Empj_ProjectInfoListService service;// 楼盘表地图
	@Autowired
	private Empj_ProjectInfoRebuild rebuild;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;//查询默认地图坐标

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public Properties execute(Tb_b_contractFrom model)
	{
		Properties properties = new MyProperties();
		/*
		 * xsz by time 2018-9-12 14:19:53
		 * 根据当前登录人员信息，查询权限
		 * 控制其菜单权限、项目展示权限、通知公告
		 */

		// 获取当前登录人ID
		Long userId = model.getUserId();
		if (null == userId || userId <= 0)
		{
			userId = 1L;
		}

		/*
		 * xsz by time 2018-9-12 14:21:35
		 * 默认先获取当前登录用户所在开发企业，
		 * 通过开发企业查询需要展示的项目信息
		 */
		Sm_User user = sm_UserDao.findById(userId);
		if (null == user)
		{
			return MyBackInfo.fail(properties, "未查询到登录用户信息");
		}

		properties.put("userName", user.getTheName());

		Emmp_CompanyInfo company = user.getCompany();
		if (null == company || null == company.getTableId())
		{
			return MyBackInfo.fail(properties, "未查询到登录用户所在组织信息");
		}

		/*
		 * xsz by time 2018-11-22 18:13:35
		 * 注释原因：调用公共的项目加载
		 * ========================start=============================
		 */
		// 根据开发企业tableId查询项目信息列表
		// Long companyId = company.getTableId();
		// Empj_ProjectInfoForm proModel = new Empj_ProjectInfoForm();
		// proModel.setDevelopCompanyId(companyId);
		//
		// List<Empj_ProjectInfo> empj_ProjectInfoList = new
		// ArrayList<Empj_ProjectInfo>();
		//
		// empj_ProjectInfoList = empj_ProjectInfoDao
		// .findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(),
		// proModel));
		//
		// if (null == empj_ProjectInfoList || empj_ProjectInfoList.size() == 0)
		// {
		// empj_ProjectInfoList = new ArrayList<Empj_ProjectInfo>();
		// }

		/*
		 * xsz by time 2018-11-22 18:13:35
		 * 注释原因：调用公共的项目加载
		 * ========================end=============================
		 */
		Empj_ProjectInfoForm p = new Empj_ProjectInfoForm();
		p.setUser(model.getUser());
		p.setUserId(model.getUserId());
		p.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
		p.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
		p.setProjectInfoIdArr(model.getProjectInfoIdArr());
		Properties execute = service.execute(p);

		if (null != execute && null != execute.get("empj_ProjectInfoList"))
		{
			if(MyBackInfo.isSuccess(execute))
			{
				properties.put("empj_ProjectInfoList", rebuild.execute((List)(execute.get("empj_ProjectInfoList"))));
			}
		}

		/*
		 * xsz by time 2018-9-12 14:37:53
		 * 快捷导航
		 * 
		 */
		Sm_FastNavigateForm fastModel = new Sm_FastNavigateForm();
		fastModel.setUserTableId(userId);

		List<Sm_FastNavigate> sm_FastNavigateList = new ArrayList<Sm_FastNavigate>();
		sm_FastNavigateList = sm_FastNavigateDao
				.findByPage(sm_FastNavigateDao.getQuery(sm_FastNavigateDao.getBasicHQL(), fastModel));

		if (null == sm_FastNavigateList || sm_FastNavigateList.size() == 0)
		{
			sm_FastNavigateList = new ArrayList<Sm_FastNavigate>();
		}
		properties.put("sm_FastNavigateList", sm_FastNavigateList);

		/*
		 * xsz by time 2018-9-12 14:46:50
		 * 消息通知，暂无
		 */
		
		/*
		 * xsz by time 2018-12-20 08:55:18
		 * 添加默认地图加载坐标显示
		 * Parametertype = 64
		 * TheValue = 6401
		 * TheName : 默认坐标（以中文，分割）
		 */
		HashMap<String, String> map = new HashMap<>();
		
		Sm_BaseParameterForm baseParameterModel = new Sm_BaseParameterForm();
		baseParameterModel.setTheState(S_TheState.Normal);
		baseParameterModel.setParametertype("64");
		baseParameterModel.setTheValue("6401");
		
		
		List<Sm_BaseParameter> listMap = new ArrayList<>();
		listMap = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterModel));
		if(null!=listMap&&listMap.size()>0)
		{
			//119.980563，31.817432，溧阳市
			String theName = listMap.get(0).getTheName();
			String[] split = theName.split("，");
			
			map.put("x", split[0]);
			map.put("y", split[1]);
			map.put("z", split[2]);
			
		}
		else
		{
			map.put("x", "119.490709");
			map.put("y", "31.422955");
			map.put("z", "溧阳市");
			
		}
		properties.put("baiduMap", map);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

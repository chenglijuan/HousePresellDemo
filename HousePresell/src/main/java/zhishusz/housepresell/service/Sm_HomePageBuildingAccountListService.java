package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.extra.Qs_BuildingAccount_ViewForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.extra.Qs_BuildingAccount_ViewDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.Qs_BuildingAccount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service开发企业登录楼幢托管信息加载
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_HomePageBuildingAccountListService
{
	@Autowired
	private Qs_BuildingAccount_ViewDao qs_BuildingAccount_ViewDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_BuildingAccount_ViewForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2019-1-10 14:00:43
		 * 1.查询用户所属开发企业下的所有项目信息
		 * 2.取第一条项目信息加载该项目下的所有楼幢信息
		 */

		//
		Sm_User user = model.getUser();
		Emmp_CompanyInfo company = user.getCompany();

		// 设置楼幢查询条件
//		model.setTheNameOfCompany(company.getTheName());

		
		Empj_ProjectInfoForm empj_ProjectInfoModel = new Empj_ProjectInfoForm();
		empj_ProjectInfoModel.setTheState(S_TheState.Normal);
		empj_ProjectInfoModel.setDevelopCompanyId(company.getTableId());
		// 是否需要根据权限加载？ 暂时先注释
		// empj_ProjectInfoModel.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
		// empj_ProjectInfoModel.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
		// empj_ProjectInfoModel.setProjectInfoIdArr(model.getProjectInfoIdArr());
		List<Empj_ProjectInfo> projectInfoList;
		projectInfoList = empj_ProjectInfoDao
				.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), empj_ProjectInfoModel));
		
		// 是否带条件查询
		String theNameOfProject = model.getTheNameOfProject();
		if (null != theNameOfProject && !theNameOfProject.trim().isEmpty())
		{
			// 带条件查询，直接查询指定项目下的楼幢信息
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(Long.parseLong(theNameOfProject));

			model.setTheNameOfProject(projectInfo.getTheName());

		}
		else
		{
			// 非条件查询（初始加载），先加载所有项目信息，再加载第一个项目下的所有楼幢信息
			if (null == projectInfoList || projectInfoList.size() == 0)
			{
				projectInfoList = new ArrayList<Empj_ProjectInfo>();
			}
			else
			{
				model.setTheNameOfProject(projectInfoList.get(0).getTheName());
			}
			// 设置楼幢查询条件
//			model.setTheNameOfCompany(company.getTheName());
			
		}

		
		//图数据（加载所有）
		Integer totalCount = qs_BuildingAccount_ViewDao.findByPage_Size(
				qs_BuildingAccount_ViewDao.getQuery_Size(qs_BuildingAccount_ViewDao.getBasicHQL(), model));

		List<Qs_BuildingAccount_View> qs_BuildingAccount_ViewList;
		if (totalCount > 0)
		{
			qs_BuildingAccount_ViewList = qs_BuildingAccount_ViewDao.findByPage(qs_BuildingAccount_ViewDao.getQuery(qs_BuildingAccount_ViewDao.getBasicHQL(), model));
		}
		else
		{
			qs_BuildingAccount_ViewList = new ArrayList<Qs_BuildingAccount_View>();
		}
		
		//表数据（带分页）
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;
		
		
		List<Qs_BuildingAccount_View> qs_BuildingAccount_ViewList2;
		if (totalCount > 0)
		{
			qs_BuildingAccount_ViewList2 = qs_BuildingAccount_ViewDao.findByPage(qs_BuildingAccount_ViewDao.getQuery(qs_BuildingAccount_ViewDao.getBasicHQL(), model), pageNumber,countPerPage);
		}
		else
		{
			qs_BuildingAccount_ViewList2 = new ArrayList<Qs_BuildingAccount_View>();
		}
		
//		for (Qs_BuildingAccount_View po : qs_BuildingAccount_ViewList)
//		{
//			po.setYxsxPrice(po.getYxsxPrice()/10000);
//			po.setCurrentEscrowFund(po.getCurrentEscrowFund()/10000);
//		}
//		
//		for (Qs_BuildingAccount_View po1 : qs_BuildingAccount_ViewList2)
//		{
//			po1.setYxsxPrice(po1.getYxsxPrice()/10000);
//			po1.setCurrentEscrowFund(po1.getCurrentEscrowFund()/10000);
//		}
		//返回的项目列表信息
		properties.put("projectInfoList", projectInfoList);
		properties.put("buildingInfoListForList", qs_BuildingAccount_ViewList2);
		properties.put("buildingInfoListForMap", qs_BuildingAccount_ViewList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

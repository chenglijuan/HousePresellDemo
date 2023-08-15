package zhishusz.housepresell.aspect;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RangeForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;

/**
 * 角色授权数据自动过滤切面
 * @author zhishusz
 */
@Component
@Aspect
public class RangeAuthorizationAspect
{
//	private MyInteger myInteger = MyInteger.getInstance();
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	@Pointcut(
		//角色授权数据自动过滤切点 （批量设置）
	    "!execution(* zhishusz.housepresell.controller.Tgxy_TripleAgreementDetailController.*(..))  &&  execution(* zhishusz.housepresell.controller..*(..)) " +
	"")
	public void checkAspect(){}
	
	/**
	 * 分权限管理员查看数据
	 * 	重用代码：
	 * 		if(!model.getIsSuperAdmin())
	 *		{
	 *			//若不是超级管理员，则显示分权限的数据,反之，则显示所有
	 *			model.setCityId(model.getAdminCityId());
	 *		}
	 * @param pjp
	 * @return
	 */
	@Around("checkAspect()")
	private Object checkIsSuperAdmin(ProceedingJoinPoint pjp)
	{
		return pjpPoint(pjp);
	}

	@SuppressWarnings("unchecked")
	private Object pjpPoint(ProceedingJoinPoint pjp)
	{
		RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttribute;
		HttpServletRequest request = servletRequestAttributes.getRequest();
		
		//从request中获取session
		HttpSession session = request.getSession();
		Sm_User loginUser = (Sm_User) session.getAttribute(S_NormalFlag.user);
		
		//获取执行方法的参数  
		Object[] args = pjp.getArgs();
		if(loginUser == null)
		{
			return AsPectAopFilter.execute(pjp, args, null, null, null, null);
		}
		
		//BaseForm 预存：会员类型：正泰用户、普通机构用户 S_UserType
		Integer loginUserType = loginUser.getTheType();
		Emmp_CompanyInfo companyInfo = null;
		
		Long nowTimeStamp = System.currentTimeMillis();
		List<Sm_Permission_Range> sm_Permission_RangeList = new ArrayList<Sm_Permission_Range>();
		Sm_Permission_RangeForm sm_Permission_RangeForm = new Sm_Permission_RangeForm();
		sm_Permission_RangeForm.setTheState(S_TheState.Normal);
		sm_Permission_RangeForm.setLeAuthStartTimeStamp(nowTimeStamp);
		sm_Permission_RangeForm.setGtAuthEndTimeStamp(nowTimeStamp);
		if(S_UserType.ZhengtaiUser.equals(loginUserType))
		{
			//在角色授权数据表，根据用户，查询，获取该用户拥有的区域IdArr
			sm_Permission_RangeForm.setUser(loginUser);
			sm_Permission_RangeList = sm_Permission_RangeDao.findByPage(sm_Permission_RangeDao.getQuery(sm_Permission_RangeDao.getBasicHQL(), sm_Permission_RangeForm));
		}
		else if(S_UserType.CommonUser.equals(loginUserType))
		{
			//根据该用户所在的机构，查询出该机构所拥有的区域IdArr,项目IdArr,楼栋IdArr
			companyInfo = loginUser.getCompany();
			sm_Permission_RangeForm.setCompanyInfo(companyInfo);
			sm_Permission_RangeList = sm_Permission_RangeDao.findByPage(sm_Permission_RangeDao.getQuery(sm_Permission_RangeDao.getBasicHQL(), sm_Permission_RangeForm));
		}
		
		Set<Long> buildingIdSet_Temp = new HashSet<Long>();
		Set<Long> projectIdSet_Temp = new HashSet<Long>();
		Set<Long> cityReagionIdSet_Temp = new HashSet<Long>();
		Set<Long> developCompanyInfoIdSet_Temp = new HashSet<Long>();
		if(args == null)
		{
			
		}
		else 
		{
			//数据过滤(范围授权，系统用户范围授权)
			//List<Sm_CityRegionInfo> loginCityRegionInfoList = new ArrayList<Sm_CityRegionInfo>();
			//List<Empj_ProjectInfo> logInProjectInfoList = new ArrayList<Empj_ProjectInfo>();
			//List<Empj_BuildingInfo> logInBuildingInfoList = new ArrayList<Empj_BuildingInfo>();
			
			List<Long> logInCityRegionInfoIdArr = new ArrayList<Long>();
			List<Long> logInProjectInfoIdArr = new ArrayList<Long>();
			List<Long> logInBuildingInfoIdArr = new ArrayList<Long>();
			for(Sm_Permission_Range sm_Permission_Range_Temp : sm_Permission_RangeList)
			{
				Sm_CityRegionInfo cityRegionInfo = sm_Permission_Range_Temp.getCityRegionInfo();
				Empj_ProjectInfo projectInfo = sm_Permission_Range_Temp.getProjectInfo();
				Empj_BuildingInfo buildingInfo = sm_Permission_Range_Temp.getBuildingInfo();
				
				Long cityRegionInfoId = cityRegionInfo == null ? null : cityRegionInfo.getTableId();
				Long projectInfoId = projectInfo == null ? null : projectInfo.getTableId();
				Long buildingInfoIdId = buildingInfo == null ? null : buildingInfo.getTableId(); 
				if(cityRegionInfoId != null && !logInCityRegionInfoIdArr.contains(cityRegionInfoId))
				{
					logInCityRegionInfoIdArr.add(cityRegionInfoId);
				}
				if(projectInfoId != null && !logInProjectInfoIdArr.contains(projectInfoId))
				{
					logInProjectInfoIdArr.add(projectInfoId);
				}
				if(buildingInfoIdId != null && !logInBuildingInfoIdArr.contains(buildingInfoIdId))
				{
					logInBuildingInfoIdArr.add(buildingInfoIdId);
				}
			}
			
			/**
			 * 由于，不是每个需要数据过滤的PO模型，未维护全
			 * 	区域、项目、楼幢
			 * 这三个冗余字段，故在此处牺牲一下性能（增加过滤条件）
			 */
			if(logInCityRegionInfoIdArr.size() > 0)
			{
				cityReagionIdSet_Temp = new HashSet<Long>(logInCityRegionInfoIdArr);
				
				Empj_ProjectInfoForm projectInfo_TempModel = new Empj_ProjectInfoForm();
				projectInfo_TempModel.setCityRegionInfoIdArr(logInCityRegionInfoIdArr.toArray(new Long[logInCityRegionInfoIdArr.size()]));
				List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getListByIdArrHQL(), projectInfo_TempModel));
				for(Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList)
				{
					if(empj_ProjectInfo == null) continue;
					projectIdSet_Temp.add(empj_ProjectInfo.getTableId());
				}
				
				Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
				tempModel.setCityRegionInfoIdArr(logInCityRegionInfoIdArr.toArray(new Long[logInCityRegionInfoIdArr.size()]));
				List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
				for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
				{
					if(empj_BuildingInfo == null) continue;
					buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
				}
				
				//查区域下面的开发企业
//				Emmp_CompanyInfoForm developCompanyForm = new Emmp_CompanyInfoForm();
//				developCompanyForm.setCityRegionInfoIdArr(logInCityRegionInfoIdArr.toArray(new Long[logInCityRegionInfoIdArr.size()]));
//				List<Emmp_CompanyInfo> developCompanyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getListByIdArrHQLForDevelopCompany(), developCompanyForm));
//				for(Emmp_CompanyInfo developCompanyInfo : developCompanyInfoList)
//				{
//					if(developCompanyInfo == null) continue;
//					developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
//				}
				
				return AsPectAopFilter.execute(pjp, args, cityReagionIdSet_Temp, projectIdSet_Temp, buildingIdSet_Temp, developCompanyInfoIdSet_Temp);
			}
			
			if(logInProjectInfoIdArr.size() > 0)
			{
				Empj_ProjectInfoForm projectInfo_TempModel = new Empj_ProjectInfoForm();
				projectInfo_TempModel.setProjectInfoIdArr(logInProjectInfoIdArr.toArray(new Long[logInProjectInfoIdArr.size()]));
				List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getListByIdArrHQL(), projectInfo_TempModel));
				for(Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList)
				{
					if(empj_ProjectInfo == null) continue;
					projectIdSet_Temp.add(empj_ProjectInfo.getTableId());
					
					Sm_CityRegionInfo sm_CityRegionInfo = empj_ProjectInfo.getCityRegion();
					if(sm_CityRegionInfo != null)
					{
						cityReagionIdSet_Temp.add(sm_CityRegionInfo.getTableId());
					}
					
					//关联项目下的开发企业
					Emmp_CompanyInfo developCompanyInfo = empj_ProjectInfo.getDevelopCompany();
					if(developCompanyInfo != null)
					{
						developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
					}
				}
				
				Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
				tempModel.setProjectInfoIdArr(logInProjectInfoIdArr.toArray(new Long[logInProjectInfoIdArr.size()]));
				List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
				for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
				{
					if(empj_BuildingInfo == null) continue;
					buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
				}
				
				return AsPectAopFilter.execute(pjp, args, cityReagionIdSet_Temp, projectIdSet_Temp, buildingIdSet_Temp, developCompanyInfoIdSet_Temp);
			}
			
			if(logInBuildingInfoIdArr.size() > 0)
			{
				Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
				tempModel.setBuildingInfoIdIdArr(logInBuildingInfoIdArr.toArray(new Long[logInBuildingInfoIdArr.size()]));
				List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
				for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
				{
					if(empj_BuildingInfo == null) continue;
					buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
					
					Sm_CityRegionInfo city_Temp = empj_BuildingInfo.getCityRegion(); 
					Empj_ProjectInfo project_Temp = empj_BuildingInfo.getProject(); 
					Emmp_CompanyInfo developCompanyInfo = empj_BuildingInfo.getDevelopCompany();
					
					if(city_Temp != null)
					{
						cityReagionIdSet_Temp.add(city_Temp.getTableId());
					}
					
					if(project_Temp != null)
					{
						projectIdSet_Temp.add(project_Temp.getTableId());
					}
					
					//关联楼幢下面的开发企业
					if(developCompanyInfo != null)
					{
						developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
					}
				}
				
				return AsPectAopFilter.execute(pjp, args, cityReagionIdSet_Temp, projectIdSet_Temp, buildingIdSet_Temp, developCompanyInfoIdSet_Temp);
			}
			
			//普通用户，且未分配范围授权
			if(S_UserType.CommonUser.equals(loginUserType))
			{
				/**
				 * 20181113和苏经理沟通后的处理方案：
				 * 	原先不分配范围授权的时候，若是机构用户则只能看到自己创建的项目，楼幢信息。
				 * 	现在要求看到和登录用户机构类型一致即可以看到
				 */
				Emmp_CompanyInfo loginUserCompany = loginUser.getCompany();
				if(loginUserCompany != null)
				{
					//传机构下的项目
					Empj_ProjectInfoForm projectInfo_TempModel = new Empj_ProjectInfoForm();
					projectInfo_TempModel.setDevelopCompanyId(loginUserCompany.getTableId());
					List<Empj_ProjectInfo> empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getListByIdArrHQL(), projectInfo_TempModel));
					for(Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList)
					{
						if(empj_ProjectInfo == null) continue;
						projectIdSet_Temp.add(empj_ProjectInfo.getTableId());
						
						Sm_CityRegionInfo sm_CityRegionInfo = empj_ProjectInfo.getCityRegion();
						if(sm_CityRegionInfo != null)
						{
							cityReagionIdSet_Temp.add(sm_CityRegionInfo.getTableId());
						}
						
						Emmp_CompanyInfo developCompanyInfo = empj_ProjectInfo.getDevelopCompany();
						if(developCompanyInfo != null)
						{
							developCompanyInfoIdSet_Temp.add(developCompanyInfo.getTableId());
						}
					}
					
					//机构下未分配项目
					if(projectIdSet_Temp == null || projectIdSet_Temp.size() == 0)
					{
						projectIdSet_Temp = new HashSet<Long>();
						projectIdSet_Temp.add(-1L);

						buildingIdSet_Temp = new HashSet<Long>();
						buildingIdSet_Temp.add(-1L);
						
						//是开发企业用户
						if(companyInfo != null && S_CompanyType.Development.equals(companyInfo.getTheType()))
						{
							developCompanyInfoIdSet_Temp.add(companyInfo.getTableId());
						}
						else
						{
							developCompanyInfoIdSet_Temp.add(-1L);
						}
						
						return AsPectAopFilter.execute(pjp, args, cityReagionIdSet_Temp, projectIdSet_Temp, buildingIdSet_Temp, developCompanyInfoIdSet_Temp);
					}
					
					//传机构下的楼幢
					Empj_BuildingInfoForm tempModel = new Empj_BuildingInfoForm();
					tempModel.setProjectInfoIdArr(projectIdSet_Temp.toArray(new Long[projectIdSet_Temp.size()]));
					List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getListByIdArrHQL(), tempModel));
					for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
					{
						if(empj_BuildingInfo == null) continue;
						buildingIdSet_Temp.add(empj_BuildingInfo.getTableId());
					}
				}
				else
				{
					//过滤脏数据，登录用户，未关联机构信息
					buildingIdSet_Temp.add(-1L);
					projectIdSet_Temp.add(-1L);
					cityReagionIdSet_Temp.add(-1L);
					developCompanyInfoIdSet_Temp.add(-1L);
				}
			}
		}
		
		return AsPectAopFilter.execute(pjp, args, cityReagionIdSet_Temp, projectIdSet_Temp, buildingIdSet_Temp, developCompanyInfoIdSet_Temp);
	}

}

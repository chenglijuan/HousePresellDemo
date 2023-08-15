package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDao;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDtlDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.objectdiffer.model.Empj_PjDevProgressForcastTemplate;
import zhishusz.housepresell.util.rebuild.Empj_PjDevProgressForcastDtlRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/*
 * Service更新操作：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastUpdateService
{
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;
	@Autowired
	private Empj_PjDevProgressForcastDtlAddService empj_pjDevProgressForcastDtlAddService;
	@Autowired
	private Empj_PjDevProgressForcastDtlUpdateService empj_pjDevProgressForcastDtlUpdateService;
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_pjDevProgressForcastDtlDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_BusiState_LogAddService logAddService;
	@Autowired
	private Empj_PjDevProgressForcastDtlRebuild forcastDtlRebuild;
	
	public Properties execute(Empj_PjDevProgressForcastForm model)
	{
		Properties properties = new MyProperties();

		//更新主表信息
		//解除/重现绑定等参考托管终止
		//明细表判断ID是否存在，不存在添加，存在更新，更新时注意新旧预测完成日期

		Integer theState = S_TheState.Normal;
		String busiState = model.getBusiState();
		Long lastUpdateTimeStamp = System.currentTimeMillis();
		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		Long buildingId = model.getBuildingId();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
//		String payoutType = model.getPayoutType();
		Double currentFigureProgress = model.getCurrentFigureProgress();
		String currentBuildProgress = model.getCurrentBuildProgress();
		String patrolPerson = model.getPatrolPerson();
//		Long patrolInstruction = model.getPatrolInstruction();
		String remark = model.getRemark();
		MyDatetime myDatetime = MyDatetime.getInstance();

		String patrolTimestampStr = model.getPatrolTimestamp();

		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目名称不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "施工编号不能为空");
		}
		if(patrolPerson == null || patrolPerson.length() == 0)
		{
			return MyBackInfo.fail(properties, "巡查人不能为空");
		}
		if(patrolTimestampStr == null || "".equals(patrolTimestampStr))
		{
			return MyBackInfo.fail(properties, "巡查日期不能为空");
		}
		if (remark != null && remark.length() > 200)
		{
			return MyBackInfo.fail(properties, "备注长度不能超过200字");
		}

		Long patrolTimestamp = MyDatetime.getInstance().stringToLong(patrolTimestampStr);

		Sm_User userUpdate = (Sm_User)model.getUser();
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在，请重新登录");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "开发企业不存在");
		}
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "项目不存在");
		}
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "楼幢不存在");
		}
	
		Long empj_PjDevProgressForcastId = model.getTableId();
		Empj_PjDevProgressForcast empj_PjDevProgressForcast = (Empj_PjDevProgressForcast)empj_PjDevProgressForcastDao.findById(empj_PjDevProgressForcastId);
		List<Empj_PjDevProgressForcastDtl> detailList = empj_PjDevProgressForcast.getDetailList();

		if(empj_PjDevProgressForcast == null)
		{
			return MyBackInfo.fail(properties, "工程进度预测信息不存在");
		}
		//拷贝对象，用于查看变更日志
		Empj_PjDevProgressForcastTemplate forcastTemplate = new Empj_PjDevProgressForcastTemplate();
		forcastTemplate.setPjDevProgressForcast(empj_PjDevProgressForcast);
		forcastTemplate.setPatrolTimeStampString(myDatetime.dateToSimpleString(empj_PjDevProgressForcast.getPatrolTimestamp()));
		forcastTemplate.setRemark(empj_PjDevProgressForcast.getRemark());
		List<HashMap> detailForAdminOld = forcastDtlRebuild.getDetailForAdmin(empj_PjDevProgressForcast.getDetailList());
		forcastTemplate.setForcastDtlList(detailForAdminOld);
		Empj_PjDevProgressForcastTemplate forcastTemplateOld = ObjectCopier.copy(forcastTemplate);

//		Empj_PjDevProgressForcast empj_pjDevProgressForcastOld = ObjectCopier.copy(empj_PjDevProgressForcast);
//		Empj_PjDevProgressForcastTemplate empj_pjDevProgressForcastTemplateOld = new Empj_PjDevProgressForcastTemplate();
//		empj_pjDevProgressForcastTemplateOld.setPjDevProgressForcast(empj_pjDevProgressForcastOld);
//		empj_pjDevProgressForcastTemplateOld.createSpecialLogFieldWithDtlList(empj_pjDevProgressForcastOld.getDetailList());
//		empj_pjDevProgressForcastTemplateOld.setEmpj_pjDevProgressForcastDtlListString();

		/**
		 * 新增或明细表
		 * 修改明细表删除状态thestate=0
		 * 解除主表与明细表直接的关系
		 * 设置主表与新明细表一对多关联关系
		 */
		List<Empj_PjDevProgressForcastDtl> empj_pjDevProgressForcastDtlList = new ArrayList<Empj_PjDevProgressForcastDtl>();
		if (model.getDtlFormList() != null)
		{
			for (Empj_PjDevProgressForcastDtlForm empjPjDevProgressForcastDtlForm : model.getDtlFormList())
			{
				if (empjPjDevProgressForcastDtlForm == null)
				{
					return MyBackInfo.fail(properties, "进度详情信息不能为空");
				}
				if (empjPjDevProgressForcastDtlForm.getTableId() == null)
				{
					//新增明细表
					Properties detailProperties = empj_pjDevProgressForcastDtlAddService.execute(empjPjDevProgressForcastDtlForm);
					if (S_NormalFlag.success.equals(detailProperties.getProperty(S_NormalFlag.result)))
					{
						empj_pjDevProgressForcastDtlList.add((Empj_PjDevProgressForcastDtl) detailProperties.get(
								"empj_PjDevProgressForcastDtl"));
					}
					else
					{
						return MyBackInfo.fail(properties, detailProperties.getProperty(S_NormalFlag.result));
					}
				}
				else
				{
					Empj_PjDevProgressForcastDtl empj_pjDevProgressForcastDtl =
							(Empj_PjDevProgressForcastDtl)empj_pjDevProgressForcastDtlDao.findById(empjPjDevProgressForcastDtlForm.getTableId());
					//新增明细表
					if (empj_pjDevProgressForcastDtl == null)
					{
						Properties detailProperties = empj_pjDevProgressForcastDtlAddService.execute(empjPjDevProgressForcastDtlForm);
						if (S_NormalFlag.success.equals(detailProperties.getProperty(S_NormalFlag.result)))
						{
							empj_pjDevProgressForcastDtlList.add((Empj_PjDevProgressForcastDtl) detailProperties.get(
									"empj_PjDevProgressForcastDtl"));
						}
						else
						{
//							return detailProperties;
							return MyBackInfo.fail(properties, detailProperties.getProperty(S_NormalFlag.result));
						}
					}
					//更新明细表
					else
					{
						Properties detailProperties = empj_pjDevProgressForcastDtlUpdateService.execute(empjPjDevProgressForcastDtlForm);
						if (S_NormalFlag.success.equals(detailProperties.getProperty(S_NormalFlag.result)))
						{
							Empj_PjDevProgressForcastDtl updatePjDevProgressForcastDtl =
									(Empj_PjDevProgressForcastDtl)detailProperties.get(
											"empj_PjDevProgressForcastDtl");
							updatePjDevProgressForcastDtl.setTheState(S_TheState.Normal);
							empj_pjDevProgressForcastDtlList.add(updatePjDevProgressForcastDtl);
						}
						else
						{
							return MyBackInfo.fail(properties, detailProperties.getProperty(S_NormalFlag.result));
						}
					}
				}
			}
		}


		List<Empj_PjDevProgressForcastDtl> empj_pjDevProgressForcastDtls = empj_PjDevProgressForcast.getDetailList();
		if (empj_pjDevProgressForcastDtls != null)
		{
			for (Empj_PjDevProgressForcastDtl empj_pjDevProgressForcastDtl : empj_pjDevProgressForcastDtls)
			{
				empj_pjDevProgressForcastDtl.setTheState(S_TheState.Deleted);
			}
			empj_PjDevProgressForcast.getDetailList().clear();
		}

		empj_PjDevProgressForcast.setTheState(theState);
		empj_PjDevProgressForcast.setBusiState(busiState);
		empj_PjDevProgressForcast.setUserUpdate(userUpdate);
		empj_PjDevProgressForcast.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		empj_PjDevProgressForcast.setDevelopCompany(developCompany);
		empj_PjDevProgressForcast.seteCodeOfDevelopCompany(developCompany.geteCode());
		empj_PjDevProgressForcast.setProject(project);
		empj_PjDevProgressForcast.setTheNameOfProject(project.getTheName());
		empj_PjDevProgressForcast.seteCodeOfProject(project.geteCode());
		empj_PjDevProgressForcast.setBuilding(building);
		empj_PjDevProgressForcast.seteCodeOfBuilding(building.geteCode());
		empj_PjDevProgressForcast.seteCodeFromConstruction(building.geteCodeFromConstruction());
		empj_PjDevProgressForcast.seteCodeFromPublicSecurity(building.geteCodeFromPublicSecurity());
//		empj_PjDevProgressForcast.setPayoutType(payoutType);
//		empj_PjDevProgressForcast.setCurrentFigureProgress(currentFigureProgress);
		empj_PjDevProgressForcast.setCurrentBuildProgress(currentBuildProgress);
		empj_PjDevProgressForcast.setPatrolPerson(patrolPerson);
		empj_PjDevProgressForcast.setPatrolTimestamp(patrolTimestamp);
//		empj_PjDevProgressForcast.setPatrolInstruction(patrolInstruction);
		empj_PjDevProgressForcast.setRemark(remark);
		empj_PjDevProgressForcast.setDetailList(empj_pjDevProgressForcastDtlList);
		empj_PjDevProgressForcastDao.save(empj_PjDevProgressForcast);

		forcastTemplate.setPjDevProgressForcast(empj_PjDevProgressForcast);
		forcastTemplate.setRemark(remark);
		forcastTemplate.setPatrolTimeStampString(myDatetime.dateToSimpleString(patrolTimestamp));
		List<HashMap> detailForAdminNew = forcastDtlRebuild.getDetailForAdmin(empj_pjDevProgressForcastDtlList);
		forcastTemplate.setForcastDtlList(detailForAdminNew);
//		forcastTemplate.setForcastDtlList(empj_pjDevProgressForcastDtlList);
		Empj_PjDevProgressForcastTemplate forcastTemplateNew = ObjectCopier.copy(forcastTemplate);

//		Empj_PjDevProgressForcastTemplate empj_pjDevProgressForcastTemplate = new Empj_PjDevProgressForcastTemplate();
//		empj_pjDevProgressForcastTemplate.setPjDevProgressForcast(empj_PjDevProgressForcast);
//		empj_pjDevProgressForcastTemplate.createSpecialLogFieldWithDtlList(empj_pjDevProgressForcastDtls);
		logAddService.addLog(model, empj_PjDevProgressForcastId, forcastTemplateOld,
				forcastTemplateNew);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

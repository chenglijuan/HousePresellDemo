package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;

/*
 * Service添加操作：项目-工程进度预测-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastAddService
{
//	private static final String BUSI_CODE = "03030201";//具体业务编码参看SVN文

	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_pjDevProgressForcastDtlDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
//	@Autowired
//	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
//	@Autowired
//	private Sm_StreetInfoDao sm_StreetInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Empj_PjDevProgressForcastDtlAddService empj_pjDevProgressForcastDtlAddService;
	
	public Properties execute(Empj_PjDevProgressForcastForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		String busiState = model.getBusiState();
		String eCode = sm_BusinessCodeGetService.execute(S_BusiCode.busiCode_03030201); //自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
//		Long userStartId = model.getUserStartId();
		Long createTimeStamp = System.currentTimeMillis();
		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		Long buildingId = model.getBuildingId();
//		String eCodeOfBuilding = model.geteCodeOfBuilding();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
//		String payoutType = model.getPayoutType();
//		Double currentFigureProgress = model.getCurrentFigureProgress();
		String currentBuildProgress = model.getCurrentBuildProgress();
		String patrolPerson = model.getPatrolPerson();
//		Long patrolInstruction = model.getPatrolInstruction();
		String remark = model.getRemark();
		Long[] idArr = model.getIdArr(); // 进度详情明细id

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
//		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "施工编号不能为空");
//		}
//		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "公安编号不能为空");
//		}
//		if(payoutType == null || payoutType.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'payoutType'不能为空");
//		}
//		if(currentFigureProgress == null || currentFigureProgress < 0)
//		{
//			return MyBackInfo.fail(properties, "当前进度节点不能为空");
//		}
//		if(currentBuildProgress == null || currentBuildProgress.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "当前进度节点不能为空");
//		}
		if(patrolPerson == null || patrolPerson.length() == 0)
		{
			return MyBackInfo.fail(properties, "巡查人不能为空");
		}
		if(patrolTimestampStr == null || "".equals(patrolTimestampStr))
		{
			return MyBackInfo.fail(properties, "巡查日期不能为空");
		}
//		if(patrolInstruction == null || patrolInstruction < 1)
//		{
//			return MyBackInfo.fail(properties, "'patrolInstruction'不能为空");
//		}
		if (remark != null && remark.length() > 200)
		{
			return MyBackInfo.fail(properties, "备注长度不能超过200字");
		}

		Long patrolTimestamp = MyDatetime.getInstance().stringToLong(patrolTimestampStr);

		Sm_User userStart = (Sm_User)model.getUser();
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在, 请重新登录");
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

		List<Empj_PjDevProgressForcast> empj_PjDevProgressForcastList =
				empj_PjDevProgressForcastDao.findByPage(empj_PjDevProgressForcastDao.getPjDevProgressForcastCountForList(model)
						, null, null);

		Integer totalCount =
				empj_PjDevProgressForcastDao.findByPage_Size(empj_PjDevProgressForcastDao.getPjDevProgressForcastCountForList(model));
		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "已存在该楼幢的工程进度巡查预测");
		}

		List<Empj_PjDevProgressForcastDtl> empj_pjDevProgressForcastDtlList = new ArrayList<>();
		if (model.getDtlFormList() != null)
		{
			for (Empj_PjDevProgressForcastDtlForm empjPjDevProgressForcastDtlForm : model.getDtlFormList())
			{
				if (empjPjDevProgressForcastDtlForm == null)
				{
					return MyBackInfo.fail(properties, "进度节点信息不能为空");
				}
				Properties detailProperties = empj_pjDevProgressForcastDtlAddService.execute(empjPjDevProgressForcastDtlForm);
				if (S_NormalFlag.success.equals(detailProperties.getProperty(S_NormalFlag.result)))
				{
					empj_pjDevProgressForcastDtlList.add((Empj_PjDevProgressForcastDtl) detailProperties.get(
							"empj_PjDevProgressForcastDtl"));
				}
				else
				{
//					return detailProperties;
					return MyBackInfo.fail(properties, detailProperties.getProperty(S_NormalFlag.result));
				}
			}
		}

		Empj_PjDevProgressForcast empj_PjDevProgressForcast = new Empj_PjDevProgressForcast();
		empj_PjDevProgressForcast.setTheState(theState);
		empj_PjDevProgressForcast.setBusiState(busiState);
		empj_PjDevProgressForcast.seteCode(eCode);
		empj_PjDevProgressForcast.setUserStart(userStart);
		empj_PjDevProgressForcast.setCreateTimeStamp(createTimeStamp);
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

		properties.put("tableId", empj_PjDevProgressForcast.getTableId());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

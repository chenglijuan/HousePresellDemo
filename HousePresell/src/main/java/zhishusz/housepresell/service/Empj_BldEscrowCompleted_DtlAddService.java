package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompleted_DtlForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
	
/*
 * Service添加操作：申请表-项目托管终止（审批）-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompleted_DtlAddService
{
	private static final String BUSI_CODE = "03030102";//具体业务编码参看SVN文
//	@Autowired
//	private Empj_BldEscrowCompleted_DtlDao empj_BldEscrowCompleted_DtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	public Properties execute(Empj_BldEscrowCompleted_DtlForm model)
	{
		Properties properties = new MyProperties();

		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		Long buildingId = model.getBuildingId();				
//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);					
		Integer theState = S_TheState.Normal;
		String busiState = "1";
		String eCode = sm_BusinessCodeGetService.execute(BUSI_CODE); //自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
//		Long userStartId = model.getUserStartId();
		Long createTimeStamp = System.currentTimeMillis();
//		String eCodeOfMainTable = empj_BldEscrowCompleted.geteCode();
//		Long mainTableId = empj_BldEscrowCompleted.getTableId();
		

//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userStart'不能为空");
//		}
//		if(createTimeStamp == null || createTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
//		}
		
//		if(eCodeOfMainTable == null || eCodeOfMainTable.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'eCodeOfMainTable'不能为空");
//		}
//		if(mainTableId == null || mainTableId < 1)
//		{
//			return MyBackInfo.fail(properties, "'mainTable'不能为空");
//		}
		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "楼幢不能为空");
		}
		
//		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);	
//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "'userStart'不能为空");
//		}		
//		if(mainTable == null)
//		{
//			return MyBackInfo.fail(properties, "'mainTable'不能为空");
//		}
		if(building == null)
		{
			return MyBackInfo.fail(properties, "楼幢信息不能为空");
		}
		
		
		String geteCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		if(StrUtil.isBlank(geteCodeFromPublicSecurity)){
			return MyBackInfo.fail(properties, "请先维护：" + building.geteCodeFromConstruction() + "公安编号信息！"); 
		}
		
		
		/**
		 * TODO 更新楼幢公安编号信息
		 */
		
		Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl = new Empj_BldEscrowCompleted_Dtl();
		empj_BldEscrowCompleted_Dtl.setTheState(theState);
		empj_BldEscrowCompleted_Dtl.setBusiState(busiState);
		empj_BldEscrowCompleted_Dtl.seteCode(eCode);
//		empj_BldEscrowCompleted_Dtl.setUserStart(userStart);
		empj_BldEscrowCompleted_Dtl.setCreateTimeStamp(createTimeStamp);
//		empj_BldEscrowCompleted_Dtl.seteCodeOfMainTable(eCodeOfMainTable);
//		empj_BldEscrowCompleted_Dtl.setMainTable(mainTable);
		empj_BldEscrowCompleted_Dtl.setDevelopCompany(developCompany);
		empj_BldEscrowCompleted_Dtl.seteCodeOfDevelopCompany(developCompany.geteCode());
		empj_BldEscrowCompleted_Dtl.setProject(project);
		empj_BldEscrowCompleted_Dtl.setTheNameOfProject(project.getTheName());
		empj_BldEscrowCompleted_Dtl.seteCodeOfProject(project.geteCode());
		empj_BldEscrowCompleted_Dtl.setBuilding(building);
		empj_BldEscrowCompleted_Dtl.seteCodeOfBuilding(building.geteCode());
		empj_BldEscrowCompleted_Dtl.seteCodeFromPublicSecurity(model.geteCodeFromPublicSecurity());
		empj_BldEscrowCompleted_Dtl.seteCodeFromConstruction(building.geteCodeFromConstruction());
//		empj_BldEscrowCompleted_DtlDao.save(empj_BldEscrowCompleted_Dtl);

		properties.put("empj_BldEscrowCompleted_Dtl",empj_BldEscrowCompleted_Dtl);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

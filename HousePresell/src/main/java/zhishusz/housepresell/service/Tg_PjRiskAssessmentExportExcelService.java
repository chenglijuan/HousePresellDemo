package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tg_PjRiskAssessmentForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskAssessmentDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_PjRiskAssessment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tg_PjRiskAssessmentExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 导出：风险评估-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_PjRiskAssessmentExportExcelService
{

	@Autowired
	private Tg_PjRiskAssessmentDao tg_PjRiskAssessmentDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	private static final String excelName = "风险评估";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskAssessmentForm model)
	{
		Properties properties = new MyProperties();

		String keyword = model.getKeyword();
		String assessDate = model.getStartDate();// 风险评估日期-开始
		String assessDateEnd = model.getEndDate();// 风险评估日期-结束
		Long developCompanyId = model.getDevelopCompanyId();// 开发企业Id
		Long projectId = model.getProjectId();// 获取项目Id
		Long cityRegionId = model.getCityRegionId();// 获取区域Id

		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		if (null == assessDate || assessDate.trim().isEmpty())
		{
			model.setAssessDate(null);
		}
		else
		{
			model.setAssessDate(assessDate.trim());
		}

		if (null == assessDateEnd || assessDateEnd.trim().isEmpty())
		{
			model.setAssessDateEnd(null);
		}
		else
		{
			model.setAssessDateEnd(assessDateEnd.trim());
		}

		if (null != developCompanyId)
		{
			// 查询开发企业
			Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(developCompanyId);
			if (emmp_CompanyInfo == null)
			{
				return MyBackInfo.fail(properties, "为查询到有效的开发企业数据！");
			}
			else
			{
				model.setDevelopCompany(emmp_CompanyInfo);
			}
		}

		if (null != projectId)
		{
			// 查询预售项目
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			if (empj_ProjectInfo == null)
			{
				return MyBackInfo.fail(properties, "为查询到有效的项目数据！");
			}
			else
			{
				model.setProject(empj_ProjectInfo);
			}
		}

		if (null != cityRegionId)
		{
			// 查询所属区域
			Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			if (sm_CityRegionInfo == null)
			{
				return MyBackInfo.fail(properties, "为查询到有效的区域！");
			}
			else
			{
				model.setCityRegion(sm_CityRegionInfo);
			}
		}

		model.setTheState(S_TheState.Normal);

		List<Tg_PjRiskAssessment> tg_PjRiskAssessmentList = tg_PjRiskAssessmentDao
				.findByPage(tg_PjRiskAssessmentDao.getQuery(tg_PjRiskAssessmentDao.getBasicHQL(), model));

		if (tg_PjRiskAssessmentList == null || tg_PjRiskAssessmentList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_PjRiskAssessmentExportExcelService");// 文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20"))
			{
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			String strNewFileName = excelName + "-"
					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

			String saveFilePath = saveDirectory + strNewFileName;

			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

			// 自定义字段别名
			writer.addHeaderAlias("ordinal", "");

			writer.addHeaderAlias("eCode", "项目风险评估单号");
			writer.addHeaderAlias("theNameOfCityRegion", "所属区域");
			writer.addHeaderAlias("theCompanyName", "开发企业名称");
			writer.addHeaderAlias("theNameOfProject", "项目名称");
			writer.addHeaderAlias("assessDate", "项目风险评估日期");

			writer.addHeaderAlias("riskAssessment", "项目风险评估内容");
			writer.addHeaderAlias("createUserName", "操作人");
			writer.addHeaderAlias("createTimeStamp", "操作时间");

			List<Tg_PjRiskAssessmentExportExcelVO> list = formart(tg_PjRiskAssessmentList);
			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();
			writer.close();

			properties.put("fileName", strNewFileName);
			properties.put("fileURL", relativeDir + strNewFileName);
			properties.put("fullFileName", saveFilePath);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * po 格式化
	 * 
	 * @param tg_PjRiskAssessmentList
	 * @return
	 */
	List<Tg_PjRiskAssessmentExportExcelVO> formart(List<Tg_PjRiskAssessment> tg_PjRiskAssessmentList)
	{
		List<Tg_PjRiskAssessmentExportExcelVO> tg_PjRiskAssessmentExportExcelList = new ArrayList<Tg_PjRiskAssessmentExportExcelVO>();
		int ordinal = 0;
		for (Tg_PjRiskAssessment tg_PjRiskAssessment : tg_PjRiskAssessmentList)
		{
			++ordinal;
			Tg_PjRiskAssessmentExportExcelVO tg_PjRiskAssessmentExportExcel = new Tg_PjRiskAssessmentExportExcelVO();
			tg_PjRiskAssessmentExportExcel.setOrdinal(ordinal);

			tg_PjRiskAssessmentExportExcel.setECode(tg_PjRiskAssessment.geteCode());
			tg_PjRiskAssessmentExportExcel.setTheNameOfCityRegion(tg_PjRiskAssessment.getTheNameOfCityRegion());

			String theCompanyName = "";

			if (null != tg_PjRiskAssessment.getDevelopCompany())
			{
				theCompanyName = tg_PjRiskAssessment.getDevelopCompany().getTheName();
			}

			tg_PjRiskAssessmentExportExcel.setTheCompanyName(theCompanyName);

			tg_PjRiskAssessmentExportExcel.setTheNameOfProject(tg_PjRiskAssessment.getTheNameOfProject());
			tg_PjRiskAssessmentExportExcel.setAssessDate(tg_PjRiskAssessment.getAssessDate());
			tg_PjRiskAssessmentExportExcel.setRiskAssessment(tg_PjRiskAssessment.getRiskAssessment());

			String createUserName = "";

			if (null != tg_PjRiskAssessment.getUserStart())
			{
				createUserName = tg_PjRiskAssessment.getUserStart().getTheName();
			}

			tg_PjRiskAssessmentExportExcel.setCreateUserName(createUserName);
			
			tg_PjRiskAssessmentExportExcel.setCreateTimeStamp(MyDatetime.getInstance().dateToSimpleString(tg_PjRiskAssessment.getCreateTimeStamp()));

			tg_PjRiskAssessmentExportExcelList.add(tg_PjRiskAssessmentExportExcel);
		}
		return tg_PjRiskAssessmentExportExcelList;
	}
}

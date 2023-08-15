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
import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tg_PjRiskRatingExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：日记账统计-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_PjRiskRatingExportExcelService
{
	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	MyDatetime myDatetime = MyDatetime.getInstance();
	
	private static final String excelName = "风险评判";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskRatingForm model)
	{
		Properties properties = new MyProperties();
		
		Long developCompanyId = model.getDevelopCompanyId();//开发企业Id
		Long projectId = model.getProjectId();//获取项目Id
		Long cityRegionId = model.getCityRegionId();//获取区域Id
		String keyword = model.getKeyword();
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}
		
		if (null != developCompanyId)
		{
			// 查询开发企业
			Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(developCompanyId);
			if(emmp_CompanyInfo == null){
				return MyBackInfo.fail(properties, "为查询到有效的开发企业数据！");
			}else{
				model.setDevelopCompany(emmp_CompanyInfo);
			}
		}
		
		if (null != projectId)
		{
			//查询预售项目
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			if(empj_ProjectInfo == null){
				return MyBackInfo.fail(properties, "为查询到有效的项目数据！");
			}else{
				model.setProject(empj_ProjectInfo);
			}
		}
		
		if (null != cityRegionId)
		{
			//查询所属区域
			Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			if(sm_CityRegionInfo == null){
				return MyBackInfo.fail(properties, "为查询到有效的区域！");
			}else{
				model.setCityRegion(sm_CityRegionInfo);
			}
		}
		
		String operateDateBegin = model.getOperateDateBegin();//风险评级日期-开始
		String operateDateEnd = model.getOperateDateEnd();//风险评级日期-结束
		
		if (null == operateDateBegin || operateDateBegin.trim().isEmpty())
		{
			model.setOperateDateBegin(null);
		}
		
		if (null == operateDateEnd || operateDateEnd.trim().isEmpty())
		{
			model.setOperateDateEnd(null);
		}
		
		if (null == model.getTheLevel() || model.getTheLevel().isEmpty())
		{
			model.setTheLevel(null);
		}
			
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = tg_PjRiskRatingDao.findByPage_Size(tg_PjRiskRatingDao.getQuery_Size(tg_PjRiskRatingDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_PjRiskRating> tg_PjRiskRatingList;
		if(totalCount > 0)
		{
			tg_PjRiskRatingList = tg_PjRiskRatingDao.findByPage(tg_PjRiskRatingDao.getQuery(tg_PjRiskRatingDao.getBasicHQL(), model));
			
			
			
			
			
			
		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		
		// 初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String relativeDir = directoryUtil.createRelativePathWithDate("Tg_PjRiskRatingExportExcelService");// 文件在项目中的相对路径
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

		writer.addHeaderAlias("eCode", "评级单号");
		writer.addHeaderAlias("theNameOfCityRegion", "所属区域");
		writer.addHeaderAlias("companyName", "开发企业名称");
		writer.addHeaderAlias("projectName", "项目名称");
		writer.addHeaderAlias("operateDate", "评级日期");

		writer.addHeaderAlias("theLevel", "评级级别");
		writer.addHeaderAlias("userUpdate", "操作人");
		writer.addHeaderAlias("lastUpdateTimeStamp", "操作日期");

		List<Tg_PjRiskRatingExportExcelVO> list = formart(tg_PjRiskRatingList);
		// 一次性写出内容，使用默认样式
		writer.write(list);

		// 关闭writer，释放内存
		writer.flush();
		writer.close();

		properties.put("fileName", strNewFileName);
		properties.put("fileURL", relativeDir + strNewFileName);
		properties.put("fullFileName", saveFilePath);
		
			

			
		

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * po 格式化
	 * 
	 * @param tg_JournalCount_ViewList
	 * @return
	 */
	List<Tg_PjRiskRatingExportExcelVO> formart(List<Tg_PjRiskRating> tg_PjRiskRatingList)
	{
		List<Tg_PjRiskRatingExportExcelVO> tg_PjRiskRatingExportExcelList = new ArrayList<Tg_PjRiskRatingExportExcelVO>();
		int ordinal = 0;
		for (Tg_PjRiskRating tg_PjRiskRating : tg_PjRiskRatingList)
		{
			++ordinal;
			Tg_PjRiskRatingExportExcelVO tg_PjRiskRatingExportExcelVO = new Tg_PjRiskRatingExportExcelVO();
			tg_PjRiskRatingExportExcelVO.setOrdinal(ordinal);

			tg_PjRiskRatingExportExcelVO.seteCode(tg_PjRiskRating.geteCode());
			tg_PjRiskRatingExportExcelVO.setTheNameOfCityRegion(tg_PjRiskRating.getCityRegion().getTheName());
			tg_PjRiskRatingExportExcelVO.setCompanyName(tg_PjRiskRating.getDevelopCompany().getTheName());
			tg_PjRiskRatingExportExcelVO.setProjectName(tg_PjRiskRating.getProject().getTheName());
			tg_PjRiskRatingExportExcelVO.setOperateDate(tg_PjRiskRating.getOperateDate());
			
			String level = tg_PjRiskRating.getTheLevel();
			if( null != level && "0".equals(level))
			{
				tg_PjRiskRatingExportExcelVO.setTheLevel("高");
			}
			else if( null != level && "1".equals(level) )
			{
				tg_PjRiskRatingExportExcelVO.setTheLevel("中");
			}
			else if( null != level && "2".equals(level) )
			{
				tg_PjRiskRatingExportExcelVO.setTheLevel("低");
			}
			else
			{
				tg_PjRiskRatingExportExcelVO.setTheLevel(" ");
			}
			
			tg_PjRiskRatingExportExcelVO.setUserUpdate(tg_PjRiskRating.getUserUpdate().getTheName());
			tg_PjRiskRatingExportExcelVO.setLastUpdateTimeStamp(myDatetime.dateToSimpleString(tg_PjRiskRating.getLastUpdateTimeStamp()));

			tg_PjRiskRatingExportExcelList.add(tg_PjRiskRatingExportExcelVO);
		}
		return tg_PjRiskRatingExportExcelList;
	}
}

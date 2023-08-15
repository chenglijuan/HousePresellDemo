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
import zhishusz.housepresell.controller.form.Tg_projectDetailOfBrowse_ViewForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_projectDetailOfBrowse_ViewDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_projectDetailOfBrowse_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_projectDetailOfBrowse_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目详情一览表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_ProjectDetailOfBrowse_ViewExportExcelService
{
	@Autowired
	private Tg_projectDetailOfBrowse_ViewDao tg_projectDetailOfBrowse_ViewDao;

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	private static final String excelName = "托管项目详情一览表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_projectDetailOfBrowse_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		String querdate = model.getQueryDate();// 查询时间

		Long projectId = model.getProjectId();// 项目Id

		Long cityRegionId = model.getCityRegionId();// 区域Id

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == projectId || projectId < 1)
		{
			model.setProjectName(null);
		}
		else
		{
			// 查询项目信息
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			if (null == empj_ProjectInfo)
			{
				return MyBackInfo.fail(properties, "未查询到有效的项目信息");
			}

			model.setProjectName(empj_ProjectInfo.getTheName());
		}

		if (null == cityRegionId || cityRegionId < 1)
		{
			model.setCityRegion(null);
		}
		else
		{
			// 查询区域信息
			Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			if (null == cityRegionInfo)
			{
				return MyBackInfo.fail(properties, "未查询到有效的区域信息");
			}

			model.setCityRegion(cityRegionInfo.getTheName());
		}

		String year = null;// 当前时间年份

		String month = null;// 当前时间月份

		if (null == querdate || querdate.length() < 1)
		{
			// 获取当前日期
			Long currentTimer = System.currentTimeMillis();

			String dateToSimpleString = MyDatetime.getInstance().dateToSimpleString(currentTimer);

			year = dateToSimpleString.split("-")[0];// 获取当前时间年份

			month = dateToSimpleString.split("-")[1];// 获取当前时间月份
		}
		else
		{
			year = querdate.split("-")[0];// 获取当前时间年份

			month = querdate.split("-")[1];// 获取当前时间月份
		}

		model.setQueryYear(year);

		model.setQueryMonth(month);

		List<Tg_projectDetailOfBrowse_View> tg_ProjectDetailOfBrowse_ViewList = tg_projectDetailOfBrowse_ViewDao
				.findByPage(tg_projectDetailOfBrowse_ViewDao.getQuery(tg_projectDetailOfBrowse_ViewDao.getBasicHQL(),
						model));

		if (tg_ProjectDetailOfBrowse_ViewList == null || tg_ProjectDetailOfBrowse_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil
					.createRelativePathWithDate("Tg_ProjectDetailOfBrowse_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("cityRegion", "区域");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("eCodeFromConstruction", "施工楼幢编号");
			writer.addHeaderAlias("forEcastArea", "建筑面积");
			writer.addHeaderAlias("escrowArea", "托管面积");

			writer.addHeaderAlias("recordAveragePrice", "物价备案均价");
			writer.addHeaderAlias("houseTotal", "总户数");
			writer.addHeaderAlias("produceOfProject", "项目介绍");

			List<Tg_projectDetailOfBrowse_ViewExportExcelVO> list = formart(tg_ProjectDetailOfBrowse_ViewList);
			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();
			
			writer.autoSizeColumn(0, true);
			writer.autoSizeColumn(1, true);
			writer.autoSizeColumn(2, true);
			writer.autoSizeColumn(3, true);
			writer.autoSizeColumn(4, true);
			
			writer.autoSizeColumn(5, true);
			writer.autoSizeColumn(6, true);
			writer.autoSizeColumn(7, true);
			writer.autoSizeColumn(8, true);
			
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
	 * @param tg_ProjectDetailOfBrowse_ViewList
	 * @return
	 */
	List<Tg_projectDetailOfBrowse_ViewExportExcelVO> formart(
			List<Tg_projectDetailOfBrowse_View> tg_projectDetailOfBrowse_ViewList)
	{
		List<Tg_projectDetailOfBrowse_ViewExportExcelVO> tg_projectDetailOfBrowse_ViewExportExcelList = new ArrayList<Tg_projectDetailOfBrowse_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_projectDetailOfBrowse_View tg_projectDetailOfBrowse_View : tg_projectDetailOfBrowse_ViewList)
		{
			++ordinal;
			Tg_projectDetailOfBrowse_ViewExportExcelVO tg_projectDetailOfBrowse_ViewExportExcel = new Tg_projectDetailOfBrowse_ViewExportExcelVO();
			tg_projectDetailOfBrowse_ViewExportExcel.setOrdinal(ordinal);

			tg_projectDetailOfBrowse_ViewExportExcel.setCityRegion(tg_projectDetailOfBrowse_View.getCityRegion());
			tg_projectDetailOfBrowse_ViewExportExcel.setProjectName(tg_projectDetailOfBrowse_View.getProjectName());
			tg_projectDetailOfBrowse_ViewExportExcel
					.setECodeFromConstruction(tg_projectDetailOfBrowse_View.geteCodeFromConstruction());
			tg_projectDetailOfBrowse_ViewExportExcel.setForEcastArea(tg_projectDetailOfBrowse_View.getForEcastArea());
			tg_projectDetailOfBrowse_ViewExportExcel.setEscrowArea(tg_projectDetailOfBrowse_View.getEscrowArea());
			tg_projectDetailOfBrowse_ViewExportExcel
					.setRecordAveragePrice(tg_projectDetailOfBrowse_View.getRecordAveragePrice());
			tg_projectDetailOfBrowse_ViewExportExcel
					.setHouseTotal(tg_projectDetailOfBrowse_View.getHouseTotal());
			tg_projectDetailOfBrowse_ViewExportExcel
					.setProduceOfProject(tg_projectDetailOfBrowse_View.getProduceOfProject());

			tg_projectDetailOfBrowse_ViewExportExcelList.add(tg_projectDetailOfBrowse_ViewExportExcel);
		}
		return tg_projectDetailOfBrowse_ViewExportExcelList;
	}
}

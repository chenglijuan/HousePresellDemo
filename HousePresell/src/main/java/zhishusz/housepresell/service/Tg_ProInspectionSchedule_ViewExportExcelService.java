package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_ProInspectionSchedule_ViewForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_ProInspectionSchedule_ViewDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_ProInspectionSchedule_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_ProInspectionSchedule_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service 导出：项目巡查预测计划表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_ProInspectionSchedule_ViewExportExcelService
{

	@Autowired
	private Tg_ProInspectionSchedule_ViewDao tg_ProInspectionSchedule_ViewDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	private static final String excelName = "项目巡查预测计划表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_ProInspectionSchedule_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long projectId = model.getProjectId();// 项目ID
		Long cityRegionId = model.getCityRegionId();//区域ID
		
		String progressOfUpdateTime = model.getProgressOfUpdateTime();//当前更新进度时间

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}
		
		if (null == progressOfUpdateTime || progressOfUpdateTime.length() == 0)
		{
			model.setProgressOfUpdateTime(null);
		}
		
		if (null == cityRegionId || cityRegionId == 0)
		{
			model.setCityRegion(null);
		}
		else
		{
			Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			model.setCityRegion(cityRegionInfo.getTheName());
		}
		
		if (null == projectId || projectId == 0)
		{
			model.setProjectName(null);
		}
		else
		{
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			model.setProjectName(empj_ProjectInfo.getTheName());
		}

		List<Tg_ProInspectionSchedule_View> tg_ProInspectionSchedule_ViewList = tg_ProInspectionSchedule_ViewDao.findByPage(
				tg_ProInspectionSchedule_ViewDao.getQuery(tg_ProInspectionSchedule_ViewDao.getBasicHQL(), model));
		
		if (tg_ProInspectionSchedule_ViewList == null || tg_ProInspectionSchedule_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			//初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_ProInspectionSchedule_ViewExportExcelService");//文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();//项目路径
			
			String saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
			
			if (saveDirectory.contains("%20"))
			{
				saveDirectory = saveDirectory.replace("%20", " ");
			}
			
			directoryUtil.mkdir(saveDirectory);
			
			String strNewFileName = excelName + "-"
					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(),"yyyyMMddHHmmss")
					+ ".xlsx";
			
			String saveFilePath = saveDirectory + strNewFileName;
			
			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
			
			//自定义字段别名
			writer.addHeaderAlias("ordinal", "");
			
			writer.addHeaderAlias("cityRegion", "区域");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("eCodeFromConstruction", "托管楼幢");
			writer.addHeaderAlias("upTotalFloorNumber", "地上总层数");
			writer.addHeaderAlias("currentLimitedNote", "当前受限节点");
			
			writer.addHeaderAlias("currentBuildProgress", "当前建设进度");
			writer.addHeaderAlias("progressOfUpdateTime", "进度更新时间");
			writer.addHeaderAlias("nextChangeNode", "下一变更节点");
			writer.addHeaderAlias("forecastNextChangeTime", "预测变更时间");
			writer.addHeaderAlias("preSalePermits", "预售证领取情况");
			
			List<Tg_ProInspectionSchedule_ViewExportExcelVO> list = formart(tg_ProInspectionSchedule_ViewList);
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
			writer.autoSizeColumn(9, true);
			
			writer.autoSizeColumn(10, true);
			
			writer.close();
			
			
			properties.put("fileName", strNewFileName);
			properties.put("fileURL", relativeDir+strNewFileName);
			properties.put("fullFileName", saveFilePath);
		}
		
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	/**
	 * po 格式化
	 * @param Tg_ProInspectionSchedule_ViewList
	 * @return
	 */
	List<Tg_ProInspectionSchedule_ViewExportExcelVO> formart(List<Tg_ProInspectionSchedule_View> tg_ProInspectionSchedule_ViewList){
		List<Tg_ProInspectionSchedule_ViewExportExcelVO>  tg_ProInspectionSchedule_ViewExportExcelList = new ArrayList<Tg_ProInspectionSchedule_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_ProInspectionSchedule_View tg_ProInspectionSchedule_View : tg_ProInspectionSchedule_ViewList)
		{
			++ordinal;
			Tg_ProInspectionSchedule_ViewExportExcelVO tg_ProInspectionSchedule_ViewExportExcel = new Tg_ProInspectionSchedule_ViewExportExcelVO();
			tg_ProInspectionSchedule_ViewExportExcel.setOrdinal(ordinal);
			
			tg_ProInspectionSchedule_ViewExportExcel.setCityRegion(tg_ProInspectionSchedule_View.getCityRegion());
			tg_ProInspectionSchedule_ViewExportExcel.setProjectName(tg_ProInspectionSchedule_View.getProjectName());
			tg_ProInspectionSchedule_ViewExportExcel.seteCodeFromConstruction(tg_ProInspectionSchedule_View.geteCodeFromConstruction());
			tg_ProInspectionSchedule_ViewExportExcel.setUpTotalFloorNumber(Integer.toString(tg_ProInspectionSchedule_View.getUpTotalFloorNumber())+"F");
			tg_ProInspectionSchedule_ViewExportExcel.setCurrentLimitedNote(tg_ProInspectionSchedule_View.getCurrentLimitedNote());
			
			tg_ProInspectionSchedule_ViewExportExcel.setCurrentBuildProgress(tg_ProInspectionSchedule_View.getCurrentBuildProgress());
			tg_ProInspectionSchedule_ViewExportExcel.setProgressOfUpdateTime(tg_ProInspectionSchedule_View.getProgressOfUpdateTime());
			tg_ProInspectionSchedule_ViewExportExcel.setNextChangeNode(tg_ProInspectionSchedule_View.getNextChangeNode());
			tg_ProInspectionSchedule_ViewExportExcel.setForecastNextChangeTime(tg_ProInspectionSchedule_View.getForecastNextChangeTime());
			tg_ProInspectionSchedule_ViewExportExcel.setPreSalePermits(tg_ProInspectionSchedule_View.getPreSalePermits());
			
			tg_ProInspectionSchedule_ViewExportExcelList.add(tg_ProInspectionSchedule_ViewExportExcel);
		}
		return tg_ProInspectionSchedule_ViewExportExcelList;
	}
}

package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.controller.form.Tg_WitnessStatisticsForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_WitnessStatisticsDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_WitnessStatistics;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_WitnessStatisticsExportExcelVO;
import zhishusz.housepresell.util.*;

/*
 * Service列表查询：见证报告统计表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_WitnessStatisticsExportExcelService
{
	@Autowired
	private Tg_WitnessStatisticsDao tg_WitnessStatisticsDao;

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	private static final String excelName = "见证报告统计表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_WitnessStatisticsForm model)
	{
		Properties properties = new MyProperties();

		//关键字
		String keyword = model.getKeyword();
		if(null !=keyword && keyword.length()>0){
			model.setKeyword("%" + keyword + "%");
		}else{
			model.setKeyword(null);
		}
		//获取区域名称
		Long projectAreaId=model.getProjectAreaId();
		if(projectAreaId!=null){
			Sm_CityRegionInfo sm_CityRegionInfo=sm_CityRegionInfoDao.findById(projectAreaId);
			if(sm_CityRegionInfo!=null){
				model.setProjectArea(sm_CityRegionInfo.getTheName());
			}else{
				model.setProjectArea(null);
			}
		}
		//上传报告时间
		String billTimeStamp=model.getBillTimeStamp()==null?null:model.getBillTimeStamp().trim();
		model.setBillTimeStamp(billTimeStamp);
		String endBillTimeStamp=model.getEndBillTimeStamp()==null?null:model.getEndBillTimeStamp().trim();
		model.setEndBillTimeStamp(endBillTimeStamp);

		//监理公司名称
		Long supervisionCompanyId= model.getSupervisionCompanyId();
		if(supervisionCompanyId!=null){
			Emmp_CompanyInfo emmp_CompanyInfo=	emmp_CompanyInfoDao.findById(supervisionCompanyId);
			if(emmp_CompanyInfo!=null){
				model.setSupervisionCompany(emmp_CompanyInfo.getTheName());
			}else{
				model.setSupervisionCompany(null);
			}
		}
		//项目名称
		Long projectNameId= model.getProjectNameId();
		if(projectNameId!=null){
			Empj_ProjectInfo empj_ProjectInfo=empj_ProjectInfoDao.findById(projectNameId);
			if(empj_ProjectInfo!=null){
				model.setProjectName(empj_ProjectInfo.getTheName());
			}else{
				model.setProjectName(null);
			}
		}

		List<Tg_WitnessStatistics> tg_WitnessStatisticsList = tg_WitnessStatisticsDao.findByPage(tg_WitnessStatisticsDao.getQuery(tg_WitnessStatisticsDao.getBasicHQL(), model));

		if (tg_WitnessStatisticsList == null || tg_WitnessStatisticsList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			//初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_WitnessStatisticsExportExcelService");//文件在项目中的相对路径
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

			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("projectArea", "项目区域");
			writer.addHeaderAlias("constructionNumber", "施工编号");
			writer.addHeaderAlias("witnessNode", "见证节点");
			writer.addHeaderAlias("supervisionCompany", "监理公司");

			writer.addHeaderAlias("witnessAppoinTime", "见证预约时间");
			writer.addHeaderAlias("reportUploadTime", "报告上传时间");

			List<Tg_WitnessStatisticsExportExcelVO> list = formart(tg_WitnessStatisticsList);
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
	 * @param tg_WitnessStatisticsList
	 * @return
	 */
	List<Tg_WitnessStatisticsExportExcelVO> formart(List<Tg_WitnessStatistics> tg_WitnessStatisticsList){
		List<Tg_WitnessStatisticsExportExcelVO>  tg_WitnessStatisticsViewExportExcelList = new ArrayList<Tg_WitnessStatisticsExportExcelVO>();
		int ordinal = 0;
		for (Tg_WitnessStatistics tg_WitnessStatisticsView : tg_WitnessStatisticsList)
		{
			++ordinal;
			Tg_WitnessStatisticsExportExcelVO tg_WitnessStatisticsViewExportExcel = new Tg_WitnessStatisticsExportExcelVO();
			tg_WitnessStatisticsViewExportExcel.setOrdinal(ordinal);

			tg_WitnessStatisticsViewExportExcel.setProjectName(tg_WitnessStatisticsView.getProjectName());
			tg_WitnessStatisticsViewExportExcel.setProjectArea(tg_WitnessStatisticsView.getProjectArea());
			tg_WitnessStatisticsViewExportExcel.setConstructionNumber(tg_WitnessStatisticsView.getConstructionNumber());
			tg_WitnessStatisticsViewExportExcel.setWitnessNode(tg_WitnessStatisticsView.getWitnessNode());
			tg_WitnessStatisticsViewExportExcel.setSupervisionCompany(tg_WitnessStatisticsView.getSupervisionCompany());
			tg_WitnessStatisticsViewExportExcel.setWitnessAppoinTime(tg_WitnessStatisticsView.getWitnessAppoinTime());
			tg_WitnessStatisticsViewExportExcel.setReportUploadTime(tg_WitnessStatisticsView.getReportUploadTime());

			tg_WitnessStatisticsViewExportExcelList.add(tg_WitnessStatisticsViewExportExcel);
		}
		return tg_WitnessStatisticsViewExportExcelList;
	}
}

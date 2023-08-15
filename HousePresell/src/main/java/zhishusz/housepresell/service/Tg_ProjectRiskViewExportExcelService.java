package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tg_ProjectRiskForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_ProjectRiskDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_ProjectRiskView;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_ProjectRiskViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 导出：项目风险明细表 - 导出EXCEL
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_ProjectRiskViewExportExcelService
{

	@Autowired
	private Tg_ProjectRiskDao tg_ProjectRiskDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	private static final String excelName = "项目风险明细表";

	public Properties execute(Tg_ProjectRiskForm model)
	{

		Properties properties = new MyProperties();

		// 托管满额率
		String keyword = model.getKeyword();
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setHostingFullRate(keyword);
		}

		// 日期
		String dateRange = model.getDateRange();
		if (null == dateRange || dateRange.trim().isEmpty())
		{
			dateRange = null;
		}
		else
		{
			dateRange = dateRange.trim();
		}

		// 所属区域
		Long areaId = model.getAreaId();
		if (areaId != null && areaId > 0)
		{
			Sm_CityRegionInfo cityRegionInfo = sm_CityRegionInfoDao.findById(areaId);
			if (null == cityRegionInfo)
			{
				areaId = 0L;
			}
		}
		else
		{
			areaId = 0L;
		}

		// 托管项目
		Long managedProjectsId = model.getManagedProjectsId();

		if (managedProjectsId != null && managedProjectsId > 0)
		{
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(managedProjectsId);
			if (null == projectInfo)
			{
				managedProjectsId = 0L;
			}
		}
		else
		{
			managedProjectsId = 0L;
		}

		// 土地抵押情况 0-未抵押 1-已抵押
		String landMortgage = model.getLandMortgage();
		if (landMortgage == null || landMortgage.trim().isEmpty())
		{
			landMortgage = "99";
		}
		else
		{
			landMortgage = landMortgage.trim();

			switch (landMortgage)
			{
			case "未抵押":
				landMortgage = "0";
				break;

			case "已抵押":
				landMortgage = "1";
				break;

			default:
				landMortgage = "99";
				break;
			}

		}

		String riskRating = model.getRiskRating();
		if (riskRating == null || riskRating.trim().isEmpty())
		{
			riskRating = "99";
		}
		else
		{
			switch (riskRating)
			{
			case "高":
				riskRating = "0";
				break;

			case "中":
				riskRating = "1";
				break;

			case "低":
				riskRating = "2";
				break;
			default:
				riskRating = "99";
				break;
			}
			riskRating = riskRating.trim();
		}
		
		// 查封情况(未签)
		 String attachment = model.getAttachment();
		 if (attachment == null || attachment.trim().isEmpty())
		 {
		 attachment = null;
		 }
		 else
		 {
		 attachment = attachment.trim();
		 }

		 
		// 查封情况（已签）
		 String attachmented = model.getAttachmented();
		 if (attachmented == null || attachmented.trim().isEmpty())
		 {
			 attachmented = null;
		 }
		 else
		 {
			 attachmented = attachmented.trim();
		 }
		 
		// 转换list
		List<Tg_ProjectRiskView> tg_ProjectRiskLists = new ArrayList<Tg_ProjectRiskView>();
		// 接收list
		List<Map<String, String>> listMap = null;
		try
		{
			listMap = tg_ProjectRiskDao.getTg_ProjectRisk(areaId, managedProjectsId, dateRange,
					Long.valueOf(landMortgage), Long.valueOf(riskRating),model.getUserId(),attachment,attachmented);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return MyBackInfo.fail(properties, "查询异常：" + e.getMessage());
		}

		if (listMap == null || listMap.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			Tg_ProjectRiskView po;

			for (Map<String, String> map : listMap)
			{
				po = new Tg_ProjectRiskView();
				po.setAlreadyUnsignedContract(map.get("ALREADYUNSIGNEDCONTRACT"));
				po.setAppointedTime(map.get("APPOINTEDTIME"));
				po.setArea(map.get("AREA"));
				po.setAstrict(map.get("ASTRICT"));
				po.setAttachment(map.get("ATTACHMENT"));
				po.setContractFilingRate(map.get("CONTRACTFILINGRATE"));
				po.setContractLoanRatio(map.get("CONTRACTLOANRATIO"));
				po.setCurrentConstruction(map.get("CURRENTCONSTRUCTION"));
				po.setDateOfPresale(map.get("DATEOFPRESALE"));
				po.setDateQuery(map.get("DATEQUERY"));
				po.setFloorBuilding(map.get("FLOORBUILDING"));
				po.setHostingFullRate(map.get("HOSTINGFULLRATE"));
				po.setLandMortgage(map.get("LANDMORTGAGE"));
				po.setManagedArea(map.get("MANAGEDAREA"));
				po.setManagedProjects(map.get("MANAGEDPROJECTS"));
				po.setOtherRisks(map.get("OTHERRISKS"));
				po.setProgressEvaluation(map.get("PROGRESSEVALUATION"));
				po.setRiskRating(map.get("RISKRATING"));
				po.setSignTheEfficiency(map.get("SIGNTHEEFFICIENCY"));
				po.setTableId(Long.valueOf(map.get("TABLEID")));
				po.setTotalOfoverground(map.get("TOTALOFOVERGROUND"));
				po.setUnsignedContract(map.get("UNSIGNEDCONTRACT"));
				po.setUpdateTime(map.get("UPDATETIME"));
				po.setPayguarantee(map.get("PAYGUARANTEE"));

				tg_ProjectRiskLists.add(po);
			}

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_ProjectRiskViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("managedProjects", "托管项目");
			writer.addHeaderAlias("floorBuilding", "托管楼幢");
			writer.addHeaderAlias("managedArea", "托管面积(平方米)");
			writer.addHeaderAlias("dateOfPresale", "预售许可批准日期");

			writer.addHeaderAlias("totalOfoverground", "地上总层数");
			writer.addHeaderAlias("currentConstruction", "当前建设进度");
			writer.addHeaderAlias("updateTime", "进度更新时间");
			writer.addHeaderAlias("appointedTime", "合同约定交付时间");
			writer.addHeaderAlias("progressEvaluation", "进度评定");

			writer.addHeaderAlias("signTheEfficiency", "合同签订率");
			writer.addHeaderAlias("contractFilingRate", "合同备案率");
			writer.addHeaderAlias("contractLoanRatio", "合同贷款率");
			writer.addHeaderAlias("hostingFullRate", "托管满额率");
			writer.addHeaderAlias("unsignedContract", "未签订合同查封");

			writer.addHeaderAlias("alreadyUnsignedContract", "已签订合同查封");
			writer.addHeaderAlias("astrict", "限制");
			writer.addHeaderAlias("payguarantee", "有无保函");
			writer.addHeaderAlias("landMortgage", "土地抵押情况(有/无)");
			writer.addHeaderAlias("otherRisks", "其他风险");
			writer.addHeaderAlias("riskRating", "风险评级(高/中/低)");

			List<Tg_ProjectRiskViewExportExcelVO> list = formart(tg_ProjectRiskLists);
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
			writer.autoSizeColumn(11, true);
			writer.autoSizeColumn(12, true);
			writer.autoSizeColumn(13, true);
			writer.autoSizeColumn(14, true);
			
			writer.autoSizeColumn(15, true);
			writer.autoSizeColumn(16, true);
			writer.autoSizeColumn(17, true);
			writer.autoSizeColumn(18, true);
			writer.autoSizeColumn(19, true);
			writer.autoSizeColumn(20, true);
			
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
	 * @param tg_ProjectRiskLists
	 * @return
	 */
	List<Tg_ProjectRiskViewExportExcelVO> formart(List<Tg_ProjectRiskView> tg_ProjectRiskLists)
	{
		List<Tg_ProjectRiskViewExportExcelVO> tg_ProjectRiskViewExportExcelList = new ArrayList<Tg_ProjectRiskViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_ProjectRiskView tg_ProjectRiskView : tg_ProjectRiskLists)
		{
			++ordinal;
			Tg_ProjectRiskViewExportExcelVO tg_ProjectRiskViewExportExcel = new Tg_ProjectRiskViewExportExcelVO();
			tg_ProjectRiskViewExportExcel.setOrdinal(ordinal);

			tg_ProjectRiskViewExportExcel.setManagedProjects(tg_ProjectRiskView.getManagedProjects());// 托管项目
			tg_ProjectRiskViewExportExcel.setFloorBuilding(tg_ProjectRiskView.getFloorBuilding());// 托管楼幢
			tg_ProjectRiskViewExportExcel.setManagedArea(tg_ProjectRiskView.getManagedArea());// 托管面积(平方米)
			tg_ProjectRiskViewExportExcel.setDateOfPresale(tg_ProjectRiskView.getDateOfPresale());// 预售许可批准日期
			tg_ProjectRiskViewExportExcel.setTotalOfoverground(tg_ProjectRiskView.getTotalOfoverground());// 地上总层数

			tg_ProjectRiskViewExportExcel.setCurrentConstruction(tg_ProjectRiskView.getCurrentConstruction());// 当前建设进度
			tg_ProjectRiskViewExportExcel.setUpdateTime(tg_ProjectRiskView.getUpdateTime());// 进度更新时间
			tg_ProjectRiskViewExportExcel.setAppointedTime(tg_ProjectRiskView.getAppointedTime());// 合同约定交付时间
			tg_ProjectRiskViewExportExcel.setProgressEvaluation(tg_ProjectRiskView.getProgressEvaluation());// 进度评定
			tg_ProjectRiskViewExportExcel.setSignTheEfficiency(tg_ProjectRiskView.getSignTheEfficiency());// 合同签订率

			tg_ProjectRiskViewExportExcel.setContractFilingRate(tg_ProjectRiskView.getContractFilingRate());// 合同备案率
			tg_ProjectRiskViewExportExcel.setContractLoanRatio(tg_ProjectRiskView.getContractLoanRatio());// 合同贷款率
			tg_ProjectRiskViewExportExcel.setHostingFullRate(tg_ProjectRiskView.getHostingFullRate());// 托管满额率
			tg_ProjectRiskViewExportExcel.setUnsignedContract(tg_ProjectRiskView.getUnsignedContract());// 未签订合同查封
			tg_ProjectRiskViewExportExcel.setAlreadyUnsignedContract(tg_ProjectRiskView.getAlreadyUnsignedContract());// 已签订合同查封

			tg_ProjectRiskViewExportExcel.setAstrict(tg_ProjectRiskView.getAstrict());// 限制
			tg_ProjectRiskViewExportExcel.setPayguarantee(tg_ProjectRiskView.getPayguarantee());// 有无保函(有/无)
			tg_ProjectRiskViewExportExcel.setLandMortgage(tg_ProjectRiskView.getLandMortgage());// 土地抵押情况(有/无)
			tg_ProjectRiskViewExportExcel.setOtherRisks(tg_ProjectRiskView.getOtherRisks());// 其他风险
			tg_ProjectRiskViewExportExcel.setRiskRating(tg_ProjectRiskView.getRiskRating());// 风险评级(高/中/低)

			tg_ProjectRiskViewExportExcelList.add(tg_ProjectRiskViewExportExcel);
		}
		return tg_ProjectRiskViewExportExcelList;
	}
}

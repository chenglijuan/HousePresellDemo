package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_LoanProjectCountM_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_LoanProjectCountM_ViewListDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_LoanProjectCountM_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_LoanProjectCountM_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：托管项目统计表（财务部）-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_LoanProjectCountM_ViewExportExcelService
{
	@Autowired
	private Tg_LoanProjectCountM_ViewListDao tg_LoanProjectCountM_ViewListDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	private static final String excelName = "托管项目统计表（财务部）";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LoanProjectCountM_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字
		Long cityRegionId = model.getCityRegionId();// 区域Id
		Long companyId = model.getCompanyId();// 企业Id
		Long projectId = model.getProjectId();// 项目Id

		String contractSigningDate = model.getContractSigningDate();
		String endContractSigningDate = model.getEndContractSigningDate();
		String preSaleCardDate = model.getPreSaleCardDate();
		String endPreSaleCardDate = model.getEndPreSaleCardDate();

		String eCodeOfAgreement = model.getECodeOfAgreement();// 协议编号

		if (null == eCodeOfAgreement || eCodeOfAgreement.length() == 0)
		{
			model.setECodeOfAgreement(null);
		}
		else
		{
			model.setECodeOfAgreement(eCodeOfAgreement.trim());
		}
		if (null == contractSigningDate || contractSigningDate.length() == 0)
		{
			model.setContractSigningDate(null);
		}
		else
		{
			model.setContractSigningDate(contractSigningDate.trim());
		}
		if (null == endContractSigningDate || endContractSigningDate.length() == 0)
		{
			model.setEndContractSigningDate(null);
		}
		else
		{
			model.setEndContractSigningDate(endContractSigningDate.trim());
		}
		if (null == preSaleCardDate || preSaleCardDate.length() == 0)
		{
			model.setPreSaleCardDate(null);
		}
		else
		{
			model.setPreSaleCardDate(preSaleCardDate.trim());
		}
		if (null == endPreSaleCardDate || endPreSaleCardDate.length() == 0)
		{
			model.setEndPreSaleCardDate(null);
		}
		else
		{
			model.setEndPreSaleCardDate(endPreSaleCardDate.trim());
		}

		if (null == cityRegionId || cityRegionId == 0)
		{
			model.setCityRegion(null);
		}
		else
		{
			Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			model.setCityRegion(sm_CityRegionInfo.getTheName());
		}

		if (null == companyId || companyId == 0)
		{
			model.setCompanyName(null);
		}
		else
		{
			Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(companyId);
			model.setCompanyName(emmp_CompanyInfo.getTheName());
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

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}
		
		List<Tg_LoanProjectCountM_View> tg_LoanProjectCountM_ViewList = tg_LoanProjectCountM_ViewListDao.findByPage(
					tg_LoanProjectCountM_ViewListDao.getQuery(tg_LoanProjectCountM_ViewListDao.getBasicHQL(), model));
		
		if (tg_LoanProjectCountM_ViewList == null || tg_LoanProjectCountM_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据");
		}
		else
		{
			//初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_LoanAmountStatement_ViewExportExcelService");//文件在项目中的相对路径
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
			writer.addHeaderAlias("companyName", "企业信息");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("upTotalFloorNumber", "地上楼层数（总）");
			writer.addHeaderAlias("contractSigningDate", "托管合作协议签订日期");
			
			writer.addHeaderAlias("preSaleCardDate", "预售证日期");
			writer.addHeaderAlias("preSalePermits", "预售许可证");
			writer.addHeaderAlias("eCodeOfAgreement", "协议编号");
			writer.addHeaderAlias("remark", "备注");
			
			List<Tg_LoanProjectCountM_ViewExportExcelVO> list = formart(tg_LoanProjectCountM_ViewList);
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
	 * @param tg_LoanProjectCountM_ViewList
	 * @return
	 */
	List<Tg_LoanProjectCountM_ViewExportExcelVO> formart(List<Tg_LoanProjectCountM_View> tg_LoanProjectCountM_ViewList){
		List<Tg_LoanProjectCountM_ViewExportExcelVO>  tg_LoanProjectCountM_ViewExportExcelList = new ArrayList<Tg_LoanProjectCountM_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_LoanProjectCountM_View tg_LoanProjectCountM_View : tg_LoanProjectCountM_ViewList)
		{
			++ordinal;
			Tg_LoanProjectCountM_ViewExportExcelVO tg_LoanProjectCountM_ViewExportExcel = new Tg_LoanProjectCountM_ViewExportExcelVO();
			tg_LoanProjectCountM_ViewExportExcel.setOrdinal(ordinal);
			
			tg_LoanProjectCountM_ViewExportExcel.setCityRegion(tg_LoanProjectCountM_View.getCityRegion());
			tg_LoanProjectCountM_ViewExportExcel.setCompanyName(tg_LoanProjectCountM_View.getCompanyName());
			tg_LoanProjectCountM_ViewExportExcel.setProjectName(tg_LoanProjectCountM_View.getProjectName());
			tg_LoanProjectCountM_ViewExportExcel.setUpTotalFloorNumber(tg_LoanProjectCountM_View.getUpTotalFloorNumber());
			tg_LoanProjectCountM_ViewExportExcel.setContractSigningDate(tg_LoanProjectCountM_View.getContractSigningDate());
			tg_LoanProjectCountM_ViewExportExcel.setPreSaleCardDate(tg_LoanProjectCountM_View.getPreSaleCardDate());
			tg_LoanProjectCountM_ViewExportExcel.setPreSalePermits(tg_LoanProjectCountM_View.getPreSalePermits());
			tg_LoanProjectCountM_ViewExportExcel.setECodeOfAgreement(tg_LoanProjectCountM_View.geteCodeOfAgreement());
			tg_LoanProjectCountM_ViewExportExcel.setRemark(tg_LoanProjectCountM_View.getRemark());
			
			tg_LoanProjectCountM_ViewExportExcelList.add(tg_LoanProjectCountM_ViewExportExcel);
		}
		return tg_LoanProjectCountM_ViewExportExcelList;
	}
}

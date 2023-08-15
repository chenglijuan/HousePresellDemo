package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_BuildCount_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tg_BuildCount_ViewListDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tg_BuildCount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_BuildCount_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：托管楼幢入账统计表报表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_BuildCount_ViewExportExcelService
{
	@Autowired
	private Tg_BuildCount_ViewListDao tg_BuildCount_ViewListDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	private static final String excelName = "托管楼幢入账统计";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_BuildCount_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long companyId = model.getCompanyId();// 开发企业ID
		Long projectId = model.getProjectId();// 项目ID
		//Long buildId = model.getBuildId();// 楼幢ID

		String startBillTimeStamp = model.getBillTimeStamp();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp();// 记账日期 （结束）

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
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
			model.setCompanyName(empj_ProjectInfo.getTheName());
		}
		/*if (null == buildId || buildId == 0)
		{
			model.setECodeFroMconstruction(null);
		}
		else
		{
			Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildId);
			model.setCompanyName(empj_BuildingInfo.geteCodeFromConstruction());
		}*/
		if (null == startBillTimeStamp || startBillTimeStamp.length() == 0)
		{
			model.setBillTimeStamp(null);
		}
		else
		{
			model.setBillTimeStamp(startBillTimeStamp.trim());
		}
		if (null == endBillTimeStamp || endBillTimeStamp.length() == 0)
		{
			model.setEndBillTimeStamp(null);
		}
		else
		{
			model.setEndBillTimeStamp(endBillTimeStamp.trim());
		}

		List<Tg_BuildCount_View> tg_BuildCount_ViewList = tg_BuildCount_ViewListDao.findByPage(
					tg_BuildCount_ViewListDao.getQuery(tg_BuildCount_ViewListDao.getBasicHQL(), model));
		
		if (tg_BuildCount_ViewList == null || tg_BuildCount_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			//初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_BuildCount_ViewExportExcelService");//文件在项目中的相对路径
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
			
			writer.addHeaderAlias("billTimeStamp", "记账日期");
			writer.addHeaderAlias("companyName", "开发企业");
			writer.addHeaderAlias("projectName", "项目名称");
			/*writer.addHeaderAlias("eCodeFroMconstruction", "楼幢");
			writer.addHeaderAlias("bankName", "银行名称");*/
			
			writer.addHeaderAlias("income", "托管收入");
			writer.addHeaderAlias("payout", "托管支出");
			writer.addHeaderAlias("balance", "余额");
			writer.addHeaderAlias("commercialLoan", "商贷（托管收入）");
			writer.addHeaderAlias("accumulationFund", "公积金（托管收入）");
			
			
			List<Tg_BuildCount_ViewExportExcelVO> list = formart(tg_BuildCount_ViewList);
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
			properties.put("fileURL", relativeDir+strNewFileName);
			properties.put("fullFileName", saveFilePath);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	/**
	 * po 格式化
	 * @param tg_BuildCount_ViewList
	 * @return
	 */
	List<Tg_BuildCount_ViewExportExcelVO> formart(List<Tg_BuildCount_View> tg_BuildCount_ViewList){
		List<Tg_BuildCount_ViewExportExcelVO>  tg_BuildCount_ViewExportExcelList = new ArrayList<Tg_BuildCount_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_BuildCount_View tg_BuildCount_View : tg_BuildCount_ViewList)
		{
			++ordinal;
			Tg_BuildCount_ViewExportExcelVO tg_BuildCount_ViewExportExcel = new Tg_BuildCount_ViewExportExcelVO();
			tg_BuildCount_ViewExportExcel.setOrdinal(ordinal);
			
			tg_BuildCount_ViewExportExcel.setCompanyName(tg_BuildCount_View.getCompanyName());
			tg_BuildCount_ViewExportExcel.setProjectName(tg_BuildCount_View.getProjectName());
			tg_BuildCount_ViewExportExcel.setCommercialLoan(tg_BuildCount_View.getCommercialLoan());
			tg_BuildCount_ViewExportExcel.setAccumulationFund(tg_BuildCount_View.getAccumulationFund());
			tg_BuildCount_ViewExportExcel.setIncome(tg_BuildCount_View.getIncome());
			tg_BuildCount_ViewExportExcel.setPayout(tg_BuildCount_View.getPayout());
			tg_BuildCount_ViewExportExcel.setBalance(tg_BuildCount_View.getBalance());
			tg_BuildCount_ViewExportExcel.setBillTimeStamp(tg_BuildCount_View.getBillTimeStamp());
			
			tg_BuildCount_ViewExportExcelList.add(tg_BuildCount_ViewExportExcel);
		}
		return tg_BuildCount_ViewExportExcelList;
	}
}

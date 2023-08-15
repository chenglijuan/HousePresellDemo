package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_BuildPayout_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tg_BuildPayout_ViewListDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tg_BuildPayout_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_BuildPayout_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：托管楼幢拨付明细统计表报表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_BuildPayout_ViewExportExcelService
{
	@Autowired
	private Tg_BuildPayout_ViewListDao tg_BuildPayout_ViewListDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	private static final String excelName = "托管楼幢拨付明细统计表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_BuildPayout_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = 1;
		Integer countPerPage = 60000;

		// 获取查询条件
		String userid=String.valueOf(model.getUserId());
		String keyword = model.getKeyword();// 关键字
		String companyId = String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = String.valueOf(model.getProjectId());// 项目ID
		String buildId = String.valueOf(model.getBuildId());// 楼幢ID
		
		if (companyId=="null") {
			companyId=null;			
		}
		
		if (projectId=="null") {
			projectId=null;			
		}
		
		if (buildId=="null") {
			buildId=null;			
		}

		String payoutDate = model.getPayoutDate().trim();// 拨付日期 （起始）
		String endPayoutDate = model.getEndPayoutDate().trim();// 拨付日期 （结束）

		

		List<Tg_BuildPayout_View> tg_BuildPayout_ViewList = new ArrayList<Tg_BuildPayout_View>();
		Integer totalPage = 0;
		Integer totalCount = 0;

		if (!payoutDate.isEmpty() && !endPayoutDate.isEmpty()) {
			Map<String, Object> retmap = new HashMap<String, Object>();
			// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
			try {
				retmap = tg_BuildPayout_ViewListDao.getBuildPayout_View(userid,companyId, projectId, buildId, keyword,
						payoutDate, endPayoutDate, pageNumber, countPerPage);
				totalPage = (Integer) retmap.get("totalPage");
				totalCount = (Integer) retmap.get("totalCount");
				tg_BuildPayout_ViewList = (List<Tg_BuildPayout_View>) retmap.get("tg_BuildPayout_ViewList");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if (tg_BuildPayout_ViewList == null || tg_BuildPayout_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_BuildPayout_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("companyName", "开发企业");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("eCodeFroMconstruction", "楼幢");
			writer.addHeaderAlias("currentFigureProgress", "当前形象进度");
			writer.addHeaderAlias("eCodeFromPayoutBill", "资金拨付单号");
			writer.addHeaderAlias("currentApplyPayoutAmount", "本次申请支付金额");

			writer.addHeaderAlias("currentPayoutAmount", "本次实际支付金额");
			writer.addHeaderAlias("payoutDate", "拨付日期");
			writer.addHeaderAlias("payoutBank", "拨付银行");
			writer.addHeaderAlias("payoutBankAccount", "拨付账号");

			List<Tg_BuildPayout_ViewExportExcelVO> list = formart(tg_BuildPayout_ViewList);
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
			properties.put("fileURL", relativeDir + strNewFileName);
			properties.put("fullFileName", saveFilePath);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	
	/**
	 * po 格式化
	 * @param tg_BuildPayout_ViewList
	 * @return
	 */
	List<Tg_BuildPayout_ViewExportExcelVO> formart(List<Tg_BuildPayout_View> tg_BuildPayout_ViewList){
		List<Tg_BuildPayout_ViewExportExcelVO>  tg_BuildPayout_ViewExportExcelList = new ArrayList<Tg_BuildPayout_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_BuildPayout_View tg_BuildPayout_View : tg_BuildPayout_ViewList)
		{
			++ordinal;
			Tg_BuildPayout_ViewExportExcelVO tg_BuildPayout_ViewExportExcel = new Tg_BuildPayout_ViewExportExcelVO();
			tg_BuildPayout_ViewExportExcel.setOrdinal(ordinal);
			
			tg_BuildPayout_ViewExportExcel.setCompanyName(tg_BuildPayout_View.getCompanyName());//企业名称
			tg_BuildPayout_ViewExportExcel.setProjectName(tg_BuildPayout_View.getProjectName());//项目名称
			tg_BuildPayout_ViewExportExcel.setECodeFroMconstruction(tg_BuildPayout_View.geteCodeFroMconstruction());//楼幢
			tg_BuildPayout_ViewExportExcel.setCurrentFigureProgress(tg_BuildPayout_View.getCurrentFigureProgress());//当前形象进度 
			tg_BuildPayout_ViewExportExcel.setECodeFromPayoutBill(tg_BuildPayout_View.geteCodeFromPayoutBill());//资金拨付单号
			tg_BuildPayout_ViewExportExcel.setCurrentApplyPayoutAmount(tg_BuildPayout_View.getCurrentApplyPayoutAmount());//本次申请支付金额
			tg_BuildPayout_ViewExportExcel.setCurrentPayoutAmount(tg_BuildPayout_View.getCurrentPayoutAmount());//本次实际拨付金额
			tg_BuildPayout_ViewExportExcel.setPayoutDate(tg_BuildPayout_View.getPayoutDate());//拨付日期
			tg_BuildPayout_ViewExportExcel.setPayoutBank(tg_BuildPayout_View.getPayoutBank());//拨付银行
			tg_BuildPayout_ViewExportExcel.setPayoutBankAccount(tg_BuildPayout_View.getPayoutBankAccount());//拨付账号
			
			tg_BuildPayout_ViewExportExcelList.add(tg_BuildPayout_ViewExportExcel);
		}
		return tg_BuildPayout_ViewExportExcelList;
	}
}

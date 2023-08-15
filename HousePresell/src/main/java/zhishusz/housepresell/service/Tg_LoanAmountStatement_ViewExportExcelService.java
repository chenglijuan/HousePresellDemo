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

import zhishusz.housepresell.controller.form.Tg_LoanAmountStatement_ViewForm;
import zhishusz.housepresell.database.dao.Tg_LoanAmountStatement_ViewListDao;
import zhishusz.housepresell.database.po.Tg_LoanAmountStatement_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_LoanAmountStatement_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：托管现金流量表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_LoanAmountStatement_ViewExportExcelService
{
	@Autowired
	private Tg_LoanAmountStatement_ViewListDao tg_LoanAmountStatement_ViewListDao;

	private static final String excelName = "托管现金流量表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LoanAmountStatement_ViewForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = 1;
		Integer countPerPage = 99999999;

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字
		String startBillTimeStamp = model.getBillTimeStamp().trim();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp().trim();// 记账日期 （结束）
		
		String queryKind = model.getQueryKind();

		if (null == queryKind || queryKind.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "查询类别为空");
		}

		List<Tg_LoanAmountStatement_View> tg_LoanAmountStatement_ViewList = new ArrayList<Tg_LoanAmountStatement_View>();
		
		if (!startBillTimeStamp.isEmpty() && !endBillTimeStamp.isEmpty()) {
			Map<String, Object> retmap = new HashMap<String, Object>();		
			// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
			try {
				retmap = tg_LoanAmountStatement_ViewListDao.getLoanAmountStatement_View(keyword, startBillTimeStamp, endBillTimeStamp, pageNumber, countPerPage, queryKind);
				tg_LoanAmountStatement_ViewList = (List<Tg_LoanAmountStatement_View>) retmap.get("qs_ProjectEscrowAmount_ViewLists");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		

		if (tg_LoanAmountStatement_ViewList == null || tg_LoanAmountStatement_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil
					.createRelativePathWithDate("Tg_LoanAmountStatement_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("billTimeStamp", "日期");
			writer.addHeaderAlias("lastAmount", "上期结余");
			writer.addHeaderAlias("loanAmountIn", "托管资金入账金额");
			writer.addHeaderAlias("depositExpire", "存单到期");
			writer.addHeaderAlias("payoutAmount", "资金拨付金额");
			writer.addHeaderAlias("depositReceipt", "存单存入");			
			writer.addHeaderAlias("currentBalance", "活期余额");

			List<Tg_LoanAmountStatement_ViewExportExcelVO> list = formart(tg_LoanAmountStatement_ViewList);
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
	 * @param tg_LoanAmountStatement_ViewList
	 * @return
	 */
	List<Tg_LoanAmountStatement_ViewExportExcelVO> formart(
			List<Tg_LoanAmountStatement_View> tg_LoanAmountStatement_ViewList)
	{
		List<Tg_LoanAmountStatement_ViewExportExcelVO> tg_LoanAmountStatement_ViewExportExcelList = new ArrayList<Tg_LoanAmountStatement_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_LoanAmountStatement_View tg_LoanAmountStatement_View : tg_LoanAmountStatement_ViewList)
		{
			++ordinal;
			Tg_LoanAmountStatement_ViewExportExcelVO tg_LoanAmountStatement_ViewExportExcel = new Tg_LoanAmountStatement_ViewExportExcelVO();
			tg_LoanAmountStatement_ViewExportExcel.setOrdinal(ordinal);

			tg_LoanAmountStatement_ViewExportExcel.setBillTimeStamp(tg_LoanAmountStatement_View.getBillTimeStamp());
			tg_LoanAmountStatement_ViewExportExcel.setLastAmount(null==tg_LoanAmountStatement_View.getLastAmount()?0.00:tg_LoanAmountStatement_View.getLastAmount());
			tg_LoanAmountStatement_ViewExportExcel.setLoanAmountIn(null==tg_LoanAmountStatement_View.getLoanAmountIn()?0.00:tg_LoanAmountStatement_View.getLoanAmountIn());
			tg_LoanAmountStatement_ViewExportExcel.setDepositReceipt(null==tg_LoanAmountStatement_View.getDepositReceipt()?0.00:tg_LoanAmountStatement_View.getDepositReceipt());
			tg_LoanAmountStatement_ViewExportExcel.setPayoutAmount(null==tg_LoanAmountStatement_View.getPayoutAmount()?0.00:tg_LoanAmountStatement_View.getPayoutAmount());
			tg_LoanAmountStatement_ViewExportExcel.setDepositExpire(null==tg_LoanAmountStatement_View.getDepositExpire()?0.00:tg_LoanAmountStatement_View.getDepositExpire());
			tg_LoanAmountStatement_ViewExportExcel.setCurrentBalance(null==tg_LoanAmountStatement_View.getCurrentBalance()?0.00:tg_LoanAmountStatement_View.getCurrentBalance());

			tg_LoanAmountStatement_ViewExportExcelList.add(tg_LoanAmountStatement_ViewExportExcel);
		}
		return tg_LoanAmountStatement_ViewExportExcelList;
	}
}

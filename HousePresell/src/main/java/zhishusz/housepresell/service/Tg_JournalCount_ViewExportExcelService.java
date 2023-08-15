package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_JournalCount_ViewForm;
import zhishusz.housepresell.database.dao.Tg_JournalCount_ViewListDao;
import zhishusz.housepresell.database.po.Tg_JournalCount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_JournalCount_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：日记账统计-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_JournalCount_ViewExportExcelService
{
	@Autowired
	private Tg_JournalCount_ViewListDao tg_JournalCount_ViewListDao;

	private static final String excelName = "日记账统计";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_JournalCount_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		String startLoanInDate = model.getLoanInDate();

		String endLoanInDate = model.getEndLoanInDate();

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == startLoanInDate || startLoanInDate.length() == 0)
		{
			model.setLoanInDate(null);
		}
		else
		{
			model.setLoanInDate(startLoanInDate.trim());
		}
		if (null == endLoanInDate || endLoanInDate.length() == 0)
		{
			model.setEndLoanInDate(null);
		}
		else
		{
			model.setEndLoanInDate(endLoanInDate.trim());
		}
		
		String escrowAcountNameBak = StrUtil.isBlank(model.getEscrowAcountName()) ? null : model.getEscrowAcountName().trim();
		model.setEscrowAcountName(escrowAcountNameBak);

		List<Tg_JournalCount_View> tg_JournalCount_ViewList = tg_JournalCount_ViewListDao
				.findByPage(tg_JournalCount_ViewListDao.getQuery(tg_JournalCount_ViewListDao.getBasicHQL(), model));

		if (tg_JournalCount_ViewList == null || tg_JournalCount_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			Tg_JournalCount_View tg_JournalCount = new Tg_JournalCount_View();

			String loanInDate = "总计";// 入账日期
			String escrowAcountName = "";// 托管账户名称
			Integer tradeCount = 0;// 确认总笔数
			Double totalTradeAmount = 0.0;// 确认总金额
			Integer aToatlLoanAmoutCount = 0;// 公积金贷款总笔数
			Double aToatlLoanAmout = 0.0;// 公积金贷款总金额
			Integer bToatlLoanAmoutCount = 0;// 商业贷款总笔数
			Double bToatlLoanAmout = 0.0;// 商业贷款总金额
			Integer oToatlLoanAmoutCount = 0;// 自有资金总笔数
			Double oToatlLoanAmout = 0.0;// 自有资金总金额
			Integer aFristToatlLoanAmoutCount = 0;// 公积金首付款总笔数
			Double aFristToatlLoanAmout = 0.0;// 公积金首付款总金额
			Integer aToBusinessToatlLoanAmoutCount = 0;// 公转商贷款总笔数
			Double aToBusinessToatlLoanAmout = 0.0;// 公转商贷款总金额

			if (tg_JournalCount_ViewList.size() > 0)
			{
				for (Tg_JournalCount_View tg_JournalCount_View : tg_JournalCount_ViewList)
				{
					tradeCount = tradeCount + tg_JournalCount_View.getTradeCount();
					totalTradeAmount = MyDouble.getInstance().doubleAddDouble(totalTradeAmount,
							tg_JournalCount_View.getTotalTradeAmount());
					aToatlLoanAmoutCount = aToatlLoanAmoutCount + tg_JournalCount_View.getaToatlLoanAmoutCount();
					aToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(aToatlLoanAmout,
							tg_JournalCount_View.getaToatlLoanAmout());
					bToatlLoanAmoutCount = bToatlLoanAmoutCount + tg_JournalCount_View.getbToatlLoanAmoutCount();
					bToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(bToatlLoanAmout,
							tg_JournalCount_View.getbToatlLoanAmout());
					oToatlLoanAmoutCount = oToatlLoanAmoutCount + tg_JournalCount_View.getoToatlLoanAmoutCount();
					oToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(oToatlLoanAmout,
							tg_JournalCount_View.getoToatlLoanAmout());
					aFristToatlLoanAmoutCount = aFristToatlLoanAmoutCount
							+ tg_JournalCount_View.getaFristToatlLoanAmoutCount();
					aFristToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(aFristToatlLoanAmout,
							tg_JournalCount_View.getaFristToatlLoanAmout());
					aToBusinessToatlLoanAmoutCount = aToBusinessToatlLoanAmoutCount
							+ tg_JournalCount_View.getaToBusinessToatlLoanAmoutCount();
					aToBusinessToatlLoanAmout = MyDouble.getInstance().doubleAddDouble(aToBusinessToatlLoanAmout,
							tg_JournalCount_View.getaToBusinessToatlLoanAmout());
				}
			}
			tg_JournalCount.setLoanInDate(loanInDate);
			tg_JournalCount.setEscrowAcountName(escrowAcountName);
			tg_JournalCount.setTradeCount(tradeCount);
			tg_JournalCount.setTotalTradeAmount(totalTradeAmount);
			tg_JournalCount.setaToatlLoanAmout(aToatlLoanAmout);
			tg_JournalCount.setaToatlLoanAmoutCount(aToatlLoanAmoutCount);
			tg_JournalCount.setbToatlLoanAmout(bToatlLoanAmout);
			tg_JournalCount.setbToatlLoanAmoutCount(bToatlLoanAmoutCount);
			tg_JournalCount.setoToatlLoanAmout(oToatlLoanAmout);
			tg_JournalCount.setoToatlLoanAmoutCount(oToatlLoanAmoutCount);
			tg_JournalCount.setaFristToatlLoanAmout(aFristToatlLoanAmout);
			;
			tg_JournalCount.setaFristToatlLoanAmoutCount(aFristToatlLoanAmoutCount);
			;
			tg_JournalCount.setaToBusinessToatlLoanAmout(aToBusinessToatlLoanAmout);
			tg_JournalCount.setaToBusinessToatlLoanAmoutCount(aToBusinessToatlLoanAmoutCount);

			tg_JournalCount_ViewList.add(tg_JournalCount);

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_JournalCount_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("loanInDate", "入账日期");
			writer.addHeaderAlias("escrowAcountName", "托管账户名称");
			writer.addHeaderAlias("tradeCount", "确认总笔数");
			writer.addHeaderAlias("totalTradeAmount", "确认总金额");
			writer.addHeaderAlias("aToatlLoanAmoutCount", "公积金贷款总笔数");

			writer.addHeaderAlias("aToatlLoanAmout", "公积金贷款总金额");
			writer.addHeaderAlias("bToatlLoanAmoutCount", "商业贷款总笔数");
			writer.addHeaderAlias("bToatlLoanAmout", "商业贷款总金额");
			writer.addHeaderAlias("oToatlLoanAmoutCount", "自有资金总笔数");
			writer.addHeaderAlias("oToatlLoanAmout", "自有资金总金额");

			writer.addHeaderAlias("aFristToatlLoanAmoutCount", "公积金首付款总笔数");
			writer.addHeaderAlias("aFristToatlLoanAmout", "公积金首付款总金额");
			writer.addHeaderAlias("aToBusinessToatlLoanAmoutCount", "公转商贷款总笔数");
			writer.addHeaderAlias("aToBusinessToatlLoanAmout", "公转商贷款总金额");

			List<Tg_JournalCount_ViewExportExcelVO> list = formart(tg_JournalCount_ViewList);
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
	 * @param tg_JournalCount_ViewList
	 * @return
	 */
	List<Tg_JournalCount_ViewExportExcelVO> formart(List<Tg_JournalCount_View> tg_JournalCount_ViewList)
	{
		List<Tg_JournalCount_ViewExportExcelVO> tg_JournalCount_ViewExportExcelList = new ArrayList<Tg_JournalCount_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_JournalCount_View tg_JournalCount_View : tg_JournalCount_ViewList)
		{
			++ordinal;
			Tg_JournalCount_ViewExportExcelVO tg_JournalCount_ViewExportExcel = new Tg_JournalCount_ViewExportExcelVO();
			tg_JournalCount_ViewExportExcel.setOrdinal(ordinal);

			tg_JournalCount_ViewExportExcel.setLoanInDate(tg_JournalCount_View.getLoanInDate());
			tg_JournalCount_ViewExportExcel.setEscrowAcountName(tg_JournalCount_View.getEscrowAcountName());
			tg_JournalCount_ViewExportExcel.setTradeCount(tg_JournalCount_View.getTradeCount());
			tg_JournalCount_ViewExportExcel.setTotalTradeAmount(tg_JournalCount_View.getTotalTradeAmount());
			tg_JournalCount_ViewExportExcel.setAToatlLoanAmoutCount(tg_JournalCount_View.getaToatlLoanAmoutCount());
			tg_JournalCount_ViewExportExcel.setAToatlLoanAmout(tg_JournalCount_View.getaToatlLoanAmout());
			tg_JournalCount_ViewExportExcel.setBToatlLoanAmoutCount(tg_JournalCount_View.getbToatlLoanAmoutCount());
			tg_JournalCount_ViewExportExcel.setBToatlLoanAmout(tg_JournalCount_View.getbToatlLoanAmout());
			tg_JournalCount_ViewExportExcel.setOToatlLoanAmoutCount(tg_JournalCount_View.getoToatlLoanAmoutCount());
			tg_JournalCount_ViewExportExcel.setOToatlLoanAmout(tg_JournalCount_View.getoToatlLoanAmout());
			tg_JournalCount_ViewExportExcel
					.setAFristToatlLoanAmoutCount(tg_JournalCount_View.getaFristToatlLoanAmoutCount());
			tg_JournalCount_ViewExportExcel.setAFristToatlLoanAmout(tg_JournalCount_View.getaFristToatlLoanAmout());
			tg_JournalCount_ViewExportExcel
					.setAToBusinessToatlLoanAmoutCount(tg_JournalCount_View.getaToBusinessToatlLoanAmoutCount());
			tg_JournalCount_ViewExportExcel
					.setAToBusinessToatlLoanAmout(tg_JournalCount_View.getaToBusinessToatlLoanAmout());

			tg_JournalCount_ViewExportExcelList.add(tg_JournalCount_ViewExportExcel);
		}
		return tg_JournalCount_ViewExportExcelList;
	}
}

package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_LargeMaturityComparisonReportsForm;
import zhishusz.housepresell.database.dao.Tg_LargeMaturityComparisonReportsDao;
import zhishusz.housepresell.database.po.Tg_BigAmountCompare_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_BigAmountCompare_ViewExportExcelVo;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.Getter;
import lombok.Setter;

/**
 * 大额到期对比表导出Excel
 * 
 * @ClassName: Tg_BigAmountCompare_ViewExportExcelService
 * @Description:TODO
 *
 */
@Service
@Transactional
public class Tg_BigAmountCompare_ViewExportExcelService
{
	private static final String excelName = "大额到期对比表";
	@Autowired
	private Tg_LargeMaturityComparisonReportsDao tg_LargeMaturityComparisonReportsDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LargeMaturityComparisonReportsForm model)
	{
		Properties properties = new MyProperties();

		// 关键字
		String keyword = model.getKeyword();

		// 存入日期
		String depositDateStart = model.getDepositDateStart().trim();
		String depositDateEnd = model.getDepositDateEnd().trim();
		// 到期日期
		String dueDateStart = model.getDueDateStart().trim();
		String dueDateEnd = model.getDueDateEnd().trim();
		// 银行名称（开户行）
		String theNameOfDepositBank = model.getTheNameOfDepositBank();

		String depositMethod = model.getDepositMethod();// 存款性质

		if (null != depositDateStart && depositDateStart.trim().isEmpty())
		{
			model.setDepositDateStart(null);
		}
		else
		{
			model.setDepositDateStart(depositDateStart);
		}

		if (null == depositMethod || depositMethod.trim().isEmpty())
		{
			model.setDepositMethod(null);
		}

		if (null != depositDateEnd && depositDateEnd.trim().isEmpty())
		{
			model.setDepositDateEnd(null);
		}
		else
		{
			model.setDepositDateEnd(depositDateEnd);
		}
		if (null != dueDateStart && dueDateStart.trim().isEmpty())
		{
			model.setDueDateStart(null);
		}
		else
		{
			model.setDueDateStart(dueDateStart);
		}
		if (null != dueDateEnd && dueDateEnd.trim().isEmpty())
		{
			model.setDueDateEnd(null);
		}
		else
		{
			model.setDueDateEnd(dueDateEnd);
		}
		if (null != theNameOfDepositBank && theNameOfDepositBank.trim().isEmpty())
		{
			model.setTheNameOfDepositBank(null);
		}

		if (keyword != null && keyword.trim().isEmpty())
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		List<Tg_BigAmountCompare_View> tg_BigAmountCompare_ViewList = tg_LargeMaturityComparisonReportsDao
				.findByPage(tg_LargeMaturityComparisonReportsDao
						.getQuery(tg_LargeMaturityComparisonReportsDao.getBasicHQLbylike(), model));
		if (null != tg_BigAmountCompare_ViewList && tg_BigAmountCompare_ViewList.size() > 0)
		{
			/*
			 * xsz by time 2018-9-6 13:43:49
			 * 合计统计：
			 * ===》需要合计的字段：
			 * 存款金额（depositAmount）、
			 * 利息（expectInterest）、
			 * 实际到期利息（realInterest）、
			 * 差异（compareDifference）
			 * ===》不需要合计的字段：
			 * 银行名称（theNameOfDepositBank）、
			 * 存入时间（depositDate）、
			 * 到期时间（dueDate）、
			 * 存款期限（depositTimeLimit）、
			 * 利率（interestRate）
			 */

			Tg_BigAmountCompare_View qb = new Tg_BigAmountCompare_View();
			qb.setTheNameOfBank("合计");
			qb.setTheNameOfDepositBank(null);
			qb.setDepositDate(null);
			qb.setDueDate(null);
			qb.setDepositTimeLimit("-");
			qb.setInterestRate(null);

			// 设置初始值
			qb.setDepositAmount(0.00);
			qb.setExpectInterest(0.00);
			qb.setRealInterest(0.00);
			qb.setCompareDifference(0.00);

			/*
			 * double类型计算
			 * 
			 * doubleAddDouble 加
			 * doubleSubtractDouble 减
			 * doubleMultiplyDouble 乘
			 * div 除
			 * getShort() 保留小数位
			 */
			MyDouble dplan = MyDouble.getInstance();

			for (Tg_BigAmountCompare_View qv : tg_BigAmountCompare_ViewList)
			{
				// 存款金额
				qb.setDepositAmount(dplan.doubleAddDouble(qb.getDepositAmount(), qv.getDepositAmount()));
				// 利息
				qb.setExpectInterest(dplan.doubleAddDouble(qb.getExpectInterest(), qv.getExpectInterest()));
				// 实际到期利息
				qb.setRealInterest(dplan.doubleAddDouble(qb.getRealInterest(), qv.getRealInterest()));
				// 差异
				qb.setCompareDifference(dplan.doubleAddDouble(qb.getCompareDifference(), qv.getCompareDifference()));
			}

			// 合计信息入列表
			tg_BigAmountCompare_ViewList.add(qb);

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_BigAmountCompare_View");// 文件在项目中的相对路径
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
			writer.addHeaderAlias("ordinal", "序号");
			writer.addHeaderAlias("theNameOfBank", "银行");
			writer.addHeaderAlias("theNameOfDepositBank", "开户行");
			writer.addHeaderAlias("depositNature", "存款性质");
			writer.addHeaderAlias("theAccountOfEscrow", "托管账户");			
			writer.addHeaderAlias("depositDate", "存入时间");
			writer.addHeaderAlias("dueDate", "到期时间");
			writer.addHeaderAlias("drawDate", "提取时间");			
			writer.addHeaderAlias("depositAmount", "存款金额");
			writer.addHeaderAlias("depositTimeLimit", "存款期限");
			writer.addHeaderAlias("interestRate", "利率");
			writer.addHeaderAlias("expectInterest", "预计利息");
			writer.addHeaderAlias("realInterest", "实际到期利息");
			writer.addHeaderAlias("compareDifference", "差异");

			List<Tg_BigAmountCompare_ViewExportExcelVo> list = formart(tg_BigAmountCompare_ViewList);

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

			properties.put("fileURL", relativeDir + strNewFileName);

		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}
		
		properties.put("tg_ProjectEscrowAmount_ViewList", tg_BigAmountCompare_ViewList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	List<Tg_BigAmountCompare_ViewExportExcelVo> formart(
			List<Tg_BigAmountCompare_View> tg_LargeMaturityComparisonReports)
	{
		List<Tg_BigAmountCompare_ViewExportExcelVo> list = new ArrayList<Tg_BigAmountCompare_ViewExportExcelVo>();
		int ordinal = 0;
		for (Tg_BigAmountCompare_View po : tg_LargeMaturityComparisonReports)
		{
			++ordinal;
			Tg_BigAmountCompare_ViewExportExcelVo vo = new Tg_BigAmountCompare_ViewExportExcelVo();
			
			vo.setOrdinal(ordinal);
			vo.setTheNameOfBank(po.getTheNameOfBank());//存款银行
			vo.setTheNameOfDepositBank(po.getTheNameOfDepositBank());//开户行
			vo.setDepositNature(po.getDepositNature());//存款性质
			vo.setTheAccountOfEscrow(vo.getTheAccountOfEscrow());//托管账户
			vo.setDepositDate(po.getDepositDate());//存入时间
			vo.setDueDate(po.getDueDate());//到期时间
			vo.setDrawDate(po.getDrawDate());//提取时间
			vo.setDepositAmount(po.getDepositAmount());//存款金额
			vo.setDepositTimeLimit(po.getDepositTimeLimit());//存款期限
			vo.setRealInterest(po.getRealInterest());//实际到期利息
			vo.setExpectInterest(po.getExpectInterest());//预计利息
			vo.setInterestRate(po.getInterestRate());//利率
			vo.setCompareDifference(po.getCompareDifference());//差异
			
			list.add(vo);
		}
		return list;
	}
}
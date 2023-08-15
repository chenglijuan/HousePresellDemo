package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_AccountabilityEnquiryForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tg_AccountabilityEnquiryDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tg_AccountabilityEnquiry;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_AccountabilityEnquiryExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service : 按权责发生制查询利息情况统计表excel导出
 */
@Service
@Transactional
public class Tg_AccountabilityEnquiryExportExcelService
{

	private static final String excelName = "按权责发生制查询利息情况统计表";

	@Autowired
	private Tg_AccountabilityEnquiryDao tg_AccountabilityEnquiryDao;

	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	public Properties execute(Tg_AccountabilityEnquiryForm model)
	{
		Properties properties = new MyProperties();

		String loadTimeStart = model.getLoadTimeStart().trim();// 起始日期
		String expirationTimeEnd = model.getExpirationTimeEnd().trim();// 结束日期

		if (null == loadTimeStart || loadTimeStart.trim().isEmpty() || null == expirationTimeEnd
				|| expirationTimeEnd.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择权责日期");
		}

		Long inBankBranchId = 0L;
		Long bankBranchId = model.getBankBranchId();
		if (bankBranchId != null && bankBranchId > 0)
		{
			Emmp_BankBranch bankBranch = emmp_BankBranchDao.findById(bankBranchId);
			if (null != bankBranch)
			{
				inBankBranchId = bankBranchId;
			}
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try
		{
			list = tg_AccountabilityEnquiryDao.callFunction2(inBankBranchId, loadTimeStart, expirationTimeEnd);
		}
		catch (SQLException e)
		{
			list = new ArrayList<Map<String, Object>>();
			e.printStackTrace();
		}

		List<Tg_AccountabilityEnquiry> tg_AccountabilityEnquiryList = new ArrayList<Tg_AccountabilityEnquiry>();
		Tg_AccountabilityEnquiry po = new Tg_AccountabilityEnquiry();

		if (null != list && list.size() > 0)
		{
			/*tg_AccountabilityEnquiryList = tg_AccountabilityEnquiryDao
					.findByPage(tg_AccountabilityEnquiryDao.getQuery(tg_AccountabilityEnquiryDao.getBasicHQL(), model));*/
			
			for (Map<String, Object> map : list)
			{
				po = new Tg_AccountabilityEnquiry();
				po.setLoadTime((String) map.get("LOADTIME"));
				po.setAmountDeposited(Double.valueOf((String) map.get("AMOUNTDEPOSITED")));
				po.setBank((String) map.get("BANK"));
				po.setBankOfDeposit((String) map.get("BANKOFDEPOSIT"));
				po.setDepositCeilings((String) map.get("DEPOSITCEILINGS"));
				po.setDepositProperty((String) map.get("DEPOSITPROPERTY"));
				po.setEscrowAccount((String) map.get("ESCROWACCOUNT"));
				po.setEscrowAcountName((String) map.get("ESCROWACOUNTNAME"));
				po.setExpirationTime((String) map.get("EXPIRATIONTIME"));
				po.setFate(Integer.valueOf((String) map.get("FATE")));
				po.setInterest(Double.valueOf((String) map.get("INTEREST")));
				po.setInterestRate((String) map.get("INTERESTRATE"));
				po.setRecordDate((String) map.get("RECORDDATE"));
				po.setTableId(Long.valueOf((String) map.get("TABLEID")));

				tg_AccountabilityEnquiryList.add(po);

			}

			Tg_AccountabilityEnquiry te = new Tg_AccountabilityEnquiry();
			te.setDepositProperty("合计");
			te.setBank(null);
			te.setRecordDate(null);
			te.setLoadTime("");
			te.setExpirationTime("");
			te.setAmountDeposited(null);
			te.setDepositCeilings(null);
			te.setInterestRate(null);
			te.setInterest(null);

			te.setInterest(0.00);

			MyDouble dplan = MyDouble.getInstance();

			for (Tg_AccountabilityEnquiry qv : tg_AccountabilityEnquiryList)
			{
				te.setInterest(dplan.doubleAddDouble(te.getInterest(), qv.getInterest()));
			}
			// 合计信息入列表
			tg_AccountabilityEnquiryList.add(te);

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_AccountabilityEnquiry");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("ordinal", "序号");			
			writer.addHeaderAlias("bank", "存款银行");
			writer.addHeaderAlias("depositProperty", "存款性质");
			writer.addHeaderAlias("recordDate", "登记时间");
			writer.addHeaderAlias("loadTime", "存入时间");
		    //writer.addHeaderAlias("theOne", "");
			writer.addHeaderAlias("expirationTime", "到期时间");
			//writer.addHeaderAlias("theTwo", "");
			writer.addHeaderAlias("amountDeposited", "存款金额");
			writer.addHeaderAlias("depositCeilings", "存款期限");
			writer.addHeaderAlias("interestRate", "利率");
			writer.addHeaderAlias("calculateStart", "测算开始日期");
			writer.addHeaderAlias("calculateEnd", "测算结束日期");
			writer.addHeaderAlias("fate", "占用天数");
			writer.addHeaderAlias("interest", "应记利息");

			List<Tg_AccountabilityEnquiryExportExcelVO> tg_AccountabilityEnquiryExportExcelVO = formart(tg_AccountabilityEnquiryList,loadTimeStart,expirationTimeEnd);
			
			// 一次性写出内容，使用默认样式
			writer.write(tg_AccountabilityEnquiryExportExcelVO);

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
			
			writer.close();

			properties.put("fileDownloadPath", relativeDir + strNewFileName);

		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put("tg_AccountabilityEnquiryList", tg_AccountabilityEnquiryList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	List<Tg_AccountabilityEnquiryExportExcelVO> formart(List<Tg_AccountabilityEnquiry> tg_AccountabilityEnquiryList,String beginDate,String endDate )
	{
		List<Tg_AccountabilityEnquiryExportExcelVO> list = new ArrayList<Tg_AccountabilityEnquiryExportExcelVO>();

		int ordinal = 0;
		for (Tg_AccountabilityEnquiry po : tg_AccountabilityEnquiryList)
		{
			++ordinal;
			Tg_AccountabilityEnquiryExportExcelVO vo = new Tg_AccountabilityEnquiryExportExcelVO();
			vo.setOrdinal(ordinal);// 序号
			vo.setBank(po.getBank());// 银行
			if (po.getDepositProperty().equals("合计"))
			{
				vo.setDepositProperty("合计");
				vo.setCalculateStart(null);
				vo.setRecordDate(null);
				vo.setCalculateEnd(null);
				vo.setInterest(po.getInterest());// 利率
			}
			else
			{
				vo.setDepositProperty(po.getDepositProperty());// 存款性质
				vo.setRecordDate(po.getRecordDate());// 登记日期
				vo.setLoadTime(po.getLoadTime());// 存入时间
				vo.setExpirationTime(po.getExpirationTime());// 到期时间
				vo.setAmountDeposited(po.getAmountDeposited());// 存款金额
				vo.setDepositCeilings(po.getDepositCeilings());// 存款期限
				vo.setInterestRate(po.getInterestRate());// 利率
				// 对比参照时间
				String loadTime = po.getLoadTime();

				String expirationTime = po.getExpirationTime();
				if (beginDate.compareTo(loadTime) > 0)
				{
					vo.setCalculateStart(beginDate);
				}
				else
				{
					vo.setCalculateStart(po.getLoadTime());
				}

				if (expirationTime.compareTo(endDate) > 0)
				{
					vo.setCalculateEnd(endDate);
				}
				else
				{
					vo.setCalculateEnd(po.getExpirationTime());
				}

				vo.setFate(po.getFate());// 天数
				vo.setInterest(po.getInterest());// 利息
			}
			list.add(vo);
		}
		return list;
	}
}

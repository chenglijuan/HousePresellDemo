package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tg_InterestForecast_ViewForm;
import zhishusz.housepresell.database.dao.Tg_InterestForecast_ViewDao;
import zhishusz.housepresell.database.po.Tg_InterestForecast_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_InterestForecast_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * service 列表查询：利息预测表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_InterestForecast_ViewExportExcelService
{

	@Autowired
	private Tg_InterestForecast_ViewDao tg_InterestForecast_ViewDao;

	private static final String excelName = "利息预测表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_InterestForecast_ViewForm model)
	{
		Properties properties = new MyProperties();
		String startDate = model.getLoanInDate();// 存单存入时间
		String stopDate = model.getEndLoanInDate();// 存单到期时间

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null == startDate || startDate.trim().length() == 0)
		{
			model.setStartDate(null);
		}
		else
		{
			model.setStartDate(startDate.trim());
		}
		
		if (null == stopDate || stopDate.trim().length() == 0)
		{
			model.setStopDate(null);
		}
		else
		{
			model.setStopDate(stopDate);
		}

		Integer totalCount = tg_InterestForecast_ViewDao.findByPage_Size(
				tg_InterestForecast_ViewDao.getQuery_Size(tg_InterestForecast_ViewDao.getBasicHQL(), model));


		List<Tg_InterestForecast_View> tg_InterestForecast_ViewList = null;
		if (totalCount > 0)
		{
			tg_InterestForecast_ViewList = tg_InterestForecast_ViewDao.findByPage(
					tg_InterestForecast_ViewDao.getQuery(tg_InterestForecast_ViewDao.getBasicHQL(), model));
			
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_InterestForecast_View");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("depositProperty", "存款性质");
			writer.addHeaderAlias("bankName", "存款银行");
			writer.addHeaderAlias("registerTime", "登记时间");
			writer.addHeaderAlias("startDate", "存入时间");
			writer.addHeaderAlias("stopDate", "到期时间");
			writer.addHeaderAlias("principalAmount", "存款金额");
			writer.addHeaderAlias("storagePeriod", "存款期限");
			writer.addHeaderAlias("annualRate", "利率");
			writer.addHeaderAlias("floatAnnualRate", "浮动区间");
			writer.addHeaderAlias("interest", "利息）");
			writer.addHeaderAlias("openAccountCertificate", "开户证实书");

			List<Tg_InterestForecast_ViewExportExcelVO> list = formart(tg_InterestForecast_ViewList);

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
			
			writer.close();

			properties.put("fileURL", relativeDir + strNewFileName);
			
			
		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	private List<Tg_InterestForecast_ViewExportExcelVO> formart(
			List<Tg_InterestForecast_View> tg_InterestForecast_ViewList) {
		
		List<Tg_InterestForecast_ViewExportExcelVO> list = new ArrayList<Tg_InterestForecast_ViewExportExcelVO>();
		
		Tg_InterestForecast_ViewExportExcelVO vo;
		int ordinal = 0;
		for (Tg_InterestForecast_View po : tg_InterestForecast_ViewList) {
			ordinal++;
			vo = new Tg_InterestForecast_ViewExportExcelVO();
			
			vo.setAnnualRate(po.getAnnualRate());
			vo.setBankName(po.getBankName());
			vo.setDepositProperty(po.getDepositProperty());
			vo.setFloatAnnualRate(po.getFloatAnnualRate());
			vo.setInterest(po.getInterest());
			vo.setOpenAccountCertificate(po.getOpenAccountCertificate());
			vo.setOrdinal(ordinal);
			vo.setPrincipalAmount(po.getPrincipalAmount());
			vo.setRegisterTime(po.getRegisterTime());
			vo.setStartDate(po.getStartDate());
			vo.setStopDate(po.getStopDate());
			vo.setStoragePeriod(po.getStoragePeriod());
			
			list.add(vo);
		}
		
		return list;
		
	}
	
}

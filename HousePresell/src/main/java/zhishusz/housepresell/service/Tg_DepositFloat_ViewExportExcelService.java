package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tg_DepositFloat_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tg_DepositFloat_ViewDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tg_DepositFloat_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_DepositFloat_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 导出：托协定存款统计表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_DepositFloat_ViewExportExcelService
{

	@Autowired
	private Tg_DepositFloat_ViewDao tg_DepositFloat_ViewDao;

	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	private static final String excelName = "托协定存款统计表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositFloat_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long bankBrenchId = model.getBankBrenchId();// 开户行Id

		String signDateStart = model.getSignDateStart();

		String signDateEnd = model.getSignDateEnd();

		String endExpirationDateStart = model.getEndExpirationDateStart();

		String endExpirationDateEnd = model.getEndExpirationDateEnd();

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		if (null != bankBrenchId && keyword.length() == 0)
		{
			Emmp_BankBranch bankBranch = emmp_BankBranchDao.findById(bankBrenchId);

			if (null == bankBranch)
			{
				return MyBackInfo.fail(properties, "未查询到有效的开户行信息");
			}

			model.setBankName(bankBranch.getTheName());
		}

		if (null == signDateStart || signDateStart.length() == 0)
		{
			model.setSignDateStart(null);
		}

		if (null == signDateEnd || signDateEnd.length() == 0)
		{
			model.setSignDateEnd(null);
		}

		if (null == endExpirationDateStart || endExpirationDateStart.length() == 0)
		{
			model.setEndExpirationDateStart(null);
		}

		if (null == endExpirationDateEnd || endExpirationDateEnd.length() == 0)
		{
			model.setEndExpirationDateEnd(null);
		}

		List<Tg_DepositFloat_View> tg_DepositFloat_ViewList = tg_DepositFloat_ViewDao
				.findByPage(tg_DepositFloat_ViewDao.getQuery(tg_DepositFloat_ViewDao.getBasicHQL(), model));
		
		if (tg_DepositFloat_ViewList == null || tg_DepositFloat_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_DepositFloat_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("bankName", "银行名称");
			writer.addHeaderAlias("depositRate", "协定存款利率");
			writer.addHeaderAlias("orgAmount", "起始金额（万元）");
			writer.addHeaderAlias("signDate", "签订日期");
			writer.addHeaderAlias("timeLimit", "期限");

			writer.addHeaderAlias("endExpirationDate", "到期日期");
			writer.addHeaderAlias("remark", "备注");

			List<Tg_DepositFloat_ViewExportExcelVO> list = formart(tg_DepositFloat_ViewList);
			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();
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
	 * @param tg_DepositFloat_ViewList
	 * @return
	 */
	List<Tg_DepositFloat_ViewExportExcelVO> formart(List<Tg_DepositFloat_View> tg_DepositFloat_ViewList){
		List<Tg_DepositFloat_ViewExportExcelVO>  tg_DepositFloat_ViewExportExcelList = new ArrayList<Tg_DepositFloat_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_DepositFloat_View tg_DepositFloat_View : tg_DepositFloat_ViewList)
		{
			++ordinal;
			Tg_DepositFloat_ViewExportExcelVO tg_DepositFloat_ViewExportExcel = new Tg_DepositFloat_ViewExportExcelVO();
			tg_DepositFloat_ViewExportExcel.setOrdinal(ordinal);
			
			tg_DepositFloat_ViewExportExcel.setBankName(tg_DepositFloat_View.getBankName());
			tg_DepositFloat_ViewExportExcel.setDepositRate(tg_DepositFloat_View.getDepositRate());
			tg_DepositFloat_ViewExportExcel.setOrgAmount(tg_DepositFloat_View.getOrgAmount());
			tg_DepositFloat_ViewExportExcel.setSignDate(tg_DepositFloat_View.getSignDate());
			tg_DepositFloat_ViewExportExcel.setTimeLimit(tg_DepositFloat_View.getTimeLimit());
			tg_DepositFloat_ViewExportExcel.setEndExpirationDate(tg_DepositFloat_View.getEndExpirationDate());
			tg_DepositFloat_ViewExportExcel.setRemark(tg_DepositFloat_View.getRemark());
			
			tg_DepositFloat_ViewExportExcelList.add(tg_DepositFloat_ViewExportExcel);
		}
		return tg_DepositFloat_ViewExportExcelList;
	}
}

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

import zhishusz.housepresell.controller.form.Tg_BankLoanInSituation_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tg_BankLoanOutSituation_ViewListDao;
import zhishusz.housepresell.database.po.Tg_BankLoanOutSituation_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_BankLoanOutSituation_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：银行放款项目出账情况表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_BankLoanOutSituation_ViewExportExcelService
{
	@Autowired
	private Tg_BankLoanOutSituation_ViewListDao tg_BankLoanOutSituation_ViewListDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	private static final String excelName = "银行放款项目出账情况表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_BankLoanInSituation_ViewForm model)
	{
		Properties properties = new MyProperties();
		Integer pageNumber = 1;
		Integer countPerPage = 9999999;

		// 获取查询条件
		String userid = String.valueOf(model.getUser().getTableId());// 用户主键
		String keyword = model.getKeyword();// 关键字
		String companyId = model.getCompanyId()==null?"":String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = model.getProjectId()==null?"":String.valueOf(model.getProjectId());// 项目ID
		
		String loanOutDate = model.getLoanOutDate().trim();
		String endLoanOutDate = model.getEndLoanOutDate().trim();
		
		String escrowAcount = model.getEscrowAcount();

		List<Tg_BankLoanOutSituation_View> tg_BankLoanOutSituation_ViewList = new ArrayList<Tg_BankLoanOutSituation_View>();
		Map<String, Object> retmap = new HashMap<String, Object>();
		// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
		try {
			retmap = tg_BankLoanOutSituation_ViewListDao.getBankLoanOutSituation_View(userid, companyId, projectId,
					keyword, loanOutDate, endLoanOutDate, pageNumber, countPerPage,escrowAcount);
			tg_BankLoanOutSituation_ViewList = (List<Tg_BankLoanOutSituation_View>) retmap.get("tg_BankLoanOutSituation_ViewList");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (tg_BankLoanOutSituation_ViewList == null || tg_BankLoanOutSituation_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			//初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_BankLoanOutSituation_ViewExporExceltService");//文件在项目中的相对路径
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
			
			writer.addHeaderAlias("companyName", "开发企业");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("escrowAcount", "托管账户");
			writer.addHeaderAlias("escrowAcountShortName", "托管账户简称");
			writer.addHeaderAlias("loanOutDate", "出账日期");
			
			writer.addHeaderAlias("loanAmountOut", "出账金额");
			
			List<Tg_BankLoanOutSituation_ViewExportExcelVO> list = formart(tg_BankLoanOutSituation_ViewList);
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
	 * @param tg_BankLoanOutSituation_ViewList
	 * @return
	 */
	List<Tg_BankLoanOutSituation_ViewExportExcelVO> formart(List<Tg_BankLoanOutSituation_View> tg_BankLoanOutSituation_ViewList){
		List<Tg_BankLoanOutSituation_ViewExportExcelVO>  tg_BankLoanOutSituation_ViewExportExcelList = new ArrayList<Tg_BankLoanOutSituation_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_BankLoanOutSituation_View tg_BankLoanOutSituation_View : tg_BankLoanOutSituation_ViewList)
		{
			++ordinal;
			Tg_BankLoanOutSituation_ViewExportExcelVO tg_BankLoanOutSituation_ViewExportExcel = new Tg_BankLoanOutSituation_ViewExportExcelVO();
			tg_BankLoanOutSituation_ViewExportExcel.setOrdinal(ordinal);
			
			tg_BankLoanOutSituation_ViewExportExcel.setProjectName(tg_BankLoanOutSituation_View.getProjectName());
			tg_BankLoanOutSituation_ViewExportExcel.setEscrowAcount(tg_BankLoanOutSituation_View.getEscrowAcount());
			tg_BankLoanOutSituation_ViewExportExcel.setEscrowAcountShortName(tg_BankLoanOutSituation_View.getEscrowAcountShortName());
			tg_BankLoanOutSituation_ViewExportExcel.setLoanOutDate(tg_BankLoanOutSituation_View.getLoanOutDate());
			tg_BankLoanOutSituation_ViewExportExcel.setLoanAmountOut(tg_BankLoanOutSituation_View.getLoanAmountOut());
			tg_BankLoanOutSituation_ViewExportExcel.setCompanyName(tg_BankLoanOutSituation_View.getCompanyName());
			
			tg_BankLoanOutSituation_ViewExportExcelList.add(tg_BankLoanOutSituation_ViewExportExcel);
		}
		return tg_BankLoanOutSituation_ViewExportExcelList;
	}
}

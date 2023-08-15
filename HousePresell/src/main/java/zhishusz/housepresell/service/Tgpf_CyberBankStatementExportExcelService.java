package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatement_ViewListDao;
import zhishusz.housepresell.database.po.Tg_BankLoanInSituation_View;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tg_BankLoanInSituation_ViewExportExcelVO;
import zhishusz.housepresell.exportexcelvo.Tgpf_CyberBankStatement_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：网银数据上传-Excel导出
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_CyberBankStatementExportExcelService
{
	@Autowired
	private Tgpf_CyberBankStatement_ViewListDao tgpf_CyberBankStatement_ViewListDao;
	
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	
	private static final String excelName = "网银数据上传情况表";
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_CyberBankStatementForm model){
		Properties properties = new MyProperties();
		Integer pageNumber = 1;
		Integer countPerPage = 9999999;
		//总笔数
		Integer transactionCount = 0;
		//总金额
		Double transactionAmount = 0.0;
		
		// 获取查询条件
		String userid = String.valueOf(model.getUser().getTableId());// 用户主键
		String keyword = model.getKeyword();// 关键字
		if(StringUtils.isBlank(keyword)){
			model.setKeyword(null);
		}else{
			model.setKeyword("%" + keyword +"%");
		}
		
		String billTimeStamp = model.getBillTimeStamp().trim();//记账日期
		
		List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList = new ArrayList<Tgpf_CyberBankStatement>();

	    tgpf_CyberBankStatementList = tgpf_CyberBankStatement_ViewListDao.findByPage(tgpf_CyberBankStatement_ViewListDao.getQuery(tgpf_CyberBankStatement_ViewListDao.getBasicHQL(), model));

		if (tgpf_CyberBankStatementList == null || tgpf_CyberBankStatementList.size() == 0) {
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		} else {
			for (Tgpf_CyberBankStatement tgpf_CyberBankStatement : tgpf_CyberBankStatementList)
			{
				Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
				form.setTheState(S_TheState.Normal);
				form.setBusiState("0");
				form.setMainTable(tgpf_CyberBankStatement);
				
				//查询交易总笔数
				transactionCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
				if(transactionCount >  0){
					String queryTransactionAmountCondition = "  nvl(sum(income),0)  ";
					transactionAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form,queryTransactionAmountCondition));
				}
				
				tgpf_CyberBankStatement.setTransactionCount(transactionCount);
				tgpf_CyberBankStatement.setTransactionAmount(transactionAmount);
			}
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil
					.createRelativePathWithDate("Tgpf_CyberBankStatementExportExcelService");// 文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20")) {
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

			writer.addHeaderAlias("billTimeStamp", "记账日期");
			writer.addHeaderAlias("theNameOfBank", "银行名称");
			writer.addHeaderAlias("theNameOfBankBranch", "开户行");
			writer.addHeaderAlias("theAccountOfBankAccountEscrowed", "托管账户");
			writer.addHeaderAlias("transactionCount", "总笔数");
			writer.addHeaderAlias("transactionAmount", "总金额");
			writer.addHeaderAlias("uploadTimeStamp", "上传日期");
			writer.addHeaderAlias("fileUploadState", "上传状态");

			List<Tgpf_CyberBankStatement_ViewExportExcelVO> list = formart(tgpf_CyberBankStatementList);

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
			properties.put("fileURL", relativeDir + strNewFileName);
			properties.put("fullFileName", saveFilePath);
		}

		properties.put("tgpf_CyberBankStatementList", tgpf_CyberBankStatementList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	/**
	 * po 格式化
	 * 
	 * @param tgpf_CyberBankStatementList
	 * @return
	 */
	List<Tgpf_CyberBankStatement_ViewExportExcelVO> formart(
			List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList) {
		List<Tgpf_CyberBankStatement_ViewExportExcelVO> tgpf_CyberBankStatement_ViewExportExcelList = new ArrayList<Tgpf_CyberBankStatement_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tgpf_CyberBankStatement tgpf_CyberBankStatement_View : tgpf_CyberBankStatementList) {
			++ordinal;
			Tgpf_CyberBankStatement_ViewExportExcelVO tgpf_CyberBankStatement_ViewExportExcel = new Tgpf_CyberBankStatement_ViewExportExcelVO();
			tgpf_CyberBankStatement_ViewExportExcel.setOrdinal(ordinal);

			tgpf_CyberBankStatement_ViewExportExcel.setBillTimeStamp(tgpf_CyberBankStatement_View.getBillTimeStamp());
			tgpf_CyberBankStatement_ViewExportExcel.setTheNameOfBank(tgpf_CyberBankStatement_View.getTheNameOfBank());
			tgpf_CyberBankStatement_ViewExportExcel
					.setTheNameOfBankBranch(tgpf_CyberBankStatement_View.getTheNameOfBankBranch());
			tgpf_CyberBankStatement_ViewExportExcel.setTheAccountOfBankAccountEscrowed(tgpf_CyberBankStatement_View.getTheAccountOfBankAccountEscrowed());
			tgpf_CyberBankStatement_ViewExportExcel.setTransactionCount(tgpf_CyberBankStatement_View.getTransactionCount());
			tgpf_CyberBankStatement_ViewExportExcel.setTransactionAmount(tgpf_CyberBankStatement_View.getTransactionAmount());
			tgpf_CyberBankStatement_ViewExportExcel.setUploadTimeStamp(tgpf_CyberBankStatement_View.getUploadTimeStamp());
			if(tgpf_CyberBankStatement_View.getFileUploadState()==1){
				tgpf_CyberBankStatement_ViewExportExcel.setFileUploadState("已上传");
			}else{
				tgpf_CyberBankStatement_ViewExportExcel.setFileUploadState("未上传");
			}
			tgpf_CyberBankStatement_ViewExportExcelList.add(tgpf_CyberBankStatement_ViewExportExcel);
		}
		return tgpf_CyberBankStatement_ViewExportExcelList;
	}
}

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

import zhishusz.housepresell.controller.form.Tg_HouseLoanAmount_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tg_HouseLoanAmount_ViewListDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_HouseLoanAmount_View;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_HouseLoanAmount_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：户入账详细
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_HouseLoanAmount_ViewExportExcelService {
	@Autowired
	private Tg_HouseLoanAmount_ViewListDao tg_HouseLoanAmount_ViewListDao;

	private static final String excelName = "户入账详细";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_HouseLoanAmount_ViewForm model) {
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();

		if (null == user) {
			return MyBackInfo.fail(properties, "用户登陆失败！");
		}

		Integer pageNumber = 1;
		Integer countPerPage = 60000;

		// 获取查询条件
		String usedId = String.valueOf(model.getUserId());// 用户主键
		String keyword = model.getKeyword();// 关键字
		String companyId = model.getCompanyId() == null ? "" : String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = model.getProjectId() == null ? "" : String.valueOf(model.getProjectId());// 项目ID
		String buildId = model.getBuildId() == null ? "" : String.valueOf(model.getBuildId());// 楼幢ID

		String startBillTimeStamp = model.getBillTimeStamp().trim();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp().trim();// 记账日期 （结束）

		List<Tg_HouseLoanAmount_View> tg_HouseLoanAmount_ViewList = new ArrayList<Tg_HouseLoanAmount_View>();

		Map<String, Object> retmap = new HashMap<String, Object>();
		try {
			retmap = tg_HouseLoanAmount_ViewListDao.getHouseLoanAmount_View(usedId, companyId, projectId, buildId,
					keyword, startBillTimeStamp, endBillTimeStamp, pageNumber, countPerPage);
			tg_HouseLoanAmount_ViewList = (List<Tg_HouseLoanAmount_View>) retmap.get("tg_HouseLoanAmount_ViewList");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String relativeDir = directoryUtil.createRelativePathWithDate("Tg_HouseLoanAmount_ViewExportExcelService");// 文件在项目中的相对路径
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
		writer.addHeaderAlias("companyName", "开发企业");
		writer.addHeaderAlias("projectName", "项目名称");
		writer.addHeaderAlias("eCodeFroMconstruction", "施工楼幢号");
		writer.addHeaderAlias("eCodeFromPublicSecurity", "公安编号");
		writer.addHeaderAlias("unitCode", "单元");
		writer.addHeaderAlias("roomId", "房间号");
		writer.addHeaderAlias("buyerName", "买受人名称");
		writer.addHeaderAlias("eCodeOfcertificate", "买受人证件号");
		writer.addHeaderAlias("theNameOfCreditor", "主借款人姓名");
		writer.addHeaderAlias("loanAmountIn", "入账金额");
		writer.addHeaderAlias("loanBank", "贷款银行");
		writer.addHeaderAlias("fundProperty", "资金性质");
		writer.addHeaderAlias("eCodeOfTripleagreement", "三方协议号");
		writer.addHeaderAlias("contractSumPrice", "合同总价");
		writer.addHeaderAlias("loanAmount", "按揭金额");
		writer.addHeaderAlias("forEcastArea", "建筑面积");
		writer.addHeaderAlias("contractStatus", "合同状态");
		writer.addHeaderAlias("paymentMethod", "付款方式");
		writer.addHeaderAlias("reconciliationTSFromOB", "入账日期");

		List<Tg_HouseLoanAmount_ViewExportExcelVO> list = formart(tg_HouseLoanAmount_ViewList);
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
		writer.autoSizeColumn(14, true);

		writer.autoSizeColumn(15, true);
		writer.autoSizeColumn(16, true);
		writer.autoSizeColumn(17, true);
		writer.autoSizeColumn(18, true);
		writer.autoSizeColumn(19, true);

		writer.autoSizeColumn(20, true);

		writer.close();

		properties.put("fileName", strNewFileName);
		properties.put("fileURL", relativeDir + strNewFileName);
		properties.put("fullFileName", saveFilePath);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	/**
	 * po 格式化
	 * 
	 * @param tg_HouseLoanAmount_ViewList
	 * @return
	 */
	List<Tg_HouseLoanAmount_ViewExportExcelVO> formart(List<Tg_HouseLoanAmount_View> tg_HouseLoanAmount_ViewList) {
		List<Tg_HouseLoanAmount_ViewExportExcelVO> tg_HouseLoanAmount_ViewExportExcelList = new ArrayList<Tg_HouseLoanAmount_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_HouseLoanAmount_View tg_HouseLoanAmount_View : tg_HouseLoanAmount_ViewList) {
			++ordinal;
			Tg_HouseLoanAmount_ViewExportExcelVO tg_HouseLoanAmount_ViewExportExcel = new Tg_HouseLoanAmount_ViewExportExcelVO();
			tg_HouseLoanAmount_ViewExportExcel.setOrdinal(ordinal);

			tg_HouseLoanAmount_ViewExportExcel.setBillTimeStamp(tg_HouseLoanAmount_View.getBillTimeStamp());
			tg_HouseLoanAmount_ViewExportExcel.setCompanyName(tg_HouseLoanAmount_View.getCompanyName());
			tg_HouseLoanAmount_ViewExportExcel.setProjectName(tg_HouseLoanAmount_View.getProjectName());

			tg_HouseLoanAmount_ViewExportExcel
					.setECodeFroMconstruction(tg_HouseLoanAmount_View.geteCodeFroMconstruction());
			tg_HouseLoanAmount_ViewExportExcel
					.setECodeFromPublicSecurity(tg_HouseLoanAmount_View.geteCodeFromPublicSecurity());
			tg_HouseLoanAmount_ViewExportExcel.setUnitCode(tg_HouseLoanAmount_View.getUnitCode());
			tg_HouseLoanAmount_ViewExportExcel.setRoomId(tg_HouseLoanAmount_View.getRoomId());
			tg_HouseLoanAmount_ViewExportExcel.setForEcastArea(tg_HouseLoanAmount_View.getForEcastArea());

			String contractStatus = "";
			switch (tg_HouseLoanAmount_View.getContractStatus()) {
			case 0:
				contractStatus = "未备案";
				break;

			case 1:
				contractStatus = "已备案";
				break;

			default:
				break;
			}
			tg_HouseLoanAmount_ViewExportExcel.setContractStatus(contractStatus);

			tg_HouseLoanAmount_ViewExportExcel.setBuyerName(tg_HouseLoanAmount_View.getBuyerName());
			tg_HouseLoanAmount_ViewExportExcel.setBuyerName(tg_HouseLoanAmount_View.getBuyerName());
			tg_HouseLoanAmount_ViewExportExcel.setECodeOfcertificate(tg_HouseLoanAmount_View.geteCodeOfcertificate());
			tg_HouseLoanAmount_ViewExportExcel.setContractSumPrice(tg_HouseLoanAmount_View.getContractSumPrice());

			String paymentMethod = "";
			switch (tg_HouseLoanAmount_View.getPaymentMethod()) {
			case 1:
				paymentMethod = "一次性付款";
				break;

			case 2:
				paymentMethod = "分期付款";
				break;

			case 3:
				paymentMethod = "贷款方式付款";
				break;

			case 4:
				paymentMethod = "其它方式";
				break;

			default:
				break;
			}
			tg_HouseLoanAmount_ViewExportExcel.setPaymentMethod(paymentMethod);

			tg_HouseLoanAmount_ViewExportExcel.setLoanAmount(tg_HouseLoanAmount_View.getLoanAmount());
			tg_HouseLoanAmount_ViewExportExcel
					.setECodeOfTripleagreement(tg_HouseLoanAmount_View.geteCodeOfTripleagreement());
			tg_HouseLoanAmount_ViewExportExcel.setLoanAmountIn(tg_HouseLoanAmount_View.getLoanAmountIn());

			String fundProperty = "";
			switch (tg_HouseLoanAmount_View.getFundProperty()) {
			case 0:
				fundProperty = "自有资金";
				break;

			case 1:
				fundProperty = "商业贷款";
				break;

			case 2:
				fundProperty = "公积金贷款";
				break;

			case 3:
				fundProperty = "公转商贷款";
				break;

			case 4:
				fundProperty = "公积金首付款";
				break;

			default:
				break;
			}
			tg_HouseLoanAmount_ViewExportExcel.setFundProperty(fundProperty);
			tg_HouseLoanAmount_ViewExportExcel
					.setReconciliationTSFromOB(tg_HouseLoanAmount_View.getReconciliationTSFromOB());
			tg_HouseLoanAmount_ViewExportExcel.setLoanBank(tg_HouseLoanAmount_View.getLoanBank());
			tg_HouseLoanAmount_ViewExportExcel.setTheNameOfCreditor(tg_HouseLoanAmount_View.getTheNameOfCreditor());

			tg_HouseLoanAmount_ViewExportExcelList.add(tg_HouseLoanAmount_ViewExportExcel);
		}
		return tg_HouseLoanAmount_ViewExportExcelList;
	}
}

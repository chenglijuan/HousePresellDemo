package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_Build_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tg_Build_ViewListDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tg_Build_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_Build_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：托管楼幢详细-Excel导出
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_Build_ViewExportExcelService
{
	@Autowired
	private Tg_Build_ViewListDao tg_Build_ViewListDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	private static final String excelName = "托管楼幢详细";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_Build_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		String keyword = model.getKeyword();// 关键字

		Long companyId = model.getCompanyId();// 开发企业ID
		Long projectId = model.getProjectId();// 项目ID
		Long buildId = model.getBuildId();// 楼幢ID

		String startBillTimeStamp = model.getBillTimeStamp();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp();// 记账日期 （结束）

		if (null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}
		if (null == companyId || companyId == 0)
		{
			model.setCompanyName(null);
		}
		else
		{
			Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(companyId);
			model.setCompanyName(emmp_CompanyInfo.getTheName());
		}
		if (null == projectId || projectId == 0)
		{
			model.setProjectName(null);
		}
		else
		{
			Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
			model.setProjectName(empj_ProjectInfo.getTheName());
		}
		if (null == buildId || buildId == 0)
		{
			model.setECodeFroMconstruction(null);
		}
		else
		{
			Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildId);
			model.setECodeFroMconstruction(empj_BuildingInfo.geteCodeFromConstruction());
		}
		if (null == startBillTimeStamp || startBillTimeStamp.length() == 0)
		{
			model.setBillTimeStamp(null);
		}
		if (null == endBillTimeStamp || endBillTimeStamp.length() == 0)
		{
			model.setEndBillTimeStamp(null);
		}

		List<Tg_Build_View> tg_Build_ViewList = tg_Build_ViewListDao.findByPage(
				tg_Build_ViewListDao.getQuery(tg_Build_ViewListDao.getBasicHQL(), model));

		if (tg_Build_ViewList == null || tg_Build_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_Build_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("billTimeStamp", "记账日期");
			writer.addHeaderAlias("companyName", "开发企业");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("eCodeFroMconstruction", "楼幢");
			writer.addHeaderAlias("unitCode", "单元信息");

			writer.addHeaderAlias("roomId", "房间号");
			writer.addHeaderAlias("forEcastArea", "户建筑面积");
			writer.addHeaderAlias("contractStatus", "合同状态");
			writer.addHeaderAlias("contractNo", "合同编号");
			writer.addHeaderAlias("eCodeOfTripleagreement", "三方协议号");

			writer.addHeaderAlias("buyerName", "承购人姓名");
			writer.addHeaderAlias("eCodeOfCertificate", "证件号");
			writer.addHeaderAlias("theNameOfCreditor", "借款人姓名");
			writer.addHeaderAlias("contractSumPrice", "合同总价");
			writer.addHeaderAlias("paymentMethod", "付款方式");

			writer.addHeaderAlias("firstPaymentAmount", "首付款金额");
			writer.addHeaderAlias("loanAmount", "合同贷款金额");
			writer.addHeaderAlias("escrowState", "托管状态");
			writer.addHeaderAlias("contractSignDate", "合同签订日期");
			writer.addHeaderAlias("loanAmountIn", "贷款入账金额");

			writer.addHeaderAlias("theAmountOfRetainedequity", "留存权益");
			writer.addHeaderAlias("statementState", "对账状态");
			
			List<Tg_Build_ViewExportExcelVO> list = formart(tg_Build_ViewList);
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
			writer.autoSizeColumn(21, true);
			writer.autoSizeColumn(22, true);
			
			writer.close();

			properties.put("fileURL", relativeDir + strNewFileName);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * po 格式化
	 * 
	 * @param tg_Build_ViewList
	 * @return
	 */
	List<Tg_Build_ViewExportExcelVO> formart(List<Tg_Build_View> tg_Build_ViewList)
	{
		List<Tg_Build_ViewExportExcelVO> tg_Build_ViewExportExcelList = new ArrayList<Tg_Build_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_Build_View tg_Build_View : tg_Build_ViewList)
		{
			++ordinal;
			Tg_Build_ViewExportExcelVO tg_Build_ViewExportExcel = new Tg_Build_ViewExportExcelVO();
			tg_Build_ViewExportExcel.setOrdinal(ordinal);

			tg_Build_ViewExportExcel.setBillTimeStamp(tg_Build_View.getBillTimeStamp());
			tg_Build_ViewExportExcel.setCompanyName(tg_Build_View.getCompanyName());
			tg_Build_ViewExportExcel.setProjectName(tg_Build_View.getProjectName());
			tg_Build_ViewExportExcel.setECodeFroMconstruction(tg_Build_View.geteCodeFroMconstruction());
			tg_Build_ViewExportExcel.setUnitCode(tg_Build_View.getUnitCode());
			tg_Build_ViewExportExcel.setRoomId(tg_Build_View.getRoomId());
			tg_Build_ViewExportExcel.setForEcastArea(tg_Build_View.getForEcastArea());
			tg_Build_ViewExportExcel.setContractNo(tg_Build_View.getContractNo());
			tg_Build_ViewExportExcel.setECodeOfTripleagreement(tg_Build_View.geteCodeOfTripleagreement());
			tg_Build_ViewExportExcel.setBuyerName(tg_Build_View.getBuyerName());
			tg_Build_ViewExportExcel.setECodeOfCertificate(tg_Build_View.geteCodeOfCertificate());
			tg_Build_ViewExportExcel.setTheNameOfCreditor(tg_Build_View.getTheNameOfCreditor());
			tg_Build_ViewExportExcel.setContractSumPrice(tg_Build_View.getContractSumPrice());
			tg_Build_ViewExportExcel.setFirstPaymentAmount(tg_Build_View.getFirstPaymentAmount());
			tg_Build_ViewExportExcel.setLoanAmount(tg_Build_View.getLoanAmount());
			tg_Build_ViewExportExcel.setContractSignDate(tg_Build_View.getContractSignDate());
			tg_Build_ViewExportExcel.setLoanAmountIn(tg_Build_View.getLoanAmountIn());
			tg_Build_ViewExportExcel.setTheAmountOfRetainedequity(tg_Build_View.getTheAmountOfRetainedequity());

			String contractStatus = "";

			if (null == tg_Build_View.getContractStatus())
			{
				contractStatus = "未备案";
			}
			else
			{
				switch (tg_Build_View.getContractStatus())
				{
				case 0:
					contractStatus = "未备案";
					break;

				case 1:
					contractStatus = "已备案";
					break;

				default:
					break;
				}
			}

			tg_Build_ViewExportExcel.setContractStatus(contractStatus);

			String escrowState = "";

			if (null != tg_Build_View.getEscrowState())
			{
				switch (tg_Build_View.getEscrowState())
				{
				case 0:
					escrowState = "未托管";
					break;

				case 1:
					escrowState = "已托管";
					break;

				case 2:
					escrowState = "申请托管终止";
					break;

				case 3:
					escrowState = "托管终止";
					break;

				default:
					break;
				}
			}
			tg_Build_ViewExportExcel.setEscrowState(escrowState);

			String statementState = "";

			if (null != tg_Build_View.getStatementState())
			{
				switch (tg_Build_View.getStatementState())
				{
				case 0:
					statementState = "未对账";
					break;

				case 1:
					statementState = "已对账";
					break;

				case 2:
					statementState = "已撤销";
					break;

				default:
					break;
				}
			}
			tg_Build_ViewExportExcel.setStatementState(statementState);

			String paymentMethod = "";
			if (null != tg_Build_View.getPaymentMethod())
			{
				switch (tg_Build_View.getPaymentMethod())
				{
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
					paymentMethod = "其他";
					break;
					
				default:
					break;
				}
			}

			tg_Build_ViewExportExcel.setPaymentMethod(paymentMethod);

			tg_Build_ViewExportExcelList.add(tg_Build_ViewExportExcel);
		}
		return tg_Build_ViewExportExcelList;
	}
}

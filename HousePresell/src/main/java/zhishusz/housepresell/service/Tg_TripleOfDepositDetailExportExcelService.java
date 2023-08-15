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
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tg_TripleOfDepositDetail_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：三方协议入账查询-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_TripleOfDepositDetailExportExcelService
{
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	private static final String excelName = "三方协议入账查询";

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_DepositDetailForm model)
	{
		Properties properties = new MyProperties();

		String keyword = model.getKeyword();
		String startBillTimeStamp = model.getBillTimeStamp();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp();// 记账日期 （结束）

		Long branchId = model.getBankBranchId();// 开户行Id

		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}
		if (null == startBillTimeStamp || startBillTimeStamp.length() == 0)
		{
			model.setBillTimeStamp(null);
		}
		if (null == endBillTimeStamp || endBillTimeStamp.length() == 0)
		{
			model.setEndBillTimeStamp(null);
		}
		if (null == branchId || branchId < 0)
		{
			model.setBankBranchId(null);
		}

		Sm_User user = model.getUser();

		if (null == user)
		{
			return MyBackInfo.fail(properties, "用户登陆失败！");
		}

		// 查询用户所属机构类型
		String companyType = user.getCompany().getTheType();// 企业类型

		if (null != companyType && companyType.trim().equals(S_CompanyType.Zhengtai))
		{
			model.setCompanyNames(null);
		}
		else
		{
			Long[] developCompanyInfoIdArr = model.getDevelopCompanyInfoIdArr();

			if (null == developCompanyInfoIdArr || developCompanyInfoIdArr.length == 0)
			{
				List<Tgpf_DepositDetail> tgpf_DepositDetailList = new ArrayList<Tgpf_DepositDetail>();

				properties.put("tgpf_DepositDetailList", tgpf_DepositDetailList);

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				return properties;
			}
			else
			{
				String[] companyNames = new String[developCompanyInfoIdArr.length];

				for (int i = 0; i < developCompanyInfoIdArr.length; i++)
				{
					Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(developCompanyInfoIdArr[i]);
					if (null != companyInfo)
					{
						companyNames[i] = companyInfo.getTheName();
					}
				}

				model.setCompanyNames(companyNames);
			}
		}

		model.setTheState(S_TheState.Normal);

		List<Tgpf_DepositDetail> tgpf_DepositDetailList = tgpf_DepositDetailDao
				.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL2(), model));

		if (tgpf_DepositDetailList == null || tgpf_DepositDetailList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的数据！");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_TripleOfDepositDetailExportExcelService");// 文件在项目中的相对路径
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
			writer.addHeaderAlias("depositDatetime", "记账日期");
			writer.addHeaderAlias("sellerName", "开发企业");
			writer.addHeaderAlias("buyerName", "买受人");
			writer.addHeaderAlias("theNameOfCreditor", "主借款人");
			writer.addHeaderAlias("idNumber", "证件号码");
			writer.addHeaderAlias("eCodeOfContractRecord", "合同备案号");
			writer.addHeaderAlias("theNameOfProject", "开发项目");
			writer.addHeaderAlias("eCodeFromConstruction", "施工编号");
			writer.addHeaderAlias("eCodeOfUnit", "单元");
			writer.addHeaderAlias("unitRoom", "室号");
			writer.addHeaderAlias("loanAmount", "入账金额（元）");
			writer.addHeaderAlias("bankAccountForLoan", "银行账号（贷）");
			writer.addHeaderAlias("fundProperty", "资金性质");
			writer.addHeaderAlias("bankBranchName", "开户行");
			writer.addHeaderAlias("idType", "证件类型");
			writer.addHeaderAlias("eCodeOfTripleAgreement", "托管协议号");
			writer.addHeaderAlias("contractAmount", "合同金额");
			writer.addHeaderAlias("theAccountOfBAE", "托管账户");
			writer.addHeaderAlias("remarkFromDepositBill", "缴款记账备注");

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

			List<Tg_TripleOfDepositDetail_ViewExportExcelVO> list = formart(tgpf_DepositDetailList);
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
	 * 
	 * @param tgpf_DepositDetailList
	 * @return
	 */
	List<Tg_TripleOfDepositDetail_ViewExportExcelVO> formart(List<Tgpf_DepositDetail> tgpf_DepositDetailList)
	{
		List<Tg_TripleOfDepositDetail_ViewExportExcelVO> tg_TripleOfDepositDetail_ViewExportExcelList = new ArrayList<Tg_TripleOfDepositDetail_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tgpf_DepositDetail depositDetail : tgpf_DepositDetailList)
		{
			++ordinal;
			Tg_TripleOfDepositDetail_ViewExportExcelVO tg_TripleOfDepositDetail_ViewExportExcel = new Tg_TripleOfDepositDetail_ViewExportExcelVO();
			tg_TripleOfDepositDetail_ViewExportExcel.setOrdinal(ordinal);

			tg_TripleOfDepositDetail_ViewExportExcel.setDepositDatetime(depositDetail.getDepositDatetime());// 缴款记账日期

			if (null != depositDetail.getTripleAgreement())
			{
				Tgxy_TripleAgreement tripleAgreement = depositDetail.getTripleAgreement();
				tg_TripleOfDepositDetail_ViewExportExcel
						.setECodeOfTripleAgreement(tripleAgreement.geteCodeOfTripleAgreement());// 托管协议号
				tg_TripleOfDepositDetail_ViewExportExcel
						.setECodeOfContractRecord(tripleAgreement.geteCodeOfContractRecord());// 合同备案号
				tg_TripleOfDepositDetail_ViewExportExcel.setSellerName(tripleAgreement.getSellerName());// 开发企业
				tg_TripleOfDepositDetail_ViewExportExcel.setTheNameOfProject(tripleAgreement.getTheNameOfProject());// 开发项目

				Empj_BuildingInfo buildingInfo = tripleAgreement.getBuildingInfo();

				String isAdvanceSale = buildingInfo.getIsAdvanceSale();

				if (null != isAdvanceSale && "1".equals(isAdvanceSale.trim()))
				{
					tg_TripleOfDepositDetail_ViewExportExcel
							.setECodeFromConstruction(tripleAgreement.geteCodeFromConstruction() + "（变更预售）");
				}
				else
				{
					tg_TripleOfDepositDetail_ViewExportExcel
							.setECodeFromConstruction(tripleAgreement.geteCodeFromConstruction());// 楼幢编号
				}

				Empj_UnitInfo unitInfo = tripleAgreement.getUnitInfo();
				if (null != unitInfo)
				{
					tg_TripleOfDepositDetail_ViewExportExcel.setECodeOfUnit(unitInfo.getTheName());// 单元
				}
				else
				{
					tg_TripleOfDepositDetail_ViewExportExcel.setECodeOfUnit(tripleAgreement.geteCodeOfUnit());// 单元
				}
				tg_TripleOfDepositDetail_ViewExportExcel.setUnitRoom(tripleAgreement.getUnitRoom());// 室号
				tg_TripleOfDepositDetail_ViewExportExcel
						.setContractAmount(MyDouble.fmtMicrometer(tripleAgreement.getContractAmount()));// 合同金额
				// tg_TripleOfDepositDetail_ViewExportExcel.setLoanAmount(MyDouble.fmtMicrometer(tripleAgreement.getLoanAmount()));//
				// 贷款金额
				tg_TripleOfDepositDetail_ViewExportExcel
						.setLoanAmount(MyDouble.fmtMicrometer(depositDetail.getLoanAmountFromBank()));// 贷款金额
				tg_TripleOfDepositDetail_ViewExportExcel.setBuyerName(tripleAgreement.getBuyerName());// 购房人
			}

			tg_TripleOfDepositDetail_ViewExportExcel.setTheNameOfCreditor(depositDetail.getTheNameOfCreditor());// 借款人

			if (null == depositDetail.getIdType() || depositDetail.getIdType().trim().isEmpty())
			{
				tg_TripleOfDepositDetail_ViewExportExcel.setIdType("身份证");
			}
			else
			{
				switch (depositDetail.getIdType())
				{
				case "1":

					tg_TripleOfDepositDetail_ViewExportExcel.setIdType("身份证");

					break;

				case "2":

					tg_TripleOfDepositDetail_ViewExportExcel.setIdType("护照");

					break;

				case "3":

					tg_TripleOfDepositDetail_ViewExportExcel.setIdType("军官证");

					break;

				case "4":

					tg_TripleOfDepositDetail_ViewExportExcel.setIdType("港澳台居民通行证");

					break;

				case "5":

					tg_TripleOfDepositDetail_ViewExportExcel.setIdType("户口簿");

					break;

				case "6":

					tg_TripleOfDepositDetail_ViewExportExcel.setIdType("其他证件");

					break;
				}
			}

			tg_TripleOfDepositDetail_ViewExportExcel.setIdNumber(depositDetail.getIdNumber());// 证件号码
			tg_TripleOfDepositDetail_ViewExportExcel.setBankAccountForLoan(depositDetail.getBankAccountForLoan());// 银行账号（贷）

			if (null != depositDetail.getFundProperty())
			{
				switch (depositDetail.getFundProperty())
				{
				case 0:

					tg_TripleOfDepositDetail_ViewExportExcel.setFundProperty("自有资金");

					break;

				case 1:

					tg_TripleOfDepositDetail_ViewExportExcel.setFundProperty("商业贷款");

					break;

				case 2:

					tg_TripleOfDepositDetail_ViewExportExcel.setFundProperty("公积金贷款");

					break;

				case 3:

					tg_TripleOfDepositDetail_ViewExportExcel.setFundProperty("公转商贷款");

					break;

				case 4:

					tg_TripleOfDepositDetail_ViewExportExcel.setFundProperty("公积金首付款");

					break;
				}
			}

			if (null != depositDetail.getBankBranch())
			{
				tg_TripleOfDepositDetail_ViewExportExcel.setBankBranchName(depositDetail.getBankBranch().getTheName());// 开户行
			}

			tg_TripleOfDepositDetail_ViewExportExcel
					.setTheAccountOfBAE(depositDetail.getTheAccountOfBankAccountEscrowed());// 托管账号
			tg_TripleOfDepositDetail_ViewExportExcel.setRemarkFromDepositBill(depositDetail.getRemarkFromDepositBill());// 缴款记账备注

			tg_TripleOfDepositDetail_ViewExportExcelList.add(tg_TripleOfDepositDetail_ViewExportExcel);
		}
		return tg_TripleOfDepositDetail_ViewExportExcelList;
	}
}

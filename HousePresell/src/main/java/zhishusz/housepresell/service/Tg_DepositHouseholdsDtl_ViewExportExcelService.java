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
import zhishusz.housepresell.controller.form.Tg_DepositHouseholdsDtl_ViewForm;
import zhishusz.housepresell.database.dao.Tg_DepositHouseholdsDtl_ViewDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.Tg_DepositHouseholdsDtl_View;
import zhishusz.housepresell.exportexcelvo.Tg_DepositHouseholdsDtl_ViewExportExcelVO;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 导出：托管项目户信息表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_DepositHouseholdsDtl_ViewExportExcelService
{

	@Autowired
	private Tg_DepositHouseholdsDtl_ViewDao tg_DepositHouseholdsDtl_ViewDao;

	private static final String excelName = "托管项目户信息表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositHouseholdsDtl_ViewForm model)
	{
		Properties properties = new MyProperties();

		String keyword = model.getKeyword();

		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		if (null != model.getPayWay())
		{
			model.setPayMethod(model.getPayWay());
		}

		List<Tg_DepositHouseholdsDtl_View> tg_DepositHouseholdsDtl_ViewList = tg_DepositHouseholdsDtl_ViewDao
				.findByPage(
						tg_DepositHouseholdsDtl_ViewDao.getQuery(tg_DepositHouseholdsDtl_ViewDao.getBasicHQL(), model));

		if (null == tg_DepositHouseholdsDtl_ViewList || tg_DepositHouseholdsDtl_ViewList.size() == 0)
		{
			return MyBackInfo.fail(properties, "为查询到有效的数据");
		}
		else
		{
			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil
					.createRelativePathWithDate("Tg_DepositHouseholdsDtl_ViewExportExcelService");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("eCodeOfContractRecord", "合同编号");
			writer.addHeaderAlias("position", "坐落");
			writer.addHeaderAlias("buyerName", "买受人名称");
			writer.addHeaderAlias("eCodeOfcertificate", "买受人证件号");
			writer.addHeaderAlias("contactPhone", "买受人电话");

			writer.addHeaderAlias("payWay", "付款方式");
			writer.addHeaderAlias("contractCteateState", "买卖合同签订与备案状态");
			writer.addHeaderAlias("contractCreateTime", "买卖合同签订时间");
			writer.addHeaderAlias("contractRecordTime", "买卖合同备案时间");
			writer.addHeaderAlias("tripleAgreementRecordState", "三方协议备案状态");

			writer.addHeaderAlias("tripleAgreementRecordTime", "三方协议备案时间");
			writer.addHeaderAlias("totalAmountOfHouse", "入账金额");
			writer.addHeaderAlias("theAmountOfRetainedEquity", "留存权益");

			List<Tg_DepositHouseholdsDtl_ViewExportExcelVO> list = formart(tg_DepositHouseholdsDtl_ViewList);
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
	 * @param tg_DepositHouseholdsDtl_ViewList
	 * @return
	 */
	List<Tg_DepositHouseholdsDtl_ViewExportExcelVO> formart(
			List<Tg_DepositHouseholdsDtl_View> tg_DepositHouseholdsDtl_ViewList)
	{
		List<Tg_DepositHouseholdsDtl_ViewExportExcelVO> tg_Tg_DepositHouseholdsDtl_ViewExportExcelList = new ArrayList<Tg_DepositHouseholdsDtl_ViewExportExcelVO>();
		int ordinal = 0;
		for (Tg_DepositHouseholdsDtl_View tg_Tg_DepositHouseholdsDtl_View : tg_DepositHouseholdsDtl_ViewList)
		{
			++ordinal;
			Tg_DepositHouseholdsDtl_ViewExportExcelVO tg_Tg_DepositHouseholdsDtl_ViewExportExcel = new Tg_DepositHouseholdsDtl_ViewExportExcelVO();
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel.setOrdinal(ordinal);

			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setECodeOfContractRecord(tg_Tg_DepositHouseholdsDtl_View.geteCodeOfContractRecord());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel.setPosition(tg_Tg_DepositHouseholdsDtl_View.getPosition());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel.setBuyerName(tg_Tg_DepositHouseholdsDtl_View.getBuyerName());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setECodeOfcertificate(tg_Tg_DepositHouseholdsDtl_View.geteCodeOfcertificate());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setContactPhone(tg_Tg_DepositHouseholdsDtl_View.getContactPhone());

			String payMethod = "";

			if (null != tg_Tg_DepositHouseholdsDtl_View.getPayWay()
					&& !tg_Tg_DepositHouseholdsDtl_View.getPayWay().trim().isEmpty())
			{
				switch (tg_Tg_DepositHouseholdsDtl_View.getPayWay().trim())
				{
				case "1":

					payMethod = "一次性付款";

					break;

				case "2":

					payMethod = "分期付款";

					break;

				case "3":

					payMethod = "贷款方式付款";

					break;

				case "4":

					payMethod = "其他方式";

					break;

				default:
					break;
				}
			}

			tg_Tg_DepositHouseholdsDtl_ViewExportExcel.setPayWay(payMethod);

			String sContractSt = "";

			if (null != tg_Tg_DepositHouseholdsDtl_View.getContractCteateState())
			{
				switch (tg_Tg_DepositHouseholdsDtl_View.getContractCteateState())
				{
				case 0:

					sContractSt = "未签订";

					break;

				case 1:

					sContractSt = "已签订";

					break;

				default:
					break;
				}
			}

			tg_Tg_DepositHouseholdsDtl_ViewExportExcel.setContractCteateState(sContractSt);

			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setContractCreateTime(tg_Tg_DepositHouseholdsDtl_View.getContractCreateTime());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setContractRecordTime(tg_Tg_DepositHouseholdsDtl_View.getContractRecordTime());

			String tripleAgreementSta = "";

			if (null != tg_Tg_DepositHouseholdsDtl_View.getTripleAgreementRecordState())
			{
				switch (tg_Tg_DepositHouseholdsDtl_View.getTripleAgreementRecordState())
				{
				case 0:

					tripleAgreementSta = "未备案";

					break;

				case 1:

					tripleAgreementSta = "已备案";

					break;

				default:
					break;
				}
			}

			tg_Tg_DepositHouseholdsDtl_ViewExportExcel.setTripleAgreementRecordState(tripleAgreementSta);

			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setTripleAgreementRecordTime(tg_Tg_DepositHouseholdsDtl_View.getTripleAgreementRecordTime());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setTotalAmountOfHouse(tg_Tg_DepositHouseholdsDtl_View.getTotalAmountOfHouse());
			tg_Tg_DepositHouseholdsDtl_ViewExportExcel
					.setTheAmountOfRetainedEquity(tg_Tg_DepositHouseholdsDtl_View.getTheAmountOfRetainedEquity());

			tg_Tg_DepositHouseholdsDtl_ViewExportExcelList.add(tg_Tg_DepositHouseholdsDtl_ViewExportExcel);
		}
		return tg_Tg_DepositHouseholdsDtl_ViewExportExcelList;
	}
}

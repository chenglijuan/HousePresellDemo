package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tg_RetainedRightsForm;
import zhishusz.housepresell.database.dao.Tgpf_RetainedRightsDao;
import zhishusz.housepresell.database.po.Tg_RetainedRightsView2;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tg_RetainedRightsExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service : 留存权益拨付明细查询excel导出
 */
@Service
@Transactional
public class Tg_RetainedRightsExportExcelService
{

	private static final String excelName = "留存权益查询";

	@Autowired
	private Tgpf_RetainedRightsDao tgpf_RetainedRightsDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RetainedRightsForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = 0;
		Integer countPerPage = 0;
		/*// 关键字
		String keyword = model.getKeyword();

		// 项目名称
		Long projectId = model.getProjectId();

		// 拨付日期
		String fromDate = model.getFromDate();


		if (null != fromDate && fromDate.trim().isEmpty())
		{
			model.setFromDate(null);
		}
		else
		{
			model.setFromDate(fromDate);
		}
		if (keyword != null)
		{
			model.setKeyword("%" + keyword + "%");
		}*/
		Integer totalCount = 0;
		Integer totalPage = 0;

		List<Tg_RetainedRightsView2> tgpf_RetainedRightsList = new ArrayList<>();
		
		Map<String, Object> retmap = new HashMap<String, Object>();
		try {
//			retmap = tgpf_RetainedRightsDao.getRetainedRightsView(userid, projectId,
//					keyword, enterTimeStampStart, enterTimeStampEnd, pageNumber, countPerPage);
			
			retmap = tgpf_RetainedRightsDao.getRetainedRightsView2(model.getUserId(), model.getKeyword(), model.getCompanyId(), model.getProjectId(), model.getBuildingId(), model.getBilltTimeStampStart(), model.getBilltTimeStampStart(), pageNumber, countPerPage);
			
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
			tgpf_RetainedRightsList = (List<Tg_RetainedRightsView2>) retmap.get("tg_RetainedRightsList");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (null != tgpf_RetainedRightsList&&tgpf_RetainedRightsList.size()>0)
		{
//			tgpf_RetainedRightsList = tgpf_RetainedRightsDao
//					.findByPage(tgpf_RetainedRightsDao.getQuery(tgpf_RetainedRightsDao.getBasicHQL(), model));

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tg_RetainedRightsView");// 文件在项目中的相对路径
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

			writer.addHeaderAlias("ordinal", "");

			writer.addHeaderAlias("billtTimeStamp", "留存权益计算日期");
			writer.addHeaderAlias("arrivalTimeStamp", "到账日期");
			writer.addHeaderAlias("sellerName", "企业名称");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("ecodeFromConstruction", "楼幢施工编号");
			writer.addHeaderAlias("ecodeOfBuildingUnit", "单元信息");
			writer.addHeaderAlias("ecodeFromRoom", "房间号");
			writer.addHeaderAlias("buyer", "买受人名称");
			writer.addHeaderAlias("theNameOfCreditor", "借款人名称");
			writer.addHeaderAlias("idNumberOfCreditor", "借款人身份证");
			writer.addHeaderAlias("ecodeOfContractRecord", "合同备案号");
			writer.addHeaderAlias("ecodeoftripleagreement", "三方协议号");
			writer.addHeaderAlias("actualDepositAmount", "实际入账金额");
			writer.addHeaderAlias("depositAmountFromloan", "按揭入账金额");
			writer.addHeaderAlias("theAmount", "留存权益总金额");
			writer.addHeaderAlias("amountOfInterestNotdue", "未到期权益金额");
			writer.addHeaderAlias("amountOfInterestdue", "到期权益金额");
			writer.addHeaderAlias("remaincoefficient", "触发类型");

			List<Tg_RetainedRightsExportExcelVO> list = formart(tgpf_RetainedRightsList);

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

			writer.close();

			properties.put("fileDownloadPath", relativeDir + strNewFileName);

		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put("tgpf_RetainedRightsList", tgpf_RetainedRightsList);
		properties.put(S_NormalFlag.keyword, model.getKeyword());
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	List<Tg_RetainedRightsExportExcelVO> formart(List<Tg_RetainedRightsView2> tgpf_RetainedRightsList)
	{
		List<Tg_RetainedRightsExportExcelVO> list = new ArrayList<Tg_RetainedRightsExportExcelVO>();

		int ordinal = 0;
		for (Tg_RetainedRightsView2 po : tgpf_RetainedRightsList)
		{
			++ordinal;
			Tg_RetainedRightsExportExcelVO vo = new Tg_RetainedRightsExportExcelVO();
			vo.setOrdinal(ordinal);
			vo.setBilltTimeStamp(po.getBilltTimeStamp());
			vo.setArrivalTimeStamp(po.getArrivalTimeStamp());
			vo.setSellerName(po.getSellerName());
			vo.setProjectName(po.getProjectName());
			vo.setEcodeFromConstruction(po.getEcodeFromConstruction());
			vo.setEcodeOfBuildingUnit(po.getEcodeOfBuildingUnit());
			vo.setEcodeFromRoom(po.getEcodeFromRoom());
			vo.setBuyer(po.getBuyer());
			vo.setTheNameOfCreditor(po.getTheNameOfCreditor());
			vo.setIdNumberOfCreditor(po.getIdNumberOfCreditor());
			vo.setEcodeOfContractRecord(po.getEcodeOfContractRecord());
			vo.setEcodeoftripleagreement(po.getEcodeoftripleagreement());
			vo.setActualDepositAmount(po.getActualDepositAmount());
			vo.setDepositAmountFromloan(po.getDepositAmountFromloan());
			vo.setTheAmount(po.getTheAmount());
			vo.setAmountOfInterestNotdue(po.getAmountOfInterestNotdue());
			vo.setAmountOfInterestdue(po.getAmountOfInterestdue());
			vo.setRemaincoefficient(po.getRemaincoefficient());

			list.add(vo);

		}
		return list;
	}

}

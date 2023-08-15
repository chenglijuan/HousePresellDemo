package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Qs_RecordAmount_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.extra.Qs_RecordAmount_ViewDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.extra.Qs_RecordAmount_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Qs_BuildingAccount_ViewEportExcelVO;
import zhishusz.housepresell.exportexcelvo.Qs_RecordAmount_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service添加操作：入账金额核对表-导出Excel
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Qs_RecordAmount_ViewExportExcelService
{
	private static final String excelName = "入账金额核对表";
	@Autowired
	private Qs_RecordAmount_ViewDao qs_RecordAmount_ViewDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao; // 开户行
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;// 托管账号

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_RecordAmount_ViewForm model)
	{
		Properties properties = new MyProperties();
		
		// 关键字
		String keyword = model.getKeyword();

		// 入账日期
		String recordDateStart = model.getRecordDateStart().trim();
		String recordDateEnd = model.getRecordDateEnd().trim();

		// 银行名称（开户行）
		String theNameOfBank = model.getTheNameOfBank();

		// 托管账户名称
		String theNameOfEscrow = model.getTheNameOfEscrow();

		if (null == recordDateStart || recordDateStart.trim().isEmpty())
		{
			model.setRecordDateStart(null);
		}
		else
		{
			model.setRecordDateStart(recordDateStart);
		}
		if (null == recordDateEnd || recordDateEnd.trim().isEmpty())
		{
			model.setRecordDateEnd(null);
		}
		else
		{
			model.setRecordDateEnd(recordDateEnd);
		}
		if (null == theNameOfBank || theNameOfBank.trim().isEmpty())
		{
			model.setTheNameOfBank(null);
		}
		else
		{
			Emmp_BankBranch branch = emmp_BankBranchDao.findById(Long.parseLong(theNameOfBank));

			model.setTheNameOfBank(branch.getTheName());
		}
		if (null != theNameOfEscrow && theNameOfEscrow.trim().isEmpty())
		{
			model.setTheNameOfEscrow(null);
		}
		else
		{
			Tgxy_BankAccountEscrowed escrowed = tgxy_BankAccountEscrowedDao.findById(Long.parseLong(theNameOfEscrow));

			model.setTheNameOfEscrow(escrowed.getTheName());
		}

		if (keyword != null && keyword.trim().length() >= 1)
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		Integer totalCount = qs_RecordAmount_ViewDao
				.findByPage_Size(qs_RecordAmount_ViewDao.getQuery_Size(qs_RecordAmount_ViewDao.getBasicHQL(), model));


		List<Qs_RecordAmount_View> qs_RecordAmount_ViewList;
		if (totalCount > 0)
		{
			qs_RecordAmount_ViewList = qs_RecordAmount_ViewDao.findByPage(
					qs_RecordAmount_ViewDao.getQuery(qs_RecordAmount_ViewDao.getBasicHQL(), model));

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Qs_BuildingAccount_View");// 文件在项目中的相对路径
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
			writer.addHeaderAlias("recordDate", "入账日期");
			writer.addHeaderAlias("theNameOfBank", "开户行名称");
			writer.addHeaderAlias("theAccountOfEscrow", "托管账户");
			writer.addHeaderAlias("silverAmount", "网银入账金额");
			writer.addHeaderAlias("silverNumber", "网银入账笔数");
			writer.addHeaderAlias("escrowAmount", "托管系统入账金额");
			writer.addHeaderAlias("escrowNumber", "托管系统入账笔数");
			writer.addHeaderAlias("compareDifference", "比对差额");
			writer.addHeaderAlias("differenceNote", "差异备注");
			
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
		
			List<Qs_RecordAmount_ViewExportExcelVO> list = formart(qs_RecordAmount_ViewList);

			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();
			writer.close();

			properties.put("fileURL", relativeDir + strNewFileName);

		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put("qs_RecordAmount_ViewList", qs_RecordAmount_ViewList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	List<Qs_RecordAmount_ViewExportExcelVO> formart(List<Qs_RecordAmount_View> qs_RecordAmount_ViewList)
	{

		List<Qs_RecordAmount_ViewExportExcelVO> list = new ArrayList<Qs_RecordAmount_ViewExportExcelVO>();

		int ordinal = 0;
		for (Qs_RecordAmount_View po : qs_RecordAmount_ViewList)
		{
			++ordinal;
			Qs_RecordAmount_ViewExportExcelVO vo = new Qs_RecordAmount_ViewExportExcelVO();

			vo.setCompareDifference(po.getCompareDifference());
			vo.setDifferenceNote(po.getDifferenceNote());
			vo.setEscrowAmount(po.getEscrowAmount());
			vo.setEscrowNumber(po.getEscrowNumber());
			vo.setTheAccountOfEscrow(po.getTheAccountOfEscrow());
			vo.setOrdinal(ordinal);
			vo.setSilverAmount(po.getSilverAmount());
			vo.setSilverNumber(po.getSilverNumber());
			vo.setTheNameOfBank(po.getTheNameOfBank());
			vo.setRecordDate(po.getRecordDate());

			list.add(vo);

		}

		return list;

	}

}

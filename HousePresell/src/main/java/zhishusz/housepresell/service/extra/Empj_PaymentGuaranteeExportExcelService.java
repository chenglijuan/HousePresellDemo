package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Empj_PaymentGuaranteeExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_PjDevProgressForcastTemplate;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表导出:支付保证申请撤销
 * by li 2018-10-08
 */
@Service
@Transactional
public class Empj_PaymentGuaranteeExportExcelService
{

	private static final String excelName = "支付保证撤销";

	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao; // 开发企业

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao; // 项目名称

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		// 关键字
		String keyword = model.getKeyword();

		// 开发企业
		String companyName= model.getCompanyName();
		// 项目名称
		String projectName = model.getProjectName();
		//支付保证出具单位
		String guaranteeCompany = model.getGuaranteeCompany();
		// 申请日期
		String applyDate = model.getApplyDate();
		
		if (null != companyName && companyName.trim().isEmpty())
		{
			model.setCompanyName(null);
		}
		else
		{
			model.setCompanyName(companyName);
		}
		if (null != projectName && projectName.trim().isEmpty())
		{
			model.setProjectName(null);
		}
		else
		{
			model.setProjectName(projectName);
		}
		if (null != applyDate && applyDate.trim().isEmpty())
		{
			model.setApplyDate(null);
		}
		else
		{
			model.setApplyDate(applyDate);
		}
		if (keyword != null)
		{
			model.setKeyword("%" + keyword + "%");
		}

		Integer totalCount = empj_PaymentGuaranteeDao
				.findByPage_Size(empj_PaymentGuaranteeDao.getQuery_Size(empj_PaymentGuaranteeDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList;
		if (totalCount > 0)
		{
			empj_PaymentGuaranteeList = empj_PaymentGuaranteeDao.findByPage(
					empj_PaymentGuaranteeDao.getQuery(empj_PaymentGuaranteeDao.getBasicHQL(), model), pageNumber,
					countPerPage);

			// 初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Empj_PaymentGuarantee");// 文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20"))
			{
				saveDirectory = saveDirectory.replace("%20", " ");
			}
			
			directoryUtil.mkdir(saveDirectory);

//			String strNewFileName = excelName + "-"
//					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";
			String strNewFileName = MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

			String saveFilePath = saveDirectory + strNewFileName;

			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

			// 自定义字段别名
			writer.addHeaderAlias("ordinal", "序号");
			
			writer.addHeaderAlias("revokeNo", "支付保证撤销申请单号");
			writer.addHeaderAlias("applyDate", "申请日期");
			writer.addHeaderAlias("eCode", "支付保证申请单号");
			writer.addHeaderAlias("companyName", "开发企业");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("guaranteeNo", "支付保证单号");
			writer.addHeaderAlias("guaranteeCompany", "支付保证出具单位");
			writer.addHeaderAlias("guaranteeType", "支付保证类型 ");
			writer.addHeaderAlias("alreadyActualAmount", "撤销项目建设工程已实际支付金额");
			writer.addHeaderAlias("actualAmount", "撤销项目建设工程待支付承保金额");
			writer.addHeaderAlias("guaranteedAmount", "撤销已落实支付保证金额");
			writer.addHeaderAlias("remark", "备注");
			writer.addHeaderAlias("userUpdate", "操作人");
			writer.addHeaderAlias("lastUpdateTimeStamp", "操作日期");
			writer.addHeaderAlias("userRecord", "审核人");
			writer.addHeaderAlias("recordTimeStamp", "审核日期");

			List<Empj_PaymentGuaranteeExportExcelVO> list = formart(empj_PaymentGuaranteeList);
			
			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();
			writer.close();

			properties.put("fileDownloadPath", relativeDir + strNewFileName);
		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}
		
		properties.put("empj_PaymentGuaranteeList", empj_PaymentGuaranteeList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}

	List<Empj_PaymentGuaranteeExportExcelVO> formart(List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList)
	{
		
		List<Empj_PaymentGuaranteeExportExcelVO> list = new ArrayList<Empj_PaymentGuaranteeExportExcelVO>();
		
		int ordinal = 0;
		for (Empj_PaymentGuarantee po : empj_PaymentGuaranteeList)
		{
			++ordinal;
			Empj_PaymentGuaranteeExportExcelVO vo = new Empj_PaymentGuaranteeExportExcelVO();
			
			vo.setOrdinal(ordinal);
			vo.setRevokeNo(po.getRevokeNo());
			vo.setApplyDate(po.getApplyDate());
			vo.seteCode(po.geteCode());
			vo.setCompanyName(po.getCompanyName());
			vo.setProjectName(po.getProjectName());
			vo.setGuaranteeNo(po.getGuaranteeNo());
			vo.setGuaranteeCompany(po.getGuaranteeCompany());
			vo.setGuaranteeType(po.getGuaranteeType());
			vo.setAlreadyActualAmount(po.getAlreadyActualAmount());
			vo.setActualAmount(po.getActualAmount());
			vo.setGuaranteedAmount(po.getGuaranteedAmount());
			vo.setRemark(po.getRemark());
			vo.setUserUpdate(po.getUserUpdate());
			vo.setLastUpdateTimeStamp(po.getLastUpdateTimeStamp());
			vo.setUserRecord(po.getUserRecord());
			vo.setRecordTimeStamp(po.getRecordTimeStamp());

			list.add(vo);
		}

		return list;

	}
}

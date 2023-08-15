package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tgpf_SpecialFundAppropriated_AFExcelVO;
import zhishusz.housepresell.util.*;

/*
 * Service列表查询：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_SpecialFundAppropriated_AFListService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;

	private static final String excelName = "特殊拨付报表";

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		// 设置关键字 拼接like条件
		if (null != keyword)
			model.setKeyword("%" + keyword + "%");
		String applyDate = model.getApplyDate();
		if (null == applyDate || applyDate.trim().isEmpty())
		{
			model.setApplyDate(null);
		}
		String timeStampStart = model.getTimeStampStart();
		if (StringUtils.isBlank(timeStampStart)) {
			model.setTimeStampStart(null);
		} else {
			model.setTimeStampStart(timeStampStart.trim());
		}

		String timeStampEnd = model.getTimeStampEnd();
		if (StringUtils.isBlank(timeStampEnd)) {
			model.setTimeStampEnd(null);
		} else {
			model.setTimeStampEnd(timeStampEnd.trim());
		}

		Integer totalCount = tgpf_SpecialFundAppropriated_AFDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDao
				.getQuery_Size(tgpf_SpecialFundAppropriated_AFDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgpf_SpecialFundAppropriated_AF> tgpf_SpecialFundAppropriated_AFList;
		if (totalCount > 0)
		{
			tgpf_SpecialFundAppropriated_AFList = tgpf_SpecialFundAppropriated_AFDao.findByPage(
					tgpf_SpecialFundAppropriated_AFDao.getQuery(tgpf_SpecialFundAppropriated_AFDao.getBasicHQL(),
							model),
					pageNumber, countPerPage);
		}
		else
		{
			tgpf_SpecialFundAppropriated_AFList = new ArrayList<Tgpf_SpecialFundAppropriated_AF>();
		}

		properties.put("tgpf_SpecialFundAppropriated_AFList", tgpf_SpecialFundAppropriated_AFList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}


	/**
	 * 报表导出
	 *
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Properties reportExportExecute(Tgpf_SpecialFundAppropriated_AFForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		String keyword = model.getKeyword();

		// 设置关键字 拼接like条件
		if (null != keyword)
			model.setKeyword("%" + keyword + "%");
		String applyDate = model.getApplyDate();
		if (null == applyDate || applyDate.trim().isEmpty())
		{
			model.setApplyDate(null);
		}
		String timeStampStart = model.getTimeStampStart();
		if (StringUtils.isBlank(timeStampStart)) {
			model.setTimeStampStart(null);
		} else {
			model.setTimeStampStart(timeStampStart.trim());
		}

		String timeStampEnd = model.getTimeStampEnd();
		if (StringUtils.isBlank(timeStampEnd)) {
			model.setTimeStampEnd(null);
		} else {
			model.setTimeStampEnd(timeStampEnd.trim());
		}

//		System.out.println("timeStampStart="+timeStampStart);
//		System.out.println("timeStampEnd="+timeStampEnd);

		if (StringUtils.isBlank(keyword)) {
			model.setKeyword(null);
		} else {
			model.setKeyword("%" + keyword + "%");
		}

		model.setTheState(S_TheState.Normal);

		Integer totalCount = tgpf_SpecialFundAppropriated_AFDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDao
				.getQuery_Size(tgpf_SpecialFundAppropriated_AFDao.getBasicHQL(), model));


		List<Tgpf_SpecialFundAppropriated_AF> tgpf_SpecialFundAppropriated_AFList;
		List<Tgpf_SpecialFundAppropriated_AFExcelVO> list = new ArrayList<Tgpf_SpecialFundAppropriated_AFExcelVO>();
		if (totalCount > 0) {
			tgpf_SpecialFundAppropriated_AFList = tgpf_SpecialFundAppropriated_AFDao.findByPage(
					tgpf_SpecialFundAppropriated_AFDao.getQuery(tgpf_SpecialFundAppropriated_AFDao.getBasicHQL(), model));


			int i = 0;
			for (Tgpf_SpecialFundAppropriated_AF tgpf_specialFundAppropriated_af : tgpf_SpecialFundAppropriated_AFList) {

				Tgpf_SpecialFundAppropriated_AFExcelVO pro = new Tgpf_SpecialFundAppropriated_AFExcelVO();

				pro.setOrdinal(++i);
				//开发企业名称
				pro.setTheNameOfDevelopCompany(StringUtils.isBlank(tgpf_specialFundAppropriated_af.getTheNameOfDevelopCompany()) ? "" : tgpf_specialFundAppropriated_af.getTheNameOfDevelopCompany());
				pro.setTheNameOfProject(StringUtils.isBlank(tgpf_specialFundAppropriated_af.getTheNameOfProject()) ? "" : tgpf_specialFundAppropriated_af.getTheNameOfProject());
				pro.setEcode(StringUtils.isBlank(tgpf_specialFundAppropriated_af.geteCode()) ? "" : tgpf_specialFundAppropriated_af.geteCode());
				pro.setApplydate(StringUtils.isBlank(tgpf_specialFundAppropriated_af.getApplyDate()) ? "" : tgpf_specialFundAppropriated_af.getApplyDate());
				pro.setECodeFromConstruction(tgpf_specialFundAppropriated_af.geteCodeFromConstruction());
				if(tgpf_specialFundAppropriated_af.getApplyState() != null){
					if(2 == tgpf_specialFundAppropriated_af.getApplyState().intValue()){
						pro.setApplystate("已拨付");
					}else{
						pro.setApplystate("未拨付");
					}
				}else {
					pro.setApplystate("");
				}
				pro.setAfPayoutDate(tgpf_specialFundAppropriated_af.getAfPayoutDate());
				pro.setApprovalstate(tgpf_specialFundAppropriated_af.getApprovalState());
				pro.setTotalapplyamount(tgpf_specialFundAppropriated_af.getTotalApplyAmount());
				pro.setTheNameOfBankAccount(tgpf_specialFundAppropriated_af.getTheNameOfBankAccount());
				list.add(pro);
			}

			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Tgpf_SpecialFundAppropriated_AFListService");// 文件在项目中的相对路径
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
			writer.addHeaderAlias("ordinal", "序号");
			writer.addHeaderAlias("ecode", "用款申请单号");
			writer.addHeaderAlias("applydate", "用款申请日期");
			writer.addHeaderAlias("theNameOfDevelopCompany", "开发企业名称");
			writer.addHeaderAlias("theNameOfProject", "项目名称");
//			writer.addHeaderAlias("approvalDate", "照片上传日期");
			writer.addHeaderAlias("eCodeFromConstruction", "施工编号");
			writer.addHeaderAlias("theNameOfBankAccount", "收款方名称");
			writer.addHeaderAlias("totalapplyamount", "申请金额（元）");
			writer.addHeaderAlias("afPayoutDate", "拨付日期");
			writer.addHeaderAlias("approvalstate", "审批状态");
			writer.addHeaderAlias("applystate", "支付状态");



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

			writer.close();

			properties.put("fileURL", relativeDir + strNewFileName);

		} else {
			return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
		}

		return properties;
	}

}

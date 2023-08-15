package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementReview_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementReview_ViewDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tg_Build_View;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementReview_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tg_Build_ViewExportExcelVO;
import zhishusz.housepresell.exportexcelvo.Tgxy_TripleAgreementReviewExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service 列表查询：三方协议 excel 导出
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgxy_TripleAgreementReview_ViewExcelService
{
	@Autowired
	private Tgxy_TripleAgreementReview_ViewDao tgxy_TripleAgreementReview_ViewDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	
	private static final String excelName = "三方协议考评";

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_TripleAgreementReview_ViewForm model)
	{
		Properties properties = new MyProperties();

		// 获取前台传递过来的分页条件
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		// 设置关键字 拼接like条件
		if (null != keyword)
		{
			model.setKeyword("%" + keyword + "%");
		}else
		{
			model.setKeyword(null);
		}
		// 退回原因
		Long rejectReasonId = model.getRejectReasonId();
		if (null == rejectReasonId || rejectReasonId == 0)
		{
			model.setRejectReason(null);
		}
		else
		{
			Sm_BaseParameter sm_BaseParameter = (Sm_BaseParameter)sm_BaseParameterDao.findById(rejectReasonId);
			if(sm_BaseParameter == null || S_TheState.Deleted.equals(sm_BaseParameter.getTheState()))
			{
				model.setRejectReason(null);
			}
			else
			{
				model.setRejectReason(sm_BaseParameter.getTheValue());
			}
		}
		// 代理公司
		Long proxyCompanyId = model.getProxyCompanyId();
		
		if (null == proxyCompanyId || proxyCompanyId==0)
		{
			model.setProxyCompany(null);
		}
		else
		{
			Emmp_CompanyInfo emmp_CompanyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(proxyCompanyId);
			if(emmp_CompanyInfo == null || S_TheState.Deleted.equals(emmp_CompanyInfo.getTheState()))
			{
				model.setProxyCompany(null);
			}
			else
			{
				model.setProxyCompany(emmp_CompanyInfo.getTheName());
			}
		}
		
		// 起始时间
		String beginTime = model.getBeginTime();
		
		if (null == beginTime || beginTime.trim().isEmpty())
		{
			model.setBeginTime(null);
		}else
		{
			model.setBeginTime(beginTime.trim());
		}
		
		// 结束时间
		String endTime = model.getEndTime();
		
		if (null == endTime || endTime.trim().isEmpty())
		{
			model.setEndTime(null);
		}
		else
		{
			model.setEndTime(endTime.trim());
		}

		Integer totalCount = tgxy_TripleAgreementReview_ViewDao
				.findByPage_Size(tgxy_TripleAgreementReview_ViewDao.getQuery_Size(tgxy_TripleAgreementReview_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgxy_TripleAgreementReview_View> tgxy_TripleAgreementReview_ViewList;
		if (totalCount > 0)
		{
			tgxy_TripleAgreementReview_ViewList = tgxy_TripleAgreementReview_ViewDao.findByPage(
					tgxy_TripleAgreementReview_ViewDao.getQuery(tgxy_TripleAgreementReview_ViewDao.getBasicHQL(), model));
		}
		else
		{
			return MyBackInfo.fail(properties, "没有查询到有效的数据，请核对查询条件！");
		}


		//初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String relativeDir = directoryUtil.createRelativePathWithDate("Tgxy_TripleAgreementReviewExcel");//文件在项目中的相对路径
		String localPath = directoryUtil.getProjectRoot();//项目路径
		
		String saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
		
		saveDirectory = saveDirectory.replace("%20"," ");
		directoryUtil.mkdir(saveDirectory);
		
		String strNewFileName = excelName + "-"
				+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(),"yyyyMMddHHmmss")
				+ ".xlsx";
		
		String saveFilePath = saveDirectory + strNewFileName;
		
		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
		
		//自定义字段别名
		writer.addHeaderAlias("ordinal", "");
		
		writer.addHeaderAlias("eCode", "协议编号");
		writer.addHeaderAlias("projectName", "项目名称");
		writer.addHeaderAlias("position", "房屋坐落");
		writer.addHeaderAlias("buyerName", "买受人姓名");
		writer.addHeaderAlias("areaManager", "区域负责人");
		
		writer.addHeaderAlias("rejectReason", "退回原因");
		writer.addHeaderAlias("rejectTimeStamp", "退回时间");
		writer.addHeaderAlias("proxyCompany", "代理公司");
		writer.addHeaderAlias("changeTimeStamp", "整改时间");
	
		List<Tgxy_TripleAgreementReviewExcelVO> list = formart(tgxy_TripleAgreementReview_ViewList);
		// 一次性写出内容，使用默认样式
		writer.write(list);
		
		// 关闭writer，释放内存
		writer.flush();
		writer.close();
		
		properties.put("fileURL", relativeDir+strNewFileName);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	
	
	/**
	 * po 格式化
	 * @param tg_Build_ViewList
	 * @return
	 */
	List<Tgxy_TripleAgreementReviewExcelVO> formart(List<Tgxy_TripleAgreementReview_View> tgxy_TripleAgreementReview_ViewList){
		List<Tgxy_TripleAgreementReviewExcelVO>  tgxy_TripleAgreementReviewExcelList = new ArrayList<Tgxy_TripleAgreementReviewExcelVO>();
		int ordinal = 0;
		for (Tgxy_TripleAgreementReview_View tripleAgreementReview : tgxy_TripleAgreementReview_ViewList)
		{
			++ordinal;
			Tgxy_TripleAgreementReviewExcelVO tgxy_TripleAgreementReviewExcel = new Tgxy_TripleAgreementReviewExcelVO();
			tgxy_TripleAgreementReviewExcel.setOrdinal(ordinal);
			
			tgxy_TripleAgreementReviewExcel.seteCode(tripleAgreementReview.geteCode());
			tgxy_TripleAgreementReviewExcel.setProjectName(tripleAgreementReview.getProjectName());
			tgxy_TripleAgreementReviewExcel.setPosition(tripleAgreementReview.getPosition());
			tgxy_TripleAgreementReviewExcel.setBuyerName(tripleAgreementReview.getBuyerName());
			tgxy_TripleAgreementReviewExcel.setAreaManager (tripleAgreementReview.getAreaManager());
			tgxy_TripleAgreementReviewExcel.setRejectReason(tripleAgreementReview.getRejectReason());
			tgxy_TripleAgreementReviewExcel.setRejectTimeStamp(tripleAgreementReview.getRejectTimeStamp());
			tgxy_TripleAgreementReviewExcel.setProxyCompany(tripleAgreementReview.getProxyCompany());
			tgxy_TripleAgreementReviewExcel.setChangeTimeStamp(tripleAgreementReview.getChangeTimeStamp());
			
			tgxy_TripleAgreementReviewExcelList.add(tgxy_TripleAgreementReviewExcel);
		}
		return tgxy_TripleAgreementReviewExcelList;
	}
}

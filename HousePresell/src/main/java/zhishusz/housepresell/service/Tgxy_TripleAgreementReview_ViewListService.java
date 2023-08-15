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
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementReview_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：三方协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgxy_TripleAgreementReview_ViewListService
{
	@Autowired
	private Tgxy_TripleAgreementReview_ViewDao tgxy_TripleAgreementReview_ViewDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

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
					tgxy_TripleAgreementReview_ViewDao.getQuery(tgxy_TripleAgreementReview_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tgxy_TripleAgreementReview_ViewList = new ArrayList<Tgxy_TripleAgreementReview_View>();
		}

		properties.put("Tgxy_TripleAgreementReview_ViewList", tgxy_TripleAgreementReview_ViewList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

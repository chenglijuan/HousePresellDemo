package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_CompanyType;
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
public class Tgxy_TripleAgreementListService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		// 获取前台传递过来的分页条件
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		model.setTheState(S_TheState.Normal);
		
		Sm_User user = model.getUser();
		if(null == user)
		{
			return MyBackInfo.fail(properties, "请先登录！");
		}
		/*
		 * xsz by time 2019-2-25 18:29:19
		 * 判断当前登录人是否是代理公司人员
		 */
		Emmp_CompanyInfo company = user.getCompany();
		if(null != company && ( S_CompanyType.Agency.equals(company.getTheType()) || S_CompanyType.Zhengtai.equals(company.getTheType())) )
		{
			model.setIsAgency(null);
		}
		else
		{
			model.setIsAgency("0");
			model.setCompanyId(company.getTableId());
		}

		//区域
		if(null == model.getCityRegionId() || model.getCityRegionId()<0)
		{
			model.setCityRegionId(null);
		}
		// 设置关键字 拼接like条件
		if (null != keyword)
		{
			model.setKeyword("%" + keyword + "%");
		}
		String theStateOfTripleAgreement = model.getTheStateOfTripleAgreement();
		if (null == theStateOfTripleAgreement || theStateOfTripleAgreement.trim().isEmpty())
		{
			model.setTheStateOfTripleAgreement(null);
		}
		
		String startDate = model.getStartDate();
		if(null == startDate || startDate.trim().isEmpty())
		{
			model.setStartDate(null);
		}
		String endDate = model.getEndDate();
		if(null == endDate || endDate.trim().isEmpty())
		{
			model.setEndDate(null);
		}

		//如果分配的区域为大于6个，查询所有
		if(null != model.getCityRegionInfoIdArr() && model.getCityRegionInfoIdArr().length >= 6){
			Long[] temp = null;
			model.setCityRegionInfoIdArr(temp);
			model.setProjectInfoIdArr(temp);
			model.setBuildingInfoIdIdArr(temp);
		}

		Integer totalCount = tgxy_TripleAgreementDao
				.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgxy_TripleAgreement> tgxy_TripleAgreementList ;
		if (totalCount > 0)
		{
			tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(
					tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tgxy_TripleAgreementList = new ArrayList<Tgxy_TripleAgreement>();
		}

		properties.put("tgxy_TripleAgreementList", tgxy_TripleAgreementList);
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

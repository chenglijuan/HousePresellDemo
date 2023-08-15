package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.util.MyBackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Emmp_CompanyInfoListService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_UserDao sm_userDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();

		Long loginUserId = model.getUserId();
		Sm_User loginUser = sm_userDao.findById(loginUserId);

		model.setTheState(S_TheState.Normal);
		Long companyInfoId = null;
		if(loginUser.getCompany()==null)
		{
			return MyBackInfo.fail(properties, "登录用户所属企业不能为空");
		}

		if ("".equals(model.getOrderBy()))
		{
			model.setOrderBy(null);
		}
		else if (model.getOrderBy() != null)
		{
			String[] orderByAr = model.getOrderBy().split(" ");
			model.setOrderBy(orderByAr[0]);
			model.setOrderByType(orderByAr[1]);
		}

		Emmp_CompanyInfo emmp_companyInfo = loginUser.getCompany();

		if(emmp_companyInfo.getTheType() == null)
		{
			return MyBackInfo.fail(properties, "机构类型不能为空");
		}
		if(S_CompanyType.Development.equals(emmp_companyInfo.getTheType()))
		{
			companyInfoId = emmp_companyInfo.getTableId();
			properties.put("emmp_companyInfo",emmp_companyInfo);
			
			model.setTableId(emmp_companyInfo.getTableId());
			model.setOrgMember("1");
			
		}
		else {
			
		}
		
		/*if(S_UserType.ZhengtaiUser.equals(loginUser.getTheType()))//正泰用户
		{
			
		}
		else//普通用户
		{
			model.setTableId(emmp_companyInfo.getTableId());
		}*/
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Long cityRegionId = model.getCityRegionId();
		
		if(keyword != null)
		{
			model.setKeyword("%"+keyword+"%");
		}
		
		// 启用备注字段用来，区分是否为机构用户
		/*String orgMember = model.getOrgMember();
		if(orgMember == null || orgMember.length() == 0)
		{

		}
		else
		{
			model.setOrgMember("1");
		}*/

		if (model.getBusiState() != null) {
			if (model.getBusiState().length() == 0)
			{
				model.setBusiState(null);
			}
		}

		if (model.getApprovalState() != null) {
			if (model.getApprovalState().length() == 0) {
				model.setApprovalState(null);
			}
		}

		if(cityRegionId != null && cityRegionId > 0){
			Sm_CityRegionInfo sm_CityRegionInfo = sm_CityRegionInfoDao.findById(cityRegionId);
			model.setCityRegion(sm_CityRegionInfo);
		}
		
		Integer totalCount = emmp_CompanyInfoDao.findByPage_Size(emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Emmp_CompanyInfo> emmp_CompanyInfoList;
		if(totalCount > 0)
		{
			emmp_CompanyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			emmp_CompanyInfoList = new ArrayList<Emmp_CompanyInfo>();
		}
		
		properties.put("emmp_CompanyInfoList", emmp_CompanyInfoList);
		properties.put("companyInfoId",companyInfoId);

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

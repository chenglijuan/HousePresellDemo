package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.MyBackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_ProjectInfoListService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@Autowired
	private Sm_UserDao sm_userDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		Sm_User sm_user = model.getUser();
		if(sm_user == null)
		{
			return MyBackInfo.fail(properties, "登录用户,不能为空");
		}

		if(!S_UserType.ZhengtaiUser.equals(sm_user.getTheType()) && sm_user.getCompany() == null)
		{
			//非正泰用户需要筛选企业ID（已在Dao中实现，使用userStart.tableId过滤）
			//非正泰用户需要判断所属机是否为空
			return MyBackInfo.fail(properties, "登录用户所属机构,不能为空");
		}
//		if(S_UserType.ZhengtaiUser.equals(sm_user.getTheType()))
//		{
//			//正泰用户可以查看所有企业不需要筛选企业ID
//			//model.setDevelopCompanyId(null);
//		}
//		else
//		{
//			//非正泰用户需要筛选企业ID
//			if(sm_user.getCompany() == null)
//			{
//				return MyBackInfo.fail(properties, "登录用户所属机构,不能为空");
//			}
//			/*Long developCompanyId = sm_user.getCompany().getTableId();//获取企业ID
//
//			//有传入筛选的企业ID则不需要存入用户的所属企业ID，反之则需要
//			if(model.getDevelopCompanyId() == null  || model.getDevelopCompanyId() < 1)
//			{
//				model.setDevelopCompanyId(developCompanyId);
//			}*/
//		}

		String keyword = model.getKeyword();
		String busiState = model.getBusiState();
		String approcvalState = model.getApprovalState();
		Long cityRegionId = model.getCityRegionId();
		if(cityRegionId == null || cityRegionId < 1){
			model.setCityRegionId(null);
		}
		
		if (keyword != null && !"".equals(keyword)) 
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}
		if (busiState == null || "".equals(busiState) || "全部".equals(busiState))
		{
			model.setBusiState(null);
		}
		model.setApprovalState(null);
//		if (approcvalState == null || "".equals(approcvalState) || "全部".equals(approcvalState))
//		{
//			model.setApprovalState(null);
//		}
		model.setTheState(S_TheState.Normal);

		//如果分配的区域为大于6个，查询所有
		if(null != model.getCityRegionInfoIdArr() && model.getCityRegionInfoIdArr().length >= 6){
			Long[] temp = null;
			model.setCityRegionInfoIdArr(temp);
			model.setProjectInfoIdArr(temp);
			model.setBuildingInfoIdIdArr(temp);
		}

		Integer totalCount = empj_ProjectInfoDao.findByPage_Size(empj_ProjectInfoDao.createNewCriteriaForList(model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_ProjectInfo> empj_ProjectInfoList;
		if(totalCount > 0)
		{
			empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.createNewCriteriaForList(model), pageNumber, countPerPage);
		}
		else
		{
			empj_ProjectInfoList = new ArrayList<Empj_ProjectInfo>();
		}
		
		properties.put("empj_ProjectInfoList", empj_ProjectInfoList);

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

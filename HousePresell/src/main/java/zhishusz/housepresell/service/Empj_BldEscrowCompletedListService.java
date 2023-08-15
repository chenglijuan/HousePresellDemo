package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.MyBackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BldEscrowCompletedListService
{
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

//		Sm_User sm_user = model.getUser();
//		if(sm_user == null)
//		{
//			return MyBackInfo.fail(properties, "登录用户不存在，请重新登录");
//		}
//		if(S_UserType.ZhengtaiUser.equals(sm_user.getTheType()))
//		{
//			//正泰用户可以查看所有企业不需要筛选企业ID
//			model.setDevelopCompanyId(null);
//		}
//		else
//		{
//			//非正泰用户需要筛选企业ID
//			if(sm_user.getCompany() == null)
//			{
//				return MyBackInfo.fail(properties, "登录用户所属机构,不能为空");
//			}
//			Long developCompanyId = sm_user.getCompany().getTableId();//获取企业ID
//
//			//有传入筛选的企业ID则不需要存入用户的所属企业ID，反之则需要
//			if(model.getDevelopCompanyId() == null  || model.getDevelopCompanyId() < 1)
//			{
//				model.setDevelopCompanyId(developCompanyId);
//			}
//		}


		String keyword = model.getKeyword();
		String busiState = model.getBusiState();
		model.setTheState(S_TheState.Normal);
		if (keyword != null && !"".equals(keyword))
		{
			model.setKeyword("%"+keyword+"%");
		}
		else
		{
			model.setKeyword(null);
		}
		if ("".equals(busiState) || "0".equals(busiState) || "全部".equals(busiState))
		{
			model.setBusiState(null);
		}
		model.setTheState(S_TheState.Normal);
		
		String hasPush = model.getHasPush();
		if(StrUtil.isBlank(hasPush)){
			model.setHasPush(null);
		}else{
			model.setHasPush(model.getHasPush().trim());
		}

		Integer totalCount =
				empj_BldEscrowCompletedDao.findByPage_Size(empj_BldEscrowCompletedDao.createNewCriteriaForList(model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_BldEscrowCompleted> empj_BldEscrowCompletedList;
		if(totalCount > 0)
		{
			empj_BldEscrowCompletedList = empj_BldEscrowCompletedDao.findByPage(empj_BldEscrowCompletedDao.createNewCriteriaForList(model), pageNumber, countPerPage);
		}
		else
		{
			empj_BldEscrowCompletedList = new ArrayList<Empj_BldEscrowCompleted>();
		}
		
		properties.put("empj_BldEscrowCompletedList", empj_BldEscrowCompletedList);
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

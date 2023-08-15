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
 * 根据开发企业加载项目列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_SpecialFundProjectInfoListService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = empj_ProjectInfoDao.findByPage_Size(empj_ProjectInfoDao.getQuery_Size(empj_ProjectInfoDao.getBasicHQL(), model));
		
		List<Empj_ProjectInfo> empj_ProjectInfoList;
		if(totalCount > 0)
		{
			empj_ProjectInfoList = empj_ProjectInfoDao.findByPage(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), model));
		}
		else
		{
			empj_ProjectInfoList = new ArrayList<Empj_ProjectInfo>();
		}
		
		properties.put("empj_ProjectInfoList", empj_ProjectInfoList);

		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

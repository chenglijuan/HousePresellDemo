package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：
 * 根据选择项目加载楼幢列表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_SpecialFundBuildingInfoListService
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();
		
		model.setKeyword(null);
		
		/*
		 * xsz by time 2018-11-30 09:35:45
		 * 已于段总确认，只需要加载已备案的楼幢即可
		 */
		model.setBusiState(S_BusiState.HaveRecord);
		
		Integer totalCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getBasicHQL(), model));
		
		
		List<Empj_BuildingInfo> empj_BuildingInfoList;
		if(totalCount > 0)
		{
			empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), model));
		}
		else
		{
			empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
		}

		properties.put("empj_BuildingInfoList", empj_BuildingInfoList);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

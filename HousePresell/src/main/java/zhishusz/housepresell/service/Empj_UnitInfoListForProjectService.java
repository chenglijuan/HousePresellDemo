package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_UnitInfoListForProjectService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		model.setTheState(S_TheState.Normal);
		Long projectId = model.getProjectId();
		if (projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目不存在");
		}

		Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
		buildingInfoForm.setInterfaceVersion(model.getInterfaceVersion());
		buildingInfoForm.setTheState(S_TheState.Normal);
		buildingInfoForm.setProjectId(projectId);

		List<Empj_BuildingInfo> empj_BuildingInfoList =
				empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(),
						buildingInfoForm), null, null);
		List<Long> idArray = new ArrayList<Long>();
		if (empj_BuildingInfoList != null && empj_BuildingInfoList.size() > 0)
		{
			for (Empj_BuildingInfo itemBuildingInfo : empj_BuildingInfoList)
			{
				idArray.add(itemBuildingInfo.getTableId());
			}
			Long[] idArr = new Long[idArray.size()];
			idArray.toArray(idArr);
			model.setIdArr(idArr);
		}
		else
		{
			model.setIdArr(null);
		}

		Integer totalCount;
		if (model.getIdArr() != null && model.getIdArr().length > 0)
		{
			totalCount = empj_UnitInfoDao.findByPage_Size(empj_UnitInfoDao.getQuery_Size(empj_UnitInfoDao.getExcelListForProjectHQL(), model));
		}
		else
		{
			totalCount = 0;
		}

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_UnitInfo> empj_UnitInfoList;
		if(totalCount > 0)
		{
			empj_UnitInfoList = empj_UnitInfoDao.findByPage(empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getExcelListForProjectHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			empj_UnitInfoList = new ArrayList<Empj_UnitInfo>();
		}
		
		properties.put("empj_UnitInfoList", empj_UnitInfoList);
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

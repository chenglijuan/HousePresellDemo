package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


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

/*
 * Service列表查询：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_UnitInfoDetailListService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long empj_BuildingInfoId = model.getTableId();
		
		Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(empj_BuildingInfoId);
		if(empj_BuildingInfo == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "该楼幢信息有误！");
		}
		
		// 项目名称
		String projectName = empj_BuildingInfo.getTheNameOfProject();
		// 楼幢施工编号
		String eCodeFromConstruction = empj_BuildingInfo.geteCodeFromConstruction();
		// 单元总数
		int unitNumber = 0;
		if(null == empj_BuildingInfo.getUnitNumber() || 0 == empj_BuildingInfo.getUnitNumber())
		{
			unitNumber = 0;
		}
		else
		{
			unitNumber = empj_BuildingInfo.getUnitNumber();
		}
			
		// 查询楼幢-单元表
		// 查询条件：1.状态：正常 2.楼幢
		Empj_UnitInfoForm empj_UnitInfoForm = new Empj_UnitInfoForm();
		empj_UnitInfoForm.setTheState(S_TheState.Normal);
		empj_UnitInfoForm.setBuilding(empj_BuildingInfo);
				
		Integer unitCount = empj_UnitInfoDao.findByPage_Size(empj_UnitInfoDao.getQuery_Size(empj_UnitInfoDao.getBasicHQL(), empj_UnitInfoForm));	
		
		List<Empj_UnitInfo> empj_UnitInfoList;
		if(unitCount > 0)
		{
			empj_UnitInfoList = empj_UnitInfoDao.findByPage(empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getBasicHQL(), empj_UnitInfoForm));
		}
		else
		{
			empj_UnitInfoList = new ArrayList<Empj_UnitInfo>();
		}
		
		properties.put("empj_UnitInfoList", empj_UnitInfoList);
		properties.put("projectName", projectName);
		properties.put("eCodeFromConstruction", eCodeFromConstruction);
		properties.put("unitNumber", unitNumber);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_UnitInfoBatchDeleteService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(tableId);
			if(empj_UnitInfo == null)
			{
				return MyBackInfo.fail(properties, "选择的单元没有信息！");
			}
			
			Empj_HouseInfoForm empj_HouseInfoForm = new Empj_HouseInfoForm();
			empj_HouseInfoForm.setUnitInfo(empj_UnitInfo);
			empj_HouseInfoForm.setTheState(S_TheState.Normal);
			
			Integer totalCount = empj_HouseInfoDao.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getBasicHQL(), empj_HouseInfoForm));
			
			if(totalCount > 0)
			{
				return MyBackInfo.fail(properties, "请先删除单元下所有的户信息！");
			}
		
			Empj_BuildingInfo empj_BuildingInfo = empj_UnitInfo.getBuilding();
			// 修改楼幢信息表中的单元数
			int unitNumber = 0;
			
			if( null == empj_BuildingInfo.getUnitNumber() || 0 == empj_BuildingInfo.getUnitNumber())
			{
				unitNumber = 0 ;
			}
			else
			{
				unitNumber = empj_BuildingInfo.getUnitNumber();
			}
			
			if( unitNumber > 0)
			{
				empj_BuildingInfo.setUnitNumber(unitNumber-1);		
			}
			empj_BuildingInfoDao.save(empj_BuildingInfo);
			
			empj_UnitInfo.setTheState(S_TheState.Deleted);
			empj_UnitInfoDao.save(empj_UnitInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

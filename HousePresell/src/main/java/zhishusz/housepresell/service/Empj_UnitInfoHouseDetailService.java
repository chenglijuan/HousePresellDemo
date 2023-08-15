package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢-单元
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_UnitInfoHouseDetailService
{
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	
	MyDouble mydouble = MyDouble.getInstance();

	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_UnitInfoId = model.getTableId();
		Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);
		if(empj_UnitInfo == null || S_TheState.Deleted.equals(empj_UnitInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "单元信息不存在");
		}
		
		int upfloorNumber = mydouble.getShort(empj_UnitInfo.getUpfloorNumber(), 0).intValue();	//地上楼层数
		int upfloorHouseHoldNumber = empj_UnitInfo.getUpfloorHouseHoldNumber();	//地上每层户数
		int downfloorNumber = mydouble.getShort(empj_UnitInfo.getDownfloorNumber(), 0).intValue();//地下楼层数
		int downfloorHouseHoldNumber = empj_UnitInfo.getDownfloorHouseHoldNumber();//地下每层户数
		
		List<Empj_HouseInfo> empj_HouseInfoListUpfloor = new ArrayList<Empj_HouseInfo>();
		List<Empj_HouseInfo> empj_HouseInfoListDownfloor = new ArrayList<Empj_HouseInfo>();
		
		if( upfloorNumber > 0 && upfloorHouseHoldNumber > 0)
		{
			for(int i = 0;i < upfloorHouseHoldNumber ; i++)
			{
				Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
				
				empj_HouseInfo.setRoomId("10"+(i+1));				
				empj_HouseInfo.setPurpose("0");				
				empj_HouseInfoListUpfloor.add(empj_HouseInfo);
			}			
		}
		
		if( downfloorNumber > 0 && downfloorHouseHoldNumber > 0)
		{
			for(int i = 0;i < downfloorHouseHoldNumber ; i++)
			{
				Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
				
				empj_HouseInfo.setRoomId("地下室0"+(i+1));
				empj_HouseInfo.setPurpose("0");
				empj_HouseInfoListDownfloor.add(empj_HouseInfo);
			}		
		}
	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_HouseInfoListUpfloor", empj_HouseInfoListUpfloor);
		properties.put("empj_HouseInfoListDownfloor", empj_HouseInfoListDownfloor);

		return properties;
	}
}

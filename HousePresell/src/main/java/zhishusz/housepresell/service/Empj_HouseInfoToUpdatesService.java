package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoToUpdatesService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long empj_HouseInfoid=model.getTableId();
		Empj_HouseInfo empj_HouseInfo=(Empj_HouseInfo)empj_HouseInfoDao.findById(empj_HouseInfoid);
		if(empj_HouseInfo == null || S_TheState.Deleted.equals(empj_HouseInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_HouseInfo(Id:" + empj_HouseInfoid + ")'不存在");
		}
		Empj_HouseInfo outempj_HouseInfo=new Empj_HouseInfo();
		outempj_HouseInfo.setTableId(empj_HouseInfo.getTableId());
		outempj_HouseInfo.setRoomId(empj_HouseInfo.getRoomId());
		outempj_HouseInfo.setPosition(empj_HouseInfo.getPosition());
		outempj_HouseInfo.setFloor(empj_HouseInfo.getFloor());
		outempj_HouseInfo.setForecastArea(empj_HouseInfo.getForecastArea());
		outempj_HouseInfo.setShareConsArea(empj_HouseInfo.getShareConsArea());
		outempj_HouseInfo.setInnerconsArea(empj_HouseInfo.getInnerconsArea());
		outempj_HouseInfo.setPurpose(empj_HouseInfo.getPurpose());
		outempj_HouseInfo.setRecordPrice(empj_HouseInfo.getRecordPrice());
		outempj_HouseInfo.setLastTimeStampSyncRecordPriceToPresellSystem(empj_HouseInfo.getLastTimeStampSyncRecordPriceToPresellSystem());
		//房屋状态theHouseState
		//outempj_HouseInfo.setTheHouseState(empj_HouseInfo.getTheHouseState());
		//获取托管状态
		outempj_HouseInfo.setBusiState(empj_HouseInfo.getBusiState());
		//三方协议号
		if(empj_HouseInfo.getTripleAgreement()!=null){
			outempj_HouseInfo.setTripleAgreement(empj_HouseInfo.getTripleAgreement());
			outempj_HouseInfo.getTripleAgreement().seteCodeOfTripleAgreement(empj_HouseInfo.getTripleAgreement().geteCodeOfTripleAgreement());
		}
		outempj_HouseInfo.setRemark(empj_HouseInfo.getRemark());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_HouseInfo", outempj_HouseInfo);
		
		return properties;
	}
}

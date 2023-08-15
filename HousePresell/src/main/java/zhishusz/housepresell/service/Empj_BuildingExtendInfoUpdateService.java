package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_BuildingExtendInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;

/*
 * Service更新操作：楼幢-扩展信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingExtendInfoUpdateService
{
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	public Properties execute(Empj_BuildingExtendInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		Long buildingInfoId = model.getBuildingInfoId();
		String presellState = model.getPresellState();
		String eCodeOfPresell = model.geteCodeOfPresell();
		Long presellDate = model.getPresellDate();
		String limitState = model.getLimitState();
		String escrowState = model.getEscrowState();
		Integer landMortgageState = model.getLandMortgageState();
		String landMortgagor = model.getLandMortgagor();
		Double landMortgageAmount = model.getLandMortgageAmount();
		Boolean isSupportPGS = model.getIsSupportPGS();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(buildingInfoId == null || buildingInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingInfo'不能为空");
		}
		if(presellState == null || presellState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'presellState'不能为空");
		}
		if(eCodeOfPresell == null || eCodeOfPresell.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfPresell'不能为空");
		}
		if(presellDate == null || presellDate < 1)
		{
			return MyBackInfo.fail(properties, "'presellDate'不能为空");
		}
		if(limitState == null || limitState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'limitState'不能为空");
		}
		if(escrowState == null || escrowState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'escrowState'不能为空");
		}
		if(landMortgageState == null || landMortgageState < 1)
		{
			return MyBackInfo.fail(properties, "'landMortgageState'不能为空");
		}
		if(landMortgagor == null || landMortgagor.length() == 0)
		{
			return MyBackInfo.fail(properties, "'landMortgagor'不能为空");
		}
		if(landMortgageAmount == null || landMortgageAmount < 1)
		{
			return MyBackInfo.fail(properties, "'landMortgageAmount'不能为空");
		}
		if(isSupportPGS == null)
		{
			return MyBackInfo.fail(properties, "'isSupportPGS'不能为空");
		}
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingInfoId);
		if(buildingInfo == null)
		{
			return MyBackInfo.fail(properties, "'buildingInfo(Id:" + buildingInfoId + ")'不存在");
		}
	
		Long empj_BuildingExtendInfoId = model.getTableId();
		Empj_BuildingExtendInfo empj_BuildingExtendInfo = (Empj_BuildingExtendInfo)empj_BuildingExtendInfoDao.findById(empj_BuildingExtendInfoId);
		if(empj_BuildingExtendInfo == null)
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingExtendInfo(Id:" + empj_BuildingExtendInfoId + ")'不存在");
		}
		
		empj_BuildingExtendInfo.setTheState(theState);
		empj_BuildingExtendInfo.setBuildingInfo(buildingInfo);
		empj_BuildingExtendInfo.setPresellState(presellState);
		empj_BuildingExtendInfo.seteCodeOfPresell(eCodeOfPresell);
		empj_BuildingExtendInfo.setPresellDate(presellDate);
		empj_BuildingExtendInfo.setLimitState(limitState);
		empj_BuildingExtendInfo.setEscrowState(escrowState);
		empj_BuildingExtendInfo.setLandMortgageState(landMortgageState);
		empj_BuildingExtendInfo.setLandMortgagor(landMortgagor);
		empj_BuildingExtendInfo.setLandMortgageAmount(landMortgageAmount);
		empj_BuildingExtendInfo.setIsSupportPGS(isSupportPGS);
	
		empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

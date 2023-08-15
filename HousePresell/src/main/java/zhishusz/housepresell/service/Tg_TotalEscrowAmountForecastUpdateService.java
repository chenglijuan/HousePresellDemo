package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tg_TotalEscrowAmountForecastForm;
import zhishusz.housepresell.database.dao.Tg_TotalEscrowAmountForecastDao;
import zhishusz.housepresell.database.po.Tg_TotalEscrowAmountForecast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：托管总资金的预测结存
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_TotalEscrowAmountForecastUpdateService
{
	@Autowired
	private Tg_TotalEscrowAmountForecastDao tg_TotalEscrowAmountForecastDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tg_TotalEscrowAmountForecastForm model)
	{
		Properties properties = new MyProperties();
		
		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		String lastAmount = model.getLastAmount();
		Double incomeTotal = model.getIncomeTotal();
		Double payTotal = model.getPayTotal();
		Double currentAmount = model.getCurrentAmount();
		Double escrowAmountReferenceValue = model.getEscrowAmountReferenceValue();
		Double escrowAmount = model.getEscrowAmount();
		Double referenceValue1 = model.getReferenceValue1();
		Double referenceValue2 = model.getReferenceValue2();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(lastAmount == null || lastAmount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'lastAmount'不能为空");
		}
		if(incomeTotal == null || incomeTotal < 1)
		{
			return MyBackInfo.fail(properties, "'incomeTotal'不能为空");
		}
		if(payTotal == null || payTotal < 1)
		{
			return MyBackInfo.fail(properties, "'payTotal'不能为空");
		}
		if(currentAmount == null || currentAmount < 1)
		{
			return MyBackInfo.fail(properties, "'currentAmount'不能为空");
		}
		if(escrowAmountReferenceValue == null || escrowAmountReferenceValue < 1)
		{
			return MyBackInfo.fail(properties, "'escrowAmountReferenceValue'不能为空");
		}
		if(escrowAmount == null || escrowAmount < 1)
		{
			return MyBackInfo.fail(properties, "'escrowAmount'不能为空");
		}
		if(referenceValue1 == null || referenceValue1 < 1)
		{
			return MyBackInfo.fail(properties, "'referenceValue1'不能为空");
		}
		if(referenceValue2 == null || referenceValue2 < 1)
		{
			return MyBackInfo.fail(properties, "'referenceValue2'不能为空");
		}
		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart(Id:" + userStartId + ")'不存在");
		}
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord(Id:" + userRecordId + ")'不存在");
		}
	
		Long tg_TotalEscrowAmountForecastId = model.getTableId();
		Tg_TotalEscrowAmountForecast tg_TotalEscrowAmountForecast = (Tg_TotalEscrowAmountForecast)tg_TotalEscrowAmountForecastDao.findById(tg_TotalEscrowAmountForecastId);
		if(tg_TotalEscrowAmountForecast == null)
		{
			return MyBackInfo.fail(properties, "'Tg_TotalEscrowAmountForecast(Id:" + tg_TotalEscrowAmountForecastId + ")'不存在");
		}
		
		tg_TotalEscrowAmountForecast.setTheState(theState);
		tg_TotalEscrowAmountForecast.setBusiState(busiState);
		tg_TotalEscrowAmountForecast.seteCode(eCode);
		tg_TotalEscrowAmountForecast.setUserStart(userStart);
		tg_TotalEscrowAmountForecast.setCreateTimeStamp(createTimeStamp);
		tg_TotalEscrowAmountForecast.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tg_TotalEscrowAmountForecast.setUserRecord(userRecord);
		tg_TotalEscrowAmountForecast.setRecordTimeStamp(recordTimeStamp);
		tg_TotalEscrowAmountForecast.setLastAmount(lastAmount);
		tg_TotalEscrowAmountForecast.setIncomeTotal(incomeTotal);
		tg_TotalEscrowAmountForecast.setPayTotal(payTotal);
		tg_TotalEscrowAmountForecast.setCurrentAmount(currentAmount);
		tg_TotalEscrowAmountForecast.setEscrowAmountReferenceValue(escrowAmountReferenceValue);
		tg_TotalEscrowAmountForecast.setEscrowAmount(escrowAmount);
		tg_TotalEscrowAmountForecast.setReferenceValue1(referenceValue1);
		tg_TotalEscrowAmountForecast.setReferenceValue2(referenceValue2);
	
		tg_TotalEscrowAmountForecastDao.save(tg_TotalEscrowAmountForecast);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

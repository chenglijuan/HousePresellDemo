package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineMonthSumForm;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/*
 * Service详情：风控月度小结
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_RiskRoutineMonthSumDetailService
{
	@Autowired
	private Tg_RiskRoutineCheckInfoListService tg_riskRoutineCheckInfoListService;
	@Autowired
	private Tg_RiskCheckBusiCodeSumListService tg_riskCheckBusiCodeSumListService;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskRoutineMonthSumForm model)
	{
		Properties properties = new MyProperties();
		String enableDateSearchStr = model.getDateStr();
		if(enableDateSearchStr != null && enableDateSearchStr.length() > 0)
		{
			long startTimeStamp = MyDatetime.getInstance().stringToLong(enableDateSearchStr, "yyyy-MM");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startTimeStamp);
			calendar.add(Calendar.MONTH, 1);
			model.setStartDateTimeStamp(startTimeStamp);
			model.setEndDateTimeStamp(calendar.getTimeInMillis());
		}

		Tg_RiskCheckBusiCodeSumForm form = new Tg_RiskCheckBusiCodeSumForm();
		form.setTheState(S_TheState.Normal);
		form.setKeyword(model.getKeyword());
		form.setStartDateTimeStamp(model.getStartDateTimeStamp());
		form.setEndDateTimeStamp(model.getEndDateTimeStamp());
		form.setBigBusiType(model.getBigBusiType());
		List<Tg_RiskCheckBusiCodeSum> tg_RiskCheckMonthSumList = (List<Tg_RiskCheckBusiCodeSum>) tg_riskCheckBusiCodeSumListService.execute(form).get("tg_RiskCheckBusiCodeSumList");

		properties.put("tg_RiskRoutineMonthSumDetail", tg_RiskCheckMonthSumList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	private boolean isOutTime(Long startTime, Long endTime, int timeLimit) {
		return endTime - startTime > timeLimit * 24L * 60 * 60 * 1000;
	}

	private Integer getTimeLimit(List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList, String theType) {
		for (Tg_HandleTimeLimitConfig tg_handleTimeLimitConfig : tg_HandleTimeLimitConfigList) {
			if (theType.equals(tg_handleTimeLimitConfig.getTheType())) {
				return tg_handleTimeLimitConfig.getLimitDayNumber();
			}
		}
		return null;
	}

	private Tg_HandleTimeLimitConfig getHandleTimeLimitConfig(List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList, String theType) {
		for (Tg_HandleTimeLimitConfig tg_handleTimeLimitConfig : tg_HandleTimeLimitConfigList) {
			if (theType.equals(tg_handleTimeLimitConfig.getTheType())) {
				return tg_handleTimeLimitConfig;
			}
		}
		return null;
	}

}

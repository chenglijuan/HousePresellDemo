package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineMonthSumForm;
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
 * Service列表查询：风控月度小结
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_RiskRoutineMonthSumListService
{
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
		List<Tg_RiskCheckBusiCodeSum> tg_RiskCheckMonthSumList = (List<Tg_RiskCheckBusiCodeSum>) tg_riskCheckBusiCodeSumListService.execute(form).get("tg_RiskCheckBusiCodeSumList");

		properties.put("tg_RiskRoutineMonthSumList", tg_RiskCheckMonthSumList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

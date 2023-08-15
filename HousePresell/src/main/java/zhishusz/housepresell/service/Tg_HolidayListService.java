package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_HolidayForm;
import zhishusz.housepresell.database.dao.Tg_HolidayDao;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.Tg_Holiday;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：假期
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_HolidayListService
{
	@Autowired
	private Tg_HolidayDao tg_holidayDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_HolidayForm model)
	{
		Properties properties = new MyProperties();

		List<Tg_Holiday> tg_holidays = tg_holidayDao.findByPage(tg_holidayDao.getQuery(tg_holidayDao.getBasicHQL(), model), 0, 10);

		properties.put("tg_holidays", tg_holidays);
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
	
	public static class WorkTimeLimit {
		
		@Getter @Setter @IFieldAnnotation(remark="类型")
		private Integer type;
		@Getter @Setter @IFieldAnnotation(remark="类型名称")
		private String typeName;
		@Getter @Setter @IFieldAnnotation(remark="总数")
		private Integer size;
		@Getter @Setter @IFieldAnnotation(remark="超时个数")
		private Integer timeOutCount;
		@Getter @Setter @IFieldAnnotation(remark="业务代码")
		private String busiCode;
	}
}

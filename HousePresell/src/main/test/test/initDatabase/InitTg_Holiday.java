package test.initDatabase;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_HolidayForm;
import zhishusz.housepresell.database.dao.Tg_HolidayDao;
import zhishusz.housepresell.database.po.Tg_Holiday;
import zhishusz.housepresell.util.MyDatetime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTg_Holiday extends BaseJunitTest 
{
	@Autowired
	private Tg_HolidayDao tg_HolidayDao;
	private static final String StartDate = "2018-01-01";
	private static final String EndDate = "2019-12-31";
	
	@Test
	public void execute() throws Exception 
	{
		String date = StartDate;
		while (DateUtil.parse(date).getTime() <= DateUtil.parse(EndDate).getTime()) {
			String result = HttpUtil.get("http://api.goseek.cn/Tools/holiday?date=" + date);
			Gson gson = new GsonBuilder().create();
			Tg_Holiday tg_Holiday = gson.fromJson(result, new TypeToken<Tg_Holiday>(){}.getType());
			System.out.println("tg_Holiday:" + tg_Holiday.getType());
			tg_Holiday.setDateTime(date);
			Tg_HolidayForm tg_HolidayForm = new Tg_HolidayForm();
			tg_HolidayForm.setDateTime(tg_Holiday.getDateTime());
			Tg_Holiday tg_HolidayDate = tg_HolidayDao.findOneByQuery_T(tg_HolidayDao.getQuery(tg_HolidayDao.getBasicHQL(), tg_HolidayForm));
			if (tg_HolidayDate == null) {
				tg_HolidayDao.save(tg_Holiday);
			} else {
				tg_HolidayDate.setType(tg_Holiday.getType());
				tg_HolidayDao.save(tg_HolidayDate);
			}
			long timeStamp = DateUtil.parseDate(date).getTime();
			date = DateUtil.formatDate(new Date(timeStamp + 24 * 60 * 60 * 1000));
		}
	}
}

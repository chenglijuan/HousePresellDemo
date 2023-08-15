package zhishusz.housepresell.initialize;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import zhishusz.housepresell.controller.form.Tg_HolidayForm;
import zhishusz.housepresell.database.dao.Tg_HolidayDao;
import zhishusz.housepresell.database.po.Tg_Holiday;
import zhishusz.housepresell.database.po.state.S_TheState;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

/*
 * Service:假日初始化业务代码
 * */
@Service
@Transactional
public class Tg_HolidayInitService
{
	@Autowired
	private Tg_HolidayDao tg_HolidayDao;
	private static final String StartDate = "2020-01-01";
	private static final String EndDate = "2020-12-31";
	
	public void Step7_Holiday()
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
	
	/**
	 * XSZ by 2019-12-30 16:21:34
	 * 上面的API服务已停止，这个是可以用的
	 */
	public void Step7_Holiday2()
	{
		String date = StartDate;
		while (DateUtil.parse(date).getTime() <= DateUtil.parse(EndDate).getTime()) {
			String result = HttpUtil.get("HTTPS://timor.tech/api/holiday/info/" + date);
			
			Gson gson = new GsonBuilder().create();
//			{"code":0,"type":{"type":2,"name":"元旦","week":3},"holiday":{"holiday":true,"name":"元旦","wage":3,"date":"2020-01-01"}}
			Map<String,Object> tg_HolidayMap = gson.fromJson(result, Map.class);
			Map<String,Object> type_map = (Map<String, Object>) tg_HolidayMap.get("type");
			Double type = (Double) type_map.get("type");
			System.out.println(tg_HolidayMap);
			
			Tg_Holiday tg_Holiday = new Tg_Holiday();
			tg_Holiday.setTableId(Long.valueOf(date.replace("-", "")));
			tg_Holiday.setTheState(S_TheState.Normal);
			tg_Holiday.setBusiState("1");
			tg_Holiday.seteCode(date.replace("-", ""));
			if(0 == type){
				tg_Holiday.setType(0);
			}else{
				tg_Holiday.setType(1);
			}
			tg_Holiday.setDateTime(date);
			
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

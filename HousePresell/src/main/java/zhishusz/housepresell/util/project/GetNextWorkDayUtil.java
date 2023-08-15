package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.Tg_HolidayForm;
import zhishusz.housepresell.database.dao.Tg_HolidayDao;
import zhishusz.housepresell.database.po.Tg_Holiday;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.util.MyDatetime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dechert on 2018/11/29.
 * Company: zhishusz
 * Usage:
 */
@Service
public class GetNextWorkDayUtil {
    @Autowired
    private Tg_HolidayDao holidayDao;
    MyDatetime myDatetime = MyDatetime.getInstance();

    public Long getNextWorkDayEnd(long requestTimeStamp){
        Tg_Holiday nextWorkDay = getNextWorkDay(requestTimeStamp);
        if(nextWorkDay==null){
            return null;
        }
        String dateTime = nextWorkDay.getDateTime();
        long startTimeStamp = myDatetime.stringToLong(dateTime);
        long endTimeStamp = startTimeStamp+myDatetime.getOneDayMs()-1000;
        return endTimeStamp;
    }

    public Long getNextWorkDayStart(long requestTimeStamp){
        Tg_Holiday nextWorkDay = getNextWorkDay(requestTimeStamp);
        if(nextWorkDay==null){
            return null;
        }
        String dateTime = nextWorkDay.getDateTime();
        long startTimeStamp = myDatetime.stringToLong(dateTime);
        return startTimeStamp;
    }

    public Tg_Holiday getNextWorkDay(long requestTimeStamp){
        String dayTimeString = myDatetime.dateToSimpleString(requestTimeStamp);
        Tg_HolidayForm holidayForm = new Tg_HolidayForm();
        holidayForm.setDateTime(dayTimeString);
//        holidayForm.setTheState(S_TheState.Normal);
        holidayForm.setType(0);
        List<Tg_Holiday> workDayList = holidayDao.findByPage(holidayDao.getQuery(holidayDao.getNextWorkDay(), holidayForm));
        if(workDayList.size()>0){

        }else{
            System.out.println("数据库中没有这个日期数据！");
            return null;
        }
        Tg_Holiday nextWorkDay = workDayList.get(0);
        return nextWorkDay;
    }

    public MsgInfo isWorkDay(Long requestTimeStamp) {
        MsgInfo msgInfo = new MsgInfo();
        String dayTimeString = myDatetime.dateToSimpleString(requestTimeStamp);
        Tg_HolidayForm holidayForm = new Tg_HolidayForm();
        holidayForm.setDateTime(dayTimeString);
        List<Tg_Holiday> workDayList = holidayDao.findByPage(holidayDao.getQuery(holidayDao.getNowWorkDay(), holidayForm));
        if (workDayList.size() > 0) {

        } else {
            msgInfo.setSuccess(false);
            msgInfo.setInfo("数据库中没有这个日期数据！");
            return msgInfo;
        }
        Tg_Holiday tg_holiday = workDayList.get(0);
        Integer type = tg_holiday.getType();
        if (type.equals(0)) {
            msgInfo.setSuccess(true);
            msgInfo.setExtra(true);
            //            return true;
        } else {
            msgInfo.setSuccess(true);
            msgInfo.setExtra(false);
            //            return false;
        }
        return msgInfo;
    }
    
    public Long getNextTwoWorkDayEnd(long requestTimeStamp){
        Tg_Holiday nextWorkDay = getNextTwoWorkDay(requestTimeStamp);
        if(nextWorkDay==null){
            return null;
        }
        String dateTime = nextWorkDay.getDateTime();
        long startTimeStamp = myDatetime.stringToLong(dateTime);
        long endTimeStamp = startTimeStamp+myDatetime.getOneDayMs()-1000;
        return endTimeStamp;
    }
    
    public Tg_Holiday getNextTwoWorkDay(long requestTimeStamp){
        String dayTimeString = myDatetime.dateToSimpleString(requestTimeStamp);
        Tg_HolidayForm holidayForm = new Tg_HolidayForm();
        holidayForm.setDateTime(dayTimeString);
        holidayForm.setType(0);
        List<Tg_Holiday> workDayList = holidayDao.findByPage(holidayDao.getQuery(holidayDao.getNextWorkDay(), holidayForm));
        if(workDayList.size()>0){

        }else{
            System.out.println("数据库中没有这个日期数据！");
            return null;
        }
        Tg_Holiday nextTwoWorkDay = workDayList.get(1);
        return nextTwoWorkDay;
    }
}


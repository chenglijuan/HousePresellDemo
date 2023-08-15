package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_ExpForecastForm;
import zhishusz.housepresell.controller.form.Tg_IncomeExpDepositForecastForm;
import zhishusz.housepresell.controller.form.Tg_IncomeForecastForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Tg_IncomeExpDepositForecastDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tg_ExpForecast;
import zhishusz.housepresell.database.po.Tg_IncomeExpDepositForecast;
import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_TimeType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyBoolean;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.GetNextWorkDayUtil;

/*
 * Service列表查询：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeExpDepositForecastListService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Tg_IncomeExpDepositForecastDao tg_IncomeExpDepositForecastDao;

	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	@Autowired
	private Tg_IncomeForecastListService tg_IncomeForecastListService;
	@Autowired
	private Tg_ExpForecastListService tg_ExpForecastListService;

	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;

	@Autowired
	private GetNextWorkDayUtil getNextWorkDayUtil;

	@SuppressWarnings({"unchecked", "rawtypes", "static-access", "unused"})
	public Properties execute(Tg_IncomeExpDepositForecastForm model)
	{
		Properties properties = new MyProperties();

		if (model.getStartTimeStr() == null || model.getStartTimeStr().length() == 0 ||
			model.getEndTimeStr() == null || model.getEndTimeStr().length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择时间");
		}

		Long startTimeStamp = myDatetime.stringToLong(model.getStartTimeStr());
		Long endTimeStamp =   myDatetime.stringToLong(model.getEndTimeStr());

		Long count = (endTimeStamp - startTimeStamp) / S_TimeType.Day;

		Integer configurationTime = 10;
		/**
		 * 获取预测时间段配置
		 */
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("61", "210103");
		if(sm_BaseParameter != null)
		{
			configurationTime = MyInteger.getInstance().parse(sm_BaseParameter.getTheName());
		}
//
//		if (count > configurationTime)
//		{
//			return MyBackInfo.fail(properties, "预测的时间不能超过"+ configurationTime +"天");
//		}

		/**
		 * 收入预测取数
		 */
		Tg_IncomeForecastForm tg_IncomeForecastForm = new Tg_IncomeForecastForm();
		tg_IncomeForecastForm.setStartTimeStr(model.getStartTimeStr());
		tg_IncomeForecastForm.setEndTimeStr(model.getEndTimeStr());
//		tg_IncomeForecastForm.setConfigurationTime(configurationTime);
		Properties incomeProperties = tg_IncomeForecastListService.execute(tg_IncomeForecastForm);
		List<Tg_IncomeForecast> incomeForecastList = (List)(incomeProperties.get("tg_IncomeForecastList"));
		/**
		 * 支出预测取数
		 */
		Tg_ExpForecastForm tg_ExpForecastForm = new Tg_ExpForecastForm();
		tg_ExpForecastForm.setStartTimeStr(model.getStartTimeStr());
		tg_ExpForecastForm.setEndTimeStr(model.getEndTimeStr());
//		tg_ExpForecastForm.setConfigurationTime(configurationTime);
		Properties expProperties = tg_ExpForecastListService.execute(tg_ExpForecastForm);
		List<Tg_ExpForecast> expForecastList = (List)(expProperties.get("tg_ExpForecastList"));

		/**
		 * 开始
		 */

		Double lastDaySurplus = 0.0;
		Double  lastDaySur = 0.0;

		Integer theWorkDayIndex = 0;
		
		Double incomeTotal = 0.0;

		for (int i = 0; i <= count; i += 1)
		{

			//先看看工作日表有没有维护
			MsgInfo msgInfo = getNextWorkDayUtil.isWorkDay(startTimeStamp + i * S_TimeType.Day);
			if (!msgInfo.isSuccess())
			{
				return MyBackInfo.fail(properties, "数据库中没有维护" + myDatetime.dateToSimpleString(startTimeStamp + i * S_TimeType.Day) + "这个日期");
			}
			else
			{
				if (!MyBoolean.getInstance().parse(msgInfo.getExtra()))
				{
					//判断时间戳是不是 工作日
					continue;
				}
			}

			Tg_IncomeExpDepositForecastForm theForm2 = new Tg_IncomeExpDepositForecastForm();
			theForm2.setTheState(S_TheState.Normal);
			theForm2.setTheDay(startTimeStamp + i * S_TimeType.Day);

			List<Tg_IncomeExpDepositForecast> incomeExpDepositForecastList = tg_IncomeExpDepositForecastDao.findByPage(tg_IncomeExpDepositForecastDao.getQuery(tg_IncomeExpDepositForecastDao.getBasicHQL(), theForm2), null, null);

			Tg_IncomeExpDepositForecast incomeExpDepositForecast;
			if (incomeExpDepositForecastList.size() == 1)
			{
				incomeExpDepositForecast = incomeExpDepositForecastList.get(0);
			}
			else
			{
				if (incomeExpDepositForecastList.size() > 1)
				{
					for (Tg_IncomeExpDepositForecast thePo: incomeExpDepositForecastList)
					{
						thePo.setTheState(S_TheState.Deleted);
						tg_IncomeExpDepositForecastDao.save(thePo);
					}
				}
				incomeExpDepositForecast = new Tg_IncomeExpDepositForecast();
			}

			/**
			 * 活期结余 取数
			 */
			if ( theWorkDayIndex == 0 ) //第一天的活期结余从 托管账号中获取
			{
				Tgxy_BankAccountEscrowedForm theBaeForm = new Tgxy_BankAccountEscrowedForm();
				theBaeForm.setTheState(S_TheState.Normal);
				theBaeForm.setIsUsing(0);

				lastDaySurplus = MyDouble.getInstance().parse(tgxy_BankAccountEscrowedDao.findByPage_DoubleSum(tgxy_BankAccountEscrowedDao.getQuery_Sum(tgxy_BankAccountEscrowedDao.getBasicHQL(), "currentBalance", theBaeForm)));
				lastDaySur = lastDaySurplus;
			}

			/**
			 * 活期结余 存值
			 */
			incomeExpDepositForecast.setLastDaySurplus(lastDaySur);

			/**
			 * 收入预计 取数
			 */
			Double incomeTotalSum;
		    Tg_IncomeForecast incomeForecast = incomeForecastList.get(theWorkDayIndex);
            incomeTotalSum = ((incomeForecast.getIncomeTrendForecast() == null) ? 0 : incomeForecast.getIncomeTrendForecast())
                    + ((incomeForecast.getFixedExpire() == null) ? 0 : incomeForecast.getFixedExpire())
                    + ((incomeForecast.getBankLending() == null) ? 0 : incomeForecast.getBankLending())
                    + ((incomeForecast.getIncomeForecast1() == null) ? 0 : incomeForecast.getIncomeForecast1())
                    + ((incomeForecast.getIncomeForecast2() == null) ? 0 : incomeForecast.getIncomeForecast2())
                    + ((incomeForecast.getIncomeForecast3() == null) ? 0 : incomeForecast.getIncomeForecast3());
			
            incomeTotal += incomeTotalSum;
			incomeExpDepositForecast.setIncomeTotal(incomeTotal);

			/**
			 * 支出预计 取数
			 */
			Tg_ExpForecast expForecast = expForecastList.get(theWorkDayIndex);
			/*Double expTotal = ((expForecast.getPayTrendForecast() == null) ? 0 : expForecast.getPayTrendForecast())
					+ ((expForecast.getApplyAmount() == null) ? 0 : expForecast.getApplyAmount())
					+ ((expForecast.getPayableFund() == null) ? 0 : expForecast.getPayableFund())
					+ ((expForecast.getNodeChangePayForecast() == null) ? 0 : expForecast.getNodeChangePayForecast())
					+ ((expForecast.getHandlingFixedDeposit() == null) ? 0 : expForecast.getHandlingFixedDeposit())
					+ ((expForecast.getPayForecast1() == null) ? 0 : expForecast.getPayForecast1())
					+ ((expForecast.getPayForecast2() == null) ? 0 : expForecast.getPayForecast2())
					+ ((expForecast.getPayForecast3() == null) ? 0 : expForecast.getPayForecast3());*/
			
			Double expTotal = (expForecast.getNodeChangePayForecast() == null) ? 0 : expForecast.getNodeChangePayForecast();
			incomeExpDepositForecast.setExpTotal(expTotal);

			/**
			 * 本日活期结余 计算存值
			 */
//			Double todaySurplus = lastDaySurplus + incomeTotal - expTotal;
			Double todaySurplus = lastDaySur + incomeTotal - expTotal;
			incomeExpDepositForecast.setTodaySurplus(todaySurplus);

			/**
			 * 托管余额参考值 从Sm_BaseParameter取
			 */
			Double collocationReference = 0.0;
			/*Sm_BaseParameter sm_BaseParameter2 = sm_BaseParameterGetService.getParameter("62", "210103");
			if(sm_BaseParameter != null)
			{
				collocationReference = MyDouble.getInstance().parse(sm_BaseParameter2.getTheName());
			}*/
//			incomeExpDepositForecast.setCollocationReference(collocationReference);
			
			collocationReference = null == incomeExpDepositForecast.getCollocationReference() ? 0.00 : incomeExpDepositForecast.getCollocationReference();
			incomeExpDepositForecast.setCollocationReference(collocationReference);

			/**
			 * 扣减参考值后的托管余额
			 */
			Double collocationBalance = todaySurplus - collocationReference;
			incomeExpDepositForecast.setCollocationBalance(todaySurplus);

			/**
			 * 更新值结束 保存
			 */
			incomeExpDepositForecast.setTheDay(startTimeStamp + i * S_TimeType.Day);
			incomeExpDepositForecast.setTheWeek(myDatetime.dateToWeek(myDatetime.dateToSimpleString(incomeExpDepositForecast.getTheDay())));
			incomeExpDepositForecast.setTheState(S_TheState.Normal);
			tg_IncomeExpDepositForecastDao.save(incomeExpDepositForecast);

			/**
			 * 把本日活期结余赋值给上日活期结余
			 */
			lastDaySurplus = todaySurplus;

			theWorkDayIndex += 1;
		}

		/**
		 * 取值
		 */
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		model.setStartTimeStamp(startTimeStamp);
		model.setEndTimeStamp(endTimeStamp);
		model.setTheState(S_TheState.Normal);

		Integer totalCount = tg_IncomeExpDepositForecastDao.findByPage_Size(tg_IncomeExpDepositForecastDao.getQuery_Size(tg_IncomeExpDepositForecastDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_IncomeExpDepositForecast> tg_IncomeExpDepositForecastList;
		if(totalCount > 0)
		{
			tg_IncomeExpDepositForecastList = tg_IncomeExpDepositForecastDao.findByPage(tg_IncomeExpDepositForecastDao.getQuery(tg_IncomeExpDepositForecastDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tg_IncomeExpDepositForecastList = new ArrayList<>();
		}
		
		for (Tg_IncomeExpDepositForecast tg_IncomeExpDepositForecast : tg_IncomeExpDepositForecastList) {
		    
		    if(tg_IncomeExpDepositForecast.getExpTotal() < 0){
		        tg_IncomeExpDepositForecast.setExpTotal(0.00);
		    }
		    
        }
		
		
		
		properties.put("tg_IncomeExpDepositForecastList", tg_IncomeExpDepositForecastList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

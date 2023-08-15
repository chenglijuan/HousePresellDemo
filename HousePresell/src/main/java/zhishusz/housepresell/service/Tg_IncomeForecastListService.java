package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.controller.form.Tg_IncomeForecastForm;
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tg_IncomeForecastDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tg_IncomeForecast;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_DepositPropertyType;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_TimeType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyBoolean;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.MyLong;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.GetNextWorkDayUtil;

/*
 * Service列表查询：收入预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_IncomeForecastListService
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Tg_IncomeForecastDao tg_IncomeForecastDao;

	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;

	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;

	@Autowired
	private GetNextWorkDayUtil getNextWorkDayUtil;

	@SuppressWarnings({ "unchecked", "static-access" })
	public Properties execute(Tg_IncomeForecastForm model)
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
		if (model.getConfigurationTime() != null && model.getConfigurationTime() > 0)
		{
			configurationTime = model.getConfigurationTime();
		}
		else
		{
			Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("61", "210101");
			if(sm_BaseParameter != null)
			{
				configurationTime = MyInteger.getInstance().parse(sm_BaseParameter.getTheName());
			}
		}

//		if (count > configurationTime)
//		{
//			return MyBackInfo.fail(properties, "预测的时间不能超过"+ configurationTime +"天");
//		}

		Double forecastTotal = 0.0;
		List<Double> theForecastList = new ArrayList<>();

		Integer theWorkDayIndex = 0;

		for (Integer i = 0; i <= count; i += 1)
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

			Tg_IncomeForecastForm theForm2 = new Tg_IncomeForecastForm();
			theForm2.setTheState(S_TheState.Normal);
			theForm2.setTheDay(startTimeStamp + i * S_TimeType.Day);
			List<Tg_IncomeForecast> incomeForecastList = tg_IncomeForecastDao.findByPage(tg_IncomeForecastDao.getQuery(tg_IncomeForecastDao.getBasicHQL(), theForm2), null, null);

			Tg_IncomeForecast incomeForecast;
			if (incomeForecastList.size() == 1)
			{
				incomeForecast = incomeForecastList.get(0);
			}
			else
			{
				if (incomeForecastList.size() > 1)
				{
					for (Tg_IncomeForecast thePo: incomeForecastList)
					{
						thePo.setTheState(S_TheState.Deleted);
						tg_IncomeForecastDao.save(thePo);
					}
				}
				incomeForecast = new Tg_IncomeForecast();
			}

			/**
			 *  入账资金趋势预测 取数
			 */
			Double incomeAvg = 0.0;
			if (theWorkDayIndex < configurationTime)
			{
				Double incomeDou = tgpf_DayEndBalancingDao.getDayEndBalanceSum(configurationTime - theWorkDayIndex);
				incomeAvg = (forecastTotal + incomeDou) / configurationTime;
			}
			else
			{
				/**
				 *  超过 configurationTime 就只从本表中取数据 ———— 然而有一个 forecastTotal & theForecastList 在记录，可直接获取
				 */
				incomeAvg = forecastTotal / configurationTime;

				//但是预测每往后一天，就要删除最前面的一天的预测值
				//获取下index
				Integer theIndex = theWorkDayIndex - configurationTime;
				Double theFirstIncomeAvg = theForecastList.get(theIndex);

				//删除最前的一个
				forecastTotal -= theFirstIncomeAvg;
			}
//			incomeAvg = tgpf_DayEndBalancingDao.getDayEndBalanceSum(startTimeStamp + i * S_TimeType.Day);
			
			/**
			 *  塞入 入账资金趋势预测
			 */
			incomeForecast.setIncomeTrendForecast(incomeAvg);
			//记录单个的预测值 和 整体 的值
			theForecastList.add(incomeAvg);
			forecastTotal += incomeAvg;


			/**
			 *  大额存单 定期到期 取数
			 */
			Tg_DepositManagementForm theDepForm = new Tg_DepositManagementForm();
			theDepForm.setTheState(S_TheState.Normal);
			theDepForm.setBusiState("已备案");
			theDepForm.setDepositState(S_DepositState.Deposit); //存单存入，还未提取的存单
			theDepForm.setStopDate(startTimeStamp + i * S_TimeType.Day);
// 			theDepForm.setDepositProperty(S_DepositPropertyType.CertificateOfDeposit); //大额存单
			/**
			 *  大额存单 定期到期 取数 -- 之前有没有提取的大额存单算到今天
			 */
			Double depositDou;
			Double expectedInterest;
			if (theWorkDayIndex == 0)  //i equals 0 说明是今天
			{
				depositDou = MyDouble.getInstance().parse(tg_DepositManagementDao.findByPage_DoubleSum(tg_DepositManagementDao.getQuery_Sum(tg_DepositManagementDao.getSumHQL2(), "principalAmount", theDepForm)));
			
				expectedInterest = MyDouble.getInstance().parse(tg_DepositManagementDao.findByPage_DoubleSum(tg_DepositManagementDao.getQuery_Sum(tg_DepositManagementDao.getSumHQL2(), "expectedInterest", theDepForm)));
			}
			else
			{
				
				if(1 == myDatetime.dateToWeek(myDatetime.dateToSimpleString(startTimeStamp + i * S_TimeType.Day))){
					theDepForm.setStartDate(startTimeStamp + (i - 2) * S_TimeType.Day);
//					depositDou = MyDouble.getInstance().parse(tg_DepositManagementDao.findByPage_DoubleSum(tg_DepositManagementDao.getQuery_Sum(tg_DepositManagementDao.getSumHQL3(), "principalAmount", theDepForm)));
					depositDou = tg_DepositManagementDao.getDayEndBalanceSum(startTimeStamp + (i - 2) * S_TimeType.Day, startTimeStamp + i * S_TimeType.Day, "1");
				}else{
//					depositDou = MyDouble.getInstance().parse(tg_DepositManagementDao.findByPage_DoubleSum(tg_DepositManagementDao.getQuery_Sum(tg_DepositManagementDao.getSumHQL(), "principalAmount", theDepForm)));
				    depositDou = tg_DepositManagementDao.getDayEndBalanceSum(0L, startTimeStamp + i * S_TimeType.Day, "0");
				}
				expectedInterest = MyDouble.getInstance().parse(tg_DepositManagementDao.findByPage_DoubleSum(tg_DepositManagementDao.getQuery_Sum(tg_DepositManagementDao.getSumHQL(), "expectedInterest", theDepForm)));
			}
			if (depositDou == null)
			{
				depositDou = 0.0;
			}
			
			if (expectedInterest == null){
				expectedInterest = 0.0;
			}
			/**
			 *  塞入 定期到期
			 *  本金 + 利息
			 */
//			incomeForecast.setFixedExpire(depositDou + expectedInterest);
			
			/**
			 *  收支存预测调整
             *  塞入 定期到期
             *  本金
             */
            incomeForecast.setFixedExpire(depositDou);

			incomeForecast.setTheDay(startTimeStamp + i * S_TimeType.Day);
			incomeForecast.setTheWeek(myDatetime.dateToWeek(myDatetime.dateToSimpleString(incomeForecast.getTheDay())));
			incomeForecast.setTheState(S_TheState.Normal);
//
//			Long theDay = incomeForecast.getTheDay();

			tg_IncomeForecastDao.save(incomeForecast);

			//工作日自增
			theWorkDayIndex += 1;
		}

		/**
		 * 取值
		 */
		model.setStartTimeStamp(startTimeStamp);
		model.setEndTimeStamp(endTimeStamp);
		model.setTheState(S_TheState.Normal);

		Integer totalCount = tg_IncomeForecastDao.findByPage_Size(tg_IncomeForecastDao.getQuery_Size(tg_IncomeForecastDao.getBasicHQL(), model));
		
		List<Tg_IncomeForecast> tg_IncomeForecastList;
		if(totalCount > 0)
		{
			tg_IncomeForecastList = tg_IncomeForecastDao.findByPage(tg_IncomeForecastDao.getQuery(tg_IncomeForecastDao.getBasicHQL(), model), null, null);
		}
		else
		{
			tg_IncomeForecastList = new ArrayList<Tg_IncomeForecast>();
		}
		
		properties.put("tg_IncomeForecastList", tg_IncomeForecastList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

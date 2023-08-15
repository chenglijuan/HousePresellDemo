package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_DTLForm;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.controller.form.Tg_ExpForecastForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_DTLDao;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tg_ExpForecastDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_DTL;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tg_ExpForecast;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
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
 * Service列表查询：支出预测 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_ExpForecastListService {
    private MyDatetime myDatetime = MyDatetime.getInstance();

    @Autowired
    private Tg_ExpForecastDao tg_ExpForecastDao;
    @Autowired
    private Empj_PaymentBondDao empj_PaymentBondDao;
    @Autowired
    private Tg_DepositManagementDao tg_DepositManagementDao;

    @Autowired
    private Empj_ProjProgInspection_DTLDao empj_ProjProgInspection_DTLDao;
    @Autowired
    private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;

    @Autowired
    private Sm_BaseParameterGetService sm_BaseParameterGetService;

    @Autowired
    private GetNextWorkDayUtil getNextWorkDayUtil;

    @SuppressWarnings("unchecked")
    public Properties execute(Tg_ExpForecastForm model) {
        Properties properties = new MyProperties();

        if (model.getStartTimeStr() == null || model.getStartTimeStr().length() == 0 || model.getEndTimeStr() == null
            || model.getEndTimeStr().length() == 0) {
            return MyBackInfo.fail(properties, "请选择时间");
        }

        Long startTimeStamp = myDatetime.stringToLong(model.getStartTimeStr());
        Long endTimeStamp = myDatetime.stringToLong(model.getEndTimeStr());

        Long count = (endTimeStamp - startTimeStamp) / S_TimeType.Day;

        Integer configurationTime = 10;

        /**
         * 获取预测时间段配置
         */
        if (model.getConfigurationTime() != null && model.getConfigurationTime() > 0) {
            configurationTime = model.getConfigurationTime();
        } else {
            Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("61", "210101");
            if (sm_BaseParameter != null) {
                configurationTime = MyInteger.getInstance().parse(sm_BaseParameter.getTheName());
            }
        }

        // if (count > configurationTime)
        // {
        // return MyBackInfo.fail(properties, "预测的时间不能超过"+ configurationTime +"天");
        // }

        Double forecastTotal = 0.0;
        List<Double> theForecastList = new ArrayList<>();

        Integer theWorkDayIndex = 0;

        for (int i = 0; i <= count; i += 1) {
            // 先判断时间戳是不是 工作日

            // 先看看工作日表有没有维护
            MsgInfo msgInfo = getNextWorkDayUtil.isWorkDay(startTimeStamp + i * S_TimeType.Day);
            if (!msgInfo.isSuccess()) {
                return MyBackInfo.fail(properties,
                    "数据库中没有维护" + myDatetime.dateToSimpleString(startTimeStamp + i * S_TimeType.Day) + "这个日期");
            } else {
                if (!MyBoolean.getInstance().parse(msgInfo.getExtra())) {
                    // 判断时间戳是不是 工作日
                    continue;
                }
            }

            Tg_ExpForecastForm theForm2 = new Tg_ExpForecastForm();
            theForm2.setTheState(S_TheState.Normal);
            theForm2.setTheDay(startTimeStamp + i * S_TimeType.Day);

            List<Tg_ExpForecast> expForecastList = tg_ExpForecastDao
                .findByPage(tg_ExpForecastDao.getQuery(tg_ExpForecastDao.getBasicHQL(), theForm2), null, null);

            Tg_ExpForecast expForecast;
            if (expForecastList.size() == 1) {
                expForecast = expForecastList.get(0);
            } else {
                if (expForecastList.size() > 1) {
                    for (Tg_ExpForecast thePo : expForecastList) {
                        thePo.setTheState(S_TheState.Deleted);
                        tg_ExpForecastDao.save(thePo);
                    }
                }
                expForecast = new Tg_ExpForecast();
            }

            /**
             * 支出资金趋势预测 取数
             */
            /*
            Double expAvg = 0.0;
            if (theWorkDayIndex < configurationTime)
            {
            Double expDou = tg_ExpForecastDao.getPayTrendForecastSum(configurationTime - theWorkDayIndex);
            expAvg = (forecastTotal + expDou) / configurationTime;
            }
            else
            {
            *//**
               * 超过 configurationTime 就只从本表中取数据 ———— 然而有一个 forecastTotal & theForecastList 在记录，可直接获取
               */
            /*
            expAvg = forecastTotal / configurationTime;
            
            //但是预测每往后一天，就要删除最前面的一天的预测值
            //获取下index
            Integer theIndex = theWorkDayIndex - configurationTime;
            Double theFirstIncomeAvg = theForecastList.get(theIndex);
            
            //删除最前的一个
            forecastTotal -= theFirstIncomeAvg;
            }
            *//**
               * 塞入 支出资金趋势预测
               *//*
                expForecast.setPayTrendForecast(expAvg);*/

            /**
             * 支出资金趋势预测 取保函预测
             */
            Double expAvg = 0.0;
            if (theWorkDayIndex == 0) {

                Empj_PaymentBondForm bondForm = new Empj_PaymentBondForm();
                bondForm.setTheState(S_TheState.Normal);
                bondForm.setApprovalState("审核中");
                expAvg = MyDouble.getInstance().parse(empj_PaymentBondDao.findByPage_DoubleSum(empj_PaymentBondDao
                    .getQuery_Sum(empj_PaymentBondDao.getSumBasicHQL(), "guaranteedSumAmount", bondForm)));
                
                if(null == expAvg){
                    expAvg = 0.00;
                }
            } else {
            }
            /**
             * 塞入 支出资金趋势预测 取保函预测
             */
            expForecast.setPayTrendForecast(expAvg);

            // 记录单个的预测值 和 整体 的值
            theForecastList.add(expAvg);
            forecastTotal += expAvg;

            /**
             * 拨付资金预测 取数 ————只有当天的这个字段会存值
             */
            if (theWorkDayIndex == 0) {
                /**
                 * 塞入 拨付资金预测
                 */
                Double applySum = tg_ExpForecastDao.getApplyAmountSum();
                expForecast.setApplyAmount(applySum);
            } else {
                expForecast.setApplyAmount(null);
            }

            /**
             * 可拨付金额预测 取数 ———— 开发企业已经溢出尚未申请拨付金额 ———— 只有当天的这个字段会存值
             */
            if (theWorkDayIndex == 0) {
                /**
                 * 塞入 拨付资金预测
                 */
                Double payableFundSum = tg_ExpForecastDao.getPayableFundSum();
                expForecast.setPayableFund(payableFundSum);
            } else {
                expForecast.setPayableFund(null);
            }

            /**
             * 节点变更拨付
             * 
             * 2020-9-21 14:06:55--取值调整 根据项目进度巡查中的节点预测数据，在对应的节点变更日期进行测算，对流程中（审核中状态）的交付备案中的托管余额数据求和，在查询当天的日期展示。
             * 
             */
            /*Double pjdSum = tg_ExpForecastDao.getNodeChangePayForecastSum(startTimeStamp + i * S_TimeType.Day);
            expForecast.setNodeChangePayForecast(pjdSum);*/

            Double pjdSum = 0.00;

            Tgpj_BuildingAccount buildingAccount;
            Double currentEscrowFund;

            /*Empj_ProjProgInspection_DTLForm dtlModel = new Empj_ProjProgInspection_DTLForm();
            dtlModel.setTheState(S_TheState.Normal);
            dtlModel.setForecastCompleteDate(myDatetime.dateToSimpleString(startTimeStamp + i * S_TimeType.Day));
            List<Empj_ProjProgInspection_DTL> dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));

            Tgpj_BldLimitAmountVer_AFDtl forecastNode;
            Double orgLimitedAmount;
            Map<String, Object> listMap;
            List<Map<String, Object>> listVersion;
            Map<String, Object> versionMap;
            Double beforelimitAmount = 0.00;
            for (Empj_ProjProgInspection_DTL empj_ProjProgInspection_DTL : dtlList) {
                forecastNode = empj_ProjProgInspection_DTL.getForecastNode();
                if (null != forecastNode && forecastNode.getLimitedAmount() != 100.00
                    && forecastNode.getLimitedAmount() != 0.00) {

                    buildingAccount = empj_ProjProgInspection_DTL.getBuildInfo().getBuildingAccount();
                    orgLimitedAmount = buildingAccount.getOrgLimitedAmount();

                    try {
                        listMap = empj_ProjProgInspection_DTLDao.getBldLimitAmountVerAfDtl(
                            empj_ProjProgInspection_DTL.getBuildInfo().getTableId(),
                            String.valueOf(buildingAccount.getCurrentLimitedRatio()));
                        listVersion = new ArrayList<>();
                        listVersion = (List<Map<String, Object>>)listMap.get("versionList");
                        if(!listVersion.isEmpty()){
                            versionMap = listVersion.get(listVersion.size() - 1);
                            if(null != versionMap && null != versionMap.get("limitedAmount")){
                                
                                beforelimitAmount = Double.parseDouble((String)versionMap.get("limitedAmount"));
                                pjdSum += orgLimitedAmount * (forecastNode.getLimitedAmount() - beforelimitAmount) / 100;
                            }
                        }
                        
                    } catch (SQLException e) {

                    }

                }
            }*/
            
            //以上逻辑代码中实现耗时，改用存储过程数据库计算
            pjdSum = tg_ExpForecastDao.getNodeChangePayForecastSum(myDatetime.dateToSimpleString(startTimeStamp + i * S_TimeType.Day));

            /*if (theWorkDayIndex == 0) {
                Empj_BldEscrowCompletedForm bldEscrowCompletedModel = new Empj_BldEscrowCompletedForm();
                bldEscrowCompletedModel.setTheState(S_TheState.Normal);
                bldEscrowCompletedModel.setApprovalState("审核中");
                bldEscrowCompletedModel.setBusiState("未备案");
                List<Empj_BldEscrowCompleted> empj_BldEscrowCompletedList =
                    empj_BldEscrowCompletedDao.findByPage(empj_BldEscrowCompletedDao
                        .getQuery(empj_BldEscrowCompletedDao.getBasicHQL(), bldEscrowCompletedModel));

                List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList;
                for (Empj_BldEscrowCompleted empj_BldEscrowCompleted : empj_BldEscrowCompletedList) {

                    empj_BldEscrowCompleted_DtlList = empj_BldEscrowCompleted.getEmpj_BldEscrowCompleted_DtlList();
                    for (Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl : empj_BldEscrowCompleted_DtlList) {
                        buildingAccount = empj_BldEscrowCompleted_Dtl.getBuilding().getBuildingAccount();
                        currentEscrowFund = null == buildingAccount.getCurrentEscrowFund() ? 0.00
                            : buildingAccount.getCurrentEscrowFund();

                        pjdSum += currentEscrowFund;
                    }
                }
            }*/

            expForecast.setNodeChangePayForecast(pjdSum);

            /**
             * 正在办理存单金额预测 取数 ———— 开发企业已经溢出尚未申请拨付金额 ———— 只有当天的这个字段会存值
             * 
             * 2020-9-21 11:26:34 --取值调整 正在办理中的定期存款，取存单正在办理中的本金金额，根据办理日期，汇总到指定的预测日期中去。
             */
            Tg_DepositManagementForm depositManagementModel = new Tg_DepositManagementForm();
            depositManagementModel.setTheState(S_TheState.Normal);
            depositManagementModel.setStartDate(startTimeStamp + i * S_TimeType.Day);
            depositManagementModel.setDepositState("03");
            Double handlingFixedDeposit =
                MyDouble.getInstance().parse(tg_DepositManagementDao.findByPage_DoubleSum(tg_DepositManagementDao
                    .getQuery_Sum(tg_DepositManagementDao.getSumHQL(), "principalAmount", depositManagementModel)));
            expForecast.setHandlingFixedDeposit(handlingFixedDeposit);

            expForecast.setTheDay(startTimeStamp + i * S_TimeType.Day);
            expForecast.setTheWeek(myDatetime.dateToWeek(myDatetime.dateToSimpleString(expForecast.getTheDay())));
            expForecast.setTheState(S_TheState.Normal);
            tg_ExpForecastDao.save(expForecast);

            // 工作日自增
            theWorkDayIndex += 1;
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

        model.setStartTimeStamp(startTimeStamp);
        model.setEndTimeStamp(endTimeStamp);
        model.setTheState(S_TheState.Normal);

        Integer totalCount =
            tg_ExpForecastDao.findByPage_Size(tg_ExpForecastDao.getQuery_Size(tg_ExpForecastDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Tg_ExpForecast> tg_ExpForecastList;
        if (totalCount > 0) {
            tg_ExpForecastList = tg_ExpForecastDao.findByPage(
                tg_ExpForecastDao.getQuery(tg_ExpForecastDao.getBasicHQL(), model), null, null);
        } else {
            tg_ExpForecastList = new ArrayList<Tg_ExpForecast>();
        }

        properties.put("tg_ExpForecastList", tg_ExpForecastList);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}

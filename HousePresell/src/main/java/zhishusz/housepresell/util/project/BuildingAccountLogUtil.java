package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLogCalculateService;
import zhishusz.housepresell.util.MyBackInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Created by Dechert on 2018-11-07.
 * Company: zhishusz
 */
@Service
public class BuildingAccountLogUtil {
    @Autowired
    private Tgpj_BuildingAccountLogCalculateService calculateService;
    @Autowired
    private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
    @Autowired
    private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;

    /**
     * 初始化BuildingAccountLog的方法
     *
     * @param buildingInfo 需要修改的楼幢
     * @param tableId      主要po的tableId
     * @param busiCode     当前执行的busiCode
     * @return 初始化好的BuildingAccountLog
     */
    public Tgpj_BuildingAccountLog getInitBuildingAccountLog(Empj_BuildingInfo buildingInfo, Long tableId, String busiCode) {
        if (buildingInfo == null) {
            return null;
        }
        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        if (buildingAccount == null) {
            return null;
        }
        Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
        tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
        tgpj_BuildingAccountLog.setBusiState("0");
        tgpj_BuildingAccountLog.seteCode(buildingAccount.geteCode());
        tgpj_BuildingAccountLog.setUserStart(buildingAccount.getUserStart());
        tgpj_BuildingAccountLog.setCreateTimeStamp(buildingAccount.getCreateTimeStamp());
        tgpj_BuildingAccountLog.setUserUpdate(buildingAccount.getUserUpdate());
        tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpj_BuildingAccountLog.setUserRecord(buildingAccount.getUserRecord());
        tgpj_BuildingAccountLog.setRecordTimeStamp(buildingAccount.getRecordTimeStamp());
        tgpj_BuildingAccountLog.setDevelopCompany(buildingAccount.getDevelopCompany());
        tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(buildingAccount.geteCodeOfDevelopCompany());
        tgpj_BuildingAccountLog.setProject(buildingAccount.getProject());
        tgpj_BuildingAccountLog.setTheNameOfProject(buildingAccount.getTheNameOfProject());
        tgpj_BuildingAccountLog.setBuilding(buildingInfo);
        tgpj_BuildingAccountLog.setPayment(buildingAccount.getPayment());
        tgpj_BuildingAccountLog.seteCodeOfBuilding(buildingAccount.geteCodeOfBuilding());
        tgpj_BuildingAccountLog.setEscrowStandard(buildingAccount.getEscrowStandard());
        tgpj_BuildingAccountLog.setEscrowArea(buildingAccount.getEscrowArea());
        tgpj_BuildingAccountLog.setBuildingArea(buildingAccount.getBuildingArea());
        tgpj_BuildingAccountLog.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
        tgpj_BuildingAccountLog.setCurrentFigureProgress(buildingAccount.getCurrentFigureProgress());
        tgpj_BuildingAccountLog.setCurrentLimitedRatio(buildingAccount.getCurrentLimitedRatio());
        tgpj_BuildingAccountLog.setNodeLimitedAmount(buildingAccount.getNodeLimitedAmount());
        tgpj_BuildingAccountLog.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
        tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
        tgpj_BuildingAccountLog.setTotalAccountAmount(buildingAccount.getTotalAccountAmount());
        tgpj_BuildingAccountLog.setPayoutAmount(buildingAccount.getPayoutAmount());
        tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(buildingAccount.getAppliedNoPayoutAmount());
        tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(buildingAccount.getApplyRefundPayoutAmount());
        tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
        tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
        tgpj_BuildingAccountLog.setAppropriateFrozenAmount(buildingAccount.getAppropriateFrozenAmount());
        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
        tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
        tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
        tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
        tgpj_BuildingAccountLog.setRelatedBusiCode(busiCode);
        tgpj_BuildingAccountLog.setRelatedBusiTableId(tableId);
        tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
        tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
        tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
        tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
        tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
        tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
        tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
        tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
        tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
        tgpj_BuildingAccountLog.setAllocableAmount(buildingAccount.getAllocableAmount());
        tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(buildingAccount.getBldLimitAmountVerDtl());

        return tgpj_BuildingAccountLog;
    }

    /**
     * 对Tgpj_BuildingAccountLog改变有需要改变的值，也就是set有变动的值，然后计算保存Log
     *
     * @param buildingInfo
     * @param tableId
     * @param busiCode
     * @param setChange
     */
    public void changeAndCaculate(Empj_BuildingInfo buildingInfo, Long tableId, String busiCode, SetChange setChange) {
        Tgpj_BuildingAccountLog initBuildingAccountLog = getInitBuildingAccountLog(buildingInfo, tableId, busiCode);
        setChange.onSetChange(initBuildingAccountLog);
        calculateService.execute(initBuildingAccountLog);
    }

    /**
     * 审批回调中执行，计算并更新账户表
     *
     * @param buildingInfo
     * @param properties
     * @param tableId
     * @param busiCode
     * @param setChange
     * @return
     */
    public Properties callBackChange(Empj_BuildingInfo buildingInfo, Properties properties, Long tableId, String busiCode, SetChangeWithLogForm setChange) {
        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        if (buildingAccount != null) {
            Long accountVersion = buildingAccount.getVersionNo();
            // 根据楼幢账户查询版本号最大的楼幢账户log表
            // 查询条件：1.业务编码 2.楼幢账户 3.关联主键 4.根据版本号大小排序
            Tgpj_BuildingAccountLogForm tgpj_BuildingAccountLogForm = new Tgpj_BuildingAccountLogForm();
            tgpj_BuildingAccountLogForm.setTheState(S_TheState.Normal);
            tgpj_BuildingAccountLogForm.setRelatedBusiCode(busiCode);
            tgpj_BuildingAccountLogForm.setTgpj_BuildingAccount(buildingAccount);
            tgpj_BuildingAccountLogForm.setRelatedBusiTableId(tableId);
            Integer logCount = tgpj_BuildingAccountLogDao.findByPage_Size(tgpj_BuildingAccountLogDao.getQuery_Size(tgpj_BuildingAccountLogDao.getSpecialHQL(), tgpj_BuildingAccountLogForm));

            List<Tgpj_BuildingAccountLog> tgpj_BuildingAccountLogList;
            if (logCount > 0) {
                tgpj_BuildingAccountLogList = tgpj_BuildingAccountLogDao.findByPage(
                        tgpj_BuildingAccountLogDao.getQuery(tgpj_BuildingAccountLogDao.getBasicHQL(),
                                tgpj_BuildingAccountLogForm));

                Tgpj_BuildingAccountLog buildingAccountLog = tgpj_BuildingAccountLogList.get(0);
                // 获取日志表的版本号
                Long logVersionNo = buildingAccountLog.getVersionNo();
                if (accountVersion != null) {

                } else {
                    accountVersion = 1L;
                }
                if (null == buildingAccountLog.getVersionNo() || buildingAccountLog.getVersionNo() < 0) {
                    logVersionNo = 1l;
                }
                if (logVersionNo == accountVersion) {
                    tgpj_BuildingAccountLimitedUpdateService.execute(buildingAccountLog);
                } else if (logVersionNo < accountVersion) {
                    Tgpj_BuildingAccountLog buildingAccountLogNew = getInitBuildingAccountLog(buildingInfo, tableId, busiCode);
                    setChange.onSetChange(buildingAccountLogNew, tgpj_BuildingAccountLogForm);
                    tgpj_BuildingAccountLimitedUpdateService.execute(buildingAccountLogNew);
                } else if (logVersionNo > accountVersion) {
//                    return properties;
                    return MyBackInfo.fail(properties, "备案版本存在回档，请核实后重新发起！");
                }
            }
        }
        return properties;
    }

    /**
     * 更新楼幢账户表，这个是不走审批流的情况
     *
     * @param buildingInfo
     * @param tableId
     * @param busiCode
     * @param setChange
     */
    public void calculateWithoutApproval(Empj_BuildingInfo buildingInfo, Long tableId, String busiCode, SetChange setChange) {
        Tgpj_BuildingAccountLog initBuildingAccountLog = getInitBuildingAccountLog(buildingInfo, tableId, busiCode);
        setChange.onSetChange(initBuildingAccountLog);
        tgpj_BuildingAccountLimitedUpdateService.execute(initBuildingAccountLog);

    }

    public interface SetChange {
        void onSetChange(Tgpj_BuildingAccountLog tgpj_BuildingAccountLog);
    }

    public interface SetChangeWithLogForm {
        void onSetChange(Tgpj_BuildingAccountLog tgpj_BuildingAccountLog, Tgpj_BuildingAccountLogForm accountLogForm);
    }

}

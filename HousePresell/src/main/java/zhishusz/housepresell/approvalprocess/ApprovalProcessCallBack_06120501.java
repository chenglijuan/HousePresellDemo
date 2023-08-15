package zhishusz.housepresell.approvalprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDetailDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentBond;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;

/**
 * 支付保函申请
 * 
 * @author Administrator
 * @date 2020/05/22
 */
@Transactional
public class ApprovalProcessCallBack_06120501 implements IApprovalProcessCallback {
    @Autowired
    private Empj_PaymentBondDao empj_PaymentBondDao;
    @Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;

    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

    @Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
    @Autowired
    private Empj_BuildingAccountSupervisedDao empj_buildingAccountSupervisedDao;
    @Autowired
    private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;// 楼幢账户
    @Autowired
    private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
    @Autowired
    private Tgpf_FundOverallPlanDetailDao tgpf_FundOverallPlanDetailDao;

    @Autowired
    private Gson gson;

    @SuppressWarnings("unchecked")
    @Override
    public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm model) {
        Properties properties = new Properties();

        Long nowDate = System.currentTimeMillis();
        try {
            String workflowEcode = approvalProcessWorkflow.geteCode();

            // 获取正在处理的申请单
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

            // 获取正在处理的申请单所属的流程配置
            Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
            String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

            // 获取正在审批的存单
            Long paymentBondId = sm_ApprovalProcess_AF.getSourceId();
            if (paymentBondId == null || paymentBondId < 1) {
                return MyBackInfo.fail(properties, "审批的存单不存在");
            }
            Empj_PaymentBond paymentBond = empj_PaymentBondDao.findById(paymentBondId);

            if (paymentBond == null) {
                return MyBackInfo.fail(properties, "审批的存单不存在");
            }

            switch (approvalProcessWork) {
                case "06120501001_ZS":
                    if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
                        && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {

                        paymentBond.setApprovalState(S_ApprovalState.Completed);
                        paymentBond.setBusiState("1");
                        /*paymentBond.setUserUpdate(model.getUser());
                        paymentBond.setLastUpdateTimeStamp(nowDate);*/
                        paymentBond.setUserRecord(model.getUser());
                        paymentBond.setRecordTimeStamp(nowDate);

                        /*
                         * 审批结束后推送到用款申请
                         */
                        // 查询子表信息
                        Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
                        childModel.setTheState(S_TheState.Normal);
                        childModel.setEmpj_PaymentBond(paymentBond);
                        List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao.findByPage(
                            empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));

                        Tgpf_FundAppropriated_AF af = new Tgpf_FundAppropriated_AF();
                        af.seteCode(sm_BusinessCodeGetService.execute("06120301"));
                        af.setTheState(S_TheState.Normal);
                        af.setUserStart(paymentBond.getUserStart());
                        af.setUserUpdate(paymentBond.getUserStart());
                        af.setUserRecord(paymentBond.getUserStart());
                        af.setCreateTimeStamp(nowDate);
                        af.setLastUpdateTimeStamp(nowDate);
                        af.setRecordTimeStamp(nowDate);

                        af.setPaymentBondCode(paymentBond.geteCode());
                        af.setPaymentBondId(paymentBond.getTableId());

                        af.setApplyDate(MyDatetime.getInstance().dateToSimpleString(nowDate));

                        af.setDevelopCompany(paymentBond.getCompany());
                        af.setApplyDate(MyDatetime.getInstance().getCurrentDate());
                        af.seteCodeOfDevelopCompany(
                            null == paymentBond.getCompany() ? "" : paymentBond.getCompany().geteCode());
                        af.setProject(paymentBond.getProject());
                        af.setTheNameOfDevelopCompany(
                            null == paymentBond.getCompany() ? "" : paymentBond.getCompany().getTheName());
                        af.setTheNameOfProject(
                            StringUtils.isBlank(paymentBond.getProjectName()) ? "" : paymentBond.getProjectName());
                        af.seteCodeOfProject(
                            null == paymentBond.getProject() ? "" : paymentBond.getProject().geteCode());
                        /*af.setTotalApplyAmount(
                            null == paymentBond.getGuaranteedSumAmount() ? 0.00 : paymentBond.getGuaranteedSumAmount());*/
                        af.setApplyState(2);
                        af.setApprovalState("已完结");
                        af.setApplyType("1");// 保函支付
                        af.setPayState(af.getProject().getPayState());

                        tgpf_FundAppropriated_AFDao.save(af);

                        // 楼幢信息
                        Empj_BuildingInfo empj_BuildingInfo;
                        // 楼幢账户信息
                        Tgpj_BuildingAccount buildingAccount;
                        // 楼幢监管账号
                        Empj_BuildingAccountSupervisedForm accountSupervisedForm;
                        List<Empj_BuildingAccountSupervised> empj_buildingAccountSuperviseds;

                        Tgpj_BankAccountSupervised bankAccountSupervised = new Tgpj_BankAccountSupervised();

                        List<Tgpf_FundAppropriated_AFDtl> dtlList = new ArrayList<>();
                        // 用款申请子表信息
                        for (Empj_PaymentBondChild child : childList) {
                            empj_BuildingInfo = child.getEmpj_BuildingInfo();
                            buildingAccount = empj_BuildingInfo.getBuildingAccount();
                            accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
                            accountSupervisedForm.setTheState(S_TheState.Normal);
                            accountSupervisedForm.setIsUsing(0);
                            accountSupervisedForm.setBuildingInfoId(empj_BuildingInfo.getTableId());
                            empj_buildingAccountSuperviseds =
                                empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao
                                    .getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));

                            if (empj_buildingAccountSuperviseds.size() > 0) {
                                for (Empj_BuildingAccountSupervised buildingAccountSupervised : empj_buildingAccountSuperviseds) {
                                    if (null != buildingAccountSupervised.getBankAccountSupervised()
                                        && S_TheState.Normal == buildingAccountSupervised.getBankAccountSupervised()
                                            .getTheState()
                                        && 0 == buildingAccountSupervised.getBankAccountSupervised().getIsUsing()
                                        && StrUtil.isNotBlank(
                                            buildingAccountSupervised.getBankAccountSupervised().getTheAccount())) {
                                        bankAccountSupervised = buildingAccountSupervised.getBankAccountSupervised();
                                    }else{
                                        return MyBackInfo.fail(properties, "未查询到有效的监管账户信息！");
                                    }
                                }

                                Tgpf_FundAppropriated_AFDtl dtl = new Tgpf_FundAppropriated_AFDtl();
                                dtl.setTheState(S_TheState.Normal);
                                dtl.setBuilding(child.getEmpj_BuildingInfo());
                                dtl.seteCodeOfBuilding(child.geteCodeFromConstruction());
                                dtl.setMainTable(af);

                                dtl.setBankAccountSupervised(bankAccountSupervised);
                                dtl.setSupervisedBankAccount(bankAccountSupervised.getTheAccount());
                                dtl.setPayoutState(S_PayoutState.NotAppropriated); // 拨付状态 1: 未拨付
                                dtl.setAppliedAmount(
                                    null == child.getCanApplyAmount() ? 0.00 : child.getCanApplyAmount());
                                dtl.setAllocableAmount(null == buildingAccount.getSpilloverAmount() ? 0.00
                                    : buildingAccount.getSpilloverAmount());
                                dtl.setEscrowStandard(null == buildingAccount.getEscrowStandard() ? ""
                                    : buildingAccount.getEscrowStandard());
                                dtl.setOrgLimitedAmount(null == buildingAccount.getOrgLimitedAmount() ? 0.00
                                    : buildingAccount.getOrgLimitedAmount());
                                dtl.setCurrentFigureProgress(
                                    StringUtils.isBlank(buildingAccount.getCurrentFigureProgress()) ? ""
                                        : buildingAccount.getCurrentFigureProgress());
                                dtl.setCurrentLimitedRatio(null == buildingAccount.getCurrentLimitedRatio() ? 100.00
                                    : buildingAccount.getCurrentLimitedRatio());
                                dtl.setCurrentLimitedAmount(
                                    dtl.getOrgLimitedAmount() * dtl.getCurrentLimitedRatio() / 100);
                                dtl.setTotalAccountAmount(null == buildingAccount.getTotalAccountAmount() ? 0.00
                                    : buildingAccount.getTotalAccountAmount());
                                dtl.setAppliedPayoutAmount(null == buildingAccount.getPayoutAmount() ? 0.00
                                    : buildingAccount.getPayoutAmount());
                                dtl.setCurrentEscrowFund(null == buildingAccount.getCurrentEscrowFund() ? 0.00
                                    : buildingAccount.getCurrentEscrowFund());
                                dtl.setRefundAmount(null == buildingAccount.getRefundAmount() ? 0.00
                                    : buildingAccount.getRefundAmount());
                                dtl.setUserStart(model.getUser());
                                dtl.setCreateTimeStamp(nowDate);
                                dtl.setUserUpdate(model.getUser());
                                dtl.setLastUpdateTimeStamp(nowDate);

                                dtl.setCashLimitedAmount(null == child.getAfterCashLimitedAmount() ? 0.00
                                    : child.getAfterCashLimitedAmount());
                                dtl.setEffectiveLimitedAmount(null == child.getAfterEffectiveLimitedAmount() ? 0.00
                                    : child.getAfterEffectiveLimitedAmount());
                                dtl.setActualReleaseAmount(null == child.getActualReleaseAmount()? 0.00 : child.getActualReleaseAmount());
                                dtlList.add(dtl);
                                
                            }else{
                                return MyBackInfo.fail(properties, "未查询到有效的监管账户信息！");
                            }
                        }

                        // 用款申请汇总数据
                        List<Tgpf_FundOverallPlanDetail> detailList = new ArrayList<>();
                        Tgpf_FundOverallPlanDetail planDetail;
                        for (Tgpf_FundAppropriated_AFDtl dtl : dtlList) {

                            boolean isSave = true;
                            for (Tgpf_FundOverallPlanDetail detail : detailList) {
                                if (dtl.getSupervisedBankAccount().equals(detail.getSupervisedBankAccount())) {
                                    isSave = false;
                                    detail.setAppliedAmount(detail.getAppliedAmount() + dtl.getAppliedAmount());
                                }
                            }

                            if (isSave) {
                                planDetail = new Tgpf_FundOverallPlanDetail();
                                planDetail.setTheState(S_TheState.Normal);
                                planDetail.setCreateTimeStamp(nowDate);
                                planDetail.setUserStart(model.getUser());
                                planDetail.setUserUpdate(model.getUser());
                                planDetail.setLastUpdateTimeStamp(nowDate);

                                planDetail.setMainTable(af);
                                planDetail.setTheNameOfProject(StringUtils.isBlank(paymentBond.getProjectName()) ? ""
                                    : paymentBond.getProjectName());
                                planDetail.setTheNameOfBankBranch(dtl.getBankAccountSupervised().getTheNameOfBank());
                                planDetail.setBankAccountSupervised(dtl.getBankAccountSupervised());
                                planDetail.setTheNameOfAccount(dtl.getBankAccountSupervised().getTheName());
                                planDetail.setSupervisedBankAccount(dtl.getSupervisedBankAccount());
                                planDetail.setAppliedAmount(dtl.getAppliedAmount());
                                detailList.add(planDetail);
                            }

                            af.setTotalApplyAmount((null == af.getTotalApplyAmount() ? 0.00 : af.getTotalApplyAmount())
                                + (null == dtl.getAppliedAmount() ? 0.00 : dtl.getAppliedAmount()));
                        }

                        for (Tgpf_FundOverallPlanDetail tgpf_FundOverallPlanDetail : detailList) {
                            tgpf_FundOverallPlanDetailDao.save(tgpf_FundOverallPlanDetail);
                        }

                        af.setFundAppropriated_AFDtlList(dtlList);
                        af.setFundOverallPlanDetailList(detailList);
                        tgpf_FundAppropriated_AFDao.update(af);

                        empj_PaymentBondDao.update(paymentBond);
                        
                        /*
                         * 更新楼幢账户表相关数据
                         * 现金受限额度=业务办理后现金受限额度（保函业务）
                         * 有效受限额度=业务办理后有效受限额度（保函业务）
                         * 申请金额=原有溢出金额+楼幢实际可替代保证额度（保函业务）
                         */
                        for (Empj_PaymentBondChild child : childList) {
                            empj_BuildingInfo = child.getEmpj_BuildingInfo();
                            buildingAccount = empj_BuildingInfo.getBuildingAccount();
                            accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
                            accountSupervisedForm.setTheState(S_TheState.Normal);
                            accountSupervisedForm.setIsUsing(0);
                            accountSupervisedForm.setBuildingInfoId(empj_BuildingInfo.getTableId());
                            empj_buildingAccountSuperviseds =
                                empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao
                                    .getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));

                            if (empj_buildingAccountSuperviseds.size() > 0) {
                                buildingAccount.setCashLimitedAmount(child.getAfterCashLimitedAmount());
                                buildingAccount.setEffectiveLimitedAmount(child.getAfterEffectiveLimitedAmount());
                                buildingAccount.setTotalAmountGuaranteed((null == buildingAccount.getTotalAmountGuaranteed()? 0.00 : buildingAccount.getTotalAmountGuaranteed()) + child.getActualReleaseAmount());
                                tgpj_buildingAccountDao.update(buildingAccount);

                            }
                        }

                    }
                    break;
                default:
                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, "没有需要处理的回调");
            }
        } catch (Exception e) {
            e.printStackTrace();
            properties.put(S_NormalFlag.result, S_NormalFlag.fail);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
        }
        return properties;
    }
}

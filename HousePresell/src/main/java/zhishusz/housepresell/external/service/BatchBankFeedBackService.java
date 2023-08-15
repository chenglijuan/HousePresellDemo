package zhishusz.housepresell.external.service;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_SpecialFundApplyState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.BankFeedBackDtlModel;
import zhishusz.housepresell.external.po.BankFeedBackModel;
import zhishusz.housepresell.service.Sm_ApprovalProcess_EndService;
import zhishusz.housepresell.service.Tgpf_BuildingRemainRightLogPublicAddService;
import zhishusz.housepresell.service.Tgpf_FundAppropriatedUpdateService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;

/**
 * 
 * 接受支付结果推送信息
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
@Slf4j
public class BatchBankFeedBackService {

    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表
    @Autowired
    private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;// 特殊拨付主表
    @Autowired
    private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;// 特殊拨付子表
    @Autowired
    private Tgpf_RefundInfoDao tgpf_RefundInfoDao;// 退房退款
    @Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;// 一般拨付

    @Autowired
    private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
    @Autowired
    private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
    @Autowired
    private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
    @Autowired
    private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
    @Autowired
    private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;

    @Autowired
    private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAdd;// 留存权益计算方法

    @Autowired
    private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao;// 拨付信息

    @Autowired
    private Tgxy_BankAccountEscrowedDao tgxy_bankAccountEscrowedDao;

    @Autowired
    private Sm_ApprovalProcess_EndService endService;// 审批流手动通过处理
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;// 审批流申请单

    @Autowired
    private Sm_UserDao sm_UserDao;

    @Autowired
    private Tgpf_FundAppropriatedUpdateService tgpf_FundAppropriatedUpdateService;// 拨付完成更新

    MyDatetime myDatetime = MyDatetime.getInstance();

    @SuppressWarnings("unchecked")
    public synchronized Properties execute(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String json = obj.toJSONString();

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间

        tgpf_SocketMsg.setMsgDirection("BANK_TO_ZT");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(json);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        if (StringUtils.isBlank(json)) {
            return MyBackInfo.fail(properties, "请输入传递参数！");
        }

        try {

            BankFeedBackModel model = JSONObject.parseObject(json, BankFeedBackModel.class);
            List<BankFeedBackDtlModel> dtlList = model.getData();
            if (null == dtlList || dtlList.size() == 0) {
                return MyBackInfo.fail(properties, "推送明细为空！");
            }

            // 查询审批单
            Sm_ApprovalProcess_AFForm approvalProcess_AFModel = new Sm_ApprovalProcess_AFForm();
            approvalProcess_AFModel.setTheState(S_TheState.Normal);
            approvalProcess_AFModel.setOrderBy(" startTimeStamp desc");

            List<Sm_ApprovalProcess_AF> approvalProcess_AFList;
            Long workFlowId;

            List<Tgpf_SpecialFundAppropriated_AFDtl> specialFundAppropriated_AFDtlList;
            // 循环处理推送明细，根据业务类型进行不同的业务数据更新
            for (BankFeedBackDtlModel dtl : dtlList) {
                String use = dtl.getUse();
                String bizId = dtl.getBizId();

                if (StringUtils.isBlank(use) || StringUtils.isBlank(bizId)) {
                    return MyBackInfo.fail(properties, "流水号：" + dtl.getSerialNo() + "bizId或use为空！");
                }

                switch (use) {
                    case "061206":
                        // 特殊拨付

                        Tgpf_SpecialFundAppropriated_AFDtl specialFundAppropriated_AFDtl =
                            tgpf_SpecialFundAppropriated_AFDtlDao.findById(Long.valueOf(bizId));
                        if (null == specialFundAppropriated_AFDtl) {
                            return MyBackInfo.fail(properties, "未查询到有效的单据信息");
                        }

                        if (S_NormalFlag.success.equals(dtl.getBankStatusFlag())) {

                            /*
                             * 获取主表，遍历是不是所有拨付子表都已接收到银企直联的成功反馈
                             */
                            Tgpf_SpecialFundAppropriated_AF specialAppropriated =
                                specialFundAppropriated_AFDtl.getSpecialAppropriated();

                            Empj_BuildingInfo building = specialAppropriated.getBuilding();
                            if (null == building || null == building.getBuildingAccount()) {
                                return MyBackInfo.fail(properties, "关联楼幢或楼幢账户为空");
                            }

                            // 更新拨付的反馈状态
                            specialFundAppropriated_AFDtl.setPushState("1");
                            specialFundAppropriated_AFDtl.setPayoutDate(dtl.getTradeTime());
                            tgpf_SpecialFundAppropriated_AFDtlDao.update(specialFundAppropriated_AFDtl);

                            Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm =
                                new Tgpf_SpecialFundAppropriated_AFDtlForm();
                            dtlForm.setTheState(S_TheState.Normal);
                            dtlForm.setSpecialAppropriated(specialAppropriated);

                            specialFundAppropriated_AFDtlList =
                                tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
                                    .getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

                            boolean isFlag = true;
                            for (Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl : specialFundAppropriated_AFDtlList) {
                                if (!"1".equals(tgpf_SpecialFundAppropriated_AFDtl.getPushState())) {
                                    isFlag = false;
                                    break;
                                }
                            }

                            if (S_ApplyState.Admissible == specialAppropriated.getApplyState()
                                && S_ApprovalState.Completed.equals(specialAppropriated.getApprovalState())) {
                                isFlag = false;
                            }

                            // 全部收到银企直联的成功反馈，做最后的更新
                            if (isFlag) {
                                // 更新拨付信息
                                for (Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl : specialFundAppropriated_AFDtlList) {

                                    /*
                                     * 根据拨付金额，从托管账户中减去相应的金额 canPayAmount：托管可拨付
                                     * appliedAmount：拨付金额
                                     */
                                    // 本次拨付金额
                                    Double appliedAmount = tgpf_SpecialFundAppropriated_AFDtl.getAppliedAmount();
                                    Tgxy_BankAccountEscrowed bankAccountEscrowed =
                                        tgpf_SpecialFundAppropriated_AFDtl.getBankAccountEscrowed();
                                    /**
                                     * 托管支出 + 活期余额 - 托管可拨付 -
                                     */
                                    // 托管支出
                                    Double payout = null == bankAccountEscrowed.getPayout() ? 0.00
                                        : bankAccountEscrowed.getPayout();
                                    // 活期余额
                                    Double currentBalance = null == bankAccountEscrowed.getCurrentBalance() ? 0.00
                                        : bankAccountEscrowed.getCurrentBalance();
                                    // 托管可拨付金额
                                    Double canPayAmount = null == bankAccountEscrowed.getCanPayAmount() ? 0.00
                                        : bankAccountEscrowed.getCanPayAmount();

                                    bankAccountEscrowed
                                        .setPayout(MyDouble.getInstance().doubleAddDouble(payout, appliedAmount));
                                    bankAccountEscrowed.setCurrentBalance(
                                        MyDouble.getInstance().doubleSubtractDouble(currentBalance, appliedAmount));
                                    // 更新托管账户
                                    bankAccountEscrowed.setCanPayAmount(
                                        MyDouble.getInstance().doubleSubtractDouble(canPayAmount, appliedAmount));

                                    tgxy_BankAccountEscrowedDao.save(bankAccountEscrowed);

                                    tgpf_SpecialFundAppropriated_AFDtl
                                        .setPayoutState(S_SpecialFundApplyState.Appropriated);
                                    tgpf_SpecialFundAppropriated_AFDtlDao.save(tgpf_SpecialFundAppropriated_AFDtl);
                                }

                                specialAppropriated.setApplyState(S_ApplyState.Admissible); // 用款申请单状态
                                specialAppropriated.setApprovalState(S_ApprovalState.Completed);// 流程状态
                                specialAppropriated.setRecordTimeStamp(System.currentTimeMillis());// 备案日期
                                specialAppropriated.setAfPayoutDate(dtl.getTradeTime());

                                tgpf_SpecialFundAppropriated_AFDao.update(specialAppropriated);

                                // 对楼幢账户进行计算
                                passChange(building.getBuildingAccount(), specialAppropriated);

                                // 留存权益计算
                                Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm =
                                    new Tgpf_BuildingRemainRightLogForm();
                                tgpf_BuildingRemainRightLogForm.setBillTimeStamp(
                                    MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));// 记账日期（当前日期）
                                tgpf_BuildingRemainRightLogForm.setBuildingId(building.getTableId());
                                tgpf_BuildingRemainRightLogForm.setSrcBusiType("入账");

                                tgpf_BuildingRemainRightLogPublicAdd.execute(tgpf_BuildingRemainRightLogForm);

                                // 更新最后的审批单结束
                                approvalProcess_AFModel.setBusiCode("061206");
                                approvalProcess_AFModel.setSourceId(specialAppropriated.getTableId());
                                approvalProcess_AFList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao
                                    .getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), approvalProcess_AFModel));
                                if (null != approvalProcess_AFList && approvalProcess_AFList.size() > 0) {
                                    workFlowId = approvalProcess_AFList.get(0).getCurrentIndex();

                                    endService.execute(workFlowId);
                                }

                            }
                        } else {

                            specialFundAppropriated_AFDtl.setPushState("2");
                            tgpf_SpecialFundAppropriated_AFDtlDao.update(specialFundAppropriated_AFDtl);

                        }

                        break;

                    case "06120302":
                        // 一般拨付统筹
                        Tgpf_FundAppropriated tgpf_FundAppropriated =
                            tgpf_FundAppropriatedDao.findById(Long.valueOf(bizId));
                        if (null == tgpf_FundAppropriated) {
                            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
                        }

                        if (S_NormalFlag.success.equals(dtl.getBankStatusFlag())) {

                            if (!"2".equals(tgpf_FundAppropriated.getBusiState())) {

                                tgpf_FundAppropriated.setPushState("1");
                                tgpf_FundAppropriated.setActualPayoutDate(dtl.getTradeTime());
                                tgpf_FundAppropriatedDao.update(tgpf_FundAppropriated);

                                Tgpf_FundAppropriated_AF fundAppropriated_AF =
                                    tgpf_FundAppropriated.getFundAppropriated_AF();
                                if (null == fundAppropriated_AF) {
                                    return MyBackInfo.fail(properties, "拨付申请信息不存在！");
                                }

                                // 查询有效的统筹拨付信息
                                Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
                                fundAppropriatedForm.setTheState(S_TheState.Normal);
                                fundAppropriatedForm.setFundAppropriated_AFId(fundAppropriated_AF.getTableId());
                                fundAppropriatedForm.setOverallPlanPayoutAmount(0D);
                                List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList =
                                    tgpf_FundAppropriatedDao.findByPage(tgpf_FundAppropriatedDao
                                        .getQuery(tgpf_FundAppropriatedDao.getBasicHQL(), fundAppropriatedForm));

                                // 统筹拨付信息是否全部接受到银企返回
                                boolean isFlag = true;
                                for (Tgpf_FundAppropriated tgpf_FundAppropriated2 : tgpf_FundAppropriatedList) {
                                    if (!"1".equals(tgpf_FundAppropriated2.getPushState())) {
                                        isFlag = false;
                                        break;
                                    }
                                }

                                if (S_ApplyState.Alreadydisbursed == fundAppropriated_AF.getApplyState()) {
                                    isFlag = false;
                                }
                                // 进行最后的拨付完成操作
                                if (isFlag) {

                                    Sm_User sm_User = sm_UserDao.findById(1038L);
                                    Tgpf_FundAppropriatedForm form = new Tgpf_FundAppropriatedForm();
                                    form.setUser(sm_User);
                                    form.setUserId(1038L);
                                    form.setFundAppropriated_AFId(fundAppropriated_AF.getTableId());
                                    form.setInterfaceVersion(19000101);
                                    form.setFundAppropriatedList(tgpf_FundAppropriatedList);
                                    form.setButtonType("2");

                                    tgpf_FundAppropriatedUpdateService.execute(form);

                                }
                            }

                        } else {
                            tgpf_FundAppropriated.setPushState("2");
                            tgpf_FundAppropriatedDao.update(tgpf_FundAppropriated);
                        }

                        break;

                    case "06120201":
                        // 退房退款（已结清）
                    case "06120202":
                        // 退房退款（未结清）

                        Tgpf_RefundInfo tgpf_RefundInfo = tgpf_RefundInfoDao.findById(Long.valueOf(bizId));
                        if (null == tgpf_RefundInfo) {
                            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
                        }

                        // 判断是否交易成功
                        if (S_NormalFlag.success.equals(dtl.getBankStatusFlag())) {
                            // 成功

                            if (S_ApprovalState.Completed.equals(tgpf_RefundInfo.getApprovalState())
                                && S_BusiState.HaveRecord.equals(tgpf_RefundInfo.getBusiState())) {
                            } else {

                                // 查询楼幢账户
                                Empj_BuildingInfo building = tgpf_RefundInfo.getBuilding();
                                if (building == null) {
                                    return MyBackInfo.fail(properties, "未查询到楼幢信息");
                                }

                                Tgpj_BuildingAccountForm form = new Tgpj_BuildingAccountForm();
                                form.setTheState(0);
                                form.setBuilding(building);
                                Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao.findOneByQuery_T(
                                    tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), form));
                                if (null == tgpj_BuildingAccount) {
                                    return MyBackInfo.fail(properties, "未查询到有效的楼幢托管账户信息");
                                }

                                // 获取实际退款金额
                                Double actualRefundAmount = tgpf_RefundInfo.getActualRefundAmount();
                                /*
                                 * 更新托管账户表 计算：增加托管支出 减少活期余额
                                 */
                                Tgxy_BankAccountEscrowed theBankAccountEscrowed =
                                    tgpf_RefundInfo.getTheBankAccountEscrowed();
                                if (theBankAccountEscrowed == null) {
                                    return MyBackInfo.fail(properties, "未查询到银行账户信息，请核对退款银行和退款账号信息。");
                                }

                                // 托管支出
                                Double payout = theBankAccountEscrowed.getPayout();
                                // 活期余额
                                Double currentBalance = theBankAccountEscrowed.getCurrentBalance();
                                // 托管可拨付金额
                                Double canPayAmount = theBankAccountEscrowed.getCanPayAmount();

                                /**
                                 * 托管支出 + 活期余额 - 托管可拨付 -
                                 */

                                if (null == canPayAmount) {
                                    canPayAmount = 0.00;
                                }
                                canPayAmount =
                                    MyDouble.getInstance().doubleSubtractDouble(canPayAmount, actualRefundAmount);

                                if (null == canPayAmount || 0 > canPayAmount) {
                                    canPayAmount = 0.00;
                                }
                                theBankAccountEscrowed.setCanPayAmount(canPayAmount);

                                if (null == payout || payout == 0) {
                                    payout = actualRefundAmount;
                                } else {
                                    payout = MyDouble.getInstance().doubleAddDouble(payout, actualRefundAmount);
                                }

                                if (null == currentBalance) {
                                    currentBalance = 0.00;
                                }
                                currentBalance =
                                    MyDouble.getInstance().doubleSubtractDouble(currentBalance, actualRefundAmount);

                                if (null == currentBalance || 0 > currentBalance) {
                                    currentBalance = 0.00;
                                }
                                theBankAccountEscrowed.setPayout(payout);
                                theBankAccountEscrowed.setCurrentBalance(currentBalance);

                                tgxy_BankAccountEscrowedDao.save(theBankAccountEscrowed);

                                /*
                                 * 更新楼幢账户表 计算：增加“已退款金额（元）”、减少“已申请退款未拨付金额（元）”
                                 */
                                // 已退款金额
                                Double refundAmount = tgpj_BuildingAccount.getRefundAmount();
                                // 已申请退款未拨付金额（元）
                                Double applyRefundPayoutAmount = tgpj_BuildingAccount.getApplyRefundPayoutAmount();

                                if (null == refundAmount || refundAmount == 0) {
                                    refundAmount = actualRefundAmount;
                                } else {
                                    refundAmount =
                                        MyDouble.getInstance().doubleAddDouble(refundAmount, actualRefundAmount);
                                }

                                applyRefundPayoutAmount = MyDouble.getInstance()
                                    .doubleSubtractDouble(applyRefundPayoutAmount, actualRefundAmount);

                                properties = setChange(properties, refundAmount, applyRefundPayoutAmount,
                                    tgpj_BuildingAccount, tgpf_RefundInfo, use);

                                // 查询三方协议
                                Tgxy_TripleAgreement tgxy_TripleAgreement = tgpf_RefundInfo.getTripleAgreement();
                                if (tgxy_TripleAgreement == null) {
                                    return MyBackInfo.fail(properties, "未查询到有效的三方协议数据！");
                                }

                                tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect("3");

                                tgxy_TripleAgreement.setTheAmountOfRetainedEquity(0.00);
                                tgxy_TripleAgreement.setTheAmountOfInterestRetained(0.00);
                                tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(0.00);
                                tgxy_TripleAgreement.setTotalAmountOfHouse(0.00);
                                tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);

                                // 维护退房退款信息
                                tgpf_RefundInfo.setPushState("1");
                                tgpf_RefundInfo.setRefundTimeStamp(dtl.getTradeTime());
                                tgpf_RefundInfo.setBusiState(S_BusiState.HaveRecord);
                                tgpf_RefundInfo.setApprovalState(S_ApprovalState.Completed);

                                tgpf_RefundInfoDao.save(tgpf_RefundInfo);

                                // 更新最后的审批单结束
                                approvalProcess_AFModel.setBusiCode(use);
                                approvalProcess_AFModel.setSourceId(Long.valueOf(bizId));
                                approvalProcess_AFList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao
                                    .getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), approvalProcess_AFModel));
                                if (null != approvalProcess_AFList && approvalProcess_AFList.size() > 0) {
                                    workFlowId = approvalProcess_AFList.get(0).getCurrentIndex();

                                    endService.execute(workFlowId);
                                }
                            }
                        } else {
                            // 失败
                            tgpf_RefundInfo.setPushState("2");
                            tgpf_RefundInfoDao.save(tgpf_RefundInfo);

                        }
                        break;

                    default:
                        break;
                }

            }

        } catch (Exception e) {
            tgpf_SocketMsg.setReturnCode("400");// 返回码
            log.error("转账支付推送异常：" + e.getMessage(), e);
            e.printStackTrace();
        }

        return properties;

    }

    /**
     * 保存修改文件
     * 
     * @param properties
     * @param refundAmount
     *            实际退款金额
     * @param applyRefundPayoutAmount
     *            已申请退款未拨付金额
     * @param tgpj_BuildingAccount
     * @param tgpf_RefundInfo
     * @return
     */
    @SuppressWarnings("unchecked")
    private Properties setChange(Properties properties, Double refundAmount, Double applyRefundPayoutAmount,
        Tgpj_BuildingAccount tgpj_BuildingAccount, Tgpf_RefundInfo tgpf_RefundInfo, String busiCode) {

        Long accountVersion = tgpj_BuildingAccount.getVersionNo();
        if (null == tgpj_BuildingAccount.getVersionNo() || tgpj_BuildingAccount.getVersionNo() < 0) {
            accountVersion = 1l;
        }

        // 根据楼幢账户查询版本号最大的楼幢账户log表
        // 查询条件：1.业务编码 2.楼幢账户 3.关联主键 4.根据版本号大小排序
        Tgpj_BuildingAccountLogForm tgpj_BuildingAccountLogForm = new Tgpj_BuildingAccountLogForm();
        tgpj_BuildingAccountLogForm.setTheState(S_TheState.Normal);
        tgpj_BuildingAccountLogForm.setRelatedBusiCode(busiCode);
        tgpj_BuildingAccountLogForm.setTgpj_BuildingAccount(tgpj_BuildingAccount);
        tgpj_BuildingAccountLogForm.setRelatedBusiTableId(tgpf_RefundInfo.getTableId());

        Integer logCount = tgpj_BuildingAccountLogDao.findByPage_Size(tgpj_BuildingAccountLogDao
            .getQuery_Size(tgpj_BuildingAccountLogDao.getSpecialHQL(), tgpj_BuildingAccountLogForm));

        List<Tgpj_BuildingAccountLog> tgpj_BuildingAccountLogList;
        if (logCount > 0) {
            tgpj_BuildingAccountLogList = tgpj_BuildingAccountLogDao.findByPage(tgpj_BuildingAccountLogDao
                .getQuery(tgpj_BuildingAccountLogDao.getBasicHQL(), tgpj_BuildingAccountLogForm));

            Tgpj_BuildingAccountLog buildingAccountLog = tgpj_BuildingAccountLogList.get(0);
            // 获取日志表的版本号
            Long logVersionNo = buildingAccountLog.getVersionNo();
            if (null == buildingAccountLog.getVersionNo() || buildingAccountLog.getVersionNo() < 0) {
                logVersionNo = 1l;
            }
            if (logVersionNo > accountVersion) {
                return MyBackInfo.fail(properties, "备案版本存在回档，请核实后重新发起！");
            } else {

                // 不发生修改的字段
                Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
                tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(tgpj_BuildingAccount.getBldLimitAmountVerDtl());
                tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
                tgpj_BuildingAccountLog.setBusiState(tgpj_BuildingAccount.getBusiState());
                tgpj_BuildingAccountLog.seteCode(tgpj_BuildingAccount.geteCode());
                tgpj_BuildingAccountLog.setUserStart(tgpj_BuildingAccount.getUserStart());
                tgpj_BuildingAccountLog.setCreateTimeStamp(tgpj_BuildingAccount.getCreateTimeStamp());
                tgpj_BuildingAccountLog.setUserUpdate(tgpj_BuildingAccount.getUserUpdate());
                tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
                tgpj_BuildingAccountLog.setUserRecord(tgpj_BuildingAccount.getUserRecord());
                tgpj_BuildingAccountLog.setRecordTimeStamp(tgpj_BuildingAccount.getRecordTimeStamp());
                tgpj_BuildingAccountLog.setDevelopCompany(tgpj_BuildingAccount.getDevelopCompany());
                tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(tgpj_BuildingAccount.geteCodeOfDevelopCompany());
                tgpj_BuildingAccountLog.setProject(tgpj_BuildingAccount.getProject());
                tgpj_BuildingAccountLog.setTheNameOfProject(tgpj_BuildingAccount.getTheNameOfProject());
                tgpj_BuildingAccountLog.setBuilding(tgpj_BuildingAccount.getBuilding());
                tgpj_BuildingAccountLog.setPayment(tgpj_BuildingAccount.getPayment());
                tgpj_BuildingAccountLog.seteCodeOfBuilding(tgpj_BuildingAccount.geteCodeOfBuilding());
                tgpj_BuildingAccountLog.setEscrowStandard(tgpj_BuildingAccount.getEscrowStandard());
                tgpj_BuildingAccountLog.setEscrowArea(tgpj_BuildingAccount.getEscrowArea());
                tgpj_BuildingAccountLog.setBuildingArea(tgpj_BuildingAccount.getBuildingArea());
                tgpj_BuildingAccountLog.setOrgLimitedAmount(tgpj_BuildingAccount.getOrgLimitedAmount());
                tgpj_BuildingAccountLog.setCurrentFigureProgress(tgpj_BuildingAccount.getCurrentFigureProgress());
                tgpj_BuildingAccountLog.setCurrentLimitedRatio(tgpj_BuildingAccount.getCurrentLimitedRatio());
                tgpj_BuildingAccountLog.setNodeLimitedAmount(tgpj_BuildingAccount.getNodeLimitedAmount());
                tgpj_BuildingAccountLog.setTotalGuaranteeAmount(tgpj_BuildingAccount.getTotalGuaranteeAmount());
                tgpj_BuildingAccountLog.setCashLimitedAmount(tgpj_BuildingAccount.getCashLimitedAmount());
                tgpj_BuildingAccountLog.setTotalAccountAmount(tgpj_BuildingAccount.getTotalAccountAmount());
                tgpj_BuildingAccountLog.setPayoutAmount(tgpj_BuildingAccount.getPayoutAmount());
                tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(tgpj_BuildingAccount.getAppliedNoPayoutAmount());
                tgpj_BuildingAccountLog.setCurrentEscrowFund(tgpj_BuildingAccount.getCurrentEscrowFund());
                tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(
                    tgpj_BuildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
                tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(tgpj_BuildingAccount.getRecordAvgPriceOfBuilding());
                tgpj_BuildingAccountLog.setLogId(tgpj_BuildingAccount.getLogId());
                tgpj_BuildingAccountLog.setActualAmount(tgpj_BuildingAccount.getActualAmount());
                tgpj_BuildingAccountLog.setPaymentLines(tgpj_BuildingAccount.getPaymentLines());
                tgpj_BuildingAccountLog.setRelatedBusiCode(busiCode);
                tgpj_BuildingAccountLog.setRelatedBusiTableId(tgpf_RefundInfo.getTableId());
                tgpj_BuildingAccountLog.setTgpj_BuildingAccount(tgpj_BuildingAccount);
                tgpj_BuildingAccountLog.setVersionNo(tgpj_BuildingAccount.getVersionNo());
                tgpj_BuildingAccountLog.setPaymentProportion(tgpj_BuildingAccount.getPaymentProportion());
                tgpj_BuildingAccountLog.setBuildAmountPaid(tgpj_BuildingAccount.getBuildAmountPaid());
                tgpj_BuildingAccountLog.setBuildAmountPay(tgpj_BuildingAccount.getBuildAmountPay());
                tgpj_BuildingAccountLog.setTotalAmountGuaranteed(tgpj_BuildingAccount.getTotalAmountGuaranteed());
                tgpj_BuildingAccountLog.setCashLimitedAmount(tgpj_BuildingAccount.getCashLimitedAmount());
                tgpj_BuildingAccountLog.setEffectiveLimitedAmount(tgpj_BuildingAccount.getEffectiveLimitedAmount());
                tgpj_BuildingAccountLog.setSpilloverAmount(tgpj_BuildingAccount.getSpilloverAmount());
                tgpj_BuildingAccountLog.setAllocableAmount(tgpj_BuildingAccount.getAllocableAmount());
                tgpj_BuildingAccountLog.setAppropriateFrozenAmount(tgpj_BuildingAccount.getAppropriateFrozenAmount());

                // 修改产生了变更的字段
                tgpj_BuildingAccountLog.setRefundAmount(refundAmount);
                tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(applyRefundPayoutAmount);

                tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);

                tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
            }
        } else {
            return MyBackInfo.fail(properties, "存在未申请备案的楼幢，请核实后重新发起！");
        }
        return properties;
    }

    /**
     * 特殊拨付
     * 
     * @param buildingAccount
     * @param AF
     */
    public void passChange(Tgpj_BuildingAccount buildingAccount, Tgpf_SpecialFundAppropriated_AF AF) {
        // 不发生修改的字段
        Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
        tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
        tgpj_BuildingAccountLog.setBusiState("1");
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
        tgpj_BuildingAccountLog.setBuilding(buildingAccount.getBuilding());
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
        tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(
            buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
        tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
        tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
        tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
        tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
        tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
        tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
        tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
        tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
        tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
        tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
        tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
        tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(buildingAccount.getBldLimitAmountVerDtl());
        tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
        tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(buildingAccount.getApplyRefundPayoutAmount());

        tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
        tgpj_BuildingAccountLog.setRelatedBusiCode("061206");
        tgpj_BuildingAccountLog.setRelatedBusiTableId(AF.getTableId());

        Double allocableAmount = buildingAccount.getAllocableAmount();
        if (null == allocableAmount || allocableAmount < 0) {
            allocableAmount = 0.00;
        }
        Double appliedNoPayoutAmount = buildingAccount.getAppliedNoPayoutAmount();
        if (null == appliedNoPayoutAmount || appliedNoPayoutAmount < 0) {
            appliedNoPayoutAmount = 0.00;
        }
        Double appropriateFrozenAmount = buildingAccount.getAppropriateFrozenAmount();
        if (null == appropriateFrozenAmount || appropriateFrozenAmount < 0) {
            appropriateFrozenAmount = 0.00;
        }
        Double payoutAmount = buildingAccount.getPayoutAmount();
        if (null == payoutAmount || payoutAmount < 0) {
            payoutAmount = 0.00;
        }

        // 已拨付金额 +
        payoutAmount = MyDouble.getInstance().doubleAddDouble(payoutAmount, AF.getTotalApplyAmount());
        // 已申请未拨付金额（元） -
        appliedNoPayoutAmount =
            MyDouble.getInstance().doubleSubtractDouble(appliedNoPayoutAmount, AF.getTotalApplyAmount());
        // 拨付冻结金额 ：appropriateFrozenAmount -
        appropriateFrozenAmount =
            MyDouble.getInstance().doubleSubtractDouble(appropriateFrozenAmount, AF.getTotalApplyAmount());

        // 已拨付金额 +
        tgpj_BuildingAccountLog.setPayoutAmount(payoutAmount);
        // 已申请未拨付金额（元）：appliedNoPayoutAmount -
        tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
        // 拨付冻结金额 ：appropriateFrozenAmount -
        tgpj_BuildingAccountLog.setAppropriateFrozenAmount(appropriateFrozenAmount);

        tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
    }

}

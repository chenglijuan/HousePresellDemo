package zhishusz.housepresell.external.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;

/**
 * 网银数据推送接收service
 *
 * @author Administrator
 */
@Service
@Transactional
@Slf4j
public class UploadBalanceOfAccountService {

    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表


    @Autowired
    private Tgxy_TripleAgreementDao tgxy_tripleAgreementDao;

    @Autowired
    private Tgxy_BankAccountEscrowedDao tgxy_bankAccountEscrowedDao;

    @Autowired
    private Tgpf_DepositDetailDao tgpf_depositDetailDao;

    @Autowired
    private Tgpf_CyberBankStatementDao tgpf_cyberBankStatementDao;

    @Autowired
    private Tgpf_CyberBankStatementDtlDao tgpf_cyberBankStatementDtlDao;

    @Autowired
    private Tgpf_BankUploadDataDetailDao tgpf_bankUploadDataDetailDao;


    @Autowired
    private Tgpf_BalanceOfAccountDao tgpf_balanceOfAccountDao;

    @Autowired
    private Tgpf_DayEndBalancingDao tgpf_dayEndBalancingDao;


    //
    public synchronized Properties uploadBalanceOfAccountExecute(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        if (obj == null) {

            return MyBackInfo.fail(properties, "请输入传递参数");
        }
        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg.setMsgDirection("UPLOAD_BALANCE_TO_ZT");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(obj.toJSONString());// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        // 预售证编号
        String result = obj.getString("result");
        if (StringUtils.isBlank(result)) {
            return MyBackInfo.fail(properties, "数据不能为空");
        }

        Long current = 0l;
        Sm_User user = new Sm_User();
        user.setTableId(652L);
        user.setTheAccount("TEST");

        JSONArray array = obj.getJSONArray("result");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);

            current = System.currentTimeMillis();
            String reconciliationTime = object.getString("reconciliationTime"); //对账时间
            String theNameOfBankAccountEscrowed = object.getString("theNameOfBankAccountEscrowed");  //托管账号
            String billTimeStamp = object.getString("billTimeStamp");  //记账时间
            Double transactionAmount = object.getDouble("transactionAmount");  // 交易总金额
            Integer transactionCount = object.getInteger("transactionCount");  // 交易送条数

            //如果已经有数据直接跳过
            Tgpf_CyberBankStatementForm statementForm = new Tgpf_CyberBankStatementForm();
            statementForm.setTheState(S_TheState.Normal);
            statementForm.setTheAccountOfBankAccountEscrowed(theNameOfBankAccountEscrowed);
            statementForm.setBillTimeStamp(billTimeStamp);

            // 如果对账时间或者托管账号为空
            if (StringUtils.isBlank(billTimeStamp) || StringUtils.isBlank(theNameOfBankAccountEscrowed)) {
                continue;
            }

            //如果有记录 继续 没有就对所有表进行新增
            Integer statementCount = tgpf_cyberBankStatementDao.findByPage_Size(
                    tgpf_cyberBankStatementDao.getQuery_Size(tgpf_cyberBankStatementDao.getBasicHQL(), statementForm));
            if (statementCount > 0) {

                //查看是否做过日终结算，如果做过结算，不修改,没做过日终结算需要将其他的删除

                Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
                tgpf_DayEndBalancingForm.setEscrowedAccount(theNameOfBankAccountEscrowed);
                tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
                tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStamp);

                List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList =
                        tgpf_dayEndBalancingDao.findByPage(tgpf_dayEndBalancingDao.getQuery(tgpf_dayEndBalancingDao.getBasicHQL(), tgpf_DayEndBalancingForm));
                Tgpf_DayEndBalancing tgpf_DayEndBalancing = null;
                if (tgpf_DayEndBalancingList != null && tgpf_DayEndBalancingList.size() > 0) {
                    tgpf_DayEndBalancing = tgpf_DayEndBalancingList.get(0);
                    //如果已经结算了 ，不再更新明细数据
                    if (null != tgpf_DayEndBalancing.getSettlementState() && (tgpf_DayEndBalancing.getSettlementState().intValue() == 2)) {
                        continue;
                    }
                } else {
                    System.out.println("调用了存储过程");
                    System.out.println("billTimeStamp=" + billTimeStamp);
                    System.out.println("theNameOfBankAccountEscrowed=" + theNameOfBankAccountEscrowed);
                    properties = tgpf_cyberBankStatementDao.deletedzdata(billTimeStamp, theNameOfBankAccountEscrowed);
                }
            }
            Tgxy_BankAccountEscrowedForm escrowedForm = new Tgxy_BankAccountEscrowedForm();
            escrowedForm.setTheAccount(theNameOfBankAccountEscrowed);
            escrowedForm.setTheState(S_TheState.Normal);

            Tgxy_BankAccountEscrowed bankAccountEscrowed = null;
            // 查询资金归集明细表
            Integer escrowedCount = tgxy_bankAccountEscrowedDao.findByPage_Size(
                    tgxy_bankAccountEscrowedDao.getQuery_Size(tgxy_bankAccountEscrowedDao.getBasicHQL(), escrowedForm));
            if (escrowedCount > 0) {
                List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList = tgxy_bankAccountEscrowedDao.findByPage(tgxy_bankAccountEscrowedDao
                        .getQuery(tgxy_bankAccountEscrowedDao.getBasicHQL(), escrowedForm));
                bankAccountEscrowed = tgxy_BankAccountEscrowedList.get(0);
            }

            if (bankAccountEscrowed == null || StringUtils.isBlank(bankAccountEscrowed.getTheAccount())) {
                System.out.println("账号不存在");
                continue;
            }

            Tgpf_CyberBankStatement cyberBankStatement = new Tgpf_CyberBankStatement();
            cyberBankStatement.setBillTimeStamp(billTimeStamp);
            cyberBankStatement.setTheState(S_TheState.Normal);
            cyberBankStatement.setBusiState("0");
            cyberBankStatement.setUserStart(user);
            cyberBankStatement.setCreateTimeStamp(current);
            cyberBankStatement.setUserUpdate(user);
            cyberBankStatement.setLastUpdateTimeStamp(current);
            cyberBankStatement.setUserRecord(user);
            cyberBankStatement.setRecordTimeStamp(current);
            cyberBankStatement.setTheAccountOfBankAccountEscrowed(bankAccountEscrowed.getTheAccount());// 托管账号
            cyberBankStatement.setTheNameOfBankAccountEscrowed(bankAccountEscrowed.getTheName());// 托管账号名称
            cyberBankStatement.setTheNameOfBankBranch(bankAccountEscrowed.getBankBranch().getTheName());// 开户行名称
            cyberBankStatement.setTheNameOfBank(bankAccountEscrowed.getBank().getTheName());// 银行名称


            cyberBankStatement.setReconciliationState(1);
            cyberBankStatement.setReconciliationUser("TEST");
            cyberBankStatement.setUploadTimeStamp(billTimeStamp);
            cyberBankStatement.setBillTimeStamp(billTimeStamp);
            cyberBankStatement.setFileUploadState(1);
            tgpf_cyberBankStatementDao.save(cyberBankStatement);

            Tgpf_BalanceOfAccount balanceOfAccount = new Tgpf_BalanceOfAccount();
            balanceOfAccount.setTheState(S_TheState.Normal);
            balanceOfAccount.setBusiState("1011");
            balanceOfAccount.setUserStart(user);
            balanceOfAccount.setCreateTimeStamp(current);
            balanceOfAccount.setLastUpdateTimeStamp(current);
            balanceOfAccount.setUserRecord(user);
            balanceOfAccount.setRecordTimeStamp(current);
            balanceOfAccount.setBillTimeStamp(billTimeStamp);
//            Emmp_BankInfo bank = ;
            balanceOfAccount.setBankName(bankAccountEscrowed.getBank().getTheName()); //银行名称
            balanceOfAccount.setEscrowedAccount(bankAccountEscrowed.getTheAccount());  //托管账号
            balanceOfAccount.setEscrowedAccountTheName(bankAccountEscrowed.getTheName());  // 托管账号名称
            balanceOfAccount.setCenterTotalCount(transactionCount);
            balanceOfAccount.setCenterTotalAmount(transactionAmount);
            balanceOfAccount.setBankTotalAmount(transactionAmount);
            balanceOfAccount.setBankTotalCount(transactionCount);
            balanceOfAccount.setCyberBankTotalAmount(transactionAmount);
            balanceOfAccount.setCyberBankTotalCount(transactionCount);
            balanceOfAccount.setAccountType(1);
            balanceOfAccount.setReconciliationDate(reconciliationTime); // 对账时间
            balanceOfAccount.setReconciliationState(1);
            balanceOfAccount.setBankBranch(bankAccountEscrowed.getBankBranch());
            balanceOfAccount.setTgxy_BankAccountEscrowed(bankAccountEscrowed);
            tgpf_balanceOfAccountDao.save(balanceOfAccount);


            JSONArray dtlArray = object.getJSONArray("dtllist");

            for (int j = 0; j < dtlArray.size(); j++) {
                JSONObject dtlobject = dtlArray.getJSONObject(j);

                Double amount = dtlobject.getDouble("amount");  //贷款金额
                Long detailedId = dtlobject.getLong("detailedId");  //交易记录id
                Integer fundProperty = dtlobject.getInteger("fundProperty"); // 1商用贷款 2.公积金
                String recipientAccount = dtlobject.getString("recipientAccount");  //收款账号
                String recipientName = dtlobject.getString("recipientName");  //收款方名称
                String remark = dtlobject.getString("remark");  //备注
                Long tripleagreement = dtlobject.getLong("tripleagreement");// 三方协议id

                Tgxy_TripleAgreement tripleAgreement = tgxy_tripleAgreementDao.findById(tripleagreement);
                // 判断三方协议是否删除
                if (tripleAgreement == null && StringUtils.isBlank(tripleAgreement.geteCodeOfContractRecord())) {
                    //跳出内层循环
                    break;
                }

                //银行端流水号
                String ecodeFromBankPlatform = "tgpf" + detailedId;

                Tgpf_DepositDetail tgpf_depositDetail = new Tgpf_DepositDetail();
                tgpf_depositDetail.setTheState(S_TheState.Normal);
                tgpf_depositDetail.setUserStart(user);
                tgpf_depositDetail.setCreateTimeStamp(current);
                tgpf_depositDetail.setLastUpdateTimeStamp(current);
                tgpf_depositDetail.setRecordTimeStamp(current);
                tgpf_depositDetail.setUserRecord(user);
                //缴款序号
                tgpf_depositDetail.setFundProperty(fundProperty); //资金性质
                tgpf_depositDetail.setBankAccountEscrowed(bankAccountEscrowed);  //托管账号
                tgpf_depositDetail.setTripleAgreement(tripleAgreement);   //三方协议
                tgpf_depositDetail.setBankBranch(bankAccountEscrowed.getBankBranch());
                tgpf_depositDetail.setTheNameOfBankAccountEscrowed(bankAccountEscrowed.getTheName());  //托管账户名称
                tgpf_depositDetail.setTheAccountOfBankAccountEscrowed(bankAccountEscrowed.getTheAccount());  //托管账户
                tgpf_depositDetail.setTheNameOfCreditor(recipientName);
                tgpf_depositDetail.setIdType("1");
                tgpf_depositDetail.setBankAccountForLoan(recipientAccount);// 用于接收贷款的银行账号
                tgpf_depositDetail.setLoanAmountFromBank(amount);// 银行放款金额（元）
                tgpf_depositDetail.setBillTimeStamp(billTimeStamp);
                tgpf_depositDetail.setDepositState(1);
                tgpf_depositDetail.setDepositDatetime(billTimeStamp);
                tgpf_depositDetail.setReconciliationStateFromBusiness(1); //业务对账状态
                tgpf_depositDetail.setReconciliationTimeStampFromBusiness(reconciliationTime); //业务对账时间
                tgpf_depositDetail.setReconciliationTimeStampFromCyberBank(reconciliationTime);  //网银对账时间
                tgpf_depositDetail.setReconciliationStateFromCyberBank(1); // 网银对账时间
                tgpf_depositDetail.setHasVoucher(false);
                tgpf_depositDetail.setTheStateFromReverse(0);
                tgpf_depositDetail.seteCodeFromBankPlatform(ecodeFromBankPlatform); //银行端流水号
                tgpf_depositDetailDao.save(tgpf_depositDetail);


                //网银对账明细
                Tgpf_CyberBankStatementDtl statementDtl = new Tgpf_CyberBankStatementDtl();
                statementDtl.setTheState(S_TheState.Normal);
                statementDtl.setBusiState("0");
                statementDtl.setUserStart(user);
                statementDtl.setCreateTimeStamp(current);
                statementDtl.setLastUpdateTimeStamp(current);
                statementDtl.setUserRecord(user);
                statementDtl.setRecordTimeStamp(current);
                statementDtl.setMainTable(cyberBankStatement);
                statementDtl.setTradeTimeStamp(billTimeStamp); //记账时间
                statementDtl.setRecipientAccount(recipientAccount);
                statementDtl.setRecipientName(recipientName);
                statementDtl.setRemark(remark);
                statementDtl.setIncome(amount);
                statementDtl.setReconciliationState(1); //网银对账状态
                statementDtl.setReconciliationStamp(reconciliationTime);//网银对账时间  当前时间
                statementDtl.setUploadTimeStamp(billTimeStamp);
                statementDtl.setSourceType("1"); //数据类型 接口
                statementDtl.setDetailedId(detailedId);
                statementDtl.setTgpf_DepositDetailId(tgpf_depositDetail.getTableId());
                tgpf_cyberBankStatementDtlDao.save(statementDtl);


                Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = new Tgpf_BankUploadDataDetail();
                tgpf_BankUploadDataDetail.setTheState(S_TheState.Normal);//状态 S_TheState 初始为Normal
//                tgpf_BankUploadDataDetail.seteCode(sm_BusinessCodeGetService.execute(BankUpload_BUSI_CODE));//编号
                tgpf_BankUploadDataDetail.setUserStart(user);//创建人
                tgpf_BankUploadDataDetail.setCreateTimeStamp(current);//创建时间
                tgpf_BankUploadDataDetail.setUserUpdate(user);
                tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(current);//最后修改日期
                tgpf_BankUploadDataDetail.setBank(bankAccountEscrowed.getBank());//银行
                tgpf_BankUploadDataDetail.setTheNameOfBank(bankAccountEscrowed.getBank().getTheName());//银行名称
                tgpf_BankUploadDataDetail.setBankBranch(bankAccountEscrowed.getBankBranch());//开户行
//            			tgpf_BankUploadDataDetail.setTheNameOfBankBranch(theNameOfBankBranch);//支行名称
                tgpf_BankUploadDataDetail.setBankAccountEscrowed(tgpf_depositDetail.getBankAccountEscrowed());//托管账号
                tgpf_BankUploadDataDetail.setTheNameOfBankAccountEscrowed(tgpf_depositDetail.getBankAccountEscrowed().getTheNameOfBank());//托管银行名称
                tgpf_BankUploadDataDetail.setTheAccountBankAccountEscrowed(tgpf_depositDetail.getTheNameOfBankAccountEscrowed());//托管账号名称
                tgpf_BankUploadDataDetail.setTheAccountOfBankAccountEscrowed(tgpf_depositDetail.getTheAccountOfBankAccountEscrowed());//托管账户账号
                tgpf_BankUploadDataDetail.setTradeAmount(amount);//交易金额
                tgpf_BankUploadDataDetail.setEnterTimeStamp(billTimeStamp);//入账日期
                tgpf_BankUploadDataDetail.setRecipientAccount(recipientAccount);//对方账号
                tgpf_BankUploadDataDetail.setRecipientName(recipientName);//对方名称
//                tgpf_BankUploadDataDetail.setBkpltNo(accountMsg[3]);//银行平台流水号  //无银行端 暂时不需要
                tgpf_BankUploadDataDetail.seteCodeOfTripleAgreement(tripleAgreement.geteCodeOfTripleAgreement());//三方协议号22
                tgpf_BankUploadDataDetail.setReconciliationState(0);//业务对账状态
                tgpf_BankUploadDataDetail.setBkpltNo(ecodeFromBankPlatform);
                tgpf_BankUploadDataDetail.setReconciliationState(1); //业务对账状态
                tgpf_BankUploadDataDetail.setReconciliationStamp(reconciliationTime);
                tgpf_BankUploadDataDetail.setRemark(remark);
                tgpf_BankUploadDataDetail.setReconciliationUser("TEST");
                tgpf_bankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);
            }

        }

        return properties;
    }


}

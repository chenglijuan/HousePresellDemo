package zhishusz.housepresell.external.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Comm_BankUploadDtlModelDao;
import zhishusz.housepresell.database.dao.Tgpf_BasicAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_BasicAccountVoucherDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Comm_BankUploadDtlModel;
import zhishusz.housepresell.database.po.Tgpf_BasicAccount;
import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucher;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.BankUploadDtlModel;
import zhishusz.housepresell.external.po.BankUploadModel;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;

/**
 * 网银数据推送接收service
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
@Slf4j
public class BankUploadService {

    @Autowired
    private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;// 网银主表
    @Autowired
    private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;// 网银子表
    @Autowired
    private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;// 托管账户
    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// 业务编码
    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表
    @Autowired
    private Tgpf_BasicAccountDao tgpf_BasicAccountDao;// 非基本户凭证
    @Autowired
    private Tgpf_BasicAccountVoucherDao tgpf_BasicAccountVoucherDao;// 基本户凭证
    @Autowired
    private Comm_BankUploadDtlModelDao bankUploadDtlModelDao;

    String busiCode = "200201";

    public synchronized Properties execute(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();

        Long nowDate = System.currentTimeMillis();

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

        tgpf_SocketMsg.setMsgDirection("BANK_UPLOAD_ZT");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(json);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        // String json = request.getParameter("data");
        if (StringUtils.isBlank(json)) {
            return MyBackInfo.fail(properties, "请输入传递参数");
        }

        try {
            BankUploadModel model = JSONObject.parseObject(json, BankUploadModel.class);
            if (null == model) {
                return MyBackInfo.fail(properties, "对象内容为空！");
            }

            List<BankUploadDtlModel> data = model.getData();
            if (null == data || data.size() == 0) {
                return MyBackInfo.fail(properties, "明细对象为空！");
            }

            // 当前时间戳
            long currentTimeMillis = System.currentTimeMillis();
            /*
             * 遍历明细记录，进行模拟网银数据插入 1.查询是否存在托管账户 2.根据交易日期和托管账户查询是否存在主表记录 3.
             */
            Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm;// 托管账户Form
            Integer accountCount = 0;// 托管账户统计

            Tgpf_CyberBankStatementForm cyberBankStatementForm;// 网银主表Form
            String tradeDate = "";// 交易时间
            String dataUpdateDate = "";// 上传时间

            Tgpf_CyberBankStatement cyberBankStatement;// 网银主表
            Tgxy_BankAccountEscrowed bankAccountEscrowed;// 托管账户
            Tgpf_CyberBankStatementDtl cyberBankStatementDtl;// 网银子表
            
            Comm_BankUploadDtlModel commonModel;

            for (BankUploadDtlModel po : data) {
            	
            	commonModel = new Comm_BankUploadDtlModel(po);
            	bankUploadDtlModelDao.save(commonModel);

                Long id = po.getId();
                Date tradeTime = po.getTradeTime();// 交易时间
                if (null == tradeTime) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "交易时间为空！");
                }
                tradeDate = MyDatetime.getInstance().dateToSimpleString(tradeTime);

                Date dataUpdateTime = po.getDataUpdateTime();
                if (null == dataUpdateTime) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "接收时间为空！");
                }
                dataUpdateDate = MyDatetime.getInstance().dateToSimpleString(dataUpdateTime);

                String digest = po.getDigest();

                String accountNo = po.getAccountNo();// 本方账户（托管账户）
                if (StringUtils.isBlank(accountNo)) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "本方账户（托管账户）为空！");
                }

                Double amount = po.getAmount();
                if (null == amount) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "金额为空！");
                }

                /*
                 * 构建凭证数据
                    1、基本户凭证需要主子表，可编辑
                    2、基本户根据 字段accountNo:32050162863609688888（定值） 识别
                    3、非基本户根据 digest 字段识别分类
                 */

                if ("32050162863609688888".equals(accountNo)) {
                    // 基本户凭证
                    Tgpf_BasicAccountVoucher basicAccountVoucher = new Tgpf_BasicAccountVoucher();
                    basicAccountVoucher.setCreateTimeStamp(nowDate);
                    basicAccountVoucher.setLastUpdateTimeStamp(nowDate);
                    basicAccountVoucher.setBillTimeStamp(MyDatetime.getInstance().dateToSimpleString(tradeTime));
                    basicAccountVoucher.setBusiState("0");
                    basicAccountVoucher.setTheState(S_TheState.Normal);
                    basicAccountVoucher.setIsSplit("0");
                    basicAccountVoucher.setSendState("0");

                    List<String> splitStr = splitStr(digest);
                    if (null != splitStr && !splitStr.isEmpty()) {
                        basicAccountVoucher.setRemark(splitStr.get(1));
                        basicAccountVoucher.setSubCode(splitStr.get(0));
                    } else {
                        basicAccountVoucher.setRemark(digest);
                        basicAccountVoucher.setSubCode("");
                        basicAccountVoucher.setTheState(S_TheState.Deleted);
                        basicAccountVoucher.seteCode(digest);
                    }

                    basicAccountVoucher.setTotalTradeAmount(amount);
                    basicAccountVoucher.setTotalTradeAmountSum(amount);
                    basicAccountVoucher.setAccountNumber(accountNo);
                    tgpf_BasicAccountVoucherDao.save(basicAccountVoucher);

                } else if ("手续费".equals(digest) || "结息".equals(digest) || "利息划转".equals(digest)) {
                    /*<span v-if="scope.row.voucherType == '0'">手续费</span>
                    <span v-if="scope.row.voucherType == '1'">结息</span>
                    <span v-if="scope.row.voucherType == '2'">利息划转</span>*/
                    // 非基本户凭证
                    Tgpf_BasicAccount basicAccount = new Tgpf_BasicAccount();
                    basicAccount.setTheState(S_TheState.Normal);
                    basicAccount.setBusiState("0");
                    basicAccount.setCreateTimeStamp(nowDate);
                    basicAccount.setLastUpdateTimeStamp(nowDate);
                    basicAccount.setBillTimeStamp(MyDatetime.getInstance().dateToSimpleString(tradeTime));
                    basicAccount.setRemark(digest);
                    basicAccount.setSendState("0");
                    basicAccount.setSubCode("");
                    basicAccount.setTotalTradeAmount(amount);
                    basicAccount.setVoucherType(digest);
                    basicAccount.setAccountName(po.getBankName());
                    basicAccount.setAccountNumber(accountNo);
                    tgpf_BasicAccountDao.save(basicAccount);

                }

            }

            Tgpf_CyberBankStatementDtlForm dtlModel;
            Integer size;
            for (BankUploadDtlModel po : data) {

                Long id = po.getId();
                
                
                /*
                 * 查询交易Id是否重复推送
                 */
                dtlModel = new Tgpf_CyberBankStatementDtlForm();
                dtlModel.setTheState(S_TheState.Normal);
                dtlModel.setDetailedId(id);
                size = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL3(), dtlModel));
                if(size > 0){
                	continue;
                }
                
                Date tradeTime = po.getTradeTime();// 交易时间
                if (null == tradeTime) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "交易时间为空！");
                }
                tradeDate = MyDatetime.getInstance().dateToSimpleString(tradeTime);

                Date dataUpdateTime = po.getDataUpdateTime();
                if (null == dataUpdateTime) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "接收时间为空！");
                }
                dataUpdateDate = MyDatetime.getInstance().dateToSimpleString(dataUpdateTime);

                String oppaccountNo = po.getOppaccountNo();
                /*
                 * if (StringUtils.isBlank(oppaccountNo)) { return
                 * MyBackInfo.fail(properties, "明细id：" + id + "对方账号为空！"); }
                 */

                String oppaccountName = po.getOppaccountName();
                /*
                 * if (StringUtils.isBlank(oppaccountName)) { return
                 * MyBackInfo.fail(properties, "明细id：" + id + "对方户名为空！"); }
                 */

                String accountNo = po.getAccountNo();// 本方账户（托管账户）
                if (StringUtils.isBlank(accountNo)) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "本方账户（托管账户）为空！");
                }

                Double amount = po.getAmount();
                if (null == amount) {
                    return MyBackInfo.fail(properties, "明细id：" + id + "金额为空！");
                }

                // 根据摘要为“个人贷款”匹配生成网银数据
                if ("个人贷款".equals(po.getDigest())) {
                    // 查询是否存在托管账户
                    bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
                    bankAccountEscrowedForm.setTheAccount(accountNo);// 托管账户
                    bankAccountEscrowedForm.setTheState(S_TheState.Normal); // 状态为正常
                    accountCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao
                        .getQuery_Size(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));
                    if (accountCount > 0) {

                        cyberBankStatementForm = new Tgpf_CyberBankStatementForm();
                        cyberBankStatementForm.setTheState(S_TheState.Normal);
                        cyberBankStatementForm.setTheAccountOfBankAccountEscrowed(accountNo);
                        cyberBankStatementForm.setBillTimeStamp(tradeDate);
                        cyberBankStatement = tgpf_CyberBankStatementDao.findOneByQuery_T(tgpf_CyberBankStatementDao
                            .getQuery(tgpf_CyberBankStatementDao.getBasicHQL(), cyberBankStatementForm));

                        if (null == cyberBankStatement) {
                            // 根据托管账号查询详细信息，构建主表信息
                            bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
                            bankAccountEscrowedForm.setTheAccount(accountNo);// 托管账户
                            bankAccountEscrowedForm.setTheState(S_TheState.Normal); // 状态为正常
                            bankAccountEscrowed =
                                tgxy_BankAccountEscrowedDao.findOneByQuery_T(tgxy_BankAccountEscrowedDao
                                    .getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

                            cyberBankStatement = new Tgpf_CyberBankStatement();
                            // 设置主表属性
                            putBankUpload(currentTimeMillis, tradeDate, cyberBankStatement, bankAccountEscrowed);

                            cyberBankStatement.setUploadTimeStamp(dataUpdateDate);

                            tgpf_CyberBankStatementDao.save(cyberBankStatement);

                            // 构建子表信息
                            cyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
                            // 设置子表属性
                            putBankUploadDtl(currentTimeMillis, tradeDate, cyberBankStatement, cyberBankStatementDtl,
                                po, oppaccountNo, oppaccountName, amount);
                            
                            cyberBankStatementDtl.setDetailedId(id);

                            tgpf_CyberBankStatementDtlDao.save(cyberBankStatementDtl);

                        } else {
                            // 构建子表信息
                            cyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
                            // 设置子表属性
                            putBankUploadDtl(currentTimeMillis, tradeDate, cyberBankStatement, cyberBankStatementDtl,
                                po, oppaccountNo, oppaccountName, amount);
                            cyberBankStatementDtl.setDetailedId(id);

                            tgpf_CyberBankStatementDtlDao.save(cyberBankStatementDtl);
                        }

                    } else {
                        // return MyBackInfo.fail(properties, "本方账号（托管账号）：" + accountNo + "不存在！");
                    }
                }

            }

        } catch (Exception e) {
            tgpf_SocketMsg.setReturnCode("400");// 返回码
            tgpf_SocketMsgDao.update(tgpf_SocketMsg);
            log.error("网银数据推送异常：" + e.getMessage(), e);
            e.printStackTrace();
        }

        return properties;

    }

    /**
     * 设置子表属性
     * 
     * @param currentTimeMillis
     * @param billTimeStamp
     * @param cyberBankStatement
     * @param cyberBankStatementDtl
     * @param po
     * @param oppaccountNo
     * @param oppaccountName
     * @param amount
     */
    private void putBankUploadDtl(long currentTimeMillis, String billTimeStamp,
        Tgpf_CyberBankStatement cyberBankStatement, Tgpf_CyberBankStatementDtl cyberBankStatementDtl,
        BankUploadDtlModel po, String oppaccountNo, String oppaccountName, Double amount) {
        cyberBankStatementDtl.setTheState(S_TheState.Normal);
        cyberBankStatementDtl.setBusiState("0");
        cyberBankStatementDtl.setCreateTimeStamp(currentTimeMillis);
        cyberBankStatementDtl.setLastUpdateTimeStamp(currentTimeMillis);

        cyberBankStatementDtl.setMainTable(cyberBankStatement);// 主表
        cyberBankStatementDtl.setTradeTimeStamp(billTimeStamp);// 交易日期
        cyberBankStatementDtl.setUploadTimeStamp(billTimeStamp);// 文件上传时间

        cyberBankStatementDtl.setReconciliationState(0);// 网银对账状态

        cyberBankStatementDtl.setRecipientAccount(oppaccountNo);// 对方账号
        cyberBankStatementDtl.setRecipientName(oppaccountName);// 对方户名
        cyberBankStatementDtl.setRemark(null == po.getDigest() ? "" : po.getDigest());// 摘要

        cyberBankStatementDtl.setIncome(amount);// 金额
        cyberBankStatementDtl.setBalance(po.getBalance());// 余额

        cyberBankStatementDtl.setSourceType("1");// 数据来源 1-接口
    }

    /**
     * 设置主表属性
     * 
     * @param currentTimeMillis
     * @param billTimeStamp
     * @param cyberBankStatement
     * @param bankAccountEscrowed
     */
    private void putBankUpload(long currentTimeMillis, String billTimeStamp, Tgpf_CyberBankStatement cyberBankStatement,
        Tgxy_BankAccountEscrowed bankAccountEscrowed) {
        cyberBankStatement.setTheState(S_TheState.Normal);
        cyberBankStatement.setBusiState("0");
        //cyberBankStatement.seteCode(sm_BusinessCodeGetService.execute(busiCode));
        cyberBankStatement.seteCode(null == cyberBankStatement.getTableId() ? "" : String.valueOf(cyberBankStatement.getTableId()));
        cyberBankStatement.setCreateTimeStamp(currentTimeMillis);
        cyberBankStatement.setLastUpdateTimeStamp(currentTimeMillis);

        cyberBankStatement.setFileUploadState(1);// 文件上传状态
        cyberBankStatement.setReconciliationState(0);// 网银对账状态

        cyberBankStatement.setTheAccountOfBankAccountEscrowed(bankAccountEscrowed.getTheAccount());// 托管账号
        cyberBankStatement.setTheNameOfBankAccountEscrowed(bankAccountEscrowed.getTheName());// 托管账号名称
        cyberBankStatement.setTheNameOfBankBranch(bankAccountEscrowed.getBankBranch().getTheName());// 开户行名称
        cyberBankStatement.setTheNameOfBank(bankAccountEscrowed.getBank().getTheName());// 银行名称

        cyberBankStatement.setBillTimeStamp(billTimeStamp);
    }

    /**
     * 提取字符串中的数字
     * 
     * @param str
     * @return
     */
    private String outNumber(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private List<String> splitStr(String s) {

        List<String> strList = new ArrayList<String>();

        for (int index = 0; index <= s.length() - 1; index++) {
            // 将字符串拆开成单个的字符
            String w = s.substring(index, index + 1);
            // \u4e00-\u9fa5 中文汉字的范围
            if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {
                String sub1 = s.substring(0, index);
                String sub2 = s.substring(index, s.length());

                strList.add(sub1);
                strList.add(sub2);

                return strList;
            }
        }
        return strList;
    }

}

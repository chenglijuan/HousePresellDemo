package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TripleagreementState;
import zhishusz.housepresell.external.service.Tgxy_TripleAgreementApprovalProcessInterfaceService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：三方协议 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_TripleAgreementApprovalProcessService {
    @Autowired
    private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Tgxy_TripleAgreementApprovalProcessInterfaceService approvalProcessInterfaceService;

    private static Logger log = Logger.getLogger(Tgxy_TripleAgreementApprovalProcessService.class);

    public Properties execute(Tgxy_TripleAgreementForm model) {
        Properties properties = new MyProperties();
        
        log.info("tijiaosanfangxieyi======111======start=========" + System.currentTimeMillis());

        // 1： 保存按钮 2：提交按钮
        String buttonType = model.getButtonType();
        if (null == buttonType || buttonType.trim().isEmpty()) {
            buttonType = "2";
        }
        Long tableId = model.getTableId();
        Sm_User user = model.getUser();

        model.setButtonType(buttonType);

        if (tableId == null || tableId < 1) {
            return MyBackInfo.fail(properties, "请选择有效的三方协议");
        }

        Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(tableId);
        if (null == tripleAgreement) {
            return MyBackInfo.fail(properties, "未查询到有效的三方协议");
        }

        log.info("tijiaosanfangxieyi======111======end=========" + System.currentTimeMillis());        

        log.info("tijiaosanfangxieyi============start=========" + System.currentTimeMillis());
        Properties execute = approvalProcessInterfaceService.execute(tripleAgreement, model);
        if (execute.isEmpty() || S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result))) {
            return MyBackInfo.fail(properties, "提交失败，请稍后重试！");
        }
        
        // 审批流程状态-审核中
        tripleAgreement.setApprovalState(S_ApprovalState.Examining);
        tripleAgreement.setUserUpdate(user);
        tripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
        // 提交状态置为已提交，控制重复提交
        tripleAgreement.setBusiState(S_TripleagreementState.IsCommit);
        tgxy_TripleAgreementDao.update(tripleAgreement);

        log.info("tijiaosanfangxieyi============end=========" + System.currentTimeMillis());

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;

    }
    
    
    public Properties execute_copy(Tgxy_TripleAgreementForm model) {
        Properties properties = new MyProperties();
        
        log.info("tijiaosanfangxieyi======111======start=========" + System.currentTimeMillis());

        // 1： 保存按钮 2：提交按钮
        String buttonType = model.getButtonType();
        if (null == buttonType || buttonType.trim().isEmpty()) {
            buttonType = "2";
        }

        String busiCode = model.getBusiCode();
        Long tableId = model.getTableId();
        Sm_User user = model.getUser();

        model.setButtonType(buttonType);

        if (busiCode == null || busiCode.length() == 0) {
            return MyBackInfo.fail(properties, "'业务编码'不能为空");
        }

        if (tableId == null || tableId < 1) {
            return MyBackInfo.fail(properties, "请选择有效的三方协议");
        }

        Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(tableId);
        if (null == tripleAgreement) {
            return MyBackInfo.fail(properties, "未查询到有效的三方协议");
        }

        /*
         * xsz by time 2018-11-13 15:23:33
         * 根据审批状态判断是否提交
         */
        if (S_ApprovalState.Examining.equals(tripleAgreement.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
        } else if (S_ApprovalState.Completed.equals(tripleAgreement.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
        }
        
        log.info("tijiaosanfangxieyi======111======end=========" + System.currentTimeMillis());        
//        properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
        if ("noApproval".equals(properties.getProperty("info"))) {
            tripleAgreement.setTheStateOfTripleAgreement("3");
            // 审批流程状态-已完结
            tripleAgreement.setApprovalState(S_ApprovalState.Completed);
        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            // 判断当前登录用户是否有权限发起审批
            return properties;
        } else {
            /**
             * xsz by time 2019-7-8 14:13:24 将提交操作更改为与档案系统审批对接 ========================start===========================
             */
            log.info("tijiaosanfangxieyi============start=========" + System.currentTimeMillis());
            Properties execute = approvalProcessInterfaceService.execute(tripleAgreement, model);
            if (execute.isEmpty() || S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result))) {
                return MyBackInfo.fail(properties, "提交失败，请稍后重试！");
            }
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
                (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");

            // 审批操作
//            sm_approvalProcessService.execute(tripleAgreement, model, sm_approvalProcess_cfg);
            // 审批流程状态-审核中
            tripleAgreement.setApprovalState(S_ApprovalState.Examining);
            /**
             * xsz by time 2019-7-8 14:13:24 将提交操作更改为与档案系统审批对接 ========================end===========================
             */
        }

        tripleAgreement.setUserUpdate(user);
        tripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
        // 提交状态置为已提交，控制重复提交
        tripleAgreement.setBusiState(S_TripleagreementState.IsCommit);
        tgxy_TripleAgreementDao.save(tripleAgreement);

        log.info("tijiaosanfangxieyi============end=========" + System.currentTimeMillis());

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;

    }
}

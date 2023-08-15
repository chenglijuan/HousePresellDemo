package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.List;
import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：申请-用款-主表 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFDetailService {
    @Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
    @Autowired
    private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;

    @Autowired
    private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;

    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Tgpf_FundAppropriated_AFForm model) {
        Properties properties = new MyProperties();

        Long tgpf_FundAppropriated_AFId = model.getTableId();
        Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF =
            (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(tgpf_FundAppropriated_AFId);
        if (tgpf_FundAppropriated_AF == null) {
            return MyBackInfo.fail(properties, "'用款申请单(Id:" + tgpf_FundAppropriated_AFId + ")'不存在");
        }

        // 楼幢信息
        List<Tgpf_FundAppropriated_AFDtl> tgpf_fundAppropriated_afDtlList;
        if (tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList() != null) {
            tgpf_fundAppropriated_afDtlList = tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList();

            Tgpf_FundAppropriated_AFDtlForm fundAppropriated_afDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
            fundAppropriated_afDtlForm.setTheState(S_TheState.Normal);
            fundAppropriated_afDtlForm.setAfId(tgpf_FundAppropriated_AFId);
            tgpf_fundAppropriated_afDtlList = tgpf_FundAppropriated_AFDtlDao.findByPage(tgpf_FundAppropriated_AFDtlDao
                .getQuery(tgpf_FundAppropriated_AFDtlDao.getBuildListSortHQL(), fundAppropriated_afDtlForm));

        } else {
            tgpf_fundAppropriated_afDtlList = new ArrayList<>();
        }

        // 用款申请汇总信息
        List<Tgpf_FundOverallPlanDetail> tgpf_fundOverallPlanDetailList;
        if (tgpf_FundAppropriated_AF.getFundOverallPlanDetailList() != null) {
            tgpf_fundOverallPlanDetailList = tgpf_FundAppropriated_AF.getFundOverallPlanDetailList();
        } else {
            tgpf_fundOverallPlanDetailList = new ArrayList<>();
        }

        // --------------------审批---------------------------------------//
        Long afId = model.getAfId();// 申请单Id
        Sm_ApprovalProcess_AF sm_approvalProcess_af;
        Boolean isNeedBackup = null;
        if (afId != null && afId > 0) {
            sm_approvalProcess_af = sm_approvalProcess_afDao.findById(afId);

            if (sm_approvalProcess_af == null || S_TheState.Deleted.equals(sm_approvalProcess_af.getTheState())) {
                return MyBackInfo.fail(properties, "'申请单'不存在");
            }
            Long currentNode = sm_approvalProcess_af.getCurrentIndex();
            Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow =
                sm_approvalProcess_workflowDao.findById(currentNode);
            if (sm_approvalProcess_workflow.getNextWorkFlow() == null) {
                if (sm_approvalProcess_af.getIsNeedBackup().equals(1)) {
                    isNeedBackup = true;
                }
            } else {
                isNeedBackup = false;
            }
        }
        // --------------------审批---------------------------------------//

        properties.put("tgpf_FundAppropriated_AF", tgpf_FundAppropriated_AF);
        properties.put("tgpf_fundAppropriated_afDtlList", tgpf_fundAppropriated_afDtlList);
        properties.put("tgpf_fundOverallPlanDetailList", tgpf_fundOverallPlanDetailList);
        properties.put("isNeedBackup", isNeedBackup);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}

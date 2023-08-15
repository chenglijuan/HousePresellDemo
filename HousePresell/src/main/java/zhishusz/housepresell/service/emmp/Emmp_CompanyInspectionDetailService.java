package zhishusz.housepresell.service.emmp;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：无人机巡查机构 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Emmp_CompanyInspectionDetailService {
    @Autowired
    private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

    @Autowired
    private Emmp_OrgMemberDao emmp_OrgMemberDao;

    @Autowired
    private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;

    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Emmp_CompanyInfoForm model) {
        Properties properties = new MyProperties();

        Long emmp_CompanyInfoId = model.getTableId();
        Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(emmp_CompanyInfoId);
        if (emmp_CompanyInfo == null) {
            return MyBackInfo.fail(properties, "无人机巡查机构不存在");
        }

        Emmp_OrgMemberForm model2 = new Emmp_OrgMemberForm();
        model2.setCompanyId(emmp_CompanyInfoId);
        model2.setTheState(S_TheState.Normal);
        List<Emmp_OrgMember> emmp_OrgMemberList = emmp_OrgMemberDao
            .findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), model2), null, null);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        properties.put("emmp_CompanyInfo", emmp_CompanyInfo);
        properties.put("emmp_OrgMemberList", emmp_OrgMemberList);

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
        properties.put("isNeedBackup", isNeedBackup);
        // --------------------审批---------------------------------------//

        return properties;
    }
}

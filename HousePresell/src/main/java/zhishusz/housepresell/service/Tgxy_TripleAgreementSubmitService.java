package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_CfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Sm_BusinessRecordForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_CfgDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_BusinessRecordDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Condition;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_WorkflowCondition;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_BusinessRecord;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_TripleagreementState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.external.po.TripleAgreementModel;
import zhishusz.housepresell.util.ApproveUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.SocketUtil;

/*
 * Service详情：三方协议 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_TripleAgreementSubmitService {
    @Autowired
    private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
    // 附件
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;
    // 接口报文表
    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

    // =================审批验证相关===========
    @Autowired
    private Sm_ApprovalProcess_CfgDao sm_ApprovalProcess_CfgDao;
    @Autowired
    private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
    @Autowired
    private Sm_UserDao sm_userDao;

    // =================审批提交相关===========
    @Autowired
    private Sm_ApprovalProcess_ApplyService sm_ApprovalProcess_applyService;
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;

    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;

    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
    @Autowired
    private Sm_BusinessRecordDao sm_BusinessRecordDao;

    private static Logger log = Logger.getLogger(Tgxy_TripleAgreementSubmitService.class);

    @SuppressWarnings({"unchecked", "static-access"})
    public Properties execute(Tgxy_TripleAgreementForm model) {
        Properties properties = new MyProperties();

        log.info("tijiaosanfangxieyi============start=========" + System.currentTimeMillis());

        String busiCode = model.getBusiCode();

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

        if (S_ApprovalState.Examining.equals(tripleAgreement.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
        } else if (S_ApprovalState.Completed.equals(tripleAgreement.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
        }

        properties = verificationExecute(busiCode, model.getUserId());
        if ("noApproval".equals(properties.getProperty("info"))) {
            tripleAgreement.setTheStateOfTripleAgreement("3");
            // 审批流程状态-已完结
            tripleAgreement.setApprovalState(S_ApprovalState.Completed);
        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            // 判断当前登录用户是否有权限发起审批
            return properties;
        } else {

            // 三方协议推送==================START
            Empj_ProjectInfo project = tripleAgreement.getProject();
            Emmp_CompanyInfo developCompany = project.getDevelopCompany();
            Empj_HouseInfo house = tripleAgreement.getHouse();

            log.info("推送SFXY附件信息START：" + System.currentTimeMillis());
            System.out.println("推送SFXY附件信息START：" + System.currentTimeMillis());

            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
            tgpf_SocketMsg.setTheState(S_TheState.Normal);
            tgpf_SocketMsg.setUserStart(model.getUser());
            tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
            tgpf_SocketMsg.setUserUpdate(model.getUser());
            tgpf_SocketMsg.setMsgStatus(1);
            tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
            tgpf_SocketMsg.setMsgDirection("DAOUT");
            tgpf_SocketMsgDao.save(tgpf_SocketMsg);

            /*
             * 单据附件信息
             */
            Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
            sm_AttachmentForm.setSourceId(String.valueOf(tripleAgreement.getTableId()));
            sm_AttachmentForm.setBusiType("06110301");
            sm_AttachmentForm.setTheState(S_TheState.Normal);

            // 加载所有相关附件信息
            List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

            /*
             * 除“买卖合同签字页”的附件，其他附件拼接推送
             */
            StringBuffer sb = new StringBuffer();
            for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
                if (null != sm_Attachment.getAttachmentCfg() && null != sm_Attachment.getAttachmentCfg().getTheName()) {
                    sb.append(sm_Attachment.getAttachmentCfg().getTheName() + "#" + sm_Attachment.getTheLink() + ",");
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }

            log.info("推送SFXY附件信息END：" + System.currentTimeMillis());
            System.out.println("推送SFXY附件信息END：" + System.currentTimeMillis());

            TripleAgreementModel modelVo = new TripleAgreementModel();
            modelVo.setHtbh(tripleAgreement.geteCodeOfContractRecord());
            modelVo.setXybh(tripleAgreement.geteCodeOfTripleAgreement());
            modelVo.setQymc(developCompany.getTheName());
            modelVo.setXmmc(project.getTheName());
            modelVo.setSgzl(house.getPosition());
            modelVo.setJzmj(house.getActualArea().toString());
            modelVo.setFjm(sb.toString());
            modelVo.setMsrxm(tripleAgreement.getBuyerName());

            /*
             *  参数名  类型 描述  Int  
             *  返回值“1”表示数据插入成功
             *  返回值“0”表示数据插入异常
             */
            String query = modelVo.toStringAdd();

            Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
            paraModel.setParametertype("70");
            paraModel.setTheValue("700001");
            List<Sm_BaseParameter> list = new ArrayList<>();
            list = sm_BaseParameterDao
                .findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));

            if (list.isEmpty()) {
                return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
            }

            String url = list.get(0).getTheName();

            log.info("推送SFXY========START：" + System.currentTimeMillis());
            System.out.println("推送SFXY========START：" + System.currentTimeMillis());
            // 正式接口请求
            int restFul = SocketUtil.getInstance().getRestFul(url, query);
//            int restFul = 1;
            log.info("推送SFXY========END：" + System.currentTimeMillis());
            System.out.println("推送SFXY========END：" + System.currentTimeMillis());

            tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
            tgpf_SocketMsg.setRemark(url);
            tgpf_SocketMsg.setMsgContentArchives(query);
            tgpf_SocketMsg.setReturnCode(String.valueOf(restFul));
            tgpf_SocketMsgDao.update(tgpf_SocketMsg);

            log.info("query:" + query);

            if (restFul == 0) {
                return MyBackInfo.fail(properties, "推送失败，请检查后再试！");
            }

            // 三方协议推送==================START
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
                (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");

            // 审批操作
            approvalExecute(tripleAgreement, model, sm_approvalProcess_cfg);
            // 审批流程状态-审核中
            tripleAgreement.setApprovalState(S_ApprovalState.Examining);
            // 提交状态置为已提交，控制重复提交
            tripleAgreement.setBusiState(S_TripleagreementState.IsCommit);
        }

        tripleAgreement.setUserUpdate(user);
        tripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

        tgxy_TripleAgreementDao.update(tripleAgreement);

        log.info("tijiaosanfangxieyi============end=========" + System.currentTimeMillis());

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;

    }

    /**
     * 验证用户是否具备提交权限
     * 
     * @param busiCode
     * @param userStartId
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties verificationExecute(String busiCode, Long userStartId) {
        Properties properties = new MyProperties();

        Sm_User userStart = sm_userDao.findById(userStartId);
        if (userStart == null) {
            return MyBackInfo.fail(properties, "登录用户不能为空");
        }

        if (userStart.getCompany() == null) {
            return MyBackInfo.fail(properties, "用户所属机构不能为空");
        }
        Emmp_CompanyInfo emmp_companyInfo = userStart.getCompany();

        if (emmp_companyInfo.getTheName() == null || emmp_companyInfo.getTheName().length() == 0) {
            return MyBackInfo.fail(properties, "机构名称不能为空");
        }

        Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
        roleUserForm.setTheState(S_TheState.Normal);
        roleUserForm.setSm_UserId(userStartId);
        List<Sm_Permission_RoleUser> sm_permission_roleUserList = sm_permission_roleUserDao
            .findByPage(sm_permission_roleUserDao.getQuery(sm_permission_roleUserDao.getBasicHQL(), roleUserForm));

        Sm_ApprovalProcess_CfgForm cfgFormModel = new Sm_ApprovalProcess_CfgForm();
        cfgFormModel.setTheState(S_TheState.Normal);
        cfgFormModel.setBusiCode(busiCode);
        List<Sm_ApprovalProcess_Cfg> sm_ApprovalProcess_CfgList = sm_ApprovalProcess_CfgDao
            .findByPage(sm_ApprovalProcess_CfgDao.getQuery(sm_ApprovalProcess_CfgDao.getBasicHQL(), cfgFormModel));

        if (sm_ApprovalProcess_CfgList == null || sm_ApprovalProcess_CfgList.isEmpty()) {
            properties.put(S_NormalFlag.info, "noApproval");
            return properties;
        }

        List<Sm_ApprovalProcess_Cfg> sm_approvalProcess_cfgList2 = new ArrayList<>();

        for (Sm_Permission_RoleUser sm_Permission_RoleUser : sm_permission_roleUserList) {
            if (sm_Permission_RoleUser.getSm_Permission_Role() == null) {
                return MyBackInfo.fail(properties, "角色不能为空");
            }
            Sm_Permission_Role sm_permission_role1 = sm_Permission_RoleUser.getSm_Permission_Role(); // 登录用户角色
            Long userRoleId = sm_permission_role1.getTableId(); // 登录用户角色Id
            List<Long> cfgRoleIdList = new ArrayList<Long>();// 流程配置第一个节点（发起人节点）角色ID列表

            int count = 0;
            for (Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg : sm_ApprovalProcess_CfgList) {
                if (sm_ApprovalProcess_Cfg.getNodeList() == null) {
                    return MyBackInfo.fail(properties, "审批流程结点信息不能为空");
                }
                Sm_ApprovalProcess_Node sm_approvalProcess_node = sm_ApprovalProcess_Cfg.getNodeList().get(0);
                if (sm_approvalProcess_node.getRole() == null) {
                    return MyBackInfo.fail(properties, "审批结点角色不能为空");
                }

                Sm_Permission_Role sm_permission_role2 = sm_approvalProcess_node.getRole(); // 流程配置第一个结点 角色信息
                Long cfgRoleId = sm_permission_role2.getTableId(); // 流程配置第一个结点 角色Id
                cfgRoleIdList.add(cfgRoleId);
                if (userRoleId.equals(cfgRoleId)) {
                    count++;
                }
            }
            if (count == 1) {
                sm_approvalProcess_cfgList2.add(sm_ApprovalProcess_CfgList.get(cfgRoleIdList.indexOf(userRoleId)));
            }
        }

        if (sm_approvalProcess_cfgList2.isEmpty()) {
            return MyBackInfo.fail(properties, "没有权限发起审批");
        }
        if (sm_approvalProcess_cfgList2.size() > 1) {
            return MyBackInfo.fail(properties, "发现" + sm_approvalProcess_cfgList2.size() + "个审批流程");
        }

        Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_approvalProcess_cfgList2.get(0);

        if (sm_approvalProcess_cfg.getBusiType() == null || sm_approvalProcess_cfg.getBusiType().length() == 0) {
            return MyBackInfo.fail(properties, "业务类型不能为空");
        }

        properties.put("sm_approvalProcess_cfg", sm_approvalProcess_cfg);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    public Properties approvalExecute(IApprovable iApprovable, BaseForm baseForm,
        Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg) {
        Properties properties = new MyProperties();

        Long sourceId = iApprovable.getSourceId();
        String busiCode = sm_approvalProcess_cfg.getBusiCode();
        String buttonType = baseForm.getButtonType();
        Long loginUserId = baseForm.getUserId();
        Sm_User loginUser = baseForm.getUser();

        // 可以变更的属性列表
        List<String> peddingApprovalKeyList = iApprovable.getPeddingApprovalkey();
        String orgObjJson = null;
        String expectObjJson = null;
        if (peddingApprovalKeyList != null && !peddingApprovalKeyList.isEmpty()) {
            ApproveUtil approveUtil = new ApproveUtil();
            // 修改前的数据这里有个缺陷就是非本表的字段上传不到OSS
            orgObjJson = approveUtil.getJsonData(iApprovable, peddingApprovalKeyList);
            // 修改后的数据
            expectObjJson = approveUtil.getJsonData(baseForm, peddingApprovalKeyList);
        }

        // 查找申请单
        Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
        sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
        sm_approvalProcess_afForm.setBusiCode(busiCode);
        sm_approvalProcess_afForm.setSourceId(sourceId);
        sm_approvalProcess_afForm.setBusiState(S_ApprovalState.WaitSubmit);

        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(
            sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));

        if (sm_ApprovalProcess_AF != null && !"06120501".equals(busiCode)) {
            String orgObjJsonFilePath = null;
            String expectObjJsonFilePath = null;

            Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_ApprovalProcess_AF.getWorkFlowList().get(0);

            // --------------------------------------------------------生成发起记录start-----------------------------------//
            List<Sm_ApprovalProcess_Record> recordList = new ArrayList<>();
            Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = new Sm_ApprovalProcess_Record();
            sm_ApprovalProcess_Record.setTheState(S_TheState.Normal);
            sm_ApprovalProcess_Record.setConfiguration(sm_approvalProcess_cfg); // 流程配置
            sm_ApprovalProcess_Record.setCreateTimeStamp(System.currentTimeMillis());
            sm_ApprovalProcess_Record.setLastUpdateTimeStamp(System.currentTimeMillis());
            sm_ApprovalProcess_Record.setUserStart(loginUser);
            sm_ApprovalProcess_Record.setUserUpdate(loginUser);
            sm_ApprovalProcess_Record.setUserOperate(loginUser); // 发起人
            sm_ApprovalProcess_Record.setOperateTimeStamp(System.currentTimeMillis()); // 操作时间点

            if (sm_approvalProcess_workflow.getApprovalProcess_recordList() != null
                && sm_approvalProcess_workflow.getApprovalProcess_recordList().size() > 0) {
                sm_approvalProcess_workflow.getApprovalProcess_recordList().add(sm_ApprovalProcess_Record);
            } else {
                sm_approvalProcess_workflow.setApprovalProcess_recordList(recordList);
                recordList.add(sm_ApprovalProcess_Record);
            }
            // --------------------------------------------------------生成发起记录end-------------------------------------//

            sm_ApprovalProcess_AF.setOrgObjJsonFilePath(orgObjJsonFilePath);
            sm_ApprovalProcess_AF.setExpectObjJsonFilePath(expectObjJsonFilePath);
            sm_ApprovalProcess_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
            sm_ApprovalProcess_AF.setStartTimeStamp(System.currentTimeMillis());
            sm_ApprovalProcess_AF.setUserStart(loginUser);
            sm_ApprovalProcess_AF.setApplicant(loginUser.getTheName());
            sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);
        } else {
            Properties applyProperties = sm_ApprovalProcess_applyService.execute(iApprovable, null, null,
                loginUserId, buttonType, sm_approvalProcess_cfg);
            if (applyProperties.getProperty("result").equals("fail")) {
                return applyProperties;
            }
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     *
     * @param modelPo
     *            审批对象
     * @param orgObjJson
     *            原数据
     * @param expectObjJson
     *            修改后数据
     * @param userStartId
     *            登录用户ID
     * @param buttonType
     *            按钮来源
     * @param sm_approvalProcess_cfg
     *            审批对象与登录用户匹配成功的审批流程
     * @return
     */
    public Properties execute(IApprovable modelPo, String orgObjJson, String expectObjJson, Long userStartId,
        String buttonType, Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg, Sm_User loginUser) {
        Properties properties = new MyProperties();

        String busiCode_01030203 = S_BusiCode.busiCode_01030203; // 我发起的业务编码
        String loginUserName = loginUser.getTheName();// 申请人
        Emmp_CompanyInfo emmp_companyInfo = loginUser.getCompany(); // 登录用户所属机构
        String theNameOfCompanyInfo = emmp_companyInfo.getTheName();// 机构名称
        String busiCode = sm_approvalProcess_cfg.getBusiCode();
        String busiType = sm_approvalProcess_cfg.getBusiType();

        // 修改前数据文件路径
        String orgObjJsonFilePath = null;
        // 修改后数据文件路径
        String expectObjJsonFilePath = null;

        // 1.通过配置信息(cfg)得到结点
        List<Sm_ApprovalProcess_Node> nodeList = sm_approvalProcess_cfg.getNodeList();// 将要走的审批流程的节点列表

        Sm_Permission_Role cfgRole = nodeList.get(0).getRole();// 第一个节点（发起人）的角色

        // 2.通过结点创建审批流程
        List<Sm_ApprovalProcess_Workflow> workflowList = new ArrayList<>();
        for (int i = 0; i < nodeList.size(); i++) {
            Sm_ApprovalProcess_Node sm_approvalProcess_node = nodeList.get(i);
            Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow =
                new Sm_ApprovalProcess_Workflow(sm_approvalProcess_node);
            sm_approvalProcess_workflowDao.save(sm_ApprovalProcess_Workflow);

            // --------------------------------------结点
            // ====》消息模板start---------------------------------------------//
            if (sm_approvalProcess_node.getSm_messageTemplate_cfgList() != null
                && sm_approvalProcess_node.getSm_messageTemplate_cfgList().size() > 0) {
                List<Sm_MessageTemplate_Cfg> sm_messageTemplate_cfgs = new ArrayList<>();
                sm_messageTemplate_cfgs.addAll(sm_approvalProcess_node.getSm_messageTemplate_cfgList());
                sm_ApprovalProcess_Workflow.setSm_messageTemplate_cfgList(sm_messageTemplate_cfgs);
            }
            // --------------------------------------结点
            // ====》消息模板end---------------------------------------------//
            workflowList.add(sm_ApprovalProcess_Workflow);

            // --------------------------------------结点与结点之间关联start--------------------------------------------//
            if (i > 0) {
                Sm_ApprovalProcess_Workflow preWorkflow = workflowList.get(i - 1);
                preWorkflow.setNextWorkFlow(sm_ApprovalProcess_Workflow);
                sm_ApprovalProcess_Workflow.setLastWorkFlow(preWorkflow);
            }
            // --------------------------------------结点与结点之间关联end---------------------------------------------//
        }
        // -------------------------------------------结点
        // ====》条件start---------------------------------------------//
        for (int nodeIndex = 0; nodeIndex < nodeList.size(); nodeIndex++) {
            Sm_ApprovalProcess_Node sm_approvalProcess_node = nodeList.get(nodeIndex);
            Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = workflowList.get(nodeIndex);
            List<Sm_ApprovalProcess_WorkflowCondition> workflowConditionList = new ArrayList<>();
            if (sm_approvalProcess_node.getApprovalProcess_conditionList() != null
                && sm_approvalProcess_node.getApprovalProcess_conditionList().size() > 0) {
                for (Sm_ApprovalProcess_Condition sm_approvalProcess_condition : sm_approvalProcess_node
                    .getApprovalProcess_conditionList()) {
                    Long getNextStepWorkflowId =
                        getNextStepWorkflowId(sm_approvalProcess_condition, nodeList, workflowList);

                    Sm_ApprovalProcess_WorkflowCondition workflowCondition = new Sm_ApprovalProcess_WorkflowCondition();
                    workflowCondition.setTheState(S_TheState.Normal);
                    workflowCondition.setTheContent(sm_approvalProcess_condition.getTheContent());
                    workflowCondition.setNextStep(getNextStepWorkflowId);
                    workflowCondition.setUserStart(loginUser);
                    workflowCondition.setCreateTimeStamp(System.currentTimeMillis());
                    workflowConditionList.add(workflowCondition);
                }
                sm_approvalProcess_workflow.setWorkflowConditionList(workflowConditionList);
            }
        }
        // -------------------------------------------------结点====》条件end------------------------------------------//

        // 提交按钮 设置第一个结点状态为通过
        if (S_ButtonType.Submit.equals(buttonType)) {
            workflowList.get(0).setBusiState(S_WorkflowBusiState.Pass);// 发起结点

            if (workflowList.size() > 1) {
                workflowList.get(1).setBusiState(S_WorkflowBusiState.Examining);
            }
        }

        // 3.创建申请单
        Sm_ApprovalProcess_AF af = new Sm_ApprovalProcess_AF();
        af.setApplicant(loginUserName); // 申请人
        af.setTheNameOfCompanyInfo(theNameOfCompanyInfo); // 申请机构
        af.setConfiguration(sm_approvalProcess_cfg); // 流程配置
        af.setIsNeedBackup(sm_approvalProcess_cfg.getIsNeedBackup());// 是否备案
        af.setCompanyInfo(emmp_companyInfo); // 归属企业
        af.seteCode(sm_BusinessCodeGetService.execute(busiCode_01030203)); // 业务编码
        af.setTheState(S_TheState.Normal);
        af.setPermissionRole(cfgRole);
        af.setBusiCode(busiCode);// 业务编码
        af.setBusiType(busiType);// 业务类型

        /*
        * xsz by time 2019-3-27 15:40:05 根据业务类型添加不同的主题
        */
        Long sourceId = modelPo.getSourceId();// 单据主键
        // 三方协议签署
        Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(sourceId);
        if (null != tripleAgreement) {
            af.setTheme(tripleAgreement.getProject().getCityRegion().getTheName() + " 坐落："
                + tripleAgreement.getProject().getTheName() + " "
                + tripleAgreement.getBuildingInfo().geteCodeFromConstruction() + " " + tripleAgreement.getUnitRoom()
                + "户 协议编号：" + tripleAgreement.geteCodeOfTripleAgreement() + " 买受人：" + tripleAgreement.getBuyerName());
        } else {
            af.setTheme(theNameOfCompanyInfo + " " + busiType);
        }

        // 根据按钮类型 设置流程状态 1 保存按钮 ： 待提交 ； 2 提交按钮 ： 审核中
        af.setBusiState(S_ApprovalState.Examining); // 流程状态 ：审核中
        af.setSourceId(modelPo.getSourceId());
        af.setSourceType(modelPo.getSourceType());
        af.setOrgObjJsonFilePath(orgObjJsonFilePath);
        af.setExpectObjJsonFilePath(expectObjJsonFilePath);
        af.setCurrentIndex(workflowList.get(1).getTableId());
        af.setWorkFlowList(workflowList);
        af.setUserStart(loginUser);
        af.setStartTimeStamp(System.currentTimeMillis());
        af.setCreateTimeStamp(System.currentTimeMillis());
        sm_ApprovalProcess_AFDao.save(af);

        // 第一个结点
        Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = workflowList.get(0);

        // --------------------------------------------------------生成发起记录start-----------------------------------//
        List<Sm_ApprovalProcess_Record> recordList = new ArrayList<>();
        Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = new Sm_ApprovalProcess_Record();
        sm_ApprovalProcess_Record.setTheState(S_TheState.Normal);
        sm_ApprovalProcess_Record.setConfiguration(sm_approvalProcess_cfg); // 流程配置
        sm_ApprovalProcess_Record.setCreateTimeStamp(System.currentTimeMillis());
        sm_ApprovalProcess_Record.setLastUpdateTimeStamp(System.currentTimeMillis());
        sm_ApprovalProcess_Record.setUserStart(loginUser);
        sm_ApprovalProcess_Record.setUserUpdate(loginUser);
        sm_ApprovalProcess_Record.setUserOperate(loginUser); // 发起人
        sm_ApprovalProcess_Record.setOperateTimeStamp(System.currentTimeMillis()); // 操作时间点

        recordList.add(sm_ApprovalProcess_Record);
        sm_approvalProcess_workflow.setApprovalProcess_recordList(recordList);
        sm_approvalProcess_workflowDao.save(sm_approvalProcess_workflow);
        // --------------------------------------------------------生成发起记录end-------------------------------------//

        // -------------------------------------------------------业务关联记录表start----------------------------------//
        BaseForm baseForm = new BaseForm();
        baseForm.setUserId(userStartId);
        baseForm.setUser(loginUser);
        properties = addBusinessRecord(busiCode, modelPo.getEcodeOfBusiness(), af.getTableId(), baseForm);
        if (!MyBackInfo.isSuccess(properties)) {
            throw new RoolBackException(MyString.getInstance().parse(properties.get(S_NormalFlag.info)));
        }
        // -------------------------------------------------------业务关联记录表end------------------------------------//

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    public Properties addBusinessRecord(String busiCode, String codeOfBusiness, Long afTableId, BaseForm form) {

        Properties properties = new MyProperties();

        try {
            if (null == busiCode || busiCode.trim().isEmpty()) {
                return MyBackInfo.fail(properties, "业务编码不能为空");
            }

            if (null == codeOfBusiness || codeOfBusiness.trim().isEmpty()) {
                return MyBackInfo.fail(properties, "业务编码不能为空");
            }

            // 和申请单关联
            if (null == afTableId || afTableId <= 0) {
                return MyBackInfo.fail(properties, "申请单ID不能为空");
            }

            // 查询审批申请单
            Sm_ApprovalProcess_AF approvalProcess_AF = sm_ApprovalProcess_AFDao.findById(afTableId);
            if (null == approvalProcess_AF) {
                return MyBackInfo.fail(properties, "未查询到申请单信息");
            }

            /*
             * 先判断业务单号是否在业务关联表中存在
             * 
             */
            Sm_BusinessRecordForm bmodel = new Sm_BusinessRecordForm();
            bmodel.setTheState(S_TheState.Normal);
            bmodel.setBusiCode(busiCode);
            bmodel.setCodeOfBusiness(codeOfBusiness);
            bmodel.setApprovalProcess_AF(approvalProcess_AF);
            Integer count = sm_BusinessRecordDao
                .findByPage_Size(sm_BusinessRecordDao.getQuery_Size(sm_BusinessRecordDao.getBasicHQL(), bmodel));

            if (count > 0) {
                return MyBackInfo.fail(properties, "业务单号已经存在！");
            }

            // 业务开始（对具体的业务开始处理）
            // 贷款托管三方协议
            properties = getTripleAgreement(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

        } catch (Exception e) {
            // 异常返回信息
            properties.put(S_NormalFlag.debugInfo, e.getMessage());
            properties.put(S_NormalFlag.result, S_NormalFlag.fail);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);

        }

        return properties;

    }

    // 贷款三方协议
    private Properties getTripleAgreement(String busiCode, String codeOfBusiness,
        Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties) {
        // 查询对应的业务
        Tgxy_TripleAgreementForm model = new Tgxy_TripleAgreementForm();
        model.setTheState(S_TheState.Normal);
        model.seteCode(codeOfBusiness);

        Tgxy_TripleAgreement tgxy_TripleAgreement;
        tgxy_TripleAgreement = tgxy_TripleAgreementDao
            .findOneByQuery_T(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), model));
        if (null == tgxy_TripleAgreement) {
            return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
        }

        /*
         * 三方协议关联信息：
         * 关联开发企业-开发企业编号
         * 关联项目-项目编号
         * 关联区域-区域编号-区域名称
         * 关联楼幢-楼幢编号
         * 
         */
        Emmp_CompanyInfo company;
        Empj_ProjectInfo project;
        Sm_CityRegionInfo cityRegion;
        Empj_BuildingInfo building;

        building = tgxy_TripleAgreement.getBuildingInfo();
        if (null == building) {
            // 未获取楼幢信息
            return MyBackInfo.fail(properties, "未获取到相关楼幢信息");
        }
        cityRegion = building.getCityRegion();
        project = building.getProject();
        company = building.getDevelopCompany();

        // 创建保存对象
        Sm_BusinessRecord businessRecord;

        // 调用公共设置方法
        businessRecord = new Sm_BusinessRecord();
        businessRecord.setTheState(S_TheState.Normal);
        businessRecord.setBusiState(S_BusiState.HaveRecord);
        businessRecord.setEcode(sm_BusinessCodeGetService.execute("YWGL00"));
        businessRecord.setUserStart(form.getUser());
        businessRecord.setUserUpdate(form.getUser());
        businessRecord.setCreateTimeStamp(System.currentTimeMillis());
        businessRecord.setLastUpdateTimeStamp(System.currentTimeMillis());
        // 申请单信息
        businessRecord.setApprovalProcess_AF(approvalProcess_AF);
        businessRecord.setCompanyInfo(company);
        businessRecord.setCodeOfCompany(company.geteCode());
        businessRecord.setProjectInfo(project);
        businessRecord.setCodeOfProject(project.geteCode());
        businessRecord.setCityRegion(cityRegion);
        businessRecord.setTheNameOfCityRegion(cityRegion.getTheName());
        businessRecord.setCodeOfCityRegion(cityRegion.geteCode());
        businessRecord.setBuildingInfo(building);
        businessRecord.setCodeOfBuilding(building.geteCode());
        businessRecord.setTripleAgreement(tgxy_TripleAgreement);
        businessRecord.setCodeOfTripleAgreement(tgxy_TripleAgreement.geteCode());
        businessRecord.setUserBegin(tgxy_TripleAgreement.getUserStart());
        businessRecord.setDateBegin(tgxy_TripleAgreement.getCreateTimeStamp());

        businessRecord.setBusiCode(busiCode);
        businessRecord.setCodeOfBusiness(codeOfBusiness);

        // 保存对象
        sm_BusinessRecordDao.save(businessRecord);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    // 获取下一个结点索引
    public Long getNextStepWorkflowId(Sm_ApprovalProcess_Condition condition, List<Sm_ApprovalProcess_Node> nodeList,
        List<Sm_ApprovalProcess_Workflow> workflowList) {
        Long nextStep = condition.getNextStep(); // 下一步骤 结点Id
        Long nextStepWorkflowId = null;
        for (int index = 0; index < nodeList.size(); index++) {
            if (nextStep.equals(nodeList.get(index).getTableId())) {
                nextStepWorkflowId = workflowList.get(index).getTableId();
                break;
            }
        }
        return nextStepWorkflowId;
    }

}

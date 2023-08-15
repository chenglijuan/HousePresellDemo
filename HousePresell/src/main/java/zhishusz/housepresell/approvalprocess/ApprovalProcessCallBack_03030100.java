package zhishusz.housepresell.approvalprocess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_DtlForm;
import zhishusz.housepresell.controller.form.Empj_NodePredictionForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_DTLForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_DtlDao;
import zhishusz.housepresell.database.dao.Empj_NodePredictionDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_DTLDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_NodePrediction;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_DTL;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_NodeChange;
import zhishusz.housepresell.dingding.service.impl.PanYunDingDingPushDataServiceImpl;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ToInterface;

/**
 * 进度节点变更 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03030100 implements IApprovalProcessCallback {
    @Autowired
    private Gson gson;
    private static String BUSICODE = "03030100";

    @Autowired
    private Empj_BldLimitAmountDao empj_BldLimitAmountDao;
    @Autowired
    private Empj_BldLimitAmount_DtlDao empj_BldLimitAmount_DtlDao;

    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    @Autowired
    private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

    @Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;

    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;

    @Autowired
    private Empj_NodePredictionDao empj_NodePredictionDao;

    @Autowired
    private Empj_ProjProgInspection_AFDao empj_ProjProgInspection_AFDao;
    @Autowired
    private Empj_ProjProgInspection_DTLDao empj_ProjProgInspection_DTLDao;

    @Autowired
    private PanYunDingDingPushDataServiceImpl dingdingServiceImpl;

    @SuppressWarnings("unchecked")
    public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
        Properties properties = new MyProperties();

        long nowTimeStamp = System.currentTimeMillis();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyMMdd");
        Date d = new Date();
        String dateStr = formatDate.format(d);

        try {

            String workflowEcode = approvalProcessWorkflow.geteCode();// 节点编码
            // 获取正在处理的申请单
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();
            // 获取正在处理的申请单所属的流程配置
            Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
            // 流程编码加节点编码
            String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

            // 获取正在审批的楼幢
            Long tableId = sm_ApprovalProcess_AF.getSourceId();
            Empj_BldLimitAmount limitAmount = empj_BldLimitAmountDao.findById(tableId);
            if (limitAmount == null) {
                return MyBackInfo.fail(properties, "审批的单据信息不存在！");
            }

            switch (approvalProcessWork) {
                case "03030100002_ZS":
                    /*
                     * 审批通过
                     * 
                     * 更新进度节点信息到楼幢账户表
                     * 
                     * 生成对应的受限节点变更单
                     */
                    if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
                        && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {

                        limitAmount.setApprovalState(S_ApprovalState.Completed);
                        limitAmount.setRecordTimeStamp(nowTimeStamp);
                        limitAmount.setUserRecord(baseForm.getUser());
                        empj_BldLimitAmountDao.update(limitAmount);

                        Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
                        dtlForm.setTheState(S_TheState.Normal);
                        dtlForm.seteCodeOfMainTable(limitAmount.geteCode());
                        dtlForm.setMainTable(limitAmount);
                        List<Empj_BldLimitAmount_Dtl> dtlList;
                        dtlList = empj_BldLimitAmount_DtlDao.findByPage(
                            empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));
                        /*
                         * 校验申请楼幢信息是否全部审批
                         */
                        for (Empj_BldLimitAmount_Dtl dtl : dtlList) {
                            if ("0".equals(dtl.getApprovalResult()) && StringUtils.isBlank(dtl.getApprovalInfo())) {
                                return MyBackInfo.fail(properties,
                                    "楼幢：" + dtl.geteCodeFromConstruction() + " 未维护不通过原因！");
                            }
                        }

                        Sm_AttachmentForm sm_AttachmentForm;
                        List<Sm_Attachment> sm_AttachmentList;
                        Sm_Attachment attachement;

                        Empj_BldLimitAmount_AF bldLimitAmount_AF;
                        for (Empj_BldLimitAmount_Dtl dtl : dtlList) {

                            if (!"0".equals(dtl.getApprovalResult())) {

                                dtl.setApprovalResult("1");
                                empj_BldLimitAmount_DtlDao.update(dtl);

                                Empj_BuildingInfo building = dtl.getBuilding();
                                Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
                                /**
                                 * 先更新楼幢账户表中的进度节点等信息（不更新金额）
                                 */
                                buildingAccount.setCurrentFigureProgress(dtl.getExpectFigureProgress().getStageName());
                                buildingAccount.setCurrentLimitedRatio(dtl.getExpectLimitedRatio());
                                buildingAccount.setBldLimitAmountVerDtl(dtl.getExpectFigureProgress());
                                buildingAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
                                tgpj_BuildingAccountDao.update(buildingAccount);

                                bldLimitAmount_AF = new Empj_BldLimitAmount_AF();

                                bldLimitAmount_AF.setTheState(S_TheState.Normal);
                                bldLimitAmount_AF.setApprovalState(S_ApprovalState.Examining);
                                bldLimitAmount_AF.setBusiState("未备案");
                                bldLimitAmount_AF.setUserStart(baseForm.getUser());
                                bldLimitAmount_AF.setCreateTimeStamp(nowTimeStamp);
                                bldLimitAmount_AF.setLastUpdateTimeStamp(nowTimeStamp);
                                bldLimitAmount_AF.setUserUpdate(baseForm.getUser());

                                bldLimitAmount_AF.setDevelopCompany(limitAmount.getDevelopCompany());
                                bldLimitAmount_AF.seteCodeOfDevelopCompany(limitAmount.getDevelopCompany().geteCode());
                                bldLimitAmount_AF.setProject(limitAmount.getProject());
                                bldLimitAmount_AF.seteCodeOfProject(limitAmount.geteCodeOfProject());
                                bldLimitAmount_AF.setTheNameOfProject(limitAmount.getTheNameOfProject());

                                bldLimitAmount_AF.setBuilding(building);
                                bldLimitAmount_AF.seteCodeOfBuilding(building.geteCode());
                                bldLimitAmount_AF.setUpfloorNumber(dtl.getUpfloorNumber());
                                bldLimitAmount_AF.seteCodeFromConstruction(dtl.geteCodeFromConstruction());
                                bldLimitAmount_AF.seteCodeFromPublicSecurity(dtl.geteCodeFromPublicSecurity());
                                bldLimitAmount_AF.setExpectFigureProgress(dtl.getExpectFigureProgress());

                                bldLimitAmount_AF.setDtlId(dtl.getTableId());

                                empj_BldLimitAmount_AFDao.save(bldLimitAmount_AF);
                                bldLimitAmount_AF.seteCode("03030101N" + dateStr
                                    + String.format("%06d", bldLimitAmount_AF.getTableId().intValue()));
                                empj_BldLimitAmount_AFDao.update(bldLimitAmount_AF);

                                /*
                                 * 附件信息
                                 */
                                sm_AttachmentForm = new Sm_AttachmentForm();
                                sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
                                // sm_AttachmentForm.setRemark(dtl.getTableId().toString());
                                sm_AttachmentForm.setBusiType("03030100");
                                sm_AttachmentForm.setTheState(S_TheState.Normal);
                                // 加载所有相关附件信息
                                sm_AttachmentList = sm_AttachmentDao.findByPage(
                                    sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

                                for (Sm_Attachment po : sm_AttachmentList) {
                                    attachement = new Sm_Attachment();
                                    attachement.setTheState(S_TheState.Normal);
                                    attachement.setAttachmentCfg(po.getAttachmentCfg());
                                    attachement.setBankId(po.getBankId());
                                    attachement.setBusiType("03030101");
                                    attachement.setCreateTimeStamp(nowTimeStamp);
                                    attachement.setFileType(po.getFileType());
                                    attachement.setLastUpdateTimeStamp(nowTimeStamp);
                                    attachement.setSourceId(bldLimitAmount_AF.getTableId().toString());
                                    attachement.setSourceType(po.getSourceType());
                                    attachement.setTheLink(po.getTheLink());
                                    attachement.setTheSize(po.getTheSize());
                                    sm_AttachmentDao.save(attachement);

                                }

                            }
                        }

                    }
                    break;

                case "03030100001_ZS":
                    /*
                     * 审批通过
                     * 
                     * 更新进度节点信息到楼幢账户表
                     * 
                     * 生成对应的受限节点变更单
                     */
                    if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
                        && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {

                        limitAmount.setBusiState("已备案");
                        limitAmount.setApprovalState(S_ApprovalState.Completed);
                        limitAmount.setRecordTimeStamp(nowTimeStamp);
                        limitAmount.setUserRecord(baseForm.getUser());
                        empj_BldLimitAmountDao.update(limitAmount);

                        Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
                        dtlForm.setTheState(S_TheState.Normal);
                        dtlForm.seteCodeOfMainTable(limitAmount.geteCode());
                        dtlForm.setMainTable(limitAmount);
                        List<Empj_BldLimitAmount_Dtl> dtlList;
                        dtlList = empj_BldLimitAmount_DtlDao.findByPage(
                            empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));
                        /*
                         * 校验申请楼幢信息是否全部审批
                         */
                        for (Empj_BldLimitAmount_Dtl dtl : dtlList) {

                            /*if ("0".equals(dtl.getResultOne()) && StringUtils.isBlank(dtl.getResultInfoOne())) {
                                return MyBackInfo.fail(properties,
                                    "楼幢：" + dtl.geteCodeFromConstruction() + " 未维护A不通过原因！");
                            }
                            
                            if ("0".equals(dtl.getResultTwo()) && StringUtils.isBlank(dtl.getResultInfoTwo())) {
                                return MyBackInfo.fail(properties,
                                    "楼幢：" + dtl.geteCodeFromConstruction() + " 未维护B不通过原因！");
                            }*/

                            /*if ("0".equals(dtl.getApprovalResult()) && StringUtils.isBlank(dtl.getApprovalInfo())) {
                                return MyBackInfo.fail(properties,
                                    "楼幢：" + dtl.geteCodeFromConstruction() + " 未维护不通过原因！");
                            }*/
                        }

                        Sm_AttachmentForm sm_AttachmentForm;
                        List<Sm_Attachment> sm_AttachmentList;
                        Sm_Attachment attachement;

                        Empj_BldLimitAmount_AF bldLimitAmount_AF;
                        Empj_NodePredictionForm empj_NodePredictionForm;
                        List<Empj_NodePrediction> nodeList;
                        Empj_NodePrediction nodePrediction;
                        Empj_BuildingInfo building;
                        Tgpj_BuildingAccount buildingAccount;

                        String jlbg_url1;
                        String jlbg_url2;
                        for (Empj_BldLimitAmount_Dtl dtl : dtlList) {

                            building = dtl.getBuilding();
                            buildingAccount = building.getBuildingAccount();

                            empj_NodePredictionForm = new Empj_NodePredictionForm();
                            empj_NodePredictionForm.setTheState(S_TheState.Normal);
                            empj_NodePredictionForm.setBuildingId(building.getTableId());
                            empj_NodePredictionForm
                                .setExpectFigureProgressId(dtl.getExpectFigureProgress().getTableId());
                            empj_NodePredictionForm.setExpectFigureProgress(dtl.getExpectFigureProgress());
                            nodeList = empj_NodePredictionDao.findByPage(empj_NodePredictionDao
                                .getQuery(empj_NodePredictionDao.getBasicHQL(), empj_NodePredictionForm));
                            
                            Date limitDate = dtl.getCompleteDateOne();
//                            if (dtl.getCompleteDateOne().after(dtl.getCompleteDateTwo())) {
//                                limitDate = dtl.getCompleteDateTwo();
//                            } else {
//                                limitDate = dtl.getCompleteDateOne();
//                            }
                            
                            if(nodeList.size() > 0){
                                nodePrediction = nodeList.get(0);
                                
//                                if (dtl.getCompleteDateOne().after(dtl.getCompleteDateTwo())) {
//                                    nodePrediction.setCompleteDate(dtl.getCompleteDateTwo());
//                                } else {
//                                    nodePrediction.setCompleteDate(dtl.getCompleteDateOne());
//                                }

                                nodePrediction.setCompleteDate(dtl.getCompleteDateOne());
                                empj_NodePredictionDao.update(nodePrediction);
                            }

                            // if(!"0".equals(dtl.getApprovalResult())){

                            if (!"0".equals(dtl.getResultOne()) && !"0".equals(dtl.getApprovalResult())) {

                                dtl.setBusiState("已备案");
                                dtl.setApprovalState(S_ApprovalState.Completed);
                                dtl.setApprovalResult("1");
                                dtl.setResultOne("1");
                                dtl.setResultTwo("1");
                                empj_BldLimitAmount_DtlDao.update(dtl);

                                /**
                                 * 先更新楼幢账户表中的进度节点等信息（不更新金额）
                                 */
                                buildingAccount.setCurrentFigureProgress(dtl.getExpectFigureProgress().getStageName());
                                buildingAccount.setCurrentLimitedRatio(dtl.getExpectLimitedRatio());
                                buildingAccount.setBldLimitAmountVerDtl(dtl.getExpectFigureProgress());
                                buildingAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
                                tgpj_BuildingAccountDao.update(buildingAccount);

                                bldLimitAmount_AF = new Empj_BldLimitAmount_AF();

                                bldLimitAmount_AF.setTheState(S_TheState.Normal);
                                bldLimitAmount_AF.setApprovalState(S_ApprovalState.Examining);
                                bldLimitAmount_AF.setBusiState("未备案");
                                bldLimitAmount_AF.setUserStart(baseForm.getUser());
                                bldLimitAmount_AF.setCreateTimeStamp(nowTimeStamp);
                                bldLimitAmount_AF.setLastUpdateTimeStamp(nowTimeStamp);
                                bldLimitAmount_AF.setUserUpdate(baseForm.getUser());

                                bldLimitAmount_AF.setDevelopCompany(limitAmount.getDevelopCompany());
                                bldLimitAmount_AF.seteCodeOfDevelopCompany(limitAmount.getDevelopCompany().geteCode());
                                bldLimitAmount_AF.setProject(limitAmount.getProject());
                                bldLimitAmount_AF.seteCodeOfProject(limitAmount.geteCodeOfProject());
                                bldLimitAmount_AF.setTheNameOfProject(limitAmount.getTheNameOfProject());

                                bldLimitAmount_AF.setBuilding(building);
                                bldLimitAmount_AF.seteCodeOfBuilding(building.geteCode());
                                bldLimitAmount_AF.setUpfloorNumber(dtl.getUpfloorNumber());
                                bldLimitAmount_AF.seteCodeFromConstruction(dtl.geteCodeFromConstruction());
                                bldLimitAmount_AF.seteCodeFromPublicSecurity(dtl.geteCodeFromPublicSecurity());
                                bldLimitAmount_AF.setExpectFigureProgress(dtl.getExpectFigureProgress());

                                bldLimitAmount_AF.setDtlId(dtl.getTableId());

                                empj_BldLimitAmount_AFDao.save(bldLimitAmount_AF);
                                bldLimitAmount_AF.seteCode("03030101N" + dateStr
                                    + String.format("%06d", bldLimitAmount_AF.getTableId().intValue()));
                                empj_BldLimitAmount_AFDao.update(bldLimitAmount_AF);

                                /*
                                 * 附件信息
                                 */
                                sm_AttachmentForm = new Sm_AttachmentForm();
                                sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
                                // sm_AttachmentForm.setRemark(dtl.getTableId().toString());
                                sm_AttachmentForm.setBusiType("03030100");
                                sm_AttachmentForm.setTheState(S_TheState.Normal);
                                // 加载所有相关附件信息
                                sm_AttachmentList = sm_AttachmentDao.findByPage(
                                    sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

                                jlbg_url1 = "";
                                jlbg_url2 = "";

                                for (Sm_Attachment po : sm_AttachmentList) {

                                    if ("010201N18112400004".equals(po.getSourceType())) {
                                        jlbg_url1 = po.getTheLink();
                                    }

                                    if ("010201N20110600001".equals(po.getSourceType())) {
                                        jlbg_url2 = po.getTheLink();
                                    }

                                    attachement = new Sm_Attachment();
                                    attachement.setTheState(S_TheState.Normal);
                                    attachement.setAttachmentCfg(po.getAttachmentCfg());
                                    attachement.setBankId(po.getBankId());
                                    attachement.setBusiType("03030101");
                                    attachement.setCreateTimeStamp(nowTimeStamp);
                                    attachement.setFileType(po.getFileType());
                                    attachement.setLastUpdateTimeStamp(nowTimeStamp);
                                    attachement.setSourceId(bldLimitAmount_AF.getTableId().toString());
                                    attachement.setSourceType(po.getSourceType());
                                    attachement.setTheLink(po.getTheLink());
                                    attachement.setTheSize(po.getTheSize());
                                    sm_AttachmentDao.save(attachement);

                                }

                                /**
                                 * xsz by 2020-8-22 12:51:15 判断是否需要推送 START
                                 */

                                // 查询开关
                                Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
                                baseParameterForm0.setTheState(S_TheState.Normal);
                                baseParameterForm0.setTheValue("710000");
                                baseParameterForm0.setParametertype("71");
                                
                                
                                
                                Sm_BaseParameter baseParameter0 =
                                    sm_BaseParameterDao.findOneByQuery_T(sm_BaseParameterDao
                                        .getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

                                //TODO
//                                baseParameter0 = null;
                                if (null != baseParameter0 && "1".equals(baseParameter0.getTheName())) {

                                    toInterFaceAction(building, String.valueOf(tableId),
                                        "完成" + dtl.getExpectFigureProgress().getStageName(),
                                        StringUtils.isBlank(limitAmount.getCompanyOneName()) ? ""
                                            : limitAmount.getCompanyOneName(),
                                        jlbg_url1, StringUtils.isBlank(limitAmount.getCompanyTwoName()) ? ""
                                            : limitAmount.getCompanyTwoName(),
                                        jlbg_url2);
                                }

                                /**
                                 * xsz by 2020-8-22 12:51:15 判断是否需要推送 END
                                 */

                                /*
                                 * 更新节点预测信息
                                 */
                                updateInspection(baseForm.getUser(), building, dtl.getPredictionNode(), limitDate);
                                /*
                                 * 更新节点预测信息
                                 */

                            }
                        }

                    }
                    break;

                case "03030100001_WQCS":

                    //TODO
                    if (0 == approvalProcessWorkflow.getLastAction()) {
                        /**
                         * 20230515需求变更 任务推送到钉钉系统 只推送一家监理任务
                         */

                        Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
                        dtlForm.setTheState(S_TheState.Normal);
                        dtlForm.seteCodeOfMainTable(limitAmount.geteCode());
                        dtlForm.setMainTable(limitAmount);
                        List<Empj_BldLimitAmount_Dtl> dtlList;
                        dtlList = empj_BldLimitAmount_DtlDao.findByPage(
                            empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));

                        if (null != dtlList && !dtlList.isEmpty()) {
                            limitAmount.setDtlList(dtlList);

                            boolean pushSupervisorCompanyData = dingdingServiceImpl.pushSupervisorCompanyData(limitAmount);
                            System.out.println("pushSupervisorCompanyData="+pushSupervisorCompanyData);
                            if(pushSupervisorCompanyData){
                            	boolean pushProjectData = dingdingServiceImpl.pushProjectData(limitAmount);
                                System.out.println("pushProjectData="+pushProjectData);
                            	if(pushProjectData){
                            		boolean pushBuildingData = dingdingServiceImpl.pushBuildingData(limitAmount);
                                    System.out.println("pushBuildingData="+pushBuildingData);
                            		if(pushBuildingData){
                                        System.out.println("pushBuildingData="+pushBuildingData);
                            			dingdingServiceImpl.pushChangeNodeData(limitAmount);
                                        boolean assignTask = dingdingServiceImpl.assignTask(limitAmount);
                                        if(!assignTask){
                                        	return MyBackInfo.fail(properties, "推送钉钉失败，请联系管理人员！1");
                                        }
                            		}else{
                            			return MyBackInfo.fail(properties, "推送钉钉失败，请联系管理人员！2");
                            		}

                            	}else{
                            		return MyBackInfo.fail(properties, "推送钉钉失败，请联系管理人员！3");
                            	}

                            }else{
                            	return MyBackInfo.fail(properties, "推送钉钉失败，请联系管理人员！4");
                            }

                        }
                    }

                    break;

                case "03030100001_WQFS":

                    if (0 < approvalProcessWorkflow.getLastAction()) {
                        limitAmount.setApprovalOne("");
                        limitAmount.setApprovalTwo("");
                        empj_BldLimitAmountDao.update(limitAmount);
                        
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

    private Log log = LogFactory.getCurrentLogFactory().createLog(ApprovalProcessCallBack_03030100.class);
    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

    /**
     * 系统推送数据到门户网站
     * 
     * @param buildingInfo
     *            楼幢信息
     * @param ts_id
     *            进度变更ID
     * @param jdzt
     *            进度状态
     * @param jlbg_name1
     *            监理公司A
     * @param jlbg_url1
     *            监理报告A
     * @param jlbg_name2
     *            监理公司B
     * @param jlbg_url2
     *            监理报告B
     * @return
     */
    public Boolean toInterFaceAction(Empj_BuildingInfo buildingInfo, String ts_id, String jdzt, String jlbg_name1,
        String jlbg_url1, String jlbg_name2, String jlbg_url2) {

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
            .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.equals("未查询到配置路径！");

            return false;
        }

        To_NodeChange nodeChange = new To_NodeChange();
        nodeChange.setAction("add");
        nodeChange.setCate("ldjd");
        nodeChange.setTs_bld_id(String.valueOf(buildingInfo.getTableId()));
        nodeChange.setTs_id(ts_id);
        nodeChange.setJdzt(jdzt);
        nodeChange.setJfbatzs_url("");
        nodeChange.setGswz_url("");
        nodeChange.setJlbg_name1(jlbg_name1);
        nodeChange.setJlbg_url1(jlbg_url1);
        nodeChange.setJlbg_name2(jlbg_name2);
        nodeChange.setJlbg_url2(jlbg_url2);
        nodeChange.setJdtime(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
        nodeChange.setGabhdzb("");
        nodeChange.setGswz_picurl("");
        nodeChange.setJlbg_url(jlbg_url1 + "," + jlbg_url2);

        Gson gson = new Gson();
        String jsonMap = gson.toJson(nodeChange);
        System.out.println(jsonMap);
        String decodeStr = Base64Encoder.encode(jsonMap);
        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间

        tgpf_SocketMsg.setMsgDirection("TGZZ_MHTS");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());
    }

    @SuppressWarnings("unchecked")
    private void updateInspection(Sm_User user, Empj_BuildingInfo buildingInfo,
        Tgpj_BldLimitAmountVer_AFDtl limitAmountVer_AFDtl, Date completeDate) {
        /*
         * 更新项目进度巡查预测完成时间
         * 
         * 查询预测节点的预测信息
         * 
         */

        MyDatetime instance = MyDatetime.getInstance();
        Empj_ProjProgInspection_DTLForm dtlModel = new Empj_ProjProgInspection_DTLForm();
        dtlModel.setTheState(S_TheState.Normal);
        dtlModel.setBuildInfo(buildingInfo);
        dtlModel.setForecastNode(limitAmountVer_AFDtl);
        List<Empj_ProjProgInspection_DTL> dtlList = empj_ProjProgInspection_DTLDao.findByPage(
            empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));

        Date limitDate = null;
        Long timeCompare = 0L;
        for (int i = 0; i < dtlList.size(); i++) {
            if (StrUtil.isNotBlank(dtlList.get(i).getForecastCompleteDate())) {
                limitDate = instance.parse(dtlList.get(i).getForecastCompleteDate().trim());
                break;
            }
        }

        
        // 更新预测节点数据
        Empj_ProjProgInspection_AF afInfo;
        for (Empj_ProjProgInspection_DTL dtl : dtlList) {
            dtl.setForecastCompleteDate(instance.dateToSimpleString(completeDate));
            dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
            dtl.setUserUpdate(user);
            dtl.setDataSources("工程进度更新");
            empj_ProjProgInspection_DTLDao.update(dtl);
            
            afInfo = dtl.getAfInfo();
            if(null != afInfo){
                afInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
                afInfo.setUserUpdate(user);
                afInfo.setDataSources("工程进度更新");
                afInfo.setUpdateDateTime(System.currentTimeMillis());
                empj_ProjProgInspection_AFDao.update(afInfo);
            }
            
        }
        
        if (null != limitDate) {
            timeCompare = completeDate.getTime() - limitDate.getTime();

            // 查询预测节点之后的数据并根据时间差顺延日期
            dtlModel = new Empj_ProjProgInspection_DTLForm();
            dtlModel.setTheState(S_TheState.Normal);
            dtlModel.setBuildInfo(buildingInfo);
            dtlModel.setForecastNode(limitAmountVer_AFDtl);
            dtlModel.setNowLimit(limitAmountVer_AFDtl.getLimitedAmount());
            dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicCompareHQL(), dtlModel));
            for (Empj_ProjProgInspection_DTL dtl : dtlList) {
                if (StrUtil.isNotBlank(dtl.getForecastCompleteDate())) {
                    dtl.setForecastCompleteDate(
                        instance.dateToString(instance.parse(dtl.getForecastCompleteDate()).getTime() + timeCompare));
                    dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
                    dtl.setUserUpdate(user);
                    dtl.setDataSources("工程进度更新");
                    empj_ProjProgInspection_DTLDao.update(dtl);
                }
            }

            /*Empj_ProjProgInspection_AFForm afModel = new Empj_ProjProgInspection_AFForm();
            afModel.setTheState(S_TheState.Normal);
            afModel.setBuildInfo(buildingInfo);
            List<Empj_ProjProgInspection_AF> listAf = empj_ProjProgInspection_AFDao.findByPage(
                empj_ProjProgInspection_AFDao.getQuery(empj_ProjProgInspection_AFDao.getBasicHQLUpdate(), afModel));
            for (Empj_ProjProgInspection_AF afInfo : listAf) {
                afInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
                afInfo.setUserUpdate(user);
                afInfo.setDataSources("工程进度更新");
                afInfo.setUpdateDateTime(System.currentTimeMillis());
                empj_ProjProgInspection_AFDao.update(afInfo);
            }*/
        }

    }
}

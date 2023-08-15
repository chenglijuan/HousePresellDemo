package zhishusz.housepresell.service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.approvalprocess.ApprovalProcessCallbackService;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_DtlDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalAction;
import zhishusz.housepresell.database.po.state.S_ApprovalModel;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.project.ApprovalProcessGetWorkFlowUtil;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.GetNextWorkDayUtil;

/**
 * 代办详情审批
 *
 * @author Glad.Wang
 *
 */
@Service
@Transactional
public class Sm_ApprovalProcess_HandlerService {
    @Autowired
    private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
    @Autowired
    private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    @Autowired
    private Sm_ApprovalProcess_CheckService sm_approvalProcess_checkService;
    @Autowired
    private Sm_ApprovalProcess_MessagePushletService messagePushletService;
    @Autowired
    private Sm_ApprovalProcess_ConditionJudgeService conditionJudgeService;
    @Autowired
    ApprovalProcessCallbackService approvalProcessCallbackService;
    @Autowired
    private Sm_BaseDao sm_BaseDao;
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Sm_AttachmentCfgDao attachmentCfgDao;
    @Autowired
    private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
    @Autowired
    private Empj_BldLimitAmount_AFDao bldLimitAmountAfDao;
    @Autowired
    private ApprovalProcessGetWorkFlowUtil approvalProcessGetWorkFlowUtil;
    private MyDatetime myDatetime = MyDatetime.getInstance();
    @Autowired
    private GetNextWorkDayUtil getNextWorkDayUtil;
    @Autowired
    private Empj_BuildingInfoDao buildingInfoDao;
    @Autowired
    private ExportPdfByWordService exportPdfByWordService;// 生成PDF

    @Autowired
    private Empj_BldLimitAmountDao bldLimitAmountDao;
    @Autowired
    private Empj_BldLimitAmount_DtlDao bldLimitAmount_DtlDao;
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Sm_ApprovalProcess_RecordForm model) {
        Properties properties = new MyProperties();

        long nowTimeStamp = System.currentTimeMillis();

        String buttonType = model.getButtonType();
        Long approvalProcessId = model.getApprovalProcessId(); // 审批流程Id
        Long userOperateId = model.getUserId(); // 审批人Id
        String theContent = model.getTheContent(); // 审批评语
        Integer theAction = model.getTheAction(); // 审批动作

        if (approvalProcessId == null || approvalProcessId < 1) {
            return MyBackInfo.fail(properties, "'业务审批流'不存在");
        }
        if (theAction == null || theAction < 0) {
            return MyBackInfo.fail(properties, "请选择'审批结果'");
        }

        if (theContent == null || theContent.length() == 0) {
            if (S_ApprovalAction.theAction_Reject.equals(theAction)) {
                return MyBackInfo.fail(properties, "请填写驳回原因");
            }
            if (S_ApprovalAction.theAction_NoPass.equals(theAction)) {
                return MyBackInfo.fail(properties, "请填写不通过原因");
            }
        }

        if (userOperateId == null || userOperateId < 1) {
            return MyBackInfo.fail(properties, "'审批人'不能为空");
        }
        Sm_User userOperate = model.getUser();
        if (userOperate == null) {
            return MyBackInfo.fail(properties, "'审批人'不能为空");
        }
        Sm_ApprovalProcess_Workflow approvalProcess_Workflow =
                sm_ApprovalProcess_WorkflowDao.findById(approvalProcessId);// 当前节点信息
        if (approvalProcess_Workflow == null) {
            return MyBackInfo.fail(properties, "'当前节点'不能为空");
        }

        /**
         * XSZ BY TIME 2019-7-25 14:34:58 审批之前校验审批单是否处于提交状态
         */
        Sm_ApprovalProcess_AF approvalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();
        if (null == approvalProcess_AF) {
            return MyBackInfo.fail(properties, "申请单信息不存在！");
        }
        if ("待提交".equals(approvalProcess_AF.getBusiState())) {
            return MyBackInfo.fail(properties, "申请信息已撤销，请确认后重试！");
        }

        MsgInfo msgInfoHandleExtra = handleExtraInfo(model, approvalProcess_Workflow);
        if (!msgInfoHandleExtra.isSuccess()) {
            return MyBackInfo.fail(properties, msgInfoHandleExtra.getInfo());
        }

        // 审批人校验
        properties = sm_approvalProcess_checkService.execute(approvalProcess_Workflow, userOperateId);
        if (properties.getProperty("result").equals("fail")) {
            return properties;
        }

        // 先判断该节点状态是否改变
        if (S_WorkflowBusiState.Completed.equals(approvalProcess_Workflow.getBusiState())) {
            return MyBackInfo.fail(properties, "当前节点已被处理");
        }

        if (approvalProcess_Workflow.getApprovalProcess_AF() == null) {
            return MyBackInfo.fail(properties, "'申请单'不能为空");
        }
        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();

        if (sm_ApprovalProcess_AF.getConfiguration() == null) {
            return MyBackInfo.fail(properties, "'流程配置'不能为空");
        }

        // 审批附件材料是否必传
        MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
        if (!msgInfo.isSuccess()) {
            return MyBackInfo.fail(properties, msgInfo.getInfo());
        }

        /*
         * 审批流最后终审（ZS）签章问题
         * 暂定受限额度变更（03030101）和托管合作协议（06110201）托管终止（03030102）需要
         *
         */
        String busiCode = sm_ApprovalProcess_AF.getBusiCode();// 业务编码
        String geteCode = approvalProcess_Workflow.geteCode();// 流程编码（ZS）

        if ("03030100".equals(busiCode) && (theAction == 0)) {
            // 进度节点批量更新
            /*
             * 1.外勤初审维护预约日期
             * 2.进度见证维护附件信息
             * 3.每个节点都可以维护楼幢的审批信息
             */
            Integer tableId;
            String approvalResult;
            String approvalInfo;

            String resultOne;
            String resultInfoOne;
//            String resultTwo;
//            String resultInfoTwo;

            String completeDateOne;
//            String completeDateTwo;

            Empj_BldLimitAmount_Dtl bldLimitAmount_Dtl;
            Sm_AttachmentForm sm_AttachmentForm;
            List<Sm_Attachment> sm_AttachmentList;
            Sm_Attachment attachment;
            Sm_AttachmentCfgForm form;
            Sm_AttachmentCfg sm_AttachmentCfg;

            Map<String, Object> extraObj = model.getExtraObj();
            String tableId_AF = (String)extraObj.get("tableId");
            Empj_BldLimitAmount empj_BldLimitAmount = bldLimitAmountDao.findById(Long.valueOf(tableId_AF));

            String userType = (String)extraObj.get("userType");
            System.out.println("userType="+userType);

            if ("WQCS".equals(geteCode)) {
                // 维护预约时间
                Object appointTimeOneString_obj = extraObj.get("appointTimeOneString");
                if (null == appointTimeOneString_obj) {
                    return MyBackInfo.fail(properties, "请维护预约时间A！");
                }

                if (StringUtils.isBlank((String)appointTimeOneString_obj)) {
                    return MyBackInfo.fail(properties, "请维护预约时间A！");
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse_date;
                try {
                    parse_date = sdf.parse((String)appointTimeOneString_obj);
                    empj_BldLimitAmount.setAppointTimeStamp(parse_date.getTime());
                    empj_BldLimitAmount.setAppointmentDateOne(parse_date);
                    bldLimitAmountDao.update(empj_BldLimitAmount);
                } catch (ParseException e) {
                }

            } else if ("JDJZ".equals(geteCode)) {

                if ("all".equals(userType)) {
                    if (StrUtil.isNotBlank(empj_BldLimitAmount.getApprovalOne())) {
                        return MyBackInfo.fail(properties, "该单据已被处理！");
                    }

                }
                // 维护附件信息(先删除，再新增)
                Object dtlList_Obj = extraObj.get("dtlList");
                if (null != dtlList_Obj) {
                    List<Map<String, Object>> list_dtlList = (List<Map<String, Object>>)dtlList_Obj;

                    Integer aCount;
                    Integer bCount;

                    for (Map<String, Object> map : list_dtlList) {
                        List<Map<String, Object>> list_files = (List<Map<String, Object>>)map.get("files");
                        aCount = 0;
                        bCount = 0;

                        tableId = (Integer)map.get("tableId");
                        bldLimitAmount_Dtl = bldLimitAmount_DtlDao.findById(tableId.longValue());

                        sm_AttachmentForm = new Sm_AttachmentForm();
                        sm_AttachmentForm.setSourceId(bldLimitAmount_Dtl.getTableId().toString());
                        sm_AttachmentForm.setBusiType("03030100");
                        sm_AttachmentForm.setTheState(S_TheState.Normal);

                        if (null != list_files) {

                            if (null != bldLimitAmount_Dtl) {

                                // 加载所有相关附件信息
                                sm_AttachmentList = sm_AttachmentDao.findByPage(
                                        sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
                                for (Sm_Attachment po : sm_AttachmentList) {

                                    if ("all".equals(userType)) {
                                        if (!"010201N20110600001".equals(po.getSourceType())
                                                && !"010201N20110600002".equals(po.getSourceType())
                                                && !"010201N18112400002".equals(po.getSourceType())
                                                && !"010201N18112400003".equals(po.getSourceType())) {
                                            po.setTheState(S_TheState.Deleted);
                                            sm_AttachmentDao.update(po);
                                        }
                                    } else if ("B".equals(userType)) {
                                        if (!"010201N18112400004".equals(po.getSourceType())
                                                && !"010201N18112400005".equals(po.getSourceType())
                                                && !"010201N18112400002".equals(po.getSourceType())
                                                && !"010201N18112400003".equals(po.getSourceType())) {
                                            po.setTheState(S_TheState.Deleted);
                                            sm_AttachmentDao.update(po);
                                        }
                                    }
                                }

                                for (Map<String, Object> file_map : list_files) {
                                    String sourceType = (String)file_map.get("sourceType");
                                    String theLink = (String)file_map.get("theLink");
                                    String fileType = (String)file_map.get("fileType");
                                    String theSize = (String)file_map.get("theSize");
                                    String remark = (String)file_map.get("remark");

                                    // && !"010201N18112400002".equals(po.getSourceType()) &&
                                    // !"010201N18112400003".equals(po.getSourceType())
                                    if (!"010201N18112400002".equals(sourceType)
                                            && !"010201N18112400003".equals(sourceType)) {

                                        attachment = new Sm_Attachment();

                                        form = new Sm_AttachmentCfgForm();
                                        form.seteCode(sourceType);
                                        sm_AttachmentCfg = attachmentCfgDao.findOneByQuery_T(
                                                attachmentCfgDao.getQuery(attachmentCfgDao.getBasicHQL(), form));

                                        attachment.setTheLink(theLink);
                                        attachment.setTheSize(theSize);
                                        attachment.setFileType(fileType);
                                        attachment.setSourceType(sourceType);
                                        attachment.setAttachmentCfg(sm_AttachmentCfg);
                                        attachment.setSourceId(bldLimitAmount_Dtl.getTableId().toString());
                                        attachment.setBusiType("03030100");// 业务类型
                                        attachment.setTheState(S_TheState.Normal);
                                        attachment.setRemark(remark);
                                        attachment.setUserStart(model.getUser());// 创建人
                                        attachment.setUserUpdate(model.getUser());// 操作人
                                        attachment.setCreateTimeStamp(nowTimeStamp);
                                        attachment.setLastUpdateTimeStamp(nowTimeStamp);
                                        sm_AttachmentDao.save(attachment);
                                    }

                                }
                            }
                        }
                        sm_AttachmentList = sm_AttachmentDao
                                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

                        if (null == sm_AttachmentList || sm_AttachmentList.size() < 1) {
                            return MyBackInfo.fail(properties,
                                    "楼幢：" + bldLimitAmount_Dtl.geteCodeFromConstruction() + "未上传附件信息！");
                        } else {

                            if ("all".equals(userType)) {

                                if ("1".equals(bldLimitAmount_Dtl.getResultOne())) {

                                    for (Sm_Attachment attach : sm_AttachmentList) {
                                        if ("010201N18112400004".equals(attach.getSourceType())) {
                                            aCount++;
                                        }

                                        if ("010201N18112400005".equals(attach.getSourceType())) {
                                            bCount++;
                                        }
                                    }

                                    if (aCount < 1) {
                                        return MyBackInfo.fail(properties,
                                                "楼幢：" + bldLimitAmount_Dtl.geteCodeFromConstruction() + "未上传见证报告附件信息！");
                                    }
                                    if (bCount < 1) {
                                        return MyBackInfo.fail(properties,
                                                "楼幢：" + bldLimitAmount_Dtl.geteCodeFromConstruction() + "未上传实勘图片附件信息！");
                                    }

                                }
                            }

                            if ("B".equals(userType)) {
                                if ("1".equals(bldLimitAmount_Dtl.getResultTwo())) {

                                    for (Sm_Attachment attach : sm_AttachmentList) {
                                        if ("010201N20110600001".equals(attach.getSourceType())) {
                                            aCount++;
                                        }

                                        if ("010201N20110600002".equals(attach.getSourceType())) {
                                            bCount++;
                                        }
                                    }

                                    if (aCount < 1) {
                                        return MyBackInfo.fail(properties,
                                                "楼幢：" + bldLimitAmount_Dtl.geteCodeFromConstruction() + "未上传见证报告附件信息！");
                                    }
                                    if (bCount < 1) {
                                        return MyBackInfo.fail(properties,
                                                "楼幢：" + bldLimitAmount_Dtl.geteCodeFromConstruction() + "未上传实勘图片附件信息！");
                                    }

                                }
                            }

                        }

                    }
                }

                if ("all".equals(userType)) {
                    if (StrUtil.isNotBlank(empj_BldLimitAmount.getApprovalOne())) {
                        return MyBackInfo.fail(properties, "该单据已被处理！");
                    }

                    empj_BldLimitAmount.setApprovalOne("1");
                    bldLimitAmountDao.update(empj_BldLimitAmount);

                }

                if ("B".equals(userType)) {
                    if (StrUtil.isNotBlank(empj_BldLimitAmount.getApprovalTwo())) {
                        return MyBackInfo.fail(properties, "该单据已被处理！");
                    }

                    empj_BldLimitAmount.setApprovalTwo("1");
                    bldLimitAmountDao.update(empj_BldLimitAmount);
                }

            } else if ("WQFS".equals(geteCode)) {
                // 外勤复审
            } else if ("ZS".equals(geteCode)) {
                // 终审
            }

            // 维护楼幢审批信息
            Object dtlList_Obj = extraObj.get("dtlList");
            if (null != dtlList_Obj) {
                List<Map<String, Object>> list_dtlList = (List<Map<String, Object>>)dtlList_Obj;
                for (Map<String, Object> map : list_dtlList) {
                    tableId = (Integer)map.get("tableId");
                    bldLimitAmount_Dtl = bldLimitAmount_DtlDao.findById(tableId.longValue());

                    if (null != bldLimitAmount_Dtl) {
                        approvalResult = (String)map.get("approvalResult");
                        approvalInfo = (String)map.get("approvalInfo");

                        resultOne = (String)map.get("resultOne");
                        resultInfoOne = (String)map.get("resultInfoOne");

                        completeDateOne = (String)map.get("completeDateOne");

                        if("all".equals(userType)){
                            bldLimitAmount_Dtl.setCompleteDateOne(myDatetime.parse(completeDateOne));
                            bldLimitAmount_Dtl.setResultInfoOne(resultInfoOne);
                            bldLimitAmount_Dtl.setResultOne(resultOne);
                        }


                        bldLimitAmount_Dtl.setApprovalResult(approvalResult);
                        bldLimitAmount_Dtl.setApprovalInfo(approvalInfo);

                        if ("WQCS".equals(geteCode)) {
                            if ("0".equals(approvalResult)) {
                                bldLimitAmount_Dtl.setResultInfoOne(resultInfoOne);
                                bldLimitAmount_Dtl.setResultOne(approvalResult);
                                bldLimitAmount_Dtl.setApprovalResult(approvalResult);
                                bldLimitAmount_Dtl.setApprovalInfo(approvalInfo);
                            }
                        }

                        if ("JDJZ".equals(geteCode)) {
                            /*if ("0".equals(approvalResult) && "0".equals(resultOne)) {
                                if (approvalInfo.equals(resultInfoOne)) {
                                    bldLimitAmount_Dtl.setResultInfoOne("");
                                }
                            }

                            if ("0".equals(approvalResult) && "0".equals(resultTwo)) {
                                if (approvalInfo.equals(resultInfoTwo)) {
                                    bldLimitAmount_Dtl.setResultInfoTwo("");
                                }
                            }*/
                        }

                        bldLimitAmount_DtlDao.update(bldLimitAmount_Dtl);

                    }
                }
            }

        }

        String isSign = model.getIsSign();// 是否需要签章
        if (null == isSign) {
            isSign = "0";
        }

        // if (("06120501".equals(busiCode)) && "CWBFZR".equals(geteCode) && (theAction == 0) && !"1".equals(isSign)) {
        if (("06120501".equals(busiCode)) && "ZS".equals(geteCode) && (theAction == 0) && !"1".equals(isSign)) {
            // 支付保函申请
            // 需要签章的流程处理
            String isSignature = sm_ApprovalProcess_AF.getUserStart().getIsSignature();
            if (null != isSignature && "1".equals(isSignature)) {
                if (null != model.getUser().getIsSignature() && "1".equals(model.getUser().getIsSignature())) {

                    ExportPdfForm pdfModel = new ExportPdfForm();
                    pdfModel.setSourceBusiCode(busiCode);
                    pdfModel.setSourceId(String.valueOf(sm_ApprovalProcess_AF.getSourceId()));
                    Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                    String pdfUrl = (String)executeProperties.get("pdfUrl");

                    Map<String, String> signatureMap = new HashMap<>();

                    signatureMap.put("signaturePath", pdfUrl);
                    // TODO 此配置后期做成配置
                    signatureMap.put("signatureKeyword", "托管机构（盖章）");
                    signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                    properties.put("signatureMap", signatureMap);

                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, "操作成功");

                    return properties;

                }
            }
        }

        if (("03030102".equals(busiCode) || "03030101".equals(busiCode) || "06110201".equals(busiCode))
                && "ZS".equals(geteCode) && (theAction == 0) && !"1".equals(isSign)) {
            if ("03030101".equals(busiCode)) {
                // 受限额度变更
                // 需要签章的流程处理
                String isSignature = sm_ApprovalProcess_AF.getUserStart().getIsSignature();
                if (null != isSignature && "1".equals(isSignature)) {
                    if (null != model.getUser().getIsSignature() && "1".equals(model.getUser().getIsSignature())) {

                        ExportPdfForm pdfModel = new ExportPdfForm();
                        pdfModel.setSourceBusiCode(busiCode);
                        pdfModel.setSourceId(String.valueOf(sm_ApprovalProcess_AF.getSourceId()));
                        Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                        String pdfUrl = (String)executeProperties.get("pdfUrl");

                        Map<String, String> signatureMap = new HashMap<>();

                        signatureMap.put("signaturePath", pdfUrl);
                        // TODO 此配置后期做成配置
                        signatureMap.put("signatureKeyword", "项目部负责人意见：");
                        signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                        properties.put("signatureMap", signatureMap);

                        properties.put(S_NormalFlag.result, S_NormalFlag.success);
                        properties.put(S_NormalFlag.info, "操作成功");

                        return properties;

                    }
                }
            }
            if ("06110201".equals(busiCode)) {
                // 托管合作协议
                String isSignature = sm_ApprovalProcess_AF.getUserStart().getIsSignature();
                if (null != isSignature && "1".equals(isSignature)) {
                    if (null != model.getUser().getIsSignature() && "1".equals(model.getUser().getIsSignature())) {

                        ExportPdfForm pdfModel = new ExportPdfForm();
                        pdfModel.setSourceBusiCode(busiCode);
                        pdfModel.setSourceId(String.valueOf(sm_ApprovalProcess_AF.getSourceId()));
                        Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                        String pdfUrl = (String)executeProperties.get("pdfUrl");

                        Map<String, String> signatureMap = new HashMap<>();

                        signatureMap.put("signaturePath", pdfUrl);
                        // TODO 此配置后期做成配置
                        signatureMap.put("signatureKeyword", "乙方（盖章）");
                        signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                        properties.put("signatureMap", signatureMap);

                        properties.put(S_NormalFlag.result, S_NormalFlag.success);
                        properties.put(S_NormalFlag.info, "操作成功");

                        return properties;

                    }
                }
            }
            if ("03030102".equals(busiCode)) {
                // 托管终止变更
                // 需要签章的流程处理
                String isSignature = sm_ApprovalProcess_AF.getUserStart().getIsSignature();
                if (null != isSignature && "1".equals(isSignature)) {
                    if (null != model.getUser().getIsSignature() && "1".equals(model.getUser().getIsSignature())) {

                        ExportPdfForm pdfModel = new ExportPdfForm();
                        pdfModel.setSourceBusiCode(busiCode);
                        pdfModel.setSourceId(String.valueOf(sm_ApprovalProcess_AF.getSourceId()));
                        Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                        String pdfUrl = (String)executeProperties.get("pdfUrl");

                        Map<String, String> signatureMap = new HashMap<>();

                        signatureMap.put("signaturePath", pdfUrl);
                        // TODO 此配置后期做成配置
                        signatureMap.put("signatureKeyword", "常州正泰房产居间服务有限公司（盖章）");
                        signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                        properties.put("signatureMap", signatureMap);
                        properties.put(S_NormalFlag.result, S_NormalFlag.success);
                        properties.put(S_NormalFlag.info, "操作成功");

                        return properties;

                    }
                }
            }

        }

        // 备案按钮
        if (buttonType.equals("2")) {
            passExecute(approvalProcess_Workflow, userOperate, theAction);
        } else {
            /**
             * 1.抢占模式： 不通过操作 新增业务 删除po ， 流程状态和结点状态：不通过 2.会签模式： 不通过操作 相当于驳回
             */
            Integer approvalModel = approvalProcess_Workflow.getApprovalModel();// 审批模式

            // 抢占模式
            if (approvalModel == S_ApprovalModel.PreemptionModel) {
                // 通过
                if (theAction == 0) {
                    passExecute(approvalProcess_Workflow, userOperate, theAction);
                }

                // 驳回
                if (theAction == 1) {
                    rejectExecute(approvalProcess_Workflow, userOperate, theAction);
                }

                // 不通过
                if (theAction == 2) {
                    noPassExecute(approvalProcess_Workflow, sm_ApprovalProcess_AF);
                }
            }

            // 会签模式
            if (approvalModel.equals(S_ApprovalModel.SignModel)) {

                MyDouble myDouble = MyDouble.getInstance();

                Double finishPercentage = 1.0 * approvalProcess_Workflow.getFinishPercentage() / 100;// 会签完成阀值
                Double passPercentage = 1.0 * approvalProcess_Workflow.getPassPercentage() / 100;// 会签通过阀值

                Integer currentPassNum = 1;// 当前审核通过人数

                if ("03030100".equals(busiCode) && "JDJZ".equals(geteCode)) {
                    // 针对进度节点更新双监理处理（一个监理公司只需要审批一次）

                    Empj_BldLimitAmount empj_BldLimitAmount =
                            bldLimitAmountDao.findById(approvalProcess_AF.getSourceId());

                    if (theAction == 0) {
                        if ("1".equals(empj_BldLimitAmount.getApprovalOne())
                                && "1".equals(empj_BldLimitAmount.getApprovalTwo())) {
                            passExecute(approvalProcess_Workflow, userOperate, approvalModel);
                        }
                    } else if (theAction == 1 || theAction == 2) {
                        rejectExecute(approvalProcess_Workflow, userOperate, approvalModel);
                    }

                } else {
                    // 通过
                    if (theAction == 0) {
                        currentPassNum = 1;
                    }
                    // 驳回 ，不通过
                    // 会签模式下 不通过 相当于驳回操作
                    else if (theAction == 1 || theAction == 2) {
                        currentPassNum = 0;
                    }

                    Sm_Permission_Role sm_permission_role = approvalProcess_Workflow.getRole();// 当前节点所对应角色
                    Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
                    roleUserForm.setTheState(S_TheState.Normal);
                    roleUserForm.setSm_Permission_RoleId(sm_permission_role.getTableId());

                    // 需要审批人数
                    Integer needApprovalNum = sm_permission_roleUserDao.findByPage_Size(
                            sm_permission_roleUserDao.getQuery_Size(sm_permission_roleUserDao.getBasicHQL(), roleUserForm));
                    Sm_ApprovalProcess_RecordForm recordForm = new Sm_ApprovalProcess_RecordForm();
                    recordForm.setTheState(S_TheState.Normal);
                    recordForm.setApprovalProcessId(approvalProcess_Workflow.getTableId());
                    recordForm.setWorkflowTime(approvalProcess_Workflow.getLastUpdateTimeStamp());
                    List<Sm_ApprovalProcess_Record> sm_approvalProcess_recordList =
                            sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao
                                    .getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), recordForm));
                    if (sm_approvalProcess_recordList == null)
                        sm_approvalProcess_recordList = new ArrayList<Sm_ApprovalProcess_Record>();
                    Integer currentapprovalNum = sm_approvalProcess_recordList.size() + 1;// 当前审核完成人数
                    for (Sm_ApprovalProcess_Record sm_approvalProcess_record : sm_approvalProcess_recordList) {
                        if (sm_approvalProcess_record.getTheAction().equals(0)) {
                            currentPassNum++;
                        }
                    }

                    if (currentapprovalNum >= myDouble.getShort(needApprovalNum * finishPercentage, 1))// 人数达到 //
                    // currentapprovalNum
                    // :当前审批人数
                    // needApprovalNum：需要审批人数
                    {
                        if (currentPassNum >= myDouble.getShort(currentapprovalNum * passPercentage, 1))// 通过率达到
                        {
                            // 这样才会走正在的通过操作走到下一个节点
                            passExecute(approvalProcess_Workflow, userOperate, approvalModel);
                        } else// 通过率未达到
                        {
                            // 驳回
                            rejectExecute(approvalProcess_Workflow, userOperate, approvalModel);
                        }
                    } else {
                        // 人数未达到

                        /*
                         * xsz by time 2020-7-14 14:13:04
                         * 接上，如果存在一个人驳回，则全部驳回
                         *
                         * ==============START=======================
                         */
                        if (theAction == 1 || theAction == 2) {
                            // 驳回
                            rejectExecute(approvalProcess_Workflow, userOperate, approvalModel);
                        }

                        /*
                         * xsz by time 2020-7-14 14:13:04
                         * 接上，如果存在一个人驳回，则全部驳回
                         * ==============END=======================
                         */
                    }
                }
            }
        }

        Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = sm_ApprovalProcess_AF.getConfiguration();

        /**
         * 3.生成一条审批记录
         */
        List recordList = new ArrayList();
        Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = new Sm_ApprovalProcess_Record();
        sm_ApprovalProcess_Record.setTheState(S_TheState.Normal);
        sm_ApprovalProcess_Record.setConfiguration(sm_approvalProcess_cfg); // 流程配置
        sm_ApprovalProcess_Record.setCreateTimeStamp(System.currentTimeMillis());
        sm_ApprovalProcess_Record.setLastUpdateTimeStamp(System.currentTimeMillis());
        sm_ApprovalProcess_Record.setUserStart(userOperate);
        sm_ApprovalProcess_Record.setUserUpdate(userOperate);
        sm_ApprovalProcess_Record.setUserOperate(userOperate); // 审批人
        sm_ApprovalProcess_Record.setTheContent(theContent); // 审批评语
        sm_ApprovalProcess_Record.setTheAction(theAction); // 审批动作
        sm_ApprovalProcess_Record.setOperateTimeStamp(System.currentTimeMillis()); // 操作时间点
        sm_ApprovalProcess_RecordDao.save(sm_ApprovalProcess_Record);

        if (approvalProcess_Workflow.getApprovalProcess_recordList() != null
                && approvalProcess_Workflow.getApprovalProcess_recordList().size() > 0) {
            approvalProcess_Workflow.getApprovalProcess_recordList().add(sm_ApprovalProcess_Record);
        } else {
            approvalProcess_Workflow.setApprovalProcess_recordList(recordList);
            recordList.add(sm_ApprovalProcess_Record);
        }

        sm_AttachmentBatchAddService.execute(model, sm_ApprovalProcess_Record.getTableId());

        sm_ApprovalProcess_AF.setUserUpdate(userOperate);
        sm_ApprovalProcess_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
        sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);

        sm_ApprovalProcess_WorkflowDao.save(approvalProcess_Workflow);

        // 审批流回调
        properties = approvalProcessCallbackService.execute(approvalProcess_Workflow, model);
        if (MyBackInfo.isFail(properties)) {
            throw new RoolBackException(MyString.getInstance().parse(properties.get(S_NormalFlag.info)));
        }

        // 消息推送
        // properties = messagePushletService.execute(approvalProcess_Workflow,userOperate);

        /**
         * xsz by time 只有审批状态是通过时，才进行消息推送
         */
        if (S_ApprovalAction.theAction_Pass.equals(theAction)) {
            messagePushletService.execute(approvalProcess_Workflow, userOperate);
        }

        Object extraExtra = msgInfoHandleExtra.getExtra();
        if (extraExtra != null) {
            properties.put("noPresellCert", "该楼幢的预售证号为空");
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 处理额外事项
     *
     * @param model
     * @param approvalProcess_Workflow
     * @return
     */
    private MsgInfo handleExtraInfo(Sm_ApprovalProcess_RecordForm model,
                                    Sm_ApprovalProcess_Workflow approvalProcess_Workflow) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setSuccess(true);
        Map<String, Object> extraObj = model.getExtraObj();
        if (extraObj == null) {
            return msgInfo;
        }
        String sourceType = (String)extraObj.get("sourceType");
        if (sourceType == null) {
            return msgInfo;
        }
        switch (sourceType) {
            case "Empj_BldLimitAmount_AF":
                handleLimitAmout(model, approvalProcess_Workflow, msgInfo);
                break;
            case "Tgpj_BuildingAvgPrice":
                handleBuildingAvgPrice(msgInfo, extraObj);
                break;
        }
        return msgInfo;
    }

    // 通过
    public void passExecute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow, Sm_User userOperate,
                            Integer theAction) {
        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();
        Integer approvalModel = approvalProcess_Workflow.getApprovalModel();// 审批模式

        // 根据条件查找下一个环节
        Long sendId = null;
        Sm_ApprovalProcess_Workflow sendWofkflow = null;// 下一个环节
        Sm_ApprovalProcess_Workflow nextApprovalProcess_Workflow = null;// 下一个环节

        if (approvalProcess_Workflow.getNextWorkFlow() != null)
            nextApprovalProcess_Workflow = approvalProcess_Workflow.getNextWorkFlow(); // 下一个节点信息

        if (nextApprovalProcess_Workflow != null) {
            if (approvalProcess_Workflow.getWorkflowConditionList() != null
                    && !approvalProcess_Workflow.getWorkflowConditionList().isEmpty()) {
                // 条件判断
                Properties condition_Pro = conditionJudgeService.execute(approvalProcess_Workflow);

                if (S_NormalFlag.success.equals(condition_Pro.getProperty("result"))) {
                    sendId = Long.valueOf(condition_Pro.getProperty("info"));
                    sendWofkflow = sm_ApprovalProcess_WorkflowDao.findById(sendId);
                } else// 条件没找到
                {
                    sendId = nextApprovalProcess_Workflow.getTableId();
                    sendWofkflow = nextApprovalProcess_Workflow;
                }
            } else// 没有条件
            {
                sendId = nextApprovalProcess_Workflow.getTableId();
                sendWofkflow = nextApprovalProcess_Workflow;
            }

            sm_ApprovalProcess_AF.setCurrentIndex(sendId);// 当前流程处于哪个节点

            approvalProcess_Workflow.setUserOperate(null);
            approvalProcess_Workflow.setOperateTimeStamp(null);
            approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.Completed);// 当前节点状态
            approvalProcess_Workflow.setSendId(sendId);// 下一环节节点Id
            approvalProcess_Workflow.setRejectNodeId(null);

            sendWofkflow.setBusiState(S_WorkflowBusiState.Examining);// 下一个节点状态
            sendWofkflow.setSourceId(approvalProcess_Workflow.getTableId());
            sm_ApprovalProcess_WorkflowDao.save(sendWofkflow);// 下一个环节
        } else {
            // 当前节点是最终节点
            sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.Completed);
            sm_ApprovalProcess_AF.setCurrentIndex(approvalProcess_Workflow.getTableId()); // 当前流程处于哪个节点

            approvalProcess_Workflow.setUserOperate(null);
            approvalProcess_Workflow.setOperateTimeStamp(null);
            approvalProcess_Workflow.setRejectNodeId(null);
            approvalProcess_Workflow.setLastUpdateTimeStamp(System.currentTimeMillis());
            approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.Completed);

        }
        approvalProcess_Workflow.setUserStart(null);
        approvalProcess_Workflow.setLastAction(theAction);
        approvalProcess_Workflow.setUserUpdate(userOperate);
        if (approvalModel == 0) {
            approvalProcess_Workflow.setLastUpdateTimeStamp(System.currentTimeMillis());
        }
    }

    // 驳回
    public void rejectExecute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow, Sm_User userOperate,
                              Integer theAction) {
        Long currentNodeId = approvalProcess_Workflow.getTableId();

        Long sourceId = approvalProcess_Workflow.getSourceId();

        Sm_ApprovalProcess_Workflow sourceWorkflow = sm_ApprovalProcess_WorkflowDao.findById(sourceId);// 来源节点

        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();

        Integer rejectModel = approvalProcess_Workflow.getRejectModel(); // 驳回模式

        if (sourceId == null) {
            // 驳回到发起人
            sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.WaitSubmit);

            approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.WaitSubmit);
            approvalProcess_Workflow.setUserOperate(null);
            approvalProcess_Workflow.setOperateTimeStamp(null);

            sm_ApprovalProcess_AF.getWorkFlowList().get(0).setBusiState(S_WorkflowBusiState.WaitSubmit); // 第一个节点设置为待办

            if (!S_BusiCode.busiCode9.equals(sm_ApprovalProcess_AF.getBusiCode())) {
                setApprovalState(sm_ApprovalProcess_AF.getSourceType(), sm_ApprovalProcess_AF.getSourceId());
            }
        } else {
            if (rejectModel == 0) {
                // 驳回到发起人
                sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.WaitSubmit);

                approvalProcess_Workflow.setSourceId(null);
                approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.WaitSubmit);

                sm_ApprovalProcess_AF.getWorkFlowList().get(0).setBusiState(S_WorkflowBusiState.WaitSubmit); // 第一个节点设置为待办

                if (!S_BusiCode.busiCode9.equals(sm_ApprovalProcess_AF.getBusiCode())) {
                    setApprovalState(sm_ApprovalProcess_AF.getSourceType(), sm_ApprovalProcess_AF.getSourceId());
                }
            } else {
                // 驳回到上一个环节
                sm_ApprovalProcess_AF.setCurrentIndex(sourceId);// 当前流程处于哪个节点

                approvalProcess_Workflow.setSourceId(null);
                approvalProcess_Workflow.setBusiState(S_WorkflowBusiState.WaitSubmit);

                sourceWorkflow.setBusiState(S_WorkflowBusiState.Examining);// 驳回给来源节点
                sourceWorkflow.setRejectNodeId(currentNodeId);
                sourceWorkflow.setSendId(null);
                sourceWorkflow.setUserUpdate(userOperate);
                sourceWorkflow.setLastUpdateTimeStamp(System.currentTimeMillis());
                sm_ApprovalProcess_WorkflowDao.save(sourceWorkflow);// 来源节点
            }
        }
        approvalProcess_Workflow.setUserStart(null);
        approvalProcess_Workflow.setLastAction(theAction); // 驳回
        approvalProcess_Workflow.setUserUpdate(userOperate);
        approvalProcess_Workflow.setLastUpdateTimeStamp(System.currentTimeMillis());
    }

    public void noPassExecute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow,
                              Sm_ApprovalProcess_AF sm_ApprovalProcess_AF) {
        List<Sm_ApprovalProcess_Workflow> approvalProcess_WorkflowList = sm_ApprovalProcess_AF.getWorkFlowList();
        if (approvalProcess_WorkflowList != null && !approvalProcess_WorkflowList.isEmpty()) {
            for (Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow : approvalProcess_WorkflowList) {
                sm_approvalProcess_workflow.setBusiState(S_WorkflowBusiState.Completed);
                sm_ApprovalProcess_WorkflowDao.save(sm_approvalProcess_workflow);
            }
        }

        sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.NoPass); // 审批状态：不通过
        sm_ApprovalProcess_AF.setLastUpdateTimeStamp(System.currentTimeMillis());

        sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);

        /**
         * 审批流程 --- 修改Po 业务状态 和审批状态
         */
        noPassSetApprovalState(sm_ApprovalProcess_AF);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setApprovalState(String className, Long sourceId) {
        try {
            Class expectObjClass = Class.forName(className);
            Object queryObject = sm_BaseDao.findById(expectObjClass, sourceId);
            Field[] declaredFields = queryObject.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.getName().equals("approvalState")) {
                    try {
                        declaredField.set(queryObject, S_ApprovalState.WaitSubmit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            sm_BaseDao.update(queryObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审批流程 --- 修改Po 业务状态 和审批状态
     */
    public void noPassSetApprovalState(Sm_ApprovalProcess_AF sm_approvalProcess_af) {
        Long sourceId = sm_approvalProcess_af.getSourceId();
        String className = sm_approvalProcess_af.getSourceType();
        Object queryObject = null;
        Object ossObject = null;
        try {
            Class expectObjClass = Class.forName(className);

            queryObject = sm_BaseDao.findById(expectObjClass, sourceId);

            Map<String, Object> queryMap = BeanUtil.beanToMap(queryObject);
            if (queryMap.get("busiState") != null && queryMap.get("busiState").equals(S_BusiState.NoRecord)) {
                queryMap.put("theState", S_TheState.Deleted); // 业务状态 ： 逻辑删除
                queryMap.put("approvalState", S_ApprovalState.NoPass); // 审批状态 ：不通过
            } else if (queryMap.get("busiState") != null && queryMap.get("busiState").equals(S_BusiState.HaveRecord)) {
                queryMap.put("approvalState", S_ApprovalState.Completed);
            }

            Object oldObject = (Object)BeanUtil.mapToBean(queryMap, expectObjClass, true);
            BeanCopier beanCopier = BeanCopier.create(expectObjClass, expectObjClass, false);
            beanCopier.copy(oldObject, queryObject, null);

            sm_BaseDao.save(queryObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLimitAmout(Sm_ApprovalProcess_RecordForm model,
                                  Sm_ApprovalProcess_Workflow approvalProcess_Workflow, MsgInfo msgInfo) {
        Map<String, Object> extraObj = model.getExtraObj();
        Sm_ApprovalProcess_AF approvalProcess_af = approvalProcess_Workflow.getApprovalProcess_AF();
        // Integer workFlowNowIndex = approvalProcessGetWorkFlowUtil.getWorkFlowNowIndex(approvalProcess_af);
        // if(workFlowNowIndex==null){
        // msgInfo.setSuccess(false);
        // msgInfo.setInfo("申请单节点没有找到");
        // return;
        // }
        // int index=workFlowNowIndex;
        String nowWorkFlowName = approvalProcessGetWorkFlowUtil.getNowWorkFlowName(approvalProcess_af);
        if (nowWorkFlowName == null) {
            return;
        }
        System.out.println("nowWorkFlowName is " + nowWorkFlowName);
        Long tableId = Long.parseLong((String)extraObj.get("tableId"));
        String acceptExplain = (String)extraObj.get("acceptExplain");
        String appointExplain = (String)extraObj.get("appointExplain");
        String sceneInvestigationExplain = (String)extraObj.get("sceneInvestigationExplain");
        String appointTimeString = (String)extraObj.get("appointTimeString");
        List<LinkedHashMap<String, Object>> sourceList =
                (List<LinkedHashMap<String, Object>>)extraObj.get("attachMent");
        Empj_BldLimitAmount_AF limitAmountAf = bldLimitAmountAfDao.findById(tableId);
        Long startTimeStamp = approvalProcess_af.getStartTimeStamp();// 提交时间
        Integer theAction = model.getTheAction();
        switch (nowWorkFlowName) {
            case "CS":// 初审，设置受理时间、受理说明、预约时间、预约说明
                System.out.println("in handleExtraInfo in case 1");
                if (theAction.equals(S_ApprovalAction.theAction_Pass)) {// 如果是审批通过的情况下要判断
                    long acceptTimeStamp = System.currentTimeMillis();

                    // 判断提交时间是周几
                    // String week = getWeek(startTimeStamp);
                    // if("周四".equals(week)||"周五".equals(week))
                    // {
                    // //如果提交时间是周四或者周五，则需要在原来的工作日上再加两天（周六、周日）
                    //
                    // }
                    // Long nextWorkDayEnd = getNextWorkDayUtil.getNextWorkDayEnd(startTimeStamp);
                    Long nextWorkDayEnd = getNextWorkDayUtil.getNextTwoWorkDayEnd(startTimeStamp);
                    if (System.currentTimeMillis() > nextWorkDayEnd) {// 目前时间大于了一个工作日
                        if (StringUtils.isEmpty(acceptExplain)) {// 受理说明为空
                            msgInfo.setSuccess(false);
                            msgInfo.setInfo("受理时间大于两个工作日，需要填写受理说明");
                            return;
                        }
                    }
                    if (StringUtils.isEmpty(appointTimeString)) {
                        msgInfo.setSuccess(false);
                        msgInfo.setInfo("请选择预约时间");
                        return;
                    }
                    Long appointTimeStamp = myDatetime.stringMinuteToLong(appointTimeString);
                    if (appointTimeStamp < acceptTimeStamp) {// 如果预约时间小于受理时间
                        msgInfo.setSuccess(false);
                        msgInfo.setInfo("预约时间不能小于当前时间");
                        return;
                    }
                    if (appointTimeStamp > startTimeStamp + myDatetime.calculateHours(36)) {// 如果预约时间大于提交申请后的36小时
                        if (StringUtils.isEmpty(appointExplain)) {// 如果预约说明为空
                            msgInfo.setSuccess(false);
                            msgInfo.setInfo("预约时间晚于提交申请后的36小时，需要填写说明");
                            return;
                        }
                    }
                    limitAmountAf.setAcceptTimeStamp(acceptTimeStamp);// 设置受理时间
                    if (StringUtils.isNotEmpty(acceptExplain)) {
                        limitAmountAf.setAcceptExplain(acceptExplain);// 设置受理原因
                    }
                    if (StringUtils.isNotEmpty(appointExplain)) {
                        limitAmountAf.setAppointExplain(appointExplain);// 设置预约原因
                    }
                    limitAmountAf.setAppointTimeStamp(appointTimeStamp);// 设置预约时间
                } else {
                    return;
                }
                break;
            case "TK":// 探勘，需要上传图片
                System.out.println("in handleExtraInfo in case 2");
                if (theAction.equals(S_ApprovalAction.theAction_Pass)) {// 如果是审批通过的情况下要判断是否到预约时间等等
                    Long appointTimeStampInDb = limitAmountAf.getAppointTimeStamp();// 数据库中存的该对象的预约时间
                    if (appointTimeStampInDb > System.currentTimeMillis()) {// 如果实体勘察的审批时间小于预约时间
                        msgInfo.setSuccess(false);
                        msgInfo.setInfo("还没有到预约时间，无法进行进度见证审批");
                        return;
                    }
                    String sceneAttachmentEcode = "010201N18112400005";// attachmentCfg.geteCode();
                    String witnessAttachmentEcode = "010201N18112400004";// attachmentCfg.geteCode();
                    Sm_AttachmentForm[] sm_attachmentFormUploadList = new Sm_AttachmentForm[sourceList.size()];
                    boolean isHasSceneAttachment = false;
                    boolean isHasWitnessAttachment = false;
                    for (int i = 0; i < sm_attachmentFormUploadList.length; i++) {
                        LinkedHashMap<String, Object> sourceAttachment = sourceList.get(i);
                        String sourceEcode = (String)sourceAttachment.get("sourceType");
                        if (sourceEcode.equals(sceneAttachmentEcode)) {
                            isHasSceneAttachment = true;
                        } else if (sourceEcode.equals(witnessAttachmentEcode)) {
                            isHasWitnessAttachment = true;
                        }
                        Sm_AttachmentForm sm_attachmentForm = new Sm_AttachmentForm();
                        sm_attachmentForm.setBusiType((String)sourceAttachment.get("busiType"));
                        sm_attachmentForm.setFileType((String)sourceAttachment.get("fileType"));
                        sm_attachmentForm.setRemark((String)sourceAttachment.get("remark"));
                        sm_attachmentForm.setSourceType((String)sourceAttachment.get("sourceType"));
                        sm_attachmentForm.setTableId(((Integer)sourceAttachment.get("tableId")).longValue());
                        sm_attachmentForm.setTheLink((String)sourceAttachment.get("theLink"));
                        sm_attachmentForm.setTheSize((String)sourceAttachment.get("theSize"));
                        sm_attachmentFormUploadList[i] = sm_attachmentForm;
                    }
                    if (!isHasSceneAttachment) {
                        msgInfo.setSuccess(false);
                        msgInfo.setInfo("请上传实地勘察照片");
                        return;
                    }
                    if (!isHasWitnessAttachment) {
                        msgInfo.setSuccess(false);
                        msgInfo.setInfo("请上传见证报告");
                        return;
                    }
                    // 保存附件信息
                    Empj_BldLimitAmount_AFForm limitAmount_afForm = new Empj_BldLimitAmount_AFForm();
                    limitAmount_afForm.setBusiType(S_BusiCode.busiCode_03030101);
                    limitAmount_afForm.setGeneralAttachmentList(sm_attachmentFormUploadList);
                    limitAmount_afForm.setUserId(model.getUserId());
                    sm_AttachmentBatchAddService.execute(limitAmount_afForm, tableId);
                    // 检查是否要填写勘察说明
                    int appointHour = myDatetime.getHour(appointTimeStampInDb);// 获取预约时候的小时数
                    long nowTime = System.currentTimeMillis();
                    if (appointHour < 12) {// 如果预约时间小于12点，当天必须上传
                        long dayEndTimeStamp = myDatetime.getDayEndTimeStamp(appointTimeStampInDb);
                        if (nowTime > dayEndTimeStamp) {// 如果超过了当天，必须填写说明
                            if (StringUtils.isEmpty(sceneInvestigationExplain)) {// 如果没有填写说明
                                msgInfo.setSuccess(false);
                                msgInfo.setInfo("审批时间超过了当天预约时间，必须填写实地勘察说明");
                                return;
                            }
                        } else {

                        }
                    } else {// 如果预约时间大于12点则必须在下一个工作日的12点之前上传完成
                        Long nextWorkDayStart = getNextWorkDayUtil.getNextWorkDayStart(appointTimeStampInDb);
                        long newWorkDay12Hour = nextWorkDayStart + myDatetime.calculateHours(12);// 预约时间下一个工作日的12点
                        if (nowTime > newWorkDay12Hour) {// 如果目前时间大于了下个工作日的12点则必须上传说明
                            if (StringUtils.isEmpty(sceneInvestigationExplain)) {// 如果没有填写说明
                                msgInfo.setSuccess(false);
                                msgInfo.setInfo("审批时间超过了预约时间后的一个工作日的12点，必须填写实地勘察说明");
                                return;
                            } else {

                            }
                        }
                    }
                    limitAmountAf.setSceneInvestigationTimeStamp(System.currentTimeMillis());// 设置勘查时间
                    if (StringUtils.isNotEmpty(sceneInvestigationExplain)) {
                        limitAmountAf.setSceneInvestigationExplain(sceneInvestigationExplain);// 设置勘查说明
                    }
                } else {// 如果是不通过或者驳回

                    Sm_AttachmentForm[] sm_attachmentFormUploadList = new Sm_AttachmentForm[sourceList.size()];
                    for (int i = 0; i < sm_attachmentFormUploadList.length; i++) {
                        LinkedHashMap<String, Object> sourceAttachment = sourceList.get(i);
                        Sm_AttachmentForm sm_attachmentForm = new Sm_AttachmentForm();
                        sm_attachmentForm.setBusiType((String)sourceAttachment.get("busiType"));
                        sm_attachmentForm.setFileType((String)sourceAttachment.get("fileType"));
                        sm_attachmentForm.setRemark((String)sourceAttachment.get("remark"));
                        sm_attachmentForm.setSourceType((String)sourceAttachment.get("sourceType"));
                        sm_attachmentForm.setTableId(((Integer)sourceAttachment.get("tableId")).longValue());
                        sm_attachmentForm.setTheLink((String)sourceAttachment.get("theLink"));
                        sm_attachmentForm.setTheSize((String)sourceAttachment.get("theSize"));
                        sm_attachmentFormUploadList[i] = sm_attachmentForm;
                    }
                    // 保存附件信息
                    Empj_BldLimitAmount_AFForm limitAmount_afForm = new Empj_BldLimitAmount_AFForm();
                    limitAmount_afForm.setBusiType(S_BusiCode.busiCode_03030101);
                    limitAmount_afForm.setGeneralAttachmentList(sm_attachmentFormUploadList);
                    limitAmount_afForm.setUserId(model.getUserId());
                    sm_AttachmentBatchAddService.execute(limitAmount_afForm, tableId);

                    return;// 直接返回成功
                }
                break;
            default:
                System.out.println("in default");
                break;
        }
        bldLimitAmountAfDao.save(limitAmountAf);
    }

    private void handleBuildingAvgPrice(MsgInfo msgInfo, Map<String, Object> extraObj) {
        Long tableId = Long.parseLong((String)extraObj.get("tableId"));
        Long buildingId = ((Integer)extraObj.get("buildingId")).longValue();
        String busiCode = (String)extraObj.get("busiCode");
        if (busiCode.equals(S_BusiCode.busiCode_03010301)) {
            System.out.println("handle Tgpj_BuildingAvgPrice add approval");
            Empj_BuildingInfo buildingInfo = buildingInfoDao.findById(buildingId);
            String eCodeFromPresellCert = buildingInfo.geteCodeFromPresellCert();
            if (StringUtils.isEmpty(eCodeFromPresellCert)) {// 如果预售证号为空
                // properties.put("noPresellCert", "该楼幢的预售证号为空");
                msgInfo.setExtra("该楼幢的预售证号为空");
            }
        }
    }

    /*
     * xsz by time 2019-4-9 10:33:22
     * 获取当前时间是周几
     */
    private String getWeek(Long time) {
        String week = "";
        Date today = null;
        if (null == time) {
            today = new Date();
        } else {
            today = new Date(time);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            week = "周日";
        } else if (weekday == 2) {
            week = "周一";
        } else if (weekday == 3) {
            week = "周二";
        } else if (weekday == 4) {
            week = "周三";
        } else if (weekday == 5) {
            week = "周四";
        } else if (weekday == 6) {
            week = "周五";
        } else if (weekday == 7) {
            week = "周六";
        }
        return week;
    }
}

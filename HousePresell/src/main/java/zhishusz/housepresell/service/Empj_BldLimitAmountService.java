
package zhishusz.housepresell.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaominfo.oss.sdk.OSSClientProperty;

import cn.hutool.core.util.ReUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmountForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_DtlForm;
import zhishusz.housepresell.controller.form.Empj_NodePredictionForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.controller.form.extra.BldLimitAmountAttachementForm;
import zhishusz.housepresell.controller.form.extra.BldLimitAmountForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_DtlDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_NodePredictionDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Qs_BldLimitAmount_ViewDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_NodePrediction;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Qs_BldLimitAmount_View;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg_Copy;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.extra.ReportVO;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_Regex;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.NoteChangeReportExcelVO;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgDataInfo;

/*
 * Service添加操作：申请表-进度节点变更 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_BldLimitAmountService {

    private static final String BUSI_CODE = "03030100";
    @Autowired
    private Empj_BldLimitAmountDao empj_BldLimitAmountDao;
    @Autowired
    private Qs_BldLimitAmount_ViewDao empj_BldLimitAmount_ViewDao;
    @Autowired
    private Empj_BldLimitAmount_DtlDao empj_BldLimitAmount_DtlDao;
    @Autowired
    private Sm_UserDao sm_UserDao;
    @Autowired
    private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
    @Autowired
    private Empj_ProjectInfoDao empj_ProjectInfoDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_bldLimitAmountVer_afDtlDao;
    @Autowired
    private Sm_AttachmentCfgDao smAttachmentCfgDao;
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    @Autowired
    private Empj_NodePredictionDao empj_NodePredictionDao;

    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private OSSClientProperty oss;

    @Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;

    @Autowired
    private ExportPdfByWordService exportPdfByWordService;// 生成PDF

    /**
     * @param model
     * @return
     */
    public Properties execute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        Sm_User user = model.getUser();
        long nowTimeStamp = System.currentTimeMillis();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 新增保存
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties saveExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();
        long nowTimeStamp = System.currentTimeMillis();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyMMdd");
        Date d = new Date();
        String dateStr = formatDate.format(d);

        Sm_User user = model.getUser();

        Long developCompanyId = model.getDevelopCompanyId();
        if (null == developCompanyId) {
            return MyBackInfo.fail(properties, "请选择开发企业！");
        }
        Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(developCompanyId);
        if (null == emmp_CompanyInfo) {
            return MyBackInfo.fail(properties, "未查询到所选企业信息，请重新选择！");
        }
        Long projectId = model.getProjectId();
        if (null == projectId) {
            return MyBackInfo.fail(properties, "请选择项目！");
        }
        Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
        if (null == empj_ProjectInfo) {
            return MyBackInfo.fail(properties, "未查询到所选项目信息，请重新选择！");
        }

        String countUnit = model.getCountUnit();
        if (StringUtils.isBlank(countUnit)) {
            return MyBackInfo.fail(properties, "请输入总包单位！");
        }

        String contactOne = model.getContactOne();
        if (StringUtils.isBlank(contactOne)) {
            return MyBackInfo.fail(properties, "请输入联系人A！");
        }
        String contactTwo = model.getContactTwo();
        if (StringUtils.isBlank(contactTwo)) {
            return MyBackInfo.fail(properties, "请输入联系人B！");
        }
        String telephoneOne = model.getTelephoneOne();
        if (StringUtils.isBlank(telephoneOne)) {
            return MyBackInfo.fail(properties, "请输入A联系方式！");
        }

        /*boolean matchOne = ReUtil.isMatch(S_Regex.PhoneNumber, telephoneOne);
        if(!matchOne){
            return MyBackInfo.fail(properties, "请输入正确的A联系方式！");
        }*/

        if(telephoneOne.trim().length() != 11){
        	return MyBackInfo.fail(properties, "请输入正确的A联系方式！");
        }

        String telephoneTwo = model.getTelephoneTwo();
        if (StringUtils.isBlank(telephoneTwo)) {
            return MyBackInfo.fail(properties, "请输入B联系方式！");
        }
        /*boolean matchTwo = ReUtil.isMatch(S_Regex.PhoneNumber, telephoneTwo);
        if(!matchTwo){
            return MyBackInfo.fail(properties, "请输入正确的B联系方式！");
        }*/
        if(telephoneTwo.trim().length() != 11){
        	return MyBackInfo.fail(properties, "请输入正确的B联系方式！");
        }

        List<BldLimitAmountForm> dtlList = model.getDtlList();
        if (null == dtlList || dtlList.size() < 1) {
            return MyBackInfo.fail(properties, "请选择需要变更的楼幢信息！");
        }

        /*
         * 保存主表信息
         */
        Empj_BldLimitAmount bldLimitAmount = new Empj_BldLimitAmount();
        bldLimitAmount.setTheState(S_TheState.Normal);
        bldLimitAmount.setApprovalState(S_ApprovalState.WaitSubmit);
        bldLimitAmount.setBusiState("未备案");

        bldLimitAmount.setCreateTimeStamp(nowTimeStamp);
        bldLimitAmount.setLastUpdateTimeStamp(nowTimeStamp);
        bldLimitAmount.setUserStart(user);
        bldLimitAmount.setUserUpdate(user);

        bldLimitAmount.setContactOne(contactOne);
        bldLimitAmount.setContactTwo(contactTwo);
        bldLimitAmount.setTelephoneOne(telephoneOne);
        bldLimitAmount.setTelephoneTwo(telephoneTwo);

        bldLimitAmount.setCountUnit(countUnit);

        bldLimitAmount.setDevelopCompany(emmp_CompanyInfo);
        bldLimitAmount.seteCodeOfDevelopCompany(emmp_CompanyInfo.geteCode());

        bldLimitAmount.setProject(empj_ProjectInfo);
        bldLimitAmount.seteCodeOfProject(empj_ProjectInfo.geteCode());
        bldLimitAmount.setTheNameOfProject(empj_ProjectInfo.getTheName());

        /*
         * 保存申请信息
         */
        Empj_BldLimitAmount_Dtl dtl;
        Empj_BuildingInfo buildingInfo;
        Long buildingId;
        List<BldLimitAmountAttachementForm> attachementList;
        Sm_Attachment attachment;
        Sm_AttachmentCfgForm form;
        Sm_AttachmentCfg sm_AttachmentCfg;
        Empj_NodePredictionForm nodePredictionForm;
        Integer countNum;
        /*
         * 校验信息
         */
        for (BldLimitAmountForm bldAmount : dtlList) {

            buildingId = bldAmount.getBuildingId();
            buildingInfo = empj_BuildingInfoDao.findById(buildingId);
            if (null == buildingInfo) {
                return MyBackInfo.fail(properties, "存在无效的楼幢信息！");
            }

            // 支付保函
            Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_BuildingInfo(buildingInfo);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao.findByPage(
                empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQLNoRecord(), childModel));
            if (!childList.isEmpty()) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + "已发起支付保函申请！");
            }

            Long limitedId = bldAmount.getLimitedId();
            if (null == limitedId || limitedId < 0) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 未选择变更形象进度！");
            }
            Tgpj_BldLimitAmountVer_AFDtl versionAfDtl = tgpj_bldLimitAmountVer_afDtlDao.findById(limitedId);
            if (null == versionAfDtl) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 未查询到有效的形象进度！");
            }

            attachementList = bldAmount.getAttachementList();
            if (null == attachementList || attachementList.size() < 1) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 附件信息未上传！");
            }

            // 校验楼幢是否维护预测节点数据
            nodePredictionForm = new Empj_NodePredictionForm();
            nodePredictionForm.setTheState(S_TheState.Normal);
            nodePredictionForm.setBuildingId(buildingId);
            countNum = empj_NodePredictionDao.findByPage_Size(
                empj_NodePredictionDao.getQuery_Size(empj_NodePredictionDao.getBasicHQL(), nodePredictionForm));
            /*if (countNum < 1) {
                return MyBackInfo.fail(properties, "请先维护楼幢：" + buildingInfo.geteCodeFromConstruction() + " 预测节点信息！");
            }*/

            // 校验附件的必传项
            Sm_AttachmentCfgForm cfgForm = new Sm_AttachmentCfgForm();
            cfgForm.setTheState(S_TheState.Normal);
            cfgForm.setBusiType("03030101");
            List<Sm_AttachmentCfg> sm_AttachmentCfgList =
                smAttachmentCfgDao.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), cfgForm));

            List<String> attString = new ArrayList<>();
            if (null != attachementList && attachementList.size() > 0) {
                for (BldLimitAmountAttachementForm bldLimitAmountAttachementForm : attachementList) {
                    attString.add(bldLimitAmountAttachementForm.getSourceType());
                }
            }

            for (Sm_AttachmentCfg sm_AttachmentCfg2 : sm_AttachmentCfgList) {
                if (!attString.contains(sm_AttachmentCfg2.geteCode()) && (sm_AttachmentCfg2.getIsNeeded())) {
                    return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + "--"
                        + sm_AttachmentCfg2.getTheName() + " 附件信息未上传！");
                }
            }

        }

        empj_BldLimitAmountDao.save(bldLimitAmount);
        bldLimitAmount.seteCode("03030100N" + dateStr + String.format("%06d", bldLimitAmount.getTableId().intValue()));

        // 业务办理码
        bldLimitAmount.setBusinessCode(bldLimitAmount.geteCode().substring(bldLimitAmount.geteCode().length() - 4));
        bldLimitAmount.setBuildCount(dtlList.size());
        empj_BldLimitAmountDao.update(bldLimitAmount);

        /*
         * 正式保存信息
         */
        List<Empj_NodePrediction> nodeList;
        Tgpj_BuildingAccount buildingAccount;
        Tgpj_BldLimitAmountVer_AFDtl versionAfDtl;
        Long limitedId;
        Tgpj_BldLimitAmountVer_AFDtlForm afDtlModel;
        List<Tgpj_BldLimitAmountVer_AFDtl> afDtlList;
        for (BldLimitAmountForm bldAmount : dtlList) {
            dtl = new Empj_BldLimitAmount_Dtl();

            dtl.setTheState(S_TheState.Normal);
            dtl.setApprovalState(S_ApprovalState.WaitSubmit);
            dtl.setBusiState("未备案");

            dtl.setCreateTimeStamp(nowTimeStamp);
            dtl.setLastUpdateTimeStamp(nowTimeStamp);
            dtl.setUserStart(user);
            dtl.setUserUpdate(user);

            dtl.setDevelopCompany(emmp_CompanyInfo);
            dtl.seteCodeOfDevelopCompany(emmp_CompanyInfo.geteCode());

            dtl.setProject(empj_ProjectInfo);
            dtl.seteCodeOfProject(empj_ProjectInfo.geteCode());
            dtl.setTheNameOfProject(empj_ProjectInfo.getTheName());

            dtl.setMainTable(bldLimitAmount);
            dtl.seteCodeOfMainTable(bldLimitAmount.geteCode());

            buildingId = bldAmount.getBuildingId();
            buildingInfo = empj_BuildingInfoDao.findById(buildingId);

            dtl.setBuilding(buildingInfo);
            dtl.seteCodeOfBuilding(buildingInfo.geteCode());
            dtl.setUpfloorNumber(buildingInfo.getUpfloorNumber());
            dtl.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
            dtl.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());

            limitedId = bldAmount.getLimitedId();
            versionAfDtl = tgpj_bldLimitAmountVer_afDtlDao.findById(limitedId);

            dtl.setExpectFigureProgress(versionAfDtl);
            dtl.setExpectLimitedRatio(versionAfDtl.getLimitedAmount());

            /*
             * 获取变更前的楼幢数据
             */
            buildingAccount = buildingInfo.getBuildingAccount();

            dtl.setOrgLimitedAmount(
                null == buildingAccount.getOrgLimitedAmount() ? 0.00 : buildingAccount.getOrgLimitedAmount());
            dtl.setCurrentFigureProgress(
                null == buildingAccount.getCurrentFigureProgress() ? "" : buildingAccount.getCurrentFigureProgress());
            dtl.setCurrentLimitedRatio(
                null == buildingAccount.getCurrentLimitedRatio() ? 0.00 : buildingAccount.getCurrentLimitedRatio());
            dtl.setNodeLimitedAmount(
                null == buildingAccount.getNodeLimitedAmount() ? 0.00 : buildingAccount.getNodeLimitedAmount());
            dtl.setTotalGuaranteeAmount(
                null == buildingAccount.getTotalGuaranteeAmount() ? 0.00 : buildingAccount.getTotalGuaranteeAmount());
            dtl.setCashLimitedAmount(
                null == buildingAccount.getCashLimitedAmount() ? 0.00 : buildingAccount.getCashLimitedAmount());
            dtl.setEffectiveLimitedAmount(null == buildingAccount.getEffectiveLimitedAmount() ? 0.00
                : buildingAccount.getEffectiveLimitedAmount());

            /*
             * 维护预测下个节点信息
             */
            /*nodePredictionForm = new Empj_NodePredictionForm();
            nodePredictionForm.setTheState(S_TheState.Normal);
            nodePredictionForm.setBuildingId(buildingId);
            nodeList = empj_NodePredictionDao.findByPage(
                empj_NodePredictionDao.getQuery(empj_NodePredictionDao.getBasicDescHQL(), nodePredictionForm));
            for (Empj_NodePrediction empj_NodePrediction : nodeList) {
                Double expectLimitedRatio = empj_NodePrediction.getExpectLimitedRatio();
                if (versionAfDtl.getLimitedAmount() - expectLimitedRatio > 0.00) {
                    dtl.setPredictionNode(empj_NodePrediction.getExpectFigureProgress());
                    dtl.setPredictionNodeName(empj_NodePrediction.getExpectLimitedName());
                    dtl.setCompleteDateOne(empj_NodePrediction.getCompleteDate());
                    dtl.setCompleteDateTwo(empj_NodePrediction.getCompleteDate());

                    break;
                }
            }*/

            afDtlModel = new Tgpj_BldLimitAmountVer_AFDtlForm();
            afDtlModel.setTheState(S_TheState.Normal);
            afDtlModel.setOrderBy("limitedAmount");
            afDtlModel.setBldLimitAmountVerMng(versionAfDtl.getBldLimitAmountVerMng());
            afDtlModel.setBldLimitAmountVerMngId(versionAfDtl.getBldLimitAmountVerMng().getTableId());
            afDtlList = tgpj_bldLimitAmountVer_afDtlDao.findByPage(tgpj_bldLimitAmountVer_afDtlDao.getQuery(tgpj_bldLimitAmountVer_afDtlDao.getBasicDescHQL(), afDtlModel));
            for (Tgpj_BldLimitAmountVer_AFDtl tgpj_BldLimitAmountVer_AFDtl : afDtlList) {
                Double limitedAmount = tgpj_BldLimitAmountVer_AFDtl.getLimitedAmount();
                if (versionAfDtl.getLimitedAmount() - limitedAmount > 0.00) {
                    dtl.setPredictionNode(tgpj_BldLimitAmountVer_AFDtl);
                    dtl.setPredictionNodeName(tgpj_BldLimitAmountVer_AFDtl.getStageName());
//                    dtl.setCompleteDateOne(empj_NodePrediction.getCompleteDate());
//                    dtl.setCompleteDateTwo(empj_NodePrediction.getCompleteDate());

                    break;
                }
            }



            empj_BldLimitAmount_DtlDao.save(dtl);

            attachementList = bldAmount.getAttachementList();
            if (null != attachementList && attachementList.size() > 0) {
                for (BldLimitAmountAttachementForm bldLimitAmountAttachementForm : attachementList) {
                    attachment = new Sm_Attachment();
                    // 查询附件配置表
                    form = new Sm_AttachmentCfgForm();
                    form.seteCode(bldLimitAmountAttachementForm.getSourceType());
                    sm_AttachmentCfg = smAttachmentCfgDao
                        .findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

                    attachment.setTheLink(bldLimitAmountAttachementForm.getTheLink());
                    attachment.setTheSize(bldLimitAmountAttachementForm.getTheSize());
                    attachment.setFileType(bldLimitAmountAttachementForm.getFileType());
                    attachment.setSourceType(bldLimitAmountAttachementForm.getSourceType());
                    attachment.setAttachmentCfg(sm_AttachmentCfg);
                    attachment.setSourceId(dtl.getTableId().toString());
                    attachment.setBusiType("03030100");// 业务类型
                    attachment.setTheState(S_TheState.Normal);
                    attachment.setRemark(bldLimitAmountAttachementForm.getRemark());
                    attachment.setUserStart(user);// 创建人
                    attachment.setUserUpdate(user);// 操作人
                    attachment.setCreateTimeStamp(nowTimeStamp);
                    attachment.setLastUpdateTimeStamp(nowTimeStamp);
                    sm_AttachmentDao.save(attachment);
                }
            }
        }

        properties.put("tableId", bldLimitAmount.getTableId());
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;
    }

    /**
     * 新增保存
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties saveExecuteCopy(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();
        long nowTimeStamp = System.currentTimeMillis();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyMMdd");
        Date d = new Date();
        String dateStr = formatDate.format(d);

        Sm_User user = model.getUser();

        Long developCompanyId = model.getDevelopCompanyId();
        if (null == developCompanyId) {
            return MyBackInfo.fail(properties, "请选择开发企业！");
        }
        Emmp_CompanyInfo emmp_CompanyInfo = emmp_CompanyInfoDao.findById(developCompanyId);
        if (null == emmp_CompanyInfo) {
            return MyBackInfo.fail(properties, "未查询到所选企业信息，请重新选择！");
        }
        Long projectId = model.getProjectId();
        if (null == projectId) {
            return MyBackInfo.fail(properties, "请选择项目！");
        }
        Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(projectId);
        if (null == empj_ProjectInfo) {
            return MyBackInfo.fail(properties, "未查询到所选项目信息，请重新选择！");
        }

        String countUnit = model.getCountUnit();
        if (StringUtils.isBlank(countUnit)) {
            return MyBackInfo.fail(properties, "请输入总包单位！");
        }

        String contactOne = model.getContactOne();
        if (StringUtils.isBlank(contactOne)) {
            return MyBackInfo.fail(properties, "请输入联系人A！");
        }
        String contactTwo = model.getContactTwo();
        if (StringUtils.isBlank(contactTwo)) {
            return MyBackInfo.fail(properties, "请输入联系人B！");
        }
        String telephoneOne = model.getTelephoneOne();
        if (StringUtils.isBlank(telephoneOne)) {
            return MyBackInfo.fail(properties, "请输入A联系方式！");
        }
        String telephoneTwo = model.getTelephoneTwo();
        if (StringUtils.isBlank(telephoneTwo)) {
            return MyBackInfo.fail(properties, "请输入B联系方式！");
        }

        List<BldLimitAmountForm> dtlList = model.getDtlList();
        if (null == dtlList || dtlList.size() < 1) {
            return MyBackInfo.fail(properties, "请选择需要变更的楼幢信息！");
        }

        /*
         * 保存主表信息
         */
        Empj_BldLimitAmount bldLimitAmount = new Empj_BldLimitAmount();
        bldLimitAmount.setTheState(S_TheState.Normal);
        bldLimitAmount.setApprovalState(S_ApprovalState.WaitSubmit);
        bldLimitAmount.setBusiState("未备案");

        bldLimitAmount.setCreateTimeStamp(nowTimeStamp);
        bldLimitAmount.setLastUpdateTimeStamp(nowTimeStamp);
        bldLimitAmount.setUserStart(user);
        bldLimitAmount.setUserUpdate(user);

        bldLimitAmount.setContactOne(contactOne);
        bldLimitAmount.setContactTwo(contactTwo);
        bldLimitAmount.setTelephoneOne(telephoneOne);
        bldLimitAmount.setTelephoneTwo(telephoneTwo);

        bldLimitAmount.setCountUnit(countUnit);

        bldLimitAmount.setDevelopCompany(emmp_CompanyInfo);
        bldLimitAmount.seteCodeOfDevelopCompany(emmp_CompanyInfo.geteCode());

        bldLimitAmount.setProject(empj_ProjectInfo);
        bldLimitAmount.seteCodeOfProject(empj_ProjectInfo.geteCode());
        bldLimitAmount.setTheNameOfProject(empj_ProjectInfo.getTheName());

        /*
         * 保存申请信息
         */
        Empj_BldLimitAmount_Dtl dtl;
        Empj_BuildingInfo buildingInfo;
        Long buildingId;
        List<BldLimitAmountAttachementForm> attachementList;
        Sm_Attachment attachment;
        Sm_AttachmentCfgForm form;
        Sm_AttachmentCfg sm_AttachmentCfg;
        /*
         * 校验信息
         */
        for (BldLimitAmountForm bldAmount : dtlList) {

            buildingId = bldAmount.getBuildingId();
            buildingInfo = empj_BuildingInfoDao.findById(buildingId);
            if (null == buildingInfo) {
                return MyBackInfo.fail(properties, "存在无效的楼幢信息！");
            }

            // 支付保函
            Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_BuildingInfo(buildingInfo);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao.findByPage(
                empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQLNoRecord(), childModel));
            if (!childList.isEmpty()) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + "已发起支付保函申请！");
            }

            Long limitedId = bldAmount.getLimitedId();
            if (null == limitedId || limitedId < 0) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 未选择变更形象进度！");
            }
            Tgpj_BldLimitAmountVer_AFDtl versionAfDtl = tgpj_bldLimitAmountVer_afDtlDao.findById(limitedId);
            if (null == versionAfDtl) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 未查询到有效的形象进度！");
            }

            attachementList = bldAmount.getAttachementList();
            if (null == attachementList || attachementList.size() < 1) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 附件信息未上传！");
            }

            // 校验附件的必传项
            Sm_AttachmentCfgForm cfgForm = new Sm_AttachmentCfgForm();
            cfgForm.setTheState(S_TheState.Normal);
            cfgForm.setBusiType("03030101");
            List<Sm_AttachmentCfg> sm_AttachmentCfgList =
                smAttachmentCfgDao.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), cfgForm));

            List<String> attString = new ArrayList<>();
            if (null != attachementList && attachementList.size() > 0) {
                for (BldLimitAmountAttachementForm bldLimitAmountAttachementForm : attachementList) {
                    attString.add(bldLimitAmountAttachementForm.getSourceType());
                }
            }

            for (Sm_AttachmentCfg sm_AttachmentCfg2 : sm_AttachmentCfgList) {
                if (!attString.contains(sm_AttachmentCfg2.geteCode()) && (sm_AttachmentCfg2.getIsNeeded())) {
                    return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + "--"
                        + sm_AttachmentCfg2.getTheName() + " 附件信息未上传！");
                }
            }

        }

        empj_BldLimitAmountDao.save(bldLimitAmount);
        bldLimitAmount.seteCode("03030100N" + dateStr + String.format("%06d", bldLimitAmount.getTableId().intValue()));

        // 业务办理码
        bldLimitAmount.setBusinessCode(bldLimitAmount.geteCode().substring(bldLimitAmount.geteCode().length() - 4));
        empj_BldLimitAmountDao.update(bldLimitAmount);

        /*
         * 正式保存信息
         */
        for (BldLimitAmountForm bldAmount : dtlList) {
            dtl = new Empj_BldLimitAmount_Dtl();

            dtl.setTheState(S_TheState.Normal);
            dtl.setApprovalState(S_ApprovalState.WaitSubmit);
            dtl.setBusiState("未备案");

            dtl.setCreateTimeStamp(nowTimeStamp);
            dtl.setLastUpdateTimeStamp(nowTimeStamp);
            dtl.setUserStart(user);
            dtl.setUserUpdate(user);

            dtl.setDevelopCompany(emmp_CompanyInfo);
            dtl.seteCodeOfDevelopCompany(emmp_CompanyInfo.geteCode());

            dtl.setProject(empj_ProjectInfo);
            dtl.seteCodeOfProject(empj_ProjectInfo.geteCode());
            dtl.setTheNameOfProject(empj_ProjectInfo.getTheName());

            dtl.setMainTable(bldLimitAmount);
            dtl.seteCodeOfMainTable(bldLimitAmount.geteCode());

            buildingId = bldAmount.getBuildingId();
            buildingInfo = empj_BuildingInfoDao.findById(buildingId);

            dtl.setBuilding(buildingInfo);
            dtl.seteCodeOfBuilding(buildingInfo.geteCode());
            dtl.setUpfloorNumber(buildingInfo.getUpfloorNumber());
            dtl.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
            dtl.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());

            Long limitedId = bldAmount.getLimitedId();
            Tgpj_BldLimitAmountVer_AFDtl versionAfDtl = tgpj_bldLimitAmountVer_afDtlDao.findById(limitedId);

            dtl.setExpectFigureProgress(versionAfDtl);
            dtl.setExpectLimitedRatio(versionAfDtl.getLimitedAmount());

            /*
             * 获取变更前的楼幢数据
             */
            Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();

            dtl.setOrgLimitedAmount(
                null == buildingAccount.getOrgLimitedAmount() ? 0.00 : buildingAccount.getOrgLimitedAmount());
            dtl.setCurrentFigureProgress(
                null == buildingAccount.getCurrentFigureProgress() ? "" : buildingAccount.getCurrentFigureProgress());
            dtl.setCurrentLimitedRatio(
                null == buildingAccount.getCurrentLimitedRatio() ? 0.00 : buildingAccount.getCurrentLimitedRatio());
            dtl.setNodeLimitedAmount(
                null == buildingAccount.getNodeLimitedAmount() ? 0.00 : buildingAccount.getNodeLimitedAmount());
            dtl.setTotalGuaranteeAmount(
                null == buildingAccount.getTotalGuaranteeAmount() ? 0.00 : buildingAccount.getTotalGuaranteeAmount());
            dtl.setCashLimitedAmount(
                null == buildingAccount.getCashLimitedAmount() ? 0.00 : buildingAccount.getCashLimitedAmount());
            dtl.setEffectiveLimitedAmount(null == buildingAccount.getEffectiveLimitedAmount() ? 0.00
                : buildingAccount.getEffectiveLimitedAmount());

            empj_BldLimitAmount_DtlDao.save(dtl);

            attachementList = bldAmount.getAttachementList();
            if (null != attachementList && attachementList.size() > 0) {
                for (BldLimitAmountAttachementForm bldLimitAmountAttachementForm : attachementList) {
                    attachment = new Sm_Attachment();
                    // 查询附件配置表
                    form = new Sm_AttachmentCfgForm();
                    form.seteCode(bldLimitAmountAttachementForm.getSourceType());
                    sm_AttachmentCfg = smAttachmentCfgDao
                        .findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

                    attachment.setTheLink(bldLimitAmountAttachementForm.getTheLink());
                    attachment.setTheSize(bldLimitAmountAttachementForm.getTheSize());
                    attachment.setFileType(bldLimitAmountAttachementForm.getFileType());
                    attachment.setSourceType(bldLimitAmountAttachementForm.getSourceType());
                    attachment.setAttachmentCfg(sm_AttachmentCfg);
                    attachment.setSourceId(dtl.getTableId().toString());
                    attachment.setBusiType("03030100");// 业务类型
                    attachment.setTheState(S_TheState.Normal);
                    attachment.setRemark(bldLimitAmountAttachementForm.getRemark());
                    attachment.setUserStart(user);// 创建人
                    attachment.setUserUpdate(user);// 操作人
                    attachment.setCreateTimeStamp(nowTimeStamp);
                    attachment.setLastUpdateTimeStamp(nowTimeStamp);
                    sm_AttachmentDao.save(attachment);
                }
            }
        }

        properties.put("tableId", bldLimitAmount.getTableId());
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;
    }

    /**
     * 删除
     *
     * @param model
     * @return
     */
    public Properties deleteExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();
        Sm_User user = model.getUser();
        long nowTimeStamp = System.currentTimeMillis();

        Long[] idArr = model.getIdArr();
        if (null == idArr || idArr.length < 1) {
            return MyBackInfo.fail(properties, "请选择需要删除的信息！");
        }

        Empj_BldLimitAmount limitAmount;
        Empj_BldLimitAmount_Dtl dtl;
        for (Long tableId : idArr) {

            /*
             * 查询主表信息
             */
            limitAmount = empj_BldLimitAmountDao.findById(tableId);
            limitAmount.setTheState(S_TheState.Deleted);
            limitAmount.setLastUpdateTimeStamp(nowTimeStamp);
            limitAmount.setUserUpdate(user);
            empj_BldLimitAmountDao.update(limitAmount);

            /*
             * 查询子表信息
             */

        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 列表查询
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties listExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        model.setTheState(S_TheState.Normal);

        String keyword = model.getKeyword();
        if (StringUtils.isNoneBlank(keyword)) {
            model.setKeyword("%" + keyword + "%");
        } else {
            model.setKeyword(null);
        }

        Integer totalCount = empj_BldLimitAmountDao
            .findByPage_Size(empj_BldLimitAmountDao.getQuery_Size(empj_BldLimitAmountDao.getBasicHQL(), model));
        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Empj_BldLimitAmount> empj_BldLimitAmountList;
        if (totalCount > 0) {
            empj_BldLimitAmountList = empj_BldLimitAmountDao.findByPage(
                empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicHQL(), model), pageNumber, countPerPage);
        } else {
            empj_BldLimitAmountList = new ArrayList<Empj_BldLimitAmount>();
        }

        properties.put("empj_BldLimitAmountList", empj_BldLimitAmountList);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 详情
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties detailExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        String userType = "all";

        Sm_User user = model.getUser();

        Long tableId = model.getTableId();

        /*
         * 查询主表信息
         */
        Empj_BldLimitAmount limitAmount = empj_BldLimitAmountDao.findById(tableId);
        if (null == limitAmount) {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }

        Long companyOneId = null;
        Long companyTwoId = null;
        if (null != limitAmount.getCompanyOne() && null != limitAmount.getCompanyTwo()) {
            companyOneId = limitAmount.getCompanyOne().getTableId();
            companyTwoId = limitAmount.getCompanyTwo().getTableId();
        }

        /*
         * 根据当前登录人去判断是否是监理公司人员信息
         * 只有当前登录人员为进度见证人员和监理公司人员做校验
         */
        if (null != companyOneId && null != companyTwoId && null != user && null != user.getCompany()
            && (S_CompanyType.Witness.equals(user.getCompany().getTheType())
                || S_CompanyType.Supervisor.equals(user.getCompany().getTheType()))) {

            if (companyOneId.equals(user.getCompany().getTableId())) {
                userType = "A";
            }
            if (companyTwoId.equals(user.getCompany().getTableId())) {
                userType = "B";
            }

        }

        /*
         * 查询子表信息
         */
        Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
        dtlForm.setTheState(S_TheState.Normal);
        dtlForm.seteCodeOfMainTable(limitAmount.geteCode());
        dtlForm.setMainTable(limitAmount);
        List<Empj_BldLimitAmount_Dtl> dtlList;
        dtlList = empj_BldLimitAmount_DtlDao
            .findByPage(empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));
        if (null != dtlList && dtlList.size() > 0) {
            /*
             * 查询附件信息
             */

            Sm_AttachmentCfgDataInfo dataInfo;

            Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
            form.setBusiType("03030101");
            form.setTheState(S_TheState.Normal);
            List<Sm_AttachmentCfg> smAttachmentCfgList_Copy =
                smAttachmentCfgDao.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

            /*
             * for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) {
             * dataInfo = new Sm_AttachmentCfgDataInfo();
             * dataInfo.setExtra(sm_AttachmentCfg.geteCode());
             * dataInfo.setAppid(oss.getAppid());
             * dataInfo.setAppsecret(oss.getAppsecret());
             * dataInfo.setRemote(oss.getRemote());
             * dataInfo.setProject(oss.getProject());
             *
             * //http://192.168.1.8:19000/oss/material/bananaUpload/
             * uploadMaterial String remote = oss.getRemote(); String subremote
             * = remote.substring(0,
             * remote.lastIndexOf("/")+1)+oss.getProject()+"/uploadMaterial";
             *
             * dataInfo.setUpLoadUrl(subremote);
             *
             * sm_AttachmentCfg.setData(dataInfo); }
             */

            Sm_AttachmentForm sm_AttachmentForm;
            List<Sm_Attachment> sm_AttachmentList;
            // 查询同一附件类型下的所有附件信息（附件信息归类）
            List<Sm_Attachment> smList = null;

            List<Sm_AttachmentCfg_Copy> smAttachmentCfgList;
            Sm_AttachmentCfg_Copy copy;
            for (Empj_BldLimitAmount_Dtl dtl : dtlList) {
                smAttachmentCfgList = new ArrayList<>();
                for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList_Copy) {
                    copy = new Sm_AttachmentCfg_Copy();
                    BeanUtils.copyProperties(sm_AttachmentCfg, copy);

                    /*
                     * 根据当前登录人去判断是否是监理公司人员信息
                     * 只有当前登录人员为进度见证人员和监理公司人员做校验
                     */
                    if ("all".equals(userType)) {
                        smAttachmentCfgList.add(copy);
                    }

                    if ("A".equals(userType)) {
                        if (!sm_AttachmentCfg.getTheName().contains("B")) {
                            smAttachmentCfgList.add(copy);
                        }
                    }

                    if ("B".equals(userType)) {
                        if (!sm_AttachmentCfg.getTheName().contains("A")) {
                            smAttachmentCfgList.add(copy);
                        }
                    }

                }

                sm_AttachmentForm = new Sm_AttachmentForm();
                sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
                // sm_AttachmentForm.setRemark(dtl.getTableId().toString());
                sm_AttachmentForm.setBusiType("03030100");
                sm_AttachmentForm.setTheState(S_TheState.Normal);

                // 加载所有楼幢下的相关附件信息推送钉钉失败，请联系管理人员
                sm_AttachmentList = sm_AttachmentDao
                    .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
                if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
                    sm_AttachmentList = new ArrayList<Sm_Attachment>();
                }

                for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
                    String sourceType = sm_Attachment.getSourceType();
                    for (Sm_AttachmentCfg_Copy sm_AttachmentCfg : smAttachmentCfgList) {
                        if (sm_AttachmentCfg.geteCode().equals(sourceType)) {
                            smList = sm_AttachmentCfg.getSmAttachmentList();
                            if (null == smList || smList.size() == 0) {
                                smList = new ArrayList<Sm_Attachment>();
                            }
                            smList.add(sm_Attachment);
                            sm_AttachmentCfg.setSmAttachmentList(smList);
                        }
                    }
                }

                dtl.setSmAttachmentCfgList(smAttachmentCfgList);

            }
        }

        limitAmount.setDtlList(dtlList);

        properties.put("empj_BldLimitAmount", limitAmount);
        properties.put("userType", userType);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 审批详情
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties approvalDetailExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        String userType = "all";

        Sm_User user = model.getUser();

        Long tableId = model.getTableId();

        /*
         * 查询主表信息
         */
        Empj_BldLimitAmount limitAmount = empj_BldLimitAmountDao.findById(tableId);
        if (null == limitAmount) {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }

        Long companyOneId = null;
        Long companyTwoId = null;
        if (null != limitAmount.getCompanyOne() && null != limitAmount.getCompanyTwo()) {
            companyOneId = limitAmount.getCompanyOne().getTableId();
            companyTwoId = limitAmount.getCompanyTwo().getTableId();
        }

        /*
         * 根据当前登录人去判断是否是监理公司人员信息
         * 只有当前登录人员为进度见证人员和监理公司人员做校验
         */
        if (null != companyOneId && null != companyTwoId && null != user && null != user.getCompany()
            && (S_CompanyType.Witness.equals(user.getCompany().getTheType())
                || S_CompanyType.Supervisor.equals(user.getCompany().getTheType()))) {

            if (companyOneId.equals(user.getCompany().getTableId())) {
                userType = "A";
            }
            if (companyTwoId.equals(user.getCompany().getTableId())) {
                userType = "B";
            }

        }

        if (S_CompanyType.Development.equals(user.getCompany().getTheType())){
            //开发企业用户
            userType = "Z";
        }

        /*
         * 查询子表信息
         */
        Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
        dtlForm.setTheState(S_TheState.Normal);
        dtlForm.seteCodeOfMainTable(limitAmount.geteCode());
        dtlForm.setMainTable(limitAmount);
        List<Empj_BldLimitAmount_Dtl> dtlList;
        dtlList = empj_BldLimitAmount_DtlDao
            .findByPage(empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));
        if (null != dtlList && dtlList.size() > 0) {
            /*
             * 查询附件信息
             */

            Sm_AttachmentCfgDataInfo dataInfo;

            Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
            form.setBusiType("03030101");
            form.setTheState(S_TheState.Normal);
            List<Sm_AttachmentCfg> smAttachmentCfgList_Copy =
                smAttachmentCfgDao.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

            /*
             * for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) {
             * dataInfo = new Sm_AttachmentCfgDataInfo();
             * dataInfo.setExtra(sm_AttachmentCfg.geteCode());
             * dataInfo.setAppid(oss.getAppid());
             * dataInfo.setAppsecret(oss.getAppsecret());
             * dataInfo.setRemote(oss.getRemote());
             * dataInfo.setProject(oss.getProject());
             *
             * //http://192.168.1.8:19000/oss/material/bananaUpload/
             * uploadMaterial String remote = oss.getRemote(); String subremote
             * = remote.substring(0,
             * remote.lastIndexOf("/")+1)+oss.getProject()+"/uploadMaterial";
             *
             * dataInfo.setUpLoadUrl(subremote);
             *
             * sm_AttachmentCfg.setData(dataInfo); }
             */

            Sm_AttachmentForm sm_AttachmentForm;
            List<Sm_Attachment> sm_AttachmentList;
            // 查询同一附件类型下的所有附件信息（附件信息归类）
            List<Sm_Attachment> smList = null;

            List<Sm_AttachmentCfg_Copy> smAttachmentCfgList;
            Sm_AttachmentCfg_Copy copy;
            for (Empj_BldLimitAmount_Dtl dtl : dtlList) {
                smAttachmentCfgList = new ArrayList<>();
                for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList_Copy) {
                    copy = new Sm_AttachmentCfg_Copy();
                    BeanUtils.copyProperties(sm_AttachmentCfg, copy);

                    /*
                     * 根据当前登录人去判断是否是监理公司人员信息
                     * 只有当前登录人员为进度见证人员和监理公司人员做校验
                     */
                    if ("all".equals(userType) || "Z".equals(userType)) {
                        smAttachmentCfgList.add(copy);
                    }

                    if ("A".equals(userType)) {
                        if (!sm_AttachmentCfg.getTheName().contains("B")) {
                            smAttachmentCfgList.add(copy);
                        }
                    }

                    if ("B".equals(userType)) {
                        if (!sm_AttachmentCfg.getTheName().contains("A")) {
                            smAttachmentCfgList.add(copy);
                        }
                    }

                }

                sm_AttachmentForm = new Sm_AttachmentForm();
                sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
                // sm_AttachmentForm.setRemark(dtl.getTableId().toString());
                sm_AttachmentForm.setBusiType("03030100");
                sm_AttachmentForm.setTheState(S_TheState.Normal);

                // 加载所有楼幢下的相关附件信息
                sm_AttachmentList = sm_AttachmentDao
                    .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
                if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
                    sm_AttachmentList = new ArrayList<Sm_Attachment>();
                }

                for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
                    String sourceType = sm_Attachment.getSourceType();
                    for (Sm_AttachmentCfg_Copy sm_AttachmentCfg : smAttachmentCfgList) {
                        if (sm_AttachmentCfg.geteCode().equals(sourceType)) {
                            smList = sm_AttachmentCfg.getSmAttachmentList();
                            if (null == smList || smList.size() == 0) {
                                smList = new ArrayList<Sm_Attachment>();
                            }
                            smList.add(sm_Attachment);
                            sm_AttachmentCfg.setSmAttachmentList(smList);
                        }
                    }
                }

                dtl.setSmAttachmentCfgList(smAttachmentCfgList);

            }
        }

        limitAmount.setDtlList(dtlList);

        properties.put("empj_BldLimitAmount", limitAmount);
        properties.put("userType", userType);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 修改
     *
     * @param model
     * @return
     */
    public Properties editExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        Sm_User user = model.getUser();
        long nowTimeStamp = System.currentTimeMillis();

        Long tableId = model.getTableId();
        /*String busiCode = model.getBusiCode();
        Empj_BldLimitAmount_Dtl dtl = empj_BldLimitAmount_DtlDao.findById(tableId);*/

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(tableId.toString());;
        sm_AttachmentForm.setBusiType("03030100");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 先删除
        List<Sm_Attachment> sm_AttachmentList =
            sm_AttachmentDao.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
        for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
            sm_Attachment.setTheState(S_TheState.Deleted);
            sm_Attachment.setUserUpdate(user);
            sm_Attachment.setLastUpdateTimeStamp(nowTimeStamp);
            sm_AttachmentDao.update(sm_Attachment);
        }

        // 再保存 attachementList
        Sm_Attachment attachment;
        Sm_AttachmentCfgForm form;
        Sm_AttachmentCfg sm_AttachmentCfg;
        List<Map<String, Object>> attachmentList = model.getAttachmentList();
        for (Map<String, Object> map : attachmentList) {

            attachment = new Sm_Attachment();
            // 查询附件配置表
            form = new Sm_AttachmentCfgForm();
            form.seteCode((String)map.get("sourceType"));
            sm_AttachmentCfg = smAttachmentCfgDao
                .findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

            attachment.setTheLink((String)(null == map.get("theLink") ? "" : map.get("theLink")));
            attachment.setTheSize((String)(null == map.get("theSize") ? "" : map.get("theSize")));
            attachment.setFileType((String)(null == map.get("fileType") ? "" : map.get("fileType")));
            attachment.setSourceType((String)(null == map.get("sourceType") ? "" : map.get("sourceType")));
            attachment.setAttachmentCfg(sm_AttachmentCfg);
            attachment.setSourceId(tableId.toString());
            attachment.setBusiType("03030100");// 业务类型
            attachment.setTheState(S_TheState.Normal);
            attachment.setRemark((String)(null == map.get("remark") ? "" : map.get("remark")));
            attachment.setUserStart(user);// 创建人
            attachment.setUserUpdate(user);// 操作人
            attachment.setCreateTimeStamp(nowTimeStamp);
            attachment.setLastUpdateTimeStamp(nowTimeStamp);
            sm_AttachmentDao.save(attachment);

        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }


    /**
     * 20230512 正泰要求设置10选一家单监理
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties submitExecute(Empj_BldLimitAmountForm model) {

        /*
         * 更新提交申请日期 更新楼幢相关性信息-提交时不更新
         */
        Properties properties = new MyProperties();

        Sm_User user = model.getUser();
        long nowTimeStamp = System.currentTimeMillis();

        model.setButtonType("2");
        String busiCode = BUSI_CODE;

        Long tableId = model.getTableId();
        if (tableId == null || tableId < 1) {
            return MyBackInfo.fail(properties, "请选择有效的单据信息！");
        }

        Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao.findById(tableId);
        if (null == bldLimitAmount) {
            return MyBackInfo.fail(properties, "选择的单据信息已失效，请刷新后重试！");
        }

        // 判断单据审批状态
        if (S_ApprovalState.Examining.equals(bldLimitAmount.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
        }
        if (S_ApprovalState.Completed.equals(bldLimitAmount.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
        }

        properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());

        if ("noApproval".equals(properties.getProperty("info"))) {
            return MyBackInfo.fail(properties, "未配置对应的审批流程！");
        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            return properties;
        } else {


            Emmp_CompanyInfoForm companyInfoModel = new Emmp_CompanyInfoForm();
            companyInfoModel.setTheState(S_TheState.Normal);
            companyInfoModel.setTheType(S_CompanyType.Witness);
            companyInfoModel.setApprovalState("已完结");
            companyInfoModel.setBusiState("已备案");
            companyInfoModel.setIsUsedState("1");

            List<Emmp_CompanyInfo> companyList1 = emmp_CompanyInfoDao
                    .findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBldLimitAmountCompanyListHQL(), companyInfoModel));

            List<Emmp_CompanyInfo> givenList = new ArrayList<Emmp_CompanyInfo>();
            givenList.addAll(companyList1);

            List<Integer> integers = solution(companyList1);

            if(integers == null && integers.size() <= 0){
                return MyBackInfo.fail(properties, "监理公司返回异常！");
            }
            Emmp_CompanyInfo witnessCompany = givenList.get(integers.get(0));
            if (null == witnessCompany) {
                return MyBackInfo.fail(properties, "请先维护有效的监理机构！");
            }

    /*
             * xsz by time 2019-4-11 16:17:07
             * 正式提交前校验签章
             */
            String isSign = model.getIsSign();
            if (null == isSign) {
                isSign = "0";
            }

            if (!"1".equals(isSign)) {
                /*
                 * xsz by time 2019-1-19 10:44:26
                 * 审批操作结束后，生成发对应的pdf并检查是否有签章权限
                 */
                /*
                 * 并将生成PDF后上传值OSS路径返回给前端
                 *
                 * 参数：
                 * sourceBusiCode：业务编码
                 * sourceId：单据ID
                 */
                // 登录人是否具有签章
                if (null != user.getIsSignature() && "1".equals(user.getIsSignature())) {

                    ExportPdfForm pdfModel = new ExportPdfForm();
                    pdfModel.setSourceBusiCode(BUSI_CODE);
                    pdfModel.setSourceId(String.valueOf(tableId));
                    Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                    String pdfUrl = (String)executeProperties.get("pdfUrl");

                    Map<String, String> signatureMap = new HashMap<>();

                    signatureMap.put("signaturePath", pdfUrl);
                    // TODO 此配置后期做成配置
                    signatureMap.put("signatureKeyword", "开发企业：");
                    signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                    properties = new MyProperties();
                    properties.put("signatureMap", signatureMap);
                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

                    return properties;

                }
            }

            // 判断当前登录用户是否有权限发起审批
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
                    (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");
            // 审批操作
            properties = sm_approvalProcessService.execute(bldLimitAmount, model, sm_approvalProcess_cfg);

            bldLimitAmount.setLastUpdateTimeStamp(nowTimeStamp);
            bldLimitAmount.setUserUpdate(user);
            bldLimitAmount.setApplyDate(nowTimeStamp);
            bldLimitAmount.setApprovalState(S_ApprovalState.Examining);

            bldLimitAmount.setCompanyOne(witnessCompany);
            bldLimitAmount.setCompanyOneName(witnessCompany.getTheName());

            /*
             * 查询子表信息
             */

            Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
            dtlForm.setTheState(S_TheState.Normal);
            dtlForm.seteCodeOfMainTable(bldLimitAmount.geteCode());
            dtlForm.setMainTable(bldLimitAmount);
            List<Empj_BldLimitAmount_Dtl> dtlList;
            dtlList = empj_BldLimitAmount_DtlDao
                    .findByPage(empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));
            for (Empj_BldLimitAmount_Dtl empj_BldLimitAmount_Dtl : dtlList) {
                empj_BldLimitAmount_Dtl.setCompanyOne(witnessCompany);
                empj_BldLimitAmount_Dtl.setCompanyOneName(witnessCompany.getTheName());

                empj_BldLimitAmount_DtlDao.update(empj_BldLimitAmount_Dtl);

            }

        }

        empj_BldLimitAmountDao.update(bldLimitAmount);

        properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }



//    /**
//     * 提交2.0 提交时生成双监理公司随机取数
//     *
//     * @param model
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public Properties submitExecute(Empj_BldLimitAmountForm model) {
//
//        /*
//         * 更新提交申请日期 更新楼幢相关性信息-提交时不更新
//         */
//        Properties properties = new MyProperties();
//
//        Sm_User user = model.getUser();
//        long nowTimeStamp = System.currentTimeMillis();
//
//        model.setButtonType("2");
//        String busiCode = BUSI_CODE;
//
//        Long tableId = model.getTableId();
//        if (tableId == null || tableId < 1) {
//            return MyBackInfo.fail(properties, "请选择有效的单据信息！");
//        }
//
//        Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao.findById(tableId);
//        if (null == bldLimitAmount) {
//            return MyBackInfo.fail(properties, "选择的单据信息已失效，请刷新后重试！");
//        }
//
//        // 判断单据审批状态
//        if (S_ApprovalState.Examining.equals(bldLimitAmount.getApprovalState())) {
//            return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
//        }
//        if (S_ApprovalState.Completed.equals(bldLimitAmount.getApprovalState())) {
//            return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
//        }
//
//        properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
//
//        if ("noApproval".equals(properties.getProperty("info"))) {
//            return MyBackInfo.fail(properties, "未配置对应的审批流程！");
//        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
//            return properties;
//        } else {
//
//            /*
//             * 绑定监理机构A和监理机构B
//             * 监理机构A:
//             * 随机从进度见证单位中获取
//             * Witness 12
//             * 监理机构B:
//             * 随机从监理机构中获取
//             * Supervisor 31
//             */
////            Emmp_CompanyInfo witnessCompany = randomCompany(S_CompanyType.Witness);
////            if (null == witnessCompany) {
////                return MyBackInfo.fail(properties, "请先维护有效的进度见证单位！");
////            }
////
////            Emmp_CompanyInfo supervisorCompany = randomCompany(S_CompanyType.Supervisor);
////            if (null == supervisorCompany) {
////                return MyBackInfo.fail(properties, "请先维护有效的监理机构！");
////            }
//
//
//            Emmp_CompanyInfoForm companyInfoModel = new Emmp_CompanyInfoForm();
//            companyInfoModel.setTheState(S_TheState.Normal);
//            companyInfoModel.setTheType(S_CompanyType.Witness);
//            companyInfoModel.setApprovalState("已完结");
//            companyInfoModel.setBusiState("已备案");
//            companyInfoModel.setIsUsedState("1");
//
//            List<Emmp_CompanyInfo> companyList1 = emmp_CompanyInfoDao
//                    .findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), companyInfoModel));
//
//            companyInfoModel.setTheType(S_CompanyType.Supervisor);
////            List<Emmp_CompanyInfo> companyList2 = emmp_CompanyInfoDao
////                    .findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), companyInfoModel));
//
//            List<Emmp_CompanyInfo> givenList = new ArrayList<Emmp_CompanyInfo>();
//            givenList.addAll(companyList1);
////            givenList.addAll(companyList2);
//
//            List<Integer> integers = solution(givenList);
//
//            if(integers == null && integers.size() <= 0){
//                return MyBackInfo.fail(properties, "监理公司返回异常！");
//            }
//            Emmp_CompanyInfo witnessCompany = givenList.get(integers.get(0));
//            if (null == witnessCompany) {
//                return MyBackInfo.fail(properties, "请先维护有效的监理机构！");
//            }
//
////            Emmp_CompanyInfo supervisorCompany = givenList.get(integers.get(1));
////            if (null == supervisorCompany) {
////                return MyBackInfo.fail(properties, "请先维护有效的监理机构！");
////            }
//
//            /*
//             * xsz by time 2019-4-11 16:17:07
//             * 正式提交前校验签章
//             */
//            String isSign = model.getIsSign();
//            if (null == isSign) {
//                isSign = "0";
//            }
//
//            if (!"1".equals(isSign)) {
//                /*
//                 * xsz by time 2019-1-19 10:44:26
//                 * 审批操作结束后，生成发对应的pdf并检查是否有签章权限
//                 */
//                /*
//                 * 并将生成PDF后上传值OSS路径返回给前端
//                 *
//                 * 参数：
//                 * sourceBusiCode：业务编码
//                 * sourceId：单据ID
//                 */
//                // 登录人是否具有签章
//                if (null != user.getIsSignature() && "1".equals(user.getIsSignature())) {
//
//                    ExportPdfForm pdfModel = new ExportPdfForm();
//                    pdfModel.setSourceBusiCode(BUSI_CODE);
//                    pdfModel.setSourceId(String.valueOf(tableId));
//                    Properties executeProperties = exportPdfByWordService.execute(pdfModel);
//                    String pdfUrl = (String)executeProperties.get("pdfUrl");
//
//                    Map<String, String> signatureMap = new HashMap<>();
//
//                    signatureMap.put("signaturePath", pdfUrl);
//                    // TODO 此配置后期做成配置
//                    signatureMap.put("signatureKeyword", "开发企业：");
//                    signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
//
//                    properties = new MyProperties();
//                    properties.put("signatureMap", signatureMap);
//                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
//                    properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//
//                    return properties;
//
//                }
//            }
//
//            // 判断当前登录用户是否有权限发起审批
//            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
//                (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");
//            // 审批操作
//            properties = sm_approvalProcessService.execute(bldLimitAmount, model, sm_approvalProcess_cfg);
//
//            bldLimitAmount.setLastUpdateTimeStamp(nowTimeStamp);
//            bldLimitAmount.setUserUpdate(user);
//            bldLimitAmount.setApplyDate(nowTimeStamp);
//            bldLimitAmount.setApprovalState(S_ApprovalState.Examining);
//
//            bldLimitAmount.setCompanyOne(witnessCompany);
//            bldLimitAmount.setCompanyOneName(witnessCompany.getTheName());
////            bldLimitAmount.setCompanyTwo(supervisorCompany);
////            bldLimitAmount.setCompanyTwoName(supervisorCompany.getTheName());
//
//            /*
//             * 查询子表信息
//             */
//
//            Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
//            dtlForm.setTheState(S_TheState.Normal);
//            dtlForm.seteCodeOfMainTable(bldLimitAmount.geteCode());
//            dtlForm.setMainTable(bldLimitAmount);
//            List<Empj_BldLimitAmount_Dtl> dtlList;
//            dtlList = empj_BldLimitAmount_DtlDao
//                .findByPage(empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));
//            for (Empj_BldLimitAmount_Dtl empj_BldLimitAmount_Dtl : dtlList) {
//                empj_BldLimitAmount_Dtl.setCompanyOne(witnessCompany);
//                empj_BldLimitAmount_Dtl.setCompanyOneName(witnessCompany.getTheName());
////                empj_BldLimitAmount_Dtl.setCompanyTwo(supervisorCompany);
////                empj_BldLimitAmount_Dtl.setCompanyTwoName(supervisorCompany.getTheName());
//
//                empj_BldLimitAmount_DtlDao.update(empj_BldLimitAmount_Dtl);
//
//            }
//
//        }
//
//        empj_BldLimitAmountDao.update(bldLimitAmount);
//
//        properties = new MyProperties();
//        properties.put(S_NormalFlag.result, S_NormalFlag.success);
//        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//        return properties;
//
//    }

    /**
     * 提交
     *
     * @param model
     * @return
     */
    public Properties submitExecuteCopy(Empj_BldLimitAmountForm model) {

        /*
         * 更新提交申请日期 更新楼幢相关性信息-提交时不更新
         */
        Properties properties = new MyProperties();

        Sm_User user = model.getUser();
        long nowTimeStamp = System.currentTimeMillis();

        model.setButtonType("2");
        String busiCode = BUSI_CODE;

        Long tableId = model.getTableId();
        if (tableId == null || tableId < 1) {
            return MyBackInfo.fail(properties, "请选择有效的单据信息！");
        }

        Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao.findById(tableId);
        if (null == bldLimitAmount) {
            return MyBackInfo.fail(properties, "选择的单据信息已失效，请刷新后重试！");
        }

        // 判断单据审批状态
        if (S_ApprovalState.Examining.equals(bldLimitAmount.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
        }
        if (S_ApprovalState.Completed.equals(bldLimitAmount.getApprovalState())) {
            return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
        }

        properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());

        if ("noApproval".equals(properties.getProperty("info"))) {
            return MyBackInfo.fail(properties, "未配置对应的审批流程！");
        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            return properties;
        } else {

            /*
             * xsz by time 2019-4-11 16:17:07
             * 正式提交前校验签章
             */
            String isSign = model.getIsSign();
            if (null == isSign) {
                isSign = "0";
            }

            if (!"1".equals(isSign)) {
                /*
                 * xsz by time 2019-1-19 10:44:26
                 * 审批操作结束后，生成发对应的pdf并检查是否有签章权限
                 */
                /*
                 * 并将生成PDF后上传值OSS路径返回给前端
                 *
                 * 参数：
                 * sourceBusiCode：业务编码
                 * sourceId：单据ID
                 */
                // 登录人是否具有签章
                if (null != user.getIsSignature() && "1".equals(user.getIsSignature())) {

                    ExportPdfForm pdfModel = new ExportPdfForm();
                    pdfModel.setSourceBusiCode(BUSI_CODE);
                    pdfModel.setSourceId(String.valueOf(tableId));
                    Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                    String pdfUrl = (String)executeProperties.get("pdfUrl");

                    Map<String, String> signatureMap = new HashMap<>();

                    signatureMap.put("signaturePath", pdfUrl);
                    // TODO 此配置后期做成配置
                    signatureMap.put("signatureKeyword", "开发企业：（盖章）");
                    signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                    properties.put("signatureMap", signatureMap);
                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

                    return properties;

                }
            }

            // 判断当前登录用户是否有权限发起审批
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
                (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");
            // 审批操作
            properties = sm_approvalProcessService.execute(bldLimitAmount, model, sm_approvalProcess_cfg);

            bldLimitAmount.setLastUpdateTimeStamp(nowTimeStamp);
            bldLimitAmount.setUserUpdate(user);
            bldLimitAmount.setApplyDate(nowTimeStamp);
            bldLimitAmount.setApprovalState(S_ApprovalState.Examining);

            /*
             * 查询子表信息
             */
            /*
             * Empj_BldLimitAmount_DtlForm dtlForm = new
             * Empj_BldLimitAmount_DtlForm();
             * dtlForm.setTheState(S_TheState.Normal);
             * dtlForm.seteCodeOfMainTable(bldLimitAmount.geteCode());
             * dtlForm.setMainTable(bldLimitAmount);
             * List<Empj_BldLimitAmount_Dtl> dtlList; dtlList =
             * empj_BldLimitAmount_DtlDao.findByPage(empj_BldLimitAmount_DtlDao.
             * getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm)); for
             * (Empj_BldLimitAmount_Dtl empj_BldLimitAmount_Dtl : dtlList) {
             * Empj_BuildingInfo building =
             * empj_BldLimitAmount_Dtl.getBuilding(); Tgpj_BuildingAccount
             * buildingAccount = building.getBuildingAccount();
             *
             * }
             */

        }

        empj_BldLimitAmountDao.update(bldLimitAmount);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 更新申请表的审批信息
     *
     * @param model
     * @return
     */
    public Properties updateDtlExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        String approvalResult = model.getApprovalResult();
        String approvalInfo = model.getApprovalInfo();

        Long tableId = model.getTableId();
        Empj_BldLimitAmount_Dtl limitAmount_Dtl = empj_BldLimitAmount_DtlDao.findById(tableId);
        if (null == limitAmount_Dtl) {
            /* return MyBackInfo.fail(properties, "申请信息已失效！"); */
        } else {
            if ("0".equals(approvalResult)) {
                if (StringUtils.isBlank(approvalInfo)) {
                    return MyBackInfo.fail(properties,
                        "楼幢：" + limitAmount_Dtl.geteCodeFromConstruction() + " 请维护不通过原因！");
                }
                limitAmount_Dtl.setApprovalInfo(approvalInfo);
                limitAmount_Dtl.setApprovalResult(approvalResult);
            } else {
                limitAmount_Dtl.setApprovalInfo(approvalInfo);
                limitAmount_Dtl.setApprovalResult(null);
            }

            empj_BldLimitAmount_DtlDao.update(limitAmount_Dtl);
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 随机获取列表中的获取两条
     * @return
     */
//    public   List<Integer> solution(List<Emmp_CompanyInfo> givenList){
//
//        List<Integer> givenListInteger = new ArrayList<Integer>();
//        for (int i =0; i < givenList.size() ; i ++) {
//            givenListInteger.add(i);
//        }
//
////        System.out.println("givenListInteger.size()="+givenListInteger.size());
//        Collections.shuffle(givenListInteger);
////        System.out.println("------------------givenListInteger1="+givenListInteger);
//        Collections.shuffle(givenListInteger);
////        System.out.println("------------------givenListInteger2="+givenListInteger);
//
//        int randomSeriesLength = 2;
//
//        List<Integer> randomSeries = givenListInteger.subList(0, randomSeriesLength);
//        System.out.println("randomSeries="+randomSeries);
//        return randomSeries;
//    }

    // 随机从列表中获取一条
    public   List<Integer> solution(List<Emmp_CompanyInfo> givenList){

        List<Integer> givenListInteger = new ArrayList<Integer>();
        for (int i =0; i < givenList.size() ; i ++) {
            givenListInteger.add(i);
        }
        Collections.shuffle(givenListInteger);
        int randomSeriesLength = 1;
        List<Integer> randomSeries = givenListInteger.subList(0, randomSeriesLength);
        return randomSeries;
    }


    /**
     * 随机选择公司
     *
     * @param companyType
     * @return
     */
    @SuppressWarnings("unchecked")
    private Emmp_CompanyInfo randomCompany(String companyType) {
        Emmp_CompanyInfoForm companyInfoModel = new Emmp_CompanyInfoForm();
        companyInfoModel.setTheState(S_TheState.Normal);
        companyInfoModel.setTheType(companyType);
        companyInfoModel.setApprovalState("已完结");
        companyInfoModel.setBusiState("已备案");
        companyInfoModel.setIsUsedState("1");

        List<Emmp_CompanyInfo> companyList = emmp_CompanyInfoDao
            .findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), companyInfoModel));
        if (null != companyList && !companyList.isEmpty()) {
            // 随机数从List取数
            int index = (int)(Math.random() * companyList.size());

            return companyList.get(index);
        }

        return null;
    }

    /**
     * 随机选择公司
     *
     * @param
     * @return
     */
    @SuppressWarnings({"unchecked", "unused"})
    private Emmp_CompanyInfo randomCompany(int index) {
        Emmp_CompanyInfoForm companyInfoModel = new Emmp_CompanyInfoForm();
        companyInfoModel.setTheState(S_TheState.Normal);
        companyInfoModel.setTheType(S_CompanyType.Supervisor);
        companyInfoModel.setApprovalState("已完结");
        companyInfoModel.setBusiState("已备案");
        companyInfoModel.setIsUsedState("1");

        List<Emmp_CompanyInfo> companyList = emmp_CompanyInfoDao
            .findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), companyInfoModel));
        if (null != companyList && !companyList.isEmpty()) {
            // 随机数从List取数
            return companyList.get(index);
        }

        return null;
    }

    /**
     * 监理报告查询加载监理公司
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties queryCompanyListExecute(Empj_BldLimitAmountForm model){

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        List<Emmp_CompanyInfo> companyInfoList = emmp_CompanyInfoDao.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBldLimitAmountCompanyListHQL(), model));
        /*if(!companyInfoList.isEmpty()){
            for (Emmp_CompanyInfo emmp_CompanyInfo : companyInfoList) {



            }
        }*/


        properties.put("emmp_CompanyInfoList", companyInfoList);

        return properties;


    }

    /**
     * 报表查询
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties queryReportListExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        model.setTheState(S_TheState.Normal);

        Long companyId = model.getCompanyId();
        if(null == companyId){
            model.setCompanyId(null);
            model.setCompanyName(null);
            //return MyBackInfo.fail(properties, "请选择监理公司查询！");
        }else{
            Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(companyId);
            if(null == companyInfo){
                return MyBackInfo.fail(properties, "选择的监理公司已失效，请重新选择！");
            }

            model.setCompanyName("%" + companyInfo.getTheName() + "%");
            model.setCompanyId(companyId);
        }

        if(StringUtils.isNotBlank(model.getCompleteStart())){
            model.setCompleteStartLong(MyDatetime.getInstance().stringDayToLong(model.getCompleteStart().trim()));
        }

        if(StringUtils.isNotBlank(model.getCompleteEnd())){
            model.setCompleteEndLong(MyDatetime.getInstance().theDayAfterSpecifiedDayTimeStamp(MyDatetime.getInstance().stringDayToLong(model.getCompleteEnd().trim())));
        }

        String keyword = model.getKeyword();
        if (StringUtils.isNoneBlank(keyword)) {
            model.setKeyword("%" + keyword + "%");
        } else {
            model.setKeyword(null);
        }

        Integer totalCount = empj_BldLimitAmount_ViewDao
            .findByPage_Size(empj_BldLimitAmount_ViewDao.getQuery_Size(empj_BldLimitAmount_ViewDao.getBasicreportHQL(), model));
        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Qs_BldLimitAmount_View> empj_BldLimitAmountList;

        List<ReportVO> reportList = new ArrayList<ReportVO>();
        ReportVO vo;
        if (totalCount > 0) {

            empj_BldLimitAmountList = empj_BldLimitAmount_ViewDao.findByPage(
                empj_BldLimitAmount_ViewDao.getQuery(empj_BldLimitAmount_ViewDao.getBasicreportHQL(), model), pageNumber,
                countPerPage);

            for (Qs_BldLimitAmount_View po : empj_BldLimitAmountList) {

                vo = new ReportVO();

                vo.setCompanyName(po.getCompanyName());
                vo.setBuildCount(null == po.getBuildCount() ? 0 : po.getBuildCount());
                vo.setProjectName(po.getProject().getTheName());
                vo.setAreaName(po.getProject().getCityRegion().getTheName());
                vo.setBusiCode(po.geteCode());
                vo.setBusiId(po.getId());
                vo.setCompleteDate(null == po.getAppointTimeStamp() ? ""
                    : MyDatetime.getInstance().dateToSimpleString(po.getAppointTimeStamp()));
                reportList.add(vo);

            }
        }

        properties.put("reportList", reportList);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 报表导出
     *
     * @param model
     * @return
     */

    private static final String excelName = "监理报告查询";

    @SuppressWarnings("unchecked")
    public Properties reportExportListExecute(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        model.setTheState(S_TheState.Normal);

        Long companyId = model.getCompanyId();
        if(null == companyId){
            model.setCompanyId(null);
            model.setCompanyName(null);
            //return MyBackInfo.fail(properties, "请选择监理公司查询！");
        }else{
            Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(companyId);
            if(null == companyInfo){
                return MyBackInfo.fail(properties, "选择的监理公司已失效，请重新选择！");
            }

            model.setCompanyName("%" + companyInfo.getTheName() + "%");
            model.setCompanyId(companyId);
        }

        if(StringUtils.isNotBlank(model.getCompleteStart())){
            model.setCompleteStartLong(MyDatetime.getInstance().stringDayToLong(model.getCompleteStart().trim()));
        }

        if(StringUtils.isNotBlank(model.getCompleteEnd())){
            model.setCompleteEndLong(MyDatetime.getInstance().theDayAfterSpecifiedDayTimeStamp(MyDatetime.getInstance().stringDayToLong(model.getCompleteEnd().trim())));
        }

        String keyword = model.getKeyword();
        if (StringUtils.isNoneBlank(keyword)) {
            model.setKeyword("%" + keyword + "%");
        } else {
            model.setKeyword(null);
        }

        Integer totalCount = empj_BldLimitAmount_ViewDao
            .findByPage_Size(empj_BldLimitAmount_ViewDao.getQuery_Size(empj_BldLimitAmount_ViewDao.getBasicreportHQL(), model));
        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Qs_BldLimitAmount_View> empj_BldLimitAmountList;

        List<ReportVO> reportList = new ArrayList<ReportVO>();
        ReportVO vo;
        if (totalCount > 0) {

            empj_BldLimitAmountList = empj_BldLimitAmount_ViewDao.findByPage(
                empj_BldLimitAmount_ViewDao.getQuery(empj_BldLimitAmount_ViewDao.getBasicreportHQL(), model), pageNumber,
                countPerPage);

            // 初始化文件保存路径，创建相应文件夹
            DirectoryUtil directoryUtil = new DirectoryUtil();
            String relativeDir = directoryUtil.createRelativePathWithDate("Empj_PaymentGuarantee");
            String localPath = directoryUtil.getProjectRoot();

            // 文件在服务器文件系统中的完整路径
            String saveDirectory = localPath + relativeDir;

            if (saveDirectory.contains("%20")) {
                saveDirectory = saveDirectory.replace("%20", " ");
            }

            directoryUtil.mkdir(saveDirectory);

            String strNewFileName =
                MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

            String saveFilePath = saveDirectory + strNewFileName;

            ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
            // 自定义字段别名
            writer.addHeaderAlias("ordinal", "序号");
            writer.addHeaderAlias("companyName", "监理公司");
            writer.addHeaderAlias("buildCount", "楼幢数");
            writer.addHeaderAlias("projectName", "项目");
            writer.addHeaderAlias("areaName", "区域");
            writer.addHeaderAlias("busiCode", "业务单号");
            writer.addHeaderAlias("completeDate", "完成时间");

            List<NoteChangeReportExcelVO> list = formart(empj_BldLimitAmountList);

            // 一次性写出内容，使用默认样式
            writer.write(list);

            // 关闭writer，释放内存
            writer.flush();
            writer.close();

            properties.put("fileDownloadPath", relativeDir + strNewFileName);

        }

        properties.put("reportList", reportList);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }

    /**
     * 报表导出
     *
     * @param model
     * @return
     */

    @SuppressWarnings("unchecked")
    public Properties reportExportListExecuteOld(Empj_BldLimitAmountForm model) {

        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        model.setTheState(S_TheState.Normal);

        Long companyId = model.getCompanyId();
        if(null == companyId){
            return MyBackInfo.fail(properties, "请选择监理公司查询！");
        }

        Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(companyId);
        if(null == companyInfo){
            return MyBackInfo.fail(properties, "选择的监理公司已失效，请重新选择！");
        }

        model.setCompanyName("%" + companyInfo.getTheName() + "%");
        model.setCompanyId(companyId);

        if(StringUtils.isNotBlank(model.getCompleteStart())){
            model.setCompleteStartLong(MyDatetime.getInstance().stringDayToLong(model.getCompleteStart().trim()));
        }

        if(StringUtils.isNotBlank(model.getCompleteEnd())){
            model.setCompleteEndLong(MyDatetime.getInstance().theDayAfterSpecifiedDayTimeStamp(MyDatetime.getInstance().stringDayToLong(model.getCompleteEnd().trim())));
        }

        /*String companyName = model.getCompanyName();
        if (StringUtils.isNotBlank(companyName)) {
            model.setCompanyName("%" + companyName + "%");
        } else {
            return MyBackInfo.fail(properties, "请输入监理公司查询！");
        }*/

        String keyword = model.getKeyword();
        if (StringUtils.isNoneBlank(keyword)) {
            model.setKeyword("%" + keyword + "%");
        } else {
            model.setKeyword(null);
        }

        Integer totalCount = empj_BldLimitAmountDao
            .findByPage_Size(empj_BldLimitAmountDao.getQuery_Size(empj_BldLimitAmountDao.getBasicreportHQL(), model));
        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Qs_BldLimitAmount_View> empj_BldLimitAmountList;

        List<ReportVO> reportList = new ArrayList<ReportVO>();
        ReportVO vo;
        if (totalCount > 0) {

            empj_BldLimitAmountList = empj_BldLimitAmountDao.findByPage(
                empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicreportHQL(), model));

            // 初始化文件保存路径，创建相应文件夹
            DirectoryUtil directoryUtil = new DirectoryUtil();
            String relativeDir = directoryUtil.createRelativePathWithDate("Empj_PaymentGuarantee");
            String localPath = directoryUtil.getProjectRoot();

            // 文件在服务器文件系统中的完整路径
            String saveDirectory = localPath + relativeDir;

            if (saveDirectory.contains("%20")) {
                saveDirectory = saveDirectory.replace("%20", " ");
            }

            directoryUtil.mkdir(saveDirectory);

            String strNewFileName =
                MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

            String saveFilePath = saveDirectory + strNewFileName;

            ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
            // 自定义字段别名
            writer.addHeaderAlias("ordinal", "序号");
            writer.addHeaderAlias("companyName", "监理公司");
            writer.addHeaderAlias("buildCount", "楼幢数");
            writer.addHeaderAlias("projectName", "项目");
            writer.addHeaderAlias("areaName", "区域");
            writer.addHeaderAlias("busiCode", "业务单号");
            writer.addHeaderAlias("completeDate", "完成时间");

            List<NoteChangeReportExcelVO> list = formart(empj_BldLimitAmountList);

            // 一次性写出内容，使用默认样式
            writer.write(list);

            // 关闭writer，释放内存
            writer.flush();
            writer.close();

            properties.put("fileDownloadPath", relativeDir + strNewFileName);

        }

        properties.put("reportList", reportList);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;

    }


    List<NoteChangeReportExcelVO> formart(List<Qs_BldLimitAmount_View> empj_BldLimitAmountList) {
        List<NoteChangeReportExcelVO> list = new ArrayList<NoteChangeReportExcelVO>();
        int ordinal = 0;
        for (Qs_BldLimitAmount_View po : empj_BldLimitAmountList) {
            ++ordinal;
            NoteChangeReportExcelVO vo = new NoteChangeReportExcelVO();

            vo.setOrdinal(ordinal);
            vo.setCompanyName(po.getCompanyName());
            vo.setBuildCount(null == po.getBuildCount() ? 0 : po.getBuildCount());
            vo.setProjectName(po.getProject().getTheName());
            vo.setAreaName(po.getProject().getCityRegion().getTheName());
            vo.setBusiCode(po.geteCode());
            vo.setCompleteDate(null == po.getAppointTimeStamp() ? ""
                : MyDatetime.getInstance().dateToSimpleString(po.getAppointTimeStamp()));

            list.add(vo);
        }
        return list;

    }



}

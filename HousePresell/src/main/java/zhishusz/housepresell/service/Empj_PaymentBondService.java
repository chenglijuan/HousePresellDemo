package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_DtlForm;
import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_DtlDao;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentBond;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/**
 * 支付保函service
 * 
 * @author xsz
 * @since 2020-5-12 11:32:29
 */
@Service
@Transactional
public class Empj_PaymentBondService {
    @Autowired
    private Empj_PaymentBondDao empj_PaymentBondDao;
    @Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;
    @Autowired
    private Sm_ApprovalProcess_DeleteService deleteService;
    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
    @Autowired
    private Sm_AttachmentDao smAttachmentDao;
    @Autowired
    private Sm_AttachmentCfgDao smAttachmentCfgDao;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;

    @Autowired
    private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
    @Autowired
    private Empj_ProjectInfoDao empj_ProjectInfoDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;

    @Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
    @Autowired
    private Empj_BuildingAccountSupervisedDao empj_buildingAccountSupervisedDao;
    @Autowired
    private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;// 楼幢账户
    @Autowired
    private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
    @Autowired
    private Tgpf_FundOverallPlanDetailDao tgpf_FundOverallPlanDetailDao;

    @Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;// 受限额度变更
    @Autowired
    private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;// 特殊拨付

    @Autowired
    private Empj_BldLimitAmount_DtlDao empj_BldLimitAmount_DtlDao; // 进度节点子表

    @Autowired
    private ExportPdfByWordService exportPdfByWordService;// 生成PDF
    @Autowired
    private Sm_AttachmentDao attacmentDao;
    @Autowired
    private Sm_AttachmentCfgDao attacmentcfgDao;

    private static final String BUSI_CODE = "06120501";

    public Properties execute(Empj_PaymentBondForm model) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Properties listExecute(Empj_PaymentBondForm model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        String keyword = model.getKeyword();

        String applyDate = model.getApplyDate();
        if (StringUtils.isBlank(applyDate)) {
            model.setApplyDate(null);
        }

        String approvalState = model.getApprovalState();
        if (StringUtils.isBlank(approvalState)) {
            model.setApprovalState(null);
        }

        if (StringUtils.isBlank(keyword)) {
            model.setKeyword(null);
        } else {
            model.setKeyword("%" + keyword + "%");
        }

        model.setTheState(S_TheState.Normal);

        Integer totalCount = empj_PaymentBondDao
            .findByPage_Size(empj_PaymentBondDao.getQuery_Size(empj_PaymentBondDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Empj_PaymentBond> empj_PaymentBondList;
        if (totalCount > 0) {
            empj_PaymentBondList = empj_PaymentBondDao.findByPage(
                empj_PaymentBondDao.getQuery(empj_PaymentBondDao.getBasicHQL(), model), pageNumber, countPerPage);
        } else {
            empj_PaymentBondList = new ArrayList<>();
        }

        List<Properties> list = new ArrayList<>();
        Properties pro;
        for (Empj_PaymentBond empj_PaymentBond : empj_PaymentBondList) {
            pro = new MyProperties();
            pro.put("tableId", empj_PaymentBond.getTableId());
            pro.put("eCode", empj_PaymentBond.geteCode());
            pro.put("applyDate",
                StringUtils.isBlank(empj_PaymentBond.getApplyDate()) ? "" : empj_PaymentBond.getApplyDate());
            pro.put("companyName",
                StringUtils.isBlank(empj_PaymentBond.getCompanyName()) ? "" : empj_PaymentBond.getCompanyName());
            pro.put("projectName",
                StringUtils.isBlank(empj_PaymentBond.getProjectName()) ? "" : empj_PaymentBond.getProjectName());
            pro.put("guaranteeNo",
                StringUtils.isBlank(empj_PaymentBond.getGuaranteeNo()) ? "" : empj_PaymentBond.getGuaranteeNo());
            pro.put("controlPercentage",
                null == empj_PaymentBond.getControlPercentage() ? 0.00 : empj_PaymentBond.getControlPercentage());
            pro.put("guaranteedSumAmount",
                null == empj_PaymentBond.getGuaranteedSumAmount() ? 0.00 : empj_PaymentBond.getGuaranteedSumAmount());
            pro.put("approvalState",
                StringUtils.isBlank(empj_PaymentBond.getApprovalState()) ? "0" : empj_PaymentBond.getApprovalState());
            pro.put("busiState",
                StringUtils.isBlank(empj_PaymentBond.getBusiState()) ? "0" : empj_PaymentBond.getBusiState());

            list.add(pro);

        }

        properties.put("empj_PaymentBondList", list);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        return properties;

    }

    @SuppressWarnings("unchecked")
    public Properties batchDeleteExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long[] idArr = model.getIdArr();

        if (idArr == null || idArr.length < 1) {
            return MyBackInfo.fail(properties, "没有需要删除的信息");
        }

        Empj_PaymentBond paymentBond;
        Empj_PaymentBondChildForm childModel;
        List<Empj_PaymentBondChild> empj_PaymentBondChildList;
        for (Long tableId : idArr) {
            paymentBond = empj_PaymentBondDao.findById(tableId);
            if (paymentBond == null) {
                return MyBackInfo.fail(properties, "存在无效的支付保函信息！");
            }

            paymentBond.setTheState(S_TheState.Deleted);
            empj_PaymentBondDao.save(paymentBond);

            childModel = new Empj_PaymentBondChildForm();
            childModel.setEmpj_PaymentBond(paymentBond);

            empj_PaymentBondChildList = empj_PaymentBondChildDao
                .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));
            for (Empj_PaymentBondChild empj_PaymentBondChild : empj_PaymentBondChildList) {
                empj_PaymentBondChild.setTheState(S_TheState.Deleted);
                empj_PaymentBondChildDao.save(empj_PaymentBondChild);
            }

            // 删除审批流
            deleteService.execute(tableId, model.getBusiCode());
        }

        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties addExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        String applyDate = model.getApplyDate();
        if (StringUtils.isBlank(applyDate)) {
            return MyBackInfo.fail(properties, "申请日期不能为空！");
        }
        Long companyId = model.getCompanyId();
        if (null == companyId) {
            return MyBackInfo.fail(properties, "请选择开发企业！");
        }
        Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(companyId);
        if (null == companyInfo) {
            return MyBackInfo.fail(properties, "选择的开发企业已失效，请刷新后重试！");
        }
        Long projectId = model.getProjectId();
        if (null == projectId) {
            return MyBackInfo.fail(properties, "请选择开发项目！");
        }
        Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(projectId);
        if (null == projectInfo) {
            return MyBackInfo.fail(properties, "选择的项目信息已失效，请刷新后重试！");
        }

        String guaranteeNo = model.getGuaranteeNo();
        Double controlPercentage = model.getControlPercentage();
        if (null == controlPercentage || 0.00 == controlPercentage) {
            return MyBackInfo.fail(properties, "请选择保函最低控制线！");
        }
        Double guaranteedAmount = model.getGuaranteedSumAmount();
        if (null == guaranteedAmount || 0.00 == guaranteedAmount) {
            return MyBackInfo.fail(properties, "保函总金额不能为空！");
        }
        String guaranteeCompany = model.getGuaranteeCompany();
        if (StringUtils.isBlank(guaranteeCompany)) {
            return MyBackInfo.fail(properties, "请选择保证机构！");
        }
        String guaranteeType = model.getGuaranteeType();
        if (StringUtils.isBlank(guaranteeType)) {
            return MyBackInfo.fail(properties, "请选择保函类型！");
        }
        String personOfProfits = model.getPersonOfProfits();
        if (StringUtils.isBlank(personOfProfits)) {
            return MyBackInfo.fail(properties, "请输入受益人！");
        }
        String remark = model.getRemark();

        List<Empj_PaymentBondChildForm> empj_PaymentBondChildList = model.getEmpj_PaymentBondChildList();
        if (null == empj_PaymentBondChildList || empj_PaymentBondChildList.size() < 1) {
            return MyBackInfo.fail(properties, "请选择需要支付保函的楼幢信息！");
        }

        Long nowDate = System.currentTimeMillis();

        Empj_PaymentBond paymentBond = new Empj_PaymentBond();
        paymentBond.setTheState(S_TheState.Normal);
        paymentBond.setBusiState("0");
        paymentBond.setApprovalState(S_ApprovalState.WaitSubmit);
        paymentBond.setCreateTimeStamp(nowDate);
        paymentBond.setLastUpdateTimeStamp(nowDate);
        paymentBond.setUserStart(user);
        paymentBond.setUserUpdate(user);

        paymentBond.setApplyDate(applyDate);
        paymentBond.setCompany(companyInfo);
        paymentBond.setCompanyName(companyInfo.getTheName());
        paymentBond.setProject(projectInfo);
        paymentBond.setProjectName(projectInfo.getTheName());
        paymentBond.setGuaranteeNo(guaranteeNo);
        paymentBond.setControlPercentage(controlPercentage);
        paymentBond.setGuaranteedSumAmount(guaranteedAmount);
        paymentBond.setGuaranteeCompany(guaranteeCompany);
        paymentBond.setGuaranteeType(guaranteeType);
        paymentBond.setPersonOfProfits(personOfProfits);
        paymentBond.setRemark(remark);

        /*
         * 校验互斥业务：
         * 进度节点变更
         * 用款申请
         * 退房退款
         * 特殊拨付
         * 自身
         */

        Empj_BuildingInfo buildingInfo;
        Empj_PaymentBondChild bondChild;

        Empj_BldLimitAmount_DtlForm dtlModel;
        List<Empj_BldLimitAmount_Dtl> listDtls;

        Empj_PaymentBondChildForm childModel;
        Empj_BldLimitAmount_AFForm AFModel;
        Tgpf_FundAppropriated_AFDtlForm fundAppropriated_AFDtlForm;
        Tgpf_SpecialFundAppropriated_AFForm specialFundAppropriated_AFForm;

        List<Empj_PaymentBondChild> childList;
        Empj_PaymentBondChild empj_PaymentBondChild;
        List<Empj_BldLimitAmount_AF> afList;
        Empj_BldLimitAmount_AF empj_BldLimitAmount_AF;
        Integer countNum = 0;
        for (Empj_PaymentBondChildForm form : empj_PaymentBondChildList) {
            buildingInfo = empj_BuildingInfoDao.findById(form.getBuildingId());
            if (null == buildingInfo) {
                continue;
            }

            // 支付保函校验
            childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_BuildingInfo(buildingInfo);
            childList = empj_PaymentBondChildDao.findByPage(
                empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQLNoRecord(), childModel));
            if (!childList.isEmpty()) {
                return MyBackInfo.fail(properties, "楼幢：" + buildingInfo.geteCodeFromConstruction() + "已发起支付保函申请！");
            }

            // empj_BldLimitAmount_DtlDao
            // 进度变更校验
            dtlModel = new Empj_BldLimitAmount_DtlForm();
            dtlModel.setTheState(S_TheState.Normal);
            dtlModel.setBuilding(buildingInfo);
            dtlModel.seteCodeOfBuilding(buildingInfo.geteCode());

            listDtls = empj_BldLimitAmount_DtlDao.findByPage(
                empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getCheckByBuild(), dtlModel));
            if (listDtls.size() > 0) {
                return MyBackInfo.fail(properties,
                    "楼幢：" + buildingInfo.geteCodeFromConstruction() + "已发起节点变更流程，请待流程结束后重新申请！");
            }

            fundAppropriated_AFDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
            fundAppropriated_AFDtlForm.setTheState(S_TheState.Normal);
            fundAppropriated_AFDtlForm.setBuildingId(buildingInfo.getTableId());
            Integer findByPage_Size2 = tgpf_FundAppropriated_AFDtlDao.findByPage_Size(tgpf_FundAppropriated_AFDtlDao
                .getQuery_Size(tgpf_FundAppropriated_AFDtlDao.getCheckHQL(), fundAppropriated_AFDtlForm));
            if (findByPage_Size2 > 0) {
                return MyBackInfo.fail(properties,
                    "楼幢编号：" + buildingInfo.geteCodeFromConstruction() + " 已进行用款申请业务，请通过审批后再进行此业务！");
            }

            // 特殊拨付校验
            specialFundAppropriated_AFForm = new Tgpf_SpecialFundAppropriated_AFForm();
            specialFundAppropriated_AFForm.setTheState(S_TheState.Normal);
            specialFundAppropriated_AFForm.setBuildingId(buildingInfo.getTableId());
            countNum = tgpf_SpecialFundAppropriated_AFDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDao
                .getQuery_Size(tgpf_SpecialFundAppropriated_AFDao.getCheckHQL(), specialFundAppropriated_AFForm));
            if (countNum > 0) {
                return MyBackInfo.fail(properties,
                    "楼幢：" + buildingInfo.geteCodeFromConstruction() + " 已进行特殊拨付业务，请通过审批后再进行此业务！");
            }

        }

        paymentBond.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
        Serializable tableId = empj_PaymentBondDao.save(paymentBond);

        Tgpj_BuildingAccount buildingAccount;
        for (Empj_PaymentBondChildForm form : empj_PaymentBondChildList) {
            bondChild = new Empj_PaymentBondChild();
            bondChild.setTheState(S_TheState.Normal);
            bondChild.setBusiState("0");
            bondChild.setCreateTimeStamp(nowDate);
            bondChild.setLastUpdateTimeStamp(nowDate);
            bondChild.setUserStart(user);
            bondChild.setUserUpdate(user);

            buildingInfo = empj_BuildingInfoDao.findById(form.getBuildingId());
            if (null == buildingInfo) {
                continue;
            }
            bondChild.setEmpj_BuildingInfo(buildingInfo);
            bondChild.setEmpj_PaymentBond(paymentBond);

            // 查询楼幢是否做过支付保函
            /*childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_BuildingInfo(buildingInfo);
            childList = empj_PaymentBondChildDao
                .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQLRecord(), childModel));
            if (!childList.isEmpty()) {
                bondChild.setHasExist("有");
            }else{
                bondChild.setHasExist("无");
            }*/

            buildingAccount = buildingInfo.getBuildingAccount();
            if (null == buildingAccount) {
                continue;
            }
            if ((null == buildingAccount.getTotalGuaranteeAmount() ? 0.00
                : buildingAccount.getTotalGuaranteeAmount()) > 0.00) {
                bondChild.setHasExist("有");
            } else {
                bondChild.setHasExist("无");
            }

            bondChild.seteCodeFromConstruction(form.geteCodeFromConstruction());
            bondChild.setOrgLimitedAmount(form.getOrgLimitedAmount());
            bondChild.setCurrentFigureProgress(form.getCurrentFigureProgress());
            bondChild.setCurrentLimitedRatio(form.getCurrentLimitedRatio());
            bondChild.setNodeLimitedAmount(form.getNodeLimitedAmount());
            bondChild.setCurrentEscrowFund(form.getCurrentEscrowFund());
            bondChild.setSpilloverAmount(form.getSpilloverAmount());
            bondChild.setControlAmount(form.getControlAmount());
            bondChild.setReleaseAmount(form.getReleaseAmount());
            bondChild.setPaymentBondAmount(form.getPaymentBondAmount());
            bondChild.setActualReleaseAmount(form.getActualReleaseAmount());
            bondChild.setAfterCashLimitedAmount(form.getAfterCashLimitedAmount());
            bondChild.setAfterEffectiveLimitedAmount(form.getAfterEffectiveLimitedAmount());
            bondChild.setEffectiveLimitedAmount(form.getEffectiveLimitedAmount());
            bondChild.setCanApplyAmount(form.getCanApplyAmount());

            empj_PaymentBondChildDao.save(bondChild);

        }

        // 附件信息
        String smAttachmentList = null;
        if (null != model.getSmAttachmentList()) {
            smAttachmentList = model.getSmAttachmentList().toString();
        }
        List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);
        if (null != gasList && gasList.size() > 0) {
            for (Sm_Attachment sm_Attachment : gasList) {
                // 查询附件配置表
                Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
                form.seteCode(sm_Attachment.getSourceType());
                Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
                    .findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

                sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
                sm_Attachment.setSourceId(tableId.toString());
                sm_Attachment.setTheState(S_TheState.Normal);
                smAttachmentDao.save(sm_Attachment);
            }
        }

        properties.put("tableId", tableId);

        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties editExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要修改的单据信息！");
        }
        Empj_PaymentBond paymentBond = empj_PaymentBondDao.findById(tableId);
        if (null == paymentBond) {
            return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
        }

        String applyDate = model.getApplyDate();
        if (StringUtils.isBlank(applyDate)) {
            return MyBackInfo.fail(properties, "申请日期不能为空！");
        }
        Long companyId = model.getCompanyId();
        if (null == companyId) {
            return MyBackInfo.fail(properties, "请选择开发企业！");
        }
        Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(companyId);
        if (null == companyInfo) {
            return MyBackInfo.fail(properties, "选择的开发企业已失效，请刷新后重试！");
        }
        Long projectId = model.getProjectId();
        if (null == projectId) {
            return MyBackInfo.fail(properties, "请选择开发项目！");
        }
        Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(projectId);
        if (null == projectInfo) {
            return MyBackInfo.fail(properties, "选择的项目信息已失效，请刷新后重试！");
        }

        String guaranteeNo = model.getGuaranteeNo();
        Double controlPercentage = model.getControlPercentage();
        if (null == controlPercentage || 0.00 == controlPercentage) {
            return MyBackInfo.fail(properties, "请选择保函最低控制线！");
        }
        Double guaranteedAmount = model.getGuaranteedSumAmount();
        if (null == guaranteedAmount || 0.00 == guaranteedAmount) {
            return MyBackInfo.fail(properties, "保函总金额不能为空！");
        }
        String guaranteeCompany = model.getGuaranteeCompany();
        if (StringUtils.isBlank(guaranteeCompany)) {
            return MyBackInfo.fail(properties, "请选择保证机构！");
        }
        String guaranteeType = model.getGuaranteeType();
        if (StringUtils.isBlank(guaranteeType)) {
            return MyBackInfo.fail(properties, "请选择保函类型！");
        }
        String personOfProfits = model.getPersonOfProfits();
        if (StringUtils.isBlank(personOfProfits)) {
            return MyBackInfo.fail(properties, "请输入受益人！");
        }
        String remark = model.getRemark();

        List<Empj_PaymentBondChildForm> empj_PaymentBondChildList = model.getEmpj_PaymentBondChildList();
        if (null == empj_PaymentBondChildList || empj_PaymentBondChildList.size() < 1) {
            return MyBackInfo.fail(properties, "请选择需要支付保函的楼幢信息！");
        }

        Long nowDate = System.currentTimeMillis();

        paymentBond.setLastUpdateTimeStamp(nowDate);
        paymentBond.setUserUpdate(user);

        paymentBond.setApplyDate(applyDate);
        paymentBond.setCompany(companyInfo);
        paymentBond.setCompanyName(companyInfo.getTheName());
        paymentBond.setProject(projectInfo);
        paymentBond.setProjectName(projectInfo.getTheName());
        paymentBond.setGuaranteeNo(guaranteeNo);
        paymentBond.setControlPercentage(controlPercentage);
        paymentBond.setGuaranteedSumAmount(guaranteedAmount);
        paymentBond.setGuaranteeCompany(guaranteeCompany);
        paymentBond.setGuaranteeType(guaranteeType);
        paymentBond.setPersonOfProfits(personOfProfits);
        paymentBond.setRemark(remark);

        empj_PaymentBondDao.update(paymentBond);

        // 删除原子表信息
        Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
        childModel.setTheState(S_TheState.Normal);
        childModel.setEmpj_PaymentBond(paymentBond);
        List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
            .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));
        for (Empj_PaymentBondChild empj_PaymentBondChild : childList) {
            empj_PaymentBondChild.setTheState(S_TheState.Deleted);
            empj_PaymentBondChildDao.update(empj_PaymentBondChild);
        }
        
        Tgpj_BuildingAccount buildingAccount;

        Empj_BuildingInfo buildingInfo;
        Empj_PaymentBondChild bondChild;
        for (Empj_PaymentBondChildForm form : empj_PaymentBondChildList) {
            bondChild = new Empj_PaymentBondChild();
            bondChild.setTheState(S_TheState.Normal);
            bondChild.setBusiState("0");
            bondChild.setCreateTimeStamp(nowDate);
            bondChild.setLastUpdateTimeStamp(nowDate);
            bondChild.setUserStart(user);
            bondChild.setUserUpdate(user);

            buildingInfo = empj_BuildingInfoDao.findById(form.getBuildingId());
            if (null == buildingInfo) {
                continue;
            }
            
            buildingAccount = buildingInfo.getBuildingAccount();
            if (null == buildingAccount) {
                continue;
            }
            if ((null == buildingAccount.getTotalGuaranteeAmount() ? 0.00
                : buildingAccount.getTotalGuaranteeAmount()) > 0.00) {
                bondChild.setHasExist("有");
            } else {
                bondChild.setHasExist("无");
            }
            
            bondChild.setEmpj_BuildingInfo(buildingInfo);
            bondChild.setEmpj_PaymentBond(paymentBond);

            bondChild.seteCodeFromConstruction(form.geteCodeFromConstruction());
            bondChild.setOrgLimitedAmount(form.getOrgLimitedAmount());
            bondChild.setCurrentFigureProgress(form.getCurrentFigureProgress());
            bondChild.setCurrentLimitedRatio(form.getCurrentLimitedRatio());
            bondChild.setNodeLimitedAmount(form.getNodeLimitedAmount());
            bondChild.setCurrentEscrowFund(form.getCurrentEscrowFund());
            bondChild.setSpilloverAmount(form.getSpilloverAmount());
            bondChild.setControlAmount(form.getControlAmount());
            bondChild.setReleaseAmount(form.getReleaseAmount());
            bondChild.setPaymentBondAmount(form.getPaymentBondAmount());
            bondChild.setActualReleaseAmount(form.getActualReleaseAmount());
            bondChild.setAfterCashLimitedAmount(form.getAfterCashLimitedAmount());
            bondChild.setAfterEffectiveLimitedAmount(form.getAfterEffectiveLimitedAmount());
            bondChild.setEffectiveLimitedAmount(form.getEffectiveLimitedAmount());
            bondChild.setCanApplyAmount(form.getCanApplyAmount());

            empj_PaymentBondChildDao.save(bondChild);

        }

        /*
         * 修改附件
         * 附件需要先进行删除操作，然后进行重新上传保存功能
         */
        // 附件信息
        String smAttachmentJson = null;
        if (null != model.getSmAttachmentList() && model.getSmAttachmentList().length() > 0) {

            Sm_AttachmentForm from = new Sm_AttachmentForm();

            String sourceId = String.valueOf(tableId);
            from.setTheState(S_TheState.Normal);
            from.setSourceId(sourceId);

            // 查询附件
            List<Sm_Attachment> smAttachmentList =
                smAttachmentDao.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), from));
            // 删除附件
            if (null != smAttachmentList && smAttachmentList.size() > 0) {
                for (Sm_Attachment sm_Attachment : smAttachmentList) {
                    sm_Attachment.setTheState(S_TheState.Deleted);
                    smAttachmentDao.save(sm_Attachment);
                }
            }

            // 重新保存附件
            smAttachmentJson = model.getSmAttachmentList().toString();
            List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);

            if (null != gasList && gasList.size() > 0) {
                for (Sm_Attachment sm_Attachment : gasList) {
                    // 查询附件配置表
                    Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
                    form.seteCode(sm_Attachment.getSourceType());
                    Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
                        .findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

                    sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
                    sm_Attachment.setSourceId(tableId.toString());
                    sm_Attachment.setTheState(S_TheState.Normal);
                    sm_Attachment.setBusiType(BUSI_CODE);// 业务类型
                    smAttachmentDao.save(sm_Attachment);
                }
            }
        }

        properties.put("tableId", tableId);

        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties detailExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据信息！");
        }
        Empj_PaymentBond paymentBond = empj_PaymentBondDao.findById(tableId);
        if (null == paymentBond) {
            return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
        }

        Properties afPro = new MyProperties();
        afPro.put("tableId", paymentBond.getTableId());
        afPro.put("busiState", StringUtils.isBlank(paymentBond.getBusiState()) ? "0" : paymentBond.getBusiState());
        afPro.put("approvalState",
            StringUtils.isBlank(paymentBond.getApprovalState()) ? "0" : paymentBond.getApprovalState());
        afPro.put("eCode", StringUtils.isBlank(paymentBond.geteCode()) ? "" : paymentBond.geteCode());
        afPro.put("applyDate", StringUtils.isBlank(paymentBond.getApplyDate()) ? "" : paymentBond.getApplyDate());
        afPro.put("companyId", null == paymentBond.getCompany() ? "" : paymentBond.getCompany().getTableId());
        afPro.put("companyName", StringUtils.isBlank(paymentBond.getCompanyName()) ? "" : paymentBond.getCompanyName());
        afPro.put("projectId", null == paymentBond.getProject() ? "" : paymentBond.getProject().getTableId());
        afPro.put("projectName", StringUtils.isBlank(paymentBond.getProjectName()) ? "" : paymentBond.getProjectName());
        afPro.put("guaranteeNo", StringUtils.isBlank(paymentBond.getGuaranteeNo()) ? "" : paymentBond.getGuaranteeNo());
        afPro.put("controlPercentage",
            null == paymentBond.getControlPercentage() ? 0.00 : paymentBond.getControlPercentage());
        afPro.put("guaranteedSumAmount",
            null == paymentBond.getGuaranteedSumAmount() ? 0.00 : paymentBond.getGuaranteedSumAmount());
        afPro.put("guaranteeCompany",
            StringUtils.isBlank(paymentBond.getGuaranteeCompany()) ? "1" : paymentBond.getGuaranteeCompany());
        afPro.put("guaranteeType",
            StringUtils.isBlank(paymentBond.getGuaranteeType()) ? "1" : paymentBond.getGuaranteeType());
        afPro.put("personOfProfits",
            StringUtils.isBlank(paymentBond.getPersonOfProfits()) ? "" : paymentBond.getPersonOfProfits());
        afPro.put("remark", StringUtils.isBlank(paymentBond.getRemark()) ? "" : paymentBond.getRemark());

        afPro.put("userUpdate", null == paymentBond.getUserUpdate() ? "" : paymentBond.getUserUpdate().getTheName());
        afPro.put("lastUpdateTimeStamp", null == paymentBond.getLastUpdateTimeStamp() ? null
            : MyDatetime.getInstance().dateToString2(paymentBond.getLastUpdateTimeStamp()));
        afPro.put("userRecord", null == paymentBond.getUserRecord() ? "" : paymentBond.getUserRecord().getTheName());
        afPro.put("recordTimeStamp", null == paymentBond.getRecordTimeStamp() ? null
            : MyDatetime.getInstance().dateToString2(paymentBond.getRecordTimeStamp()));

        properties.put("empj_PaymentBond", afPro);

        // 查询子表信息
        Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
        childModel.setTheState(S_TheState.Normal);
        childModel.setEmpj_PaymentBond(paymentBond);
        List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
            .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBuildListSortHQL(), childModel));

        List<Properties> dtlList = new ArrayList<>();
        Properties dtlPro;
        if (childList.size() > 0) {
            for (Empj_PaymentBondChild empj_PaymentBondChild : childList) {
                dtlPro = new MyProperties();
                dtlPro.put("tableId", empj_PaymentBondChild.getTableId());
                dtlPro.put("buildingId", null == empj_PaymentBondChild.getEmpj_BuildingInfo() ? ""
                    : empj_PaymentBondChild.getEmpj_BuildingInfo().getTableId());
                dtlPro.put("eCodeFromConstruction",
                    StringUtils.isBlank(empj_PaymentBondChild.geteCodeFromConstruction()) ? ""
                        : empj_PaymentBondChild.geteCodeFromConstruction());
                dtlPro.put("orgLimitedAmount", null == empj_PaymentBondChild.getOrgLimitedAmount() ? 0.00
                    : empj_PaymentBondChild.getOrgLimitedAmount());
                dtlPro.put("currentFigureProgress",
                    StringUtils.isBlank(empj_PaymentBondChild.getCurrentFigureProgress()) ? ""
                        : empj_PaymentBondChild.getCurrentFigureProgress());
                dtlPro.put("currentLimitedRatio", null == empj_PaymentBondChild.getCurrentLimitedRatio() ? 0.00
                    : empj_PaymentBondChild.getCurrentLimitedRatio());
                dtlPro.put("nodeLimitedAmount", null == empj_PaymentBondChild.getNodeLimitedAmount() ? 0.00
                    : empj_PaymentBondChild.getNodeLimitedAmount());
                dtlPro.put("currentEscrowFund", null == empj_PaymentBondChild.getCurrentEscrowFund() ? 0.00
                    : empj_PaymentBondChild.getCurrentEscrowFund());
                dtlPro.put("spilloverAmount", null == empj_PaymentBondChild.getSpilloverAmount() ? 0.00
                    : empj_PaymentBondChild.getSpilloverAmount());
                dtlPro.put("controlAmount",
                    null == empj_PaymentBondChild.getControlAmount() ? 0.00 : empj_PaymentBondChild.getControlAmount());
                dtlPro.put("releaseAmount",
                    null == empj_PaymentBondChild.getReleaseAmount() ? 0.00 : empj_PaymentBondChild.getReleaseAmount());
                dtlPro.put("paymentBondAmount", null == empj_PaymentBondChild.getPaymentBondAmount() ? 0.00
                    : empj_PaymentBondChild.getPaymentBondAmount());
                dtlPro.put("actualReleaseAmount", null == empj_PaymentBondChild.getActualReleaseAmount() ? 0.00
                    : empj_PaymentBondChild.getActualReleaseAmount());
                dtlPro.put("afterCashLimitedAmount", null == empj_PaymentBondChild.getAfterCashLimitedAmount() ? 0.00
                    : empj_PaymentBondChild.getAfterCashLimitedAmount());
                dtlPro.put("afterEffectiveLimitedAmount", null == empj_PaymentBondChild.getAfterEffectiveLimitedAmount()
                    ? 0.00 : empj_PaymentBondChild.getAfterEffectiveLimitedAmount());
                dtlPro.put("effectiveLimitedAmount", null == empj_PaymentBondChild.getEffectiveLimitedAmount() ? 0.00
                    : empj_PaymentBondChild.getEffectiveLimitedAmount());
                dtlPro.put("hasExist",
                    StrUtil.isBlank(empj_PaymentBondChild.getHasExist()) ? "无" : empj_PaymentBondChild.getHasExist());
                dtlPro.put("canApplyAmount", null == empj_PaymentBondChild.getCanApplyAmount() ? 0.00
                    : empj_PaymentBondChild.getCanApplyAmount());

                dtlList.add(dtlPro);
            }
        }

        properties.put("empj_PaymentBondChildList", dtlList);

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(String.valueOf(tableId));
        sm_AttachmentForm.setBusiType(BUSI_CODE);
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 加载所有相关附件信息
        List<Sm_Attachment> sm_AttachmentList =
            smAttachmentDao.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL(), sm_AttachmentForm));
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            sm_AttachmentList = new ArrayList<Sm_Attachment>();
        }

        // 查询同一附件类型下的所有附件信息（附件信息归类）
        List<Sm_Attachment> smList = null;

        Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
        form.setBusiType(BUSI_CODE);
        form.setTheState(S_TheState.Normal);

        List<Sm_AttachmentCfg> smAttachmentCfgList =
            smAttachmentCfgDao.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

        if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0) {
            smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
        } else {
            for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
                String sourceType = sm_Attachment.getSourceType();

                for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) {
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
        }

        properties.put("smAttachmentCfgList", smAttachmentCfgList);
        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties submitExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Long nowDate = System.currentTimeMillis();

        String buttonType = model.getButtonType();
        if (StringUtils.isBlank(buttonType)) {
            buttonType = "2";
        }
        String busiCode = model.getBusiCode();
        if (StringUtils.isBlank(busiCode)) {
            busiCode = BUSI_CODE;
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据信息！");
        }
        Empj_PaymentBond paymentBond = empj_PaymentBondDao.findById(tableId);
        if (null == paymentBond) {
            return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
        }

        model.setButtonType(buttonType);
        model.setBusiCode(busiCode);

        if (S_ApprovalState.Examining.equals(paymentBond.getApprovalState())) {
            return MyBackInfo.fail(properties, "该单据已在审核中，不可重复提交");
        } else if (S_ApprovalState.Completed.equals(paymentBond.getApprovalState())) {
            return MyBackInfo.fail(properties, "该单据已审批完成，不可重复提交");
        }

        properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
        if ("noApproval".equals(properties.getProperty("info"))) {

            paymentBond.setApprovalState(S_ApprovalState.Completed);
            paymentBond.setBusiState("1");
            paymentBond.setLastUpdateTimeStamp(nowDate);
            paymentBond.setUserUpdate(user);
            paymentBond.setUserRecord(user);
            paymentBond.setRecordTimeStamp(nowDate);

            /*
             * 审批结束后推送到用款申请
             */
            // 查询子表信息
            Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_PaymentBond(paymentBond);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
                .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));

            Tgpf_FundAppropriated_AF af = new Tgpf_FundAppropriated_AF();
            af.seteCode(sm_BusinessCodeGetService.execute("06120301"));
            af.setTheState(S_TheState.Normal);
            af.setUserStart(paymentBond.getUserStart());
            af.setUserUpdate(paymentBond.getUserStart());
            af.setUserRecord(paymentBond.getUserStart());
            af.setCreateTimeStamp(nowDate);
            af.setLastUpdateTimeStamp(nowDate);
            af.setRecordTimeStamp(nowDate);
            af.setApplyDate(MyDatetime.getInstance().dateToSimpleString(nowDate));

            af.setDevelopCompany(paymentBond.getCompany());
            af.setApplyDate(MyDatetime.getInstance().getCurrentDate());
            af.seteCodeOfDevelopCompany(null == paymentBond.getCompany() ? "" : paymentBond.getCompany().geteCode());
            af.setProject(paymentBond.getProject());
            af.setTheNameOfDevelopCompany(
                null == paymentBond.getCompany() ? "" : paymentBond.getCompany().getTheName());
            af.setTheNameOfProject(
                StringUtils.isBlank(paymentBond.getProjectName()) ? "" : paymentBond.getProjectName());
            af.seteCodeOfProject(null == paymentBond.getProject() ? "" : paymentBond.getProject().geteCode());
            af.setTotalApplyAmount(
                null == paymentBond.getGuaranteedSumAmount() ? 0.00 : paymentBond.getGuaranteedSumAmount());
            af.setApplyState(2);
            af.setApprovalState("已完结");
            af.setApplyType("1");// 保函支付

            // 楼幢信息
            Empj_BuildingInfo empj_BuildingInfo;
            // 楼幢账户信息
            Tgpj_BuildingAccount buildingAccount;
            // 楼幢监管账号
            Empj_BuildingAccountSupervisedForm accountSupervisedForm;
            List<Empj_BuildingAccountSupervised> empj_buildingAccountSuperviseds;

            List<Tgpf_FundAppropriated_AFDtl> dtlList = new ArrayList<>();
            // 用款申请子表信息
            for (Empj_PaymentBondChild child : childList) {
                empj_BuildingInfo = child.getEmpj_BuildingInfo();
                buildingAccount = empj_BuildingInfo.getBuildingAccount();
                accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
                accountSupervisedForm.setTheState(S_TheState.Normal);
                accountSupervisedForm.setBuildingInfoId(empj_BuildingInfo.getTableId());
                empj_buildingAccountSuperviseds =
                    empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao
                        .getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));

                if (empj_buildingAccountSuperviseds.size() > 0) {
                    Tgpf_FundAppropriated_AFDtl dtl = new Tgpf_FundAppropriated_AFDtl();
                    dtl.setTheState(S_TheState.Normal);
                    dtl.setBuilding(child.getEmpj_BuildingInfo());
                    dtl.seteCodeOfBuilding(child.geteCodeFromConstruction());
                    dtl.setMainTable(af);

                    dtl.setBankAccountSupervised(empj_buildingAccountSuperviseds.get(0).getBankAccountSupervised());
                    dtl.setSupervisedBankAccount(
                        empj_buildingAccountSuperviseds.get(0).getBankAccountSupervised().getTheAccount());
                    dtl.setPayoutState(S_PayoutState.NotAppropriated); // 拨付状态 1: 未拨付
                    dtl.setAppliedAmount(
                        null == child.getActualReleaseAmount() ? 0.00 : child.getActualReleaseAmount());
                    dtl.setAllocableAmount(
                        null == buildingAccount.getAllocableAmount() ? 0.00 : buildingAccount.getAllocableAmount());
                    dtl.setEscrowStandard(
                        null == buildingAccount.getEscrowStandard() ? "" : buildingAccount.getEscrowStandard());
                    dtl.setOrgLimitedAmount(
                        null == buildingAccount.getOrgLimitedAmount() ? 0.00 : buildingAccount.getOrgLimitedAmount());
                    dtl.setCurrentFigureProgress(StringUtils.isBlank(buildingAccount.getCurrentFigureProgress()) ? ""
                        : buildingAccount.getCurrentFigureProgress());
                    dtl.setCurrentLimitedRatio(null == buildingAccount.getCurrentLimitedRatio() ? 100.00
                        : buildingAccount.getCurrentLimitedRatio());
                    dtl.setCurrentLimitedAmount(dtl.getOrgLimitedAmount() * dtl.getCurrentLimitedRatio() / 100);
                    dtl.setTotalAccountAmount(null == buildingAccount.getTotalAccountAmount() ? 0.00
                        : buildingAccount.getTotalAccountAmount());
                    dtl.setAppliedPayoutAmount(null == buildingAccount.getAppliedNoPayoutAmount() ? 0.00
                        : buildingAccount.getAppliedNoPayoutAmount());
                    dtl.setCurrentEscrowFund(
                        null == buildingAccount.getCurrentEscrowFund() ? 0.00 : buildingAccount.getCurrentEscrowFund());
                    dtl.setRefundAmount(
                        null == buildingAccount.getRefundAmount() ? 0.00 : buildingAccount.getRefundAmount());
                    dtl.setUserStart(model.getUser());
                    dtl.setCreateTimeStamp(nowDate);
                    dtl.setUserUpdate(model.getUser());
                    dtl.setLastUpdateTimeStamp(nowDate);

                    dtl.setCashLimitedAmount(
                        null == buildingAccount.getCashLimitedAmount() ? 0.00 : buildingAccount.getCashLimitedAmount());
                    dtl.setEffectiveLimitedAmount(null == buildingAccount.getEffectiveLimitedAmount() ? 0.00
                        : buildingAccount.getEffectiveLimitedAmount());
                    // tgpf_FundAppropriated_AFDtlDao.save(dtl);

                    dtlList.add(dtl);
                }
            }

            // 用款申请汇总数据
            List<Tgpf_FundOverallPlanDetail> detailList = new ArrayList<>();
            Tgpf_FundOverallPlanDetail planDetail;
            for (Tgpf_FundAppropriated_AFDtl dtl : dtlList) {

                boolean isSave = true;
                for (Tgpf_FundOverallPlanDetail detail : detailList) {
                    if (dtl.equals(detail.getSupervisedBankAccount())) {
                        isSave = false;
                        detail.setAppliedAmount(detail.getAppliedAmount() + dtl.getAppliedAmount());
                        tgpf_FundOverallPlanDetailDao.update(detail);
                    }
                }

                if (isSave) {
                    planDetail = new Tgpf_FundOverallPlanDetail();
                    planDetail.setTheState(S_TheState.Normal);
                    planDetail.setCreateTimeStamp(nowDate);
                    planDetail.setUserStart(model.getUser());
                    planDetail.setUserUpdate(model.getUser());
                    planDetail.setLastUpdateTimeStamp(nowDate);

                    planDetail.setMainTable(af);
                    planDetail.setTheNameOfProject(
                        StringUtils.isBlank(paymentBond.getProjectName()) ? "" : paymentBond.getProjectName());
                    planDetail.setTheNameOfBankBranch(dtl.getBankAccountSupervised().getTheNameOfBank());
                    planDetail.setBankAccountSupervised(dtl.getBankAccountSupervised());
                    planDetail.setTheNameOfAccount(dtl.getBankAccountSupervised().getTheName());
                    planDetail.setSupervisedBankAccount(dtl.getSupervisedBankAccount());
                    planDetail.setAppliedAmount(dtl.getAppliedAmount());
                    // tgpf_FundOverallPlanDetailDao.save(planDetail);
                    detailList.add(planDetail);
                }

            }
            af.setFundAppropriated_AFDtlList(dtlList);
            af.setFundOverallPlanDetailList(detailList);
            tgpf_FundAppropriated_AFDao.save(af);

        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            // 判断当前登录用户是否有权限发起审批
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
                 * xsz by time 提交结束后调用生成PDF方法
                 * 并将生成PDF后上传值OSS路径返回给前端
                 * 
                 * 参数：
                 * sourceBusiCode：业务编码
                 * sourceId：单据ID
                 * 
                 * xsz by time 2019-3-11 19:28:10
                 * 2.0
                 * 每次点击提交时，重新生成新的协议pdf
                 */
                if (null != user.getIsSignature() && "1".equals(user.getIsSignature())) {

                    // 查询是否已经存在PDF附件
                    Sm_Attachment attachment = isSaveAttachment(busiCode, String.valueOf(tableId));
                    if (null != attachment) {
                        // 如果存在附件，置为删除态重新生成
                        attachment.setTheState(S_TheState.Deleted);
                        attacmentDao.save(attachment);
                    }

                    ExportPdfForm pdfModel = new ExportPdfForm();
                    pdfModel.setSourceBusiCode(busiCode);
                    pdfModel.setSourceId(String.valueOf(tableId));
                    Properties executeProperties = exportPdfByWordService.execute(pdfModel);
                    String pdfUrl = (String)executeProperties.get("pdfUrl");

                    Map<String, String> signatureMap = new HashMap<>();

                    signatureMap.put("signaturePath", pdfUrl);
                    // TODO 此配置后期做成配置
                    signatureMap.put("signatureKeyword", "开发企业（盖章）");
                    System.out.println(signatureMap.get("signatureKeyword"));
                    signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());

                    properties.put("signatureMap", signatureMap);
                    properties.put(S_NormalFlag.result, S_NormalFlag.success);
                    properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

                    return properties;
                }
            }

            /*************************************标题新增楼栋显示************************************************************************/

            StringBuffer sb = new StringBuffer().append(":");
            // 查询子表信息
            Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_PaymentBond(paymentBond);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
                    .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));
            for (Empj_PaymentBondChild child:childList) {
                sb.append(child.geteCodeFromConstruction()).append(",");
            }
            model.setKeyword(sb.toString());
//            System.out.println("提交的时候："+model.getKeyword());

            /**********************************************************************************************************************/


            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
                (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");

            // 审批操作
            sm_approvalProcessService.execute(paymentBond, model, sm_approvalProcess_cfg);
            // 审批流程状态-审核中
            paymentBond.setApprovalState(S_ApprovalState.Examining);

        }

        paymentBond.setUserUpdate(user);
        paymentBond.setLastUpdateTimeStamp(nowDate);

        empj_PaymentBondDao.update(paymentBond);

        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties submitExecute_Copy(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Long nowDate = System.currentTimeMillis();

        String buttonType = model.getButtonType();
        if (StringUtils.isBlank(buttonType)) {
            buttonType = "2";
        }
        String busiCode = model.getBusiCode();
        if (StringUtils.isBlank(busiCode)) {
            busiCode = BUSI_CODE;
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据信息！");
        }
        Empj_PaymentBond paymentBond = empj_PaymentBondDao.findById(tableId);
        if (null == paymentBond) {
            return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
        }

        model.setButtonType(buttonType);
        model.setBusiCode(busiCode);

        if (S_ApprovalState.Examining.equals(paymentBond.getApprovalState())) {
            return MyBackInfo.fail(properties, "该单据已在审核中，不可重复提交");
        } else if (S_ApprovalState.Completed.equals(paymentBond.getApprovalState())) {
            return MyBackInfo.fail(properties, "该单据已审批完成，不可重复提交");
        }

        properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
        if ("noApproval".equals(properties.getProperty("info"))) {

            paymentBond.setApprovalState(S_ApprovalState.Completed);
            paymentBond.setBusiState("1");
            paymentBond.setLastUpdateTimeStamp(nowDate);
            paymentBond.setUserUpdate(user);
            paymentBond.setUserRecord(user);
            paymentBond.setRecordTimeStamp(nowDate);

            /*
             * 审批结束后推送到用款申请
             */
            // 查询子表信息
            Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_PaymentBond(paymentBond);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
                .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));

            Tgpf_FundAppropriated_AF af = new Tgpf_FundAppropriated_AF();
            af.seteCode(sm_BusinessCodeGetService.execute("06120301"));
            af.setTheState(S_TheState.Normal);
            af.setUserStart(model.getUser());
            af.setUserUpdate(model.getUser());
            af.setUserRecord(model.getUser());
            af.setCreateTimeStamp(nowDate);
            af.setLastUpdateTimeStamp(nowDate);
            af.setRecordTimeStamp(nowDate);

            af.setDevelopCompany(paymentBond.getCompany());
            af.setApplyDate(MyDatetime.getInstance().getCurrentDate());
            af.seteCodeOfDevelopCompany(null == paymentBond.getCompany() ? "" : paymentBond.getCompany().geteCode());
            af.setProject(paymentBond.getProject());
            af.setTheNameOfDevelopCompany(
                null == paymentBond.getCompany() ? "" : paymentBond.getCompany().getTheName());
            af.setTheNameOfProject(
                StringUtils.isBlank(paymentBond.getProjectName()) ? "" : paymentBond.getProjectName());
            af.seteCodeOfProject(null == paymentBond.getProject() ? "" : paymentBond.getProject().geteCode());
            af.setTotalApplyAmount(
                null == paymentBond.getGuaranteedSumAmount() ? 0.00 : paymentBond.getGuaranteedSumAmount());
            af.setApplyState(2);
            af.setApprovalState("已完结");
            af.setApplyType("1");// 保函支付

            // 楼幢信息
            Empj_BuildingInfo empj_BuildingInfo;
            // 楼幢账户信息
            Tgpj_BuildingAccount buildingAccount;
            // 楼幢监管账号
            Empj_BuildingAccountSupervisedForm accountSupervisedForm;
            List<Empj_BuildingAccountSupervised> empj_buildingAccountSuperviseds;

            List<Tgpf_FundAppropriated_AFDtl> dtlList = new ArrayList<>();
            // 用款申请子表信息
            for (Empj_PaymentBondChild child : childList) {
                empj_BuildingInfo = child.getEmpj_BuildingInfo();
                buildingAccount = empj_BuildingInfo.getBuildingAccount();
                accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
                accountSupervisedForm.setTheState(S_TheState.Normal);
                accountSupervisedForm.setBuildingInfoId(empj_BuildingInfo.getTableId());
                empj_buildingAccountSuperviseds =
                    empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao
                        .getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));

                if (empj_buildingAccountSuperviseds.size() > 0) {
                    Tgpf_FundAppropriated_AFDtl dtl = new Tgpf_FundAppropriated_AFDtl();
                    dtl.setTheState(S_TheState.Normal);
                    dtl.setBuilding(child.getEmpj_BuildingInfo());
                    dtl.seteCodeOfBuilding(child.geteCodeFromConstruction());
                    dtl.setMainTable(af);

                    dtl.setBankAccountSupervised(empj_buildingAccountSuperviseds.get(0).getBankAccountSupervised());
                    dtl.setSupervisedBankAccount(
                        empj_buildingAccountSuperviseds.get(0).getBankAccountSupervised().getTheAccount());
                    dtl.setPayoutState(S_PayoutState.NotAppropriated); // 拨付状态 1: 未拨付
                    dtl.setAppliedAmount(
                        null == child.getActualReleaseAmount() ? 0.00 : child.getActualReleaseAmount());
                    dtl.setAllocableAmount(
                        null == buildingAccount.getAllocableAmount() ? 0.00 : buildingAccount.getAllocableAmount());
                    dtl.setEscrowStandard(
                        null == buildingAccount.getEscrowStandard() ? "" : buildingAccount.getEscrowStandard());
                    dtl.setOrgLimitedAmount(
                        null == buildingAccount.getOrgLimitedAmount() ? 0.00 : buildingAccount.getOrgLimitedAmount());
                    dtl.setCurrentFigureProgress(StringUtils.isBlank(buildingAccount.getCurrentFigureProgress()) ? ""
                        : buildingAccount.getCurrentFigureProgress());
                    dtl.setCurrentLimitedRatio(null == buildingAccount.getCurrentLimitedRatio() ? 100.00
                        : buildingAccount.getCurrentLimitedRatio());
                    dtl.setCurrentLimitedAmount(dtl.getOrgLimitedAmount() * dtl.getCurrentLimitedRatio() / 100);
                    dtl.setTotalAccountAmount(null == buildingAccount.getTotalAccountAmount() ? 0.00
                        : buildingAccount.getTotalAccountAmount());
                    dtl.setAppliedPayoutAmount(null == buildingAccount.getAppliedNoPayoutAmount() ? 0.00
                        : buildingAccount.getAppliedNoPayoutAmount());
                    dtl.setCurrentEscrowFund(
                        null == buildingAccount.getCurrentEscrowFund() ? 0.00 : buildingAccount.getCurrentEscrowFund());
                    dtl.setRefundAmount(
                        null == buildingAccount.getRefundAmount() ? 0.00 : buildingAccount.getRefundAmount());
                    dtl.setUserStart(model.getUser());
                    dtl.setCreateTimeStamp(nowDate);
                    dtl.setUserUpdate(model.getUser());
                    dtl.setLastUpdateTimeStamp(nowDate);

                    dtl.setCashLimitedAmount(
                        null == buildingAccount.getCashLimitedAmount() ? 0.00 : buildingAccount.getCashLimitedAmount());
                    dtl.setEffectiveLimitedAmount(null == buildingAccount.getEffectiveLimitedAmount() ? 0.00
                        : buildingAccount.getEffectiveLimitedAmount());
                    // tgpf_FundAppropriated_AFDtlDao.save(dtl);

                    dtlList.add(dtl);
                }
            }

            // 用款申请汇总数据
            List<Tgpf_FundOverallPlanDetail> detailList = new ArrayList<>();
            Tgpf_FundOverallPlanDetail planDetail;
            for (Tgpf_FundAppropriated_AFDtl dtl : dtlList) {

                boolean isSave = true;
                for (Tgpf_FundOverallPlanDetail detail : detailList) {
                    if (dtl.equals(detail.getSupervisedBankAccount())) {
                        isSave = false;
                        detail.setAppliedAmount(detail.getAppliedAmount() + dtl.getAppliedAmount());
                        tgpf_FundOverallPlanDetailDao.update(detail);
                    }
                }

                if (isSave) {
                    planDetail = new Tgpf_FundOverallPlanDetail();
                    planDetail.setTheState(S_TheState.Normal);
                    planDetail.setCreateTimeStamp(nowDate);
                    planDetail.setUserStart(model.getUser());
                    planDetail.setUserUpdate(model.getUser());
                    planDetail.setLastUpdateTimeStamp(nowDate);

                    planDetail.setMainTable(af);
                    planDetail.setTheNameOfProject(
                        StringUtils.isBlank(paymentBond.getProjectName()) ? "" : paymentBond.getProjectName());
                    planDetail.setTheNameOfBankBranch(dtl.getBankAccountSupervised().getTheNameOfBank());
                    planDetail.setBankAccountSupervised(dtl.getBankAccountSupervised());
                    planDetail.setTheNameOfAccount(dtl.getBankAccountSupervised().getTheName());
                    planDetail.setSupervisedBankAccount(dtl.getSupervisedBankAccount());
                    planDetail.setAppliedAmount(dtl.getAppliedAmount());
                    // tgpf_FundOverallPlanDetailDao.save(planDetail);
                    detailList.add(planDetail);
                }

            }
            af.setFundAppropriated_AFDtlList(dtlList);
            af.setFundOverallPlanDetailList(detailList);
            tgpf_FundAppropriated_AFDao.save(af);

        } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            // 判断当前登录用户是否有权限发起审批
            return properties;
        } else {
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
                (Sm_ApprovalProcess_Cfg)properties.get("sm_approvalProcess_cfg");

            // 审批操作
            sm_approvalProcessService.execute(paymentBond, model, sm_approvalProcess_cfg);
            // 审批流程状态-审核中
            paymentBond.setApprovalState(S_ApprovalState.Examining);

        }

        paymentBond.setUserUpdate(user);
        paymentBond.setLastUpdateTimeStamp(nowDate);

        empj_PaymentBondDao.update(paymentBond);

        return properties;
    }

    public Properties printExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据信息！");
        }
        Empj_PaymentBond paymentBond = empj_PaymentBondDao.findById(tableId);
        if (null == paymentBond) {
            return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
        }

        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties loadBuildByProjectIdExecute(Empj_PaymentBondForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Long projectId = model.getProjectId();
        if (null == projectId) {
            return MyBackInfo.fail(properties, "请选择项目信息！");
        }

        Empj_BuildingInfoForm buildModel = new Empj_BuildingInfoForm();
        buildModel.setTheState(S_TheState.Normal);
        buildModel.setApprovalState(S_ApprovalState.Completed);
        buildModel.setProjectId(projectId);
        buildModel.setProjectInfoIdArr(model.getProjectInfoIdArr());
        buildModel.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
        buildModel.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
        List<Empj_BuildingInfo> buildingList = empj_BuildingInfoDao
            .findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), buildModel));

        // 构造返回信息
        List<Properties> list = new ArrayList<Properties>();
        Properties pro;

        Empj_PaymentBondChildForm childModel;
        List<Empj_PaymentBondChild> childList;

        Tgpj_BuildingAccount buildingAccount;
        if (buildingList.size() > 0) {
            for (Empj_BuildingInfo build : buildingList) {
                buildingAccount = build.getBuildingAccount();
                if (null == buildingAccount) {
                    continue;
                }
                
                if(((null == buildingAccount.getAppropriateFrozenAmount() ? 0.00 : buildingAccount.getAppropriateFrozenAmount()) - 0.00 )> 0.00){
                    continue;
                }
                

                pro = new MyProperties();
                pro.put("tableId", build.getTableId());
                pro.put("eCodeFromConstruction",
                    StringUtils.isBlank(build.geteCodeFromConstruction()) ? "" : build.geteCodeFromConstruction());
                pro.put("orgLimitedAmount",
                    null == buildingAccount.getOrgLimitedAmount() ? 0.00 : buildingAccount.getOrgLimitedAmount());
                pro.put("currentFigureProgress", StringUtils.isBlank(buildingAccount.getCurrentFigureProgress()) ? ""
                    : buildingAccount.getCurrentFigureProgress());
                pro.put("currentLimitedRatio",
                    null == buildingAccount.getCurrentLimitedRatio() ? 0.00 : buildingAccount.getCurrentLimitedRatio());
                pro.put("nodeLimitedAmount",
                    null == buildingAccount.getNodeLimitedAmount() ? 0.00 : buildingAccount.getNodeLimitedAmount());
                pro.put("currentEscrowFund",
                    null == buildingAccount.getCurrentEscrowFund() ? 0.00 : buildingAccount.getCurrentEscrowFund());
                pro.put("spilloverAmount",
                    null == buildingAccount.getSpilloverAmount() ? 0.00 : buildingAccount.getSpilloverAmount());
                pro.put("effectiveLimitedAmount", null == buildingAccount.getEffectiveLimitedAmount() ? 0.00
                    : buildingAccount.getEffectiveLimitedAmount());

                // 查询楼幢是否做过支付保函
                /*childModel = new Empj_PaymentBondChildForm();
                childModel.setTheState(S_TheState.Normal);
                childModel.setEmpj_BuildingInfo(build);
                
                childList = empj_PaymentBondChildDao
                    .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQLRecord(), childModel));
                if (!childList.isEmpty()) {
                    pro.put("hasExist","有");
                }else{
                    pro.put("hasExist","无");
                }*/

                if ((null == buildingAccount.getTotalGuaranteeAmount() ? 0.00
                    : buildingAccount.getTotalGuaranteeAmount()) > 0.00) {
                    pro.put("hasExist", "有");
                } else {
                    pro.put("hasExist", "无");
                }

                list.add(pro);

            }
        }

        properties.put("buildingList", list);

        return properties;
    }

    /**
     * 是否存在PDF
     * 
     * @param sourceBusiCode
     *            业务编码
     * @param sourceId
     *            业务数据Id
     * @return
     */
    Sm_Attachment isSaveAttachment(String sourceBusiCode, String sourceId) {
        // 托管终止打印编码
        String attacmentcfg = "240108";

        Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfg(attacmentcfg);

        if (null == sm_AttachmentCfg) {
            return null;
        }

        Sm_AttachmentForm form = new Sm_AttachmentForm();
        form.setSourceId(sourceId);
        form.setBusiType(sourceBusiCode);
        form.setSourceType(sm_AttachmentCfg.geteCode());
        form.setTheState(S_TheState.Normal);

        Sm_Attachment attachment =
            attacmentDao.findOneByQuery_T(attacmentDao.getQuery(attacmentDao.getBasicHQL(), form));

        if (null == attachment) {
            return null;
        }
        return attachment;
    }

    /**
     * 是否进行档案配置
     * 
     * @param attacmentcfg
     *            档案类型编码
     * @return
     */
    Sm_AttachmentCfg isSaveAttachmentCfg(String attacmentcfg) {
        // 根据业务编号查询配置文件
        Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
        form.setTheState(S_TheState.Normal);
        form.setBusiType(attacmentcfg);

        Sm_AttachmentCfg sm_AttachmentCfg =
            attacmentcfgDao.findOneByQuery_T(attacmentcfgDao.getQuery(attacmentcfgDao.getBasicHQL(), form));

        if (null == sm_AttachmentCfg) {
            return null;
        }
        return sm_AttachmentCfg;
    }

}

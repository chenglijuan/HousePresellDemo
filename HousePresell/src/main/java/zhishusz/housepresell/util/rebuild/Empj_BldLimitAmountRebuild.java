package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaominfo.oss.sdk.OSSClientProperty;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：申请表-进度节点变更 Company：ZhiShuSZ
 */
@Service
public class Empj_BldLimitAmountRebuild extends RebuilderBase<Empj_BldLimitAmount> {

    @Autowired
    private OSSClientProperty oss;

    @Autowired
    private Sm_AttachmentCfgRebuild cfgRebuild;

    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;

    private MyDouble myDouble = MyDouble.getInstance();

    @Override
    public Properties getSimpleInfo(Empj_BldLimitAmount object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();
        properties.put("tableId", object.getTableId());

        return properties;
    }

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public Properties getDetail(Empj_BldLimitAmount object) {

        if (object == null)
            return null;
        Properties properties = new MyProperties();
        /*
         * tableId 主键 Long eCode 申请单号 String applyDate 申请日期 String developName
         * 开发企业名称 String theNameOfProject 项目名称 String contactOne 联系人A String
         * contactTwo 联系人B String telephoneOne A联系方式 String telephoneTwo B联系方式
         * String approvalState 审批状态 String userUpdateName 操作人 String updateDate
         * 操作日期 String userRecordName 备案人 String recordDate 备案日期 String
         */
        properties.put("tableId", object.getTableId());
        properties.put("eCode", object.geteCode());
        properties.put("applyDate",
            null == object.getApplyDate() ? "--" : MyDatetime.getInstance().dateToString2(object.getApplyDate()));
        properties.put("developName", object.getDevelopCompany().getTheName());
        properties.put("theNameOfProject", object.getTheNameOfProject());
        properties.put("contactOne", object.getContactOne());
        properties.put("telephoneTwo", object.getTelephoneTwo());
        properties.put("telephoneOne", object.getTelephoneOne());
        properties.put("contactTwo", object.getContactTwo());
        properties.put("approvalState", object.getApprovalState());
        properties.put("userUpdateName", object.getUserUpdate().getTheName());
        properties.put("updateDate", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
        properties.put("userRecordName", null == object.getUserRecord() ? "--" : object.getUserRecord().getTheName());
        properties.put("recordDate", null == object.getRecordTimeStamp() ? "--"
            : MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
        properties.put("appointTimeStamp", null == object.getAppointTimeStamp() ? "--"
            : MyDatetime.getInstance().dateToString(object.getAppointTimeStamp()));

        properties.put("businessCode", object.getBusinessCode());
        properties.put("countUnit", object.getCountUnit());
        properties.put("companyOne", object.getCompanyOneName());
        properties.put("appointmentDateOne", null == object.getAppointmentDateOne() ? null
            : MyDatetime.getInstance().dateToString(object.getAppointmentDateOne(), "yyyy-MM-dd HH:mm"));
        properties.put("companyTwo", object.getCompanyTwoName());
        properties.put("appointmentDateTwo", null == object.getAppointmentDateTwo() ? null
            : MyDatetime.getInstance().dateToString(object.getAppointmentDateTwo(), "yyyy-MM-dd HH:mm"));

        properties.put("uploadOne", null == object.getUploadOne() ? "" : object.getUploadOne());
        properties.put("uploadTwo", null == object.getUploadTwo() ? "" : object.getUploadTwo());
        
        properties.put("returnTimeOne", null == object.getReturnTimeOne() ? "" : object.getReturnTimeOne());
        properties.put("returnTimeTwo", null == object.getReturnTimeTwo() ? "" : object.getReturnTimeTwo());
        properties.put("assignTasksTimeOne", null == object.getAssignTasksTimeOne() ? "" : object.getAssignTasksTimeOne());
        properties.put("assignTasksTimeTwo", null == object.getAssignTasksTimeTwo() ? "" : object.getAssignTasksTimeTwo());
        properties.put("signTimeOne", null == object.getSignTimeOne() ? "" : object.getSignTimeOne());
        properties.put("signTimeTwo", null == object.getSignTimeTwo() ? "" : object.getSignTimeTwo());

        // 查找申请单，给审批情况用的
        Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
        sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
        sm_approvalProcess_afForm.setBusiCode("03030100");
        sm_approvalProcess_afForm.setSourceId(object.getTableId());
        sm_approvalProcess_afForm.setOrderBy("createTimeStamp desc");
        List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(
            sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
        if (null != afList && afList.size() > 0) {
            Sm_ApprovalProcess_AF sm_approvalProcess_af = afList.get(0);
            Sm_ApprovalProcess_Workflow approvalProcess_Workflow;
            if (sm_approvalProcess_af != null) {
                properties.put("afId", sm_approvalProcess_af.getTableId()); // 申请单id
                properties.put("workflowId", sm_approvalProcess_af.getCurrentIndex());// 当前结点Id
                properties.put("busiType", sm_approvalProcess_af.getBusiType()); // 业务类型
                properties.put("busiCode", sm_approvalProcess_af.getBusiCode()); // 业务编码

                approvalProcess_Workflow =
                    sm_ApprovalProcess_WorkflowDao.findById(sm_approvalProcess_af.getCurrentIndex());
                if (null != approvalProcess_Workflow) {
                    properties.put("workflowCode", approvalProcess_Workflow.geteCode());
                } else {
                    properties.put("workflowCode", "");
                }
            }
        } else {
            properties.put("workflowCode", "");
        }

        List<Empj_BldLimitAmount_Dtl> dtlList = object.getDtlList();
        MyProperties pro;
        List<MyProperties> listPro = new ArrayList<>();
        for (Empj_BldLimitAmount_Dtl dtl : dtlList) {
            /*
             * buildingId 楼幢主键 Long eCodeFromConstruction 施工编号 String
             * escrowStandard 托管标准 String deliveryType 交付类型 String 1：毛坯房 2：成品房
             * orgLimitedAmount 初始受限额度 String upfloorNumber 地上层数 Int signingDate
             * 签约时间 String bldLimitAmountName 受限额度版本 String bldLimitAmountId
             * 受限额度版本id Long limitedAmount 形象进度比例 String limitedName 形象进度名称
             * String approvalResult 审批结果 String 0：不通过 1：通过 approvalInfo 审批批语
             * String
             */
            pro = new MyProperties();
            /*
             * 基本信息
             */
            Empj_BuildingInfo building = dtl.getBuilding();
            Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();

            pro.put("tableId", dtl.getTableId());
            pro.put("buildingId", building.getTableId());
            pro.put("eCodeFromConstruction", building.geteCodeFromConstruction());
            pro.put("deliveryType", building.getDeliveryType());
            /*
             * 1.如果是标准金额，则直接取值 2.如果是标准比例且等于30% 根据毛坯房或者成品房的规则计算取值
             */
            if (S_EscrowStandardType.StandardAmount.equals(building.getEscrowStandardVerMng().getTheType())) {
                pro.put("escrowStandard", building.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

            }
            if (S_EscrowStandardType.StandardPercentage.equals(building.getEscrowStandardVerMng().getTheType())) {

                pro.put("escrowStandard", "物价备案均价*" + building.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例

                if (building.getEscrowStandardVerMng().getPercentage() == 30) {
                    
                    Double recordAvgPriceOfBuilding = null == buildingAccount.getRecordAvgPriceOfBuilding() ? 0.00 : buildingAccount.getRecordAvgPriceOfBuilding();
                    
                    double price = recordAvgPriceOfBuilding * 0.3;
                    if ("1".equals(building.getDeliveryType())) {
                        // 毛坯
                        if (price - 4000 > 0) {
                            pro.put("escrowStandard", "4000元/m²");
                        }
                    } else {
                        // 成品
                        if (price - 6000 > 0) {
                            pro.put("escrowStandard", "6000元/m²");
                        }
                    }
                }
            }
            // 初始受限额度
            pro.put("orgLimitedAmount",
                MyDouble.getInstance().pointTOThousandths(buildingAccount.getOrgLimitedAmount()));
            // 地上层数
            pro.put("upfloorNumber", MyDouble.getInstance().getShort(building.getUpfloorNumber(), 0));
            // 签约时间
            String sql =
                "SELECT B.* FROM REL_ESCROWAGREEMENT_BUILDING A LEFT JOIN TGXY_ESCROWAGREEMENT B ON A.TGXY_ESCROWAGREEMENT = B.TABLEID WHERE A.EMPJ_BUILDINGINFO = "
                    + building.getTableId() + " AND B.THESTATE=0 ORDER BY B.CREATETIMESTAMP DESC";
            List<Tgxy_EscrowAgreement> tgxy_EscrowAgreementList = new ArrayList<Tgxy_EscrowAgreement>();
            tgxy_EscrowAgreementList =
                sessionFactory.getCurrentSession().createNativeQuery(sql, Tgxy_EscrowAgreement.class).getResultList();
            if (null == tgxy_EscrowAgreementList || tgxy_EscrowAgreementList.size() < 1) {
                continue;
            }
            pro.put("signingDate", null == tgxy_EscrowAgreementList.get(0).getContractApplicationDate() ? ""
                : tgxy_EscrowAgreementList.get(0).getContractApplicationDate());

            // 受限额度版本
            Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = buildingAccount.getBldLimitAmountVerDtl();
            if (null != bldLimitAmountVerDtl) {
                Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
                pro.put("bldLimitAmountName", bldLimitAmountVer_AF.getTheName());
                pro.put("bldLimitAmountId", bldLimitAmountVer_AF.getTableId());
            }

            // 形象进度
            Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = dtl.getExpectFigureProgress();
            pro.put("limitedAmount", expectFigureProgress.getLimitedAmount());
            pro.put("limitedName", expectFigureProgress.getStageName());

            pro.put("approvalResult", null == dtl.getApprovalResult() ? "1" : dtl.getApprovalResult());
            pro.put("approvalInfo", null == dtl.getApprovalInfo() ? "" : dtl.getApprovalInfo());

            Double currentLimitedRatio =
                null == buildingAccount.getCurrentLimitedRatio() ? 0.00 : buildingAccount.getCurrentLimitedRatio();
            /*String currentFigureProgress =
                null == buildingAccount.getCurrentFigureProgress() ? "" : buildingAccount.getCurrentFigureProgress();*/
            
            String currentFigureProgress = dtl.getCurrentFigureProgress();
            pro.put("currentLimitedRatio",
                null == buildingAccount.getCurrentLimitedRatio() ? 0.00 : buildingAccount.getCurrentLimitedRatio());
            pro.put("nowLimitedAmount", currentFigureProgress);

            pro.put("predictionNodeName",
                StringUtils.isBlank(dtl.getPredictionNodeName()) ? "" : dtl.getPredictionNodeName());
            pro.put("completeDateOne", null == dtl.getCompleteDateOne() ? null
                : MyDatetime.getInstance().dateToSimpleString(dtl.getCompleteDateOne()));
            pro.put("completeDateTwo", null == dtl.getCompleteDateTwo() ? null
                : MyDatetime.getInstance().dateToSimpleString(dtl.getCompleteDateTwo()));
            pro.put("resultOne", null == dtl.getResultOne() ? "1" : dtl.getResultOne());
            pro.put("resultInfoOne", null == dtl.getResultInfoOne() ? "" : dtl.getResultInfoOne());
            pro.put("resultTwo", null == dtl.getResultTwo() ? "1" : dtl.getResultTwo());
            pro.put("resultInfoTwo", null == dtl.getResultInfoTwo() ? "" : dtl.getResultInfoTwo());

            if ("JDJZ".equals(properties.get("workflowCode"))) {
                pro.put("resultInfoOne", null == dtl.getApprovalInfo() ? "" : dtl.getApprovalInfo());
                pro.put("resultInfoTwo", null == dtl.getApprovalInfo() ? "" : dtl.getApprovalInfo());
            }

            /*
             * 附件信息
             */
            List detailForAdmin2 = cfgRebuild.getDetailForAdmin3(dtl.getSmAttachmentCfgList());

            pro.put("attachementList", detailForAdmin2);

            listPro.add(pro);
        }

        properties.put("dtlList", listPro);

        return properties;
    }

    /**
     * 列表加载
     * 
     * @param empj_BldLimitAmountList
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List empj_BldLimitAmountList(List<Empj_BldLimitAmount> empj_BldLimitAmountList) {

        List<Properties> list = new ArrayList<Properties>();
        if (empj_BldLimitAmountList != null) {
            for (Empj_BldLimitAmount object : empj_BldLimitAmountList) {
                if (object == null)
                    continue;

                Properties properties = new MyProperties();

                /*
                 * tableId 主键 Long eCode 申请单号 String applyDate 申请日期 String
                 * developName 开发企业名称 String theNameOfProject 项目名称 String
                 * approvalState 审批状态 String
                 */

                properties.put("tableId", object.getTableId());
                properties.put("eCode", object.geteCode());
                properties.put("applyDate", null == object.getApplyDate() ? "-"
                    : MyDatetime.getInstance().dateToString2(object.getApplyDate()));
                properties.put("developName", object.getDevelopCompany().getTheName());
                properties.put("theNameOfProject", object.getTheNameOfProject());
                properties.put("approvalState", object.getApprovalState());

                properties.put("developCompanyId", object.getDevelopCompany().getTableId());
                properties.put("projectId", object.getProject().getTableId());

                // 查找申请单，给审批情况用的
                Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
                sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
                sm_approvalProcess_afForm.setBusiCode("03030100");
                sm_approvalProcess_afForm.setSourceId(object.getTableId());
                sm_approvalProcess_afForm.setOrderBy("createTimeStamp desc ");
                List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao
                    .getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
                if (null != afList && afList.size() > 0) {
                    Sm_ApprovalProcess_AF sm_approvalProcess_af = afList.get(0);
                    if (sm_approvalProcess_af != null) {
                        properties.put("afId", sm_approvalProcess_af.getTableId()); // 申请单id
                        properties.put("workflowId", sm_approvalProcess_af.getCurrentIndex());// 当前结点Id
                        properties.put("busiType", sm_approvalProcess_af.getBusiType()); // 业务类型
                        properties.put("busiCode", sm_approvalProcess_af.getBusiCode()); // 业务编码
                    }
                }

                list.add(properties);
            }
        }
        return list;
    }

}

package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_HouseBusiState;
import zhishusz.housepresell.database.po.state.S_PayoutState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Empj_BldAccountGetLimitAmountVerService;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.excel.model.Empj_BuildingTableTemplate;

/*
 * Rebuilder：楼幢-基础信息 Company：ZhiShuSZ
 */
@Service
public class Empj_BuildingInfoRebuild extends RebuilderBase<Empj_BuildingInfo> {
    @Autowired
    private Empj_HouseInfoDao empj_HouseInfoDao;
    @Autowired
    private Empj_UnitInfoDao empj_UnitInfoDao;
    @Autowired
    private Empj_HouseInfoRebuild empj_HouseInfoRebuild;
    @Autowired
    private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
    @Autowired
    private Empj_BuildingAccountSupervisedDao empj_buildingAccountSupervisedDao;
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    @Autowired
    private Gson gson;
    @Autowired
    private Sm_BaseParameterGetService sm_BaseParameterGetService;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;// 受限额度版本

    @Autowired
    private Empj_BldAccountGetLimitAmountVerService bldAccountGetLimitAmountVerService;

    @Autowired
    private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;// 托管合作协议
    @Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;

    private MyDouble myDouble = MyDouble.getInstance();

    @Override
    public Properties getSimpleInfo(Empj_BuildingInfo object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
        properties.put("busiState", object.getBusiState()); // 状态
        properties.put("approvalState", object.getApprovalState());// 流程状态
        properties.put("eCode", object.geteCode()); // 楼幢编号
        if (object.getDevelopCompany() != null) {
            properties.put("developCompanyId", object.getDevelopCompany().getTableId());
            properties.put("developCompanyName", object.getDevelopCompany().getTheName());
        }
        if (object.getProject() != null) {
            properties.put("projectId", object.getProject().getTableId());
            properties.put("theNameOfProject", object.getProject().getTheName());
        }

        MyDouble muDouble = MyDouble.getInstance();
        properties.put("theNameFromPresellSystem", object.getTheNameFromPresellSystem());// 预售项目名称
        properties.put("buildingArea", muDouble.getShort(object.getBuildingArea(), 2)); // 建筑面积
        properties.put("escrowArea", muDouble.getShort(object.getEscrowArea(), 2)); // 托管面积
        properties.put("deliveryType", object.getDeliveryType()); // 交付类型
        Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
        if (sm_BaseParameter != null) {
            properties.put("deliveryTypeName", sm_BaseParameter.getTheName());
        }

        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
        /*
         * xsz by time 2018-8-28 15:03:11 添加显示字段： eCodeFromPublicSecurity：公安编号
         * eCodeOfLand：幢编号（国土） eCodeFromPresellSystem：预售系统楼幢编号
         */
        properties.put("theName", object.geteCodeFromConstruction()); // 用于工程进度巡查预测中施工编号的筛选
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity()); // 公安编号
        properties.put("eCodeOfLand", object.geteCodeOfLand());
        properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
        properties.put("position", object.getPosition());
        properties.put("escrowStandard", object.getEscrowStandard()); // 托管标准
        try {
            Tgpj_EscrowStandardVerMng escrowStandardVerMng = object.getEscrowStandardVerMng();
            if (escrowStandardVerMng != null) {
                if (S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType())) {
                    Double amount = escrowStandardVerMng.getAmount();
                    if (amount == null) {
                        amount = 0.0;
                    }
                    properties.put("newEscrowStandard", MyDouble.pointTOThousandths(amount) + "（元/m²）");
                } else {
                    Double percentage = escrowStandardVerMng.getPercentage();
                    if (percentage == null) {
                        percentage = 0.0;
                    }
                    properties.put("newEscrowStandard",
                        "物价备案均价*" + MyString.getInstance().parse(percentage.intValue()) + "%");
                }
            }

            Tgpj_BuildingAccount buildingAccount = object.getBuildingAccount(); // 关联楼幢账户
            if (buildingAccount != null) {
                properties.put("currentEscrowFund",
                    MyDouble.pointTOThousandths(buildingAccount.getCurrentEscrowFund())); // 托管余额
                properties.put("allocableAmount", MyDouble.pointTOThousandths(buildingAccount.getAllocableAmount())); // 可划拨金额
                properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress()); // 当前形象进度
                properties.put("effectiveLimitedAmount",
                    MyDouble.pointTOThousandths(buildingAccount.getEffectiveLimitedAmount())); // 当前受限额度/有效受限额度
                properties.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio()); // 当前受限比例
                properties.put("recordAvgPriceOfBuilding",
                    muDouble.getShort(buildingAccount.getRecordAvgPriceOfBuilding(), 2));
                Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl = buildingAccount.getBldLimitAmountVerDtl();
                if (bldLimitAmountVer_afDtl != null) {
                    properties.put("currentBldLimitAmountVerAfDtlId", bldLimitAmountVer_afDtl.getTableId()); // 受限额度节点ID
                    properties.put("newCurrentFigureProgress", bldLimitAmountVer_afDtl.getStageName()); // 当前形象进度
                    properties.put("newCurrentLimitedRatio", bldLimitAmountVer_afDtl.getLimitedAmount()); // 当前受限比例
                }
            }

            // 楼幢扩展信息
            if (object.getExtendInfo() != null) {
                properties.put("escrowState", object.getExtendInfo().getEscrowState()); // 托管状态
                properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor()); // 土地抵押权人
                properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState()); // 土地抵押状态
            }

        } catch (Exception e) {
            Logger.getLogger(Empj_BuildingInfoRebuild.class).info("楼幢信息：" + object.getTableId().toString());
        }

        properties.put("purpose", object.getPurpose()); // 房屋用途
        properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
        properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数
        properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());// 预售证号

        properties.put("theNameOfCityRegion", object.getCityRegion().getTheName());

        return properties;
    }

    @Override
    public Properties getDetail(Empj_BuildingInfo object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
        if (object.getDevelopCompany() != null) {
            properties.put("developCompanyName", object.getDevelopCompany().getTheName());
        }
        if (object.getProject() != null) {
            properties.put("empj_ProjectInfoName", object.getProject().getTheName());
        }
        properties.put("eCode", object.geteCode());// 楼幢编号
        properties.put("theNameFromFinancialAccounting", object.getTheNameFromFinancialAccounting());
        if (object.getExtendInfo() != null)// 扩展信息
        {
            // properties.put("isSupportPGS",
            // object.getExtendInfo().getIsSupportPGS());
            properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor());
            properties.put("landMortgageAmount", object.getExtendInfo().getLandMortgageAmount());
            properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState());// 土地抵押状态
            properties.put("escrowState", object.getExtendInfo().getEscrowState());// 托管状态
        }
        if (object.getUserStart() != null) {
            properties.put("sm_UserStartName", object.getUserStart().getRealName());
        }
        if (object.getUserRecord() != null) {
            properties.put("sm_UserRecordName", object.getUserRecord().getRealName());
        }
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号
        properties.put("eCodeOfProjectFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());// 预售项目编号
        properties.put("theNameFromPresellSystem", object.getTheNameFromPresellSystem());// 预售项目名称
        properties.put("busiState", object.getBusiState());
        properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
        properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());// 公安编号
        properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
        properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());// 预售证号
        properties.put("buildingArea", object.getBuildingArea());
        properties.put("escrowArea", object.getEscrowArea());
        properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
        properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数
        properties.put("theType", object.getTheType());
        properties.put("remark", object.getRemark());
        properties.put("escrowStandard", object.getEscrowStandard()); // 托管标准
        if (object.getEscrowStandardVerMng() != null) {
            properties.put("tgpj_EscrowStandardVerMngId", object.getEscrowStandardVerMng().getTableId());// 托管标准ID
        } else {
            properties.put("tgpj_EscrowStandardVerMngId", "");
        }
        properties.put("deliveryType", object.getDeliveryType());// 交付类型

        Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());

        if (sm_BaseParameter != null) {
            properties.put("sm_BaseParameter", sm_BaseParameter);
        }

        if (object.getBuildingAccount() != null) {
            properties.put("buildAmountPaid", object.getBuildingAccount().getBuildAmountPaid());// 楼幢项目建设已实际支付金额（元）
            properties.put("totalAmountGuaranteed", object.getBuildingAccount().getTotalAmountGuaranteed());// 已落实支付保证累计金额（元）
            properties.put("buildAmountPay", object.getBuildingAccount().getBuildAmountPay());// 楼幢项目建设待支付承保累计金额（元）
            properties.put("paymentLines", object.getBuildingAccount().getPaymentLines());// 支付保证封顶百分比
            // 已落实支付保证累计金额（元）> 0 说明该楼幢已经支付保证
            if (null != object.getBuildingAccount().getTotalAmountGuaranteed()
                && object.getBuildingAccount().getTotalAmountGuaranteed() > 0) {
                properties.put("isSupportPGS", "是"); // 是否支付保证业务楼幢
            } else {
                properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
            }
        } else {
            properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
        }

        properties.put("approvalState", object.getApprovalState());// 流程状态

        // 查待提交的申请单
        // Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new
        // Sm_ApprovalProcess_AFForm();
        // sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
        // sm_ApprovalProcess_AFForm.setBusiState("待提交");
        // sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
        // Sm_ApprovalProcess_AF sm_ApprovalProcess_AF =
        // sm_ApprovalProcess_AFDao.findOneByQuery_T(
        // sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(),
        // sm_ApprovalProcess_AFForm));
        // if (sm_ApprovalProcess_AF != null)
        // {
        // String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();
        // if (jsonNewStr != null && jsonNewStr.length() > 0)
        // {
        // Empj_BuildingInfoForm empj_BuildingInfoForm =
        // gson.fromJson(jsonNewStr, Empj_BuildingInfoForm.class);
        // properties.put("expectBuilding", getDetailForOld(object));// 修改前的对象
        //
        // properties.put("buildingArea",
        // empj_BuildingInfoForm.getBuildingArea());
        // properties.put("escrowArea", empj_BuildingInfoForm.getEscrowArea());
        // properties.put("deliveryType",
        // empj_BuildingInfoForm.getDeliveryType());// 交付类型
        // properties.put("upfloorNumber",
        // MyDouble.getInstance().getShort(empj_BuildingInfoForm.getUpfloorNumber(),
        // 0)); // 地上层数
        // properties.put("downfloorNumber",
        // MyDouble.getInstance().getShort(empj_BuildingInfoForm.getDownfloorNumber(),
        // 0)); // 地下层数
        //
        // if (object.getExtendInfo() != null)// 扩展信息
        // {
        // properties.put("landMortgagor",
        // empj_BuildingInfoForm.getLandMortgagor());
        // properties.put("landMortgageAmount",
        // empj_BuildingInfoForm.getLandMortgageAmount());
        // properties.put("landMortgageState",
        // empj_BuildingInfoForm.getLandMortgageState());// 土地抵押状态
        // }
        // }
        // }
        // else
        // {
        // sm_ApprovalProcess_AFForm.setBusiState("审核中");
        // sm_ApprovalProcess_AF =
        // sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao
        // .getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(),
        // sm_ApprovalProcess_AFForm));
        // if (sm_ApprovalProcess_AF != null)
        // {
        // String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();
        // if (jsonNewStr != null && jsonNewStr.length() > 0)
        // {
        // Empj_BuildingInfoForm empj_BuildingInfoForm =
        // gson.fromJson(jsonNewStr,
        // Empj_BuildingInfoForm.class);
        // properties.put("expectBuilding", getDetailForOld(object));// 修改前的对象
        //
        // properties.put("buildingArea",
        // empj_BuildingInfoForm.getBuildingArea());
        // properties.put("escrowArea", empj_BuildingInfoForm.getEscrowArea());
        // properties.put("deliveryType",
        // empj_BuildingInfoForm.getDeliveryType());// 交付类型
        // properties.put("upfloorNumber",
        // MyDouble.getInstance().getShort(empj_BuildingInfoForm.getUpfloorNumber(),
        // 0)); // 地上层数
        // properties.put("downfloorNumber",
        // MyDouble.getInstance().getShort(empj_BuildingInfoForm.getDownfloorNumber(),
        // 0)); // 地下层数
        //
        // if (object.getExtendInfo() != null)// 扩展信息
        // {
        // properties.put("landMortgagor",
        // empj_BuildingInfoForm.getLandMortgagor());
        // properties.put("landMortgageAmount",
        // empj_BuildingInfoForm.getLandMortgageAmount());
        // properties.put("landMortgageState",
        // empj_BuildingInfoForm.getLandMortgageState());// 土地抵押状态
        // }
        // }
        // }
        // }

        return properties;
    }

    public Properties getDetailForApprovalProcess(Empj_BuildingInfo object, BaseForm model) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
        if (object.getDevelopCompany() != null) {
            properties.put("developCompanyName", object.getDevelopCompany().getTheName());
        }
        if (object.getProject() != null) {
            properties.put("empj_ProjectInfoName", object.getProject().getTheName());
        }
        properties.put("eCode", object.geteCode());// 楼幢编号
        properties.put("theNameFromFinancialAccounting", object.getTheNameFromFinancialAccounting());
        if (object.getExtendInfo() != null)// 扩展信息
        {
            // properties.put("isSupportPGS",
            // object.getExtendInfo().getIsSupportPGS());
            properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor());
            properties.put("landMortgageAmount", object.getExtendInfo().getLandMortgageAmount());
            properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState());// 土地抵押状态
            properties.put("escrowState", object.getExtendInfo().getEscrowState());// 托管状态
        }
        if (object.getUserStart() != null) {
            properties.put("sm_UserStartName", object.getUserStart().getTheName());
        }
        if (object.getUserRecord() != null) {
            properties.put("sm_UserRecordName", object.getUserRecord().getTheName());
        }
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号
        properties.put("eCodeOfProjectFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());// 预售项目编号
        properties.put("theNameFromPresellSystem", object.getTheNameFromPresellSystem());// 预售项目名称
        properties.put("busiState", object.getBusiState());
        properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
        properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());// 公安编号
        // properties.put("theNameFromPresellSystem",
        // object.geteCodeOfProjectFromPresellSystem());
        properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
        properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());// 预售证号
        properties.put("buildingArea", object.getBuildingArea());
        properties.put("escrowArea", object.getEscrowArea());
        properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
        properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数
        properties.put("theType", object.getTheType());
        properties.put("remark", object.getRemark());
        if (object.getEscrowStandardVerMng() != null) {
            properties.put("tgpj_EscrowStandardVerMngId", object.getEscrowStandardVerMng().getTableId());// 托管标准ID
            if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

            }
            if (S_EscrowStandardType.StandardPercentage.equals(object.getEscrowStandardVerMng().getTheType())) {

                properties.put("escrowStandard", "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例
            }
        } else {
            properties.put("tgpj_EscrowStandardVerMngId", "");
        }
        properties.put("deliveryType", object.getDeliveryType());// 交付类型

        Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
        if (sm_BaseParameter != null) {
            properties.put("sm_BaseParameter", sm_BaseParameter);
            properties.put("parameterName", sm_BaseParameter.getTheName());
            properties.put("deliveryTypeName", sm_BaseParameter.getTheName());
        }

        if (object.getBuildingAccount() != null) {
            properties.put("buildAmountPaid", object.getBuildingAccount().getBuildAmountPaid());// 楼幢项目建设已实际支付金额（元）
            properties.put("totalAmountGuaranteed", object.getBuildingAccount().getTotalAmountGuaranteed());// 已落实支付保证累计金额（元）
            properties.put("buildAmountPay", object.getBuildingAccount().getBuildAmountPay());// 楼幢项目建设待支付承保累计金额（元）
            properties.put("paymentLines", object.getBuildingAccount().getPaymentLines());// 支付保证封顶百分比

            String paymentLinesPercent = "";
            if (object.getBuildingAccount().getPaymentLines() != null) {
                paymentLinesPercent = object.getBuildingAccount().getPaymentLines() + "%";
            }
            properties.put("paymentLinesPercent", paymentLinesPercent);// 支付保证封顶百分比

            // 已落实支付保证累计金额（元）> 0 说明该楼幢已经支付保证
            if (null != object.getBuildingAccount().getTotalAmountGuaranteed()
                && object.getBuildingAccount().getTotalAmountGuaranteed() > 0) {
                properties.put("isSupportPGS", "是"); // 是否支付保证业务楼幢
            } else {
                properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
            }

        } else {
            properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
        }

        properties.put("approvalState", object.getApprovalState());// 流程状态

        // if(!"详情".equals(model.getReqSource()) &&
        // S_BusiState.HaveRecord.equals(object.getBusiState()))
        // {
        // //查待提交的申请单
        // Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new
        // Sm_ApprovalProcess_AFForm();
        // sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
        // sm_ApprovalProcess_AFForm.setBusiState("待提交");
        // sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
        // if(S_BusiState.HaveRecord.equals(object.getBusiState()))
        // {
        // sm_ApprovalProcess_AFForm.setBusiCode("03010202");
        // }
        // if(S_BusiState.NoRecord.equals(object.getBusiState()))
        // {
        // sm_ApprovalProcess_AFForm.setBusiCode("03010201");
        // }
        // Sm_ApprovalProcess_AF sm_ApprovalProcess_AF =
        // sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(),
        // sm_ApprovalProcess_AFForm));
        //
        // if(sm_ApprovalProcess_AF == null)
        // {
        // sm_ApprovalProcess_AFForm.setBusiState("审核中");
        // sm_ApprovalProcess_AF =
        // sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(),
        // sm_ApprovalProcess_AFForm));
        //
        // if(sm_ApprovalProcess_AF == null)
        // {
        // return properties;
        // }
        // }
        //
        // Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
        // Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow =
        // sm_approvalProcess_workflowDao.findById(currentNode);
        // Boolean isNeedBackup = null;
        //
        // if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
        // {
        // if(S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup()))
        // {
        // isNeedBackup = true;
        // }
        // }
        // else
        // {
        // isNeedBackup = false;
        // }
        //
        // properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮
        //
        // String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();
        // if(jsonNewStr != null && jsonNewStr.length() > 0)
        // {
        // Empj_BuildingInfoForm empj_BuildingInfoForm =
        // gson.fromJson(jsonNewStr, Empj_BuildingInfoForm.class);
        //
        // properties.put("expectBuilding", getDetailForOld(object));//修改前的对象
        //
        // properties.put("buildingArea",
        // empj_BuildingInfoForm.getBuildingArea());
        // properties.put("escrowArea", empj_BuildingInfoForm.getEscrowArea());
        // properties.put("deliveryType",
        // empj_BuildingInfoForm.getDeliveryType());//交付类型
        // properties.put("upfloorNumber",
        // MyDouble.getInstance().getShort(empj_BuildingInfoForm.getUpfloorNumber(),
        // 0)); //地上层数
        // properties.put("downfloorNumber",
        // MyDouble.getInstance().getShort(empj_BuildingInfoForm.getDownfloorNumber(),
        // 0)); //地下层数
        //
        // if(object.getExtendInfo() != null)//扩展信息
        // {
        // properties.put("landMortgagor",
        // empj_BuildingInfoForm.getLandMortgagor());
        // properties.put("landMortgageAmount",
        // empj_BuildingInfoForm.getLandMortgageAmount());
        // properties.put("landMortgageState",
        // empj_BuildingInfoForm.getLandMortgageState());//土地抵押状态
        // }
        // }
        // }

        return properties;
    }

    public Properties getDetailForAf(Empj_BuildingInfo object, BaseForm model) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
        if (object.getDevelopCompany() != null) {
            properties.put("developCompanyName", object.getDevelopCompany().getTheName());
        }
        if (object.getProject() != null) {
            properties.put("empj_ProjectInfoName", object.getProject().getTheName());
        }
        properties.put("eCode", object.geteCode());// 楼幢编号
        properties.put("theNameFromFinancialAccounting", object.getTheNameFromFinancialAccounting());
        if (object.getExtendInfo() != null)// 扩展信息
        {
            // properties.put("isSupportPGS",
            // object.getExtendInfo().getIsSupportPGS());
            properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor());
            properties.put("landMortgageAmount", object.getExtendInfo().getLandMortgageAmount());
            properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState());// 土地抵押状态
            properties.put("escrowState", object.getExtendInfo().getEscrowState());// 托管状态
        }
        if (object.getUserStart() != null) {
            properties.put("sm_UserStartName", object.getUserStart().getTheName());
        }
        if (object.getUserRecord() != null) {
            properties.put("sm_UserRecordName", object.getUserRecord().getTheName());
        }
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号
        properties.put("eCodeOfProjectFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());// 预售项目编号
        properties.put("theNameFromPresellSystem", object.getTheNameFromPresellSystem());// 预售项目名称
        properties.put("busiState", object.getBusiState());
        properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
        properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());// 公安编号
        properties.put("theNameFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());
        properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
        properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());// 预售证号
        properties.put("buildingArea", object.getBuildingArea());
        properties.put("escrowArea", object.getEscrowArea());
        properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
        properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数
        properties.put("theType", object.getTheType());
        properties.put("remark", object.getRemark());
        if (object.getEscrowStandardVerMng() != null) {
            properties.put("tgpj_EscrowStandardVerMngId", object.getEscrowStandardVerMng().getTableId());// 托管标准ID
            if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

            }
            if (S_EscrowStandardType.StandardPercentage.equals(object.getEscrowStandardVerMng().getTheType())) {

                properties.put("escrowStandard", "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例
            }
        } else {
            properties.put("tgpj_EscrowStandardVerMngId", "");
        }
        properties.put("deliveryType", object.getDeliveryType());// 交付类型

        Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
        if (sm_BaseParameter != null) {
            properties.put("sm_BaseParameter", sm_BaseParameter);
            properties.put("parameterName", sm_BaseParameter.getTheName());
            properties.put("deliveryTypeName", sm_BaseParameter.getTheName());
        }

        if (object.getBuildingAccount() != null) {
            properties.put("buildAmountPaid", object.getBuildingAccount().getBuildAmountPaid());// 楼幢项目建设已实际支付金额（元）
            properties.put("totalAmountGuaranteed", object.getBuildingAccount().getTotalAmountGuaranteed());// 已落实支付保证累计金额（元）
            properties.put("buildAmountPay", object.getBuildingAccount().getBuildAmountPay());// 楼幢项目建设待支付承保累计金额（元）
            properties.put("paymentLines", object.getBuildingAccount().getPaymentLines());// 支付保证封顶百分比

            String paymentLinesPercent = "";
            if (object.getBuildingAccount().getPaymentLines() != null) {
                paymentLinesPercent = object.getBuildingAccount().getPaymentLines() + "%";
            }
            properties.put("paymentLinesPercent", paymentLinesPercent);// 支付保证封顶百分比

            // 已落实支付保证累计金额（元）> 0 说明该楼幢已经支付保证
            if (null != object.getBuildingAccount().getTotalAmountGuaranteed()
                && object.getBuildingAccount().getTotalAmountGuaranteed() > 0) {
                properties.put("isSupportPGS", "是"); // 是否支付保证业务楼幢
            } else {
                properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
            }

        } else {
            properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
        }

        properties.put("approvalState", object.getApprovalState());// 流程状态

        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findById(model.getAfId());
        if (sm_ApprovalProcess_AF != null) {
            String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();// 该申请单OSS上的数据
            if (jsonNewStr != null && jsonNewStr.length() > 0) {
                Empj_BuildingInfoForm empj_BuildingInfoForm = gson.fromJson(jsonNewStr, Empj_BuildingInfoForm.class);

                // S_BusiCode.busiCode_03010201.equals(sm_ApprovalProcess_AF.getBusiCode())//表示新增
                // S_BusiCode.busiCode3.equals(sm_ApprovalProcess_AF.getBusiCode())//表示变更
                //
                // S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState())//待提交
                // S_ApprovalState.Examining.equals(sm_ApprovalProcess_AF.getBusiState())//审核中
                // S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())//已完结
                // S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState())//不通过

                // 同时显示当前po和申请单的值
                if (S_BusiCode.busiCode3.equals(sm_ApprovalProcess_AF.getBusiCode())
                    && (S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState())
                        || S_ApprovalState.Examining.equals(sm_ApprovalProcess_AF.getBusiState()))) {
                    properties.put("expectBuilding", getDetailForOld(object));// 修改前的对象

                    properties.put("buildingArea", empj_BuildingInfoForm.getBuildingArea());
                    properties.put("escrowArea", empj_BuildingInfoForm.getEscrowArea());
                    properties.put("deliveryType", empj_BuildingInfoForm.getDeliveryType());// 交付类型
                    properties.put("upfloorNumber",
                        MyDouble.getInstance().getShort(empj_BuildingInfoForm.getUpfloorNumber(), 0)); // 地上层数
                    properties.put("downfloorNumber",
                        MyDouble.getInstance().getShort(empj_BuildingInfoForm.getDownfloorNumber(), 0)); // 地下层数

                    if (object.getExtendInfo() != null)// 扩展信息
                    {
                        properties.put("landMortgagor", empj_BuildingInfoForm.getLandMortgagor());
                        properties.put("landMortgageAmount", empj_BuildingInfoForm.getLandMortgageAmount());
                        properties.put("landMortgageState", empj_BuildingInfoForm.getLandMortgageState());// 土地抵押状态
                    }
                }

                if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
                    || S_ApprovalState.NoPass.equals(sm_ApprovalProcess_AF.getBusiState())) {
                    properties.put("buildingArea", empj_BuildingInfoForm.getBuildingArea());
                    properties.put("escrowArea", empj_BuildingInfoForm.getEscrowArea());
                    properties.put("deliveryType", empj_BuildingInfoForm.getDeliveryType());// 交付类型
                    properties.put("upfloorNumber",
                        MyDouble.getInstance().getShort(empj_BuildingInfoForm.getUpfloorNumber(), 0)); // 地上层数
                    properties.put("downfloorNumber",
                        MyDouble.getInstance().getShort(empj_BuildingInfoForm.getDownfloorNumber(), 0)); // 地下层数

                    if (object.getExtendInfo() != null)// 扩展信息
                    {
                        properties.put("landMortgagor", empj_BuildingInfoForm.getLandMortgagor());
                        properties.put("landMortgageAmount", empj_BuildingInfoForm.getLandMortgageAmount());
                        properties.put("landMortgageState", empj_BuildingInfoForm.getLandMortgageState());// 土地抵押状态
                    }
                }

                // if(S_BusiCode.busiCode_03010201.equals(sm_ApprovalProcess_AF.getBusiCode())
                // &&
                // (S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState())
                // ||
                // S_ApprovalState.Examining.equals(sm_ApprovalProcess_AF.getBusiState())))
                // {
                //
                // }
            }
        }

        return properties;
    }

    public Properties getDetailForOld(Empj_BuildingInfo object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢

        if (object.getExtendInfo() != null)// 扩展信息
        {
            properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor());
            properties.put("landMortgageAmount", object.getExtendInfo().getLandMortgageAmount());
            properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState());// 土地抵押状态
        }
        properties.put("buildingArea", object.getBuildingArea());
        properties.put("escrowArea", object.getEscrowArea());
        properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
        properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数
        properties.put("deliveryType", object.getDeliveryType());// 交付类型

        Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
        if (sm_BaseParameter != null) {
            properties.put("sm_BaseParameter", sm_BaseParameter);
            properties.put("parameterName", sm_BaseParameter.getTheName());
        }

        return properties;
    }

    @SuppressWarnings("rawtypes")
    public List getDetailForAdmin(List<Empj_BuildingInfo> empj_BuildingInfoList) {
        List<Properties> list = new ArrayList<Properties>();
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {
                Properties properties = new MyProperties();

                properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
                properties.put("theState", object.getTheState());
                properties.put("busiState", object.getBusiState());
                properties.put("eCode", object.geteCode());
                properties.put("userStart", object.getUserStart());
                properties.put("userStartId", object.getUserStart().getTableId());
                properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
                properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
                properties.put("userRecord", object.getUserRecord());
                properties.put("userRecordId", object.getUserRecord().getTableId());
                properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
                properties.put("project", object.getProject());
                properties.put("projectId", object.getProject().getTableId());
                properties.put("theNameOfProject", object.getTheNameOfProject());
                properties.put("eCodeOfProject", object.geteCodeOfProject());
                properties.put("extendInfo", object.getExtendInfo());
                properties.put("extendInfoId", object.getExtendInfo().getTableId());
                properties.put("theNameFromPresellSystem", object.getTheNameFromPresellSystem());
                properties.put("eCodeOfProjectFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());
                properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
                properties.put("theNameFromFinancialAccounting", object.getTheNameFromFinancialAccounting());
                properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());
                properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
                properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
                // properties.put("bankAccountSupervisedList",
                // object.getBankAccountSupervisedList());
                properties.put("eCodeOfProjectPartition", object.geteCodeOfProjectPartition());
                properties.put("zoneCode", object.getZoneCode());
                properties.put("cityRegion", object.getCityRegion());
                properties.put("cityRegionId", object.getCityRegion().getTableId());
                properties.put("theNameOfCityRegion", object.getTheNameOfCityRegion());
                properties.put("streetInfo", object.getStreetInfo());
                properties.put("streetInfoId", object.getStreetInfo().getTableId());
                properties.put("theNameOfStreet", object.getTheNameOfStreet());
                properties.put("eCodeOfGround", object.geteCodeOfGround());
                properties.put("eCodeOfLand", object.geteCodeOfLand());
                properties.put("position", object.getPosition());
                properties.put("purpose", object.getPurpose());
                properties.put("structureProperty", object.getStructureProperty());
                properties.put("theType", object.getTheType());
                properties.put("theProperty", object.getTheProperty());
                properties.put("decorationType", object.getDecorationType());
                properties.put("combType", object.getCombType());
                properties.put("floorNumer", object.getFloorNumer());
                properties.put("upfloorNumber", object.getUpfloorNumber());
                properties.put("downfloorNumber", object.getDownfloorNumber());
                properties.put("heigh", object.getHeigh());
                properties.put("unitNumber", object.getUnitNumber());
                properties.put("sumFamilyNumber", object.getSumFamilyNumber());
                properties.put("buildingArea", object.getBuildingArea());
                properties.put("occupyArea", object.getOccupyArea());
                properties.put("shareArea", object.getShareArea());
                properties.put("beginDate", object.getBeginDate());
                properties.put("endDate", object.getEndDate());
                properties.put("deliveryDate", object.getDeliveryDate());
                properties.put("deliveryType", object.getDeliveryType());
                properties.put("warrantyDate", object.getWarrantyDate());
                properties.put("geoCoordinate", object.getGeoCoordinate());
                properties.put("eCodeOfGis", object.geteCodeOfGis());
                properties.put("eCodeOfMapping", object.geteCodeOfMapping());
                properties.put("eCodeOfPicture", object.geteCodeOfPicture());
                properties.put("buildingFacilities", object.getBuildingFacilities());
                properties.put("buildingArround", object.getBuildingArround());
                properties.put("introduction", object.getIntroduction());
                properties.put("developCompany", object.getDevelopCompany());
                properties.put("developCompanyId", object.getDevelopCompany().getTableId());
                properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
                properties.put("busCompany", object.getBusCompany());
                properties.put("busCompanyId", object.getBusCompany().getTableId());
                properties.put("eCodeOfBusCompany", object.geteCodeOfBusCompany());
                properties.put("ownerCommittee", object.getOwnerCommittee());
                properties.put("ownerCommitteeId", object.getOwnerCommittee().getTableId());
                properties.put("eCodeOfOwnerCommittee", object.geteCodeOfOwnerCommittee());
                properties.put("mappingUnit", object.getMappingUnit());
                properties.put("mappingUnitId", object.getMappingUnit().getTableId());
                properties.put("eCodeOfMappingUnit", object.geteCodeOfMappingUnit());
                properties.put("saleAgencyUnit", object.getSaleAgencyUnit());
                properties.put("saleAgencyUnitId", object.getSaleAgencyUnit().getTableId());
                properties.put("eCodeOfS", object.geteCodeOfS());
                properties.put("consUnit", object.getConsUnit());
                properties.put("consUnitId", object.getConsUnit().getTableId());
                properties.put("eCodeOfConsUnit", object.geteCodeOfConsUnit());
                properties.put("controlUnit", object.getControlUnit());
                properties.put("controlUnitId", object.getControlUnit().getTableId());
                properties.put("eCodeOfControlUnit", object.geteCodeOfControlUnit());
                properties.put("escrowArea", object.getEscrowArea());
                properties.put("escrowStandard", object.getEscrowStandard());
                properties.put("remark", object.getRemark());

                list.add(properties);
            }
        }
        return list;
    }

    Empj_PaymentBondChildForm childModel;
    List<Empj_PaymentBondChild> childList;

    // 用款申请 - 获取楼幢信息
    @SuppressWarnings({"rawtypes", "unchecked", "static-access"})
    public List fundAppropriatedAF_BuildingInfoList(List<Empj_BuildingInfo> empj_BuildingInfoList) {
        List<Properties> list = new ArrayList<Properties>();
        Integer count;
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {

                boolean flag = true;

                // 支付保函
                childModel = new Empj_PaymentBondChildForm();
                childModel.setTheState(S_TheState.Normal);
                childModel.setEmpj_BuildingInfo(object);
                childModel.setBuildingId(object.getTableId());
                /*childList = empj_PaymentBondChildDao
                    .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));
                if (!childList.isEmpty()) {
                    for (Empj_PaymentBondChild empj_PaymentBondChild : childList) {
                        if (!"1".equals(empj_PaymentBondChild.getEmpj_PaymentBond().getBusiState())) {
                            flag = false;
                            break;
                        }
                    }
                
                }*/
                count = empj_PaymentBondChildDao
                    .findByPage_Size(empj_PaymentBondChildDao.getBasicHQLNoRecordCriteria(childModel));
                /*childList = empj_PaymentBondChildDao.findByPage(
                    empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQLNoRecord(), childModel));*/
                if (count > 0) {
                    flag = false;
                }

                if (flag) {
                    Properties properties = new MyProperties();
                    properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
                    properties.put("tableId", object.getTableId());
                    properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号
                    properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity()); // 公安编号
                    properties.put("bankAccountSupervisedId", "");// 监管账户Id
                    // ---------------------获取托管标准-------------------------------------------//
                    if (object.getEscrowStandardVerMng() != null) {
                        if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                            properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

                        }
                        if (S_EscrowStandardType.StandardPercentage
                            .equals(object.getEscrowStandardVerMng().getTheType())) {

                            properties.put("escrowStandard",
                                "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例
                        }
                    }
                    // ---------------------获取托管标准-------------------------------------------//

                    // ---------------------获取楼幢账户start
                    // --------------------------------------//
                    properties.put("appliedAmount", ""); // 本次划款申请金额（元）
                    if (object.getBuildingAccount() != null) {
                        Tgpj_BuildingAccount tgpj_buildingAccount = object.getBuildingAccount();// 楼幢账户

                        properties.put("cashLimitedAmount",
                            myDouble.pointTOThousandths2(tgpj_buildingAccount.getCashLimitedAmount()));// 现金受限额度（元）
                        properties.put("effectiveLimitedAmount",
                            myDouble.pointTOThousandths2(tgpj_buildingAccount.getEffectiveLimitedAmount()));// 有效受限额度（元）
                        properties.put("allocableAmount",
                            myDouble.pointTOThousandths(tgpj_buildingAccount.getAllocableAmount()));// 当前可划拨金额（元）
                        properties.put("newAllocableAmount", tgpj_buildingAccount.getAllocableAmount()); // 可划拨金额
                        properties.put("orgLimitedAmount",
                            myDouble.pointTOThousandths(tgpj_buildingAccount.getOrgLimitedAmount()));// 初始受限额度（元）
                        properties.put("currentFigureProgress", tgpj_buildingAccount.getCurrentFigureProgress());// 当前形象进度
                        properties.put("currentLimitedRatio", tgpj_buildingAccount.getCurrentLimitedRatio());// 当前受限比例（%）
                        properties.put("currentLimitedAmount",
                            myDouble.pointTOThousandths2(tgpj_buildingAccount.getNodeLimitedAmount()));// 当前受限额度（元）
                        properties.put("totalAccountAmount",
                            myDouble.pointTOThousandths(tgpj_buildingAccount.getTotalAccountAmount()));// 总入账金额（元）
                        properties.put("appliedPayoutAmount",
                            myDouble.pointTOThousandths(tgpj_buildingAccount.getPayoutAmount()));// 已申请拨付金额（元）
                        properties.put("currentEscrowFund",
                            myDouble.pointTOThousandths(tgpj_buildingAccount.getCurrentEscrowFund()));// 当前托管余额（元）
                        properties.put("refundAmount",
                            myDouble.pointTOThousandths(tgpj_buildingAccount.getRefundAmount()));// 退房退款金额（元）
                    }
                    properties.put("payoutState", S_PayoutState.NotAppropriated);// 拨付状态
                    // ---------------------获取楼幢账户end
                    // --------------------------------------//

                    // --------------- 楼幢 --> 监管账户 信息 start
                    // -----------------------------//
                    List<Empj_BuildingAccountSupervised> empj_buildingAccountSuperviseds = new ArrayList<>();
                    Empj_BuildingAccountSupervisedForm accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
                    accountSupervisedForm.setTheState(S_TheState.Normal);
                    accountSupervisedForm.setIsUsing(0);
                    accountSupervisedForm.setBuildingInfoId(object.getTableId());

                    empj_buildingAccountSuperviseds =
                        empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao
                            .getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));

                    List<Properties> bankAccountSupervisedList = new ArrayList<Properties>();
                    for (int i = 0; i < empj_buildingAccountSuperviseds.size(); i++)
                    // for (Empj_BuildingAccountSupervised
                    // empj_buildingAccountSupervised :
                    // empj_buildingAccountSuperviseds)
                    {

                        if (null == empj_buildingAccountSuperviseds.get(i)
                            || 0 != empj_buildingAccountSuperviseds.get(i).getIsUsing()
                            || S_TheState.Normal != empj_buildingAccountSuperviseds.get(i).getTheState()) {
                            continue;
                        }

                        Tgpj_BankAccountSupervised tgpj_bankAccountSupervised =
                            empj_buildingAccountSuperviseds.get(i).getBankAccountSupervised();

                        if (null == tgpj_bankAccountSupervised.getIsUsing()
                            || 0 != tgpj_bankAccountSupervised.getIsUsing()
                            || S_TheState.Normal != tgpj_bankAccountSupervised.getTheState()) {
                            continue;
                        }

                        Properties bankAccountSupervised_Pro = new MyProperties();
                        bankAccountSupervised_Pro.put("tableId", tgpj_bankAccountSupervised.getTableId()); // 监管账号Id
                        bankAccountSupervised_Pro.put("theName", tgpj_bankAccountSupervised.getTheName());// 监管账户名称
                        /*
                         * xsz by time 2018-12-26 14:29:16
                         * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段 ----start----
                         */
                        // if (tgpj_bankAccountSupervised.getBankBranch() != null)
                        // {
                        // bankAccountSupervised_Pro.put("theNameOfBankBranch",
                        // tgpj_bankAccountSupervised.getBankBranch().getTheName());//
                        // 承办行名称
                        // }
                        bankAccountSupervised_Pro.put("theNameOfBankBranch",
                            tgpj_bankAccountSupervised.getTheNameOfBank());// 承办行名称
                        /*
                         * xsz by time 2018-12-26 14:29:16
                         * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段 ----end----
                         */
                        bankAccountSupervised_Pro.put("theAccount", tgpj_bankAccountSupervised.getTheAccount());// 监管账号
                        bankAccountSupervisedList.add(bankAccountSupervised_Pro);
                    }
                    properties.put("bankAccountSupervisedList", bankAccountSupervisedList); // 预售资金监管账号
                    // --------------- 楼幢 --> 监管账户 信息
                    // end-----------------------------//

                    properties.put("disabled", true);
                    list.add(properties);
                }

            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    public List executeSuperviseBuildingList(List<Empj_BuildingInfo> empj_BuildingInfoList) {
        List<Properties> list = new ArrayList<Properties>();
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {
                if (object == null) {
                    continue;
                }
                Properties properties = new MyProperties();
                properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
                properties.put("tableId", object.getTableId());
                properties.put("project", object.getProject());
                properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号
                properties.put("eCode", object.geteCode()); // 托管系统楼幢编号
                properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity()); // 公安编号
                properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
                properties.put("endDate", object.getEndDate());
                properties.put("busiState", object.getBusiState());

                properties.put("deliveryType", object.getDeliveryType()); // 交付类型
                Sm_BaseParameter sm_BaseParameter =
                    sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
                if (sm_BaseParameter != null) {
                    properties.put("deliveryTypeName", sm_BaseParameter.getTheName());
                }
                // 托管标准
                if (object.getEscrowStandardVerMng() != null) {
                    /*
                     * if (S_EscrowStandardType.StandardAmount.equals(object.
                     * getEscrowStandardVerMng().getTheType())) {
                     * properties.put("escrowStandard",
                     * object.getEscrowStandardVerMng().getAmount() + "元");//
                     * 托管标准金额
                     * 
                     * } if
                     * (S_EscrowStandardType.StandardPercentage.equals(object.
                     * getEscrowStandardVerMng().getTheType())) {
                     * 
                     * properties.put("escrowStandard", "物价备案均价*" +
                     * object.getEscrowStandardVerMng().getPercentage() +
                     * "%");// 托管标准比例 }
                     */
                    properties.put("escrowStandardName", object.getEscrowStandardVerMng().getTheName());// 托管标准版本
                } else {
                    /*
                     * 根据楼幢类型判断显示 毛坯房：30%或4000元/m²取小值 成品房：30%或6000元/m²取小值
                     */

                }

                /*
                 * 根据楼幢类型加载受限额度版本
                 */
                Tgpj_BldLimitAmountVer_AF nowLimitAmountVer =
                    tgpj_BldLimitAmountVer_AFDao.getNowLimitAmountVer(object.getDeliveryType());

                // 备案价格
                Tgpj_BuildingAccount buildingAccount = object.getBuildingAccount();
                if (buildingAccount != null) {
                    properties.put("recordAvgPriceOfBuilding",
                        myDouble.pointTOThousandths(buildingAccount.getRecordAvgPriceOfBuilding()));// 楼幢备案均价
                    properties.put("orgLimitedAmount",
                        myDouble.pointTOThousandths(buildingAccount.getOrgLimitedAmount()));// 初始受限额度（元）

                    if (null == buildingAccount.getRecordAvgPriceOfBuilding()
                        || "".equals(buildingAccount.getRecordAvgPriceOfBuilding().toString())) {
                        if ("1".equals(object.getDeliveryType())) {
                            properties.put("escrowStandard", "30%或4000元/m²取小值");
                        } else {
                            properties.put("escrowStandard", "30%或6000元/m²取小值");
                        }
                    } else {
                        if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                            properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

                        }
                        if (S_EscrowStandardType.StandardPercentage
                            .equals(object.getEscrowStandardVerMng().getTheType())) {

                            properties.put("escrowStandard",
                                "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例

                            if (object.getEscrowStandardVerMng().getPercentage() == 30) {
                                double price = buildingAccount.getRecordAvgPriceOfBuilding() * 0.3;
                                if ("1".equals(object.getDeliveryType())) {
                                    // 毛坯
                                    if (price - 4000 > 0) {
                                        properties.put("escrowStandard", "4000元/m²");
                                    }
                                } else {
                                    // 成品
                                    if (price - 6000 > 0) {
                                        properties.put("escrowStandard", "6000元/m²");
                                    }
                                }
                            }
                        }

                    }

                    if ("1".equals(object.getDeliveryType())) {
                        properties.put("escrowStandard", "30%或4000元/m²取小值");
                    } else {
                        properties.put("escrowStandard", "30%或6000元/m²取小值");
                    }

                    Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = buildingAccount.getBldLimitAmountVerDtl();
                    if (null != bldLimitAmountVerDtl) {
                        Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
                        if (null == bldLimitAmountVer_AF) {
                            properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                            properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());
                        } else {
                            properties.put("bldLimitAmountName", bldLimitAmountVer_AF.getTheName());
                            properties.put("bldLimitAmountId", bldLimitAmountVer_AF.getTableId());
                        }
                    } else {
                        properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                        properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());
                    }

                } else {

                    /*
                     * 根据楼幢类型加载启用时间内的版本
                     */
                    if ("1".equals(object.getDeliveryType())) {
                        properties.put("escrowStandard", "30%或4000元/m²取小值");
                    } else {
                        properties.put("escrowStandard", "30%或6000元/m²取小值");
                    }

                    properties.put("recordAvgPriceOfBuilding", "0.00");// 楼幢备案均价
                    properties.put("orgLimitedAmount", "0.00");// 初始受限额度（元）
                    properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                    properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());
                }

                properties.put("escrowArea",
                    myDouble.pointTOThousandths(null == object.getEscrowArea() ? 0 : object.getEscrowArea()));// 托管面积

                /*
                 * xsz by time 2018-12-27 13:36:16 已与zcl确认地址取项目地址
                 * ----start-------
                 */
                // properties.put("position", object.getPosition());
                properties.put("position", object.getProject().getAddress());
                /*
                 * xsz by time 2018-12-27 13:36:16 已与zcl确认地址取项目地址
                 * ----start-------
                 */

                list.add(properties);
            }
        }
        return list;
    }

    @SuppressWarnings({"rawtypes", "static-access"})
    public List executeSuperviseBuildingList(List<Empj_BuildingInfo> empj_BuildingInfoList, String agreementVersion) {
        List<Properties> list = new ArrayList<Properties>();
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {
                if (object == null) {
                    continue;
                }
                Properties properties = new MyProperties();
                properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
                properties.put("tableId", object.getTableId());
                properties.put("project", object.getProject());
                properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号
                properties.put("eCode", object.geteCode()); // 托管系统楼幢编号
                properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity()); // 公安编号
                properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
                properties.put("endDate", object.getEndDate());
                properties.put("busiState", object.getBusiState());

                properties.put("deliveryType", object.getDeliveryType()); // 交付类型
                Sm_BaseParameter sm_BaseParameter =
                    sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
                if (sm_BaseParameter != null) {
                    properties.put("deliveryTypeName", sm_BaseParameter.getTheName());
                }
                // 托管标准
                if (object.getEscrowStandardVerMng() != null) {
                    properties.put("escrowStandardName", object.getEscrowStandardVerMng().getTheName());// 托管标准版本
                }

                /*
                 * 根据楼幢类型加载受限额度版本
                 */
                Tgpj_BldLimitAmountVer_AF nowLimitAmountVer =
                    tgpj_BldLimitAmountVer_AFDao.getNowLimitAmountVer(object.getDeliveryType());

                // 备案价格
                Tgpj_BuildingAccount buildingAccount = object.getBuildingAccount();
                if (buildingAccount != null) {
                    // 楼幢备案均价
                    properties.put("recordAvgPriceOfBuilding",
                        myDouble.pointTOThousandths(buildingAccount.getRecordAvgPriceOfBuilding()));
                    // 初始受限额度（元）
                    properties.put("orgLimitedAmount",
                        myDouble.pointTOThousandths(buildingAccount.getOrgLimitedAmount()));

                    Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = buildingAccount.getBldLimitAmountVerDtl();
                    if (null != bldLimitAmountVerDtl) {
                        Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
                        if (null == bldLimitAmountVer_AF) {
                            properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                            properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());
                        } else {
                            properties.put("bldLimitAmountName", bldLimitAmountVer_AF.getTheName());
                            properties.put("bldLimitAmountId", bldLimitAmountVer_AF.getTableId());
                        }
                    } else {
                        properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                        properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());
                    }

                } else {

                    // 楼幢备案均价
                    properties.put("recordAvgPriceOfBuilding", "0.00");
                    // 初始受限额度（元）
                    properties.put("orgLimitedAmount", "0.00");
                    properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                    properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());
                }

                // 托管面积
                properties.put("escrowArea",
                    myDouble.pointTOThousandths(null == object.getEscrowArea() ? 0 : object.getEscrowArea()));

                if ("1".equals(object.getDeliveryType())) {

                    switch (agreementVersion) {
                        case "V1703":
                            properties.put("escrowStandard", "3500元/m²");
                            break;
                        case "V1710":
                            properties.put("escrowStandard", "物价备案均价*40%");
                            break;
                        case "V1812":
                            properties.put("escrowStandard", "30%或4000元/m²取小值");
                            break;
                        case "V2003":
                            properties.put("escrowStandard", "30%或4000元/m²取小值");
                            break;
                        case "V2009":
                            properties.put("escrowStandard", "30%或4000元/m²取小值");
                            break;

                        default:
                            properties.put("escrowStandard", "30%或4000元/m²取小值");
                            break;
                    }

                    
                } else {
                    switch (agreementVersion) {
                        case "V1703":
                            properties.put("escrowStandard", "物价备案均价*50%");
                            break;
                        case "V1710":
                            properties.put("escrowStandard", "物价备案均价*40%");
                            break;
                        case "V1812":
                            properties.put("escrowStandard", "30%或6000元/m²取小值");
                            break;
                        case "V2003":
                            properties.put("escrowStandard", "30%或6000元/m²取小值");
                            break;
                        case "V2009":
                            properties.put("escrowStandard", "30%或6000元/m²取小值");
                            break;

                        default:
                            properties.put("escrowStandard", "30%或6000元/m²取小值");
                            break;
                    }
                }

                /*
                 * xsz by time 2018-12-27 13:36:16 已与zcl确认地址取项目地址
                 * ----start-------
                 */
                // properties.put("position", object.getPosition());
                properties.put("position", object.getProject().getAddress());
                /*
                 * xsz by time 2018-12-27 13:36:16 已与zcl确认地址取项目地址
                 * ----start-------
                 */

                list.add(properties);
            }
        }
        return list;
    }

    public Properties getByDetail(Empj_BuildingInfo object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        // 主键
        properties.put("tableId", object.getTableId());
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢

        /*
         * eCodeFromPublicSecurity：公安编号 eCodeOfLand：幢编号（国土）
         * eCodeFromPresellSystem：预售系统楼幢编号
         */
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());// 公安编号
        properties.put("eCodeOfLand", object.geteCodeOfLand());// 幢编号（国土）
        properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());// 预售系统楼幢编号
        properties.put("position", object.getPosition());
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号

        properties.put("eCode", object.geteCode());

        return properties;
    }

    @SuppressWarnings("rawtypes")
    public List getByList(List<Empj_BuildingInfo> empj_BuildingInfoList) {

        List<Properties> list = new ArrayList<Properties>();
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {
                if (object == null) {
                    continue;
                }
                Properties properties = new MyProperties();
                properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
                properties.put("tableId", object.getTableId());
                properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号

                list.add(properties);
            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    public List executeForBuildingTableDetail(List<Empj_BuildingInfo> empj_BuildingInfoList) {

        List<Properties> list = new ArrayList<Properties>();
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {
                if (object == null)
                    continue;

                Properties properties = new MyProperties();
                properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢
                properties.put("tableId", object.getTableId());
                properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号

                list.add(properties);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public Properties executeForBuildingTableDetail(Empj_BuildingInfo object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢

        if (object.getCityRegion() != null) {
            properties.put("cityRegionName", object.getCityRegion().getTheName());
        }

        if (object.getUserUpdate() != null) {
            properties.put("userUpdateName", object.getUserUpdate().getTheName());
        }
        properties.put("escrowArea", object.getEscrowArea());// 托管面积
        properties.put("eCodeOfProjectFromPresellSystem", object.geteCodeOfProjectFromPresellSystem());// 预售项目编号
        if (object.getUserRecord() != null) {
            properties.put("userRecordName", object.getUserRecord().getTheName());
            properties.put("recordTimeStamp",
                MyDatetime.getInstance().dateToString2(object.getUserRecord().getRecordTimeStamp()));
        }
        properties.put("buildingArea", object.getBuildingArea());
        if (object.getDevelopCompany() != null) {
            properties.put("developCompanyName", object.getDevelopCompany().getTheName());
        }
        if (object.getBuildingAccount() != null) {
            properties.put("buildAmountPaid", null == object.getBuildingAccount().getBuildAmountPaid() ? 0.00
                : object.getBuildingAccount().getBuildAmountPaid());// 楼幢项目建设已实际支付金额（元）
            properties.put("totalAmountGuaranteed", object.getBuildingAccount().getTotalAmountGuaranteed());// 已落实支付保证累计金额（元）

            // 已落实支付保证累计金额（元）> 0 说明该楼幢已经支付保证
            if (null != object.getBuildingAccount().getTotalAmountGuaranteed()
                && object.getBuildingAccount().getTotalAmountGuaranteed() > 0) {
                properties.put("isSupportPGS", "是"); // 是否支付保证业务楼幢
            } else {
                properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
            }

            properties.put("buildAmountPay", null == object.getBuildingAccount().getBuildAmountPay() ? 0.00
                : object.getBuildingAccount().getBuildAmountPay());// 楼幢项目建设待支付承保累计金额（元）
            properties.put("paymentLines", object.getBuildingAccount().getPaymentLines());// 支付保证封顶百分比
            properties.put("currentFigureProgress", object.getBuildingAccount().getCurrentFigureProgress());// 当前形象进度
            properties.put("recordAvgPriceOfBuilding", object.getBuildingAccount().getRecordAvgPriceOfBuilding());// 楼幢住宅备案均价
        } else {
            properties.put("isSupportPGS", "否"); // 是否支付保证业务楼幢
        }
        properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
        properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数
        properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());// 预售系统楼幢编号
        if (object.getProject() != null) {
            properties.put("empj_ProjectInfoName", object.getProject().getTheName());
            properties.put("empj_ProjectInfoAddress", object.getProject().getAddress());
        }
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号
        if (object.getExtendInfo() != null) {

            properties.put("escrowState", object.getExtendInfo().getEscrowState()); // 托管状态
            properties.put("landMortgagor", object.getExtendInfo().getLandMortgagor()); // 土地抵押权人
            properties.put("landMortgageState", object.getExtendInfo().getLandMortgageState()); // 土地抵押状态
            properties.put("landMortgageAmount", object.getExtendInfo().getLandMortgageAmount()); // 土地抵押金额
        }
        properties.put("theNameFromPresellSystem", object.getTheNameFromPresellSystem());// 预售项目名称
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());// 公安编号
        properties.put("eCodeFromPresellCert", object.geteCodeFromPresellCert());// 预售证号
        properties.put("deliveryType", object.getDeliveryType());// 交付类型

        Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("5", object.getDeliveryType());
        if (sm_BaseParameter != null) {
            properties.put("sm_BaseParameter", sm_BaseParameter);
            properties.put("parameterName", sm_BaseParameter.getTheName());
        }

        // properties.put("escrowStandard", object.getEscrowStandard());// 托管标准
        // ---------------------获取托管标准-------------------------------------------//
        if (object.getEscrowStandardVerMng() != null) {
            if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

            }
            if (S_EscrowStandardType.StandardPercentage.equals(object.getEscrowStandardVerMng().getTheType())) {

                properties.put("escrowStandard", "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例
            }
        }
        // ---------------------获取托管标准-------------------------------------------//
        properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));// 操作时间
        if (object.getBuildingAccount() != null)// 楼幢账户
        {
            properties.put("recordAvgPriceOfBuilding", object.getBuildingAccount().getRecordAvgPriceOfBuilding());// 楼幢备案均价
            properties.put("currentFigureProgress", object.getBuildingAccount().getCurrentFigureProgress());// 当前形象进度
        }

        // 楼盘表详情使用
        // 查该楼幢下有几个单元
        Empj_UnitInfoForm empj_UnitInfoForm = new Empj_UnitInfoForm();
        empj_UnitInfoForm.setTheState(S_TheState.Normal);
        empj_UnitInfoForm.setBuilding(object);

        // 查该楼幢下有几个户室
        Empj_HouseInfoForm empj_HouseInfoForm = new Empj_HouseInfoForm();
        empj_HouseInfoForm.setBuildingId(object.getTableId());
        empj_HouseInfoForm.setTheState(S_TheState.Normal);

        List<Empj_HouseInfo> empj_HouseInfoList = empj_HouseInfoDao
            .findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getBuildingHQL(), empj_HouseInfoForm));

        List<Double> floorList = new ArrayList<Double>();
        for (Empj_HouseInfo empj_HouseInfo : empj_HouseInfoList) {
            if (!floorList.contains(empj_HouseInfo.getFloor())) {
                floorList.add(empj_HouseInfo.getFloor());
            }
        }

        Collections.sort(floorList, new MyCoachComparator1());

        Integer erfloorGarageCount = 0;

        if (empj_HouseInfoList != null && !empj_HouseInfoList.isEmpty()) {
            Map<Double, List<Empj_HouseInfo>> map = new LinkedHashMap<Double, List<Empj_HouseInfo>>();
            for (int i = 0; i < empj_HouseInfoList.size(); i++) {
                Empj_HouseInfo empj_HouseInfo = empj_HouseInfoList.get(i);
                if (empj_HouseInfo.getRowSpan() != null && empj_HouseInfo.getRowSpan() > 1)
                // 判断是否有合并行，如果有将其加入到高层key对应的map中
                {
                    Integer rowspan = empj_HouseInfo.getRowSpan();
                    Integer overFloors = rowspan - 1;
                    Integer index = floorList.indexOf(empj_HouseInfo.getFloor());
                    Double showFloor = floorList.get(index - overFloors);
                    if (map.get(showFloor) == null) {
                        List<Empj_HouseInfo> list = new ArrayList<Empj_HouseInfo>();
                        list.add(empj_HouseInfo);
                        map.put(showFloor, list);
                    } else {
                        map.get(showFloor).add(empj_HouseInfo);
                    }
                } else {
                    if (map.get(empj_HouseInfo.getFloor()) == null) {
                        List<Empj_HouseInfo> list = new ArrayList<Empj_HouseInfo>();
                        list.add(empj_HouseInfo);
                        map.put(empj_HouseInfo.getFloor(), list);
                    } else {
                        map.get(empj_HouseInfo.getFloor()).add(empj_HouseInfo);
                    }
                }

                if (empj_HouseInfo.getFloor() < 0 && empj_HouseInfo.getColIndex() > erfloorGarageCount) {
                    erfloorGarageCount = empj_HouseInfo.getColIndex();
                }
            }

            List<Empj_BuildingTableTemplate> empj_BuildingTableTemplateList =
                new ArrayList<Empj_BuildingTableTemplate>();
            List<Empj_BuildingTableTemplate> empj_GarageTableTemplateList = new ArrayList<Empj_BuildingTableTemplate>();

            List<Empj_UnitInfo> empj_UnitInfoList = empj_UnitInfoDao
                .findByPage(empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getBasicHQL(), empj_UnitInfoForm));

            Integer erfloorHouseCount = 0;

            for (Empj_UnitInfo empj_UnitInfo : empj_UnitInfoList) {
                if (empj_UnitInfo.getUpfloorHouseHoldNumber() != null) {
                    erfloorHouseCount += empj_UnitInfo.getUpfloorHouseHoldNumber();
                }
            }

            for (Double floor : map.keySet()) {
                Integer perFloorHouseCount = map.get(floor).size();
                List<Empj_HouseInfo> perFloorHouseList = map.get(floor);
                List<Empj_HouseInfo> perFloorShowHouseList = new ArrayList<Empj_HouseInfo>();
                perFloorShowHouseList.addAll(perFloorHouseList);
                List<Empj_UnitInfo> perFloorUnitList =
                    perFloorHouseList.stream().map((obj) -> obj.getUnitInfo()).distinct().collect(Collectors.toList());

                if (floor > 0) {
                    Integer insertEmpty = 0;

                    if (perFloorHouseCount < erfloorHouseCount) {
                        Integer nowIndex = 0;
                        for (Empj_HouseInfo empj_HouseInfo : perFloorHouseList) {
                            Integer colNumber = empj_HouseInfo.getColNumber();
                            Integer colIndex = empj_HouseInfo.getColIndex();
                            Integer colspan = empj_HouseInfo.getColSpan();

                            if (colIndex > colNumber && colIndex - nowIndex > 1) {
                                for (int i = 1; i < colIndex - nowIndex; i++) {
                                    Empj_HouseInfo empj_HouseInfoAdd = new Empj_HouseInfo();
                                    empj_HouseInfoAdd.setBusiState(S_HouseBusiState.EmptyHouse);

                                    perFloorShowHouseList.add(colNumber - 1, empj_HouseInfoAdd);
                                }
                            }

                            if (colspan > 1) {
                                colIndex = colIndex + colspan - 1;
                                insertEmpty += colspan - 1;
                            }

                            nowIndex = colIndex;
                        }
                    }

                    insertEmpty += perFloorShowHouseList.size();

                    if (insertEmpty < erfloorHouseCount) {
                        for (int i = 0; i < erfloorHouseCount - insertEmpty; i++) {
                            Empj_HouseInfo empj_HouseInfoAdd = new Empj_HouseInfo();
                            empj_HouseInfoAdd.setBusiState(S_HouseBusiState.EmptyHouse);

                            perFloorShowHouseList.add(empj_HouseInfoAdd);
                        }
                    }

                    Empj_BuildingTableTemplate empj_BuildingTableTemplate = new Empj_BuildingTableTemplate();
                    empj_BuildingTableTemplate.setFloor(floor);
                    empj_BuildingTableTemplate
                        .setEmpj_HouseInfoList(empj_HouseInfoRebuild.executeForHouseTableDetail(perFloorShowHouseList));
                    empj_BuildingTableTemplate.setEmpj_UnitInfoList(perFloorUnitList);

                    empj_BuildingTableTemplateList.add(empj_BuildingTableTemplate);
                }

                if (floor < 0) {
                    Integer insertEmpty = 0;

                    if (perFloorHouseCount < erfloorGarageCount) {
                        Integer nowIndex = 0;
                        for (Empj_HouseInfo empj_HouseInfo : perFloorHouseList) {
                            Integer colNumber = empj_HouseInfo.getColNumber();
                            Integer colIndex = empj_HouseInfo.getColIndex();
                            Integer colspan = empj_HouseInfo.getColSpan();

                            if (colIndex > colNumber && colIndex - nowIndex > 1) {
                                for (int i = 1; i < colIndex - nowIndex; i++) {
                                    Empj_HouseInfo empj_HouseInfoAdd = new Empj_HouseInfo();
                                    empj_HouseInfoAdd.setBusiState(S_HouseBusiState.EmptyHouse);

                                    perFloorShowHouseList.add(colNumber - 1, empj_HouseInfoAdd);
                                }
                            }

                            if (colspan > 1) {
                                colIndex = colIndex + colspan - 1;
                                insertEmpty += colspan - 1;
                            }

                            nowIndex = colIndex;
                        }
                    }

                    insertEmpty += perFloorShowHouseList.size();

                    if (insertEmpty < erfloorGarageCount) {
                        for (int i = 0; i < erfloorHouseCount - insertEmpty; i++) {
                            Empj_HouseInfo empj_HouseInfoAdd = new Empj_HouseInfo();
                            empj_HouseInfoAdd.setBusiState(S_HouseBusiState.EmptyHouse);

                            perFloorShowHouseList.add(empj_HouseInfoAdd);
                        }
                    }

                    Empj_BuildingTableTemplate empj_BuildingTableTemplate = new Empj_BuildingTableTemplate();
                    empj_BuildingTableTemplate.setFloor(floor);
                    empj_BuildingTableTemplate
                        .setEmpj_HouseInfoList(empj_HouseInfoRebuild.executeForHouseTableDetail(perFloorShowHouseList));
                    empj_BuildingTableTemplate.setEmpj_UnitInfoList(perFloorUnitList);

                    empj_GarageTableTemplateList.add(empj_BuildingTableTemplate);
                }
            }

            Collections.sort(empj_BuildingTableTemplateList, new MyCoachComparator2());
            Collections.sort(empj_GarageTableTemplateList, new MyCoachComparator2());

            properties.put("empj_BuildingTableTemplateList", empj_BuildingTableTemplateList);// 户室列表
            properties.put("erfloorHouseCount", erfloorHouseCount);// 每层最大户室数
            properties.put("empj_GarageTableTemplateList", empj_GarageTableTemplateList);// 车库列表
            properties.put("erfloorGarageCount", erfloorGarageCount);// 每层最大车库数

            // 楼幢备案均价
            Tgpj_BuildingAvgPriceForm tgpj_BuildingAvgPriceForm = new Tgpj_BuildingAvgPriceForm();
            tgpj_BuildingAvgPriceForm.setBuildingInfoId(object.getTableId());
            tgpj_BuildingAvgPriceForm.setTheState(S_TheState.Normal);
            Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice =
                tgpj_BuildingAvgPriceDao.findOneByQuery_T(tgpj_BuildingAvgPriceDao
                    .getQuery(tgpj_BuildingAvgPriceDao.getBuildingHQL(), tgpj_BuildingAvgPriceForm));
            if (tgpj_BuildingAvgPrice != null) {
                properties.put("buildingAvgPriceId", tgpj_BuildingAvgPrice.getTableId());// 楼幢备案均价ID
            }
        }

        return properties;
    }

    @SuppressWarnings("rawtypes")
    public class MyCoachComparator1 implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            if (!(o1 instanceof Double)) {
                return 0;
            }
            if (!(o2 instanceof Double)) {
                return 0;
            }

            Double number1 = (Double)o1;
            Double number2 = (Double)o2;
            if (number1 == null || number2 == null) {
                return 0;// 不变
            }
            if (number1 < number2) {
                return 1;// 排前面
            } else if (number1 - number2 < 0.00001) {
                return 0;// 不变
            } else {
                return -1;// 排后面
            }
        }

        public List executeForSelectList(List<Empj_BuildingInfo> empj_buildingInfoList) {
            List<Properties> list = new ArrayList<Properties>();
            if (empj_buildingInfoList != null) {
                for (Empj_BuildingInfo object : empj_buildingInfoList) {
                    Properties properties = new MyProperties();
                    properties.put("tableId", object.getTableId());
                    properties.put("theName", object.geteCode());
                    properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
                    properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
                    properties.put("position", object.getPosition());
                    properties.put("upfloorNumber", object.getUpfloorNumber());
                    Tgpj_BuildingAccount buildingAccount = object.getBuildingAccount();
                    if (buildingAccount != null) {
                        properties.put("recordAvgPriceOfBuilding", buildingAccount.getRecordAvgPriceOfBuilding());
                    }
                    list.add(properties);
                }
            }
            return list;
        }
    }

    @SuppressWarnings("rawtypes")
    public class MyCoachComparator2 implements Comparator {
        // 先按theIndex排序（小的排前面）
        // 若theIndex一样，按照tableId顺序，小的排前面
        @Override
        public int compare(Object o1, Object o2) {
            if (!(o1 instanceof Empj_BuildingTableTemplate)) {
                return 0;
            }
            if (!(o2 instanceof Empj_BuildingTableTemplate)) {
                return 0;
            }

            Empj_BuildingTableTemplate uiResource1 = (Empj_BuildingTableTemplate)o1;
            Empj_BuildingTableTemplate uiResource2 = (Empj_BuildingTableTemplate)o2;
            Double theIndex1 = uiResource1.getFloor() == null ? 0.0 : uiResource1.getFloor();
            Double theIndex2 = uiResource2.getFloor() == null ? 0.0 : uiResource2.getFloor();

            if (theIndex2 < theIndex1) {
                return -1;// 排前面
            } else if (theIndex2 - theIndex1 < 0.0000001) {
                return 0;// 不变
            } else {
                return 1;// 排后面
            }
        }
    }

    /*
     * xsz by time 2018-11-29 16:05:58
     * 
     */
    @SuppressWarnings("unchecked")
    public Properties getDetailForSpecialFund(Empj_BuildingInfo object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();
        properties.put("isAdvanceSale", "1".equals(object.getIsAdvanceSale()) ? "是" : "否");// 是否预售楼幢

        properties.put("tableId", object.getTableId());
        properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号
        properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity()); // 公安编号
        properties.put("bankAccountSupervisedId", "");// 监管账户Id
        // ---------------------获取托管标准-------------------------------------------//
        if (object.getEscrowStandardVerMng() != null) {
            if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

            }
            if (S_EscrowStandardType.StandardPercentage.equals(object.getEscrowStandardVerMng().getTheType())) {

                properties.put("escrowStandard", "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例
            }
        }
        // ---------------------获取托管标准-------------------------------------------//

        // --------------- 楼幢 --> 监管账户 信息 start -----------------------------//
        List<Empj_BuildingAccountSupervised> empj_buildingAccountSuperviseds = new ArrayList<>();
        Empj_BuildingAccountSupervisedForm accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
        accountSupervisedForm.setTheState(S_TheState.Normal);
        accountSupervisedForm.setBuildingInfoId(object.getTableId());

        empj_buildingAccountSuperviseds = empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao
            .getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));

        List<Properties> bankAccountSupervisedList = new ArrayList<Properties>();
        for (Empj_BuildingAccountSupervised empj_buildingAccountSupervised : empj_buildingAccountSuperviseds) {
            Tgpj_BankAccountSupervised tgpj_bankAccountSupervised =
                empj_buildingAccountSupervised.getBankAccountSupervised();
            Properties bankAccountSupervised_Pro = new MyProperties();
            bankAccountSupervised_Pro.put("tableId", tgpj_bankAccountSupervised.getTableId()); // 监管账号Id
            bankAccountSupervised_Pro.put("theName", tgpj_bankAccountSupervised.getTheName());// 监管账户名称
            /*
             * xsz by time 2018-12-26 14:29:16
             * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段 ----start----
             */
            // if (null != tgpj_bankAccountSupervised.getBankBranch())
            // {
            // bankAccountSupervised_Pro.put("theNameOfBankBranch",
            // tgpj_bankAccountSupervised.getBankBranch().getTheName());// 承办行名称
            // }
            bankAccountSupervised_Pro.put("theNameOfBankBranch", tgpj_bankAccountSupervised.getTheNameOfBank());// 承办行名称
            /*
             * xsz by time 2018-12-26 14:29:16
             * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段 ----end----
             */
            if (null != tgpj_bankAccountSupervised.getBank()) {
                bankAccountSupervised_Pro.put("theNameOfBank", tgpj_bankAccountSupervised.getBank().getTheName());// 监管银行
            }
            bankAccountSupervised_Pro.put("theAccount", tgpj_bankAccountSupervised.getTheAccount());// 监管账号
            bankAccountSupervisedList.add(bankAccountSupervised_Pro);
        }
        properties.put("bankAccountSupervisedList", bankAccountSupervisedList); // 预售资金监管账号
        // --------------- 楼幢 --> 监管账户 信息 end-----------------------------//

        properties.put("disabled", true);

        return properties;
    }

    /**
     * 进度节点变更楼幢列表
     * 
     * @param empj_BuildingInfoList
     * @return
     */
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings({"static-access", "unchecked"})
    public List executeBldForBuildingList(List<Empj_BuildingInfo> empj_BuildingInfoList) {
        List<Properties> list = new ArrayList<Properties>();
        Tgxy_EscrowAgreementForm escrowAgreementForm;
        Empj_BuildingInfoForm buildingInfoForm;
        Properties execute;
        List<Map<String, Object>> listMap;
        if (empj_BuildingInfoList != null) {
            for (Empj_BuildingInfo object : empj_BuildingInfoList) {
                if (object == null) {
                    continue;
                }
                Properties properties = new MyProperties();
                properties.put("tableId", object.getTableId());
                properties.put("projectId", object.getProject().getTableId());
                properties.put("eCodeFromConstruction", object.geteCodeFromConstruction()); // 施工编号
                properties.put("busiState", object.getBusiState());

                properties.put("escrowArea",
                    myDouble.pointTOThousandths(null == object.getEscrowArea() ? 0 : object.getEscrowArea()));// 托管面积
                properties.put("upfloorNumber", MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0)); // 地上层数
                properties.put("downfloorNumber", MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0)); // 地下层数

                properties.put("deliveryType", object.getDeliveryType()); // 交付类型
                /*
                 * Sm_BaseParameter sm_BaseParameter =
                 * sm_BaseParameterGetService.getParameter("5",
                 * object.getDeliveryType()); if (sm_BaseParameter != null) {
                 * properties.put("deliveryTypeName",
                 * sm_BaseParameter.getTheName()); }
                 */
                if ("1".equals(object.getDeliveryType())) {
                    properties.put("deliveryTypeName", "毛坯房");
                } else {
                    properties.put("deliveryTypeName", "成品房");
                }

                /**
                 * 是否跳出
                 */
                boolean isCon = false;
                /*
                 * TODO 查询语句 签约时间-最近的一次托管合作协议
                 */
                /*
                 * escrowAgreementForm = new Tgxy_EscrowAgreementForm();
                 * escrowAgreementForm.setTheState(S_TheState.Normal);
                 * escrowAgreementForm.seteCodeOfBuildingInfo(object.geteCode())
                 * ; tgxy_EscrowAgreementDao
                 */

                String sql =
                    "SELECT B.* FROM REL_ESCROWAGREEMENT_BUILDING A LEFT JOIN TGXY_ESCROWAGREEMENT B ON A.TGXY_ESCROWAGREEMENT = B.TABLEID WHERE A.EMPJ_BUILDINGINFO = "
                        + object.getTableId() + " AND B.THESTATE=0 ORDER BY B.CREATETIMESTAMP DESC";
                List<Tgxy_EscrowAgreement> tgxy_EscrowAgreementList = new ArrayList<Tgxy_EscrowAgreement>();
                tgxy_EscrowAgreementList = sessionFactory.getCurrentSession()
                    .createNativeQuery(sql, Tgxy_EscrowAgreement.class).getResultList();
                if (null == tgxy_EscrowAgreementList || tgxy_EscrowAgreementList.size() < 1) {
                    continue;
                }
                properties.put("signingDate", null == tgxy_EscrowAgreementList.get(0).getContractApplicationDate() ? ""
                    : tgxy_EscrowAgreementList.get(0).getContractApplicationDate());

                /*
                 * TODO 查询楼幢是否在申请中 后期过滤
                 */

                // 托管标准版本
                if (object.getEscrowStandardVerMng() != null) {
                    properties.put("escrowStandardName", object.getEscrowStandardVerMng().getTheName());
                } else {
                    properties.put("escrowStandardName", "");
                }

                properties.put("disabled", true);
                properties.put("limitedAmount", "");
                properties.put("limitedId", "");

                /*
                 * 根据楼幢类型加载受限额度版本
                 */
                Tgpj_BldLimitAmountVer_AF nowLimitAmountVer =
                    tgpj_BldLimitAmountVer_AFDao.getNowLimitAmountVer(object.getDeliveryType());
                // 楼幢账户信息（托管标准、初始受限额度、受限额度）
                Tgpj_BuildingAccount buildingAccount = object.getBuildingAccount();
                if (buildingAccount != null) {

                    // 楼幢备案均价
                    properties.put("recordAvgPriceOfBuilding",
                        myDouble.pointTOThousandths(buildingAccount.getRecordAvgPriceOfBuilding()));
                    // 初始受限额度
                    properties.put("orgLimitedAmount",
                        myDouble.pointTOThousandths(buildingAccount.getOrgLimitedAmount()));

                    /*
                     * 楼幢备案均价未确定时，确定托管标准
                     */
                    if (null == buildingAccount.getRecordAvgPriceOfBuilding()
                        || "".equals(buildingAccount.getRecordAvgPriceOfBuilding().toString())) {
                        if ("1".equals(object.getDeliveryType())) {
                            properties.put("escrowStandard", "30%或4000元/m²取小值");
                        } else {
                            properties.put("escrowStandard", "30%或6000元/m²取小值");
                        }
                    } else {
                        /*
                         * 1.如果是标准金额，则直接取值 2.如果是标准比例且等于30% 根据毛坯房或者成品房的规则计算取值
                         */
                        if (S_EscrowStandardType.StandardAmount.equals(object.getEscrowStandardVerMng().getTheType())) {
                            properties.put("escrowStandard", object.getEscrowStandardVerMng().getAmount() + "元");// 托管标准金额

                        }
                        if (S_EscrowStandardType.StandardPercentage
                            .equals(object.getEscrowStandardVerMng().getTheType())) {

                            properties.put("escrowStandard",
                                "物价备案均价*" + object.getEscrowStandardVerMng().getPercentage() + "%");// 托管标准比例

                            if (object.getEscrowStandardVerMng().getPercentage() == 30) {
                                double price = buildingAccount.getRecordAvgPriceOfBuilding() * 0.3;
                                if ("1".equals(object.getDeliveryType())) {
                                    // 毛坯
                                    if (price - 4000 > 0) {
                                        properties.put("escrowStandard", "4000元/m²");
                                    }
                                } else {
                                    // 成品
                                    if (price - 6000 > 0) {
                                        properties.put("escrowStandard", "6000元/m²");
                                    }
                                }
                            }
                        }

                    }

                    Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = buildingAccount.getBldLimitAmountVerDtl();
                    if (null != bldLimitAmountVerDtl) {
                        Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
                        if (null == bldLimitAmountVer_AF) {
                            isCon = true;
                        } else {
                            properties.put("bldLimitAmountName", bldLimitAmountVer_AF.getTheName());
                            properties.put("bldLimitAmountId", bldLimitAmountVer_AF.getTableId());
                        }
                    } else {
                        isCon = true;
                    }

                    // 当前受限比例
                    Double currentLimitedRatio = null == buildingAccount.getCurrentLimitedRatio() ? 0.00
                        : buildingAccount.getCurrentLimitedRatio();
                    String currentFigureProgress = null == buildingAccount.getCurrentFigureProgress() ? ""
                        : buildingAccount.getCurrentFigureProgress();
                    properties.put("currentLimitedRatio", null == buildingAccount.getCurrentLimitedRatio() ? 0.00
                        : buildingAccount.getCurrentLimitedRatio());
                    // properties.put("currentFigureProgress", null ==
                    // buildingAccount.getCurrentFigureProgress()?"":buildingAccount.getCurrentFigureProgress());
                    // properties.put("nowLimitedAmount", currentFigureProgress + "-" +
                    // String.valueOf(currentLimitedRatio) +"%");
                    properties.put("nowLimitedAmount", currentFigureProgress);
                    if (0.00 == (Double)properties.get("currentLimitedRatio")) {
                        isCon = true;
                    }

                } else {
                    isCon = true;
                    /*
                     * 根据楼幢类型加载启用时间内的版本
                     */
                    if ("1".equals(object.getDeliveryType())) {
                        properties.put("escrowStandard", "30%或4000元/m²取小值");
                    } else {
                        properties.put("escrowStandard", "30%或6000元/m²取小值");
                    }

                    properties.put("recordAvgPriceOfBuilding", "0.00");// 楼幢备案均价
                    properties.put("orgLimitedAmount", "0.00");// 初始受限额度（元）
                    properties.put("bldLimitAmountName", nowLimitAmountVer.getTheName());
                    properties.put("bldLimitAmountId", nowLimitAmountVer.getTableId());

                    // 当前受限比例
                    properties.put("currentLimitedRatio", "0");
                }

                if (isCon) {
                    continue;
                }

                /*
                 * 查询每一个楼幢可下拉的拟变更形象进度
                 */
                buildingInfoForm = new Empj_BuildingInfoForm();
                buildingInfoForm.setTableId(object.getTableId());
                buildingInfoForm.setNowLimitRatio(properties.getProperty("currentLimitedRatio"));
                execute = bldAccountGetLimitAmountVerService.execute(buildingInfoForm);
                if (null != execute) {
                    listMap = new ArrayList<>();
                    listMap = (List<Map<String, Object>>)execute.get("versionList");
                    if (null == listMap || listMap.size() == 1) {
                        continue;
                    } else {
                        listMap.remove(listMap.get(0));
                    }

                    properties.put("versionList", listMap);
                } else {
                }

                list.add(properties);
            }
        }
        return list;
    }
}

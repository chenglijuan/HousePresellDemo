package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.util.MyDouble;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：申请-用款-明细 Company：ZhiShuSZ
 */
@Service
public class Tgpf_FundAppropriated_AFDtlRebuild extends RebuilderBase<Tgpf_FundAppropriated_AFDtl> {
    private MyDouble myDouble = MyDouble.getInstance();

    /**
     * 用款申请详情 - 楼幢信息
     * 
     * @param object
     * @return
     */
    @Override
    public Properties getSimpleInfo(Tgpf_FundAppropriated_AFDtl object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        if (object.getMainTable() != null) {
            Tgpf_FundAppropriated_AF tgpf_fundAppropriated_af = object.getMainTable();
            properties.put("eCode", tgpf_fundAppropriated_af.geteCode());
            if (tgpf_fundAppropriated_af.getProject() != null) {
                properties.put("eCode", tgpf_fundAppropriated_af.geteCode());
            }
            properties.put("theNameOfProject", tgpf_fundAppropriated_af.getProject().getTheName());
        }
        properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
        if (object.getBuilding() != null) {
            properties.put("buildingId", object.getBuilding().getTableId());
            properties.put("eCodeOfBuilding", object.getBuilding().geteCode());
            properties.put("eCodeFromConstruction", object.getBuilding().geteCodeFromConstruction());
            properties.put("eCodeFromPublicSecurity", object.getBuilding().geteCodeFromPublicSecurity());
        }

        if (object.getBankAccountSupervised() != null) {
            Tgpj_BankAccountSupervised tgpj_bankAccountSupervised = object.getBankAccountSupervised();
            properties.put("bankAccountSupervisedId", tgpj_bankAccountSupervised.getTableId());
            properties.put("supervisedBankAccount", tgpj_bankAccountSupervised.getTheAccount());
            /*
             * xsz by time 2018-12-26 14:29:16
             * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段
             * ----start----
             */
            // if(tgpj_bankAccountSupervised.getBankBranch()!=null)
            // {
            // properties.put("theNameOfBank", tgpj_bankAccountSupervised.getBankBranch().getTheName());
            // }
            properties.put("theNameOfBank", tgpj_bankAccountSupervised.getTheNameOfBank());
            /*
             * xsz by time 2018-12-26 14:29:16
             * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段
             * ----end----
             */
        }

        if (object.getMainTable() != null) {
            properties.put("theNameOfDevelopCompany", object.getMainTable().getTheNameOfDevelopCompany());
        }

        properties.put("allocableAmount", myDouble.pointTOThousandths(object.getAllocableAmount()));// 当前可划拨金额
        properties.put("newAllocableAmount", object.getAllocableAmount());// 当前可划拨金额
        properties.put("appliedAmount", myDouble.pointTOThousandths(object.getAppliedAmount()));// 本次划款申请金额
        properties.put("appliedAmountOld", object.getAppliedAmount());// 本次划款申请金额(未转化)
        properties.put("escrowStandard", object.getEscrowStandard());// 托管标准
        properties.put("orgLimitedAmount", myDouble.pointTOThousandths(object.getOrgLimitedAmount()));// 初始受限额度（元）
        properties.put("currentFigureProgress", object.getCurrentFigureProgress());// 当前形象进度
        properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());// 当前受限比例（%）
        properties.put("currentLimitedAmount", myDouble.pointTOThousandths2(object.getCurrentLimitedAmount()));// 当前受限额度（元）
        properties.put("totalAccountAmount", myDouble.pointTOThousandths(object.getTotalAccountAmount()));// 总入账金额（元）
        properties.put("appliedPayoutAmount", myDouble.pointTOThousandths(object.getAppliedPayoutAmount()));// 已申请拨付金额（元）
        properties.put("currentEscrowFund", myDouble.pointTOThousandths(object.getCurrentEscrowFund()));// 当前托管余额（元）
        properties.put("refundAmount", myDouble.pointTOThousandths(object.getRefundAmount()));// 退房退款金额（元）
        properties.put("actualReleaseAmount", myDouble.pointTOThousandths(object.getActualReleaseAmount()));// 实际可替代保证额度（元）
        properties.put("payoutState", object.getPayoutState());// 拨付状态

        properties.put("cashLimitedAmount", myDouble.pointTOThousandths2(null == object.getCashLimitedAmount()?0.00:object.getCashLimitedAmount()));// 现金受限额度（元）
        properties.put("effectiveLimitedAmount", myDouble.pointTOThousandths2(object.getEffectiveLimitedAmount()));// 有效受限额度（元）

        return properties;
    }

    @Override
    public Properties getDetail(Tgpf_FundAppropriated_AFDtl object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();
        return properties;
    }

    @SuppressWarnings("rawtypes")
    public List getDetailForAdmin(List<Tgpf_FundAppropriated_AFDtl> tgpf_FundAppropriated_AFDtlList) {
        List<Properties> list = new ArrayList<Properties>();
        if (tgpf_FundAppropriated_AFDtlList != null) {
            for (Tgpf_FundAppropriated_AFDtl object : tgpf_FundAppropriated_AFDtlList) {
                Properties properties = new MyProperties();

                properties.put("theState", object.getTheState());
                properties.put("busiState", object.getBusiState());
                properties.put("eCode", object.geteCode());
                properties.put("userStart", object.getUserStart());
                properties.put("userStartId", object.getUserStart().getTableId());
                properties.put("createTimeStamp", object.getCreateTimeStamp());
                properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
                properties.put("userRecord", object.getUserRecord());
                properties.put("userRecordId", object.getUserRecord().getTableId());
                properties.put("recordTimeStamp", object.getRecordTimeStamp());
                properties.put("building", object.getBuilding());
                properties.put("buildingId", object.getBuilding().getTableId());
                properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
                properties.put("mainTable", object.getMainTable());
                properties.put("mainTableId", object.getMainTable().getTableId());
                properties.put("bankAccountSupervised", object.getBankAccountSupervised());
                properties.put("bankAccountSupervisedId", object.getBankAccountSupervised().getTableId());
                properties.put("supervisedBankAccount", object.getSupervisedBankAccount());
                properties.put("allocableAmount", object.getAllocableAmount());
                properties.put("appliedAmount", object.getAppliedAmount());
                properties.put("escrowStandard", object.getEscrowStandard());
                properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
                properties.put("currentFigureProgress", object.getCurrentFigureProgress());
                properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
                properties.put("currentLimitedAmount", object.getCurrentLimitedAmount());
                properties.put("totalAccountAmount", object.getTotalAccountAmount());
                properties.put("appliedPayoutAmount", object.getAppliedPayoutAmount());
                properties.put("currentEscrowFund", object.getCurrentEscrowFund());
                properties.put("refundAmount", object.getRefundAmount());
                properties.put("payoutState", object.getPayoutState());
                properties.put("cashLimitedAmount", object.getCashLimitedAmount());// 现金受限额度（元）
                properties.put("effectiveLimitedAmount", object.getEffectiveLimitedAmount());// 有效受限额度（元）

                list.add(properties);
            }
        }
        return list;
    }

    /**
     * 资金拨付详情 - 楼幢信息
     * 
     * @param tgpf_FundAppropriated_AFDtlList
     * @return
     */
    public List getFundAppropriated_BuildingList(List<Tgpf_FundAppropriated_AFDtl> tgpf_FundAppropriated_AFDtlList) {
        List<Properties> list = new ArrayList<Properties>();
        if (tgpf_FundAppropriated_AFDtlList != null) {
            for (Tgpf_FundAppropriated_AFDtl object : tgpf_FundAppropriated_AFDtlList) {
                Properties properties = new MyProperties();

                properties.put("tableId", object.getTableId());
                properties.put("eCode", object.geteCode());
                if (object.getBuilding() != null) {
                    Empj_BuildingInfo buildingInfo = object.getBuilding();
                    properties.put("eCodeFromConstruction", buildingInfo.geteCodeFromConstruction());
                    properties.put("eCodeFromPublicSecurity", buildingInfo.geteCodeFromPublicSecurity());
                }
                properties.put("escrowStandard", object.getEscrowStandard());
                properties.put("orgLimitedAmount", object.getOrgLimitedAmount());
                properties.put("currentFigureProgress", object.getCurrentFigureProgress());
                properties.put("currentLimitedRatio", object.getCurrentLimitedRatio());
                properties.put("totalAccountAmount", object.getTotalAccountAmount());
                properties.put("payoutState", object.getPayoutState());
                list.add(properties);
            }
        }
        return list;
    }
}

package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlanDetail;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;

import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：资金统筹 Company：ZhiShuSZ
 */
@Service
public class Tgpf_FundOverallPlanRebuild extends RebuilderBase<Tgpf_FundOverallPlan> {
    private MyDatetime myDatetime = MyDatetime.getInstance();
    private MyDouble myDouble = MyDouble.getInstance();

    @Override
    public Properties getSimpleInfo(Tgpf_FundOverallPlan object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        // 列表页面
        properties.put("tableId", object.getTableId());
        properties.put("eCode", object.geteCode());
        properties.put("fundOverallPlanDate", object.getFundOverallPlanDate());
        if (object.getUserUpdate() != null) {
            properties.put("coordinatingPeo", object.getUserUpdate().getTheName());
        }
        properties.put("busiState", object.getBusiState());
        properties.put("approvalState", object.getApprovalState());

        properties.put("applyType", StrUtil.isBlank(object.getApplyType()) ? "0" : object.getApplyType());
        
        return properties;
    }

    @Override
    public Properties getDetail(Tgpf_FundOverallPlan object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        // 详情页面
        properties.put("tableId", object.getTableId());
        properties.put("eCode", object.geteCode());
        properties.put("busiState", object.getBusiState());

        properties.put("nowDateStr", myDatetime.dateToString(System.currentTimeMillis(), "yyyy-MM-dd"));
        properties.put("fundOverallPlanDate", object.getFundOverallPlanDate());

        if (object.getUserStart() != null) {
            properties.put("userStart", object.getUserStart().getTheName());
        }
        properties.put("approvalState", object.getApprovalState());
        return properties;
    }

    /**
     * 用款申请汇总信息列表
     * 
     * @param fundOverallPlanDetailList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List getFundOverallPlanDetailList(List<Tgpf_FundOverallPlanDetail> fundOverallPlanDetailList) {
        List<Properties> list = new ArrayList<Properties>();
        if (fundOverallPlanDetailList != null) {
            for (Tgpf_FundOverallPlanDetail object : fundOverallPlanDetailList) {
                if (object == null)
                    continue;

                Properties properties = new MyProperties();

                if (object.getBankAccountSupervised() != null) {
                    properties.put("supervisedBankAccountId", object.getBankAccountSupervised().getTableId());
                }
                if (object.getMainTable() != null) {
                    properties.put("mainTableId", object.getMainTable().getTableId()); // 用款申请主表id
                }
                properties.put("theNameOfProject", object.getTheNameOfProject());
                properties.put("theNameOfBankBranch", object.getTheNameOfBankBranch());
                properties.put("supervisedBankAccount", object.getSupervisedBankAccount());
                properties.put("appliedAmount", myDouble.pointTOThousandths(object.getAppliedAmount()));

                list.add(properties);
            }
        }
        return list;
    }
}

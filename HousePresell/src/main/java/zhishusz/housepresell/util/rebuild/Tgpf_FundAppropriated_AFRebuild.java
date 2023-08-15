package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：申请-用款-主表 Company：ZhiShuSZ
 */
@Service
public class Tgpf_FundAppropriated_AFRebuild extends RebuilderBase<Tgpf_FundAppropriated_AF> {
    private MyDatetime myDatetime = MyDatetime.getInstance();
    @Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
    private MyDouble myDouble = MyDouble.getInstance();

    @SuppressWarnings("unchecked")
    @Override
    public Properties getSimpleInfo(Tgpf_FundAppropriated_AF object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        properties.put("eCode", object.geteCode());
        properties.put("createTimeStamp", myDatetime.dateToSimpleString(object.getCreateTimeStamp()));
        properties.put("totalApplyAmount", myDouble.pointTOThousandths(object.getTotalApplyAmount()));
        properties.put("applyState", object.getApplyState());
        properties.put("applyDate", object.getApplyDate());
        properties.put("approvalState", object.getApprovalState());

        if (object.getProject() != null) {
            properties.put("projectId", object.getProject().getTableId());
            properties.put("theNameOfProject", object.getProject().getTheName());
        }
        if(object.getProject() != null ){
            if("1".equals(object.getPayState())){
                properties.put("paystate", "受控项目");
            }else{
                properties.put("paystate","");
            }
        }
        if (object.getFundOverallPlan() != null) {
            Tgpf_FundOverallPlan tgpf_fundOverallPlan = object.getFundOverallPlan();
            properties.put("fundOverallPlanDate",
                myDatetime.dateToSimpleString(tgpf_fundOverallPlan.getLastUpdateTimeStamp()));
        }

        properties.put("applyType", null == object.getApplyType() ? "0" : object.getApplyType());

        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("06120301");
        afModel.setSourceId(object.getTableId());
        afModel.setOrderBy("createTimeStamp");
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao
            .findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
        if (!afList.isEmpty()) {
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = afList.get(0);
            properties.put("applyDate", myDatetime.dateToSimpleString(sm_ApprovalProcess_AF.getStartTimeStamp()));// 最后修改日期

            properties.put("afId", sm_ApprovalProcess_AF.getTableId()); // 申请单id
            properties.put("workflowId", sm_ApprovalProcess_AF.getCurrentIndex());// 当前结点Id
            properties.put("busiType", sm_ApprovalProcess_AF.getBusiType()); // 业务类型
            properties.put("busiCode", sm_ApprovalProcess_AF.getBusiCode()); // 业务编码
        }

        // 查找申请单
        /*Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
        sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
        sm_approvalProcess_afForm.setBusiCode(S_BusiCode.busiCode7);
        sm_approvalProcess_afForm.setSourceId(object.getTableId());
        Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));
        
        if(sm_approvalProcess_af !=null)
        {
        	properties.put("afId",sm_approvalProcess_af.getTableId()); // 申请单id
        	properties.put("workflowId",sm_approvalProcess_af.getCurrentIndex());//当前结点Id
        	properties.put("busiType",sm_approvalProcess_af.getBusiType());  //业务类型
        	properties.put("busiCode",sm_approvalProcess_af.getBusiCode()); //业务编码
        }*/

        return properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Properties getDetail(Tgpf_FundAppropriated_AF object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());
        properties.put("eCode", object.geteCode());
        properties.put("theNameOfProject", object.getTheNameOfProject());
        properties.put("totalApplyAmount", myDouble.pointTOThousandths(object.getTotalApplyAmount()));
        properties.put("createTimeStamp", myDatetime.dateToSimpleString(object.getCreateTimeStamp()));
        properties.put("remark", object.getRemark());
        properties.put("applyState", object.getApplyState());
        properties.put("applyDate", object.getApplyDate());
        properties.put("approvalState", object.getApprovalState());

        if (object.getDevelopCompany() != null) {
            properties.put("developCompanyId", object.getDevelopCompany().getTableId());
            properties.put("theNameOfDevelopCompany", object.getDevelopCompany().getTheName());
        }
        if (object.getProject() != null) {
            properties.put("projectId", object.getProject().getTableId());
        }
        if (object.getUserUpdate() != null) {
            properties.put("userUpdate", object.getUserUpdate().getTheName());
        }
        properties.put("lastUpdateTimeStamp", myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()));
        if (object.getUserRecord() != null) {
            properties.put("userRecord", object.getUserRecord().getTheName());
        }
        properties.put("recordTimeStamp", myDatetime.dateToSimpleString(object.getRecordTimeStamp()));
        properties.put("applyType", null == object.getApplyType() ? "0" : object.getApplyType());
        
        properties.put("paymentBondId", null == object.getPaymentBondId() ? 0L : object.getPaymentBondId());
        properties.put("paymentBondCode", null == object.getPaymentBondCode() ? "-" : object.getPaymentBondCode());

        /**
         * xsz by time 2019-4-30 16:22:49 操作时间取申请单的提交时间
         * 
         */
        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("06120301");
        afModel.setSourceId(object.getTableId());
        afModel.setOrderBy("createTimeStamp");
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao
            .findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
        if (null == afList || afList.size() == 0) {
            properties.put("lastUpdateTimeStamp", "-");
        } else {
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = afList.get(0);
            properties.put("lastUpdateTimeStamp",
                myDatetime.dateToSimpleString(sm_ApprovalProcess_AF.getStartTimeStamp()));// 最后修改日期
        }

        return properties;
    }

    @SuppressWarnings("rawtypes")
    public List getDetailForAdmin(List<Tgpf_FundAppropriated_AF> tgpf_FundAppropriated_AFList) {
        List<Properties> list = new ArrayList<Properties>();
        if (tgpf_FundAppropriated_AFList != null) {
            for (Tgpf_FundAppropriated_AF object : tgpf_FundAppropriated_AFList) {
                Properties properties = new MyProperties();

                properties.put("tableId", object.getTableId());
                properties.put("eCode", object.geteCode());
                properties.put("developCompany", object.getDevelopCompany());
                properties.put("developCompanyId", object.getDevelopCompany().getTableId());
                properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
                properties.put("project", object.getProject());
                properties.put("projectId", object.getProject().getTableId());
                properties.put("theNameOfProject", object.getTheNameOfProject());
                properties.put("eCodeOfProject", object.geteCodeOfProject());
                properties.put("applyState", object.getApplyState());
                properties.put("applyType", null == object.getApplyType() ? "0" : object.getApplyType());

                list.add(properties);
            }
        }
        return list;
    }
}

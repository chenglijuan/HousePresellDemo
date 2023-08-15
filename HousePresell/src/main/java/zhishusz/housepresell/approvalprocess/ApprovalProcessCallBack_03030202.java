package zhishusz.housepresell.approvalprocess;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_DTLForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_DTLDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_DTL;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 工程进度巡查： 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03030202 implements IApprovalProcessCallback {
    @Autowired
    private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
    @Autowired
    private Empj_ProjProgForcast_DTLDao empj_ProjProgForcast_DTLDao;
    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;

    @Autowired
    private CommonService commonService;

    @SuppressWarnings("unchecked")
    public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
        Properties properties = new MyProperties();

        try {
            String workflowEcode = approvalProcessWorkflow.geteCode();

            // 获取正在处理的申请单
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

            // 获取正在处理的申请单所属的流程配置
            Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
            String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

            // 获取正在审批的楼幢
            Long sourceId = sm_ApprovalProcess_AF.getSourceId();
            Empj_ProjProgForcast_AF progForcast = empj_ProjProgForcast_AFDao.findById(sourceId);

            if (progForcast == null) {
                return MyBackInfo.fail(properties, "审批的单据不存在");
            }

            switch (approvalProcessWork) {
                case "03030202001_ZS":
                    if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
                        && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
                        progForcast.setBusiState("已备案");
                        progForcast.setApprovalState("已完结");
                        progForcast.setRecordTimeStamp(System.currentTimeMillis());
                        progForcast.setUserRecord(baseForm.getUser());

                       

                        Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
                        dtlModel.setTheState(S_TheState.Normal);
                        dtlModel.setAfCode(progForcast.geteCode());
                        dtlModel.setAfEntity(progForcast);
                        List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList =
                            empj_ProjProgForcast_DTLDao.findByPage(empj_ProjProgForcast_DTLDao
                                .getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));
                        
                        //更新有效的楼幢信息
                        dtlModel.setHasAchieve("1");
                        Integer size = empj_ProjProgForcast_DTLDao.findByPage_Size(empj_ProjProgForcast_DTLDao.getQuery_Size(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));
                        progForcast.setBuildCount(size);
                        empj_ProjProgForcast_AFDao.update(progForcast);
                        
                        
                        CommonForm commonModel;
                        for (Empj_ProjProgForcast_DTL dtl : empj_ProjProgForcast_DTLList) {
                            dtl.setBusiState("已备案");
                            dtl.setApprovalState("已完结");
                            dtl.setRecordTimeStamp(System.currentTimeMillis());
                            dtl.setUserRecord(baseForm.getUser());
                            empj_ProjProgForcast_DTLDao.update(dtl);

                            /*
                             * 更新预测时间
                             */
                            commonModel = new CommonForm();
                            commonModel.setForcastTime(progForcast.getForcastTime());
                            commonModel.setBuildId(dtl.getBuildInfo().getTableId());
                            commonModel.setNowBuildNum(
                                Integer.valueOf(null == dtl.getBuildProgress() ? "0" : dtl.getBuildProgress()));
                            commonModel.setBuildCountNum(
                                Integer.parseInt(new DecimalFormat("0").format(dtl.getFloorUpNumber())));
                            commonModel.setUser(baseForm.getUser());
                            commonModel.setNowNode(dtl.getNowNode());

                            commonModel.setBuildProgressType(
                                StrUtil.isBlank(dtl.getBuildProgressType()) ? "" : dtl.getBuildProgressType());
                            commonModel.setBuildProgress(
                                StrUtil.isBlank(dtl.getBuildProgress()) ? "" : dtl.getBuildProgress());
                            commonService.ProjProgForecastExecute(commonModel);
                            
                            
                            /**
                             * xsz by 2020-11-4 14:48:48 
                             */

                            // 查询开关
                            Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
                            baseParameterForm0.setTheState(S_TheState.Normal);
                            baseParameterForm0.setTheValue("710000");
                            baseParameterForm0.setParametertype("71");
                            Sm_BaseParameter baseParameter0 =
                                sm_BaseParameterDao.findOneByQuery_T(sm_BaseParameterDao
                                    .getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));
                            
                            //TODO 暂不对接
//                            baseParameter0 = null;

                            if (null != baseParameter0 && "1".equals(baseParameter0.getTheName())) {
                                    
                                commonService.pushProjProgForcastPhoto(progForcast, dtl);
                                
                            }

                            /**
                             * xsz by 2020-11-4 14:48:48
                             */

                        }
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

}

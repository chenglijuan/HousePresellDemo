package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.service.Sm_BusiState_LogAddService;
import zhishusz.housepresell.service.Tgpj_BuildingAccountLimitedUpdateService;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 受限额度变更：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03030101 implements IApprovalProcessCallback {
    @Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
    @Autowired
    private Gson gson;
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Sm_BusiState_LogAddService logAddService;
    @Autowired
    private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
    @Autowired
    private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao verAfDtlDao;
    private String busiCode = "03030101";
    @Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
    @Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF

    public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
        Properties properties = new MyProperties();
        try {
            String workflowEcode = approvalProcessWorkflow.geteCode();//节点编码
            // 获取正在处理的申请单
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();
            // 获取正在处理的申请单所属的流程配置
            Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
            String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;//流程编码加节点编码
            // 获取正在审批的楼幢
            Long bldLimitAmountId = sm_ApprovalProcess_AF.getSourceId();
            Empj_BldLimitAmount_AF limitAmountVer = empj_BldLimitAmount_AFDao.findById(bldLimitAmountId);
            if (limitAmountVer == null) {
                return MyBackInfo.fail(properties, "审批的受限额度不存在");
            }
            Empj_BldLimitAmount_AF limitAmountVerOld = ObjectCopier.copy(limitAmountVer);
            switch (approvalProcessWork) {
                case "03030101001_1":

                    break;
                case "03030101001_ZS":
                    finalJudgement(approvalProcessWorkflow, baseForm, properties, sm_ApprovalProcess_AF, bldLimitAmountId, limitAmountVer, limitAmountVerOld);
                    
                    /*
					 * xsz by time 提交结束后调用生成PDF方法
					 * 并将生成PDF后上传值OSS路径返回给前端
					 * 
					 * 参数：
					 * sourceBusiCode：业务编码
					 * sourceId：单据ID
					 * 
					 * xsz by time 2019-1-21 08:54:03
					 * 首先判断提交人是否具有签章
					 * 
					 */
                    /*String isSignature = sm_ApprovalProcess_AF.getUserStart().getIsSignature();
                    if(null != isSignature && "1".equals(isSignature))
                    {
                    	if(null!=baseForm.getUser().getIsSignature()&&"1".equals(baseForm.getUser().getIsSignature()))
    					{
    						
    						ExportPdfForm pdfModel = new ExportPdfForm();
    						pdfModel.setSourceBusiCode(busiCode);
    						pdfModel.setSourceId(String.valueOf(sm_ApprovalProcess_AF.getSourceId()));
    						Properties executeProperties = exportPdfByWordService.execute(pdfModel);
    						String pdfUrl = (String) executeProperties.get("pdfUrl");
    						
    						Map<String, String> signatureMap = new HashMap<>();
    						
    						signatureMap.put("signaturePath", pdfUrl);
    						//TODO 此配置后期做成配置
    						signatureMap.put("signatureKeyword", "项目部负责人意见：");
    						signatureMap.put("ukeyNumber", baseForm.getUser().getUkeyNumber());
    						
    						properties.put("signatureMap", signatureMap);
    						
    					}
                    }*/
					
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

    private void finalJudgement(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm, Properties properties, Sm_ApprovalProcess_AF sm_ApprovalProcess_AF, Long bldLimitAmountId, Empj_BldLimitAmount_AF limitAmountVer, Empj_BldLimitAmount_AF limitAmountVerOld) {
        if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
//            String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
//            if (jsonStr != null && jsonStr.length() > 0) {
//                Empj_BldLimitAmount_AFForm limitAmountVerForm = gson.fromJson(jsonStr, Empj_BldLimitAmount_AFForm.class);
        	Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = limitAmountVer.getExpectFigureProgress();
//                Long expectFigureProgressId = limitAmountVerForm.getExpectFigureProgressId();
//                Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = verAfDtlDao.findById(expectFigureProgressId);
                if (expectFigureProgress == null) {
                    System.out.println("expectFigureProgress is null");
                } else {
                    limitAmountVer.setExpectFigureProgress(expectFigureProgress);
                    limitAmountVer.setExpectLimitedRatio(expectFigureProgress.getLimitedAmount());
                    Double limitedAmount = expectFigureProgress.getLimitedAmount();
                    double expectLimitedAmount = limitedAmount * limitAmountVer.getOrgLimitedAmount() / 100.0;
                    limitAmountVer.setExpectLimitedAmount(expectLimitedAmount);
                    Double cashLimitedAmount = limitAmountVer.getCashLimitedAmount();
                    limitAmountVer.setExpectEffectLimitedAmount(expectLimitedAmount);
                    /*if (expectLimitedAmount < cashLimitedAmount) {
                        limitAmountVer.setExpectEffectLimitedAmount(expectLimitedAmount);
                    } else {
                        limitAmountVer.setExpectEffectLimitedAmount(limitAmountVerForm.getCashLimitedAmount());
                    }*/
                }
                limitAmountVer.setApprovalState(S_ApprovalState.Completed);
                limitAmountVer.setUserUpdate(sm_ApprovalProcess_AF.getUserStart());
                limitAmountVer.setLastUpdateTimeStamp(System.currentTimeMillis());
                limitAmountVer.setUserRecord(baseForm.getUser());
                limitAmountVer.setRecordTimeStamp(System.currentTimeMillis());
//                limitAmountVer.setRemark(limitAmountVerForm.getRemark());
                limitAmountVer.setBusiState(S_BusiState.HaveRecord);
                empj_BldLimitAmount_AFDao.save(limitAmountVer);
//                limitAmountVerForm.setBusiType(busiCode);
//                sm_AttachmentBatchAddService.execute(limitAmountVerForm, bldLimitAmountId);
                Empj_BldLimitAmount_AF limitAmountVerNew = ObjectCopier.copy(limitAmountVer);
                if (baseForm != null) {
                    baseForm.setUser(sm_ApprovalProcess_AF.getUserStart());
                    logAddService.addLog(baseForm, bldLimitAmountId, limitAmountVerOld, limitAmountVerNew);
                }
//							//回显数据到楼幢账户表 开始
                Empj_BuildingInfo buildingInfo = limitAmountVer.getBuilding();
                buildingAccountLogUtil.callBackChange(buildingInfo, properties, limitAmountVer.getTableId(), busiCode, (tgpj_BuildingAccountLog, logForm) -> {
                    tgpj_BuildingAccountLog.setCurrentFigureProgress(expectFigureProgress.getStageName());
                    tgpj_BuildingAccountLog.setCurrentLimitedRatio(expectFigureProgress.getLimitedAmount());
                });
            }
//        }
    }
}

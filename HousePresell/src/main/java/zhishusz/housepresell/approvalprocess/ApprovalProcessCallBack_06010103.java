package zhishusz.housepresell.approvalprocess;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 三方协议版本管理
 * 审批过后-业务逻辑处理
 *
 */
@Transactional
public class ApprovalProcessCallBack_06010103 implements IApprovalProcessCallback
{

	private static final String BUSI_CODE = "06010103";
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;

	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();
			// String workflowName = approvalProcessWorkflow.getTheName();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF approvalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = approvalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;
			
			// 获取正在审批的三方协议版本管理
			Long sourceId = approvalProcess_AF.getSourceId();

			if (null == sourceId || sourceId < 1)
			{
				return MyBackInfo.fail(properties, "获取的申请单主键为空");
			}

			Tgxy_TripleAgreementVerMng TripleAgreementVerMng;

			switch (approvalProcessWork)
			{
			// 审批节点名称
			case "1":

				TripleAgreementVerMng = tgxy_TripleAgreementVerMngDao.findById(sourceId);
				if (null == TripleAgreementVerMng)
				{
					return MyBackInfo.fail(properties, "该三方协议版本管理已处于失效状态，请刷新后重试");
				}
				/*
				 * 具体的业务逻辑操作
				 */
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有进行处理的回调");
				break;

//			case "06010103001_4":
			case "06010103001_ZS":

				if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
					TripleAgreementVerMng = tgxy_TripleAgreementVerMngDao.findById(sourceId);
					if (null == TripleAgreementVerMng)
					{
						return MyBackInfo.fail(properties, "该三方协议版本管理已处于失效状态，请刷新后重试");
					}
					/*
					 * 具体的业务逻辑操作:
					 * 三方协议状态置为已备案状态
					 */	
					Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm=new Tgxy_TripleAgreementVerMngForm();
					tgxy_TripleAgreementVerMngForm.setTheType(TripleAgreementVerMng.getTheType());
					List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicH1(), tgxy_TripleAgreementVerMngForm).getResultList();
	                if(tgxy_TripleAgreementVerMnglist.size()>1){
	                	Tgxy_TripleAgreementVerMng tgxy_TripleAgreementVerMng=new Tgxy_TripleAgreementVerMng();
	                	tgxy_TripleAgreementVerMng=tgxy_TripleAgreementVerMnglist.get(1);
	                	if(!tgxy_TripleAgreementVerMng.getTableId().equals(TripleAgreementVerMng.getTableId())){
	                		tgxy_TripleAgreementVerMng.setDownTimeStamp(TripleAgreementVerMng.getEnableTimeStamp());                	
	                    	tgxy_TripleAgreementVerMngDao.save(tgxy_TripleAgreementVerMng);
	                	}               	
	                }
					TripleAgreementVerMng.setRecordTimeStamp(System.currentTimeMillis());
					//获取审核人
					if(null!= baseForm && baseForm.getUser()!=null){
						TripleAgreementVerMng.setUserRecord(baseForm.getUser());
					}else{
						TripleAgreementVerMng.setUserRecord(null);
					}
					//设置审核的状态
					TripleAgreementVerMng.setApprovalState(S_ApprovalState.Completed);
					TripleAgreementVerMng.setBusiState("已备案");
					
					tgxy_TripleAgreementVerMngDao.save(TripleAgreementVerMng);	
				}
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");

				break;

			default:

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);

		}

		return properties;
	}

}

package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import zhishusz.housepresell.database.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TripleagreementState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.external.service.Tgxy_TripleAgreementDeleteInterfaceService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 贷款三方托管协议签署：
 * 审批过后-业务逻辑处理
 * 2018-10-11 17:20:03
 * 
 * @author ZS_XSZ
 *
 */
@Transactional
public class ApprovalProcessCallBack_06110301 implements IApprovalProcessCallback
{

	private static final String BUSI_CODE = "06110301";
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgxy_TripleAgreementDeleteInterfaceService deleteInterfaceService;

	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		// 获取当前操作人（备案人）
		Sm_User user = baseForm.getUser();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();
			// String workflowName = approvalProcessWorkflow.getTheName();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF approvalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = approvalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的三方协议
			Long sourceId = approvalProcess_AF.getSourceId();

			if (null == sourceId || sourceId < 1)
			{
				return MyBackInfo.fail(properties, "获取的申请单主键为空");
			}

			Tgxy_TripleAgreement tripleAgreement;
			tripleAgreement = tgxy_TripleAgreementDao.findById(sourceId);
			if (null == tripleAgreement)
			{
				return MyBackInfo.fail(properties, "该三方协议已处于失效状态，请刷新后重试");
			}

			// 驳回到发起人，发起人撤回
			if (S_ApprovalState.WaitSubmit.equals(approvalProcess_AF.getBusiState()))
			{
				tripleAgreement.setBusiState(S_TripleagreementState.UnCommit);
				tripleAgreement.setApprovalState(S_ApprovalState.WaitSubmit);

				tgxy_TripleAgreementDao.save(tripleAgreement);
				
				/**
				 * xsz by 2019-10-10 17:19:20
				 * 与档案系统接口对接
				 * BUG#5566 三方推送档案系统
				 * ====================start==================
				 */
				
				Properties execute = deleteInterfaceService.execute(tripleAgreement, "1", baseForm);
				if(execute.isEmpty()|| S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result)))
				{
					return MyBackInfo.fail(properties, "与档案系统对接失败，请稍后重试！");
				}
				
				/**
				 * xsz by 2019-10-10 17:19:26
				 * 与档案系统接口对接
				 * BUG#5566 三方推送档案系统
				 * ====================end==================
				 */
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");
			}

			// 不通过
			if (S_ApprovalState.NoPass.equals(approvalProcess_AF.getBusiState()))
			{
				tripleAgreement.setBusiState(S_TripleagreementState.UnCommit);
				tripleAgreement.setApprovalState(S_ApprovalState.NoPass);

				tgxy_TripleAgreementDao.save(tripleAgreement);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");
			}

			switch (approvalProcessWork)
			{
			// 审批节点名称
			case "1":

				/*
				 * 具体的业务逻辑操作
				 */
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有进行处理的回调");
				break;

			// case "06110301001_4":
			case "06110301001_ZS":

				if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					/*
					 * 具体的业务逻辑操作:
					 * 三方协议状态置为已备案状态
					 */
					tripleAgreement.setTheStateOfTripleAgreement("3");
					// 设置备案时间
					tripleAgreement.setRecordTimeStamp(System.currentTimeMillis());
					// 设置备案人
					tripleAgreement.setUserRecord(user);

					// 审批流程状态-已完结
					tripleAgreement.setApprovalState(S_ApprovalState.Completed);

					tgxy_TripleAgreementDao.save(tripleAgreement);

					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, "操作成功");
				}
				
				break;
			
			case "06110301002_ZS":

				if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					/*
					 * 具体的业务逻辑操作:
					 * 三方协议状态置为已备案状态
					 */
					tripleAgreement.setTheStateOfTripleAgreement("3");
					// 设置备案时间
					tripleAgreement.setRecordTimeStamp(System.currentTimeMillis());
					// 设置备案人
					tripleAgreement.setUserRecord(user);

					// 审批流程状态-已完结
					tripleAgreement.setApprovalState(S_ApprovalState.Completed);

					tgxy_TripleAgreementDao.save(tripleAgreement);

					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, "操作成功");
				}
				
				break;

			default:

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");

				;
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

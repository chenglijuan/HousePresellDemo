package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * 开发企业新增审批：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_020101 implements IApprovalProcessCallback
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_companyInfoDao;

	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm model)
	{
		Properties properties = new Properties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的开发企业
			Long developConpamyId = sm_ApprovalProcess_AF.getSourceId();
			if(developConpamyId == null || developConpamyId < 1)
			{
				return MyBackInfo.fail(properties, "审批的开发企业不存在");
			}
			Emmp_CompanyInfo emmp_companyInfo =  emmp_companyInfoDao.findById(developConpamyId);

			if(emmp_companyInfo == null)
			{
				return MyBackInfo.fail(properties, "审批的开发企业不存在");
			}

			switch (approvalProcessWork)
			{
				case "020101001_ZS":
					if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
					{
						String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
						if (jsonStr != null && jsonStr.length() > 0)
						{
							emmp_companyInfo.setBusiState("已备案");
							emmp_companyInfo.setApprovalState("已完结");

							emmp_companyInfo.setUserRecord(approvalProcessWorkflow.getUserUpdate());
							emmp_companyInfo.setRecordTimeStamp(System.currentTimeMillis());

							emmp_companyInfoDao.save(emmp_companyInfo);
						}
					}break;
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

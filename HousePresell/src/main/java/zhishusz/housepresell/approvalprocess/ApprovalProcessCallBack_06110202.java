package zhishusz.housepresell.approvalprocess;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import com.itextpdf.text.log.SysoCounter;

/**
 * 合作协议版本管理
 * 审批过后-业务逻辑处理
 * 
 */
@Transactional
public class ApprovalProcessCallBack_06110202 implements IApprovalProcessCallback
{

	private static final String BUSI_CODE = "06110202";
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;

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
			
			// 获取正在审批的合作协议版本管理
			Long sourceId = approvalProcess_AF.getSourceId();

			if (null == sourceId || sourceId < 1)
			{
				return MyBackInfo.fail(properties, "获取的申请单主键为空");
			}

			Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng;

			switch (approvalProcessWork)
			{
			// 审批节点名称
			case "1":

				tgxy_CoopAgreementVerMng = tgxy_CoopAgreementVerMngDao.findById(sourceId);
				if (null == tgxy_CoopAgreementVerMng)
				{
					return MyBackInfo.fail(properties, "该合作协议版本管理已处于失效状态，请刷新后重试");
				}
				/*
				 * 具体的业务逻辑操作
				 */
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有进行处理的回调");
				break;

//			case "06110202001_4":
			case "06110202001_ZS":

				tgxy_CoopAgreementVerMng = tgxy_CoopAgreementVerMngDao.findById(sourceId);
				if (null == tgxy_CoopAgreementVerMng)
				{
					return MyBackInfo.fail(properties, "该合作协议版本管理已处于失效状态，请刷新后重试");
				}				
				/*
				 * 具体的业务逻辑操作:
				 * 三方协议状态置为已备案状态
				 */
				Tgxy_CoopAgreementVerMngForm Tgxy_CoopAgreementVerMngForm=new Tgxy_CoopAgreementVerMngForm();
				Tgxy_CoopAgreementVerMngForm.setTheType(tgxy_CoopAgreementVerMng.getTheType());
				List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMnglist=tgxy_CoopAgreementVerMngDao.getQuery(tgxy_CoopAgreementVerMngDao.getBasicH2(), Tgxy_CoopAgreementVerMngForm).getResultList();               
				if(tgxy_CoopAgreementVerMnglist.size()>=2){
					Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng1=new Tgxy_CoopAgreementVerMng();
                	tgxy_CoopAgreementVerMng1=tgxy_CoopAgreementVerMnglist.get(1);
					if(!tgxy_CoopAgreementVerMng1.getTableId().equals(tgxy_CoopAgreementVerMng.getTableId())){
						tgxy_CoopAgreementVerMng1.setDownTimeStamp(tgxy_CoopAgreementVerMng.getEnableTimeStamp()); 
	                	tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng1);
					}               	
                }
				tgxy_CoopAgreementVerMng.setRecordTimeStamp(System.currentTimeMillis());				
				//获取审核人
				if(null!= baseForm && baseForm.getUser()!=null){
					tgxy_CoopAgreementVerMng.setUserRecord(baseForm.getUser());
				}else{
					tgxy_CoopAgreementVerMng.setUserRecord(null);
				}
				//设置审核的状态
				tgxy_CoopAgreementVerMng.setApprovalState(S_ApprovalState.Completed);
				
				tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng);

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");

				break;

			default:

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
				break;
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

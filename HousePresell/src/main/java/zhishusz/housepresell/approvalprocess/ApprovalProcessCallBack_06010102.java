package zhishusz.housepresell.approvalprocess;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/**
 * 受限额度：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_06010102 implements IApprovalProcessCallback
{
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao bldLimitAmountVer_afDao;

	@Autowired
	private Gson gson;

	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();
			System.out.println("sm_ApprovalProcess_AF BusiState is "+sm_ApprovalProcess_AF.getBusiState()+" approvalProcessWorkflow.getBusiState() is "+approvalProcessWorkflow.getBusiState());
			
			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;
			
			// 获取正在审批的楼幢
//			Long empj_BuildingInfoId = sm_ApprovalProcess_AF.getSourceId();
			Long bldLimitAmountVerId = sm_ApprovalProcess_AF.getSourceId();
//			Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(empj_BuildingInfoId);
			Tgpj_BldLimitAmountVer_AF bldLimitAmountVer_AF = bldLimitAmountVer_afDao.findById(bldLimitAmountVerId);

			//			empj_BuildingInfoDao.clear(empj_BuildingInfo);
			if(bldLimitAmountVer_AF == null)
			{
				return MyBackInfo.fail(properties, "审批的受限额度节点版本不存在");
			}

			switch (approvalProcessWork)
			{
				case "06010102001_ZS"://最后一步的名称
					if(S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState()) && S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
					{
						String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
						if(jsonStr != null && jsonStr.length() > 0)
						{


							//设置之前的那个结束时间
							Tgpj_BldLimitAmountVer_AFForm afFormOld = new Tgpj_BldLimitAmountVer_AFForm();
							afFormOld.setTheState(S_TheState.Normal);
							afFormOld.setApprovalState(S_ApprovalState.Completed);
							afFormOld.setBusiState(S_BusiState.HaveRecord);
							afFormOld.setTheType(bldLimitAmountVer_AF.getTheType());
							afFormOld.setOrderBy("beginExpirationDate desc");
							List<Tgpj_BldLimitAmountVer_AF> tgpj_BldLimitAmountVerList = bldLimitAmountVer_afDao.findByPage(
									bldLimitAmountVer_afDao.getQuery(bldLimitAmountVer_afDao.getBasicHQL(), afFormOld));
							if(tgpj_BldLimitAmountVerList.size()>0){
								Tgpj_BldLimitAmountVer_AF oldVersion = tgpj_BldLimitAmountVerList.get(0);
								if (oldVersion != null)
								{
									MyDatetime datetime = MyDatetime.getInstance();

									oldVersion.setEndExpirationDate(bldLimitAmountVer_AF.getBeginExpirationDate()-100);
									bldLimitAmountVer_afDao.save(oldVersion);
								}
							}

							bldLimitAmountVer_AF.setApprovalState(S_ApprovalState.Completed);
							bldLimitAmountVer_AF.setBusiState(S_BusiState.HaveRecord);
							bldLimitAmountVer_AF.setUserUpdate(sm_ApprovalProcess_AF.getUserStart());
							bldLimitAmountVer_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
							bldLimitAmountVer_AF.setUserRecord(baseForm.getUser());
							bldLimitAmountVer_AF.setRecordTimeStamp(System.currentTimeMillis());
							bldLimitAmountVer_afDao.save(bldLimitAmountVer_AF);
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

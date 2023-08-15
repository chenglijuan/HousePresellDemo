package zhishusz.housepresell.approvalprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/**
 * 三方托管协议计量结算：
 * 审批过后-业务逻辑处理
 * 2018-10-11 17:20:03
 * 
 * @author ZS_XSZ
 *
 */
@Transactional
public class ApprovalProcessCallBack_06110304 implements IApprovalProcessCallback
{

	private static final String BUSI_CODE = "06110304";
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF

	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			Sm_User user = baseForm.getUser();
			
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
			
			Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(sourceId);
			if(tgxy_CoopAgreementSettle == null)
			{
				return MyBackInfo.fail(properties, "该记录不存在！");
			}
			
			// 驳回到发起人，发起人撤回
			if (S_ApprovalState.WaitSubmit.equals(approvalProcess_AF.getBusiState()))
			{
				tgxy_CoopAgreementSettle.setBusiState(S_BusiState.NoRecord);
				tgxy_CoopAgreementSettle.setApprovalState(S_ApprovalState.WaitSubmit);
				tgxy_CoopAgreementSettle.setSettlementState(0);

				tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");
			}

			// 不通过
			if (S_ApprovalState.NoPass.equals(approvalProcess_AF.getBusiState()))
			{
				/**
				 * xsz by time 2019-7-25 15:43:49
				 * 改用存储过程调用处理
				 * ============start================
				 */
				/*tgxy_CoopAgreementSettle.setBusiState(S_BusiState.NoRecord);
				tgxy_CoopAgreementSettle.setApprovalState(S_ApprovalState.NoPass);
				tgxy_CoopAgreementSettle.setSettlementState(0);
				
				Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
				tgxy_CoopAgreementSettleDtlForm.setMainTable(tgxy_CoopAgreementSettle);
				
				// 子表
				List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
				
				Integer totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));
				
				if(totalCount > 0)
				{
					tgxy_CoopAgreementSettleDtlList = tgxy_CoopAgreementSettleDtlDao.findByPage(tgxy_CoopAgreementSettleDtlDao.getQuery(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));	
					
					for(Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl : tgxy_CoopAgreementSettleDtlList)
					{
						Tgxy_TripleAgreement tripleAgreement = tgxy_CoopAgreementSettleDtl.getTgxy_TripleAgreement();
						
						Empj_HouseInfo house = tripleAgreement.getHouse();
						
						house.setSettlementStateOfTripleAgreement(0);
						
						empj_HouseInfoDao.save(house);
						
						tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Deleted);
						
						tgxy_CoopAgreementSettleDtlDao.save(tgxy_CoopAgreementSettleDtl);
					}				
				}
				tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
				*/
				Map<String, Object> map = tgxy_CoopAgreementSettleDao.QUERY_COOPAGREEMENT_ACTION(sourceId, baseForm.getUserId(), "1");
				/**
				 * xsz by time 2019-7-25 15:43:49
				 * 改用存储过程调用处理
				 * ============end================
				 */
				if(S_NormalFlag.fail.equals(map.get("sign")))
				{
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, map.get("info"));
				}
				else
				{
					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, "操作成功");
				}
				
			}

			switch (approvalProcessWork)
			{
			// 审批节点名称
			case "1":

				break;

			// case "06110304_4":
			case "06110304001_ZS":

				if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					/**
					 * xsz by time 2019-7-25 15:43:49
					 * 改用存储过程调用处理
					 * ============start================
					 */
					/*tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
					tgxy_CoopAgreementSettle.setBusiState(S_BusiState.HaveRecord);
					tgxy_CoopAgreementSettle.setUserUpdate(user);
					tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
					tgxy_CoopAgreementSettle.setUserRecord(user);
					tgxy_CoopAgreementSettle.setRecordTimeStamp(System.currentTimeMillis());
					tgxy_CoopAgreementSettle
							.setSignTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
					tgxy_CoopAgreementSettle.setSettlementState(2);

					tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);

					// 查询子表
					Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
					tgxy_CoopAgreementSettleDtlForm.setMainTable(tgxy_CoopAgreementSettle);

					Integer totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(
							tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(),
									tgxy_CoopAgreementSettleDtlForm));

					// 子表
					List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
					if (totalCount > 0)
					{
						tgxy_CoopAgreementSettleDtlList = tgxy_CoopAgreementSettleDtlDao.findByPage(
								tgxy_CoopAgreementSettleDtlDao.getQuery(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(),
										tgxy_CoopAgreementSettleDtlForm));
						for (Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl : tgxy_CoopAgreementSettleDtlList)
						{

							Empj_HouseInfo empj_HouseInfo = tgxy_CoopAgreementSettleDtl.getHouseInfo();

							if (baseForm != null)
							{
								empj_HouseInfo.setUserUpdate(baseForm.getUser());
							}
							empj_HouseInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
							empj_HouseInfo.setSettlementStateOfTripleAgreement(2);

							empj_HouseInfoDao.save(empj_HouseInfo);
						}
					}
					else
					{
						tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
					}*/
					Map<String, Object> map = tgxy_CoopAgreementSettleDao.QUERY_COOPAGREEMENT_ACTION(sourceId, baseForm.getUserId(), "0");
					/**
					 * xsz by time 2019-7-25 15:43:49
					 * 改用存储过程调用处理
					 * ============end================
					 */
					if(S_NormalFlag.fail.equals(map.get("sign")))
					{
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, map.get("info"));
					}
					else
					{
						properties.put(S_NormalFlag.result, S_NormalFlag.success);
						properties.put(S_NormalFlag.info, "操作成功");
					}
					/**
					 * xsz by time 2019-7-25 15:43:49
					 * 改用存储过程调用处理
					 * ============end================
					 */
				}
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");
				
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
				 */
				String isSignature = approvalProcess_AF.getUserStart().getIsSignature();
                if(null != isSignature && "1".equals(isSignature))
                {
                	if(null!=user.getIsSignature()&&"1".equals(user.getIsSignature()))
    				{
    					
    					ExportPdfForm pdfModel = new ExportPdfForm();
    					pdfModel.setSourceBusiCode(BUSI_CODE);
    					pdfModel.setSourceId(String.valueOf(sourceId));
    					Properties executeProperties = exportPdfByWordService.execute(pdfModel);
    					String pdfUrl = (String) executeProperties.get("pdfUrl");
    					
    					Map<String, String> signatureMap = new HashMap<>();
    					
    					signatureMap.put("signaturePath", pdfUrl);
    					//TODO 此配置后期做成配置
    					signatureMap.put("signatureKeyword", "项目部负责人：");
    					signatureMap.put("ukeyNumber", user.getUkeyNumber());
    					
    					properties.put("signatureMap", signatureMap);
    					
    				}
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

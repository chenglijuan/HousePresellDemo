package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：审批流-申请单-公共方法
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_ApprovalProcess_AFCommonDetailService
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;// 申请单信息
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;// 审批流程信息

	public Properties execute(Sm_ApprovalProcess_AFForm model)
	{
		Properties properties = new MyProperties();

		Long sm_ApprovalProcess_AFId = model.getTableId();
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = (Sm_ApprovalProcess_AF) sm_ApprovalProcess_AFDao
				.findById(sm_ApprovalProcess_AFId);
		if (sm_ApprovalProcess_AF == null || S_TheState.Deleted.equals(sm_ApprovalProcess_AF.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Sm_ApprovalProcess_AF(Id:" + sm_ApprovalProcess_AFId + ")'不存在");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("sm_ApprovalProcess_AF", sm_ApprovalProcess_AF);

		return properties;
	}

	/**
	 * 根据单据tableId和业务编码查询申请单状态
	 * 
	 * @param sourceId
	 * @param busiCode
	 * @return
	 * 		0-不存在申请单信息（未提交或者被撤回）
	 *         1-申请单已完结（审批完成）
	 *         2-申请单待提交
	 *         3-申请单不通过
	 */
	@SuppressWarnings("unchecked")
	public String getSm_ApprovalProcess_Workflow(Long sourceId, String busiCode)
	{

		String str = "0";

		Sm_ApprovalProcess_AFForm model = new Sm_ApprovalProcess_AFForm();
		model.setSourceId(sourceId);
		model.setBusiCode(busiCode);
		model.setOrderBy("createtimestamp desc");

		List<Sm_ApprovalProcess_AF> afList;
		afList = sm_ApprovalProcess_AFDao
				.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), model));
		if (null != afList && afList.size() > 0)
		{
			// 查询到申请单信息
			Sm_ApprovalProcess_AF approvalProcess_AF = afList.get(0);

			if (S_TheState.Deleted == approvalProcess_AF.getTheState())
			{
				// 申请单已被删除
				str = "0";
				return str;
			}

			if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState()))
			{
				// 申请单已完结
				str = "1";
				return str;
			}

			if (S_ApprovalState.WaitSubmit.equals(approvalProcess_AF.getBusiState()))
			{
				// 申请单待提交
				str = "2";
				return str;
			}

			if (S_ApprovalState.NoPass.equals(approvalProcess_AF.getBusiState()))
			{
				// 申请单不通过
				str = "3";
				return str;
			}

			// 申请单处于完结状态，查询处于哪个审批节点
			if (S_ApprovalState.Examining.equals(approvalProcess_AF.getBusiState()))
			{
				// 当前审批进度
				Long currentIndex = approvalProcess_AF.getCurrentIndex();

				Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_ApprovalProcess_WorkflowDao
						.findById(currentIndex);

				if (null == sm_approvalProcess_workflow)
				{
					// 申请单已完结
					str = "1";
					return str;
				}

				str = sm_approvalProcess_workflow.geteCode();
//				if ("ZS".equals(sm_approvalProcess_workflow.geteCode()))
//				{
//					// 处于最后一个节点，说明上一个节点审批完成
//					str = "ZS";
//				}
//				else
//				{
//					
//				}

			}

		}
		else
		{
			// 未查询到申请单信息
			str = "0";
		}

		return str;

	}

}

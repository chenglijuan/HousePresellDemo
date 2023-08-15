package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgpf_RefundInfo;

/*
 * Dao数据库操作：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Repository
public class Tgpf_RefundInfoDao extends BaseDao<Tgpf_RefundInfo>
{
	public String getBasicHQL()
	{
		return "from Tgpf_RefundInfo where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				
				//风控抽查条件
				+ " <#if approvalState??> and approvalState=:approvalState</#if>"
				+ " <#if checkStartTimeStamp??> and createTimeStamp>=:checkStartTimeStamp</#if>"
				+ " <#if checkEndTimeStamp??> and createTimeStamp<:checkEndTimeStamp</#if>"
				
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if eCodeOfTripleAgreement??> and eCodeOfTripleAgreement=:eCodeOfTripleAgreement</#if>"
				+ " <#if eCodeOfContractRecord??> and eCodeOfContractRecord=:eCodeOfContractRecord</#if>"
				+ " <#if theNameOfProject??> and theNameOfProject=:theNameOfProject</#if>"
				+ " <#if positionOfBuilding??> and positionOfBuilding=:positionOfBuilding</#if>"
				+ " <#if theNameOfBuyer??> and theNameOfBuyer=:theNameOfBuyer</#if>"
				+ " <#if certificateNumberOfBuyer??> and certificateNumberOfBuyer=:certificateNumberOfBuyer</#if>"
				+ " <#if contactPhoneOfBuyer??> and contactPhoneOfBuyer=:contactPhoneOfBuyer</#if>"
				+ " <#if theNameOfCreditor??> and theNameOfCreditor=:theNameOfCreditor</#if>"
				+ " <#if fundOfTripleAgreement??> and fundOfTripleAgreement=:fundOfTripleAgreement</#if>"
				+ " <#if fundFromLoan??> and fundFromLoan=:fundFromLoan</#if>"
				+ " <#if retainRightAmount??> and retainRightAmount=:retainRightAmount</#if>"
				+ " <#if expiredAmount??> and expiredAmount=:expiredAmount</#if>"
				+ " <#if unexpiredAmount??> and unexpiredAmount=:unexpiredAmount</#if>"
				+ " <#if refundAmount??> and refundAmount=:refundAmount</#if>"
				+ " <#if receiverType??> and receiverType=:receiverType</#if>"
				+ " <#if receiverName??> and receiverName=:receiverName</#if>"
				+ " <#if receiverBankName??> and receiverBankName=:receiverBankName</#if>"
				+ " <#if refundType??> and refundType=:refundType</#if>"
				+ " <#if receiverBankAccount??> and receiverBankAccount=:receiverBankAccount</#if>"
				+ " <#if actualRefundAmount??> and actualRefundAmount=:actualRefundAmount</#if>"
				+ " <#if refundBankName??> and refundBankName=:refundBankName</#if>"
				+ " <#if refundBankAccount??> and refundBankAccount=:refundBankAccount</#if>"
				+ " <#if refundTimeStamp??> and refundTimeStamp=:refundTimeStamp</#if>"
				+ " <#if buidlingId??> and building.tableId=:buidlingId</#if>";
	}

	/**
	 * 模糊查询
	 * yangyu by time 2018.8.16 11.59
	 * 1.修改查询条件 使得根据关键字（keyword）即可查询
	 * （合同备案号 三方协议号 项目名称 买受人名称 主借款人 退款申请编号）
	 * 2.添加排序规则（创建时间降序）
	 * 
	 * 调用service:
	 * Tgpf_RefundInfoListService
	 * 
	 * @version 2.0
	 * @return
	 */
	public String getBasicHQL2()
	{
		return "from Tgpf_RefundInfo where 1=1"
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if refundType??> and refundType=:refundType</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>"
				+ " <#if projectId??> and project.tableId =:projectId</#if>"
				+ "<#if keyword ??> and (eCodeOfContractRecord like :keyword or  eCodeOfTripleAgreement like :keyword or theNameOfProject like :keyword or theNameOfBuyer like :keyword or refundCode like :keyword) </#if> "
				+ " <#if refundTimeStamp??> and refundTimeStamp=:refundTimeStamp</#if>"
//				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and project.tableId in :projectInfoIdArr</#if>"

				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> and (</#if>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)>"
					+ " <#list projectInfoIdArr as projectInfoId>"
						+ " <#if projectInfoId_index == 0> (project.tableId = ${projectInfoId?c} )</#if>"
						+ " <#if projectInfoId_index != 0> or (project.tableId = ${projectInfoId?c} )</#if>"
					+ " </#list>"
				+ " <#if projectInfoIdArr?? && (projectInfoIdArr?size>0)> )</#if>"
				+ " </#if>"

				+ " order by refundTimeStamp asc, theNameOfProject desc, eCodeOfTripleAgreement desc";
	}

	/**
	 * 用于托管终止--筛选不可用楼幢
	 * @param model
	 * @return
	 */
	public Criteria createCriteriaForRefundInfoList(Empj_BldEscrowCompletedForm model)
	{
		//退房退款 Tgpf_RefundInfo theState=0 && approvalState=审批中 && project=? ->building
		Criteria criteria = createCriteria()
				.createAlias("project", "project")
				.setProjection(Projections.groupProperty("building"));

		criteria.add(Restrictions.eq("theState", S_TheState.Normal)); //未删除
		criteria.add(Restrictions.eq("approvalState", S_ApprovalState.Examining)); //审批中

		Long projectId = model.getProjectId();
		if (projectId != null && projectId > 0)
		{
			criteria.add(Restrictions.eq("project.tableId", projectId));
		}

		return criteria;
	}
}

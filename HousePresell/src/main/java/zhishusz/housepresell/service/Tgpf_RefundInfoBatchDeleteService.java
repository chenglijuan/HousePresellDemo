package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoBatchDeleteService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;

	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

	@Autowired
	private Sm_AttachmentDao smAttachmentDao;

	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;

	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for (Long tableId : idArr)
		{
			Tgpf_RefundInfo tgpf_RefundInfo = (Tgpf_RefundInfo) tgpf_RefundInfoDao.findById(tableId);
			if (tgpf_RefundInfo == null)
			{
				return MyBackInfo.fail(properties, "'Tgpf_RefundInfo(Id:" + tableId + ")'不存在");
			}

			tgpf_RefundInfo.setTheState(S_TheState.Deleted);
			tgpf_RefundInfoDao.save(tgpf_RefundInfo);

			String busiCode = tgpf_RefundInfo.geteCode().split("N")[0];

			// 本次退款金额
			Double refundAmount = tgpf_RefundInfo.getRefundAmount();

			if (refundAmount == null)
			{
				return MyBackInfo.fail(properties, "本次退款金额不能为空");
			}

			// 查询楼幢账户
			Empj_BuildingInfo building = tgpf_RefundInfo.getBuilding();

			if (building == null)
			{
				return MyBackInfo.fail(properties, "楼幢账户不能为空");
			}

			// 查询楼幢账户
			Tgpj_BuildingAccountForm buildingAccountForm = new Tgpj_BuildingAccountForm();

			buildingAccountForm.setBuilding(building);

			buildingAccountForm.setTheState(S_TheState.Normal);

			List<Tgpj_BuildingAccount> tgpj_BuildingAccounts = tgpj_BuildingAccountDao.findByPage(
					tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), buildingAccountForm));

			if (null == tgpj_BuildingAccounts || tgpj_BuildingAccounts.size() == 0)
			{

				return MyBackInfo.fail(properties, "楼幢账户查询为空");

			}

			Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccounts.get(0);

			// 已申请退款未拨付金额（元）
			Double applyRefundPayoutAmount = tgpj_BuildingAccount.getApplyRefundPayoutAmount();

			/*
			 * 保存后更新<楼幢账户>：增加“已申请退款未拨付金额（元）”
			 */
			// 重新计算楼幢已申请退款未拨付金额（元）
			applyRefundPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(applyRefundPayoutAmount,
					refundAmount);

			if (applyRefundPayoutAmount < 0)
			{

				applyRefundPayoutAmount = 0.0;

			}

			// 重新计算楼幢账户信息
			setChanges(applyRefundPayoutAmount, tgpj_BuildingAccount, busiCode, tgpf_RefundInfo);

			// 从新计算户入账总金额
			Tgxy_TripleAgreementForm form = new Tgxy_TripleAgreementForm();
			form.setTheState(S_TheState.Normal);
			form.seteCodeOfTripleAgreement(tgpf_RefundInfo.geteCodeOfTripleAgreement());
			Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao
					.findOneByQuery_T(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), form));
			if (null == tripleAgreement)
			{
				return MyBackInfo.fail(properties, "未关联到有效的三方协议信息");
			}
			Double totalAmountOfHouse = tripleAgreement.getTotalAmountOfHouse();// 户入账总金额
			if (null == totalAmountOfHouse)
			{
				totalAmountOfHouse = 0.00;
			}
			totalAmountOfHouse = MyDouble.getInstance().doubleAddDouble(totalAmountOfHouse, refundAmount);
			tripleAgreement.setTotalAmountOfHouse(totalAmountOfHouse);
			tgxy_TripleAgreementDao.update(tripleAgreement);

			// 删除附件
			// 根据退房退款ID进行查询附件功能
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(tableId);
			from.setTheState(S_TheState.Normal);
			from.setSourceId(sourceId);

			// 查询附件
			List<Sm_Attachment> smAttachmentList = smAttachmentDao
					.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), from));
			// 删除附件
			if (null != smAttachmentList && smAttachmentList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : smAttachmentList)
				{
					sm_Attachment.setTheState(S_TheState.Deleted);
					smAttachmentDao.save(sm_Attachment);
				}
			}
			
			//删除审批流
			deleteService.execute(tableId, model.getBusiCode());
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 保存日志
	 * 
	 * @param applyRefundPayoutAmount
	 *            已申请退款未拨付金额
	 * @param buildingAccount
	 */
	private void setChanges(Double applyRefundPayoutAmount, Tgpj_BuildingAccount buildingAccount, String busiCode,
			Tgpf_RefundInfo tgpf_RefundInfo)
	{
		// 不发生修改的字段
		Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
		tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
		tgpj_BuildingAccountLog.setBusiState(buildingAccount.getBusiState());
		tgpj_BuildingAccountLog.seteCode(buildingAccount.geteCode());
		tgpj_BuildingAccountLog.setUserStart(buildingAccount.getUserStart());
		tgpj_BuildingAccountLog.setCreateTimeStamp(buildingAccount.getCreateTimeStamp());
		tgpj_BuildingAccountLog.setUserUpdate(buildingAccount.getUserUpdate());
		tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpj_BuildingAccountLog.setUserRecord(buildingAccount.getUserRecord());
		tgpj_BuildingAccountLog.setRecordTimeStamp(buildingAccount.getRecordTimeStamp());
		tgpj_BuildingAccountLog.setDevelopCompany(buildingAccount.getDevelopCompany());
		tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(buildingAccount.geteCodeOfDevelopCompany());
		tgpj_BuildingAccountLog.setProject(buildingAccount.getProject());
		tgpj_BuildingAccountLog.setTheNameOfProject(buildingAccount.getTheNameOfProject());
		tgpj_BuildingAccountLog.setBuilding(buildingAccount.getBuilding());
		tgpj_BuildingAccountLog.setPayment(buildingAccount.getPayment());
		tgpj_BuildingAccountLog.seteCodeOfBuilding(buildingAccount.geteCodeOfBuilding());
		tgpj_BuildingAccountLog.setEscrowStandard(buildingAccount.getEscrowStandard());
		tgpj_BuildingAccountLog.setEscrowArea(buildingAccount.getEscrowArea());
		tgpj_BuildingAccountLog.setBuildingArea(buildingAccount.getBuildingArea());
		tgpj_BuildingAccountLog.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
		tgpj_BuildingAccountLog.setCurrentFigureProgress(buildingAccount.getCurrentFigureProgress());
		tgpj_BuildingAccountLog.setCurrentLimitedRatio(buildingAccount.getCurrentLimitedRatio());
		tgpj_BuildingAccountLog.setNodeLimitedAmount(buildingAccount.getNodeLimitedAmount());
		tgpj_BuildingAccountLog.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
		tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
		tgpj_BuildingAccountLog.setTotalAccountAmount(buildingAccount.getTotalAccountAmount());
		tgpj_BuildingAccountLog.setPayoutAmount(buildingAccount.getPayoutAmount());
		tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(buildingAccount.getAppliedNoPayoutAmount());
		tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(applyRefundPayoutAmount);
		tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
		tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
		tgpj_BuildingAccountLog.setAppropriateFrozenAmount(buildingAccount.getAppropriateFrozenAmount());
		tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(
				buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());
		tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
		tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
		tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
		tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
		tgpj_BuildingAccountLog.setRelatedBusiCode(busiCode);
		tgpj_BuildingAccountLog.setRelatedBusiTableId(tgpf_RefundInfo.getTableId());
		tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
		tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
		tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
		tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
		tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
		tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
		tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
		tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
		tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
		tgpj_BuildingAccountLog.setAllocableAmount(buildingAccount.getAllocableAmount());
		tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(buildingAccount.getBldLimitAmountVerDtl());

		tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);
	}
}

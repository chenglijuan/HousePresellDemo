package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
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
 * Service添加操作：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoAddService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	// 业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;

	String busiCode = "06120201";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = 0;
		Long userStartId = model.getUserId();
		Long createTimeStamp = System.currentTimeMillis();
		Long lastUpdateTimeStamp = System.currentTimeMillis();
		Long tripleAgreementId = model.getTripleAgreementId();
		String eCodeOfTripleAgreement = model.geteCodeOfTripleAgreement();
		String eCodeOfContractRecord = model.geteCodeOfContractRecord();
		Long projectId = model.getProjectId();
		String theNameOfProject = model.getTheNameOfProject();
		Long buildingId = model.getBuildingId();
		String positionOfBuilding = model.getPositionOfBuilding();

		String theNameOfBuyer = model.getTheNameOfBuyer();
		String certificateNumberOfBuyer = model.getCertificateNumberOfBuyer();
		String contactPhoneOfBuyer = model.getContactPhoneOfBuyer();

		String theNameOfCreditor = model.getTheNameOfCreditor();
		Double fundOfTripleAgreement = model.getFundOfTripleAgreement();
		Double fundFromLoan = model.getFundFromLoan();
		Double retainRightAmount = model.getRetainRightAmount();
		Double expiredAmount = model.getExpiredAmount();
		Double unexpiredAmount = model.getUnexpiredAmount();
		Double refundAmount = model.getRefundAmount();
		Integer receiverType = model.getReceiverType();
		String receiverName = model.getReceiverName();
		String receiverBankName = model.getReceiverBankName();
		String refundType = "0";// 贷款已结清
		String receiverBankAccount = model.getReceiverBankAccount();
		Double actualRefundAmount = model.getActualRefundAmount();

		String developCompanyName = model.getDevelopCompanyName();// 买受人
		String bAccountSupervised = model.getBAccountSupervised();// 收款账号
		String bBankName = model.getBBankName();// 收款银行

		// 附件信息
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();
		}
		if (tripleAgreementId == null || tripleAgreementId < 1)
		{
			return MyBackInfo.fail(properties, "三方协议主键不能为空");
		}
		if (eCodeOfTripleAgreement == null || eCodeOfTripleAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "三方协议编号不能为空");
		}
		if (eCodeOfContractRecord == null || eCodeOfContractRecord.length() == 0)
		{
			return MyBackInfo.fail(properties, "合同备案号不能为空");
		}
		if (projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "关联项目不能为空");
		}
		if (theNameOfProject == null || theNameOfProject.length() == 0)
		{
			return MyBackInfo.fail(properties, "关联项目名称不能为空");
		}
		if (buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "关联楼幢不能为空");
		}
		if (positionOfBuilding == null || positionOfBuilding.length() == 0)
		{
			return MyBackInfo.fail(properties, "房屋坐落不能为空");
		}
		if (theNameOfBuyer == null || theNameOfBuyer.length() == 0)
		{
			return MyBackInfo.fail(properties, "买受人不能为空");
		}
		if (certificateNumberOfBuyer == null || certificateNumberOfBuyer.length() == 0)
		{
			return MyBackInfo.fail(properties, "买受人证件号码不能为空");
		}
		if (contactPhoneOfBuyer == null || contactPhoneOfBuyer.length() == 0)
		{
			return MyBackInfo.fail(properties, "买受人联系电话不能为空");
		}
		if (theNameOfCreditor == null || theNameOfCreditor.length() == 0)
		{
			return MyBackInfo.fail(properties, "主借款人不能为空");
		}
		if (fundOfTripleAgreement == null || fundOfTripleAgreement < 1)
		{
			return MyBackInfo.fail(properties, "合同金额不能为空");
		}

		if (receiverType == null || receiverType < 1)
		{
			return MyBackInfo.fail(properties, "收款人类型不能为空");
		}

		if (receiverType == 1)
		{
			if (receiverName == null || receiverName.length() == 0)
			{
				return MyBackInfo.fail(properties, "收款人名称不能为空");
			}
			if (receiverBankName == null || receiverBankName.length() == 0)
			{
				return MyBackInfo.fail(properties, "收款银行不能为空");
			}
			if (receiverBankAccount == null || receiverBankAccount.length() == 0)
			{
				return MyBackInfo.fail(properties, "收款账号不能为空");
			}
		}
		else
		{
			if (developCompanyName == null || developCompanyName.length() == 0)
			{
				return MyBackInfo.fail(properties, "收款人名称不能为空");
			}
			if (bBankName == null || bBankName.length() == 0)
			{
				return MyBackInfo.fail(properties, "收款银行不能为空");
			}
			if (bAccountSupervised == null || bAccountSupervised.length() == 0)
			{
				return MyBackInfo.fail(properties, "收款账号不能为空");
			}
		}
		
		if (retainRightAmount == null || retainRightAmount <= 0.00)
		{
			return MyBackInfo.fail(properties, "留存权益金额不能为0");
		}
		

		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		Tgxy_TripleAgreement tripleAgreement = (Tgxy_TripleAgreement) tgxy_TripleAgreementDao
				.findById(tripleAgreementId);
		Empj_ProjectInfo project = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(projectId);

		Empj_BuildingInfo building = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingId);

		if (tripleAgreement == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的三方协议信息");
		}
		if (project == null)
		{
			return MyBackInfo.fail(properties, "未查询到关联项目信息");
		}
		if (building == null)
		{
			return MyBackInfo.fail(properties, "未查询到关联楼幢信息");
		}

		Tgpf_RefundInfo tgpf_RefundInfo = new Tgpf_RefundInfo();

		tgpf_RefundInfo.setTheState(theState);
		tgpf_RefundInfo.setUserStart(userStart);
		tgpf_RefundInfo.setCreateTimeStamp(createTimeStamp);
		tgpf_RefundInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_RefundInfo.setTripleAgreement(tripleAgreement);
		tgpf_RefundInfo.seteCodeOfTripleAgreement(eCodeOfTripleAgreement);
		tgpf_RefundInfo.seteCodeOfContractRecord(eCodeOfContractRecord);
		tgpf_RefundInfo.setProject(project);
		tgpf_RefundInfo.setTheNameOfProject(theNameOfProject);
		tgpf_RefundInfo.setBuilding(building);
		tgpf_RefundInfo.setPositionOfBuilding(positionOfBuilding);
		tgpf_RefundInfo.setTheNameOfBuyer(theNameOfBuyer);
		tgpf_RefundInfo.setCertificateNumberOfBuyer(certificateNumberOfBuyer);
		tgpf_RefundInfo.setContactPhoneOfBuyer(contactPhoneOfBuyer);
		tgpf_RefundInfo.setTheNameOfCreditor(theNameOfCreditor);
		tgpf_RefundInfo.setFundOfTripleAgreement(fundOfTripleAgreement);
		tgpf_RefundInfo.setFundFromLoan(fundFromLoan);
		tgpf_RefundInfo.setRetainRightAmount(retainRightAmount);
		tgpf_RefundInfo.setExpiredAmount(expiredAmount);
		tgpf_RefundInfo.setUnexpiredAmount(unexpiredAmount);
		tgpf_RefundInfo.setRefundAmount(refundAmount);

		tgpf_RefundInfo.setReceiverType(receiverType);
		if (receiverType == 2)
		{
			tgpf_RefundInfo.setReceiverName(developCompanyName);
			tgpf_RefundInfo.setReceiverBankName(bBankName);
			tgpf_RefundInfo.setReceiverBankAccount(bAccountSupervised);
		}
		else
		{
			tgpf_RefundInfo.setReceiverName(receiverName);
			tgpf_RefundInfo.setReceiverBankName(receiverBankName);
			tgpf_RefundInfo.setReceiverBankAccount(receiverBankAccount);
		}

		tgpf_RefundInfo.setRefundType(refundType);
		actualRefundAmount = refundAmount;
		tgpf_RefundInfo.setActualRefundAmount(actualRefundAmount);

		// 生成退房退款编号
		tgpf_RefundInfo.seteCode(sm_BusinessCodeGetService.execute(busiCode));

		/*
		 * 保存时计算户 留存权益总金额、到期权益金额、未到期权益金额
		 */

		// 户入账总金额
		Double totalAmountOfHouse = tripleAgreement.getTotalAmountOfHouse();
		
		if (null == totalAmountOfHouse)
		{
			return MyBackInfo.fail(properties, "户入账总金额为空，请核对数据。");
		}
		
		//重新计算户入账总金额
		totalAmountOfHouse = MyDouble.getInstance().doubleSubtractDouble(totalAmountOfHouse, refundAmount);
		if (totalAmountOfHouse < 0)
		{
			totalAmountOfHouse = 0.0;
		}

		tripleAgreement.setTotalAmountOfHouse(totalAmountOfHouse);

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

		// 楼幢总入账金额
		Double totalAccountAmount = tgpj_BuildingAccount.getTotalAccountAmount();

		if (null == totalAccountAmount)
		{
			return MyBackInfo.fail(properties, "楼幢入账总金额为空，请核对数据。");
		}

		// 已申请退款未拨付金额
		Double applyRefundPayoutAmount = tgpj_BuildingAccount.getApplyRefundPayoutAmount();

		if (null == applyRefundPayoutAmount)
		{
			applyRefundPayoutAmount = 0.0;
		}

		/*
		 * 计算留存权益总金额
		 * 
		 * 留存权益（户）=（户入账金额/楼幢入账总金额）*幢托管资金实际可用余额
		 * =未到期留存权益（户）+到期留存权益（户）
		 * 户入账金额：该户入账金额；
		 * 楼幢入账总金额：该户所在楼幢托管入账总金额-已退房退款金额；
		 * 幢托管资金实际可用余额：为楼幢入账总金额-拨付累计申请金额（已拨付金额+拨付冻结金额）-退房退款累计申请金额（已退房退款+
		 * 退房退款冻结金额）
		 *
		 * 未到期留存权益（户）=（户入账金额/楼幢入账总金额）* 幢托管受限额度
		 */

		// 户留存权益
		retainRightAmount = tripleAgreement.getTheAmountOfRetainedEquity();

		if (null == retainRightAmount)
		{
			retainRightAmount = 0.0;
		}

		if (retainRightAmount > totalAccountAmount)
		{
			return MyBackInfo.fail(properties, "本次退款金额大于当前楼幢总入账金额");
		}

		// 未到期留存权益
		unexpiredAmount = tripleAgreement.getTheAmountOfInterestUnRetained();

		if (null == unexpiredAmount)
		{
			unexpiredAmount = 0.0;
		}

		// 到期留存权益
		expiredAmount = tripleAgreement.getTheAmountOfInterestRetained();

		if (null == expiredAmount)
		{
			unexpiredAmount = 0.0;
		}

		Double currentEscrowFund = tgpj_BuildingAccount.getCurrentEscrowFund();

		if (null == currentEscrowFund)
		{
			currentEscrowFund = 0.0;
		}

		// 需要恢复代码-注释仅限与流程串通
		if (refundAmount > currentEscrowFund)
		{

			return MyBackInfo.fail(properties, "本次退款金额大于当前托管余额");
		}

		tgpf_RefundInfo.setRetainRightAmount(retainRightAmount);

		tgpf_RefundInfo.setExpiredAmount(expiredAmount);

		tgpf_RefundInfo.setUnexpiredAmount(unexpiredAmount);

		tgpf_RefundInfo.setActualRefundAmount(actualRefundAmount);

		tgpf_RefundInfo.setVersion(0L);// 乐观锁

		Serializable entity = tgpf_RefundInfoDao.save(tgpf_RefundInfo);

		/*
		 * 保存后更新<楼幢账户>：增加“已申请退款未拨付金额（元）”增加冻结金额
		 */
		// 重新计算楼幢已申请退款未拨付金额（元）
		applyRefundPayoutAmount = MyDouble.getInstance().doubleAddDouble(applyRefundPayoutAmount, refundAmount);

		// 重新计算楼幢账户信息
		setChanges(applyRefundPayoutAmount, tgpj_BuildingAccount);
		
		//更新三方协议
		tgxy_TripleAgreementDao.update(tripleAgreement);

		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

		for (Sm_Attachment sm_Attachment : gasList)
		{
			// 查询附件配置表
			Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
			form.seteCode(sm_Attachment.getSourceType());
			Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
					.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

			sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
			sm_Attachment.setSourceId(entity.toString());
			sm_Attachment.setTheState(S_TheState.Normal);
			smAttachmentDao.save(sm_Attachment);
		}

		properties.put("tableId", new Long(entity.toString()));

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
	private void setChanges(Double applyRefundPayoutAmount, Tgpj_BuildingAccount buildingAccount)
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

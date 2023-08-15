package zhishusz.housepresell.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmountForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.controller.form.Tg_RiskCheckBusiCodeSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskCheckMonthSumForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckInfoForm;
import zhishusz.housepresell.controller.form.Tg_RiskRoutineCheckRatioConfigForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckBusiCodeSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskCheckMonthSumDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckInfoDao;
import zhishusz.housepresell.database.dao.Tg_RiskRoutineCheckRatioConfigDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tg_RiskCheckBusiCodeSum;
import zhishusz.housepresell.database.po.Tg_RiskCheckMonthSum;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckInfo;
import zhishusz.housepresell.database.po.Tg_RiskRoutineCheckRatioConfig;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_EntryState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_RectificationState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_YesNoStr;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;

/*
 * Service添加操作：风控例行抽查表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_RiskRoutineCheckInfoAddService
{
	private static final String BUSI_CODE = "21020102";

	@Autowired
	private Tg_RiskRoutineCheckInfoDao tg_RiskRoutineCheckInfoDao;
	@Autowired
	private Tg_RiskRoutineCheckRatioConfigDao tg_RiskRoutineCheckRatioConfigDao;
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
	@Autowired
	private Empj_BldLimitAmountDao empj_BldLimitAmountDao;
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tg_RiskCheckMonthSumDao tg_RiskCheckMonthSumDao;
	@Autowired
	private Tg_RiskCheckBusiCodeSumDao tg_RiskCheckBusiCodeSumDao;
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;

	private Integer count = 0;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public Properties execute(Tg_RiskRoutineCheckInfoForm model)
	{
		Properties properties = new MyProperties();

		MyDouble myDouble = MyDouble.getInstance();
		MyDatetime myDatetime = MyDatetime.getInstance();

		String spotTimeStr = model.getSpotTimeStr();

		if (model.getUser() == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		if (spotTimeStr == null || spotTimeStr.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择抽查月份");
		}

		model.setTheState(S_TheState.Normal);
		model.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));

		List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList = tg_RiskRoutineCheckInfoDao
				.findByPage(tg_RiskRoutineCheckInfoDao.getQuery(tg_RiskRoutineCheckInfoDao.getBasicHQL(), model));
		if (!tg_RiskRoutineCheckInfoList.isEmpty())
		{
			return MyBackInfo.fail(properties, "选择的抽查月份已经抽查过了");
		}

		// 新增之前把曾经暂存过的全部删除
		deleteCache(myDatetime.stringToLong(spotTimeStr + "-01"));

		Tg_RiskCheckMonthSum tg_RiskCheckMonthSum = new Tg_RiskCheckMonthSum();

		tg_RiskCheckMonthSum.setTheState(S_TheState.Cache);
		tg_RiskCheckMonthSum.setUserStart(model.getUser());
		tg_RiskCheckMonthSum.setCreateTimeStamp(System.currentTimeMillis());
		tg_RiskCheckMonthSum.setCheckNumber("FKCC" + ((spotTimeStr + "-01").replace("-", "")));
		tg_RiskCheckMonthSum.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));
		tg_RiskCheckMonthSum.setQualifiedCount(0);
		tg_RiskCheckMonthSum.setUnqualifiedCount(0);
		tg_RiskCheckMonthSum.setPushCount(0);
		tg_RiskCheckMonthSum.setFeedbackCount(0);
		tg_RiskCheckMonthSum.setHandleCount(0);
		tg_RiskCheckMonthSum.setSumCheckCount(tg_RiskRoutineCheckInfoList.size());
		tg_RiskCheckMonthSum.setUserUpdate(model.getUser());
		tg_RiskCheckMonthSum.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_RiskCheckMonthSum.setRectificationState(S_RectificationState.Doing);

		tg_RiskCheckMonthSumDao.save(tg_RiskCheckMonthSum);

		Long[] rangeTimeStamp = myDatetime.getBeforeMarch(spotTimeStr);

		Integer bldLimitAmountCount = 0;
		Integer pjDevProgressForcastCount = 0;
		Integer bldEscrowCompletedCount = 0;
		Integer escrowAgreementCount = 0;
		Integer tripleAgreementCount = 0;
		Integer refundInfo0Count = 0;
		Integer refundInfo1Count = 0;
		Integer fundAppropriatedCount = 0;
		Integer paymentGuaranteeCount = 0;
		Integer depositManagementCount = 0;

		Tg_RiskCheckBusiCodeSum bldLimitAmountRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum bldLimitAmountCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();// 工程进度节点更新
		Tg_RiskCheckBusiCodeSum bldEscrowCompletedRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum escrowAgreementRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum tripleAgreementRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum refundInfo0RiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum refundInfo1RiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum fundAppropriatedRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum paymentGuaranteeRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();
		Tg_RiskCheckBusiCodeSum depositManagementRiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();

		List<Object> objectList = new ArrayList<Object>();

		Tg_RiskRoutineCheckRatioConfigForm tg_RiskRoutineCheckRatioConfigForm = new Tg_RiskRoutineCheckRatioConfigForm();
		tg_RiskRoutineCheckRatioConfigForm.setTheState(S_TheState.Normal);
		List<Tg_RiskRoutineCheckRatioConfig> tg_RiskRoutineCheckRatioConfigList = tg_RiskRoutineCheckRatioConfigDao
				.findByPage(tg_RiskRoutineCheckRatioConfigDao.getQuery(tg_RiskRoutineCheckRatioConfigDao.getBasicHQL(),
						model));

		// System.out.println("开始存业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));

		for (Tg_RiskRoutineCheckRatioConfig ratioConfig : tg_RiskRoutineCheckRatioConfigList)
		{
			if (ratioConfig.getTheRatio() != null && ratioConfig.getRole() != null)
			{
				String subBusinessValue = ratioConfig.getSubBusinessValue();
				String largeBusinessValue = ratioConfig.getLargeBusinessValue();
				Double theRatio = myDouble.div(myDouble.parse(ratioConfig.getTheRatio()), 100.0, 2);

				switch (largeBusinessValue + "-" + subBusinessValue)
				{
				// 受限额度变更
				case S_BusiCode.busiCode10 + "-" + S_BusiCode.busiCode11:// 受限额度变更Empj_BldLimitAmountCheckInfo
					// System.out.println("开始存受限业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					bldLimitAmountRiskCheckBusiCodeSum = getEmpj_BldLimitAmountAFCheckInfo(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (bldLimitAmountRiskCheckBusiCodeSum != null)
					{
						bldLimitAmountCount = bldLimitAmountRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						bldLimitAmountCount = 0;
					}
					// System.out.println("结束存受限业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				// 工程进度节点更新
				case "0303" + "-" + S_BusiCode.busiCode_03030100:// 受限额度变更Empj_BldLimitAmountCheckInfo
					// System.out.println("开始存受限业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					bldLimitAmountCheckBusiCodeSum = getEmpj_BldLimitAmountCheckInfo(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (bldLimitAmountCheckBusiCodeSum != null)
					{
						pjDevProgressForcastCount = bldLimitAmountCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						pjDevProgressForcastCount = 0;
					}
					break;
				case S_BusiCode.busiCode10 + "-" + S_BusiCode.busiCode12:// 托管终止Empj_BldEscrowCompleted
					// System.out.println("开始存托管终止汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					bldEscrowCompletedRiskCheckBusiCodeSum = getEmpj_BldEscrowCompleted(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (bldEscrowCompletedRiskCheckBusiCodeSum != null)
					{
						bldEscrowCompletedCount = bldEscrowCompletedRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						bldEscrowCompletedCount = 0;
					}
					// System.out.println("结束存托管终止汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode13 + "-" + S_BusiCode.busiCode14:// 全额托管合作协议签署
					break;
				case S_BusiCode.busiCode13 + "-" + S_BusiCode.busiCode15:// 贷款托管合作协议签署Tgxy_EscrowAgreement
					// System.out.println("开始存合作协议汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					escrowAgreementRiskCheckBusiCodeSum = getTgxy_EscrowAgreement(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (escrowAgreementRiskCheckBusiCodeSum != null)
					{
						escrowAgreementCount = escrowAgreementRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						escrowAgreementCount = 0;
					}
					// System.out.println("结束存合作协议汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode16 + "-" + S_BusiCode.busiCode17:// 全额三方托管协议签署
					break;
				case S_BusiCode.busiCode16 + "-" + S_BusiCode.busiCode18:// 贷款三方托管协议签署Tgxy_TripleAgreement
					// System.out.println("开始存三方协议汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					tripleAgreementRiskCheckBusiCodeSum = getTgxy_TripleAgreement(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (tripleAgreementRiskCheckBusiCodeSum != null)
					{
						tripleAgreementCount = tripleAgreementRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						tripleAgreementCount = 0;
					}
					// System.out.println("结束存三方协议汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode20 + "-" + S_BusiCode.busiCode21:// 退房退款申请-贷款已结清Tgpf_RefundInfo/refundType0
					// System.out.println("开始存贷款已结清汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					refundInfo0RiskCheckBusiCodeSum = getTgpf_RefundInfo0(objectList, spotTimeStr, rangeTimeStamp,
							theRatio, myDatetime, model);
					if (refundInfo0RiskCheckBusiCodeSum != null)
					{
						refundInfo0Count = refundInfo0RiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						refundInfo0Count = 0;
					}
					// System.out.println("结束存贷款已结清汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode20 + "-" + S_BusiCode.busiCode22:// 退房退款申请-贷款未结清Tgpf_RefundInfo/refundType1
					// System.out.println("开始存贷款未结清汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					refundInfo1RiskCheckBusiCodeSum = getTgpf_RefundInfo1(objectList, spotTimeStr, rangeTimeStamp,
							theRatio, myDatetime, model);
					if (refundInfo1RiskCheckBusiCodeSum != null)
					{
						refundInfo1Count = refundInfo1RiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						refundInfo1Count = 0;
					}
					// System.out.println("结束存贷款未结清汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode19 + "-" + S_BusiCode.busiCode7:// 用款申请Tgpf_FundAppropriated_AF
					// System.out.println("开始存用款申请汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					fundAppropriatedRiskCheckBusiCodeSum = getTgpf_FundAppropriated_AF(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (fundAppropriatedRiskCheckBusiCodeSum != null)
					{
						fundAppropriatedCount = fundAppropriatedRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						fundAppropriatedCount = 0;
					}
					// System.out.println("结束存用款申请汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode25 + "-" + S_BusiCode.busiCode26:// 支付保证业务Empj_PaymentGuarantee
					// System.out.println("开始存支付保证汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					paymentGuaranteeRiskCheckBusiCodeSum = getEmpj_PaymentGuarantee(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (paymentGuaranteeRiskCheckBusiCodeSum != null)
					{
						paymentGuaranteeCount = paymentGuaranteeRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						paymentGuaranteeCount = 0;
					}
					// System.out.println("结束存支付保证汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				case S_BusiCode.busiCode23 + "-" + S_BusiCode.busiCode24:// 存单Tg_DepositManagement
					// System.out.println("开始存存单业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					depositManagementRiskCheckBusiCodeSum = getTg_DepositManagement(objectList, spotTimeStr,
							rangeTimeStamp, theRatio, myDatetime, model);
					if (depositManagementRiskCheckBusiCodeSum != null)
					{
						depositManagementCount = depositManagementRiskCheckBusiCodeSum.getSumCheckCount();
					}
					else
					{
						depositManagementCount = 0;
					}
					// System.out.println("结束存存单业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));
					break;
				default:
				}
			}
		}

		// System.out.println("结束存业务汇总"+myDatetime.dateToString2(System.currentTimeMillis()));

		Integer limitScale = bldLimitAmountCount;
		
		Integer completedScale = bldLimitAmountCount + bldEscrowCompletedCount;
		
		Integer escrowScale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount;
		
		Integer tripleScale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount;
		
		Integer refund0Scale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount + refundInfo0Count;
		
		Integer refund1Scale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount + refundInfo0Count + refundInfo1Count;
		
		Integer appropriatedScale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount + refundInfo0Count + refundInfo1Count + fundAppropriatedCount;
		
		Integer paymentScale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount + refundInfo0Count + refundInfo1Count + fundAppropriatedCount
				+ paymentGuaranteeCount;
		
		Integer depositScale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount + refundInfo0Count + refundInfo1Count + fundAppropriatedCount
				+ paymentGuaranteeCount + depositManagementCount;
		
		Integer pjDevProgressScale = bldLimitAmountCount + bldEscrowCompletedCount + escrowAgreementCount
				+ tripleAgreementCount + refundInfo0Count + refundInfo1Count + fundAppropriatedCount
				+ paymentGuaranteeCount + depositManagementCount + pjDevProgressForcastCount;

		if (objectList.isEmpty())
		{
			tg_RiskCheckMonthSum.setTheState(S_TheState.Deleted);
			tg_RiskCheckMonthSumDao.save(tg_RiskCheckMonthSum);

			return MyBackInfo.fail(properties, "选择的抽查月没有抽查业务");
		}
		else
		{
			tg_RiskCheckMonthSum.setSumCheckCount(objectList.size());
			tg_RiskCheckMonthSumDao.save(tg_RiskCheckMonthSum);
		}

		Session session = tg_RiskRoutineCheckInfoDao.getOpenSession();
		session.beginTransaction();

		// System.out.println("开始存抽查业务明细"+myDatetime.dateToString2(System.currentTimeMillis()));

		for (int i = 0; i < objectList.size(); i++)
		{
			// System.out.println("一次循环开始"+System.currentTimeMillis());

			Tg_RiskRoutineCheckInfo tg_RiskRoutineCheckInfo = new Tg_RiskRoutineCheckInfo();

			tg_RiskRoutineCheckInfo.setTheState(S_TheState.Cache);
			// tg_RiskRoutineCheckInfo.setBusiState(busiState);
			tg_RiskRoutineCheckInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
			tg_RiskRoutineCheckInfo.setUserStart(model.getUser());
			tg_RiskRoutineCheckInfo.setCreateTimeStamp(System.currentTimeMillis());
			// tg_RiskRoutineCheckInfo.setUserUpdate(model.getUser());
			// tg_RiskRoutineCheckInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
			// tg_RiskRoutineCheckInfo.setUserRecord(userRecord);
			// tg_RiskRoutineCheckInfo.setRecordTimeStamp(recordTimeStamp);
			tg_RiskRoutineCheckInfo.setCheckNumber("FKCC" + ((spotTimeStr + "-01").replace("-", "")));
			tg_RiskRoutineCheckInfo.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));

			tg_RiskRoutineCheckInfo.setIsChoosePush(S_YesNoStr.No);
			tg_RiskRoutineCheckInfo.setIsDoPush(S_YesNoStr.No);
			tg_RiskRoutineCheckInfo.setIsModify(S_YesNoStr.No);
			tg_RiskRoutineCheckInfo.setIsHandle(S_YesNoStr.No);
			tg_RiskRoutineCheckInfo.setRectificationState(S_RectificationState.Doing);
			tg_RiskRoutineCheckInfo.setEntryState(S_EntryState.Doing);
			tg_RiskRoutineCheckInfo.setMonthSummary(tg_RiskCheckMonthSum);

			String eCode = null;
			String relatedTableId = null;
			try
			{
				Object object = objectList.get(i);
				Class objClass = object.getClass();
				BeanInfo beanInfo = Introspector.getBeanInfo(objClass);
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

				for (PropertyDescriptor property : propertyDescriptors)
				{
					String key = property.getName();

					if (!key.equals("class"))
					{
						Method getter = property.getReadMethod();

						if (key.length() == 1)
						{
							key = key.substring(0, 1).toLowerCase();// 将属性值的首字母小写
						}
						else if (key.length() > 1)
						{
							key = key.substring(0, 1).toLowerCase() + key.substring(1, key.length());// 将属性值的首字母小写
						}

						if ("eCode".equals(key))
						{
							eCode = MyString.getInstance().parse(getter.invoke(object));
						}

						if ("tableId".equals(key))
						{
							relatedTableId = MyString.getInstance().parse(getter.invoke(object));
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			tg_RiskRoutineCheckInfo.seteCodeOfBill(eCode);
			tg_RiskRoutineCheckInfo.setRelatedTableId(Long.parseLong(relatedTableId));

			if (i < limitScale && i >= 0)
			{
				// 受限额度业务
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode10);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode11);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(bldLimitAmountRiskCheckBusiCodeSum);
			}
			if (i < completedScale && i >= limitScale)
			{
				// 托管终止
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode10);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode12);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(bldEscrowCompletedRiskCheckBusiCodeSum);
			}
			if (i < escrowScale && i >= completedScale)
			{
				// 合作协议
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode13);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode15);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(escrowAgreementRiskCheckBusiCodeSum);
			}
			if (i < tripleScale && i >= escrowScale)
			{
				// 三方协议
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode16);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode18);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(tripleAgreementRiskCheckBusiCodeSum);
			}
			if (i < refund0Scale && i >= tripleScale)
			{
				// 退房退房已结算
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode20);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode21);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(refundInfo0RiskCheckBusiCodeSum);
			}
			if (i < refund1Scale && i >= refund0Scale)
			{
				// 退房退房未结算
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode20);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode22);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(refundInfo1RiskCheckBusiCodeSum);
			}
			if (i < appropriatedScale && i >= refund1Scale)
			{
				// 用款申请
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode19);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode7);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(fundAppropriatedRiskCheckBusiCodeSum);
			}
			if (i < paymentScale && i >= appropriatedScale)
			{
				// 支付保证
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode25);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode26);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(paymentGuaranteeRiskCheckBusiCodeSum);
			}
			if (i < depositScale && i >= paymentScale)
			{
				// 存单管理
				tg_RiskRoutineCheckInfo.setBigBusiType(S_BusiCode.busiCode23);
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode24);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(depositManagementRiskCheckBusiCodeSum);
			}
			if (i < pjDevProgressScale && i >= depositScale)
			{
				// 工程进度节点更新
				tg_RiskRoutineCheckInfo.setBigBusiType("0303");
				tg_RiskRoutineCheckInfo.setSmallBusiType(S_BusiCode.busiCode_03030100);
				tg_RiskRoutineCheckInfo.setBusiCodeSummary(bldLimitAmountCheckBusiCodeSum);
			}

			session.save(tg_RiskRoutineCheckInfo);

			if (i % 100 == 0)
			{
				session.flush();
				session.clear();
			}

			// System.out.println("一次循环结束"+System.currentTimeMillis());
		}

		session.getTransaction().commit(); // 提交事物

		session.close();

		// System.out.println("结束存抽查业务明细"+myDatetime.dateToString2(System.currentTimeMillis()));

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	public static <T> Integer getRandomNum(Integer min, Integer max, List<T> result, List<T> sourceList)
	{
		Integer randNum = (int) Math.round(Math.random() * (max - min) + min);

		if (result.contains(sourceList.get(randNum)))
		{
			getRandomNum(min, max, result, sourceList);
			return null;
		}
		else
		{
			result.add(sourceList.get(randNum));

			return randNum;
		}
	}

	// 支付保证业务
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getEmpj_PaymentGuarantee(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Empj_PaymentGuaranteeForm empj_PaymentGuaranteeForm = new Empj_PaymentGuaranteeForm();
		empj_PaymentGuaranteeForm.setTheState(S_TheState.Normal);
		empj_PaymentGuaranteeForm.setApprovalState(S_ApprovalState.Completed);
		empj_PaymentGuaranteeForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		empj_PaymentGuaranteeForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Empj_PaymentGuarantee> randomPaymentGuaranteeList = new ArrayList<Empj_PaymentGuarantee>();
		List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList = empj_PaymentGuaranteeDao.findByPage(
				empj_PaymentGuaranteeDao.getQuery(empj_PaymentGuaranteeDao.getBasicHQL(), empj_PaymentGuaranteeForm));
		Integer length = empj_PaymentGuaranteeList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!empj_PaymentGuaranteeList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomPaymentGuaranteeList, empj_PaymentGuaranteeList);
			}
		}

		if (!randomPaymentGuaranteeList.isEmpty())
		{
			objectList.addAll(randomPaymentGuaranteeList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomPaymentGuaranteeList, S_BusiCode.busiCode25, S_BusiCode.busiCode26);

		return tg_RiskCheckBusiCodeSum;
	}

	// 存单
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getTg_DepositManagement(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Tg_DepositManagementForm tg_DepositManagementForm = new Tg_DepositManagementForm();
		tg_DepositManagementForm.setTheState(S_TheState.Normal);
		tg_DepositManagementForm.setApprovalState(S_ApprovalState.Completed);
		tg_DepositManagementForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		tg_DepositManagementForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Tg_DepositManagement> randomDepositManagementList = new ArrayList<Tg_DepositManagement>();
		List<Tg_DepositManagement> tg_DepositManagementList = tg_DepositManagementDao.findByPage(
				tg_DepositManagementDao.getQuery(tg_DepositManagementDao.getBasicHQL(), tg_DepositManagementForm));
		Integer length = tg_DepositManagementList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!tg_DepositManagementList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomDepositManagementList, tg_DepositManagementList);
			}
		}

		if (!randomDepositManagementList.isEmpty())
		{
			objectList.addAll(randomDepositManagementList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomDepositManagementList, S_BusiCode.busiCode23, S_BusiCode.busiCode24);

		return tg_RiskCheckBusiCodeSum;
	}

	// 退房退款申请-贷款未结清
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getTgpf_RefundInfo1(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Tgpf_RefundInfoForm tgpf_RefundInfoForm = new Tgpf_RefundInfoForm();
		tgpf_RefundInfoForm.setTheState(S_TheState.Normal);
		tgpf_RefundInfoForm.setApprovalState(S_ApprovalState.Completed);
		tgpf_RefundInfoForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		tgpf_RefundInfoForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		tgpf_RefundInfoForm.setRefundType("1");
		List<Tgpf_RefundInfo> randomRefundInfoList = new ArrayList<Tgpf_RefundInfo>();
		List<Tgpf_RefundInfo> tgpf_RefundInfoList = tgpf_RefundInfoDao
				.findByPage(tgpf_RefundInfoDao.getQuery(tgpf_RefundInfoDao.getBasicHQL(), tgpf_RefundInfoForm));
		Integer length = tgpf_RefundInfoList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!tgpf_RefundInfoList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomRefundInfoList, tgpf_RefundInfoList);
			}
		}

		if (!randomRefundInfoList.isEmpty())
		{
			objectList.addAll(randomRefundInfoList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomRefundInfoList, S_BusiCode.busiCode20, S_BusiCode.busiCode22);

		return tg_RiskCheckBusiCodeSum;
	}

	// 退房退款申请-贷款已结清
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getTgpf_RefundInfo0(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Tgpf_RefundInfoForm tgpf_RefundInfoForm = new Tgpf_RefundInfoForm();
		tgpf_RefundInfoForm.setTheState(S_TheState.Normal);
		tgpf_RefundInfoForm.setApprovalState(S_ApprovalState.Completed);
		tgpf_RefundInfoForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		tgpf_RefundInfoForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		tgpf_RefundInfoForm.setRefundType("0");
		List<Tgpf_RefundInfo> randomRefundInfoList = new ArrayList<Tgpf_RefundInfo>();
		List<Tgpf_RefundInfo> tgpf_RefundInfoList = tgpf_RefundInfoDao
				.findByPage(tgpf_RefundInfoDao.getQuery(tgpf_RefundInfoDao.getBasicHQL(), tgpf_RefundInfoForm));
		Integer length = tgpf_RefundInfoList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!tgpf_RefundInfoList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomRefundInfoList, tgpf_RefundInfoList);
			}
		}

		if (!randomRefundInfoList.isEmpty())
		{
			objectList.addAll(randomRefundInfoList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomRefundInfoList, S_BusiCode.busiCode20, S_BusiCode.busiCode21);

		return tg_RiskCheckBusiCodeSum;
	}

	// 用款申请
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getTgpf_FundAppropriated_AF(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Tgpf_FundAppropriated_AFForm tgpf_FundAppropriated_AFForm = new Tgpf_FundAppropriated_AFForm();
		tgpf_FundAppropriated_AFForm.setTheState(S_TheState.Normal);
		tgpf_FundAppropriated_AFForm.setApprovalState(S_ApprovalState.Completed);
		tgpf_FundAppropriated_AFForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		tgpf_FundAppropriated_AFForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Tgpf_FundAppropriated_AF> randomFundAppropriatedList = new ArrayList<Tgpf_FundAppropriated_AF>();
		List<Tgpf_FundAppropriated_AF> tgpf_FundAppropriated_AFList = tgpf_FundAppropriated_AFDao
				.findByPage(tgpf_FundAppropriated_AFDao.getQuery(tgpf_FundAppropriated_AFDao.getBasicHQL(),
						tgpf_FundAppropriated_AFForm));
		Integer length = tgpf_FundAppropriated_AFList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!tgpf_FundAppropriated_AFList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomFundAppropriatedList, tgpf_FundAppropriated_AFList);
			}
		}

		if (!randomFundAppropriatedList.isEmpty())
		{
			objectList.addAll(randomFundAppropriatedList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomFundAppropriatedList, S_BusiCode.busiCode19, S_BusiCode.busiCode7);

		return tg_RiskCheckBusiCodeSum;
	}

	// 三方协议
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getTgxy_TripleAgreement(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Tgxy_TripleAgreementForm tgxy_TripleAgreementForm = new Tgxy_TripleAgreementForm();
		tgxy_TripleAgreementForm.setTheState(S_TheState.Normal);
		tgxy_TripleAgreementForm.setApprovalState(S_ApprovalState.Completed);
		tgxy_TripleAgreementForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		tgxy_TripleAgreementForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Tgxy_TripleAgreement> randomTripleAgreementList = new ArrayList<Tgxy_TripleAgreement>();
		List<Tgxy_TripleAgreement> tgxy_TripleAgreementList = tgxy_TripleAgreementDao.findByPage(
				tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementForm));
		Integer length = tgxy_TripleAgreementList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!tgxy_TripleAgreementList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomTripleAgreementList, tgxy_TripleAgreementList);
			}
		}

		if (!randomTripleAgreementList.isEmpty())
		{
			objectList.addAll(randomTripleAgreementList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomTripleAgreementList, S_BusiCode.busiCode16, S_BusiCode.busiCode18);

		return tg_RiskCheckBusiCodeSum;
	}

	// 托管终止
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getEmpj_BldEscrowCompleted(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Empj_BldEscrowCompletedForm empj_BldEscrowCompletedForm = new Empj_BldEscrowCompletedForm();
		empj_BldEscrowCompletedForm.setTheState(S_TheState.Normal);
		empj_BldEscrowCompletedForm.setApprovalState(S_ApprovalState.Completed);
		empj_BldEscrowCompletedForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		empj_BldEscrowCompletedForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Empj_BldEscrowCompleted> randomBldEscrowCompletedList = new ArrayList<Empj_BldEscrowCompleted>();
		List<Empj_BldEscrowCompleted> Empj_BldEscrowCompletedList = empj_BldEscrowCompletedDao
				.findByPage(empj_BldEscrowCompletedDao.getQuery(empj_BldEscrowCompletedDao.getBasicHQL(),
						empj_BldEscrowCompletedForm));
		Integer length = Empj_BldEscrowCompletedList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!Empj_BldEscrowCompletedList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomBldEscrowCompletedList, Empj_BldEscrowCompletedList);
			}
		}

		if (!randomBldEscrowCompletedList.isEmpty())
		{
			objectList.addAll(randomBldEscrowCompletedList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomBldEscrowCompletedList, S_BusiCode.busiCode10, S_BusiCode.busiCode12);

		return tg_RiskCheckBusiCodeSum;
	}

	// 托管协议
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getTgxy_EscrowAgreement(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Tgxy_EscrowAgreementForm tgxy_EscrowAgreementForm = new Tgxy_EscrowAgreementForm();
		tgxy_EscrowAgreementForm.setTheState(S_TheState.Normal);
		tgxy_EscrowAgreementForm.setApprovalState(S_ApprovalState.Completed);
		tgxy_EscrowAgreementForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		tgxy_EscrowAgreementForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Tgxy_EscrowAgreement> randomEscrowAgreementList = new ArrayList<Tgxy_EscrowAgreement>();
		List<Tgxy_EscrowAgreement> tgxy_EscrowAgreementList = tgxy_EscrowAgreementDao.findByPage(
				tgxy_EscrowAgreementDao.getQuery(tgxy_EscrowAgreementDao.getBasicHQL(), tgxy_EscrowAgreementForm));
		Integer length = tgxy_EscrowAgreementList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!tgxy_EscrowAgreementList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomEscrowAgreementList, tgxy_EscrowAgreementList);
			}
		}

		if (!randomEscrowAgreementList.isEmpty())
		{
			objectList.addAll(randomEscrowAgreementList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomEscrowAgreementList, S_BusiCode.busiCode13, S_BusiCode.busiCode15);

		return tg_RiskCheckBusiCodeSum;
	}

	// 受限额度变更
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getEmpj_BldLimitAmountAFCheckInfo(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Empj_BldLimitAmount_AFForm empj_BldLimitAmount_AFForm = new Empj_BldLimitAmount_AFForm();
		empj_BldLimitAmount_AFForm.setTheState(S_TheState.Normal);
		empj_BldLimitAmount_AFForm.setApprovalState(S_ApprovalState.Completed);
		empj_BldLimitAmount_AFForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		empj_BldLimitAmount_AFForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Empj_BldLimitAmount_AF> randomBldLimitAmountList = new ArrayList<Empj_BldLimitAmount_AF>();
		List<Empj_BldLimitAmount_AF> empj_BldLimitAmount_AFList = empj_BldLimitAmount_AFDao
				.findByPage(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getBasicHQL(),
						empj_BldLimitAmount_AFForm));

		Integer length = empj_BldLimitAmount_AFList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!empj_BldLimitAmount_AFList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomBldLimitAmountList, empj_BldLimitAmount_AFList);
			}
		}

		if (randomBldLimitAmountList != null && !randomBldLimitAmountList.isEmpty())
		{
			objectList.addAll(randomBldLimitAmountList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomBldLimitAmountList, S_BusiCode.busiCode10, S_BusiCode.busiCode11);

		return tg_RiskCheckBusiCodeSum;
	}

	// 工程进度节点变更
	@SuppressWarnings("unchecked")
	public Tg_RiskCheckBusiCodeSum getEmpj_BldLimitAmountCheckInfo(List<Object> objectList, String spotTimeStr,
			Long[] rangeTimeStamp, Double theRatio, MyDatetime myDatetime, Tg_RiskRoutineCheckInfoForm model)
	{
		count++;
		Empj_BldLimitAmountForm empj_BldLimitAmountForm = new Empj_BldLimitAmountForm();
		empj_BldLimitAmountForm.setTheState(S_TheState.Normal);
		empj_BldLimitAmountForm.setApprovalState(S_ApprovalState.Completed);
		empj_BldLimitAmountForm.setCheckStartTimeStamp(rangeTimeStamp[0]);
		empj_BldLimitAmountForm.setCheckEndTimeStamp(rangeTimeStamp[1]);
		List<Empj_BldLimitAmount> randomBldLimitAmountList = new ArrayList<Empj_BldLimitAmount>();
		List<Empj_BldLimitAmount> empj_BldLimitAmountList = empj_BldLimitAmountDao
				.findByPage(empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicHQL(),
						empj_BldLimitAmountForm));

		Integer length = empj_BldLimitAmountList.size();
		Integer randomcount = 1;
		if (theRatio * length > 1)
		{
			randomcount = (int) (theRatio * length);
		}

		if (!empj_BldLimitAmountList.isEmpty())
		{
			for (int i = 0; i < randomcount; i++)
			{
				getRandomNum(0, length - 1, randomBldLimitAmountList, empj_BldLimitAmountList);
			}
		}

		if (randomBldLimitAmountList != null && !randomBldLimitAmountList.isEmpty())
		{
			objectList.addAll(randomBldLimitAmountList);
		}

		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = saveRiskCheckBusiCodeSum(model, myDatetime, spotTimeStr,
				randomBldLimitAmountList, "0303", S_BusiCode.busiCode_03030100);

		return tg_RiskCheckBusiCodeSum;
	}

	// 保存业务汇总
	public <T> Tg_RiskCheckBusiCodeSum saveRiskCheckBusiCodeSum(Tg_RiskRoutineCheckInfoForm model,
			MyDatetime myDatetime, String spotTimeStr, List<T> randomList, String bigBusiType, String smallBusiType)
	{
		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();

		tg_RiskCheckBusiCodeSum.setTheState(S_TheState.Cache);
		tg_RiskCheckBusiCodeSum.setUserStart(model.getUser());
		tg_RiskCheckBusiCodeSum.setCreateTimeStamp(System.currentTimeMillis());
		tg_RiskCheckBusiCodeSum.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));
		tg_RiskCheckBusiCodeSum.setBigBusiType(bigBusiType);
		tg_RiskCheckBusiCodeSum.setSmallBusiType(smallBusiType);
		tg_RiskCheckBusiCodeSum.setQualifiedCount(0);
		tg_RiskCheckBusiCodeSum.setUnqualifiedCount(0);
		tg_RiskCheckBusiCodeSum.setPushCount(0);
		tg_RiskCheckBusiCodeSum.setFeedbackCount(0);
		tg_RiskCheckBusiCodeSum.setHandleCount(0);
		tg_RiskCheckBusiCodeSum.setSumCheckCount(0);
		tg_RiskCheckBusiCodeSum.setUserUpdate(model.getUser());
		tg_RiskCheckBusiCodeSum.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_RiskCheckBusiCodeSum.setEntryState(S_EntryState.Doing);
		tg_RiskCheckBusiCodeSum.setRectificationState(S_RectificationState.Doing);
		tg_RiskCheckBusiCodeSum.setSumCheckCount(randomList.size());

		if (!randomList.isEmpty())
		{
			tg_RiskCheckBusiCodeSumDao.save(tg_RiskCheckBusiCodeSum);

			return tg_RiskCheckBusiCodeSum;
		}

		return null;
	}

	public void saveCheckInfo(Tg_RiskRoutineCheckInfoForm model, String yearMonthStr, MyDatetime myDatetime,
			String spotTimeStr, String eCode, String bigBusiType, String smallBusiType)
	{
		Tg_RiskRoutineCheckInfo tg_RiskRoutineCheckInfo = new Tg_RiskRoutineCheckInfo();

		tg_RiskRoutineCheckInfo.setTheState(S_TheState.Cache);
		// tg_RiskRoutineCheckInfo.setBusiState(busiState);
		tg_RiskRoutineCheckInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tg_RiskRoutineCheckInfo.setUserStart(model.getUser());
		tg_RiskRoutineCheckInfo.setCreateTimeStamp(System.currentTimeMillis());
		// tg_RiskRoutineCheckInfo.setUserUpdate(model.getUser());
		// tg_RiskRoutineCheckInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		// tg_RiskRoutineCheckInfo.setUserRecord(userRecord);
		// tg_RiskRoutineCheckInfo.setRecordTimeStamp(recordTimeStamp);
		tg_RiskRoutineCheckInfo.setCheckNumber("FKCC" + yearMonthStr);
		tg_RiskRoutineCheckInfo.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));
		tg_RiskRoutineCheckInfo.setBigBusiType(bigBusiType);
		tg_RiskRoutineCheckInfo.setSmallBusiType(smallBusiType);
		tg_RiskRoutineCheckInfo.seteCodeOfBill(eCode);
		tg_RiskRoutineCheckInfo.setIsChoosePush(S_YesNoStr.No);
		tg_RiskRoutineCheckInfo.setIsDoPush(S_YesNoStr.No);
		tg_RiskRoutineCheckInfo.setIsModify(S_YesNoStr.No);
		tg_RiskRoutineCheckInfo.setIsHandle(S_YesNoStr.No);
		tg_RiskRoutineCheckInfo.setRectificationState(S_RectificationState.Doing);
		tg_RiskRoutineCheckInfo.setEntryState(S_EntryState.Doing);

		tg_RiskRoutineCheckInfoDao.save(tg_RiskRoutineCheckInfo);

		Tg_RiskRoutineCheckInfoForm tg_RiskRoutineCheckInfoForm = new Tg_RiskRoutineCheckInfoForm();
		tg_RiskRoutineCheckInfoForm.setTheState(S_TheState.Cache);
		tg_RiskRoutineCheckInfoForm.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));
		Tg_RiskCheckMonthSum tg_RiskCheckMonthSum = tg_RiskCheckMonthSumDao.findOneByQuery_T(
				tg_RiskCheckMonthSumDao.getQuery(tg_RiskCheckMonthSumDao.getBasicHQL(), tg_RiskRoutineCheckInfoForm));
		if (tg_RiskCheckMonthSum == null)
		{
			tg_RiskCheckMonthSum = new Tg_RiskCheckMonthSum();
			tg_RiskCheckMonthSum.setTheState(S_TheState.Cache);
			tg_RiskCheckMonthSum.setUserStart(model.getUser());
			tg_RiskCheckMonthSum.setCreateTimeStamp(System.currentTimeMillis());
			tg_RiskCheckMonthSum.setCheckNumber("FKCC" + yearMonthStr);
			tg_RiskCheckMonthSum.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));
			tg_RiskCheckMonthSum.setSumCheckCount(1);
			tg_RiskCheckMonthSum.setQualifiedCount(0);
			tg_RiskCheckMonthSum.setUnqualifiedCount(0);
			tg_RiskCheckMonthSum.setPushCount(0);
			tg_RiskCheckMonthSum.setFeedbackCount(0);
			tg_RiskCheckMonthSum.setHandleCount(0);
		}
		else
		{
			tg_RiskCheckMonthSum.setSumCheckCount(tg_RiskCheckMonthSum.getSumCheckCount() + 1);
		}
		tg_RiskCheckMonthSum.setUserUpdate(model.getUser());
		tg_RiskCheckMonthSum.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_RiskCheckMonthSum.setRectificationState(S_RectificationState.Doing);

		tg_RiskCheckMonthSumDao.save(tg_RiskCheckMonthSum);

		tg_RiskRoutineCheckInfoForm.setBigBusiType(bigBusiType);
		tg_RiskRoutineCheckInfoForm.setSmallBusiType(smallBusiType);
		Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum = tg_RiskCheckBusiCodeSumDao
				.findOneByQuery_T(tg_RiskCheckBusiCodeSumDao.getQuery(tg_RiskCheckBusiCodeSumDao.getBasicHQL(),
						tg_RiskRoutineCheckInfoForm));
		if (tg_RiskCheckBusiCodeSum == null)
		{
			tg_RiskCheckBusiCodeSum = new Tg_RiskCheckBusiCodeSum();

			tg_RiskCheckBusiCodeSum.setTheState(S_TheState.Cache);
			tg_RiskCheckBusiCodeSum.setUserStart(model.getUser());
			tg_RiskCheckBusiCodeSum.setCreateTimeStamp(System.currentTimeMillis());
			tg_RiskCheckBusiCodeSum.setSpotTimeStamp(myDatetime.stringToLong(spotTimeStr + "-01"));
			tg_RiskCheckBusiCodeSum.setBigBusiType(bigBusiType);
			tg_RiskCheckBusiCodeSum.setSmallBusiType(smallBusiType);
			tg_RiskCheckBusiCodeSum.setSumCheckCount(1);
			tg_RiskCheckBusiCodeSum.setQualifiedCount(0);
			tg_RiskCheckBusiCodeSum.setUnqualifiedCount(0);
			tg_RiskCheckBusiCodeSum.setPushCount(0);
			tg_RiskCheckBusiCodeSum.setFeedbackCount(0);
			tg_RiskCheckBusiCodeSum.setHandleCount(0);
		}
		else
		{
			tg_RiskCheckBusiCodeSum.setSumCheckCount(tg_RiskCheckBusiCodeSum.getSumCheckCount() + 1);
		}

		tg_RiskCheckBusiCodeSum.setUserUpdate(model.getUser());
		tg_RiskCheckBusiCodeSum.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_RiskCheckBusiCodeSum.setEntryState(S_EntryState.Doing);
		tg_RiskCheckBusiCodeSum.setRectificationState(S_RectificationState.Doing);

		tg_RiskCheckBusiCodeSumDao.save(tg_RiskCheckBusiCodeSum);

		tg_RiskRoutineCheckInfo.setMonthSummary(tg_RiskCheckMonthSum);
		tg_RiskRoutineCheckInfo.setBusiCodeSummary(tg_RiskCheckBusiCodeSum);
		tg_RiskRoutineCheckInfoDao.save(tg_RiskRoutineCheckInfo);
	}

	@SuppressWarnings("unchecked")
	public void deleteCache(Long spotTimeStamp)
	{
		Tg_RiskRoutineCheckInfoForm tg_RiskRoutineCheckInfoForm = new Tg_RiskRoutineCheckInfoForm();
		tg_RiskRoutineCheckInfoForm.setTheState(S_TheState.Cache);
		tg_RiskRoutineCheckInfoForm.setSpotTimeStamp(spotTimeStamp);
		List<Tg_RiskRoutineCheckInfo> tg_RiskRoutineCheckInfoList = tg_RiskRoutineCheckInfoDao
				.findByPage(tg_RiskRoutineCheckInfoDao.getQuery(tg_RiskRoutineCheckInfoDao.getBasicHQL(),
						tg_RiskRoutineCheckInfoForm));

		Tg_RiskCheckBusiCodeSumForm tg_RiskCheckBusiCodeSumForm = new Tg_RiskCheckBusiCodeSumForm();
		tg_RiskCheckBusiCodeSumForm.setTheState(S_TheState.Cache);
		tg_RiskCheckBusiCodeSumForm.setSpotTimeStamp(spotTimeStamp);
		List<Tg_RiskCheckBusiCodeSum> tg_RiskCheckBusiCodeSumList = tg_RiskCheckBusiCodeSumDao
				.findByPage(tg_RiskCheckBusiCodeSumDao.getQuery(tg_RiskCheckBusiCodeSumDao.getBasicHQL(),
						tg_RiskCheckBusiCodeSumForm));

		Tg_RiskCheckMonthSumForm tg_RiskCheckMonthSumForm = new Tg_RiskCheckMonthSumForm();
		tg_RiskCheckMonthSumForm.setTheState(S_TheState.Cache);
		tg_RiskCheckMonthSumForm.setSpotTimeStamp(spotTimeStamp);
		List<Tg_RiskCheckMonthSum> tg_RiskCheckMonthSumList = tg_RiskCheckMonthSumDao.findByPage(
				tg_RiskCheckMonthSumDao.getQuery(tg_RiskCheckMonthSumDao.getBasicHQL(), tg_RiskCheckMonthSumForm));

		for (Tg_RiskRoutineCheckInfo tg_RiskRoutineCheckInfo : tg_RiskRoutineCheckInfoList)
		{
			tg_RiskRoutineCheckInfo.setTheState(S_TheState.Deleted);
			tg_RiskRoutineCheckInfoDao.save(tg_RiskRoutineCheckInfo);
		}
		for (Tg_RiskCheckBusiCodeSum tg_RiskCheckBusiCodeSum : tg_RiskCheckBusiCodeSumList)
		{
			tg_RiskCheckBusiCodeSum.setTheState(S_TheState.Deleted);
			tg_RiskCheckBusiCodeSumDao.save(tg_RiskCheckBusiCodeSum);
		}
		for (Tg_RiskCheckMonthSum tg_RiskCheckMonthSum : tg_RiskCheckMonthSumList)
		{
			tg_RiskCheckMonthSum.setTheState(S_TheState.Deleted);
			tg_RiskCheckMonthSumDao.save(tg_RiskCheckMonthSum);
		}
	}
}

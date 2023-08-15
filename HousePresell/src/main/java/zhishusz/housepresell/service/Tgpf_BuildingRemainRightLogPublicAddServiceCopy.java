package zhishusz.housepresell.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.core.util.NumberUtil;

/*
 * Service添加操作：公共留存权益计算工具
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightLogPublicAddServiceCopy {
	private static final String BUSI_CODE = "200302";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BuildingRemainRightLogForm model) {
		Properties properties = new MyProperties();

		Long buildingId = model.getBuildingId();// 楼幢Id
		String billTimeStamp = model.getBillTimeStamp();// 记账日期
		String srcBusiType = model.getSrcBusiType();// 来源业务类型

		if (buildingId == null || buildingId < 1) {
			return MyBackInfo.fail(properties, "请选择需要计算留存权益的楼幢");
		}

		if (billTimeStamp == null || billTimeStamp.length() < 1)
			billTimeStamp = MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis());

		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildingId);
		if (empj_BuildingInfo == null) {
			return MyBackInfo.fail(properties, "选择的楼幢不存在");
		}

		Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = new Tgpf_BuildingRemainRightLog();
		tgpf_BuildingRemainRightLog.setTheState(S_TheState.Normal);
		tgpf_BuildingRemainRightLog.setBusiState(Tgpf_BuildingRemainRightLog.Uncompared);
		tgpf_BuildingRemainRightLog.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_BuildingRemainRightLog.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
//		tgpf_BuildingRemainRightLog.setUserStart(userStart);
//		tgpf_BuildingRemainRightLog.setUserUpdate(userUpdate);
//		tgpf_BuildingRemainRightLog.setUserRecord(userRecord);
//		tgpf_BuildingRemainRightLog.setRecordTimeStamp(recordTimeStamp);
		tgpf_BuildingRemainRightLog.setProject(empj_BuildingInfo.getProject());
		if (empj_BuildingInfo.getProject() != null) {
			tgpf_BuildingRemainRightLog.setTheNameOfProject(empj_BuildingInfo.getProject().getTheName());
			tgpf_BuildingRemainRightLog.seteCodeOfProject(empj_BuildingInfo.getProject().geteCode());
		}
		// 开发企业
		tgpf_BuildingRemainRightLog.setDevelopCompany(empj_BuildingInfo.getDevelopCompany());
		if (empj_BuildingInfo.getDevelopCompany() != null) {
			tgpf_BuildingRemainRightLog.seteCodeOfDevelopCompany(empj_BuildingInfo.getDevelopCompany().geteCode());
		}
		tgpf_BuildingRemainRightLog.setBuilding(empj_BuildingInfo);
		tgpf_BuildingRemainRightLog.seteCodeFromConstruction(empj_BuildingInfo.geteCodeFromConstruction());// 施工编号
		tgpf_BuildingRemainRightLog.seteCodeFromPublicSecurity(empj_BuildingInfo.geteCodeFromPublicSecurity());// 公安编号
		tgpf_BuildingRemainRightLog.setBuildingAccount(empj_BuildingInfo.getBuildingAccount());
		tgpf_BuildingRemainRightLog.setBuildingExtendInfo(empj_BuildingInfo.getExtendInfo());
		if (empj_BuildingInfo.getBuildingAccount() != null) {
			tgpf_BuildingRemainRightLog
					.setCurrentFigureProgress(empj_BuildingInfo.getBuildingAccount().getCurrentFigureProgress());
			tgpf_BuildingRemainRightLog
					.setCurrentLimitedRatio(empj_BuildingInfo.getBuildingAccount().getCurrentLimitedRatio());
			tgpf_BuildingRemainRightLog
					.setNodeLimitedAmount(empj_BuildingInfo.getBuildingAccount().getNodeLimitedAmount());
		}
		tgpf_BuildingRemainRightLog.setSrcBusiType(srcBusiType);
		/**
		 * 楼幢入账总金额=该户所在楼幢托管入账总金额-已退房退款金额
		 */
		// 该户所在楼幢托管入账总金额
		Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();
		Double totalAccountAmount = null;
		if (buildingAccount != null && buildingAccount.getTotalAccountAmount() != null) {
			totalAccountAmount = buildingAccount.getTotalAccountAmount()
					+ (buildingAccount.getApplyRefundPayoutAmount() == null ? 0.0
							: buildingAccount.getApplyRefundPayoutAmount())
					+ (buildingAccount.getRefundAmount() == null ? 0.0 : buildingAccount.getRefundAmount());
		} else {
			totalAccountAmount = 0.0;
		}

		// 楼幢入账总金额
		tgpf_BuildingRemainRightLog.setTotalAccountAmount(totalAccountAmount);
		tgpf_BuildingRemainRightLog.setBillTimeStamp(billTimeStamp);
		tgpf_BuildingRemainRightLog.setSrcBusiType(srcBusiType);
		tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);

		List<Empj_HouseInfo> empj_HouseInfoList = empj_HouseInfoDao
				.findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getBuildingHQL2(), model));
		Integer count = 0;
		Double theSurplusAmount = 0.0;
		for (Empj_HouseInfo empj_HouseInfo : empj_HouseInfoList) {
			Tgxy_TripleAgreement tgxy_TripleAgreement = empj_HouseInfo.getTripleAgreement();

			count++;
			if (count == empj_HouseInfoList.size()) {
				// 针对每一个户室生成一个该记账日期下的Tgpf_RemainRight户室的留存权益
				Tgpf_RemainRight tgpf_RemainRight = new Tgpf_RemainRight();
				tgpf_RemainRight.setTheState(S_TheState.Normal);
				tgpf_RemainRight.setCreateTimeStamp(System.currentTimeMillis());
				tgpf_RemainRight.setBusiState(Tgpf_BuildingRemainRightLog.Uncompared);
				tgpf_RemainRight.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
//				tgpf_RemainRight.setUserStart(userStart);
//				tgpf_RemainRight.setLastUpdateTimeStamp(lastUpdateTimeStamp);
//				tgpf_RemainRight.setUserRecord(userRecord);
//				tgpf_RemainRight.setRecordTimeStamp(recordTimeStamp);
				tgpf_RemainRight.setEnterTimeStamp(MyDatetime.getInstance().stringToLong(billTimeStamp));
				tgpf_RemainRight.setBuildingRemainRightLog(tgpf_BuildingRemainRightLog);

				if (tgxy_TripleAgreement != null) {
					tgpf_RemainRight.setBuyer(tgxy_TripleAgreement.getBuyerName());

					tgpf_RemainRight
							.seteCodeOfTripleAgreement(empj_HouseInfo.getTripleAgreement().geteCodeOfTripleAgreement());

					tgpf_RemainRight.setActualDepositAmount(empj_HouseInfo.getTripleAgreement().getTotalAmount());
					tgpf_RemainRight
							.setDepositAmountFromLoan(empj_HouseInfo.getTripleAgreement().getTotalAmountOfHouse());

					Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
					tgpf_DepositDetailForm.setTripleAgreement(tgxy_TripleAgreement);
					tgpf_DepositDetailForm.setTheStateFromReverse(0);
					tgpf_DepositDetailForm.setTheState(S_TheState.Normal);
					List<Tgpf_DepositDetail> tgpf_DepositDetailList = tgpf_DepositDetailDao
							.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(),
									tgpf_DepositDetailForm));
					String theNameOfCreditor = "";
					String idNumberOfCreditor = "";
					Map<String, String> map = new HashMap<String, String>();
					for (Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList) {
						if (map.get(tgpf_DepositDetail.getIdNumber()) == null) {
							map.put(tgpf_DepositDetail.getIdNumber(), tgpf_DepositDetail.getTheNameOfCreditor());
						}
					}
					for (String idNumber : map.keySet()) {
						theNameOfCreditor += map.get(idNumber) + ";";
						idNumberOfCreditor += idNumber + ";";
					}

					tgpf_RemainRight.setTheNameOfCreditor(theNameOfCreditor);
					tgpf_RemainRight.setIdNumberOfCreditor(idNumberOfCreditor);
				}

				if (empj_HouseInfo.getContractInfo() != null) {
					tgpf_RemainRight
							.seteCodeOfContractRecord(empj_HouseInfo.getContractInfo().geteCodeOfContractRecord());
				}

				tgpf_RemainRight.setSrcBusiType(srcBusiType);
				tgpf_RemainRight.setProject(empj_BuildingInfo.getProject());
				if (empj_BuildingInfo.getProject() != null) {
					tgpf_RemainRight.setTheNameOfProject(empj_BuildingInfo.getProject().getTheName());
				}
				tgpf_RemainRight.setBuilding(empj_BuildingInfo);
				tgpf_RemainRight.seteCodeOfBuilding(empj_BuildingInfo.geteCodeFromConstruction());
				tgpf_RemainRight.setBuildingUnit(empj_HouseInfo.getUnitInfo());
				if (empj_HouseInfo.getUnitInfo() != null) {
					tgpf_RemainRight.seteCodeOfBuildingUnit(empj_HouseInfo.geteCodeOfUnitInfo());
				}
				tgpf_RemainRight.setHouse(empj_HouseInfo);
				tgpf_RemainRight.seteCodeFromRoom(empj_HouseInfo.getRoomId());

//				tgpf_RemainRight.setDepositAmountFromLoan();
//				tgpf_RemainRight.setTheAccountFromLoan(theAccountFromLoan);
//				tgpf_RemainRight.setFundProperty(fundProperty);
//				tgpf_RemainRight.setBank(bank);
//				tgpf_RemainRight.setTheNameOfBankPayedIn(theNameOfBankPayedIn);
//				tgpf_RemainRight.setCurrentDividedAmout(currentDividedAmout);
//				tgpf_RemainRight.setRemark(remark);

				// 户入账金额
				Double actualDepositAmount = tgpf_RemainRight.getDepositAmountFromLoan() == null ? 0
						: tgpf_RemainRight.getDepositAmountFromLoan();
				// 楼幢入账总金额
				Double buildingTotalAccountAmount = tgpf_BuildingRemainRightLog.getTotalAccountAmount() == null ? 0
						: tgpf_BuildingRemainRightLog.getTotalAccountAmount();

				BigDecimal data1 = new BigDecimal(buildingTotalAccountAmount);
				BigDecimal data2 = new BigDecimal(0.0);
				int result = data1.compareTo(data2);
				if (result == 0) // 为0
				{
					return MyBackInfo.fail(properties, "楼幢总金额为0！");
				}

				// 留存权益系数
				Double theRatio = actualDepositAmount / buildingTotalAccountAmount;
				// 幢托管资金实际可用余额
				Double currentEscrowFund = empj_BuildingInfo.getBuildingAccount() == null ? 0
						: empj_BuildingInfo.getBuildingAccount().getCurrentEscrowFund();
				// 幢托管受限额度
				Double effectiveLimitedAmount = empj_BuildingInfo.getBuildingAccount() == null ? 0
						: empj_BuildingInfo.getBuildingAccount().getEffectiveLimitedAmount();

				Double theAmount = NumberUtil.round(currentEscrowFund - theSurplusAmount, 2).doubleValue();

				tgpf_RemainRight.setTheRatio(NumberUtil.round(theRatio, 11).doubleValue());// 留存权益系数（保留11位小数）
				tgpf_RemainRight.setTheAmount(theAmount);// 留存权益总金额
				if (effectiveLimitedAmount < currentEscrowFund) {
					tgpf_RemainRight.setLimitedRetainRight(NumberUtil.round(theRatio * effectiveLimitedAmount, 2).doubleValue());// 受限权益（未到期留存权益）
				} else {
					tgpf_RemainRight.setLimitedRetainRight(NumberUtil.round(theRatio * currentEscrowFund, 2).doubleValue());// 受限权益（未到期留存权益）
				}
				tgpf_RemainRight.setWithdrawableRetainRight((NumberUtil.round(theAmount - tgpf_RemainRight.getLimitedRetainRight(), 2).doubleValue()));// 可支取权益（到期留存权益）

				Double retainedEquity = empj_HouseInfo.getTripleAgreement() == null ? 0
						: (empj_HouseInfo.getTripleAgreement().getTheAmountOfRetainedEquity() == null ? 0
								: empj_HouseInfo.getTripleAgreement().getTheAmountOfRetainedEquity());
				tgpf_RemainRight.setCurrentDividedAmout(retainedEquity - theAmount);
//				tgpf_RemainRight.setCurrentDividedAmout(empj_HouseInfo.getTripleAgreement().getTheAmountOfRetainedEquity()-theAmount);

				tgpf_RemainRightDao.save(tgpf_RemainRight);

				// 更改三方协议的留存权益
				/**
				 * xsz by time 2019-2-18 17:47:15
				 * 非手工触发也更新对应三方协议数据
				 */
//				if (!srcBusiType.equals("业务触发")) {
//					if (tgxy_TripleAgreement != null) {
//						tgxy_TripleAgreement.setTheAmountOfRetainedEquity(theAmount);
//						
//						if (effectiveLimitedAmount < currentEscrowFund) {
//							tgxy_TripleAgreement.setTheAmountOfInterestRetained(theRatio * effectiveLimitedAmount);
//							tgxy_TripleAgreement// 受限权益（未到期留存权益）
//									.setTheAmountOfInterestUnRetained(theAmount - theRatio * effectiveLimitedAmount);
//							
//						} else {
//							tgxy_TripleAgreement.setTheAmountOfInterestRetained(theRatio * currentEscrowFund);// 受限权益（未到期留存权益）
//							tgxy_TripleAgreement// 受限权益（未到期留存权益）
//									.setTheAmountOfInterestUnRetained(theAmount - theRatio * currentEscrowFund);
//						}
//						
//						tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
//					}
//				}
				
				if (tgxy_TripleAgreement != null) {
					tgxy_TripleAgreement.setTheAmountOfRetainedEquity(NumberUtil.round(theAmount, 2).doubleValue());
					
					if (effectiveLimitedAmount < currentEscrowFund) {
						tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(NumberUtil.round(theRatio * effectiveLimitedAmount, 2).doubleValue());
						tgxy_TripleAgreement// 受限权益（未到期留存权益）
								.setTheAmountOfInterestRetained(NumberUtil.round(theAmount - theRatio * effectiveLimitedAmount, 2).doubleValue());
						
					} else {
						tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(NumberUtil.round(theRatio * currentEscrowFund, 2).doubleValue());// 受限权益（未到期留存权益）
						tgxy_TripleAgreement// 受限权益（未到期留存权益）
								.setTheAmountOfInterestRetained(NumberUtil.round(theAmount - theRatio * currentEscrowFund, 2).doubleValue());
					}
					
					tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
				}

				continue;
			}
			// 针对每一个户室生成一个该记账日期下的Tgpf_RemainRight户室的留存权益
			Tgpf_RemainRight tgpf_RemainRight = new Tgpf_RemainRight();
			tgpf_RemainRight.setTheState(S_TheState.Normal);
			tgpf_RemainRight.setCreateTimeStamp(System.currentTimeMillis());
			tgpf_RemainRight.setBusiState(Tgpf_BuildingRemainRightLog.Uncompared);
			tgpf_RemainRight.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
//			tgpf_RemainRight.setUserStart(userStart);
//			tgpf_RemainRight.setLastUpdateTimeStamp(lastUpdateTimeStamp);
//			tgpf_RemainRight.setUserRecord(userRecord);
//			tgpf_RemainRight.setRecordTimeStamp(recordTimeStamp);
			tgpf_RemainRight.setEnterTimeStamp(MyDatetime.getInstance().stringToLong(billTimeStamp));
			tgpf_RemainRight.setBuildingRemainRightLog(tgpf_BuildingRemainRightLog);

			if (tgxy_TripleAgreement != null) {
				tgpf_RemainRight.setBuyer(tgxy_TripleAgreement.getBuyerName());
				tgpf_RemainRight.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
				tgpf_RemainRight.setActualDepositAmount(empj_HouseInfo.getTripleAgreement().getTotalAmount());
				tgpf_RemainRight.setDepositAmountFromLoan(empj_HouseInfo.getTripleAgreement().getTotalAmountOfHouse());

				Tgpf_DepositDetailForm tgpf_DepositDetailForm = new Tgpf_DepositDetailForm();
				tgpf_DepositDetailForm.setTripleAgreement(tgxy_TripleAgreement);
				tgpf_DepositDetailForm.setTheStateFromReverse(0);
				tgpf_DepositDetailForm.setTheState(S_TheState.Normal);
				List<Tgpf_DepositDetail> tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(
						tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), tgpf_DepositDetailForm));
				String theNameOfCreditor = "";
				String idNumberOfCreditor = "";
				Map<String, String> map = new HashMap<String, String>();
				for (Tgpf_DepositDetail tgpf_DepositDetail : tgpf_DepositDetailList) {
					if (map.get(tgpf_DepositDetail.getIdNumber()) == null) {
						map.put(tgpf_DepositDetail.getIdNumber(), tgpf_DepositDetail.getTheNameOfCreditor());
					}
				}
				for (String idNumber : map.keySet()) {
					theNameOfCreditor += map.get(idNumber) + ";";
					idNumberOfCreditor += idNumber + ";";
				}

				tgpf_RemainRight.setTheNameOfCreditor(theNameOfCreditor);
				tgpf_RemainRight.setIdNumberOfCreditor(idNumberOfCreditor);
			}
			
			try
			{
				Tgxy_ContractInfo contractInfo = empj_HouseInfo.getContractInfo();
				
				if (contractInfo != null) 
				{
					tgpf_RemainRight.seteCodeOfContractRecord(contractInfo.geteCodeOfContractRecord());
				}
			}
			catch (Exception e)
			{
				tgpf_RemainRight.seteCodeOfContractRecord(null);
			}
			
			tgpf_RemainRight.setSrcBusiType(srcBusiType);
			tgpf_RemainRight.setProject(empj_BuildingInfo.getProject());
			if (empj_BuildingInfo.getProject() != null) {
				tgpf_RemainRight.setTheNameOfProject(empj_BuildingInfo.getProject().getTheName());
			}
			tgpf_RemainRight.setBuilding(empj_BuildingInfo);
			tgpf_RemainRight.seteCodeOfBuilding(empj_BuildingInfo.geteCodeFromConstruction());
			tgpf_RemainRight.setBuildingUnit(empj_HouseInfo.getUnitInfo());
			if (empj_HouseInfo.getUnitInfo() != null) {
				tgpf_RemainRight.seteCodeOfBuildingUnit(empj_HouseInfo.geteCodeOfUnitInfo());
			}
			tgpf_RemainRight.setHouse(empj_HouseInfo);
			tgpf_RemainRight.seteCodeFromRoom(empj_HouseInfo.getRoomId());
//			tgpf_RemainRight.setDepositAmountFromLoan();
//			tgpf_RemainRight.setTheAccountFromLoan(theAccountFromLoan);
//			tgpf_RemainRight.setFundProperty(fundProperty);
//			tgpf_RemainRight.setBank(bank);
//			tgpf_RemainRight.setTheNameOfBankPayedIn(theNameOfBankPayedIn);
//			tgpf_RemainRight.setRemark(remark);

			// 户入账金额
			Double actualDepositAmount = tgpf_RemainRight.getDepositAmountFromLoan() == null ? 0.0
					: tgpf_RemainRight.getDepositAmountFromLoan();
			// 楼幢入账总金额
			Double buildingTotalAccountAmount = tgpf_BuildingRemainRightLog.getTotalAccountAmount();

			BigDecimal data1 = new BigDecimal(buildingTotalAccountAmount);
			BigDecimal data2 = new BigDecimal(0.0);
			int result = data1.compareTo(data2);
			if (result == 0) // 为0
			{
				return MyBackInfo.fail(properties, "楼幢总金额为0！");
			}

			// 留存权益系数
			Double theRatio = actualDepositAmount / buildingTotalAccountAmount;
			// 幢托管资金实际可用余额
			Double currentEscrowFund = empj_BuildingInfo.getBuildingAccount() == null ? 0
					: (empj_BuildingInfo.getBuildingAccount().getCurrentEscrowFund() == null ? 0
							: empj_BuildingInfo.getBuildingAccount().getCurrentEscrowFund());

			// 幢托管受限额度
			Double effectiveLimitedAmount = empj_BuildingInfo.getBuildingAccount() == null ? 0
					: (empj_BuildingInfo.getBuildingAccount().getEffectiveLimitedAmount() == null ? 0
							: empj_BuildingInfo.getBuildingAccount().getEffectiveLimitedAmount());

			BigDecimal bigDecimal = null;

			bigDecimal = NumberUtil.round(theRatio, 11);

			tgpf_RemainRight.setTheRatio(bigDecimal.doubleValue());// 留存权益系数（保留11位小数）
			tgpf_RemainRight.setTheAmount(NumberUtil.round(theRatio * currentEscrowFund, 2).doubleValue());// 留存权益总金额
			if (effectiveLimitedAmount < currentEscrowFund) {
				tgpf_RemainRight.setLimitedRetainRight(NumberUtil.round(theRatio * effectiveLimitedAmount, 2).doubleValue());// 受限权益（未到期留存权益）
			} else {
				tgpf_RemainRight.setLimitedRetainRight(NumberUtil.round(theRatio * currentEscrowFund, 2).doubleValue());// 受限权益（未到期留存权益）
			}

			tgpf_RemainRight.setWithdrawableRetainRight(
					NumberUtil.round(theRatio * currentEscrowFund - tgpf_RemainRight.getLimitedRetainRight(), 2).doubleValue());// 可支取权益（到期留存权益）

			Double retainedEquity = empj_HouseInfo.getTripleAgreement() == null ? 0
					: (empj_HouseInfo.getTripleAgreement().getTheAmountOfRetainedEquity() == null ? 0
							: empj_HouseInfo.getTripleAgreement().getTheAmountOfRetainedEquity());
			tgpf_RemainRight.setCurrentDividedAmout(retainedEquity - theRatio * currentEscrowFund);

			tgpf_RemainRightDao.save(tgpf_RemainRight);

			// 更改三方协议的留存权益
			/**
			 * xsz by time 2019-2-18 17:47:15
			 * 非手工触发也更新对应三方协议数据
			 */
//			if (!srcBusiType.equals("业务触发")) {
//				if (tgxy_TripleAgreement != null) {
//					tgxy_TripleAgreement.setTheAmountOfRetainedEquity(theRatio * currentEscrowFund);
//					if (effectiveLimitedAmount < currentEscrowFund) {
//						tgxy_TripleAgreement.setTheAmountOfInterestRetained(theRatio * effectiveLimitedAmount);
//						tgxy_TripleAgreement// 受限权益（未到期留存权益）
//								.setTheAmountOfInterestUnRetained(theRatio * currentEscrowFund - theRatio * effectiveLimitedAmount);
//						
//					} else {
//						tgxy_TripleAgreement.setTheAmountOfInterestRetained(theRatio * currentEscrowFund);// 受限权益（未到期留存权益）
//						tgxy_TripleAgreement// 受限权益（未到期留存权益）
//								.setTheAmountOfInterestUnRetained(theRatio * currentEscrowFund - theRatio * currentEscrowFund);
//					}
//					tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
//				}
//			}
			if (tgxy_TripleAgreement != null) {
				tgxy_TripleAgreement.setTheAmountOfRetainedEquity(NumberUtil.round(theRatio * currentEscrowFund, 2).doubleValue());
				if (effectiveLimitedAmount < currentEscrowFund) {
					tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(NumberUtil.round(theRatio * effectiveLimitedAmount, 2).doubleValue());
					tgxy_TripleAgreement// 受限权益（未到期留存权益）
							.setTheAmountOfInterestRetained(NumberUtil.round(theRatio * currentEscrowFund - theRatio * effectiveLimitedAmount, 2).doubleValue());
					
				} else {
					tgxy_TripleAgreement.setTheAmountOfInterestUnRetained(NumberUtil.round(theRatio * currentEscrowFund, 2).doubleValue());// 受限权益（未到期留存权益）
					tgxy_TripleAgreement// 受限权益（未到期留存权益）
							.setTheAmountOfInterestRetained(NumberUtil.round(theRatio * currentEscrowFund - theRatio * currentEscrowFund, 2).doubleValue());
				}
				tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
			}

			theSurplusAmount += theRatio * currentEscrowFund;
		}

		properties.put("tgpf_BuildingRemainRightLog", tgpf_BuildingRemainRightLog);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

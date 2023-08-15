package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.transaction.Transactional;

import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.util.MyDatetime;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAppropriated_AFDtlAddService
{
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_buildingAccountDao;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;// 支付保证
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;//受限额度变更
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_FundAppropriated_AFDtlForm model, String buttonType, Sm_User userStart, String applyDate, Boolean isNeedApproval)
	{
		Properties properties = new MyProperties();
		Long buildingId = model.getTableId(); //楼幢id
		Long bankAccountSupervisedId = model.getBankAccountSupervisedId(); // 监管账户Id
		Double appliedAmount = model.getAppliedAmount(); //本次划款申请金额
		String escrowStandard = model.getEscrowStandard(); //托管标准

		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "'该楼幢信息'不存在");
		}
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "'该楼幢信息'不存在");
		}

		//------------------------------------判断该楼幢是否正在用款申请中-------------------------------------------------//
		Tgpf_FundAppropriated_AFDtlForm fundAppropriated_afDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
		fundAppropriated_afDtlForm.setTheState(S_TheState.Normal);
		fundAppropriated_afDtlForm.setBuildingId(buildingId);
		fundAppropriated_afDtlForm.setPayoutState(S_PayoutState.NotAppropriated);
		List<Tgpf_FundAppropriated_AFDtl> tgpf_fundAppropriated_afDtls = tgpf_FundAppropriated_AFDtlDao.findByPage(tgpf_FundAppropriated_AFDtlDao.getQuery(tgpf_FundAppropriated_AFDtlDao.getBasicHQL(), fundAppropriated_afDtlForm));
		for (Tgpf_FundAppropriated_AFDtl tgpf_fundAppropriated_afDtl : tgpf_fundAppropriated_afDtls)
		{
			String eCodeFromConstruction = building.geteCodeFromConstruction();
			return MyBackInfo.fail(properties, "‘"+eCodeFromConstruction+"’正在用款申请中");
		}
		//------------------------------------判断该楼幢是否正在用款申请中-------------------------------------------------//
		
		/**
		 * xsz by time 2019-7-15 11:46:48
		 * 判断是否发起节点变更
		 */
		Empj_BldLimitAmount_AFForm AFModel = new Empj_BldLimitAmount_AFForm();
		AFModel.setTheState(S_TheState.Normal);
		AFModel.setBuilding(building);
		AFModel.setBuildingId(building.getTableId());
		List<Empj_BldLimitAmount_AF> afList = new ArrayList<>();
		afList = empj_BldLimitAmount_AFDao.findByPage(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getListHQL(), AFModel));
		
		if(!afList.isEmpty())
		{
			Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = afList.get(0);
			if(!S_ApprovalState.Completed.equals(empj_BldLimitAmount_AF.getApprovalState()))
			{
				return MyBackInfo.fail(properties, "楼幢："+building.geteCodeFromConstruction()+"已发起节点变更流程，请待流程结束后重新申请！");
			}
		}
		
		//------------------------------------判断该楼幢是否正在支付保证撤销流程中-------------------------------------------------//
		/*
		 * xsz by time 2018-12-13 15:18:31
		 * 查询楼幢是否处于支付保证或支付保证撤销中
		 */
		Empj_PaymentGuaranteeChildForm paymentGuaranteeChildModel = new Empj_PaymentGuaranteeChildForm();
		paymentGuaranteeChildModel.setTheState(S_TheState.Normal);
		paymentGuaranteeChildModel.setEmpj_BuildingInfo(building);

		List<Empj_PaymentGuaranteeChild> paymentGuaranteeChildList;
		paymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao
				.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), paymentGuaranteeChildModel));

		/**
		 * xsz by time 2018-12-13 16:43:46
		 * 和wuyu确认同一时期一个楼幢只会存在一条有效信息
		 * 
		 * 主表busiState 为1 或3 说明该楼幢处于支付保证申请或支付保证撤销中
		 */
		if (null != paymentGuaranteeChildList && paymentGuaranteeChildList.size() > 0)
		{
			
			Empj_PaymentGuaranteeChild paymentGuaranteeChild = paymentGuaranteeChildList.get(0);
			String busiState = paymentGuaranteeChild.getEmpj_PaymentGuarantee().getBusiState();
			if ("1".equals(busiState))
			{
				return MyBackInfo.fail(properties, "该楼幢已发起支付保证申请，请检查后重试！");
			}

			if ("3".equals(busiState))
			{
				return MyBackInfo.fail(properties, "该楼幢已发起支付保证撤销申请，请检查后重试！");
			}
			
		}
		//------------------------------------判断该楼幢是否正在支付保证撤销流程中-------------------------------------------------//


		if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
		{
			return MyBackInfo.fail(properties, "请维护监管账户！");
		}
		Tgpj_BankAccountSupervised bankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);

		if(bankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "请维护监管账户！");
		}
		if(appliedAmount == null || appliedAmount < 0)
		{
			return MyBackInfo.fail(properties, "'本次申请总金额'不能为空");
		}
		String eCodeOfBuilding = building.geteCode(); //楼幢编号
		String supervisedBankAccount = bankAccountSupervised.getTheAccount();

		if(supervisedBankAccount == null || supervisedBankAccount.length() == 0)
		{
			return MyBackInfo.fail(properties, "'监管账号'不能为空");
		}
		//楼幢虚拟账户
		if( building.getBuildingAccount() == null)
		{
			return MyBackInfo.fail(properties, "'请维护楼幢账户'");
		}

		Tgpj_BuildingAccount tgpj_buildingAccount  = building.getBuildingAccount();

		/**
		 * 	操作检查：
		 * 	1、对应的楼幢没有维护监管账号和备案价格，
		 * 	不可以做拨付，限制客户操作，如果没有维护监管账户，
		 * 	则提示信息“请维护监管账户！”，如果没有维护备案价格，
		 * 	则提示信息“请维护物价备案价格！”。
		 */
//		Double recordAvgPriceOfBuilding = tgpj_buildingAccount.getRecordAvgPriceOfBuilding();//楼幢住宅备案均价

		Double allocableAmount = tgpj_buildingAccount.getAllocableAmount(); // 当前可划拨金额

		Double orgLimitedAmount = tgpj_buildingAccount.getOrgLimitedAmount(); //初始受限额度
		if(orgLimitedAmount == null || orgLimitedAmount <= 0)
		{
//			return MyBackInfo.fail(properties, "请维护物价备案价格!");
			return MyBackInfo.fail(properties, "请先维护托管标准或备案价格!");
		}
		String currentFigureProgress = tgpj_buildingAccount.getCurrentFigureProgress();    //当前形象进度
		Double currentLimitedRatio = tgpj_buildingAccount.getCurrentLimitedRatio(); //当前受限比例
		Double currentLimitedAmount = tgpj_buildingAccount.getNodeLimitedAmount(); //当前受限额度
		Double totalAccountAmount = tgpj_buildingAccount.getTotalAccountAmount(); //总入账金额
		Double appliedPayoutAmount = tgpj_buildingAccount.getPayoutAmount(); //已申请拨付金额
		Double currentEscrowFund = tgpj_buildingAccount.getCurrentEscrowFund(); //当前托管余额
		Double refundAmount = tgpj_buildingAccount.getRefundAmount(); //退房退款金额
		
		Double cashLimitedAmount = tgpj_buildingAccount.getCashLimitedAmount();//现金受限额度
        Double effectiveLimitedAmount = tgpj_buildingAccount.getEffectiveLimitedAmount();//有效受限额度

		if(allocableAmount == null || allocableAmount < 0)
		{
			return MyBackInfo.fail(properties, "'当前可划拨金额'不能为空");
		}
		
		if(null == appliedAmount || appliedAmount == 0){
			return MyBackInfo.fail(properties, "请输入有效的本次划款申请金额");
		}
		
		if(allocableAmount < appliedAmount)
		{
			return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前可划拨金额，请确认！");
		}
//		if(escrowStandard == null || escrowStandard.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'托管标准'不能为空");
//		}
//		if(orgLimitedAmount == null || orgLimitedAmount < 1)
//		{
//			return MyBackInfo.fail(properties, "'初始受限额度'不能为空");
//		}
//		if(currentFigureProgress == null || currentFigureProgress.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'当前形象进度'不能为空");
//		}
//		if(currentLimitedRatio == null || currentLimitedRatio < 0)
//		{
//			return MyBackInfo.fail(properties, "'当前受限比例'不能为空");
//		}
//		if(currentLimitedAmount == null || currentLimitedAmount < 0)
//		{
//			return MyBackInfo.fail(properties, "'当前受限额度'不能为空");
//		}
//		if(totalAccountAmount == null || totalAccountAmount < 0)
//		{
//			return MyBackInfo.fail(properties, "'总入账金额'不能为空");
//		}
//		if(appliedPayoutAmount == null || appliedPayoutAmount < 0)
//		{
//			return MyBackInfo.fail(properties, "'已申请拨付金额'不能为空");
//		}
//		if(currentEscrowFund == null || currentEscrowFund < 0)
//		{
//			return MyBackInfo.fail(properties, "'当前托管余额'不能为空");
//		}
//		if(refundAmount == null || refundAmount < 0)
//		{
//			return MyBackInfo.fail(properties, "'退房退款金额'不能为空");
//		}
	
		Tgpf_FundAppropriated_AFDtl tgpf_FundAppropriated_AFDtl = new Tgpf_FundAppropriated_AFDtl();
		tgpf_FundAppropriated_AFDtl.setTheState(S_TheState.Normal);
		tgpf_FundAppropriated_AFDtl.setBuilding(building);
		tgpf_FundAppropriated_AFDtl.seteCodeOfBuilding(eCodeOfBuilding);
		tgpf_FundAppropriated_AFDtl.setBankAccountSupervised(bankAccountSupervised);
		tgpf_FundAppropriated_AFDtl.setSupervisedBankAccount(supervisedBankAccount);
		tgpf_FundAppropriated_AFDtl.setPayoutState(S_PayoutState.NotAppropriated); //拨付状态  1: 未拨付
		tgpf_FundAppropriated_AFDtl.setAppliedAmount(appliedAmount);
		tgpf_FundAppropriated_AFDtl.setAllocableAmount(allocableAmount);
		tgpf_FundAppropriated_AFDtl.setEscrowStandard(escrowStandard);
		tgpf_FundAppropriated_AFDtl.setOrgLimitedAmount(orgLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setCurrentFigureProgress(currentFigureProgress);
		tgpf_FundAppropriated_AFDtl.setCurrentLimitedRatio(currentLimitedRatio);
		tgpf_FundAppropriated_AFDtl.setCurrentLimitedAmount(currentLimitedAmount);
		tgpf_FundAppropriated_AFDtl.setTotalAccountAmount(totalAccountAmount);
		tgpf_FundAppropriated_AFDtl.setAppliedPayoutAmount(appliedPayoutAmount);
		tgpf_FundAppropriated_AFDtl.setCurrentEscrowFund(currentEscrowFund);
		tgpf_FundAppropriated_AFDtl.setRefundAmount(refundAmount);
		tgpf_FundAppropriated_AFDtl.setUserStart(userStart);
		tgpf_FundAppropriated_AFDtl.setCreateTimeStamp(myDatetime.stringToLong(applyDate));
		tgpf_FundAppropriated_AFDtl.setUserUpdate(userStart);
		tgpf_FundAppropriated_AFDtl.setLastUpdateTimeStamp(myDatetime.stringToLong(applyDate));
		
		tgpf_FundAppropriated_AFDtl.setCashLimitedAmount(cashLimitedAmount);
        tgpf_FundAppropriated_AFDtl.setEffectiveLimitedAmount(effectiveLimitedAmount);

		/**
		 * 数据更新：
		 * 	用款申请“提交”后更新金额：
		 * 	1)更新楼幢账户：增加“已申请未拨付金额（元）appliedNoPayoutAmount”、减少“可划拨金额（元）allocableAmount”。
		 *  增加"拨付冻结金额"
		 */
		if(buttonType.equals(S_ButtonType.Submit) || isNeedApproval == false)
		{
			Double appliedNoPayoutAmount = tgpj_buildingAccount.getAppliedNoPayoutAmount();// 已申请未拨付金额
			Double appropriateFrozenAmount = tgpj_buildingAccount.getAppropriateFrozenAmount(); //拨付冻结金额

			if(appliedNoPayoutAmount == null)
			{
				appliedNoPayoutAmount = 0.0;
			}
			if(appropriateFrozenAmount == null)
			{
				appropriateFrozenAmount = 0.0;
			}

			allocableAmount -=appliedAmount; //减少可划拨金额
			appliedNoPayoutAmount += appliedAmount;//增加已申请未拨付金额
			appropriateFrozenAmount += appliedAmount;//增加拨付冻结金额

			tgpj_buildingAccount.setAllocableAmount(allocableAmount);//减少可划拨金额
			tgpj_buildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);// 增加已申请未拨付金额
			tgpj_buildingAccount.setAppropriateFrozenAmount(appropriateFrozenAmount);//增加拨付冻结金额

			tgpj_buildingAccountDao.save(tgpj_buildingAccount);
		}

		properties.put("tgpf_FundAppropriated_AFDtl",tgpf_FundAppropriated_AFDtl);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}

package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountLogForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
public class Tgpf_SpecialFundAppropriated_AFRebuild extends RebuilderBase<Tgpf_SpecialFundAppropriated_AF>
{

	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;

	@Override
	public Properties getSimpleInfo(Tgpf_SpecialFundAppropriated_AF object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		// 用款申请单号
		properties.put("eCode", object.geteCode());
		// 用款申请日期
		properties.put("applyDate", object.getApplyDate());
		// 开发企业名称
		properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());
		// 项目名称
		properties.put("theNameOfProject", object.getTheNameOfProject());
		// 施工编号
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		// 特殊拨付类型
		/*
		 * 1．定向支付
		 * 2．特殊拨付
		 * 3．其他支付
		 */
		properties.put("appropriatedType", object.getAppropriatedType());
		// switch (object.getAppropriatedType())
		// {
		// case "1":
		// properties.put("appropriatedType", "定向支付");
		// break;
		//
		// case "2":
		// properties.put("appropriatedType", "特殊拨付");
		// break;
		// case "3":
		// properties.put("appropriatedType", "其他支付");
		// break;
		//
		// default:
		// properties.put("appropriatedType", "其他支付");
		// break;
		// }

		// 拨付状态
		/*
		 * 1-未拨付(初始）
		 * 2-已拨付（出纳完成系统的资金拨付更新）
		 */
		properties.put("applyState", object.getApplyState());
		// switch (object.getApplyState())
		// {
		// case 1:
		// properties.put("applyState", "未拨付");
		// break;
		//
		// case 2:
		// properties.put("applyState", "已拨付");
		// break;
		//
		// default:
		// properties.put("applyState", "未拨付");
		// break;
		// }
		// 申请单状态
		/*
		 * 1-初始（保存）
		 * 2-已提交（点击提交）
		 * 3-已确认（财务人员初审后）
		 * 4-已统筹（财务人员统筹后）
		 * 5-已退回（财务人员审批不通过）
		 * 6-已审批（财务总监已审批）
		 * 7-已驳回统筹（财务总监审批不通过）
		 */
		properties.put("busiState", object.getBusiState());

		// switch (object.getBusiState())
		// {
		// case "1":
		// properties.put("busiState", "初始");
		// break;
		// case "2":
		// properties.put("busiState", "已提交");
		// break;
		// case "3":
		// properties.put("busiState", "已确认");
		// break;
		// case "4":
		// properties.put("busiState", "已统筹");
		// break;
		// case "5":
		// properties.put("busiState", "已退回");
		// break;
		// case "6":
		// properties.put("busiState", "已审批");
		// break;
		// case "7":
		// properties.put("busiState", "已驳回统筹");
		// break;
		//
		// default:
		// properties.put("busiState", "初始");
		// break;
		// }

		// 审批状态
		properties.put("approvalState", object.getApprovalState());
		//收款方名称
		properties.put("theNameOfBankAccount",object.getTheNameOfBankAccount());
		//金额
		properties.put("totalApplyAmount",object.getTotalApplyAmount());
		//拨付日期
		properties.put("afPayoutDate",object.getAfPayoutDate());
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_SpecialFundAppropriated_AF object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("approvalState", object.getApprovalState());
		properties.put("developCompany", object.getDevelopCompany());
		properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("building", object.getBuilding());
		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
		properties.put("buildingAccountLog", object.getBuildingAccountLog());
		properties.put("buildingAccountLogId", object.getBuildingAccountLog().getTableId());
		properties.put("bankAccount", object.getBankAccount());
		properties.put("bankAccountId", object.getBankAccount().getTableId());
		properties.put("theAccountOfBankAccount", object.getTheAccountOfBankAccount());
		properties.put("theNameOfBankAccount", object.getTheNameOfBankAccount());
		properties.put("theBankOfBankAccount", object.getTheBankOfBankAccount());
		properties.put("appropriatedType", object.getAppropriatedType());
		properties.put("appropriatedRemark", object.getAppropriatedRemark());
		properties.put("totalApplyAmount", object.getTotalApplyAmount());
		properties.put("applyDate", object.getApplyDate());
		properties.put("applyState", object.getApplyState());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_SpecialFundAppropriated_AF> tgpf_SpecialFundAppropriated_AFList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_SpecialFundAppropriated_AFList != null)
		{
			for (Tgpf_SpecialFundAppropriated_AF object : tgpf_SpecialFundAppropriated_AFList)
			{
				Properties properties = new MyProperties();

				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("userUpdate", object.getUserUpdate());
				properties.put("userUpdateId", object.getUserUpdate().getTableId());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("approvalState", object.getApprovalState());
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("buildingAccountLog", object.getBuildingAccountLog());
				properties.put("buildingAccountLogId", object.getBuildingAccountLog().getTableId());
				properties.put("bankAccount", object.getBankAccount());
				properties.put("bankAccountId", object.getBankAccount().getTableId());
				properties.put("theAccountOfBankAccount", object.getTheAccountOfBankAccount());
				properties.put("theNameOfBankAccount", object.getTheNameOfBankAccount());
				properties.put("theBankOfBankAccount", object.getTheBankOfBankAccount());
				properties.put("appropriatedType", object.getAppropriatedType());
				properties.put("appropriatedRemark", object.getAppropriatedRemark());
				properties.put("totalApplyAmount", object.getTotalApplyAmount());
				properties.put("applyDate", object.getApplyDate());
				properties.put("applyState", object.getApplyState());

				list.add(properties);
			}
		}
		return list;
	}

	/**
	 * xsz by time 2018-11-30 15:42:16
	 * 特殊拨付查看详情
	 */
	@SuppressWarnings("unchecked")
	public Properties getDetailForSpecialFund(Tgpf_SpecialFundAppropriated_AF object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 查询详情
		properties.put("theState", object.getTheState());
		// 申请单状态
		properties.put("busiState", object.getBusiState());
		// 审批状态
		properties.put("approvalState", object.getApprovalState());
		// 拨付状态
		properties.put("applyState", object.getApplyState());

		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart().getTheName());
		if (null == object.getUserUpdate())
		{
			properties.put("userUpdate", "");
		}
		else
		{
			properties.put("userUpdate", object.getUserUpdate().getTheName());
		}

		if (null == object.getUserRecord())
		{
			properties.put("userRecord", "");
		}
		else
		{
			properties.put("userRecord", object.getUserRecord().getTheName());
		}

		// 处理时间格式yyyy-MM-dd HH:mm:ss
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		if (null == object.getLastUpdateTimeStamp())
		{
			properties.put("lastUpdateTimeStamp", "");
		}
		else
		{
			properties.put("lastUpdateTimeStamp",
					MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		}

		if (null == object.getRecordTimeStamp())
		{
			properties.put("recordTimeStamp", "");
		}
		else
		{
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		}

		// 开发企业名称
		properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());
		// 项目名称
		properties.put("theNameOfProject", object.getTheNameOfProject());
		
		properties.put("projectId", object.getTableId());

		// properties.put("developCompany", object.getDevelopCompany());
		// properties.put("project", object.getProject());
		// properties.put("building", object.getBuilding());

		// 施工编号
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		// 公安编号
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());

		// 申请日期
		properties.put("applyDate", object.getApplyDate());
		// 申请金额
		properties.put("totalApplyAmount", object.getTotalApplyAmount());
		// 拨付说明
		properties.put("appropriatedRemark", object.getAppropriatedRemark());
		// 拨付类型
		properties.put("appropriatedType", object.getAppropriatedType());
		

		// 楼幢信息
		Empj_BuildingInfo building = object.getBuilding();
		if (null == building)
		{
			// 初始受限额度（元）
			properties.put("orgLimitedAmount", 0);
			// 当前形象进度
			properties.put("currentFigureProgress", 0);
			// 当前受限比例（%）：
			properties.put("currentLimitedRatio", 0);
			// 节点受限额度（元）：
			properties.put("nodeLimitedAmount", 0);
			// 现金受限额度（元）：
			properties.put("cashLimitedAmount", 0);
			// 有效受限额度（元）：
			properties.put("effectiveLimitedAmount", 0);
			// 总入账金额（元）：
			properties.put("totalAccountAmount", 0);
			// 已申请拨付金额（元）：
			properties.put("payoutAmount", 0);
			// 退房退款金额（元）：
			properties.put("appropriateFrozenAmount", 0);
			// 当前托管余额（元）：
			properties.put("currentEscrowFund", 0);
			// 当前可拨付金额（元）：
			properties.put("allocableAmount", 0);

			properties.put("escrowStandard", "");
		}
		else
		{
			
			/**
			 * 根据业务类型和单据Id查询关联的最新的账户log信息
			 */
			Tgpj_BuildingAccountLogForm accountModel = new Tgpj_BuildingAccountLogForm();
			accountModel.setRelatedBusiCode("061206");
			accountModel.setRelatedBusiTableId(object.getTableId());
			List<Tgpj_BuildingAccountLog> accountList = new ArrayList<>();
			accountList = tgpj_BuildingAccountLogDao.findByPage(tgpj_BuildingAccountLogDao.getQuery(tgpj_BuildingAccountLogDao.getBasicHQL(), accountModel));
			if(null != accountList&&accountList.size()>0)
			{
				Tgpj_BuildingAccountLog buildingAccount = accountList.get(0);
				// 初始受限额度（元）
				properties.put("orgLimitedAmount", buildingAccount.getOrgLimitedAmount());
				// 当前形象进度
				properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress());
				// 当前受限比例（%）：
				properties.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio());
				// 节点受限额度（元）：
				properties.put("nodeLimitedAmount", buildingAccount.getNodeLimitedAmount());
				// 现金受限额度（元）：
				properties.put("cashLimitedAmount", buildingAccount.getCashLimitedAmount());
				// 有效受限额度（元）：
				properties.put("effectiveLimitedAmount", buildingAccount.getEffectiveLimitedAmount());
				// 总入账金额（元）：
				properties.put("totalAccountAmount", buildingAccount.getTotalAccountAmount());
				// 已申请拨付金额（元）：
				properties.put("payoutAmount", buildingAccount.getPayoutAmount());
				// 退房退款金额（元）：
				properties.put("appropriateFrozenAmount", buildingAccount.getAppropriateFrozenAmount());
				// 当前托管余额（元）：
				properties.put("currentEscrowFund", buildingAccount.getCurrentEscrowFund());
				// 当前可拨付金额（元）：
				properties.put("allocableAmount", buildingAccount.getAllocableAmount());
			}
			else
			{
				Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();

				// 初始受限额度（元）
				properties.put("orgLimitedAmount", buildingAccount.getOrgLimitedAmount());
				// 当前形象进度
				properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress());
				// 当前受限比例（%）：
				properties.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio());
				// 节点受限额度（元）：
				properties.put("nodeLimitedAmount", buildingAccount.getNodeLimitedAmount());
				// 现金受限额度（元）：
				properties.put("cashLimitedAmount", buildingAccount.getCashLimitedAmount());
				// 有效受限额度（元）：
				properties.put("effectiveLimitedAmount", buildingAccount.getEffectiveLimitedAmount());
				// 总入账金额（元）：
				properties.put("totalAccountAmount", buildingAccount.getTotalAccountAmount());
				// 已申请拨付金额（元）：
				properties.put("payoutAmount", buildingAccount.getPayoutAmount());
				// 退房退款金额（元）：
				properties.put("appropriateFrozenAmount", buildingAccount.getAppropriateFrozenAmount());
				// 当前托管余额（元）：
				properties.put("currentEscrowFund", buildingAccount.getCurrentEscrowFund());
				// 当前可拨付金额（元）：
				properties.put("allocableAmount", buildingAccount.getAllocableAmount());
			}
			
			Tgpj_EscrowStandardVerMng escrowStandardVerMng = building.getEscrowStandardVerMng();

			// ---------------------获取托管标准-------------------------------------------//
			if (null != escrowStandardVerMng)
			{
				if (S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
				{
					properties.put("escrowStandard", escrowStandardVerMng.getAmount() + "元");// 托管标准金额

				}
				if (S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
				{

					properties.put("escrowStandard", "物价备案均价*" + escrowStandardVerMng.getPercentage() + "%");// 托管标准比例
				}
			}
			// ---------------------获取托管标准-------------------------------------------//

		}

		// 监管账号信息
		Tgpj_BankAccountSupervised bankAccount = object.getBankAccount();
		if (null == bankAccount)
		{
			// 监管账号
			properties.put("theAccountOfBankAccount", object.getTheAccountOfBankAccount());
			// 监管账户名称
			properties.put("theNameOfBankAccount", "");
			// 监管银行
			properties.put("theNameOfBank", "");
			// 监管银行开户行
			properties.put("theNameOfBankBranch", object.getTheBankOfBankAccount());
		}
		else
		{ // 监管账号
			properties.put("theAccountOfBankAccount", bankAccount.getTheAccount());
			// 监管账户名称
			properties.put("theNameOfBankAccount", bankAccount.getTheName());
			// 监管银行
			if(null == bankAccount.getBank())
			{
				properties.put("theNameOfBank", "");
			}
			else
			{
				properties.put("theNameOfBank", bankAccount.getBank().getTheName());
			}
			
			// 监管银行开户行
			/*
			 * xsz by time 2018-12-26 14:29:16
			 * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段
			 * ----start----
			 */
//			properties.put("theNameOfBankBranch", bankAccount.getBankBranch().getTheName());
			properties.put("theNameOfBankBranch", bankAccount.getTheNameOfBank());
			/*
			 * xsz by time 2018-12-26 14:29:16
			 * 去除监管账号和开户行的关联关系，直接取theNameOfBank字段
			 * ----end----
			 */
			

		}
		
		properties.put("theNameOfBankAccount", object.getTheNameOfBankAccount());
		
		/**
		 * xsz by time 2018-12-3 14:40:59
		 * 查询申请子表信息
		 */
		Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
		dtlForm.setTheState(S_TheState.Normal);
		dtlForm.setSpecialAppropriated(object);

		List<Tgpf_SpecialFundAppropriated_AFDtl> list = new ArrayList<Tgpf_SpecialFundAppropriated_AFDtl>();
		list = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
				.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));

		if (null == list || list.size() < 1)
		{
			list = new ArrayList<Tgpf_SpecialFundAppropriated_AFDtl>();
		}
		
		for (Tgpf_SpecialFundAppropriated_AFDtl AFDtl : list) {
			
			Tgxy_BankAccountEscrowed bankAccountEscrowed = AFDtl.getBankAccountEscrowed();
			
			/**
			 * TASK#856 特殊拨付后银行简称发生变化
			 */
//			String theName = AFDtl.getBankAccountEscrowed().getBankBranch().getTheName();
			
//			bankAccountEscrowed.setShortNameOfBank(theName);
			
			AFDtl.setBankAccountEscrowed(bankAccountEscrowed);
			
		}

		properties.put("AFDtlList", list);
		
		// properties.put("buildingAccountLog", object.getBuildingAccountLog());
		// properties.put("buildingAccountLogId",
		// object.getBuildingAccountLog().getTableId());

		return properties;
	}
}

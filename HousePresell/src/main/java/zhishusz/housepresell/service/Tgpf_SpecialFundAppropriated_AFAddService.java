package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_SpecialFundApplyState;
import zhishusz.housepresell.database.po.state.S_SpecialFundBusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountLogDao;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;

/*
 * Service添加操作：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundAppropriated_AFAddService
{
	private static String BUSI_CODE = "YKJH";

	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;// 支付保证
	@Autowired
	private CheckMutexService checkMutexService;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * 1.基础字段
		 * applyDate：用款申请日期
		 * developCompanyId：开发企业Id
		 * projectId：项目Id
		 * buildingId：楼幢Id
		 * bankAccountId：监管账户Id
		 * totalApplyAmount：本次划款申请金额
		 * appropriatedType：拨付类型
		 * appropriatedRemark：特殊说明
		 * 
		 * 2.特殊字段
		 * theState：状态
		 * busiState：业务状态
		 * 1-初始（保存）
		 * 2-已提交（点击提交）
		 * 3-已确认（财务人员初审后）
		 * 4-已统筹（财务人员统筹后）
		 * 5-已退回（财务人员审批不通过）
		 * 6-已审批（财务总监已审批）
		 * 7-已驳回统筹（财务总监审批不通过）
		 * 
		 * eCode：编号（调用规则生成）
		 * approvalState：审批状态
		 * 
		 * applyState：拨付状态
		 * 1-未拨付(初始）
		 * 2-已拨付（出纳完成系统的资金拨付更新）
		 * 
		 */

		Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = new Tgpf_SpecialFundAppropriated_AF();

		String eCode = sm_BusinessCodeGetService.execute(BUSI_CODE);

		// 获取当前操作人信息
		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "请先登录");
		}

		tgpf_SpecialFundAppropriated_AF.setUserStart(user);
		tgpf_SpecialFundAppropriated_AF.setUserUpdate(user);

		String applyDate = model.getApplyDate();
		if (null == applyDate || applyDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择签约申请日期");
		}

		tgpf_SpecialFundAppropriated_AF.setApplyDate(applyDate);

		Double totalApplyAmount = model.getTotalApplyAmount();
		if (null == totalApplyAmount || totalApplyAmount <= 0)
		{
			return MyBackInfo.fail(properties, "请输入本次划款申请金额");
		}

		tgpf_SpecialFundAppropriated_AF.setTotalApplyAmount(totalApplyAmount);

		String appropriatedType = model.getAppropriatedType();
		if (null == appropriatedType || appropriatedType.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择拨付类型");
		}

		tgpf_SpecialFundAppropriated_AF.setAppropriatedType(appropriatedType);

		String appropriatedRemark = model.getAppropriatedRemark();
		// if (null == appropriatedRemark ||
		// appropriatedRemark.trim().isEmpty())
		// {
		// return MyBackInfo.fail(properties, "请输入特殊说明");
		// }

		tgpf_SpecialFundAppropriated_AF.setAppropriatedRemark(appropriatedRemark);

		/*
		 * 加载开发企业名称
		 */
		Long developCompanyId = model.getDevelopCompanyId();
		if (null == developCompanyId || developCompanyId < 0)
		{
			return MyBackInfo.fail(properties, "请选择开发企业");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(developCompanyId);
		if (null == developCompany || developCompany.getTheName().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "选择开发企业已失效，请刷新后重试");
		}

		tgpf_SpecialFundAppropriated_AF.setDevelopCompany(developCompany);
		tgpf_SpecialFundAppropriated_AF.setTheNameOfDevelopCompany(developCompany.getTheName().trim());
		
		String theAccountOfBankAccount = model.getTheAccountOfBankAccount();//预售资金监管账号
		String theBankOfBankAccount = model.getTheBankOfBankAccount();//监管账户开户行
		if(null == theAccountOfBankAccount || theAccountOfBankAccount.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入预售资金监管账号");
		}
		if(null == theBankOfBankAccount || theBankOfBankAccount.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入监管账户开户行");
		}
		
		String theNameOfBankAccount = model.getTheNameOfBankAccount();
		if(null == theNameOfBankAccount || theNameOfBankAccount.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入监管账户名称");
		}
			
		/*
		 * 加载项目名称
		 */
		Long projectId = model.getProjectId();
		if (null == projectId || projectId < 0)
		{
			return MyBackInfo.fail(properties, "请选择项目");
		}
		Empj_ProjectInfo project = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(projectId);
		if (null == project || project.getTheName().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "选择项目已失效，请刷新后重试");
		}

		tgpf_SpecialFundAppropriated_AF.setProject(project);
		tgpf_SpecialFundAppropriated_AF.setTheNameOfProject(project.getTheName().trim());

		/*
		 * 加载楼幢相关信息
		 * 1.基本信息
		 * 施工编号
		 * 公安编号
		 * 
		 * 2.楼幢账户信息
		 * 托管标准（元/㎡）
		 * 初始受限额度（元）
		 * 当前形象进度
		 * 当前受限比例（%）
		 * 节点受限额度（元）
		 * 现金受限额度（元）
		 * 有效受限额度（元）
		 * 总入账金额（元）
		 * 已申请拨付金额（元）
		 * 退房退款金额（元）
		 * 当前托管余额（元）
		 * 当前可拨付金额（元）
		 * 
		 * 
		 * *楼幢账户查询后将数据拷贝
		 * *保存楼幢账户log表的数据
		 * 
		 */

		Long buildingId = model.getBuildingId();
		if (null == buildingId || buildingId < 0)
		{
			return MyBackInfo.fail(properties, "请选择楼幢");
		}

		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildingId);
		if (null == empj_BuildingInfo || null == empj_BuildingInfo.geteCodeFromConstruction())
		{
			return MyBackInfo.fail(properties, "选择楼幢信息不全，请维护后重试");
		}
		
		/**
		 * BUG#4080 互斥业务
		 */
		Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
		buildingInfoForm.setTableId(buildingId);
		Properties properties1 = checkMutexService.checkSpecialFundAppropriated(buildingInfoForm);
		if(!S_NormalFlag.success.equals(properties1.get(S_NormalFlag.result)))
		{
			return properties1;
		}
		/**
		 * BUG#4080 互斥业务
		 */

		/*
		 * xsz by time 2018-12-13 15:18:31
		 * 查询楼幢是否处于支付保证或支付保证撤销中
		 * 
		 * xsz by time 2018-12-19 16:13:52
		 * 和zcl确认特殊拨付不需要加互斥
		 * =====================start======================
		 */
//		Empj_PaymentGuaranteeChildForm paymentGuaranteeChildModel = new Empj_PaymentGuaranteeChildForm();
//		paymentGuaranteeChildModel.setTheState(S_TheState.Normal);
//		paymentGuaranteeChildModel.setEmpj_BuildingInfo(empj_BuildingInfo);
//
//		List<Empj_PaymentGuaranteeChild> paymentGuaranteeChildList;
//		paymentGuaranteeChildList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao
//				.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), paymentGuaranteeChildModel));
//
//		/**
//		 * xsz by time 2018-12-13 16:43:46
//		 * 和wuyu确认同一时期一个楼幢只会存在一条有效信息
//		 * 
//		 * 主表busiState 为1 或3 说明该楼幢处于支付保证申请或支付保证撤销中
//		 */
//		if (null != paymentGuaranteeChildList && paymentGuaranteeChildList.size() > 0)
//		{
//			
//			Empj_PaymentGuaranteeChild paymentGuaranteeChild = paymentGuaranteeChildList.get(0);
//			String busiState = paymentGuaranteeChild.getEmpj_PaymentGuarantee().getBusiState();
//			if ("1".equals(busiState))
//			{
//				return MyBackInfo.fail(properties, "该楼幢已发起支付保证申请，请检查后重试！");
//			}
//
//			if ("3".equals(busiState))
//			{
//				return MyBackInfo.fail(properties, "该楼幢已发起支付保证撤销申请，请检查后重试！");
//			}
//			
//		}
		/*
		 * xsz by time 2018-12-13 15:18:31
		 * 查询楼幢是否处于支付保证或支付保证撤销中
		 * 
		 * xsz by time 2018-12-19 16:13:52
		 * 和zcl确认特殊拨付不需要加互斥
		 * =====================end======================
		 */
		/*
		 * xsz by time 2018-11-30 20:18:46
		 * 苏工确认：
		 * 同一幢楼在同一时期只可以发起一次用款申请
		 * 需要校验本地的处于未审批完成的用款申请信息
		 */
		/*Tgpf_SpecialFundAppropriated_AFForm afModel = new Tgpf_SpecialFundAppropriated_AFForm();
		afModel.setTheState(S_TheState.Normal);
		afModel.setBuilding(empj_BuildingInfo);

		List<Tgpf_SpecialFundAppropriated_AF> list;
		list = tgpf_SpecialFundAppropriated_AFDao.findByPage(
				tgpf_SpecialFundAppropriated_AFDao.getQuery(tgpf_SpecialFundAppropriated_AFDao.getBasicHQL(), afModel));
		if (null != list && list.size() > 0)
		{
			for (Tgpf_SpecialFundAppropriated_AF af : list)
			{
				if (!S_ApprovalState.Completed.equals(af.getApprovalState()))
				{
					return MyBackInfo.fail(properties, "该楼幢已发起特殊拨付，请待原流程结束后重试！");
				}
			}
		}*/

		tgpf_SpecialFundAppropriated_AF.setBuilding(empj_BuildingInfo);
		tgpf_SpecialFundAppropriated_AF.seteCodeFromConstruction(empj_BuildingInfo.geteCodeFromConstruction());
		tgpf_SpecialFundAppropriated_AF.seteCodeFromPublicSecurity(empj_BuildingInfo.geteCodeFromPublicSecurity());

		/*
		 * 监管账户信息
		 * 
		 */
		/*Long bankAccountId = model.getBankAccountId();
		if (null == bankAccountId || bankAccountId < 0)
		{
			return MyBackInfo.fail(properties, "请选择监管账户");
		}
		Tgpj_BankAccountSupervised bankAccount = (Tgpj_BankAccountSupervised) tgpj_BankAccountSupervisedDao
				.findById(bankAccountId);

		// 账号信息
		if (null == bankAccount || bankAccount.getTheAccount().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "选择监管账户已失效，请刷新后重试");
		}*/

		// 银行信息
//		if (null == bankAccount.getBankBranch())
//		{
//			return MyBackInfo.fail(properties, "选择监管账户银行信息不全，请重新选择");
//		}

//		tgpf_SpecialFundAppropriated_AF.setBankAccount(bankAccount);
		tgpf_SpecialFundAppropriated_AF.setTheAccountOfBankAccount(theAccountOfBankAccount);
		// 监管账户名称
//		tgpf_SpecialFundAppropriated_AF.setTheNameOfBankAccount(bankAccount.getTheName().trim());
		// 监管账户开户行
		/*
		 * xsz by time 2018-12-27 08:36:38
		 * 监管账户开户行直接取theNameOfBank字段
		 * ------start----------
		 */
//		tgpf_SpecialFundAppropriated_AF.setTheBankOfBankAccount(bankAccount.getBankBranch().getTheName());
		tgpf_SpecialFundAppropriated_AF.setTheBankOfBankAccount(theBankOfBankAccount);
		/*
		 * xsz by time 2018-12-27 08:36:38
		 * 监管账户开户行直接取theNameOfBank字段
		 * ------start----------
		 */
		
		tgpf_SpecialFundAppropriated_AF.setTheNameOfBankAccount(theNameOfBankAccount);

		/*
		 * 特殊字段
		 */
		// 状态，默认正常态
		tgpf_SpecialFundAppropriated_AF.setTheState(S_TheState.Normal);
		/*
		 * S_SpecialFundBusiState
		 * busiState：业务状态
		 * 1-初始（保存）
		 * 2-已提交（点击提交）
		 * 3-已确认（财务人员初审后）
		 * 4-已统筹（财务人员统筹后）
		 * 5-已退回（财务人员审批不通过）
		 * 6-已审批（财务总监已审批）
		 * 7-已驳回统筹（财务总监审批不通过）
		 */
		tgpf_SpecialFundAppropriated_AF.setBusiState(S_SpecialFundBusiState.Saved);

		/*
		 * eCode：编号（调用规则生成）
		 * approvalState：审批状态
		 * initial = 1; //初始
		 * applyState：拨付状态
		 * S_SpecialFundApplyState
		 * 1-未拨付(初始）
		 * 2-已拨付（出纳完成系统的资金拨付更新）
		 * 
		 */
		tgpf_SpecialFundAppropriated_AF.setApprovalState(S_ApprovalState.WaitSubmit);
		tgpf_SpecialFundAppropriated_AF.setApplyState(S_SpecialFundApplyState.Notappropriated);
		tgpf_SpecialFundAppropriated_AF.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_SpecialFundAppropriated_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_SpecialFundAppropriated_AF.seteCode(eCode);

		// 获取楼幢账户信息
		Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();

		/*
		 * xsz by time 2018-11-30 09:06:17
		 * 检查本次划款申请总金额<=当前可拨付金额，如有超出，则提示“本次划款申请金额不得大于当前可拨付金额，请确认！”
		 * 
		 * xsz by time 2018-12-6 11:27:13
		 * zcl提出需求：
		 * 发起特殊拨付申请时，需要检验“本次划款申请金额”<=“当前托管余额”；
		 */
		// if (totalApplyAmount > buildingAccount.getAllocableAmount())
		// {
		// return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前可拨付金额，请确认！");
		// }
		if (totalApplyAmount > buildingAccount.getCurrentEscrowFund())
		{
			return MyBackInfo.fail(properties, "本次划款申请金额不得大于当前托管余额，请确认！");
		}

		Serializable tableId = tgpf_SpecialFundAppropriated_AFDao.save(tgpf_SpecialFundAppropriated_AF);
		
		/*
		 * xsz by time 2018-9-18 15:19:02
		 * 后台附件信息整合
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			smAttachmentList = model.getSmAttachmentList().toString();

			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

				sm_Attachment.setUserStart(user);
				sm_Attachment.setUserUpdate(user);
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setSourceId(tableId.toString());// 关联Id
				// sm_Attachment.setBusiType("Tgxy_TripleAgreement");// 业务类型
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		/*
		 * 附件信息后台整合
		 * 
		 * xsz by time 2018-12-3 08:47:50
		 * 特殊拨付无附件上传模块
		 */
		// String smAttachmentList = null;
		// if (null != model.getSmAttachmentList() &&
		// !model.getSmAttachmentList().trim().isEmpty())
		// {
		// smAttachmentList = model.getSmAttachmentList().toString();
		//
		// List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList,
		// Sm_Attachment.class);
		//
		// for (Sm_Attachment sm_Attachment : gasList)
		// {
		// // 查询附件配置表
		// Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		// form.seteCode(sm_Attachment.getSourceType());
		// Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
		// .findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(),
		// form));
		//
		// sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
		//
		// sm_Attachment.setSourceId(tableId.toString());// 关联Id
		// sm_Attachment.setBusiType(BUSI_CODE);// 业务类型
		// sm_Attachment.setTheState(S_TheState.Normal);
		// sm_Attachment.setUserStart(user);// 创建人
		// sm_Attachment.setUserUpdate(user);// 操作人
		// sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
		// sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
		// sm_AttachmentDao.save(sm_Attachment);
		//
		// }
		// }

		properties.put("tableId", new Long(tableId.toString()));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

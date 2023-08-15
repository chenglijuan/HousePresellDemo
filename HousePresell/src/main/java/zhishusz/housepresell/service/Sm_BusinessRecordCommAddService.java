package zhishusz.housepresell.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmountForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_BusinessRecordForm;
import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_DTLDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_ManageDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_BusinessRecordDao;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundOverallPlanDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentBond;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_Manage;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BusinessRecord;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 
 * @author ZS_XSZ
 *         业务关联记录公共方法
 *
 */
@Service
@Transactional
public class Sm_BusinessRecordCommAddService
{
	private static final String BUSI_CODE = "YWGL00";

	@Autowired
	private Sm_BusinessRecordDao sm_BusinessRecordDao;// 业务关联
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;// 开发企业
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢信息
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;// 合作协议
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;// 退房退款 refundType 0-已结清 1-未结清
	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;// 支付保证
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;// 备案价格
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;// 托管终止
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;// 用款申请
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;// 三方协议计量结算
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;// 工程进度巡查
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;// 申请单
	@Autowired
	private Tgpf_FundOverallPlanDao tgpf_FundOverallPlanDao;// 统筹与复核
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;// 受限额度变更
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;// 托管标准
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;// 受限额度
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;// 三方协议版本管理
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;// 合作协议版本管理
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;// 存单存入提取
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;// 特殊拨付
	
	@Autowired
	private Empj_BldLimitAmountDao empj_BldLimitAmountDao;//进度节点更新

	/**
	 * xsz by time 根据传入的业务及业务编号存储相关信息
	 * 
	 * @param busiCode
	 *            业务编码（区分业务）
	 * @param codeOfBusiness
	 *            业务编号（具体某个单据）
	 * @param afTableId
	 *            申请单tableId（审批流申请单tableId）
	 * @param model
	 * @return properties
	 */
	public Properties addBusinessRecord(String busiCode, String codeOfBusiness, Long afTableId, BaseForm form)
	{

		Properties properties = new MyProperties();

		try
		{
			if (null == busiCode || busiCode.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "业务编码不能为空");
			}

			if (null == codeOfBusiness || codeOfBusiness.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "业务编码不能为空");
			}

			// 和申请单关联
			if (null == afTableId || afTableId <= 0)
			{
				return MyBackInfo.fail(properties, "申请单ID不能为空");
			}

			// 查询审批申请单
			Sm_ApprovalProcess_AF approvalProcess_AF = sm_ApprovalProcess_AFDao.findById(afTableId);
			if (null == approvalProcess_AF)
			{
				return MyBackInfo.fail(properties, "未查询到申请单信息");
			}

			/*
			 * 先判断业务单号是否在业务关联表中存在
			 * 
			 */
			Sm_BusinessRecordForm bmodel = new Sm_BusinessRecordForm();
			bmodel.setTheState(S_TheState.Normal);
			bmodel.setBusiCode(busiCode);
			bmodel.setCodeOfBusiness(codeOfBusiness);
			bmodel.setApprovalProcess_AF(approvalProcess_AF);
			Integer count = sm_BusinessRecordDao
					.findByPage_Size(sm_BusinessRecordDao.getQuery_Size(sm_BusinessRecordDao.getBasicHQL(), bmodel));

			if (count > 0)
			{
				return MyBackInfo.fail(properties, "业务单号已经存在！");
			}

			// 业务开始（对具体的业务开始处理）
			switch (busiCode)
			{
			    
			case "06120501":
			    //支付保函
			    properties = getPaymentBond(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);
			        break;

			case "020101":

				// 开发企业注册
				properties = getCompanyRegiste(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "020102":

				// 开发企业变更
				properties = getCompanyChange(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "020103":

				// 代理公司注册
				properties = getCompanyAgencyRegiste(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "020105":

				// 代理公司变更
				properties = getCompanyAgencyChange(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "020106":

				// 进度见证服务单位注册
				properties = getCompanyWitnessRegiste(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "020107":

				// 进度见证服务单位变更
				properties = getCompanyWitnessChange(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "020108":

				// 合作机构注册
				properties = getCompanyCooperationRegiste(busiCode, codeOfBusiness, approvalProcess_AF, form,
						properties);

				break;

			case "020109":

				// 合作机构变更
				properties = getCompanyCooperationChange(busiCode, codeOfBusiness, approvalProcess_AF, form,
						properties);

				break;

			case "03010101":

				// 项目信息注册与备案
				properties = getProjectInfoRegiste(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "03010102":

				// 项目信息变更与备案
				properties = getProjectInfoChange(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "03010201":

				// 楼幢信息初始维护（含楼幢、单元、户室信息维护）
				properties = getBuildingInfoRegiste(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "03010202":

				// 楼幢信息变更维护
				properties = getBuildingInfoChange(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "03010301":

				// 备案价格初始维护
				// Tgpj_BuildingAvgPrice
				properties = getBuildingAvgPrice(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "03010302":

				// 备案价格变更维护
				// Tgpj_BuildingAvgPrice
				properties = getBuildingAvgPriceChange(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "03030101":

				// 受限额度变更
				// Empj_BldLimitAmount_AF
				properties = getBldLimitAmount(busiCode, codeOfBusiness, form, properties, approvalProcess_AF);

				break;

			case "03030102":

				// 托管终止（项目）
				// Empj_BldEscrowCompleted
				properties = getBldEscrowCompleted(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06010101":

				// 托管标准版本管理
				// Tgpj_EscrowStandardVerMng
				properties = getEscrowStandardVerMng(busiCode, codeOfBusiness, form, properties, approvalProcess_AF);

				break;

			case "06010102":

				// 受限额度节点版本管理
				// Tgpj_BldLimitAmountVer_AF
				properties = getBldLimitAmountVer(busiCode, codeOfBusiness, form, properties, approvalProcess_AF);

				break;

			case "06010103":

				// 三方协议版本管理 Tgxy_TripleAgreementVerMng
				Tgxy_TripleAgreementVerMngForm tripleAgreementVerMngModel = new Tgxy_TripleAgreementVerMngForm();
				tripleAgreementVerMngModel.seteCode(codeOfBusiness);
				Tgxy_TripleAgreementVerMng tripleAgreementVerMng = tgxy_TripleAgreementVerMngDao
						.findOneByQuery_T(tgxy_TripleAgreementVerMngDao
								.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tripleAgreementVerMngModel));
				if (null == tripleAgreementVerMng)
				{
					properties.put(S_NormalFlag.info, "未查询到相关单据信息");
				}
				else
				{
					// 创建保存对象
					Sm_BusinessRecord businessRecord;

					/*
					 * 获取对象属性
					 * 0：单据对象
					 * 1：开发企业
					 * 2：项目
					 * 3：区域
					 * 4：楼幢
					 * 5：三方协议
					 */
					Object[] obj = {
							tripleAgreementVerMng, null, null, null, null, null
					};

					// 调用公共设置方法
					businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

					if (null == businessRecord)
					{
						return MyBackInfo.fail(properties, "保存失败");
					}
					else
					{
						businessRecord.setBusiCode(busiCode);
						businessRecord.setCodeOfBusiness(codeOfBusiness);
					}

					// 保存对象
					sm_BusinessRecordDao.save(businessRecord);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);

				break;

			case "06110201":

				// 贷款托管合作协议
				properties = getEscrowAgreement(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06110202":

				// 合作协议版本管理 Tgxy_CoopAgreementVerMng
				Tgxy_CoopAgreementVerMngForm coopAgreementVerMngModel = new Tgxy_CoopAgreementVerMngForm();
				coopAgreementVerMngModel.seteCode(codeOfBusiness);
				Tgxy_CoopAgreementVerMng coopAgreementVerMng = tgxy_CoopAgreementVerMngDao
						.findOneByQuery_T(tgxy_CoopAgreementVerMngDao
								.getQuery(tgxy_CoopAgreementVerMngDao.getBasicHQL(), coopAgreementVerMngModel));

				if (null == coopAgreementVerMng)
				{
					properties.put(S_NormalFlag.info, "未查询到相关单据信息");
				}
				else
				{
					// 创建保存对象
					Sm_BusinessRecord businessRecord;

					/*
					 * 获取对象属性
					 * 0：单据对象
					 * 1：开发企业
					 * 2：项目
					 * 3：区域
					 * 4：楼幢
					 * 5：三方协议
					 */
					Object[] obj = {
							coopAgreementVerMng, null, null, null, null, null
					};

					// 调用公共设置方法
					businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

					if (null == businessRecord)
					{
						return MyBackInfo.fail(properties, "保存失败");
					}
					else
					{
						businessRecord.setBusiCode(busiCode);
						businessRecord.setCodeOfBusiness(codeOfBusiness);
					}

					// 保存对象
					sm_BusinessRecordDao.save(businessRecord);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);

				break;

			case "06110301":

				// 贷款托管三方协议
				properties = getTripleAgreement(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06110304":

				// 三方协议计量结算
				// Tgxy_CoopAgreementSettle
				properties = getCoopAgreementSettle(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06120201":

				// 退房退款-贷款已结清
				properties = getRefundInfoBy0(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06120202":

				// 退房退款-贷款未结清
				properties = getRefundInfoBy1(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06120301":

				// 用款申请与复核
				// Tgpf_FundAppropriated_AF
				properties = getFundAppropriated(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06120302":

				// 统筹与复核
				// tgpf_FundOverallPlanDao
				properties = getFundOverallPlan(busiCode, codeOfBusiness, form, properties, approvalProcess_AF);

				break;

			case "06120303":

				// 资金拨付
				// Tgpf_FundAppropriated_AF
				// 同用款申请与复核
				properties = getFundAppropriated(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06120401":

				// 支付保证申请与复核

				properties = getPaymentGuarantee0(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "06120403":

				// 支付保证撤销
				properties = getPaymentGuarantee1(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "210104":

				// 存单存入管理
				Tg_DepositManagementForm depositManagementModel = new Tg_DepositManagementForm();
				depositManagementModel.seteCode(codeOfBusiness);
				Tg_DepositManagement depositManagement = tg_DepositManagementDao
						.findOneByQuery_T(tg_DepositManagementDao.getQuery(tg_DepositManagementDao.getBasicHQL(),
								depositManagementModel));
				if (null == depositManagement)
				{
					properties.put(S_NormalFlag.info, "未查询到相关单据信息");
				}
				else
				{
					// 创建保存对象
					Sm_BusinessRecord businessRecord;

					/*
					 * 获取对象属性
					 * 0：单据对象
					 * 1：开发企业
					 * 2：项目
					 * 3：区域
					 * 4：楼幢
					 * 5：三方协议
					 */
					Object[] obj = {
							depositManagement, null, null, null, null, null
					};

					// 调用公共设置方法
					businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

					if (null == businessRecord)
					{
						return MyBackInfo.fail(properties, "保存失败");
					}
					else
					{
						businessRecord.setBusiCode(busiCode);
						businessRecord.setCodeOfBusiness(codeOfBusiness);
					}

					// 保存对象
					sm_BusinessRecordDao.save(businessRecord);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);

				break;

			case "210105":

				// 存单提取管理
				depositManagementModel = new Tg_DepositManagementForm();
				depositManagementModel.seteCode(codeOfBusiness);
				depositManagement = tg_DepositManagementDao.findOneByQuery_T(tg_DepositManagementDao
						.getQuery(tg_DepositManagementDao.getBasicHQL(), depositManagementModel));
				if (null == depositManagement)
				{
					properties.put(S_NormalFlag.info, "未查询到相关单据信息");
				}
				else
				{
					// 创建保存对象
					Sm_BusinessRecord businessRecord;

					/*
					 * 获取对象属性
					 * 0：单据对象
					 * 1：开发企业
					 * 2：项目
					 * 3：区域
					 * 4：楼幢
					 * 5：三方协议
					 */
					Object[] obj = {
							depositManagement, null, null, null, null, null
					};

					// 调用公共设置方法
					businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

					if (null == businessRecord)
					{
						return MyBackInfo.fail(properties, "保存失败");
					}
					else
					{
						businessRecord.setBusiCode(busiCode);
						businessRecord.setCodeOfBusiness(codeOfBusiness);
					}

					// 保存对象
					sm_BusinessRecordDao.save(businessRecord);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);

				break;

			case "030302":

				// 工程进度巡查管理
				// Empj_PjDevProgressForcast

				properties = getPjDevProgressForcast(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);

				break;

			case "061206":

				// 特殊资金拨付
				Tgpf_SpecialFundAppropriated_AFForm specialFundAppropriated_AFForm = new Tgpf_SpecialFundAppropriated_AFForm();
				specialFundAppropriated_AFForm.seteCode(codeOfBusiness);
				Tgpf_SpecialFundAppropriated_AF specialFundAppropriated_AF = tgpf_SpecialFundAppropriated_AFDao
						.findOneByQuery_T(tgpf_SpecialFundAppropriated_AFDao.getQuery(
								tgpf_SpecialFundAppropriated_AFDao.getBasicHQL(), specialFundAppropriated_AFForm));

				if (null == specialFundAppropriated_AF)
				{
					properties.put(S_NormalFlag.info, "未查询到相关单据信息");
				}
				else
				{
					
					Emmp_CompanyInfo company;
					Empj_ProjectInfo project;
					Sm_CityRegionInfo cityRegion;
					Empj_BuildingInfo building;

					building = specialFundAppropriated_AF.getBuilding();
					project = specialFundAppropriated_AF.getProject();
					cityRegion = project.getCityRegion();
					company = specialFundAppropriated_AF.getDevelopCompany();
					//
					// 创建保存对象
					Sm_BusinessRecord businessRecord;

					/*
					 * 获取对象属性
					 * 0：单据对象
					 * 1：开发企业
					 * 2：项目
					 * 3：区域
					 * 4：楼幢
					 * 5：三方协议
					 */
					Object[] obj = {
							specialFundAppropriated_AF, company, project, cityRegion, building, null
					};

					// 调用公共设置方法
					businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

					if (null == businessRecord)
					{
						return MyBackInfo.fail(properties, "保存失败");
					}
					else
					{
						businessRecord.setBusiCode(busiCode);
						businessRecord.setCodeOfBusiness(codeOfBusiness);
					}

					// 保存对象
					sm_BusinessRecordDao.save(businessRecord);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
					
				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);

				break;
			
			case "03030100":

				// 进度节点更新
				Empj_BldLimitAmountForm empj_BldLimitAmountForm = new Empj_BldLimitAmountForm();
				empj_BldLimitAmountForm.setTheState(S_TheState.Normal);
				empj_BldLimitAmountForm.seteCode(codeOfBusiness);
				Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao.findOneByQuery_T(empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicHQL(), empj_BldLimitAmountForm));

				if (null == bldLimitAmount)
				{
					properties.put(S_NormalFlag.info, "未查询到相关单据信息");
				}
				else
				{
					
					Emmp_CompanyInfo company;
					Empj_ProjectInfo project;
					Sm_CityRegionInfo cityRegion;
					Empj_BuildingInfo building;

					building = null;
					project = bldLimitAmount.getProject();
					cityRegion = project.getCityRegion();
					company = bldLimitAmount.getDevelopCompany();
					//
					// 创建保存对象
					Sm_BusinessRecord businessRecord;

					/*
					 * 获取对象属性
					 * 0：单据对象
					 * 1：开发企业
					 * 2：项目
					 * 3：区域
					 * 4：楼幢
					 * 5：三方协议
					 */
					Object[] obj = {
							bldLimitAmount, company, project, cityRegion, null, null
					};

					// 调用公共设置方法
					businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

					if (null == businessRecord)
					{
						return MyBackInfo.fail(properties, "保存失败");
					}
					else
					{
						businessRecord.setBusiCode(busiCode);
						businessRecord.setCodeOfBusiness(codeOfBusiness);
					}

					// 保存对象
					sm_BusinessRecordDao.save(businessRecord);
					properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
					
				}

				properties.put(S_NormalFlag.result, S_NormalFlag.success);

				break;
				
			case "03030202":

                // 工程进度巡查管理
                properties = getProjProgForcast(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);
                
                break;
                
			case "03030206":

                // 工程进度巡查推送管理
                properties = getPushProjProgForcast(busiCode, codeOfBusiness, approvalProcess_AF, form, properties);
                
                break;

			default:

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "未查找到对应的审批类型");

				break;

			}
		}
		catch (Exception e)
		{
			// 异常返回信息
			properties.put(S_NormalFlag.debugInfo, e.getMessage());
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);

		}

		return properties;

	}

	private Properties getBldLimitAmountVer(String busiCode, String codeOfBusiness, BaseForm form,
			Properties properties, Sm_ApprovalProcess_AF approvalProcess_AF)
	{
		// 查询对应的业务
		Tgpj_BldLimitAmountVer_AFForm model = new Tgpj_BldLimitAmountVer_AFForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgpj_BldLimitAmountVer_AF tgpj_BldLimitAmountVer_AF;
		tgpj_BldLimitAmountVer_AF = tgpj_BldLimitAmountVer_AFDao.findOneByQuery_T(
				tgpj_BldLimitAmountVer_AFDao.getQuery(tgpj_BldLimitAmountVer_AFDao.getBasicHQL(), model));
		if (null == tgpj_BldLimitAmountVer_AF)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 此业务没有关联的信息
		 */

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgpj_BldLimitAmountVer_AF, null, null, null, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	private Properties getEscrowStandardVerMng(String busiCode, String codeOfBusiness, BaseForm form,
			Properties properties, Sm_ApprovalProcess_AF approvalProcess_AF)
	{
		// 查询对应的业务
		Tgpj_EscrowStandardVerMngForm model = new Tgpj_EscrowStandardVerMngForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgpj_EscrowStandardVerMng tgpj_EscrowStandardVerMng;
		tgpj_EscrowStandardVerMng = tgpj_EscrowStandardVerMngDao.findOneByQuery_T(
				tgpj_EscrowStandardVerMngDao.getQuery(tgpj_EscrowStandardVerMngDao.getBasicHQL(), model));
		if (null == tgpj_EscrowStandardVerMng)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 此业务没有关联的信息
		 */

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgpj_EscrowStandardVerMng, null, null, null, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	private Properties getBldLimitAmount(String busiCode, String codeOfBusiness, BaseForm form, Properties properties,
			Sm_ApprovalProcess_AF approvalProcess_AF)
	{
		// 查询对应的业务
		Empj_BldLimitAmount_AFForm model = new Empj_BldLimitAmount_AFForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_BldLimitAmount_AF empj_BldLimitAmount_AF;
		empj_BldLimitAmount_AF = empj_BldLimitAmount_AFDao
				.findOneByQuery_T(empj_BldLimitAmount_AFDao.getQuery(empj_BldLimitAmount_AFDao.getBasicHQL(), model));
		if (null == empj_BldLimitAmount_AF)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * xsz by time 2018-11-29 19:27:39
		 * 受限额度变更
		 * 
		 */
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;

		building = empj_BldLimitAmount_AF.getBuilding();
		project = empj_BldLimitAmount_AF.getProject();
		cityRegion = project.getCityRegion();
		company = empj_BldLimitAmount_AF.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_BldLimitAmount_AF, company, project, cityRegion, building, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 统筹
	private Properties getFundOverallPlan(String busiCode, String codeOfBusiness, BaseForm form, Properties properties,
			Sm_ApprovalProcess_AF approvalProcess_AF)
	{
		// 查询对应的业务
		Tgpf_FundOverallPlanForm model = new Tgpf_FundOverallPlanForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgpf_FundOverallPlan tgpf_FundOverallPlan;
		tgpf_FundOverallPlan = tgpf_FundOverallPlanDao
				.findOneByQuery_T(tgpf_FundOverallPlanDao.getQuery(tgpf_FundOverallPlanDao.getBasicHQL(), model));
		if (null == tgpf_FundOverallPlan)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * xsz by time 2018-11-26 16:41:44
		 * 统筹与复核保存关联申请单信息：
		 * 
		 */
		List<Tgpf_FundAppropriated_AF> fundAppropriated_AFList;
		fundAppropriated_AFList = tgpf_FundOverallPlan.getFundAppropriated_AFList();
		if (null == fundAppropriated_AFList || fundAppropriated_AFList.size() <= 0)
		{
			// 未查询到用款申请子表信息
			return MyBackInfo.fail(properties, "未查询到关联的明细表信息，请刷新后重试！");
		}

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		// 针对子表信息进行业务关联表的存储
		for (Tgpf_FundAppropriated_AF AF : fundAppropriated_AFList)
		{
			company = AF.getDevelopCompany();
			project = AF.getProject();
			cityRegion = project.getCityRegion();

			// 创建保存对象
			Sm_BusinessRecord businessRecord;

			/*
			 * 获取对象属性
			 * 0：单据对象
			 * 1：开发企业
			 * 2：项目
			 * 3：区域
			 * 4：楼幢
			 * 5：三方协议
			 */
			Object[] obj = {
					AF, company, project, cityRegion, null, null
			};

			// 调用公共设置方法
			businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

			if (null == businessRecord)
			{
				return MyBackInfo.fail(properties, "保存失败");
			}
			else
			{
				businessRecord.setBusiCode(busiCode);
				businessRecord.setCodeOfBusiness(codeOfBusiness);
			}

			// 保存对象
			sm_BusinessRecordDao.save(businessRecord);

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 工程进度巡查管理
	private Properties getPjDevProgressForcast(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Empj_PjDevProgressForcastForm model = new Empj_PjDevProgressForcastForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_PjDevProgressForcast empj_PjDevProgressForcast;
		empj_PjDevProgressForcast = empj_PjDevProgressForcastDao.findOneByQuery_T(
				empj_PjDevProgressForcastDao.getQuery(empj_PjDevProgressForcastDao.getBasictHQL(), model));
		if (null == empj_PjDevProgressForcast)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 工程进度巡查管理关联信息：
		 * 关联开发企业-开发企业编号(代理公司)
		 * 关联项目
		 * 关联楼幢
		 */

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;
		// Tgxy_TripleAgreement tripleAgreement;

		building = empj_PjDevProgressForcast.getBuilding();
		if (null == building)
		{
			return MyBackInfo.fail(properties, "未查询楼幢信息");
		}

		company = empj_PjDevProgressForcast.getDevelopCompany();
		if (null == company)
		{
			company = building.getDevelopCompany();
		}

		project = empj_PjDevProgressForcast.getProject();
		if (null == project)
		{
			project = building.getProject();
		}

		cityRegion = empj_PjDevProgressForcast.getCityRegion();
		if (null == cityRegion)
		{
			cityRegion = building.getCityRegion();
		}

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_PjDevProgressForcast, company, project, cityRegion, building, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 三方协议计量结算
	private Properties getCoopAgreementSettle(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Tgxy_CoopAgreementSettleForm model = new Tgxy_CoopAgreementSettleForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle;
		tgxy_CoopAgreementSettle = tgxy_CoopAgreementSettleDao.findOneByQuery_T(
				tgxy_CoopAgreementSettleDao.getQuery(tgxy_CoopAgreementSettleDao.getBasicHQL(), model));
		if (null == tgxy_CoopAgreementSettle)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 三方协议计量结算关联信息：
		 * 关联开发企业-开发企业编号(代理公司)
		 * 
		 */

		Emmp_CompanyInfo company;
		// Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		// Empj_BuildingInfo building;
		// Tgxy_TripleAgreement tripleAgreement;

		company = tgxy_CoopAgreementSettle.getAgentCompany();
		if (null == company)
		{
			return MyBackInfo.fail(properties, "未查询到代理公司信息");
		}

		cityRegion = company.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgxy_CoopAgreementSettle, company, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 用款申请与复核
	private Properties getFundAppropriated(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Tgpf_FundAppropriated_AFForm model = new Tgpf_FundAppropriated_AFForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF;
		tgpf_FundAppropriated_AF = tgpf_FundAppropriated_AFDao.findOneByQuery_T(
				tgpf_FundAppropriated_AFDao.getQuery(tgpf_FundAppropriated_AFDao.getBasicHQL(), model));
		if (null == tgpf_FundAppropriated_AF)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * xsz by time 2018-11-23 14:48:54
		 * 用款申请业务需要有主子表表关联：
		 * 此业务需要将子表信息（精确至楼幢）保存至此业务关联表中
		 */
		List<Tgpf_FundAppropriated_AFDtl> appropriated_AFDtlList;
		appropriated_AFDtlList = tgpf_FundAppropriated_AF.getFundAppropriated_AFDtlList();
		if (null == appropriated_AFDtlList || appropriated_AFDtlList.size() <= 0)
		{
			// 未查询到用款申请子表信息
			return MyBackInfo.fail(properties, "未查询到关联的用款申请子表信息，请刷新后重试！");
		}

		Empj_BuildingInfo building;
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		// 针对子表信息进行业务关联表的存储
		for (Tgpf_FundAppropriated_AFDtl AFDtl : appropriated_AFDtlList)
		{
			/*
			 * 用款申请主表关联信息：
			 * 关联项目-项目编号
			 * 关联区域-区域编号-区域名称
			 * 关联开发企业-开发企业编号
			 * 关联楼幢-楼幢编号
			 */
			building = AFDtl.getBuilding();
			project = building.getProject();
			if (null != project)
			{
				cityRegion = project.getCityRegion();
				company = project.getDevelopCompany();
			}
			else
			{
				cityRegion = null;
				company = null;
			}

			// 创建保存对象
			Sm_BusinessRecord businessRecord;

			/*
			 * 获取对象属性
			 * 0：单据对象
			 * 1：开发企业
			 * 2：项目
			 * 3：区域
			 * 4：楼幢
			 * 5：三方协议
			 */
			Object[] obj = {
					AFDtl, company, project, cityRegion, null, null
			};

			// 调用公共设置方法
			businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

			if (null == businessRecord)
			{
				return MyBackInfo.fail(properties, "保存失败");
			}
			else
			{
				businessRecord.setBusiCode(busiCode);
				businessRecord.setCodeOfBusiness(codeOfBusiness);
			}

			// 保存对象
			sm_BusinessRecordDao.save(businessRecord);

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 托管终止（项目）
	private Properties getBldEscrowCompleted(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Empj_BldEscrowCompletedForm model = new Empj_BldEscrowCompletedForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_BldEscrowCompleted empj_BldEscrowCompleted;
		empj_BldEscrowCompleted = empj_BldEscrowCompletedDao
				.findOneByQuery_T(empj_BldEscrowCompletedDao.getQuery(empj_BldEscrowCompletedDao.getBasicHQL(), model));
		if (null == empj_BldEscrowCompleted)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 托管终止（项目）明细表关联信息：
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联开发企业-开发企业编号
		 * 关联楼幢
		 */
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;
		// Tgxy_TripleAgreement tripleAgreement;
		// 托管终止存储明细表信息
		List<Empj_BldEscrowCompleted_Dtl> completed_DtlList = empj_BldEscrowCompleted
				.getEmpj_BldEscrowCompleted_DtlList();
		if (null != completed_DtlList && completed_DtlList.size() > 0)
		{

			for (Empj_BldEscrowCompleted_Dtl dtl : completed_DtlList)
			{
				project = dtl.getProject();
				if (null == project)
				{
					project = empj_BldEscrowCompleted.getProject();
				}

				company = dtl.getDevelopCompany();

				if (null == company)
				{
					company = empj_BldEscrowCompleted.getDevelopCompany();
				}

				cityRegion = project.getCityRegion();

				building = dtl.getBuilding();

				// 创建保存对象
				Sm_BusinessRecord businessRecord;

				/*
				 * 获取对象属性
				 * 0：单据对象
				 * 1：开发企业
				 * 2：项目
				 * 3：区域
				 * 4：楼幢
				 * 5：三方协议
				 */
				Object[] obj = {
						dtl, company, project, cityRegion, building, null
				};

				// 调用公共设置方法
				businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

				if (null == businessRecord)
				{
					return MyBackInfo.fail(properties, "保存失败");
				}
				else
				{
					businessRecord.setBusiCode(busiCode);
					businessRecord.setCodeOfBusiness(codeOfBusiness);
				}

				// 保存对象
				sm_BusinessRecordDao.save(businessRecord);
			}

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 备案价格初始维护
	private Properties getBuildingAvgPrice(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Tgpj_BuildingAvgPriceForm model = new Tgpj_BuildingAvgPriceForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice;
		tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao
				.findOneByQuery_T(tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getBasicHQL(), model));
		if (null == tgpj_BuildingAvgPrice)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 备案价格关联信息：
		 * 关联楼幢-楼幢编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联开发企业-开发企业编号
		 * 
		 */

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;
		// Tgxy_TripleAgreement tripleAgreement;

		building = tgpj_BuildingAvgPrice.getBuildingInfo();
		if (null == building)
		{
			return MyBackInfo.fail(properties, "未查询到关联的楼幢信息");
		}

		project = building.getProject();

		cityRegion = building.getCityRegion();

		company = building.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgpj_BuildingAvgPrice, company, project, cityRegion, building, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 备案价格变更维护
	private Properties getBuildingAvgPriceChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Tgpj_BuildingAvgPriceForm model = new Tgpj_BuildingAvgPriceForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice;
		tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao
				.findOneByQuery_T(tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getBasicHQL(), model));
		if (null == tgpj_BuildingAvgPrice)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 备案价格关联信息：
		 * 关联楼幢-楼幢编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联开发企业-开发企业编号
		 * 
		 */

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;
		// Tgxy_TripleAgreement tripleAgreement;

		building = tgpj_BuildingAvgPrice.getBuildingInfo();
		if (null == building)
		{
			return MyBackInfo.fail(properties, "未查询到关联的楼幢信息");
		}

		project = building.getProject();

		cityRegion = building.getCityRegion();

		company = building.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgpj_BuildingAvgPrice, company, project, cityRegion, building, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 代理公司变更
	private Properties getCompanyAgencyChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Agency);// 代理公司
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 代理公司注册
	private Properties getCompanyAgencyRegiste(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Agency);// 代理公司
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 合作机构变更
	private Properties getCompanyCooperationChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Cooperation);// 合作机构
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 合作机构注册
	private Properties getCompanyCooperationRegiste(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Cooperation);// 合作机构
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 进度见证服务单位变更
	private Properties getCompanyWitnessChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Witness);// 进度见证服务单位
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 进度见证服务单位注册
	private Properties getCompanyWitnessRegiste(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Witness);// 进度见证服务单位
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 楼幢信息变更维护
	private Properties getBuildingInfoChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{

		Empj_BuildingInfoForm model = new Empj_BuildingInfoForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_BuildingInfo empj_BuildingInfo;
		empj_BuildingInfo = empj_BuildingInfoDao
				.findOneByQuery_T(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), model));
		if (null == empj_BuildingInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 楼幢关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;

		project = empj_BuildingInfo.getProject();

		cityRegion = empj_BuildingInfo.getCityRegion();

		company = empj_BuildingInfo.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_BuildingInfo, company, project, cityRegion, empj_BuildingInfo, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 楼幢信息初始维护（含楼幢、单元、户室信息维护）
	private Properties getBuildingInfoRegiste(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Empj_BuildingInfoForm model = new Empj_BuildingInfoForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_BuildingInfo empj_BuildingInfo;
		empj_BuildingInfo = empj_BuildingInfoDao
				.findOneByQuery_T(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), model));
		if (null == empj_BuildingInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 楼幢关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;

		project = empj_BuildingInfo.getProject();

		cityRegion = empj_BuildingInfo.getCityRegion();

		company = empj_BuildingInfo.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_BuildingInfo, company, project, cityRegion, empj_BuildingInfo, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 项目信息变更与备案
	private Properties getProjectInfoChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Empj_ProjectInfoForm model = new Empj_ProjectInfoForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_ProjectInfo empj_ProjectInfo;
		empj_ProjectInfo = empj_ProjectInfoDao
				.findOneByQuery_T(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), model));
		if (null == empj_ProjectInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 支付保证关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Emmp_CompanyInfo company;
		Sm_CityRegionInfo cityRegion;

		company = empj_ProjectInfo.getDevelopCompany();

		cityRegion = empj_ProjectInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_ProjectInfo, company, empj_ProjectInfo, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 项目信息注册与备案
	private Properties getProjectInfoRegiste(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Empj_ProjectInfoForm model = new Empj_ProjectInfoForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Empj_ProjectInfo empj_ProjectInfo;
		empj_ProjectInfo = empj_ProjectInfoDao
				.findOneByQuery_T(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getBasicHQL(), model));
		if (null == empj_ProjectInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 支付保证关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Emmp_CompanyInfo company;
		Sm_CityRegionInfo cityRegion;

		company = empj_ProjectInfo.getDevelopCompany();

		cityRegion = empj_ProjectInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_ProjectInfo, company, empj_ProjectInfo, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 开发企业变更
	private Properties getCompanyChange(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Development);// 开发企业
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 开发企业注册
	private Properties getCompanyRegiste(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Emmp_CompanyInfoForm model = new Emmp_CompanyInfoForm();
		model.setTheState(S_TheState.Normal);
		model.setTheType(S_CompanyType.Development);// 开发企业
		model.seteCode(codeOfBusiness);

		Emmp_CompanyInfo emmp_CompanyInfo;
		emmp_CompanyInfo = emmp_CompanyInfoDao
				.findOneByQuery_T(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 开发企业关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Sm_CityRegionInfo cityRegion;

		cityRegion = emmp_CompanyInfo.getCityRegion();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				emmp_CompanyInfo, emmp_CompanyInfo, null, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 支付保证撤销
	private Properties getPaymentGuarantee1(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Empj_PaymentGuaranteeForm model = new Empj_PaymentGuaranteeForm();
		model.setTheState(S_TheState.Normal);
		model.setCancel("0");
		model.seteCode(codeOfBusiness);

		Empj_PaymentGuarantee empj_PaymentGuarantee;
		empj_PaymentGuarantee = empj_PaymentGuaranteeDao
				.findOneByQuery_T(empj_PaymentGuaranteeDao.getQuery(empj_PaymentGuaranteeDao.getBasicHQL(), model));
		if (null == empj_PaymentGuarantee)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 支付保证关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;

		project = empj_PaymentGuarantee.getProject();

		cityRegion = project.getCityRegion();

		company = empj_PaymentGuarantee.getCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_PaymentGuarantee, company, project, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 支付保证申请与复核
	private Properties getPaymentGuarantee0(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		Empj_PaymentGuaranteeForm model = new Empj_PaymentGuaranteeForm();
		model.setTheState(S_TheState.Normal);
		model.setApply("0");
		model.seteCode(codeOfBusiness);

		Empj_PaymentGuarantee empj_PaymentGuarantee;
		empj_PaymentGuarantee = empj_PaymentGuaranteeDao
				.findOneByQuery_T(empj_PaymentGuaranteeDao.getQuery(empj_PaymentGuaranteeDao.getBasicHQL(), model));
		if (null == empj_PaymentGuarantee)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 支付保证关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 
		 */
		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;

		project = empj_PaymentGuarantee.getProject();

		cityRegion = project.getCityRegion();

		company = empj_PaymentGuarantee.getCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				empj_PaymentGuarantee, company, project, cityRegion, null, null
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 退房退款申请-贷款未结清
	private Properties getRefundInfoBy1(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 退房退款申请-贷款未结清
		Tgpf_RefundInfoForm model = new Tgpf_RefundInfoForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);
		model.setRefundType("1");// 0-已结清 1-未结清

		Tgpf_RefundInfo tgpf_RefundInfo;
		tgpf_RefundInfo = tgpf_RefundInfoDao
				.findOneByQuery_T(tgpf_RefundInfoDao.getQuery(tgpf_RefundInfoDao.getBasicHQL(), model));
		if (null == tgpf_RefundInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 退房退款关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联楼幢-楼幢编号
		 * 关联三方协议-协议编号
		 * 
		 */

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;
		Tgxy_TripleAgreement tripleAgreement;

		tripleAgreement = tgpf_RefundInfo.getTripleAgreement();
		if (null == tripleAgreement)
		{
			return MyBackInfo.fail(properties, "未查询到相关协议信息");
		}

		building = tripleAgreement.getBuildingInfo();
		if (null == building)
		{
			// 未获取楼幢信息
			return MyBackInfo.fail(properties, "未获取到相关楼幢信息");
		}

		cityRegion = building.getCityRegion();

		project = building.getProject();

		company = building.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgpf_RefundInfo, company, project, cityRegion, building, tripleAgreement
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	private Properties getRefundInfoBy0(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 退房退款申请-贷款已结清
		Tgpf_RefundInfoForm model = new Tgpf_RefundInfoForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);
		model.setRefundType("0");// 0-已结清 1-未结清

		Tgpf_RefundInfo tgpf_RefundInfo;
		tgpf_RefundInfo = tgpf_RefundInfoDao
				.findOneByQuery_T(tgpf_RefundInfoDao.getQuery(tgpf_RefundInfoDao.getBasicHQL(), model));
		if (null == tgpf_RefundInfo)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 退房退款关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联楼幢-楼幢编号
		 * 关联三方协议-协议编号
		 * 
		 */

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;
		Tgxy_TripleAgreement tripleAgreement;

		tripleAgreement = tgpf_RefundInfo.getTripleAgreement();
		if (null == tripleAgreement)
		{
			return MyBackInfo.fail(properties, "未查询到相关协议信息");
		}

		building = tripleAgreement.getBuildingInfo();
		if (null == building)
		{
			// 未获取楼幢信息
			return MyBackInfo.fail(properties, "未获取到相关楼幢信息");
		}

		cityRegion = building.getCityRegion();

		project = building.getProject();

		company = building.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgpf_RefundInfo, company, project, cityRegion, building, tripleAgreement
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

	// 贷款三方协议
	private Properties getTripleAgreement(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Tgxy_TripleAgreementForm model = new Tgxy_TripleAgreementForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgxy_TripleAgreement tgxy_TripleAgreement;
		tgxy_TripleAgreement = tgxy_TripleAgreementDao
				.findOneByQuery_T(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), model));
		if (null == tgxy_TripleAgreement)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 三方协议关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联楼幢-楼幢编号
		 * 
		 */

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		Empj_BuildingInfo building;

		building = tgxy_TripleAgreement.getBuildingInfo();
		if (null == building)
		{
			// 未获取楼幢信息
			return MyBackInfo.fail(properties, "未获取到相关楼幢信息");
		}

		cityRegion = building.getCityRegion();

		project = building.getProject();

		company = building.getDevelopCompany();

		// 创建保存对象
		Sm_BusinessRecord businessRecord;

		/*
		 * 获取对象属性
		 * 0：单据对象
		 * 1：开发企业
		 * 2：项目
		 * 3：区域
		 * 4：楼幢
		 * 5：三方协议
		 */
		Object[] obj = {
				tgxy_TripleAgreement, company, project, cityRegion, building, tgxy_TripleAgreement
		};

		// 调用公共设置方法
		businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

		if (null == businessRecord)
		{
			return MyBackInfo.fail(properties, "保存失败");
		}
		else
		{
			businessRecord.setBusiCode(busiCode);
			businessRecord.setCodeOfBusiness(codeOfBusiness);
		}

		// 保存对象
		sm_BusinessRecordDao.save(businessRecord);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	// 贷款合作协议
	private Properties getEscrowAgreement(String busiCode, String codeOfBusiness,
			Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
	{
		// 查询对应的业务
		Tgxy_EscrowAgreementForm model = new Tgxy_EscrowAgreementForm();
		model.setTheState(S_TheState.Normal);
		model.seteCode(codeOfBusiness);

		Tgxy_EscrowAgreement tgxy_EscrowAgreement;
		tgxy_EscrowAgreement = tgxy_EscrowAgreementDao
				.findOneByQuery_T(tgxy_EscrowAgreementDao.getQuery(tgxy_EscrowAgreementDao.getBasicHQL(), model));
		if (null == tgxy_EscrowAgreement)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
		}

		/*
		 * 合作协议关联信息：
		 * 关联开发企业-开发企业编号
		 * 关联项目-项目编号
		 * 关联区域-区域编号-区域名称
		 * 关联楼幢-楼幢编号
		 * 关联三方协议-三方协议编号
		 * 
		 * 注意点：
		 * 1.合作协议可能关联多个楼幢信息，此时应该以楼幢为单位增加记录
		 * 2.一条合作协议可能被多个三方协议所关联，所以理论上应该在三方协议中取维护合作协议
		 * 
		 */
		List<Empj_BuildingInfo> buildingInfoList;
		buildingInfoList = tgxy_EscrowAgreement.getBuildingInfoList();
		if (null == buildingInfoList || buildingInfoList.size() == 0)
		{
			// 未获取楼幢信息
			return MyBackInfo.fail(properties, "未获取到相关楼幢信息");
		}

		Emmp_CompanyInfo company;
		Empj_ProjectInfo project;
		Sm_CityRegionInfo cityRegion;
		// Empj_BuildingInfo building;
		// Tgxy_TripleAgreement tripleAgreement;

		// 创建保存对象
		Sm_BusinessRecord businessRecord;
		for (Empj_BuildingInfo buildingInfo : buildingInfoList)
		{
			// 开发企业
			company = tgxy_EscrowAgreement.getDevelopCompany();
			if (null == company)
			{
				buildingInfo.getDevelopCompany();
			}
			// 项目
			project = tgxy_EscrowAgreement.getProject();
			if (null == project)
			{
				project = buildingInfo.getProject();
			}
			// 区域
			cityRegion = tgxy_EscrowAgreement.getCityRegion();
			if (null == cityRegion)
			{
				cityRegion = buildingInfo.getCityRegion();
			}

			/*
			 * 获取对象属性
			 * 0：单据对象
			 * 1：开发企业
			 * 2：项目
			 * 3：区域
			 * 4：楼幢
			 * 5：三方协议
			 */
			Object[] obj = {
					tgxy_EscrowAgreement, company, project, cityRegion, buildingInfo, null
			};

			// 调用公共设置方法
			businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

			if (null == businessRecord)
			{
				return MyBackInfo.fail(properties, "保存失败");
			}
			else
			{
				businessRecord.setBusiCode(busiCode);
				businessRecord.setCodeOfBusiness(codeOfBusiness);
			}

			// 保存对象
			sm_BusinessRecordDao.save(businessRecord);
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		}
		return properties;
	}
	
	@Autowired
    private Empj_PaymentBondDao empj_PaymentBondDao;
    @Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;
    
	// 支付保函
    @SuppressWarnings("unchecked")
    private Properties getPaymentBond(String busiCode, String codeOfBusiness,
            Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
    {
        // 查询对应的业务
        Empj_PaymentBondForm model = new Empj_PaymentBondForm();
        model.setTheState(S_TheState.Normal);
        model.seteCode(codeOfBusiness);
        
        Empj_PaymentBond empj_PaymentBond;
        empj_PaymentBond = empj_PaymentBondDao.findOneByQuery_T(empj_PaymentBondDao.getQuery(empj_PaymentBondDao.getBasicHQL(), model));
        if (null == empj_PaymentBond)
        {
            return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
        }
        
        // 查询子表信息
        Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
        childModel.setTheState(S_TheState.Normal);
        childModel.setEmpj_PaymentBond(empj_PaymentBond);
        List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
            .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));

        if (null == childList || childList.size() == 0)
        {
            return MyBackInfo.fail(properties, "未获取到相关子表信息");
        }

        Empj_BuildingInfo buildingInfo;

        // 创建保存对象
        Sm_BusinessRecord businessRecord;
        for (Empj_PaymentBondChild child : childList)
        {
            
            buildingInfo = child.getEmpj_BuildingInfo();
            /*
             * 获取对象属性
             * 0：单据对象
             * 1：开发企业
             * 2：项目
             * 3：区域
             * 4：楼幢
             * 5：三方协议
             */
            Object[] obj = {
                empj_PaymentBond, buildingInfo.getDevelopCompany(), buildingInfo.getProject(), buildingInfo.getCityRegion(), buildingInfo, null
            };

            // 调用公共设置方法
            businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

            if (null == businessRecord)
            {
                return MyBackInfo.fail(properties, "保存失败");
            }
            else
            {
                businessRecord.setBusiCode(busiCode);
                businessRecord.setCodeOfBusiness(codeOfBusiness);
            }

            // 保存对象
            sm_BusinessRecordDao.save(businessRecord);
            properties.put(S_NormalFlag.result, S_NormalFlag.success);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        }
        return properties;
    }
    
    @Autowired
    private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
    @Autowired
    private Empj_ProjProgForcast_DTLDao empj_ProjProgForcast_DTLDao;
    
    // 工程进度巡查管理
    private Properties getProjProgForcast(String busiCode, String codeOfBusiness,
            Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
    {
        // 查询对应的业务
        Empj_ProjProgForcast_AFForm model = new Empj_ProjProgForcast_AFForm();
        model.setTheState(S_TheState.Normal);
        model.seteCode(codeOfBusiness);

        Empj_ProjProgForcast_AF empj_ProjProgForcast_AF;
        empj_ProjProgForcast_AF = empj_ProjProgForcast_AFDao.findOneByQuery_T(
            empj_ProjProgForcast_AFDao.getQuery(empj_ProjProgForcast_AFDao.getBasicHQL(), model));
        if (null == empj_ProjProgForcast_AF)
        {
            return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
        }

        /*
         * 工程进度巡查管理关联信息：
         * 关联开发企业-开发企业编号(代理公司)
         * 关联项目
         * 关联楼幢
         */

        Emmp_CompanyInfo company;
        Empj_ProjectInfo project;
        Sm_CityRegionInfo cityRegion;
        Empj_BuildingInfo building;
        
        project = empj_ProjProgForcast_AF.getProject();

        company = empj_ProjProgForcast_AF.getCompany();
        if (null == company)
        {
            company = project.getDevelopCompany();
        }

        cityRegion = empj_ProjProgForcast_AF.getArea();
        if (null == cityRegion)
        {
            cityRegion = project.getCityRegion();
        }

        // 创建保存对象
        Sm_BusinessRecord businessRecord;

        /*
         * 获取对象属性
         * 0：单据对象
         * 1：开发企业
         * 2：项目
         * 3：区域
         * 4：楼幢
         * 5：三方协议
         */
        Object[] obj = {
            empj_ProjProgForcast_AF, company, project, cityRegion, null, null
        };

        // 调用公共设置方法
        businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

        if (null == businessRecord)
        {
            return MyBackInfo.fail(properties, "保存失败");
        }
        else
        {
            businessRecord.setBusiCode(busiCode);
            businessRecord.setCodeOfBusiness(codeOfBusiness);
        }

        // 保存对象
        sm_BusinessRecordDao.save(businessRecord);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
    
    
    @Autowired
	private Empj_ProjProgForcast_ManageDao manageDao;
    
    // 工程进度巡查推送管理
    private Properties getPushProjProgForcast(String busiCode, String codeOfBusiness,
            Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Properties properties)
    {
    	
    	Long sourceId = approvalProcess_AF.getSourceId();
    	Empj_ProjProgForcast_Manage manage = manageDao.findById(sourceId);
        if (null == manage)
        {
            return MyBackInfo.fail(properties, S_NormalFlag.info_NoFind);
        }

        /*
         * 工程进度巡查管理关联信息：
         * 关联开发企业-开发企业编号(代理公司)
         * 关联项目
         * 关联楼幢
         */

        Emmp_CompanyInfo company;
        Empj_ProjectInfo project;
        Sm_CityRegionInfo cityRegion;
        Empj_BuildingInfo building;
        
        project = manage.getProject();
        company = project.getDevelopCompany();

        cityRegion = manage.getArea();
        if (null == cityRegion)
        {
            cityRegion = project.getCityRegion();
        }

        // 创建保存对象
        Sm_BusinessRecord businessRecord;

        /*
         * 获取对象属性
         * 0：单据对象
         * 1：开发企业
         * 2：项目
         * 3：区域
         * 4：楼幢
         * 5：三方协议
         */
        Object[] obj = {
        		manage, company, project, cityRegion, null, null
        };

        // 调用公共设置方法
        businessRecord = this.setPubBean(approvalProcess_AF, form, obj);

        if (null == businessRecord)
        {
            return MyBackInfo.fail(properties, "保存失败");
        }
        else
        {
            businessRecord.setBusiCode(busiCode);
            businessRecord.setCodeOfBusiness(codeOfBusiness);
        }

        // 保存对象
        sm_BusinessRecordDao.save(businessRecord);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
    

	/**
	 * 设置共参数
	 * 
	 * @param form
	 *            获取当前登录人信息
	 * @param clazz
	 *            一些需要储存的对象信息
	 *            obj：单据对象
	 *            Emmp_CompanyInfo 开发企业
	 *            Empj_ProjectInfo 项目
	 *            Sm_CityRegionInfo 区域
	 *            Empj_BuildingInfo 楼幢
	 *            Tgxy_TripleAgreement 三方协议
	 * 
	 * @return
	 */
	private Sm_BusinessRecord setPubBean(Sm_ApprovalProcess_AF approvalProcess_AF, BaseForm form, Object[] obj)
	{

		/*
		 * 存储公共字段：
		 * theState 状态 S_TheState 初始为Normal
		 * busiState 业务状态
		 * ecode 编号
		 * userStart 创建人
		 * createTimeStamp 创建时间
		 * userUpdate 修改人
		 * lastUpdateTimeStamp 最后修改日期
		 * 
		 * userBegin 单据创建人
		 * dateBegin 单据创建时间
		 * 
		 */
		Sm_BusinessRecord businessRecord = new Sm_BusinessRecord();
		try
		{
			businessRecord.setTheState(S_TheState.Normal);
			businessRecord.setBusiState(S_BusiState.HaveRecord);
			businessRecord.setEcode(sm_BusinessCodeGetService.execute(BUSI_CODE));
			businessRecord.setUserStart(form.getUser());
			businessRecord.setUserUpdate(form.getUser());
			businessRecord.setCreateTimeStamp(System.currentTimeMillis());
			businessRecord.setLastUpdateTimeStamp(System.currentTimeMillis());
			// 申请单信息
			businessRecord.setApprovalProcess_AF(approvalProcess_AF);

			/*
			 * 获取对象属性
			 * 0：单据对象
			 * 1：开发企业
			 * 2：项目
			 * 3：区域
			 * 4：楼幢
			 * 5：三方协议
			 * 
			 */

			// 获取开发企业信息
			Emmp_CompanyInfo company;
			if (null == obj[1])
			{
				company = null;
				businessRecord.setCompanyInfo(null);
				businessRecord.setCodeOfCompany(null);

			}
			else
			{
				company = (Emmp_CompanyInfo) obj[1];
				businessRecord.setCompanyInfo(company);
				businessRecord.setCodeOfCompany(company.geteCode());

			}

			// 获取项目信息
			Empj_ProjectInfo project;
			if (null == obj[2])
			{
				project = null;
				businessRecord.setProjectInfo(null);
				businessRecord.setCodeOfProject(null);

			}
			else
			{
				project = (Empj_ProjectInfo) obj[2];
				businessRecord.setProjectInfo(project);
				businessRecord.setCodeOfProject(project.geteCode());

			}

			// 获取区域信息
			Sm_CityRegionInfo cityRegionInfo;
			if (null == obj[3])
			{
				cityRegionInfo = null;
				businessRecord.setCityRegion(null);
				businessRecord.setTheNameOfCityRegion(null);
				businessRecord.setCodeOfCityRegion(null);

			}
			else
			{
				cityRegionInfo = (Sm_CityRegionInfo) obj[3];
				businessRecord.setCityRegion(cityRegionInfo);
				businessRecord.setTheNameOfCityRegion(cityRegionInfo.getTheName());
				businessRecord.setCodeOfCityRegion(cityRegionInfo.geteCode());

			}

			// 获取楼幢信息
			Empj_BuildingInfo buildingInfo;
			if (null == obj[4])
			{
				buildingInfo = null;
				businessRecord.setBuildingInfo(null);
				businessRecord.setCodeOfBuilding(null);

			}
			else
			{
				buildingInfo = (Empj_BuildingInfo) obj[4];
				businessRecord.setBuildingInfo(buildingInfo);
				businessRecord.setCodeOfBuilding(buildingInfo.geteCode());

			}

			// 获取三方协议信息
			Tgxy_TripleAgreement tripleAgreement;
			if (null == obj[5])
			{
				tripleAgreement = null;
				businessRecord.setTripleAgreement(null);
				businessRecord.setCodeOfTripleAgreement(null);

			}
			else
			{
				tripleAgreement = (Tgxy_TripleAgreement) obj[5];
				businessRecord.setTripleAgreement(tripleAgreement);
				businessRecord.setCodeOfTripleAgreement(tripleAgreement.geteCode());

			}

			/*
			 * 通过反射获取单据公共属性:
			 * 单据创建人 userStart
			 * 单据创建时间 createTimeStamp
			 */
			Sm_User userBegin;
			Long dateBegin;
			try
			{
				Field field;
				// 单据创建人
				field = obj[0].getClass().getDeclaredField("userStart");
				if (null != field)
				{
					// 设置对象的访问权限，保证对private的属性的访问
					field.setAccessible(true);
					userBegin = (Sm_User) field.get(obj[0]);
				}
				else
				{
					userBegin = null;
				}

				// 单据创建时间
				field = obj[0].getClass().getDeclaredField("createTimeStamp");
				if (null != field)
				{
					// 设置对象的访问权限，保证对private的属性的访问
					field.setAccessible(true);
					dateBegin = (Long) field.get(obj[0]);
				}
				else
				{
					dateBegin = null;
				}

			}
			catch (Exception e)
			{

				userBegin = null;
				dateBegin = null;

			}

			businessRecord.setUserBegin(userBegin);
			businessRecord.setDateBegin(dateBegin);
		}
		catch (Exception e)
		{
			return null;
		}

		return businessRecord;

	}

}

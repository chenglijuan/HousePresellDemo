package zhishusz.housepresell.external.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_FundOverallPlan;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.PaymentLaunchDtlModel;
import zhishusz.housepresell.external.po.PaymentLaunchModel;
import zhishusz.housepresell.service.Sm_ApprovalProcess_EndService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.SocketUtil;

/**
 * 批量拨付申请
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
@Slf4j
public class BatchBankTransfersService {

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 基础参数
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;// 特殊拨付主表
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;// 特殊拨付子表
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;// 退房退款
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;// 一般拨付
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_fundAppropriatedDao;
	@Autowired
	private Sm_ApprovalProcess_EndService endService;// 审批流手动通过处理

	@SuppressWarnings("unchecked")
	public Properties execute(String busiCode, Long tableId, Long workFlowId, BaseForm baseForm) {

		Properties properties = new Properties();

		Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
		paraModel.setParametertype("80");
		paraModel.setTheValue("800001");
		List<Sm_BaseParameter> list = new ArrayList<>();
		list = sm_BaseParameterDao
				.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
		if (list.isEmpty()) {
			return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
		}
		String url = list.get(0).getTheName();
		paraModel = new Sm_BaseParameterForm();
		paraModel.setParametertype("80");
		paraModel.setTheValue("800005");
		list = new ArrayList<>();
		list = sm_BaseParameterDao
				.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
		if (list.isEmpty()) {
			return MyBackInfo.fail(properties, "未查询到操作密钥，请查询基础参数是否正确！");
		}
		String secretKey = list.get(0).getTheName();

		paraModel = new Sm_BaseParameterForm();
		paraModel.setParametertype("80");
		paraModel.setTheValue("800006");
		list = new ArrayList<>();
		list = sm_BaseParameterDao
				.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
		if (list.isEmpty()) {
			return MyBackInfo.fail(properties, "未查询到企业唯一标识，请查询基础参数是否正确！");
		}
		String orgCode = list.get(0).getTheName();

		PaymentLaunchModel afModel;
		String jsonString;
		List<PaymentLaunchDtlModel> transferData;
		PaymentLaunchDtlModel dtlModel;
		Tgxy_BankAccountEscrowed bankAccountEscrowed;
		String httpStringPostRequest;
		Tgpf_RefundInfo refundInfo;

		Tgpf_SocketMsg tgpf_SocketMsg;

		switch (busiCode) {
		case "061206":
			// 特殊拨付
			Tgpf_SpecialFundAppropriated_AF af = tgpf_SpecialFundAppropriated_AFDao.findById(tableId);
			if (null == af) {
				return MyBackInfo.fail(properties, "未查询到相关信息！");
			}
			String accType = null == af.getAccType() ? "1" : af.getAccType();// 默认对公

			// 查询划款子表信息
			Tgpf_SpecialFundAppropriated_AFDtlForm dtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
			dtlForm.setTheState(S_TheState.Normal);
			dtlForm.setSpecialAppropriated(af);
			List<Tgpf_SpecialFundAppropriated_AFDtl> listDtl;
			listDtl = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
					.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), dtlForm));
			if (null == listDtl || listDtl.size() < 1) {
				return MyBackInfo.fail(properties, "未维护划款信息！");
			}

			/*
			 * 构建发送参数，进行银企直联接口请求访问
			 */
			afModel = new PaymentLaunchModel();
			// 设置主表属性
			putAf(afModel, orgCode, secretKey);
			// 账户交易明细信息
			transferData = new ArrayList<>();
			for (Tgpf_SpecialFundAppropriated_AFDtl dtl : listDtl) {
				bankAccountEscrowed = dtl.getBankAccountEscrowed();// 托管账号
				// 设置子表属性
				dtlModel = new PaymentLaunchDtlModel();
				putDtl(dtlModel, af, accType, bankAccountEscrowed, dtl);

				transferData.add(dtlModel);
			}
			afModel.setTransferData(transferData);
			// 传递参数
			jsonString = JSONObject.toJSONString(afModel);

			break;

		case "06120201":
			// 退房退款（已结清）
			refundInfo = tgpf_RefundInfoDao.findById(tableId);
			if (null == refundInfo) {
				return MyBackInfo.fail(properties, "未查询到有效的信息！");
			}

			/*
			 * 构建发送参数，进行银企直联接口请求访问
			 */
			afModel = new PaymentLaunchModel();
			// 设置主表属性
			putAf(afModel, orgCode, secretKey);
			// 账户交易明细信息
			transferData = new ArrayList<>();
			// 托管账户
			bankAccountEscrowed = refundInfo.getTheBankAccountEscrowed();
			// 设置明细表属性
			dtlModel = new PaymentLaunchDtlModel();
			putRefundModel(dtlModel, bankAccountEscrowed, refundInfo, "06120201");
			transferData.add(dtlModel);

			afModel.setTransferData(transferData);
			// 传递参数
			jsonString = JSONObject.toJSONString(afModel);

			break;

		case "06120202":
			// 退房退款（未结清）
			refundInfo = tgpf_RefundInfoDao.findById(tableId);
			if (null == refundInfo) {
				return MyBackInfo.fail(properties, "未查询到有效的信息！");
			}

			/*
			 * 构建发送参数，进行银企直联接口请求访问
			 */
			afModel = new PaymentLaunchModel();
			// 设置主表属性
			putAf(afModel, orgCode, secretKey);
			// 账户交易明细信息
			transferData = new ArrayList<>();
			// 托管账户
			bankAccountEscrowed = refundInfo.getTheBankAccountEscrowed();
			// 设置明细表属性
			dtlModel = new PaymentLaunchDtlModel();
			putRefundModel(dtlModel, bankAccountEscrowed, refundInfo, "06120202");
			transferData.add(dtlModel);

			afModel.setTransferData(transferData);
			// 传递参数
			jsonString = JSONObject.toJSONString(afModel);

			break;

		case "06120302":
			// （一般拨付）统筹
			Tgpf_FundAppropriated_AF fundAppropriated_AF = tgpf_FundAppropriated_AFDao.findById(tableId);
			if (null == fundAppropriated_AF) {
				return MyBackInfo.fail(properties, "未查询到有效的信息！");
			}

			Tgpf_FundOverallPlan tgpf_fundOverallPlan = fundAppropriated_AF.getFundOverallPlan();
			String projectName =  fundAppropriated_AF.getProject().getTheName();
			/*
			 * 构建发送参数，进行银企直联接口请求访问
			 */
			afModel = new PaymentLaunchModel();
			// 设置主表属性
			putAf(afModel, orgCode, secretKey);
			// 账户交易明细信息
			transferData = new ArrayList<>();
			// 获取资金拨付信息
			List<Tgpf_FundAppropriated> fundAppropriatedList = fundAppropriated_AF.getFundAppropriatedList();
			Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
			fundAppropriatedForm.setTheState(S_TheState.Normal);
			fundAppropriatedForm.setFundAppropriated_AFId(fundAppropriated_AF.getTableId());
			fundAppropriatedForm.setFundOverallPlanId(tgpf_fundOverallPlan.getTableId());
			fundAppropriatedForm.setOverallPlanPayoutAmount(0D);
			fundAppropriatedList = tgpf_fundAppropriatedDao.findByPage(
					tgpf_fundAppropriatedDao.getQuery(tgpf_fundAppropriatedDao.getBasicHQL(), fundAppropriatedForm));
			for (Tgpf_FundAppropriated tgpf_FundAppropriated : fundAppropriatedList) {

				bankAccountEscrowed = tgpf_FundAppropriated.getBankAccountEscrowed();// 托管账户

				// 设置子表属性
				dtlModel = new PaymentLaunchDtlModel();
				dtlModel.setProjectName(projectName);
				putFundModel(dtlModel, bankAccountEscrowed, tgpf_FundAppropriated);

				transferData.add(dtlModel);
			}

			afModel.setTransferData(transferData);
			// 传递参数
			jsonString = JSONObject.toJSONString(afModel);

			break;

		default:
			jsonString = "";
			break;
		}

		// 记录接口交互信息
		tgpf_SocketMsg = new Tgpf_SocketMsg();
		putSocketMsg(jsonString, tgpf_SocketMsg);
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		httpStringPostRequest = "";
		try {
			httpStringPostRequest = SocketUtil.getInstance().HttpStringPostRequest(url, jsonString);
		} catch (Exception e) {
			httpStringPostRequest = "error";
			log.error("exception-jsonString:" + jsonString);
		}

		tgpf_SocketMsg.setReturnCode(httpStringPostRequest);// 返回码
		tgpf_SocketMsgDao.update(tgpf_SocketMsg);

		// 对接下来的审批流进行结束处理
		/*
		 * Properties execute = endService.execute(workFlowId, baseForm);
		 * endHandleApproval(baseForm, execute);
		 */

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 迭代处理剩下的审批流程
	 * 
	 * @param baseForm
	 * @param execute
	 */
	private void endHandleApproval(BaseForm baseForm, Properties execute) {
		if (S_NormalFlag.success.equals(execute.get(S_NormalFlag.result)) && (null != execute.get("nextId"))) {
			execute = endService.execute((Long) execute.get("nextId"));
			endHandleApproval(baseForm, execute);
		}
	}

	/**
	 * 一般拨付交易明细
	 * 
	 * @param dtlModel
	 * @param bankAccountEscrowed
	 * @param tgpf_FundAppropriated
	 */
	private void putFundModel(PaymentLaunchDtlModel dtlModel, Tgxy_BankAccountEscrowed bankAccountEscrowed,
			Tgpf_FundAppropriated tgpf_FundAppropriated) {
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		
		dtlModel.setPayAccNo(bankAccountEscrowed.getTheAccount());// 付款银行账户账号(托管账号)
		dtlModel.setRecBankAccNo(tgpf_FundAppropriated.getBankAccountSupervised().getTheAccount());// 收款银行账户账号(监管账号)
		dtlModel.setReceiveBankCode("");// 收款人银行编码
		// dtlModel.setLedgerBankCode(bankAccountEscrowed.getBankBranch().getInterbankCode());//
		// 联行号(托管账号-联行号)
		dtlModel.setLedgerBankCode(null == tgpf_FundAppropriated.getBankAccountSupervised().getTheNameOfBank() ? ""
				: tgpf_FundAppropriated.getBankAccountSupervised().getTheNameOfBank());// 联行号(监管账号银行名称)
		dtlModel.setReceiveAccName(tgpf_FundAppropriated.getBankAccountSupervised().getTheName());// 收款人户名(监管账号名称)
		dtlModel.setAccType("public");// 账户类型对公对私 public，private
		dtlModel.setUse("06120302_"+dtlModel.getProjectName()+"_资金拨付");// 用途
		Double amount =  null == tgpf_FundAppropriated.getOverallPlanPayoutAmount() ? 0.00 : tgpf_FundAppropriated.getOverallPlanPayoutAmount();
		String format = nf.format(amount);
		dtlModel.setAmount(new BigDecimal(format));// 转账金额(统筹金额)
		dtlModel.setBizId(String.valueOf(tgpf_FundAppropriated.getTableId()));// 单据主键
		dtlModel.setRemark(dtlModel.getProjectName()+"_资金拨付");// 备注
		dtlModel.setCrashFlag("");// 是否加急
		dtlModel.setAreaCode("");// 地区码
	}

	/**
	 * 退房退款交易明细
	 * 
	 * @param dtlModel
	 * @param bankAccountEscrowed
	 * @param refundInfo
	 * @param busicode
	 */
	private void putRefundModel(PaymentLaunchDtlModel dtlModel, Tgxy_BankAccountEscrowed bankAccountEscrowed,
			Tgpf_RefundInfo refundInfo, String busicode) {
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		
		dtlModel.setPayAccNo(bankAccountEscrowed.getTheAccount());// 付款银行账户账号(托管账号)
		dtlModel.setRecBankAccNo(refundInfo.getReceiverBankAccount());// 收款银行账户账号(监管账号)
		dtlModel.setReceiveBankCode("");// 收款人银行编码
		// dtlModel.setLedgerBankCode(bankAccountEscrowed.getBankBranch().getInterbankCode());//
		// 联行号(托管账号-联行号)
		dtlModel.setLedgerBankCode(null == refundInfo.getReceiverBankName() ? "" : refundInfo.getReceiverBankName());// 联行号(收款银行)
		dtlModel.setReceiveAccName(refundInfo.getReceiverName());// 收款人户名(监管账号名称)
		//根据收款方名字长度判断是对公还是对私  长度小于等于4 对私  否则对公
		if(StringUtils.isNotBlank(refundInfo.getReceiverName()) && refundInfo.getReceiverName().length() <= 4){
			dtlModel.setAccType("private");// 账户类型对公对私 public，private
		}else{
			dtlModel.setAccType("public");// 账户类型对公对私 public，private
		}

		dtlModel.setUse(refundInfo.getTheNameOfProject()+"_退房退款");// 用途
		double amount = null == refundInfo.getRefundAmount() ? 0.00 : refundInfo.getRefundAmount();
		String format = nf.format(amount);
		dtlModel.setAmount(new BigDecimal(format));// 转账金额(本次退款申请金额)
		dtlModel.setBizId(refundInfo.getTableId()+"_"+busicode);// 单据主键 解决推送到银企平台主键冲突问题
		dtlModel.setRemark(refundInfo.getTheNameOfProject()+"_退房退款");// 备注
		dtlModel.setCrashFlag("");// 是否加急
		dtlModel.setAreaCode("");// 地区码
	}

	/**
	 * 记录接口报文记录
	 * 
	 * @param jsonString
	 * @param tgpf_SocketMsg
	 */
	private void putSocketMsg(String jsonString, Tgpf_SocketMsg tgpf_SocketMsg) {
		tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
		tgpf_SocketMsg.setMsgStatus(1);// 发送状态
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间

		tgpf_SocketMsg.setMsgDirection("ZT_TO_BANK");// 报文方向
		tgpf_SocketMsg.setMsgContentArchives(jsonString);// 报文内容
		tgpf_SocketMsg.setReturnCode("");// 返回码
	}

	/**
	 * 统一设置主属性
	 * 
	 * @param afModel
	 * @param orgCode
	 * @param secretKey
	 */
	private void putAf(PaymentLaunchModel afModel, String orgCode, String secretKey) {
		afModel.setOrgCode(orgCode);// 企业标识符
		afModel.setSecretKey(secretKey);// 操作密钥
		afModel.setIsNeedCheck(false);// 是否走审批流
		afModel.setOperator("");// 当前操作人
		afModel.setWorkflowKey("");// 流程编号
	}

	/**
	 * 特殊拨付设置子属性
	 * 
	 * @param af
	 * @param accType
	 * @param bankAccountEscrowed
	 * @param dtl
	 * @return
	 */
	private void putDtl(PaymentLaunchDtlModel dtlModel, Tgpf_SpecialFundAppropriated_AF af, String accType,
			Tgxy_BankAccountEscrowed bankAccountEscrowed, Tgpf_SpecialFundAppropriated_AFDtl dtl) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);

		dtlModel.setPayAccNo(bankAccountEscrowed.getTheAccount());// 付款银行账户账号(托管账号)
		dtlModel.setRecBankAccNo(af.getTheAccountOfBankAccount());// 收款银行账户账号(监管账号)
		dtlModel.setReceiveBankCode("");// 收款人银行编码
		// dtlModel.setLedgerBankCode(bankAccountEscrowed.getBankBranch().getInterbankCode());//
		// 联行号(托管账号-联行号)
		dtlModel.setLedgerBankCode(null == af.getTheBankOfBankAccount() ? "" : af.getTheBankOfBankAccount());// 联行号(收款方开户行名称)
		dtlModel.setReceiveAccName(null == af.getTheNameOfBankAccount() ? "" : af.getTheNameOfBankAccount());// 收款人户名(监管账号名称)
//		dtlModel.setAccType("1".equals(accType) ? "public" : "private");// 账户类型对公对私
																		// public，private

		//根据收款方名字长度判断是对公还是对私  长度小于等于4 对私  否则对公
		if(StringUtils.isNotBlank(af.getTheNameOfBankAccount()) && af.getTheNameOfBankAccount().length() <= 4){
			dtlModel.setAccType("private");// 账户类型对公对私 public，private
		}else{
			dtlModel.setAccType("public");// 账户类型对公对私 public，private
		}
//		dtlModel.setUse("061206");// 用途
		dtlModel.setUse("061206_"+af.getProject().getTheName() + "_特殊资金拨付");// 用途
		double amount = null == dtl.getAppliedAmount() ? 0.00 : dtl.getAppliedAmount();
		String format = nf.format(amount);
		dtlModel.setAmount(new BigDecimal(format));// 转账金额(本次拨付申请金额)
		dtlModel.setBizId(String.valueOf(dtl.getTableId()));// 单据主键
		dtlModel.setRemark(af.getProject().getTheName() + "_特殊资金拨付");// 备注
		dtlModel.setCrashFlag("");// 是否加急
		dtlModel.setAreaCode("");// 地区码

	}
}
